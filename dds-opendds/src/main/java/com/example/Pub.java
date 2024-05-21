package com.example;

import java.io.File;

import org.omg.CORBA.StringSeqHolder;

import DDS.DATAWRITER_QOS_DEFAULT;
import DDS.DataWriter;
import DDS.DataWriterQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQosHolder;
import DDS.DurabilityQosPolicyKind;
import DDS.HANDLE_NIL;
import DDS.LivelinessQosPolicyKind;
import DDS.OwnershipQosPolicyKind;
import DDS.PARTICIPANT_QOS_DEFAULT;
import DDS.PUBLISHER_QOS_DEFAULT;
import DDS.Publisher;
import DDS.PublisherQosHolder;
import DDS.RETCODE_OK;
import DDS.ReliabilityQosPolicyKind;
import DDS.TOPIC_QOS_DEFAULT;
import DDS.Topic;
import DDS.TopicQosHolder;
import DDS.XCDR_DATA_REPRESENTATION;
import OpenDDS.DCPS.NO_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;

public class Pub implements Runnable {

	private static final int MAX_MSG = 150;
	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Topic topic;
	private DataWriter dataWriter;
	private Publisher publisher;
	private MyTopicDataWriter writerHelper;
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
			return;
		}
		// CREATE PUBLISHER
		publisherQosHolder.value = PUBLISHER_QOS_DEFAULT.get();
		domainParticipant.get_default_publisher_qos(publisherQosHolder);
		// start publisher qos
		publisherQosHolder.value.partition.name = new String[1];
		publisherQosHolder.value.partition.name[0] = "estalyn";
		// end publisher qos
		domainParticipant.set_default_publisher_qos(publisherQosHolder.value);
		publisher = domainParticipant.create_publisher(publisherQosHolder.value, null, NO_STATUS_MASK.value);
		if (publisher == null) {
			System.err.println("publisher");
			return;
		}

		// CREATE DATA WRITER
		dataWriterQosHolder.value = DATAWRITER_QOS_DEFAULT.get();
		publisher.get_default_datawriter_qos(dataWriterQosHolder);
		publisher.copy_from_topic_qos(dataWriterQosHolder, topicQosHolder.value);
		
		dataWriterQosHolder.value.representation.value = new short[1];
		dataWriterQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		// end dataWriter Qos
		publisher.set_default_datawriter_qos(dataWriterQosHolder.value);
		dataWriter = publisher.create_datawriter(topic, dataWriterQosHolder.value, null, NO_STATUS_MASK.value);
		if (dataWriter == null) {
			System.err.println("data_writer");
			return;
		}

		active = true;
	}

	@Override
	public final void run() {
		if(active) {
			
			for (int i = 0; i < MAX_MSG; i++) {
				MyTopic data = new MyTopic();
				data.id = String.valueOf(i);
				data.name = "prueba";
				sendData(data);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	public final void sendData(MyTopic data) {
		writerHelper = MyTopicDataWriterHelper.narrow(dataWriter);
		//int handle = writerHelper.register_instance(a);
		int status = writerHelper.write(data, HANDLE_NIL.value);
		if(status == RETCODE_OK.value) {
			System.out.println("A -> " + data.id + " " + data.name);
		}
	}

}
