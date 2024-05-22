package com.example;
public class _MyTopicDataReaderTAOPeer extends i2jrt.TAOLocalObject implements MyTopicDataReader {
  protected _MyTopicDataReaderTAOPeer(long ptr) {
    super(ptr);
  }

  public native int read(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int sample_states, int view_states, int instance_states);

  public native int take(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int sample_states, int view_states, int instance_states);

  public native int read_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, DDS.ReadCondition a_condition);

  public native int take_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, DDS.ReadCondition a_condition);

  public native int read_next_sample(com.example.MyTopicHolder received_data, DDS.SampleInfoHolder sample_info);

  public native int take_next_sample(com.example.MyTopicHolder received_data, DDS.SampleInfoHolder sample_info);

  public native int read_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);

  public native int take_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);

  public native int read_instance_w_condition(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int a_handle, DDS.ReadCondition a_condition);

  public native int take_instance_w_condition(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int a_handle, DDS.ReadCondition a_condition);

  public native int read_next_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);

  public native int take_next_instance(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq, int max_samples, int a_handle, int sample_states, int view_states, int instance_states);

  public native int read_next_instance_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int previous_handle, DDS.ReadCondition a_condition);

  public native int take_next_instance_w_condition(com.example.MyTopicSeqHolder data_values, DDS.SampleInfoSeqHolder sample_infos, int max_samples, int previous_handle, DDS.ReadCondition a_condition);

  public native int return_loan(com.example.MyTopicSeqHolder received_data, DDS.SampleInfoSeqHolder info_seq);

  public native int get_key_value(com.example.MyTopicHolder key_holder, int handle);

  public native int lookup_instance(com.example.MyTopic instance_data);

  public native void get_latency_stats(OpenDDS.DCPS.LatencyStatisticsSeqHolder stats);

  public native void reset_latency_stats();

  public native boolean statistics_enabled();

  public native void statistics_enabled(boolean statistics_enabled);

  public native DDS.ReadCondition create_readcondition(int sample_states, int view_states, int instance_states);

  public native DDS.QueryCondition create_querycondition(int sample_states, int view_states, int instance_states, String query_expression, String[] query_parameters);

  public native int delete_readcondition(DDS.ReadCondition a_condition);

  public native int delete_contained_entities();

  public native int set_qos(DDS.DataReaderQos qos);

  public native int get_qos(DDS.DataReaderQosHolder qos);

  public native int set_listener(DDS.DataReaderListener a_listener, int mask);

  public native DDS.DataReaderListener get_listener();

  public native DDS.TopicDescription get_topicdescription();

  public native DDS.Subscriber get_subscriber();

  public native int get_sample_rejected_status(DDS.SampleRejectedStatusHolder status);

  public native int get_liveliness_changed_status(DDS.LivelinessChangedStatusHolder status);

  public native int get_requested_deadline_missed_status(DDS.RequestedDeadlineMissedStatusHolder status);

  public native int get_requested_incompatible_qos_status(DDS.RequestedIncompatibleQosStatusHolder status);

  public native int get_subscription_matched_status(DDS.SubscriptionMatchedStatusHolder status);

  public native int get_sample_lost_status(DDS.SampleLostStatusHolder status);

  public native int wait_for_historical_data(DDS.Duration_t max_wait);

  public native int get_matched_publications(DDS.InstanceHandleSeqHolder publication_handles);

  public native int get_matched_publication_data(DDS.PublicationBuiltinTopicDataHolder publication_data, int publication_handle);

  public native int enable();

  public native DDS.StatusCondition get_statuscondition();

  public native int get_status_changes();

  public native int get_instance_handle();


  static {
    String propVal = System.getProperty("opendds.native.debug");
    if (propVal != null && ("1".equalsIgnoreCase(propVal) ||
        "y".equalsIgnoreCase(propVal) ||
        "yes".equalsIgnoreCase(propVal) ||
        "t".equalsIgnoreCase(propVal) ||
        "true".equalsIgnoreCase(propVal)))
      System.loadLibrary("MyTopicd");
    else System.loadLibrary("MyTopic");
  }

}
