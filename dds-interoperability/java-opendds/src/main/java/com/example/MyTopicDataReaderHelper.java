package com.example;
public abstract class MyTopicDataReaderHelper {
  // Any and TypeCode operations not currently implemented
  public static String id() { return "IDL:com/example/MyTopicDataReader:1.0"; }
  public static MyTopicDataReader narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof MyTopicDataReader)
      return (MyTopicDataReader)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
      return native_unarrow(obj);
  }

  public static MyTopicDataReader unchecked_narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof MyTopicDataReader)
      return (MyTopicDataReader)obj;
    else
      return native_unarrow(obj);
  }

  private native static MyTopicDataReader native_unarrow(org.omg.CORBA.Object obj);

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
