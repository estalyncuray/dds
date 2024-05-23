package com.example;

import DDS.SampleInfoHolder;

public interface Readable<T> {
    int read_next_sample(SampleHolder<T> data, SampleInfoHolder info);
}
