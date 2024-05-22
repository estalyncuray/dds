package com.example;

import DDS.DATA_AVAILABLE_STATUS;
import DDS.DataReader;
import DDS.DataReaderQos;
import DDS.DataReaderQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQos;
import DDS.DomainParticipantQosHolder;
import DDS.STATUS_MASK_NONE;
import DDS.Subscriber;
import DDS.SubscriberQos;
import DDS.SubscriberQosHolder;
import DDS.Topic;
import DDS.TopicQos;
import DDS.TopicQosHolder;

public class Sub implements Runnable {

	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Topic topic;
	private Subscriber subscriber;
	private DataReaderListenerImpl dataReaderListenerImpl;
	
	@SuppressWarnings("unused")
	private DataReader dataReader;

	// QoS
	DomainParticipantQosHolder domainParticipantQosHolder;
	TopicQosHolder topicQosHolder;
	SubscriberQosHolder subscriberQosHolder;
	DataReaderQosHolder dataReaderQosHolder;

	private boolean activate = false;
	
	public static void main(String[] args) {
		Sub subscriber = new Sub();
		Thread thread = new Thread(subscriber);
		thread.start();
	}

	public Sub() {
		try {
			initQoS();
			domainParticipantFactory = initConfiguration();
			domainParticipant = initDomainParticipant(domainParticipantFactory);
			String topicTypeName = initRegisterTopicType(domainParticipant);
			topic = initTopic(domainParticipant, "MyTopic", topicTypeName);
			subscriber = initSubscriber(domainParticipant, "MyTopicPartition");
			dataReaderListenerImpl = new DataReaderListenerImpl();
			dataReader = initDataReader(subscriber, topic, dataReaderListenerImpl);
			activate = true;
		} catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
		}
	}
	
	private void initQoS() {
		domainParticipantQosHolder = new DomainParticipantQosHolder();
		topicQosHolder = new TopicQosHolder();
		subscriberQosHolder = new SubscriberQosHolder();
		dataReaderQosHolder = new DataReaderQosHolder();
	}
	
	private DomainParticipantFactory initConfiguration() {
		DomainParticipantFactory dpf = DomainParticipantFactory.get_instance();
		if(dpf == null) {
			throw new NullPointerException("domain_participant_factory");
		}
		domainParticipantQosHolder.value = new DomainParticipantQos();
		dpf.get_default_participant_qos(domainParticipantQosHolder);
		dpf.set_default_participant_qos(domainParticipantQosHolder.value);
		return dpf;
	}
	
	private DomainParticipant initDomainParticipant(DomainParticipantFactory domainParticipantFactory) {
		DomainParticipant dp;
		
		dp = domainParticipantFactory.lookup_participant(0);
		if(dp == null) {
			dp = domainParticipantFactory.create_participant(0, domainParticipantQosHolder.value, null, STATUS_MASK_NONE.value);
			if(dp == null) {
				throw new NullPointerException("domain_participant");
			}
		}
		return dp;
	}
	
	private String initRegisterTopicType(DomainParticipant domainParticipant) {
		MyTopicTypeSupport ts = new MyTopicTypeSupport();
		String topicTypeName = ts.get_type_name();
		ts.register_type(domainParticipant, topicTypeName);
		return topicTypeName;
	}
	
	private Topic initTopic(DomainParticipant domainParticipant, String topicName, String topicTypeName) {
		Topic tp;
		
		topicQosHolder.value = new TopicQos();
		domainParticipant.get_default_topic_qos(topicQosHolder);
		domainParticipant.set_default_topic_qos(topicQosHolder.value);
		tp = domainParticipant.create_topic(topicName, topicTypeName, topicQosHolder.value, null, STATUS_MASK_NONE.value);
		if(tp == null) {
			throw new NullPointerException("topic");
		}
		return tp;
	}
	
	private Subscriber initSubscriber(DomainParticipant domainParticipant, String partitionName) {
		Subscriber sb;
		
		subscriberQosHolder.value = new SubscriberQos();
		domainParticipant.get_default_subscriber_qos(subscriberQosHolder);
		subscriberQosHolder.value.partition.name = new String[1];
		subscriberQosHolder.value.partition.name[0] = partitionName;
		domainParticipant.set_default_subscriber_qos(subscriberQosHolder.value);
		
		sb = domainParticipant.create_subscriber(subscriberQosHolder.value, null, STATUS_MASK_NONE.value);
		if(sb == null) {
			throw new NullPointerException("subscriber");
		}
		return sb;
	}
	
	private DataReader initDataReader(Subscriber subscriber, Topic topic, DataReaderListenerImpl dataReaderListenerImpl) {
		DataReader dr;
		
		dataReaderQosHolder.value = new DataReaderQos();
		subscriber.get_default_datareader_qos(dataReaderQosHolder);
		subscriber.copy_from_topic_qos(dataReaderQosHolder, topicQosHolder.value);
		subscriber.set_default_datareader_qos(dataReaderQosHolder.value);
		
		dr = subscriber.create_datareader(topic, dataReaderQosHolder.value, dataReaderListenerImpl, DATA_AVAILABLE_STATUS.value);
		if(dr == null) {
			throw new NullPointerException("dataReader");
		}
		return dr;
	}
	
	@Override
	public void run() {
		System.out.println("-- SUBSCRIBER [opensplice]--");
		while(activate) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
