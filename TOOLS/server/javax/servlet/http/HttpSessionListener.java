package javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionListener extends EventListener {
  void sessionCreated(HttpSessionEvent paramHttpSessionEvent);
  
  void sessionDestroyed(HttpSessionEvent paramHttpSessionEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpSessionListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */