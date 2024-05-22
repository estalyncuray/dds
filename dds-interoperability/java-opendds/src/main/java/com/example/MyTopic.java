package com.example;
public final class MyTopic implements java.io.Serializable {
  public String id;
  public String name;

  public MyTopic() {}

  public MyTopic(String _id, String _name) {
    id = _id;
    name = _name;
  }
}
