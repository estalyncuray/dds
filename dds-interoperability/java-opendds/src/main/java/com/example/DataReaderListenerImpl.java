package com.example;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LivelinessChangedStatus;
import DDS.NOT_READ_SAMPLE_STATE;
import DDS.RETCODE_OK;
import DDS.RequestedDeadlineMissedStatus;
import DDS.RequestedIncompatibleQosStatus;
import DDS.SampleInfo;
import DDS.SampleInfoHolder;
import DDS.SampleLostStatus;
import DDS.SampleRejectedStatus;
import DDS.SubscriptionMatchedStatus;
import OpenDDS.DCPS.BudgetExceededStatus;
import OpenDDS.DCPS.DataReaderListener;
import OpenDDS.DCPS.SubscriptionLostStatus;

public class DataReaderListenerImpl implements DataReaderListener {

	@Override
	public void on_data_available(DataReader arg0) {
		MyTopicDataReader rh = MyTopicDataReaderHelper.narrow(arg0);
		
		MyTopic initial = new MyTopic();
		initial.id = "";
		initial.name = "";
		
		MyTopicHolder mh = new MyTopicHolder(initial);
		SampleInfo si = new SampleInfo(NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value,
				new DDS.Time_t(), 0, 0, 0, 0, 0, 0, 0, false, 0L);
		SampleInfoHolder sih = new SampleInfoHolder(si);

		int status = rh.read_next_sample(mh, sih);
		
		if(status == RETCODE_OK.value) {
			if(sih.value.valid_data) {
				MyTopic data = mh.value;
				System.out.println("subscriber -> id: " + data.id + " name : " + data.name);
			}
		}
		
	}
	
	@Override
	public void on_budget_exceeded(DataReader arg0, BudgetExceededStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_subscription_disconnected(DataReader arg0, SubscriptionLostStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_subscription_lost(DataReader arg0, SubscriptionLostStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_subscription_reconnected(DataReader arg0, SubscriptionLostStatus arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void on_liveliness_changed(DataReader arg0, LivelinessChangedStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_requested_deadline_missed(DataReader arg0, RequestedDeadlineMissedStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_requested_incompatible_qos(DataReader arg0, RequestedIncompatibleQosStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_sample_lost(DataReader arg0, SampleLostStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_subscription_matched(DataReader arg0, SubscriptionMatchedStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Request _create_request(Context arg0, String arg1, NVList arg2, NamedValue arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Request _create_request(Context arg0, String arg1, NVList arg2, NamedValue arg3, ExceptionList arg4,
			ContextList arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainManager[] _get_domain_managers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _get_interface_def() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy _get_policy(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int _hash(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean _is_a(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean _is_equivalent(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean _non_existent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void _release() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Request _request(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _set_policy_override(Policy[] arg0, SetOverrideType arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
