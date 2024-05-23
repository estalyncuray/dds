package com.example;

import DDS.*;
import OpenDDS.DCPS.NO_STATUS_MASK;
import OpenDDS.DCPS.TheParticipantFactory;

import org.omg.CORBA.StringSeqHolder;
import java.io.File;

public abstract class AbstractReaderInitializer<T, DR extends DataReader, DRI extends DataReaderListener> implements Runnable {

    private DomainParticipantFactory domainParticipantFactory;
    private DomainParticipant domainParticipant;
    private Topic topic;
    private DataReader dataReader;
    private Subscriber subscriber;
    private DRI dataReaderListenerImpl;
    private boolean activate = false;

    // QoS holders
    private DomainParticipantQosHolder domainParticipantQosHolder = new DomainParticipantQosHolder();
    private TopicQosHolder topicQosHolder = new TopicQosHolder();
    private SubscriberQosHolder subscriberQosHolder = new SubscriberQosHolder();
    private DataReaderQosHolder dataReaderQosHolder = new DataReaderQosHolder();

    public AbstractReaderInitializer() {
        try {
            domainParticipantFactory = initConfiguration();
            domainParticipant = initDomainParticipant(domainParticipantFactory);
            String topicTypeName = initRegisterTopicType(domainParticipant);
            topic = initTopic(domainParticipant, "MyTopic", topicTypeName);
            subscriber = initSubscriber(domainParticipant, "MyTopicPartition");
            dataReaderListenerImpl = createDataReaderListener();
            dataReader = initDataReader(subscriber, topic, dataReaderListenerImpl);
            activate = true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private DomainParticipantFactory initConfiguration() {
        File file = new File("rtps.ini");
        String[] args = {"-DCPSConfigFile", file.getPath()};
        StringSeqHolder stringSeqHolder = new StringSeqHolder(args);
        DomainParticipantFactory dpf = TheParticipantFactory.WithArgs(stringSeqHolder);

        if (dpf == null) {
            throw new NullPointerException("domain_participant_factory");
        }

        domainParticipantQosHolder.value = PARTICIPANT_QOS_DEFAULT.get();
        dpf.get_default_participant_qos(domainParticipantQosHolder);
        dpf.set_default_participant_qos(domainParticipantQosHolder.value);

        return dpf;
    }

    private DomainParticipant initDomainParticipant(DomainParticipantFactory domainParticipantFactory) {
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

    private Topic initTopic(DomainParticipant domainParticipant, String topicName, String topicTypeName) {
        topicQosHolder.value = TOPIC_QOS_DEFAULT.get();
        domainParticipant.get_default_topic_qos(topicQosHolder);
        domainParticipant.set_default_topic_qos(topicQosHolder.value);

        Topic tp = domainParticipant.create_topic(topicName, topicTypeName, topicQosHolder.value, null, NO_STATUS_MASK.value);
        if (tp == null) {
            throw new NullPointerException("topic");
        }
        return tp;
    }

    private Subscriber initSubscriber(DomainParticipant domainParticipant, String partitionName) {
        subscriberQosHolder.value = SUBSCRIBER_QOS_DEFAULT.get();
        domainParticipant.get_default_subscriber_qos(subscriberQosHolder);
        subscriberQosHolder.value.partition.name = new String[]{partitionName};
        domainParticipant.set_default_subscriber_qos(subscriberQosHolder.value);

        Subscriber sb = domainParticipant.create_subscriber(subscriberQosHolder.value, null, NO_STATUS_MASK.value);
        if (sb == null) {
            throw new NullPointerException("subscriber");
        }
        return sb;
    }

    private DataReader initDataReader(Subscriber subscriber, Topic topic, DRI dataReaderListenerImpl) {
        dataReaderQosHolder.value = DATAREADER_QOS_DEFAULT.get();
        subscriber.get_default_datareader_qos(dataReaderQosHolder);
        subscriber.copy_from_topic_qos(dataReaderQosHolder, topicQosHolder.value);
        dataReaderQosHolder.value.representation.value = new short[]{XCDR_DATA_REPRESENTATION.value};
        subscriber.set_default_datareader_qos(dataReaderQosHolder.value);

        DataReader dr = subscriber.create_datareader(topic, dataReaderQosHolder.value, dataReaderListenerImpl, DATA_AVAILABLE_STATUS.value);
        if (dr == null) {
            throw new NullPointerException("data_reader");
        }
        return dr;
    }

    protected abstract String initRegisterTopicType(DomainParticipant domainParticipant);

    protected abstract DRI createDataReaderListener();

    @Override
    public void run() {
        System.out.println("-- SUBSCRIBER [opendds] --");
        while (activate) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
