package org.apache.http.conn;

import javax.net.ssl.SSLSession;
import org.apache.http.HttpInetConnection;
import org.apache.http.conn.routing.HttpRoute;

public interface HttpRoutedConnection extends HttpInetConnection {
  boolean isSecure();
  
  HttpRoute getRoute();
  
  SSLSession getSSLSession();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\HttpRoutedConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */