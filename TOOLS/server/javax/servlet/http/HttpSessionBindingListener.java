package javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionBindingListener extends EventListener {
  void valueBound(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  void valueUnbound(HttpSessionBindingEvent paramHttpSessionBindingEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpSessionBindingListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */