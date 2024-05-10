package com.example.dds.a;

import org.opensplice.dds.dcps.Utilities;

public final class ATypeSupportHelper
{

    public static com.example.dds.a.ATypeSupport narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ATypeSupport) {
            return (com.example.dds.a.ATypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.dds.a.ATypeSupport unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ATypeSupport) {
            return (com.example.dds.a.ATypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
