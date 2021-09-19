package com.wurmonline.server;

public interface ServerMonitoring {
  boolean isLagging();
  
  byte[] getExternalIp();
  
  byte[] getInternalIp();
  
  int getIntraServerPort();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\ServerMonitoring.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */