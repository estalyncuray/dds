package com.example;
public class _MyTopicTypeSupportTAOPeer extends i2jrt.TAOLocalObject implements MyTopicTypeSupport {
  protected _MyTopicTypeSupportTAOPeer(long ptr) {
    super(ptr);
  }

  public native int encode_to_string(com.example.MyTopic sample, org.omg.CORBA.StringHolder encoded, OpenDDS.DCPS.RepresentationFormat format);

  public native int encode_to_bytes(com.example.MyTopic sample, DDS.OctetSeqHolder encoded, OpenDDS.DCPS.RepresentationFormat format);

  public native int decode_from_string(String encoded, com.example.MyTopicHolder sample, OpenDDS.DCPS.RepresentationFormat format);

  public native int decode_from_bytes(byte[] encoded, com.example.MyTopicHolder sample, OpenDDS.DCPS.RepresentationFormat format);

  public native DDS.DataWriter create_datawriter();

  public native DDS.DataReader create_datareader();

  public native DDS.DataReader create_multitopic_datareader();

  public native boolean has_dcps_key();

  public native int unregister_type(DDS.DomainParticipant domain, String type_name);

  public native void representations_allowed_by_type(DDS.DataRepresentationIdSeqHolder seq);

  public native OpenDDS.DCPS.RepresentationFormat make_format(short representation);

  public native int register_type(DDS.DomainParticipant domain, String type_name);

  public native String get_type_name();


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
