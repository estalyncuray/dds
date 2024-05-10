package com.example.dds.a;
public abstract class ADataReaderHelper {
  // Any and TypeCode operations not currently implemented
  public static String id() { return "IDL:com/example/dds/a/ADataReader:1.0"; }
  public static ADataReader narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof ADataReader)
      return (ADataReader)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
      return native_unarrow(obj);
  }

  public static ADataReader unchecked_narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof ADataReader)
      return (ADataReader)obj;
    else
      return native_unarrow(obj);
  }

  private native static ADataReader native_unarrow(org.omg.CORBA.Object obj);

  static {
    String propVal = System.getProperty("opendds.native.debug");
    if (propVal != null && ("1".equalsIgnoreCase(propVal) ||
        "y".equalsIgnoreCase(propVal) ||
        "yes".equalsIgnoreCase(propVal) ||
        "t".equalsIgnoreCase(propVal) ||
        "true".equalsIgnoreCase(propVal)))
      System.loadLibrary("Ad");
    else System.loadLibrary("A");
  }

}
