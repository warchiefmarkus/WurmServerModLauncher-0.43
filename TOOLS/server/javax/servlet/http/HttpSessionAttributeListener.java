package javax.servlet.http;

import java.util.EventListener;

public interface HttpSessionAttributeListener extends EventListener {
  void attributeAdded(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  void attributeRemoved(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  void attributeReplaced(HttpSessionBindingEvent paramHttpSessionBindingEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpSessionAttributeListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */