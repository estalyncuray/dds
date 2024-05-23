package com.example;

import DDS.DataReader;
import DDS.DataReaderListener;
import DDS.LivelinessChangedStatus;
import DDS.RETCODE_OK;
import DDS.RequestedDeadlineMissedStatus;
import DDS.RequestedIncompatibleQosStatus;
import DDS.SampleInfo;
import DDS.SampleInfoHolder;
import DDS.SampleLostStatus;
import DDS.SampleRejectedStatus;
import DDS.SubscriptionMatchedStatus;
import OpenDDS.DCPS.BudgetExceededStatus;
import OpenDDS.DCPS.SubscriptionLostStatus;

public abstract class GenericDataReaderListenerImpl<T, DR extends DataReader> implements DataReaderListener {

    protected abstract DR narrow(DataReader dataReader);

    protected abstract void onDataAvailable(T data);

    protected abstract int readNextSample(DR dataReader, SampleHolder<T> data, SampleInfoHolder info);

    @Override
    public void on_data_available(DataReader dataReader) {
        DR specificDataReader = narrow(dataReader);

        T initial = createInitialSample();
        SampleInfo sampleInfo = new SampleInfo();
        SampleInfoHolder sampleInfoHolder = new SampleInfoHolder(sampleInfo);
        SampleHolder<T> sampleHolder = new SampleHolder<>(initial);

        int status = readNextSample(specificDataReader, sampleHolder, sampleInfoHolder);

        if (status == RETCODE_OK.value) {
            if (sampleInfoHolder.value.valid_data) {
                T data = sampleHolder.value;
                onDataAvailable(data);
            }
        }
    }

    protected abstract T createInitialSample();


    @Override
    public void on_liveliness_changed(DataReader dataReader, LivelinessChangedStatus status) {
        // TODO Auto-generated method stub
    }

    @Override
    public void on_requested_deadline_missed(DataReader dataReader, RequestedDeadlineMissedStatus status) {
        // TODO Auto-generated method stub
    }

    @Override
    public void on_requested_incompatible_qos(DataReader dataReader, RequestedIncompatibleQosStatus status) {
        // TODO Auto-generated method stub
    }

    @Override
    public void on_sample_lost(DataReader dataReader, SampleLostStatus status) {
        // TODO Auto-generated method stub
    }

    @Override
    public void on_sample_rejected(DataReader dataReader, SampleRejectedStatus status) {
        // TODO Auto-generated method stub
    }

    @Override
    public void on_subscription_matched(DataReader dataReader, SubscriptionMatchedStatus status) {
        // TODO Auto-generated method stub
    }
}
