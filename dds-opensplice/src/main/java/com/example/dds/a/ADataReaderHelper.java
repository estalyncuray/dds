package com.example.dds.a;

import org.opensplice.dds.dcps.Utilities;

public final class ADataReaderHelper
{

    public static com.example.dds.a.ADataReader narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ADataReader) {
            return (com.example.dds.a.ADataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.dds.a.ADataReader unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ADataReader) {
            return (com.example.dds.a.ADataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
