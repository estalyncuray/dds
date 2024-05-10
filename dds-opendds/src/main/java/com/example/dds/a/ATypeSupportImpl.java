package com.example.dds.a;
public class ATypeSupportImpl extends _ATypeSupportTAOPeer {
    public ATypeSupportImpl() {
        super(_jni_init());
    }
    private static native long _jni_init();
}
