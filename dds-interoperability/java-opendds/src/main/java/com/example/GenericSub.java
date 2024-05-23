package com.example;

import DDS.DataReader;
import DDS.DomainParticipant;

public class GenericSub extends AbstractReaderInitializer<MyTopic, MyTopicDataReader, MyTopicDataReaderListenerImpl> {

    @Override
    protected String initRegisterTopicType(DomainParticipant domainParticipant) {
        MyTopicTypeSupport typeSupport = new MyTopicTypeSupportImpl();
        String typeName = typeSupport.get_type_name();
        typeSupport.register_type(domainParticipant, typeName);
        return typeName;
    }

    @Override
    protected MyTopicDataReaderListenerImpl createDataReaderListener() {
        return new MyTopicDataReaderListenerImpl();
    }

    public static void main(String[] args) {
        GenericSub initializer = new GenericSub();
        Thread thread = new Thread(initializer);
        thread.start();
    }
}
