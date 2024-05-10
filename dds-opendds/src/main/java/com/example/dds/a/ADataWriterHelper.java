package com.example.dds.a;
public abstract class ADataWriterHelper {
  // Any and TypeCode operations not currently implemented
  public static String id() { return "IDL:com/example/dds/a/ADataWriter:1.0"; }
  public static ADataWriter narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof ADataWriter)
      return (ADataWriter)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
      return native_unarrow(obj);
  }

  public static ADataWriter unchecked_narrow(org.omg.CORBA.Object obj) {
    if (obj == null)
      return null;
    else if (obj instanceof ADataWriter)
      return (ADataWriter)obj;
    else
      return native_unarrow(obj);
  }

  private native static ADataWriter native_unarrow(org.omg.CORBA.Object obj);

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
