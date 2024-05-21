package com.example;
public abstract class _MyTopicDataWriterLocalBase extends org.omg.CORBA.LocalObject implements MyTopicDataWriter {
  private String[] _type_ids = {"IDL:com/example/MyTopicDataWriter:1.0", "IDL:DDS/DataWriter:1.0", "IDL:DDS/Entity:1.0"};

  public String[] _ids() { return (String[])_type_ids.clone(); }
}
