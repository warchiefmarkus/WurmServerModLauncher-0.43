package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public interface HttpRequestInterceptor {
  void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws HttpException, IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\HttpRequestInterceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */