package com.example.dds.a;

public interface ADataReaderViewOperations extends
    DDS.DataReaderViewOperations
{

    int read(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int take(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int read_w_condition(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            DDS.ReadCondition a_condition);

    int take_w_condition(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            DDS.ReadCondition a_condition);

    int read_next_sample(
            com.example.dds.a.AHolder received_data, 
            DDS.SampleInfoHolder sample_info);

    int take_next_sample(
            com.example.dds.a.AHolder received_data, 
            DDS.SampleInfoHolder sample_info);

    int read_instance(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples,
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int take_instance(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int read_next_instance(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int take_next_instance(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            int sample_states, 
            int view_states, 
            int instance_states);

    int read_next_instance_w_condition(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            DDS.ReadCondition a_condition);

    int take_next_instance_w_condition(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq, 
            int max_samples, 
            long a_handle, 
            DDS.ReadCondition a_condition);

    int return_loan(
            com.example.dds.a.ASeqHolder received_data, 
            DDS.SampleInfoSeqHolder info_seq);

    int get_key_value(
            com.example.dds.a.AHolder key_holder, 
            long handle);
    
    long lookup_instance(
            com.example.dds.a.A instance);

}
