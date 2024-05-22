package com.example;
public abstract class _MyTopicDataReaderLocalBase extends org.omg.CORBA.LocalObject implements MyTopicDataReader {
  private String[] _type_ids = {"IDL:com/example/MyTopicDataReader:1.0", "IDL:OpenDDS/DCPS/DataReaderEx:1.0", "IDL:DDS/DataReader:1.0", "IDL:DDS/Entity:1.0"};

  public String[] _ids() { return (String[])_type_ids.clone(); }
}
