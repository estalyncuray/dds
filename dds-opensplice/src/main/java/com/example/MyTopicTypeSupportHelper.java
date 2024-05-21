package com.example;

import org.opensplice.dds.dcps.Utilities;

public final class MyTopicTypeSupportHelper
{

    public static com.example.MyTopicTypeSupport narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicTypeSupport) {
            return (com.example.MyTopicTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

    public static com.example.MyTopicTypeSupport unchecked_narrow(java.lang.Object obj)
    {
        if (obj == null) {
            return null;
        } else if (obj instanceof com.example.MyTopicTypeSupport) {
            return (com.example.MyTopicTypeSupport)obj;
        } else {
            throw Utilities.createException(Utilities.EXCEPTION_TYPE_BAD_PARAM, null);
        }
    }

}
