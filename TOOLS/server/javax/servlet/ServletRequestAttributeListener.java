package javax.servlet;

import java.util.EventListener;

public interface ServletRequestAttributeListener extends EventListener {
  void attributeAdded(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
  
  void attributeRemoved(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
  
  void attributeReplaced(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletRequestAttributeListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */