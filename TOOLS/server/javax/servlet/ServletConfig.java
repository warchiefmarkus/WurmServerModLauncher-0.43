package javax.servlet;

import java.util.Enumeration;

public interface ServletConfig {
  String getServletName();
  
  ServletContext getServletContext();
  
  String getInitParameter(String paramString);
  
  Enumeration<String> getInitParameterNames();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */