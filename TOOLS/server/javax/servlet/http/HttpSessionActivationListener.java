package javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionActivationListener extends EventListener {
  void sessionWillPassivate(HttpSessionEvent paramHttpSessionEvent);
  
  void sessionDidActivate(HttpSessionEvent paramHttpSessionEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpSessionActivationListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */