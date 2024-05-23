package com.example;

public class GenericSub extends AbstractReaderInitializer<MyTopic, MyTopicDataReader> implements Runnable {

    private boolean activate = false;

    public static void main(String[] args) {
        GenericSub subscriber = new GenericSub();
        Thread thread = new Thread(subscriber);
        thread.start();
    }

    public GenericSub() {
        super();
        activate = true;
    }

    @Override
    protected String getTopicTypeSupportClassName() {
        return "com.example.MyTopicTypeSupportImpl";
    }

    @Override
    protected String getTopicName() {
        return "MyTopic";
    }

    @Override
    protected String getPartitionName() {
        return "MyTopicPartition";
    }

    @Override
    protected DataReaderListenerImpl createDataReaderListenerImpl() {
        return new DataReaderListenerImpl();
    }

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
