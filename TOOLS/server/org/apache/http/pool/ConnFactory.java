package org.apache.http.pool;

import java.io.IOException;

public interface ConnFactory<T, C> {
  C create(T paramT) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\pool\ConnFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */