package com.example;

import java.util.stream.IntStream;

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DATA_AVAILABLE_STATUS;
import DDS.DataReader;
import DDS.DataReaderListener;
import DDS.DataReaderQos;
import DDS.DataReaderQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQos;
import DDS.DomainParticipantQosHolder;
import DDS.LENGTH_UNLIMITED;
import DDS.LIVELINESS_CHANGED_STATUS;
import DDS.LivelinessChangedStatus;
import DDS.NOT_READ_SAMPLE_STATE;
import DDS.RequestedDeadlineMissedStatus;
import DDS.RequestedIncompatibleQosStatus;
import DDS.STATUS_MASK_ANY_V1_2;
import DDS.STATUS_MASK_NONE;
import DDS.SampleInfoSeqHolder;
import DDS.SampleLostStatus;
import DDS.SampleRejectedStatus;
import DDS.Subscriber;
import DDS.SubscriberQos;
import DDS.SubscriberQosHolder;
import DDS.SubscriptionMatchedStatus;
import DDS.Topic;
import DDS.TopicQos;
import DDS.TopicQosHolder;

public class Sub implements Runnable, DataReaderListener {

	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Subscriber subscriber;
	private Topic topic;
	private DataReader dataReader;
	private boolean active = false;
	
	// QoS
	private DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
	private TopicQosHolder topicQosHolder = new TopicQosHolder();
	private SubscriberQosHolder subscriberQosHolder = new SubscriberQosHolder();
	private DataReaderQosHolder dataReaderQosHolder = new DataReaderQosHolder();
	
	public static void main(String[] args) {
		Sub subscriber = new Sub();
		Thread thread = new Thread(subscriber);
		thread.start();
	}

	public Sub() {
		
		// CREATE PARTICIPANT
			domainParticipantFactory = DomainParticipantFactory.get_instance();
			
			//domainParticipantQosHolder.value = PARTICIPANT_QOS_DEFAULT.value;
			domainParticipantQosHolder.value = new DomainParticipantQos();
			domainParticipantFactory.get_default_participant_qos(domainParticipantQosHolder);
			domainParticipantFactory.set_default_participant_qos(domainParticipantQosHolder.value);
			domainParticipant = domainParticipantFactory.lookup_participant(0);
			if(domainParticipant == null)
			{
				domainParticipant = domainParticipantFactory.create_participant(0, domainParticipantQosHolder.value, null, STATUS_MASK_NONE.value);
				System.out.println("created domain_participant");
			}else {
				System.out.println("lookup_participant");
			}
			
			
		// REGISTER TOPIC
			MyTopicTypeSupport aTypeSupport = new MyTopicTypeSupport();
			String typeName = aTypeSupport.get_type_name();
			aTypeSupport.register_type(domainParticipant, typeName);
			
		// CREATE TOPIC
			//topicQosHolder.value = TOPIC_QOS_DEFAULT.value;
			topicQosHolder.value = new TopicQos();
			domainParticipant.get_default_topic_qos(topicQosHolder);
			// start topic qos
			// end topic qos
			domainParticipant.set_default_topic_qos(topicQosHolder.value);
			topic = domainParticipant.create_topic("MyTopic", typeName, topicQosHolder.value, null, STATUS_MASK_NONE.value);
			if(topic == null) {
				System.err.println("topic");
				return;
			}

		//CREATE SUBSCRIBER
			//subscriberQosHolder.value = SUBSCRIBER_QOS_DEFAULT.value;
			subscriberQosHolder.value = new SubscriberQos();
			domainParticipant.get_default_subscriber_qos(subscriberQosHolder);
			// start subscriber qos
			subscriberQosHolder.value.partition.name = new String[1];
			subscriberQosHolder.value.partition.name[0] = "estalyn";
			// end subscriber qos
			domainParticipant.set_default_subscriber_qos(subscriberQosHolder.value);
			subscriber = domainParticipant.create_subscriber(subscriberQosHolder.value, null, STATUS_MASK_NONE.value);
			if(subscriber == null) {
				System.err.println("subscriber");
				return;
			}

		// CREATE DATA READER
			//dataReaderQosHolder.value = DATAREADER_QOS_DEFAULT.value;
			dataReaderQosHolder.value = new DataReaderQos();
			subscriber.get_default_datareader_qos(dataReaderQosHolder);
			subscriber.copy_from_topic_qos(dataReaderQosHolder, topicQosHolder.value);
			// start data reader qos
			// end data reader qos
			subscriber.set_default_datareader_qos(dataReaderQosHolder.value);
			dataReader = subscriber.create_datareader(topic, dataReaderQosHolder.value, null, STATUS_MASK_NONE.value);
			dataReader.set_listener(this, DATA_AVAILABLE_STATUS.value);
			if(dataReader == null) {
				System.err.println("data_reader");
			}
			active = true;
	}	
	
	@Override
	public void run() {
		while (active) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// DATAREADER IMPLEMENTATION
	
	@Override
	public void on_data_available(DataReader reader) {
		MyTopicSeqHolder aSeqHolder = new MyTopicSeqHolder();
		SampleInfoSeqHolder sampleInfoSeqHolder = new SampleInfoSeqHolder();
		// BE AWARE OF INVALID SAMPLES, READ ONLY FRESH DATA
		((MyTopicDataReader) dataReader).read(aSeqHolder, sampleInfoSeqHolder, LENGTH_UNLIMITED.value, NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		
		MyTopic[] data = aSeqHolder.value;
		
		IntStream.range(0, data.length)
		.filter(i -> sampleInfoSeqHolder.value[i].valid_data)
		.mapToObj(i -> data[i])
		.forEach(validData -> {
			System.out.println("id: " + validData.id + " name : " + validData.name);
		});
		
		((MyTopicDataReader) dataReader).return_loan(aSeqHolder, sampleInfoSeqHolder);
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
	
	
	
}
