package com.example;

import DDS.HANDLE_NIL;
import DDS.RETCODE_OK;

public class GenericPub extends AbstractWriterInitializer<MyTopic, MyTopicDataWriter> implements Runnable {

    private boolean activate = false;

    public static void main(String[] args) {
        GenericPub publisher = new GenericPub();
        Thread thread = new Thread(publisher);
        thread.start();
    }

    public GenericPub() {
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
