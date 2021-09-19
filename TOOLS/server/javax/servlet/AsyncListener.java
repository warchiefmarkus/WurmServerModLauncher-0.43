package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public interface AsyncListener extends EventListener {
  void onComplete(AsyncEvent paramAsyncEvent) throws IOException;
  
  void onTimeout(AsyncEvent paramAsyncEvent) throws IOException;
  
  void onError(AsyncEvent paramAsyncEvent) throws IOException;
  
  void onStartAsync(AsyncEvent paramAsyncEvent) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\AsyncListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */