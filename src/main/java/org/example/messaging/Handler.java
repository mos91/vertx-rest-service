package org.example.messaging;

/**
 * Created by Oleg Meleshin on 11.05.2018.
 */
public interface Handler<T> {

  void send(T message);
}
