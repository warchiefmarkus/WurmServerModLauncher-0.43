package javax.servlet;

import java.io.IOException;

public interface Filter {
  void init(FilterConfig paramFilterConfig) throws ServletException;
  
  void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain) throws IOException, ServletException;
  
  void destroy();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\Filter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */