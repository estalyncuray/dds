package com.example;
public abstract class _MyTopicTypeSupportLocalBase extends org.omg.CORBA.LocalObject implements MyTopicTypeSupport {
  private String[] _type_ids = {"IDL:com/example/MyTopicTypeSupport:1.0", "IDL:OpenDDS/DCPS/TypeSupport:1.0", "IDL:DDS/TypeSupport:1.0"};

  public String[] _ids() { return (String[])_type_ids.clone(); }
}
