package javax.servlet;

import java.io.IOException;

public interface FilterChain {
  void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse) throws IOException, ServletException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\FilterChain.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */