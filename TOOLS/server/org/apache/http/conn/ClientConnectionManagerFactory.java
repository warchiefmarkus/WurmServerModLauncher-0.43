package org.apache.http.conn;

import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;

public interface ClientConnectionManagerFactory {
  ClientConnectionManager newInstance(HttpParams paramHttpParams, SchemeRegistry paramSchemeRegistry);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\conn\ClientConnectionManagerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */