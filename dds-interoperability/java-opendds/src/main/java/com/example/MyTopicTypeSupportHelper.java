package com.example;
public abstract class MyTopicTypeSupportHelper {
  // Any and TypeCode operations not currently implemented
  public static String id() { return "IDL:com/example/MyTopicTypeSupport:1.0"; }
  public static MyTopicTypeSupport narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof MyTopicTypeSupport)
      return (MyTopicTypeSupport)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
      return native_unarrow(obj);
  }

  public static MyTopicTypeSupport unchecked_narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof MyTopicTypeSupport)
      return (MyTopicTypeSupport)obj;
    else
      return native_unarrow(obj);
  }

  private native static MyTopicTypeSupport native_unarrow(org.omg.CORBA.Object obj);

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
