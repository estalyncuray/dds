package com.example;

import java.util.stream.IntStream;

import com.example.dds.a.A;
import com.example.dds.a.ADataWriter;
import com.example.dds.a.ATypeSupport;

import DDS.DATA_AVAILABLE_STATUS;
import DDS.DataWriter;
import DDS.DataWriterQos;
import DDS.DataWriterQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQos;
import DDS.DomainParticipantQosHolder;
import DDS.Publisher;
import DDS.PublisherQos;
import DDS.PublisherQosHolder;
import DDS.RETCODE_OK;
import DDS.STATUS_MASK_NONE;
import DDS.Topic;
import DDS.TopicQos;
import DDS.TopicQosHolder;

public class Pub implements Runnable {

	private static final String TOPIC_NAME = "A";
	private static final int MAX_MSG = 150;
	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Publisher publisher;
	private Topic topic;
	private DataWriter dataWriter;
	private ADataWriter writerHelper;
	private boolean active = false;
	
	// QoS
	private DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
	private TopicQosHolder topicQosHolder = new TopicQosHolder();
	private PublisherQosHolder publisherQosHolder = new PublisherQosHolder();
	private DataWriterQosHolder dataWriterQosHolder = new DataWriterQosHolder();
	
	public static void main(String[] args) {
		Pub publisher = new Pub();
		Thread thread = new Thread(publisher);
		thread.start();
	}

	public Pub() {
		
		// CREATE PARTICIPANT
			domainParticipantFactory = DomainParticipantFactory.get_instance();
			
			//domainParticipantQosHolder.value = PARTICIPANT_QOS_DEFAULT.value;
			domainParticipantQosHolder.value = new DomainParticipantQos();
			domainParticipantFactory.get_default_participant_qos(domainParticipantQosHolder);
			domainParticipantFactory.set_default_participant_qos(domainParticipantQosHolder.value);
			domainParticipant = domainParticipantFactory.create_participant(0, domainParticipantQosHolder.value, null, STATUS_MASK_NONE.value);
			if(domainParticipant == null)
			{
				System.err.println("domain_participant");
				return;
			}
			
		// REGISTER TOPIC
			ATypeSupport aTypeSupport = new ATypeSupport();
			String typeName = aTypeSupport.get_type_name();
			aTypeSupport.register_type(domainParticipant, typeName);
			
		// CREATE TOPIC
			//topicQosHolder.value = TOPIC_QOS_DEFAULT.value;
			topicQosHolder.value = new TopicQos();
			domainParticipant.get_default_topic_qos(topicQosHolder);
			domainParticipant.set_default_topic_qos(topicQosHolder.value);
			topic = domainParticipant.create_topic(TOPIC_NAME, typeName, topicQosHolder.value, null, STATUS_MASK_NONE.value);
			if(topic == null) {
				System.err.println("topic");
				return;
			}
		//CREATE PUBLISHER
			//subscriberQosHolder.value = SUBSCRIBER_QOS_DEFAULT.value;
			
			publisherQosHolder.value = new PublisherQos();
			domainParticipant.get_default_publisher_qos(publisherQosHolder);
			publisherQosHolder.value.partition.name = new String[1];
			publisherQosHolder.value.partition.name[0] = "example";
			domainParticipant.set_default_publisher_qos(publisherQosHolder.value);
			publisher = domainParticipant.create_publisher(publisherQosHolder.value, null, STATUS_MASK_NONE.value);
			if(publisher == null) {
				System.err.println("subscriber");
				return;
			}

		// CREATE DATA READER
			//dataReaderQosHolder.value = DATAREADER_QOS_DEFAULT.value;
			dataWriterQosHolder.value = new DataWriterQos();
			publisher.get_default_datawriter_qos(dataWriterQosHolder);
			publisher.copy_from_topic_qos(dataWriterQosHolder, topicQosHolder.value);
			publisher.set_default_datawriter_qos(dataWriterQosHolder.value);
			dataWriter = publisher.create_datawriter(topic, dataWriterQosHolder.value, null, DATA_AVAILABLE_STATUS.value);
			if(dataWriter == null) {
				System.err.println("data_reader");
			}
			writerHelper = ((ADataWriter) dataWriter);
			active = true;
	}
	
	

	@Override
	public void run() {
		if(active) {
			IntStream.range(0, MAX_MSG).forEach(i -> {
				sendData(i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});	
		}		
	}
	
	private void sendData(int i) {
		
		A a = new A("id "+ i, "name " + i);
		
		long handle = writerHelper.register_instance(a);
		int status = writerHelper.write(a, handle);
		if(status == RETCODE_OK.value) {
			System.out.println("Data " + i);
		}
	}
	

}
