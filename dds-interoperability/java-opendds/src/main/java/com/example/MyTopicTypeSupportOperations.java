package com.example;
public interface MyTopicTypeSupportOperations extends OpenDDS.DCPS.TypeSupportOperations {
  int encode_to_string(com.example.MyTopic sample, org.omg.CORBA.StringHolder encoded, OpenDDS.DCPS.RepresentationFormat format);
  int encode_to_bytes(com.example.MyTopic sample, DDS.OctetSeqHolder encoded, OpenDDS.DCPS.RepresentationFormat format);
  int decode_from_string(String encoded, com.example.MyTopicHolder sample, OpenDDS.DCPS.RepresentationFormat format);
  int decode_from_bytes(byte[] encoded, com.example.MyTopicHolder sample, OpenDDS.DCPS.RepresentationFormat format);
}
