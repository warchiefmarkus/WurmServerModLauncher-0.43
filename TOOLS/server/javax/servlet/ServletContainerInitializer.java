package javax.servlet;

import java.util.Set;

public interface ServletContainerInitializer {
  void onStartup(Set<Class<?>> paramSet, ServletContext paramServletContext) throws ServletException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletContainerInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */