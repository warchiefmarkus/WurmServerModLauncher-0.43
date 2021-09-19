package javax.servlet;

import java.io.IOException;

public interface Servlet {
  void init(ServletConfig paramServletConfig) throws ServletException;
  
  ServletConfig getServletConfig();
  
  void service(ServletRequest paramServletRequest, ServletResponse paramServletResponse) throws ServletException, IOException;
  
  String getServletInfo();
  
  void destroy();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\Servlet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */