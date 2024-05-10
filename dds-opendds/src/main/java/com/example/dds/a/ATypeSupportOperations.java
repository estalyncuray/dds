package com.example.dds.a;
public interface ATypeSupportOperations extends OpenDDS.DCPS.TypeSupportOperations {
  int encode_to_string(com.example.dds.a.A sample, org.omg.CORBA.StringHolder encoded, OpenDDS.DCPS.RepresentationFormat format);
  int encode_to_bytes(com.example.dds.a.A sample, DDS.OctetSeqHolder encoded, OpenDDS.DCPS.RepresentationFormat format);
  int decode_from_string(String encoded, com.example.dds.a.AHolder sample, OpenDDS.DCPS.RepresentationFormat format);
  int decode_from_bytes(byte[] encoded, com.example.dds.a.AHolder sample, OpenDDS.DCPS.RepresentationFormat format);
}
