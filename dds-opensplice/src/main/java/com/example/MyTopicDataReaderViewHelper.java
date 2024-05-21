package com.example;

import org.opensplice.dds.dcps.Utilities;

public final class MyTopicDataReaderViewHelper
{

    public static com.example.MyTopicDataReaderView narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicDataReaderView) {
            return (com.example.MyTopicDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.MyTopicDataReaderView unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicDataReaderView) {
            return (com.example.MyTopicDataReaderView)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
