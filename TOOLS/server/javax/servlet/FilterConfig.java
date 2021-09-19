package javax.servlet;

import java.util.Enumeration;

public interface FilterConfig {
  String getFilterName();
  
  ServletContext getServletContext();
  
  String getInitParameter(String paramString);
  
  Enumeration<String> getInitParameterNames();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\FilterConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */