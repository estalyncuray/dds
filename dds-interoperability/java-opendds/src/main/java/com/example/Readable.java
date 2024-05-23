package com.example;

import DDS.SampleInfoHolder;

public interface Readable<T> {
    int read_next_sample(TopicHolder<T> data, SampleInfoHolder info);
}
