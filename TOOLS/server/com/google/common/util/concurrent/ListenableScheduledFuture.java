package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.ScheduledFuture;

@Beta
public interface ListenableScheduledFuture<V> extends ScheduledFuture<V>, ListenableFuture<V> {}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\commo\\util\concurrent\ListenableScheduledFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */