package org.apache.http.conn;

import java.util.concurrent.TimeUnit;

public interface ClientConnectionRequest {
  ManagedClientConnection getConnection(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ConnectionPoolTimeoutException;
  
  void abortRequest();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\ClientConnectionRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */