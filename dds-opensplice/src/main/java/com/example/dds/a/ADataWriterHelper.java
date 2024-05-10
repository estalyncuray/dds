package com.example.dds.a;

import org.opensplice.dds.dcps.Utilities;

public final class ADataWriterHelper
{

    public static com.example.dds.a.ADataWriter narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ADataWriter) {
            return (com.example.dds.a.ADataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.dds.a.ADataWriter unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ADataWriter) {
            return (com.example.dds.a.ADataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
