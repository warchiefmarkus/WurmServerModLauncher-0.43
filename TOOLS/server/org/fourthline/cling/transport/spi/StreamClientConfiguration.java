package org.fourthline.cling.transport.spi;

import java.util.concurrent.ExecutorService;

public interface StreamClientConfiguration {
  ExecutorService getRequestExecutorService();
  
  int getTimeoutSeconds();
  
  int getLogWarningSeconds();
  
  String getUserAgentValue(int paramInt1, int paramInt2);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\StreamClientConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */