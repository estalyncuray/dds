package com.example.dds.a;

import org.opensplice.dds.dcps.Utilities;

public final class ADataReaderViewHelper
{

    public static com.example.dds.a.ADataReaderView narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ADataReaderView) {
            return (com.example.dds.a.ADataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.dds.a.ADataReaderView unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.dds.a.ADataReaderView) {
            return (com.example.dds.a.ADataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
