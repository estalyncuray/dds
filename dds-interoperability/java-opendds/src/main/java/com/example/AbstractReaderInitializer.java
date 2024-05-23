package com.example;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

public abstract class AbstractReaderInitializer<T, DR extends DataReader> {

    protected DomainParticipantFactory domainParticipantFactory;
    protected DomainParticipant domainParticipant;
    protected Topic topic;
    protected DR dataReader;
    protected Subscriber subscriber;

    // QoS
    protected DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
    protected TopicQosHolder topicQosHolder = new TopicQosHolder();
    protected SubscriberQosHolder subscriberQosHolder = new SubscriberQosHolder();
    protected DataReaderQosHolder dataReaderQosHolder = new DataReaderQosHolder();

    public AbstractReaderInitializer() {
        initQoSHolder();
        domainParticipantFactory = initConfiguration();
        domainParticipant = initDomainParticipant(domainParticipantFactory);
        try {
            String topicTypeName = initRegisterTopicType(domainParticipant, getTopicTypeSupportClassName());
            topic = initTopic(domainParticipant, getTopicName(), topicTypeName);
            subscriber = initSubscriber(domainParticipant, getPartitionName());
            dataReader = initDataReader(subscriber, topic, createDataReaderListenerImpl());
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

    protected Subscriber initSubscriber(DomainParticipant domainParticipant, String partitionName) {
        Subscriber sb;

        subscriberQosHolder.value = SUBSCRIBER_QOS_DEFAULT.get();
        domainParticipant.get_default_subscriber_qos(subscriberQosHolder);
        subscriberQosHolder.value.partition.name = new String[1];
        subscriberQosHolder.value.partition.name[0] = partitionName;
        domainParticipant.set_default_subscriber_qos(subscriberQosHolder.value);

        sb = domainParticipant.create_subscriber(subscriberQosHolder.value, null, NO_STATUS_MASK.value);
        if (sb == null) {
            throw new NullPointerException("subscriber");
        }

        return sb;
    }

    protected DR initDataReader(Subscriber subscriber, Topic topic, DataReaderListenerImpl dataReaderListenerImpl) {
        DR dr;

        dataReaderQosHolder.value = DATAREADER_QOS_DEFAULT.get();
        subscriber.get_default_datareader_qos(dataReaderQosHolder);
        subscriber.copy_from_topic_qos(dataReaderQosHolder, topicQosHolder.value);
        dataReaderQosHolder.value.representation.value = new short[1];
        dataReaderQosHolder.value.representation.value[0] = XCDR_DATA_REPRESENTATION.value;

        subscriber.set_default_datareader_qos(dataReaderQosHolder.value);

        dr = (DR) subscriber.create_datareader(topic, dataReaderQosHolder.value, dataReaderListenerImpl,
                DATA_AVAILABLE_STATUS.value);

        if (dr == null) {
            throw new NullPointerException("data_reader");
        }

        return dr;
    }

    protected void initQoSHolder() {
        domainParticipantQosHolder = new DomainParticipantQosHolder();
        topicQosHolder = new TopicQosHolder();
        subscriberQosHolder = new SubscriberQosHolder();
        dataReaderQosHolder = new DataReaderQosHolder();
    }

    protected abstract String getTopicTypeSupportClassName();

    protected abstract String getTopicName();

    protected abstract String getPartitionName();

    protected abstract DataReaderListenerImpl createDataReaderListenerImpl();
}
