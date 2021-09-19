package com.mysql.jdbc.profiler;

import com.mysql.jdbc.Extension;

public interface ProfilerEventHandler extends Extension {
  void consumeEvent(ProfilerEvent paramProfilerEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\profiler\ProfilerEventHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */