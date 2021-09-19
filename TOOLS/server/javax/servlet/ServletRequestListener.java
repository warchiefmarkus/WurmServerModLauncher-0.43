package javax.servlet;

import java.util.EventListener;

public interface ServletRequestListener extends EventListener {
  void requestDestroyed(ServletRequestEvent paramServletRequestEvent);
  
  void requestInitialized(ServletRequestEvent paramServletRequestEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletRequestListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */