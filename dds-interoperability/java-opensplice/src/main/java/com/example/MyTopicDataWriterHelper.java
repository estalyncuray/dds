package com.example;

import org.opensplice.dds.dcps.Utilities;

public final class MyTopicDataWriterHelper
{

    public static com.example.MyTopicDataWriter narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicDataWriter) {
            return (com.example.MyTopicDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.MyTopicDataWriter unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicDataWriter) {
            return (com.example.MyTopicDataWriter)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
