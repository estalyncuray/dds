package com.example.dds.a;

public interface ADataWriterOperations extends
    DDS.DataWriterOperations
{

    long register_instance(
            com.example.dds.a.A instance_data);

    long register_instance_w_timestamp(
            com.example.dds.a.A instance_data, 
            DDS.Time_t source_timestamp);

    int unregister_instance(
            com.example.dds.a.A instance_data, 
            long handle);

    int unregister_instance_w_timestamp(
            com.example.dds.a.A instance_data, 
            long handle, 
            DDS.Time_t source_timestamp);

    int write(
            com.example.dds.a.A instance_data, 
            long handle);

    int write_w_timestamp(
            com.example.dds.a.A instance_data, 
            long handle, 
            DDS.Time_t source_timestamp);

    int dispose(
            com.example.dds.a.A instance_data, 
            long instance_handle);

    int dispose_w_timestamp(
            com.example.dds.a.A instance_data, 
            long instance_handle, 
            DDS.Time_t source_timestamp);
    
    int writedispose(
            com.example.dds.a.A instance_data, 
            long instance_handle);

    int writedispose_w_timestamp(
            com.example.dds.a.A instance_data, 
            long instance_handle, 
            DDS.Time_t source_timestamp);

    int get_key_value(
            com.example.dds.a.AHolder key_holder, 
            long handle);
    
    long lookup_instance(
            com.example.dds.a.A instance_data);

}
