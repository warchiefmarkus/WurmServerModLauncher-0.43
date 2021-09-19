package javax.servlet.http;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

public interface HttpServletRequest extends ServletRequest {
  public static final String BASIC_AUTH = "BASIC";
  
  public static final String FORM_AUTH = "FORM";
  
  public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";
  
  public static final String DIGEST_AUTH = "DIGEST";
  
  String getAuthType();
  
  Cookie[] getCookies();
  
  long getDateHeader(String paramString);
  
  String getHeader(String paramString);
  
  Enumeration<String> getHeaders(String paramString);
  
  Enumeration<String> getHeaderNames();
  
  int getIntHeader(String paramString);
  
  String getMethod();
  
  String getPathInfo();
  
  String getPathTranslated();
  
  String getContextPath();
  
  String getQueryString();
  
  String getRemoteUser();
  
  boolean isUserInRole(String paramString);
  
  Principal getUserPrincipal();
  
  String getRequestedSessionId();
  
  String getRequestURI();
  
  StringBuffer getRequestURL();
  
  String getServletPath();
  
  HttpSession getSession(boolean paramBoolean);
  
  HttpSession getSession();
  
  boolean isRequestedSessionIdValid();
  
  boolean isRequestedSessionIdFromCookie();
  
  boolean isRequestedSessionIdFromURL();
  
  boolean isRequestedSessionIdFromUrl();
  
  boolean authenticate(HttpServletResponse paramHttpServletResponse) throws IOException, ServletException;
  
  void login(String paramString1, String paramString2) throws ServletException;
  
  void logout() throws ServletException;
  
  Collection<Part> getParts() throws IOException, ServletException;
  
  Part getPart(String paramString) throws IOException, ServletException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpServletRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */