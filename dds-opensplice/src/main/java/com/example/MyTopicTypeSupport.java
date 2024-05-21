package com.example;

public class MyTopicTypeSupport extends org.opensplice.dds.dcps.TypeSupportImpl implements DDS.TypeSupportOperations
{
    private static final long serialVersionUID = 1L;

    private long copyCache;

    public MyTopicTypeSupport()
    {
        super("com::example::MyTopic",
              "",
              "id",
              null,
              com.example.MyTopicMetaHolder.metaDescriptor);
    }

    @Override
    protected DDS.DataWriter create_datawriter ()
    {
        return new MyTopicDataWriterImpl(this);
    }

    @Override
    protected DDS.DataReader create_datareader ()
    {
        return new MyTopicDataReaderImpl(this);
    }

    @Override
    protected DDS.DataReaderView create_dataview ()
    {
        return new MyTopicDataReaderViewImpl(this);
    }
}
