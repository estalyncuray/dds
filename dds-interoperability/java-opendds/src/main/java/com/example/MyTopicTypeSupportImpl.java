package com.example;
public class MyTopicTypeSupportImpl extends _MyTopicTypeSupportTAOPeer {
    public MyTopicTypeSupportImpl() {
        super(_jni_init());
    }
    private static native long _jni_init();
}
