package javax.servlet;

import java.util.EventListener;

public interface ServletContextAttributeListener extends EventListener {
  void attributeAdded(ServletContextAttributeEvent paramServletContextAttributeEvent);
  
  void attributeRemoved(ServletContextAttributeEvent paramServletContextAttributeEvent);
  
  void attributeReplaced(ServletContextAttributeEvent paramServletContextAttributeEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletContextAttributeListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */