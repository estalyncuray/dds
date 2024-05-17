package com.example;

import java.io.File;
import java.util.stream.IntStream;

import org.omg.CORBA.StringSeqHolder;

import com.example.dds.a.A;
import com.example.dds.a.ADataWriter;
import com.example.dds.a.ADataWriterHelper;
import com.example.dds.a.ATypeSupport;
import com.example.dds.a.ATypeSupportImpl;

import DDS.DATAWRITER_QOS_DEFAULT;
import DDS.DataRepresentationIdSeqHolder;
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
import DDS.ReliabilityQosPolicyKind;
import DDS.TOPIC_QOS_DEFAULT;
import DDS.Topic;
import DDS.TopicQosHolder;
import DDS.XCDR_DATA_REPRESENTATION;
import OpenDDS.DCPS.DEFAULT_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;

public class Pub implements Runnable {

	private static final int MAX_MSG = 150;
	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Topic topic;
	private DataWriter dataWriter;
	private Publisher publisher;
	private ADataWriter writerHelper;
	private boolean active = false;

	// QoS
	private DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
	private TopicQosHolder topicQosHolder = new TopicQosHolder();
	private PublisherQosHolder publisherQosHolder = new PublisherQosHolder();
	private DataWriterQosHolder dataWriterQosHolder = new DataWriterQosHolder();
	private DataRepresentationIdSeqHolder dataRepresentationIdSeqHolder = new DataRepresentationIdSeqHolder();

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
			domainParticipant = domainParticipantFactory.create_participant(0, domainParticipantQosHolder.value, null, DEFAULT_STATUS_MASK.value);	
			System.out.println("created domain_participant");
		}else {
			System.out.println("lookup_participant");
		}

		// REGISTER TOPIC
		ATypeSupport aTypeSupport = new ATypeSupportImpl();
		String typeName = aTypeSupport.get_type_name();
		aTypeSupport.register_type(domainParticipant, typeName);

		// CREATE TOPIC
		topicQosHolder.value = TOPIC_QOS_DEFAULT.get();
		domainParticipant.get_default_topic_qos(topicQosHolder);
		// start topic qos
		topicQosHolder.value.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
		topicQosHolder.value.durability.kind = DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
		// topicQos.value.ownership.kind =
		// OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS;
		topicQosHolder.value.ownership.kind = OwnershipQosPolicyKind.SHARED_OWNERSHIP_QOS;
		topicQosHolder.value.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
		topicQosHolder.value.liveliness.lease_duration.sec = 0;
		topicQosHolder.value.liveliness.lease_duration.nanosec =1;
		topicQosHolder.value.representation.value = new short[1];
		topicQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		// end topic qos
		domainParticipant.set_default_topic_qos(topicQosHolder.value);
		topic = domainParticipant.create_topic("A", typeName, topicQosHolder.value, null, DEFAULT_STATUS_MASK.value);
		if (topic == null) {
			System.err.println("topic");
			return;
		}
		// CREATE PUBLISHER
		publisherQosHolder.value = PUBLISHER_QOS_DEFAULT.get();
		domainParticipant.get_default_publisher_qos(publisherQosHolder);
		// start publisher qos
		publisherQosHolder.value.partition.name = new String[1];
		publisherQosHolder.value.partition.name[0] = "example";
		// end publisher qos
		domainParticipant.set_default_publisher_qos(publisherQosHolder.value);
		publisher = domainParticipant.create_publisher(publisherQosHolder.value, null, DEFAULT_STATUS_MASK.value);
		if (publisher == null) {
			System.err.println("publisher");
			return;
		}

		// CREATE DATA WRITER
		dataWriterQosHolder.value = DATAWRITER_QOS_DEFAULT.get();
		publisher.get_default_datawriter_qos(dataWriterQosHolder);
		publisher.copy_from_topic_qos(dataWriterQosHolder, topicQosHolder.value);
		// start dataWriter Qos
		dataWriterQosHolder.value.writer_data_lifecycle.autodispose_unregistered_instances = true;
		// WQosH.value.ownership.kind =
		// OwnershipQosPolicyKind.EXCLUSIVE_OWNERSHIP_QOS;
		// WQosH.value.ownership_strength.value = strength;

		dataWriterQosHolder.value.durability.kind = DurabilityQosPolicyKind.VOLATILE_DURABILITY_QOS;
		dataWriterQosHolder.value.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
		dataWriterQosHolder.value.liveliness.kind = LivelinessQosPolicyKind.AUTOMATIC_LIVELINESS_QOS;
		dataWriterQosHolder.value.liveliness.lease_duration.sec = 0;
		dataWriterQosHolder.value.liveliness.lease_duration.nanosec = 1;
		dataWriterQosHolder.value.representation.value = new short[1];
		dataWriterQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		// end dataWriter Qos
		publisher.set_default_datawriter_qos(dataWriterQosHolder.value);
		dataWriter = publisher.create_datawriter(topic, dataWriterQosHolder.value, null, DEFAULT_STATUS_MASK.value);
		if (dataWriter == null) {
			System.err.println("data_writer");
			return;
		}
		writerHelper = ADataWriterHelper.narrow(dataWriter);
		active = true;
	}

	@Override
	public final void run() {
		if(active) {
			IntStream.range(0, MAX_MSG).forEach(i -> {
				
				try {
					Thread.sleep(1000);
					sendData(i);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});	
		}		
	}
	
	private final void sendData(int i) {
		
		A a = new A("id "+ i, "name " + i);
		
		int handle = writerHelper.register_instance(a);
		int status = writerHelper.write(a, handle);
	}

}
