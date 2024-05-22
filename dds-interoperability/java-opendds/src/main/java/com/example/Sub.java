package com.example;

import java.io.File;

import org.omg.CORBA.StringSeqHolder;

import DDS.DATAREADER_QOS_DEFAULT;
import DDS.DATA_AVAILABLE_STATUS;
import DDS.DataReader;
import DDS.DataReaderQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQosHolder;
import DDS.PARTICIPANT_QOS_DEFAULT;
import DDS.SUBSCRIBER_QOS_DEFAULT;
import DDS.Subscriber;
import DDS.SubscriberQosHolder;
import DDS.TOPIC_QOS_DEFAULT;
import DDS.Topic;
import DDS.TopicQosHolder;
import DDS.XCDR_DATA_REPRESENTATION;
import OpenDDS.DCPS.NO_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;

public class Sub implements Runnable {

	
	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Topic topic;
	@SuppressWarnings("unused")
	private DataReader dataReader;
	private Subscriber subscriber;
	private DataReaderListenerImpl dataReaderListenerImpl;
	private boolean activate = false;
	
	// QoS
	private DomainParticipantQosHolder domainParticipantQosHolder;
	private TopicQosHolder topicQosHolder;
	private SubscriberQosHolder subscriberQosHolder;
	private DataReaderQosHolder dataReaderQosHolder;
	
	public static void main(String[] args) {
		Sub subscriber = new Sub();
		Thread thread = new Thread(subscriber);
		thread.start();
	}
	
	public Sub() {
		try {
			initQoSHolder();
			domainParticipantFactory = initConfiguration();
			domainParticipant = initDomainParticipant(domainParticipantFactory);
			String topicTypeName = initRegisterTopicType(domainParticipant);
			topic = initTopic(domainParticipant, "MyTopic", topicTypeName);
			subscriber = initSubscriber(domainParticipant, "MyTopicPartition");
			dataReaderListenerImpl = new DataReaderListenerImpl();
			dataReader = initDataReader(subscriber, topic, dataReaderListenerImpl);
			activate = true;
		} catch (NullPointerException e) {

		}
	}
	
	
	private DomainParticipantFactory initConfiguration() {
		DomainParticipantFactory dpf = null;

		File file = new File("rtps.ini");
		String[] args = new String[2];
		args[0] = "-DCPSConfigFile";
		args[1] = file.getPath();

		StringSeqHolder stringSeqHolder = new StringSeqHolder(args);
		dpf = TheParticipantFactory.WithArgs(stringSeqHolder);

		if (dpf == null) {
			throw new NullPointerException("domain_participant_factory");
		}

		domainParticipantQosHolder.value = PARTICIPANT_QOS_DEFAULT.get();
		dpf.get_default_participant_qos(domainParticipantQosHolder);
		dpf.set_default_participant_qos(domainParticipantQosHolder.value);

		return dpf;
	}


	private DomainParticipant initDomainParticipant(DomainParticipantFactory domainParticipantFactory) {
		DomainParticipant dp;

		dp = domainParticipantFactory.lookup_participant(0);
		if (dp == null) {
			dp = domainParticipantFactory.create_participant(0, domainParticipantQosHolder.value, null,
					NO_STATUS_MASK.value);
			if (dp == null) {
				throw new NullPointerException("domain_participant");
			}
		}

		return dp;
	}


	private String initRegisterTopicType(DomainParticipant domainParticipant) {
		MyTopicTypeSupport aTypeSupport = new MyTopicTypeSupportImpl();
		String typeName = aTypeSupport.get_type_name();
		aTypeSupport.register_type(domainParticipant, typeName);

		return typeName;
	}


	private Topic initTopic(DomainParticipant domainParticipant, String topicName, String topicTypeName) {
		Topic tp;
		topicQosHolder.value = TOPIC_QOS_DEFAULT.get();
		domainParticipant.get_default_topic_qos(topicQosHolder);
		domainParticipant.set_default_topic_qos(topicQosHolder.value);

		tp = domainParticipant.create_topic(topicName, topicTypeName, topicQosHolder.value, null, NO_STATUS_MASK.value);
		if (tp == null) {
			throw new NullPointerException("topic");
		}

		return tp;
	}


	private Subscriber initSubscriber(DomainParticipant domainParticipant, String partitionName) {
		Subscriber sb;
		
		subscriberQosHolder.value = SUBSCRIBER_QOS_DEFAULT.get();
		domainParticipant.get_default_subscriber_qos(subscriberQosHolder);
		subscriberQosHolder.value.partition.name = new String[1];
		subscriberQosHolder.value.partition.name[0] = partitionName;
		domainParticipant.set_default_subscriber_qos(subscriberQosHolder.value);
		
		sb = domainParticipant.create_subscriber(subscriberQosHolder.value, null, NO_STATUS_MASK.value);
		if(sb == null) {
			throw new NullPointerException("subscriber");
		}
		
		return sb;
	}


	private DataReader initDataReader(Subscriber subscriber, Topic topic, DataReaderListenerImpl dataReaderListenerImpl) {
		DataReader dr;
		
		dataReaderQosHolder.value = DATAREADER_QOS_DEFAULT.get();
		subscriber.get_default_datareader_qos(dataReaderQosHolder);
		subscriber.copy_from_topic_qos(dataReaderQosHolder, topicQosHolder.value);
		dataReaderQosHolder.value.representation.value = new short[1];
		dataReaderQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		
		subscriber.set_default_datareader_qos(dataReaderQosHolder.value);
		
		dr = subscriber.create_datareader(topic, dataReaderQosHolder.value, dataReaderListenerImpl, DATA_AVAILABLE_STATUS.value);
		
		if(dr == null) {
			throw new NullPointerException("data_reader");
		}
		
		return dr;
	}


	private void initQoSHolder() {
		domainParticipantQosHolder = new DomainParticipantQosHolder();
		topicQosHolder = new TopicQosHolder();
		subscriberQosHolder = new SubscriberQosHolder();
		dataReaderQosHolder = new DataReaderQosHolder();
	}
	
	@Override
	public void run() {
		System.out.println("-- SUBSCRIBER [opendds] --");
		while(activate) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
