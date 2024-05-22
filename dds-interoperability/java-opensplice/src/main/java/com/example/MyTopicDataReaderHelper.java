package com.example;

import org.opensplice.dds.dcps.Utilities;

public final class MyTopicDataReaderHelper
{

    public static com.example.MyTopicDataReader narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicDataReader) {
            return (com.example.MyTopicDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.MyTopicDataReader unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicDataReader) {
            return (com.example.MyTopicDataReader)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
