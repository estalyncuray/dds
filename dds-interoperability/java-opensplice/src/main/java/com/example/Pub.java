package com.example;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.environment.EnvironmentUtils;

import DDS.DOMAIN_ID_DEFAULT;
import DDS.DataWriter;
import DDS.DataWriterQos;
import DDS.DataWriterQosHolder;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.DomainParticipantQos;
import DDS.DomainParticipantQosHolder;
import DDS.HANDLE_NIL;
import DDS.Publisher;
import DDS.PublisherQos;
import DDS.PublisherQosHolder;
import DDS.RETCODE_OK;
import DDS.STATUS_MASK_NONE;
import DDS.Topic;
import DDS.TopicQos;
import DDS.TopicQosHolder;

public class Pub implements Runnable {

	private static final int MAX_MSG = 150;
	private DomainParticipantFactory domainParticipantFactory;
	private DomainParticipant domainParticipant;
	private Publisher publisher;
	private Topic topic;
	private DataWriter dataWriter;
	private boolean activate = false;
	
	// QoS
	private DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
	private TopicQosHolder topicQosHolder = new TopicQosHolder();
	private PublisherQosHolder publisherQosHolder = new PublisherQosHolder();
	private DataWriterQosHolder dataWriterQosHolder = new DataWriterQosHolder();
	
	public static void main(String[] args) throws ExecuteException, IOException {
		Pub publisher = new Pub();
		Thread thread = new Thread(publisher);
		thread.start();
	}
	
	public Pub() throws ExecuteException, IOException {
		try {
			initQoSHolder();
			domainParticipantFactory = initConfiguration();
			domainParticipant = initDomainParticipant(domainParticipantFactory);
			String topicTypeName = initRegisterTopicType(domainParticipant);
			topic = initTopic(domainParticipant, "MyTopic", topicTypeName);
			publisher = initPublisher(domainParticipant, "MyTopicPartition");
			dataWriter = initDataWriter(publisher, topic);
			activate = true;
		}catch (NullPointerException e) {
			System.out.println("ERROR " + e.getMessage());
		}
	}
	
	private void initQoSHolder() {
		domainParticipantQosHolder = new DomainParticipantQosHolder();
		topicQosHolder = new TopicQosHolder();
		publisherQosHolder = new PublisherQosHolder();
		dataWriterQosHolder = new DataWriterQosHolder();
	}
	
	private DomainParticipantFactory initConfiguration() throws ExecuteException, IOException {
		/*File file = new File("ospl.xml");
		
		if(!file.exists()) {
			throw new NullPointerException("ospl_file");
		}
		
		CommandLine cmdLine = CommandLine.parse("cmd");
		cmdLine.addArgument("/c echo %OSPL_URI%", false);

		Map<String, String> env = EnvironmentUtils.getProcEnvironment();		
		env.put("OSPL_URI", "file://" + file.getPath());
		
		Executor executor = DefaultExecutor.builder().get();
		executor.execute(cmdLine,env);*/

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
		dp = domainParticipantFactory.lookup_participant(DOMAIN_ID_DEFAULT.value);
		if(dp == null) {
			dp = domainParticipantFactory.create_participant(DOMAIN_ID_DEFAULT.value, domainParticipantQosHolder.value, null, STATUS_MASK_NONE.value);
			if(dp == null) {
				throw new NullPointerException("domain_participant");
			}
		}
		
		return dp;
	}

	private String initRegisterTopicType(DomainParticipant domainParticipant) {
		MyTopicTypeSupport aTypeSupport = new MyTopicTypeSupport();
		String typeName = aTypeSupport.get_type_name();
		aTypeSupport.register_type(domainParticipant, typeName);
		
		return typeName;
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

	private Publisher initPublisher(DomainParticipant domainParticipant, String partitionName) {
		Publisher pb;
		
		publisherQosHolder.value = new PublisherQos();
		domainParticipant.get_default_publisher_qos(publisherQosHolder);
		publisherQosHolder.value.partition.name = new String[1];
		publisherQosHolder.value.partition.name[0] = partitionName;
		domainParticipant.set_default_publisher_qos(publisherQosHolder.value);
		
		pb = domainParticipant.create_publisher(publisherQosHolder.value, null, STATUS_MASK_NONE.value);
		if(pb == null) {
			throw new NullPointerException("publisher");
		}

		return pb;
	}

	private DataWriter initDataWriter(Publisher publisher, Topic topic) {
		DataWriter dw;
		
		dataWriterQosHolder.value = new DataWriterQos();
		publisher.get_default_datawriter_qos(dataWriterQosHolder);
		publisher.set_default_datawriter_qos(dataWriterQosHolder.value);
		
		dw = publisher.create_datawriter(topic, dataWriterQosHolder.value, null, STATUS_MASK_NONE.value);
		if(dw == null) {
			throw new NullPointerException("data_writer");
		}

		return dw;
	}
	
	@Override
	public void run() {
		if (activate) {
			System.out.println("-- PUBLISHER [opensplice]--");
			for (int j = 0; j < MAX_MSG; j++) {
				sendMessage(new MyTopic(String.valueOf(j), "MyTopic"));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void sendMessage(MyTopic data) {
		MyTopicDataWriter wh = MyTopicDataWriterHelper.narrow(dataWriter);
		int status = wh.write(data, HANDLE_NIL.value);
		if(status == RETCODE_OK.value) {
			System.out.println("publisher -> id: " + data.id + " name: " + data.name);
		}
	}

}
