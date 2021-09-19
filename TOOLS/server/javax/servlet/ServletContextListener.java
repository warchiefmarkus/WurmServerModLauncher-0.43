package javax.servlet;

import java.util.EventListener;

public interface ServletContextListener extends EventListener {
  void contextInitialized(ServletContextEvent paramServletContextEvent);
  
  void contextDestroyed(ServletContextEvent paramServletContextEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletContextListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */