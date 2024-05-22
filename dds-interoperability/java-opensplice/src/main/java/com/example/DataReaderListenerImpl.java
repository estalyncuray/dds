package com.example;

import java.util.stream.IntStream;

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.DataReaderListener;
import DDS.LENGTH_UNLIMITED;
import DDS.LivelinessChangedStatus;
import DDS.NOT_READ_SAMPLE_STATE;
import DDS.RequestedDeadlineMissedStatus;
import DDS.RequestedIncompatibleQosStatus;
import DDS.SampleInfoSeqHolder;
import DDS.SampleLostStatus;
import DDS.SampleRejectedStatus;
import DDS.SubscriptionMatchedStatus;

public class DataReaderListenerImpl implements DataReaderListener{

	@Override
	public void on_data_available(DataReader arg0) {
		MyTopicDataReader rd = MyTopicDataReaderHelper.narrow(arg0);
		MyTopicSeqHolder aSeqHolder = new MyTopicSeqHolder();
		SampleInfoSeqHolder sampleInfoSeqHolder = new SampleInfoSeqHolder();
		// BE AWARE OF INVALID SAMPLES, READ ONLY FRESH DATA
		//((MyTopicDataReader) dataReader).read(aSeqHolder, sampleInfoSeqHolder, LENGTH_UNLIMITED.value, NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		rd.read(aSeqHolder, sampleInfoSeqHolder, LENGTH_UNLIMITED.value, NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		
		MyTopic[] data = aSeqHolder.value;
		
		IntStream.range(0, data.length)
		.filter(i -> sampleInfoSeqHolder.value[i].valid_data)
		.mapToObj(i -> data[i])
		.forEach(validData -> {
			System.out.println("subscriber -> id: " + validData.id + " name : " + validData.name);
		});
		
		//((MyTopicDataReader) dataReader).return_loan(aSeqHolder, sampleInfoSeqHolder);
		rd.return_loan(aSeqHolder, sampleInfoSeqHolder);
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

}
