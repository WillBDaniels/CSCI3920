package com.cmdComponent;

/**
 * An interface which allows for passing of immutable messages between
 * actors.
 */
public interface Actor {
    /**
     * A thread-safe way to send messages to this actor.
     */
    public void sendMsg(Object msg);
}
