package com.example.dds.a;
public interface ADataWriterOperations extends DDS.DataWriterOperations {
  int register_instance(com.example.dds.a.A instance);
  int register_instance_w_timestamp(com.example.dds.a.A instance, DDS.Time_t timestamp);
  int unregister_instance(com.example.dds.a.A instance, int handle);
  int unregister_instance_w_timestamp(com.example.dds.a.A instance, int handle, DDS.Time_t timestamp);
  int write(com.example.dds.a.A instance_data, int handle);
  int write_w_timestamp(com.example.dds.a.A instance_data, int handle, DDS.Time_t source_timestamp);
  int dispose(com.example.dds.a.A instance_data, int instance_handle);
  int dispose_w_timestamp(com.example.dds.a.A instance_data, int instance_handle, DDS.Time_t source_timestamp);
  int get_key_value(com.example.dds.a.AHolder key_holder, int handle);
  int lookup_instance(com.example.dds.a.A instance_data);
}
