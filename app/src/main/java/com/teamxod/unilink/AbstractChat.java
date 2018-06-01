package com.teamxod.unilink;

/**
 * Common interface for chat messages, helps share code between RTDB and Firestore examples.
 */
abstract class AbstractChat {

    public abstract String getName();

    public abstract String getMessage();

    public abstract String getUid();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

}
