package com.example;
public interface MyTopicDataReaderOperations extends OpenDDS.DCPS.DataReaderExOperations {
  int read(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int sample_states, int view_states, int instance_states);
  int take(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int sample_states, int view_states, int instance_states);
  int read_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, DDS.ReadCondition a_condition);
  int take_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, DDS.ReadCondition a_condition);
  int read_next_sample(com.example.MyTopicHolder received_data, DDS.SampleInfoHolder sample_info);
  int take_next_sample(com.example.MyTopicHolder received_data, DDS.SampleInfoHolder sample_info);
  int read_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);
  int take_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);
  int read_instance_w_condition(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int a_handle, DDS.ReadCondition a_condition);
  int take_instance_w_condition(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int a_handle, DDS.ReadCondition a_condition);
  int read_next_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);
  int take_next_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);
  int read_next_instance_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int previous_handle, DDS.ReadCondition a_condition);
  int take_next_instance_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int previous_handle, DDS.ReadCondition a_condition);
  int return_loan(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq);
  int get_key_value(com.example.MyTopicHolder key_holder, int handle);
  int lookup_instance(com.example.MyTopic instance_data);
}
