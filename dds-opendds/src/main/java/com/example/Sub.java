package com.example;

import java.io.File;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;
import org.omg.CORBA.StringSeqHolder;

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DATAREADER_QOS_DEFAULT;
import DDS.DATA_AVAILABLE_STATUS;
import DDS.DataReader;
import DDS.DataReaderListener;
import DDS.DataReaderQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQosHolder;
import DDS.DurabilityQosPolicyKind;
import DDS.HistoryQosPolicyKind;
import DDS.LIVELINESS_CHANGED_STATUS;
import DDS.LivelinessChangedStatus;
import DDS.LivelinessQosPolicyKind;
import DDS.NOT_READ_SAMPLE_STATE;
import DDS.OwnershipQosPolicyKind;
import DDS.PARTICIPANT_QOS_DEFAULT;
import DDS.RETCODE_OK;
import DDS.ReliabilityQosPolicyKind;
import DDS.RequestedDeadlineMissedStatus;
import DDS.RequestedIncompatibleQosStatus;
import DDS.SUBSCRIBER_QOS_DEFAULT;
import DDS.SampleInfo;
import DDS.SampleInfoHolder;
import DDS.SampleLostStatus;
import DDS.SampleRejectedStatus;
import DDS.Subscriber;
import DDS.SubscriberQosHolder;
import DDS.SubscriptionMatchedStatus;
import DDS.TOPIC_QOS_DEFAULT;
import DDS.Topic;
import DDS.TopicQosHolder;
import DDS.XCDR2_DATA_REPRESENTATION;
import DDS.XCDR_DATA_REPRESENTATION;
import OpenDDS.DCPS.ALL_STATUS_MASK;
import OpenDDS.DCPS.NO_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;

public class Sub implements Runnable, DataReaderListener {

	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Topic topic;
	private DataReader dataReader;
	private Subscriber subscriber;
	private boolean active = false;

	// QoS
	private DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
	private TopicQosHolder topicQosHolder = new TopicQosHolder();
	private SubscriberQosHolder subscriberQosHolder = new SubscriberQosHolder();
	private DataReaderQosHolder dataReaderQosHolder = new DataReaderQosHolder();

	public static void main(String[] args) {
		Sub publisher = new Sub();
		Thread thread = new Thread(publisher);
		thread.start();
	}

	public Sub() {
		
		// CREATE PARTICIPANT
		File file = new File("rtsp.ini");
		String[] args = new String[2];
		args[0] = "-DCPSConfigFile";
		args[1] = file.getPath();

		StringSeqHolder stringSeqHolder = new StringSeqHolder(args);
		domainParticipantFactory = TheParticipantFactory.WithArgs(stringSeqHolder);
		domainParticipantFactory.get_instance();
		domainParticipantQosHolder.value = PARTICIPANT_QOS_DEFAULT.get();
		domainParticipantFactory.get_default_participant_qos(domainParticipantQosHolder);
		domainParticipantFactory.set_default_participant_qos(domainParticipantQosHolder.value);
		domainParticipant = domainParticipantFactory.lookup_participant(0);
		if (domainParticipant == null) {
			domainParticipant = domainParticipantFactory.create_participant(0, domainParticipantQosHolder.value, null, NO_STATUS_MASK.value);
			System.out.println("created domain_participant");
		}else {
			System.out.println("lookup_participant");
		}

		// REGISTER TOPIC
		MyTopicTypeSupport aTypeSupport = new MyTopicTypeSupportImpl();
		String typeName = aTypeSupport.get_type_name();
		aTypeSupport.register_type(domainParticipant, typeName);

		// CREATE TOPIC
		topicQosHolder.value = TOPIC_QOS_DEFAULT.get();
		domainParticipant.get_default_topic_qos(topicQosHolder);
		// start topic qos
		
		topicQosHolder.value.representation.value = new short[1];
		topicQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		// end topic qos
		domainParticipant.set_default_topic_qos(topicQosHolder.value);
		
		topic = domainParticipant.create_topic("MyTopic", typeName, topicQosHolder.value, null, NO_STATUS_MASK.value);
		if (topic == null) {
			System.err.println("topic");
		}
		// CREATE SUBSCRIBER
		subscriberQosHolder.value = SUBSCRIBER_QOS_DEFAULT.get();
		domainParticipant.get_default_subscriber_qos(subscriberQosHolder);
		// start subscriber qos
		subscriberQosHolder.value.partition.name = new String[1];
		subscriberQosHolder.value.partition.name[0] = "estalyn";
		// end subscriber qos
		domainParticipant.set_default_subscriber_qos(subscriberQosHolder.value);
		subscriber = domainParticipant.create_subscriber(subscriberQosHolder.value, null, NO_STATUS_MASK.value);
		if (subscriber == null) {
			System.err.println("subscriber");
		}

		// CREATE DATA READER
		dataReaderQosHolder.value = DATAREADER_QOS_DEFAULT.get();
		subscriber.get_default_datareader_qos(dataReaderQosHolder);
		subscriber.copy_from_topic_qos(dataReaderQosHolder, topicQosHolder.value);
		// start dataReader qos
		
		dataReaderQosHolder.value.representation.value = new short[1];
		dataReaderQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		
		// end dataReader qos
		subscriber.set_default_datareader_qos(dataReaderQosHolder.value);
		dataReader = subscriber.create_datareader(topic, dataReaderQosHolder.value, null, NO_STATUS_MASK.value);
		dataReader.set_listener(this, DATA_AVAILABLE_STATUS.value | LIVELINESS_CHANGED_STATUS.value);
		if (dataReader == null) {
			System.err.println("data_writer");
		}
		active = true;
	}

	@Override
	public void run() {
		while(active) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void on_data_available(DataReader arg0) {
		MyTopicDataReader readerHelper = MyTopicDataReaderHelper.narrow(arg0);
		MyTopic a = new MyTopic();
		a.id = "";
		a.name = "";
		
		MyTopicHolder mh = new MyTopicHolder(a);
		SampleInfo si = new SampleInfo(NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value,
				new DDS.Time_t(), 0, 0, 0, 0, 0, 0, 0, false, 0L);
		SampleInfoHolder sih = new SampleInfoHolder(si);
		int status = readerHelper.read_next_sample(mh, sih);
		
		if(status == RETCODE_OK.value) {
			if(sih.value.valid_data) {
				MyTopic validData = mh.value;
				System.out.println("id: " + validData.id + " name : " + validData.name);
			}
		}
	}

	@Override
	public void on_liveliness_changed(DataReader arg0, LivelinessChangedStatus arg1) {
		// TODO Auto-generated method stub
		System.out.println("on_liveliness_changed");
	}

	@Override
	public void on_requested_deadline_missed(DataReader arg0, RequestedDeadlineMissedStatus arg1) {
		// TODO Auto-generated method stub
		System.out.println("on_requested_deadline_missed");
	}

	@Override
	public void on_requested_incompatible_qos(DataReader arg0, RequestedIncompatibleQosStatus arg1) {
		// TODO Auto-generated method stub
		System.out.println("on_requested_incompatible_qos");
	}

	@Override
	public void on_sample_lost(DataReader arg0, SampleLostStatus arg1) {
		// TODO Auto-generated method stub
		System.out.println("on_sample_lost");
	}

	@Override
	public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1) {
		// TODO Auto-generated method stub
		System.out.println("on_sample_rejected");
	}

	@Override
	public void on_subscription_matched(DataReader arg0, SubscriptionMatchedStatus arg1) {
		// TODO Auto-generated method stub
		System.out.println("on_subscription_matched");
	}

	@Override
	public Request _create_request(Context arg0, String arg1, NVList arg2, NamedValue arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Request _create_request(Context arg0, String arg1, NVList arg2, NamedValue arg3, ExceptionList arg4,
			ContextList arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainManager[] _get_domain_managers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _get_interface_def() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy _get_policy(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int _hash(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean _is_a(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean _is_equivalent(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean _non_existent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void _release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Request _request(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _set_policy_override(Policy[] arg0, SetOverrideType arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
