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
import DDS.TOPIC_QOS_DEFAULT;
import DDS.Topic;
import DDS.TopicQosHolder;
import DDS.XCDR_DATA_REPRESENTATION;
import OpenDDS.DCPS.NO_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;

public abstract class AbstractWriterInitializer<T, DW extends DataWriter> {

    protected DomainParticipantFactory domainParticipantFactory;
    protected DomainParticipant domainParticipant;
    protected Topic topic;
    protected DW dataWriter;
    protected Publisher publisher;

    // QoS
    protected DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
    protected TopicQosHolder topicQosHolder = new TopicQosHolder();
    protected PublisherQosHolder publisherQosHolder = new PublisherQosHolder();
    protected DataWriterQosHolder dataWriterQosHolder = new DataWriterQosHolder();

    public AbstractWriterInitializer() {
        initQoSHolder();
        domainParticipantFactory = initConfiguration();
        domainParticipant = initDomainParticipant(domainParticipantFactory);
        try {
            String topicTypeName = initRegisterTopicType(domainParticipant, getTopicTypeSupportClassName());
            topic = initTopic(domainParticipant, getTopicName(), topicTypeName);
            publisher = initPublisher(domainParticipant, getPartitionName());
            dataWriter = initDataWriter(publisher, topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected DomainParticipantFactory initConfiguration() {
        DomainParticipantFactory dpf;

        File file = new File("rtps.ini");
        String[] args = new String[] { "-DCPSConfigFile", file.getPath() };

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

    protected DomainParticipant initDomainParticipant(DomainParticipantFactory domainParticipantFactory) {
        DomainParticipant dp = domainParticipantFactory.lookup_participant(0);
        if (dp == null) {
            dp = domainParticipantFactory.create_participant(0, domainParticipantQosHolder.value, null,
                    NO_STATUS_MASK.value);
            if (dp == null) {
                throw new NullPointerException("domain_participant");
            }
        }
        return dp;
    }

    protected String initRegisterTopicType(DomainParticipant domainParticipant, String typeSupportClassName)
            throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<?> typeSupportClass = Class.forName(typeSupportClassName);
        Object typeSupport = typeSupportClass.newInstance();
        String typeName;

        Method registerType = typeSupportClass.getMethod("register_type", DomainParticipant.class, String.class);
        Method getTypeName = typeSupportClass.getMethod("get_type_name");
        typeName = (String) getTypeName.invoke(typeSupport);
        registerType.invoke(typeSupport, domainParticipant, typeName);

        return typeName;
    }

    protected Topic initTopic(DomainParticipant domainParticipant, String topicName, String topicTypeName) {
        Topic tp;
        topicQosHolder.value = TOPIC_QOS_DEFAULT.get();
        domainParticipant.get_default_topic_qos(topicQosHolder);
        topicQosHolder.value.representation.value = new short[1];
        topicQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
        domainParticipant.set_default_topic_qos(topicQosHolder.value);

        tp = domainParticipant.create_topic(topicName, topicTypeName, topicQosHolder.value, null,
                NO_STATUS_MASK.value);
        if (tp == null) {
            throw new NullPointerException("topic");
        }
        return tp;
    }

    protected Publisher initPublisher(DomainParticipant domainParticipant, String partitionName) {
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

    protected DW initDataWriter(Publisher publisher, Topic topic) {
        DW dw;

        dataWriterQosHolder.value = DATAWRITER_QOS_DEFAULT.get();
        publisher.get_default_datawriter_qos(dataWriterQosHolder);
        dataWriterQosHolder.value.representation.value = new short[1];
        dataWriterQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;
        publisher.set_default_datawriter_qos(dataWriterQosHolder.value);

        dw = (DW) publisher.create_datawriter(topic, dataWriterQosHolder.value, null, NO_STATUS_MASK.value);
        if (dw == null) {
            throw new NullPointerException("data_writer");
        }

        return dw;
    }

    protected void initQoSHolder() {
        domainParticipantQosHolder = new DomainParticipantQosHolder();
        topicQosHolder = new TopicQosHolder();
        publisherQosHolder = new PublisherQosHolder();
        dataWriterQosHolder = new DataWriterQosHolder();
    }

    protected abstract String getTopicTypeSupportClassName();

    protected abstract String getTopicName();

    protected abstract String getPartitionName();
}
