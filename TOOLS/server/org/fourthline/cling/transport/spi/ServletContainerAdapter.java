package org.fourthline.cling.transport.spi;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import javax.servlet.Servlet;

public interface ServletContainerAdapter {
  void setExecutorService(ExecutorService paramExecutorService);
  
  int addConnector(String paramString, int paramInt) throws IOException;
  
  void removeConnector(String paramString, int paramInt);
  
  void registerServlet(String paramString, Servlet paramServlet);
  
  void startIfNotRunning();
  
  void stopIfRunning();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\ServletContainerAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */