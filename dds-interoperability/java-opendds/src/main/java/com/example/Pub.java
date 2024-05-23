package com.example;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.omg.CORBA.StringSeqHolder;

import DDS.DATAWRITER_QOS_DEFAULT;
import DDS.DataWriter;
import DDS.DataWriterQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQosHolder;
import DDS.HANDLE_NIL;
import DDS.PARTICIPANT_QOS_DEFAULT;
import DDS.PUBLISHER_QOS_DEFAULT;
import DDS.Publisher;
import DDS.PublisherQosHolder;
import DDS.RETCODE_OK;
import DDS.TOPIC_QOS_DEFAULT;
import DDS.Topic;
import DDS.TopicQosHolder;
import DDS.XCDR_DATA_REPRESENTATION;
import OpenDDS.DCPS.NO_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;

public class Pub implements Runnable {

	private boolean activate = false;
	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Topic topic;
	private DataWriter dataWriter;
	private Publisher publisher;
	@SuppressWarnings("unused")
	private boolean active = false;

	// QoS
	private DomainParticipantQosHolder domainParticipantQosHolder;
	private TopicQosHolder topicQosHolder;
	private PublisherQosHolder publisherQosHolder;
	private DataWriterQosHolder dataWriterQosHolder;

	public static void main(String[] args) {
		Pub publisher = new Pub();
		Thread thread = new Thread(publisher);
		thread.start();
	}

	public Pub() {
		try {
			initQoSHolder();
			domainParticipantFactory = initConfiguration();
			domainParticipant = initDomainParticipant(domainParticipantFactory);
			String topicTypeName = initRegisterTopicType(domainParticipant, "com.example.MyTopicTypeSupportImpl");
			topic = initTopic(domainParticipant, "MyTopic", topicTypeName);
			publisher = initPublisher(domainParticipant, "MyTopicPartition");
			dataWriter = initDataWriter(publisher, topic);
			activate = true;
		} catch (NullPointerException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {

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

	private String initRegisterTopicType(DomainParticipant domainParticipant, String typeSupportClassName) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Class<?> typeSupportClass = Class.forName(typeSupportClassName);
		Object typeSupport = null;
		String typeName = null;

        Method registerType = typeSupportClass.getMethod("register_type", DomainParticipant.class, String.class);
		typeSupport = typeSupportClass.newInstance();
        Method get_type_name = typeSupportClass.getMethod("get_type_name");
        typeName = (String) get_type_name.invoke(typeSupport);
        registerType.invoke(typeSupport,domainParticipant,typeName);

        return typeName;
	}

	private Topic initTopic(DomainParticipant domainParticipant, String topicName, String topicTypeName) {
		Topic tp;
		topicQosHolder.value = TOPIC_QOS_DEFAULT.get();
		domainParticipant.get_default_topic_qos(topicQosHolder);
		topicQosHolder.value.representation.value = new short[1];
		topicQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		domainParticipant.set_default_topic_qos(topicQosHolder.value);

		tp = domainParticipant.create_topic(topicName, topicTypeName, topicQosHolder.value, null, NO_STATUS_MASK.value);
		if (tp == null) {
			throw new NullPointerException("topic");
		}
		return tp;
	}

	private Publisher initPublisher(DomainParticipant domainParticipant, String partitionName) {
		Publisher pb;

		publisherQosHolder.value = PUBLISHER_QOS_DEFAULT.get();
		domainParticipant.get_default_publisher_qos(publisherQosHolder);
		publisherQosHolder.value.partition.name = new String[1];
		publisherQosHolder.value.partition.name[0] = partitionName;
		domainParticipant.set_default_publisher_qos(publisherQosHolder.value);

		pb = domainParticipant.create_publisher(publisherQosHolder.value, null, NO_STATUS_MASK.value);
		if (pb == null) {
			throw new NullPointerException("publisher");
		}

		return pb;
	}

	private DataWriter initDataWriter(Publisher publisher, Topic topic) {
		DataWriter dw;

		dataWriterQosHolder.value = DATAWRITER_QOS_DEFAULT.get();
		publisher.get_default_datawriter_qos(dataWriterQosHolder);
		dataWriterQosHolder.value.representation.value = new short[1];
		dataWriterQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
		publisher.set_default_datawriter_qos(dataWriterQosHolder.value);

		dw = publisher.create_datawriter(topic, dataWriterQosHolder.value, null, NO_STATUS_MASK.value);
		if (dw == null) {
			throw new NullPointerException("data_writer");
		}

		return dw;
	}

	private void initQoSHolder() {
		domainParticipantQosHolder = new DomainParticipantQosHolder();
		topicQosHolder = new TopicQosHolder();
		publisherQosHolder = new PublisherQosHolder();
		dataWriterQosHolder = new DataWriterQosHolder();
	}

	@Override
	public void run() {

		System.out.println("-- PUBLISHER [opendds] --");
		int i = 0;
		while (activate) {
			sendMessage(new MyTopic(String.valueOf(i), "MyTopic"));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}

	}

	private void sendMessage(MyTopic data) {
		MyTopicDataWriter wh = MyTopicDataWriterHelper.narrow(dataWriter);
		int status = wh.write(data, HANDLE_NIL.value);
		if (status == RETCODE_OK.value) {
			System.out.println("publisher -> id: " + data.id + " name: " + data.name);
		}
	}

}
