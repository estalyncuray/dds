package com.example;

import DDS.DataReader;
import DDS.SampleInfoHolder;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;

public class MyTopicDataReaderListenerImpl extends GenericDataReaderListenerImpl<MyTopic, MyTopicDataReader> {

    @Override
    protected MyTopicDataReader narrow(DataReader dataReader) {
        return MyTopicDataReaderHelper.narrow(dataReader);
    }

    @Override
    protected void onDataAvailable(MyTopic data) {
        System.out.println("subscriber -> id: " + data.id + " name: " + data.name);
    }

    @Override
    protected int readNextSample(MyTopicDataReader dataReader, SampleHolder<MyTopic> data, SampleInfoHolder info) {
        MyTopicHolder holder = new MyTopicHolder(data.value);
        return dataReader.read_next_sample(holder, info);
    }

    @Override
    protected MyTopic createInitialSample() {
        MyTopic initial = new MyTopic();
        initial.id = "";
        initial.name = "";
        return initial;
    }

    @Override
    public boolean _is_a(String s) {
        return false;
    }

    @Override
    public boolean _is_equivalent(Object object) {
        return false;
    }

    @Override
    public boolean _non_existent() {
        return false;
    }

    @Override
    public int _hash(int i) {
        return 0;
    }

    @Override
    public Object _duplicate() {
        return null;
    }

    @Override
    public void _release() {

    }

    @Override
    public Object _get_interface_def() {
        return null;
    }

    @Override
    public Request _request(String s) {
        return null;
    }

    @Override
    public Request _create_request(Context context, String s, NVList nvList, NamedValue namedValue) {
        return null;
    }

    @Override
    public Request _create_request(Context context, String s, NVList nvList, NamedValue namedValue, ExceptionList exceptionList, ContextList contextList) {
        return null;
    }

    @Override
    public Policy _get_policy(int i) {
        return null;
    }

    @Override
    public DomainManager[] _get_domain_managers() {
        return new DomainManager[0];
    }

    @Override
    public Object _set_policy_override(Policy[] policies, SetOverrideType setOverrideType) {
        return null;
    }
}
