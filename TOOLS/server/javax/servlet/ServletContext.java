package javax.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.descriptor.JspConfigDescriptor;

public interface ServletContext {
  public static final String TEMPDIR = "javax.servlet.context.tempdir";
  
  public static final String ORDERED_LIBS = "javax.servlet.context.orderedLibs";
  
  String getContextPath();
  
  ServletContext getContext(String paramString);
  
  int getMajorVersion();
  
  int getMinorVersion();
  
  int getEffectiveMajorVersion();
  
  int getEffectiveMinorVersion();
  
  String getMimeType(String paramString);
  
  Set<String> getResourcePaths(String paramString);
  
  URL getResource(String paramString) throws MalformedURLException;
  
  InputStream getResourceAsStream(String paramString);
  
  RequestDispatcher getRequestDispatcher(String paramString);
  
  RequestDispatcher getNamedDispatcher(String paramString);
  
  Servlet getServlet(String paramString) throws ServletException;
  
  Enumeration<Servlet> getServlets();
  
  Enumeration<String> getServletNames();
  
  void log(String paramString);
  
  void log(Exception paramException, String paramString);
  
  void log(String paramString, Throwable paramThrowable);
  
  String getRealPath(String paramString);
  
  String getServerInfo();
  
  String getInitParameter(String paramString);
  
  Enumeration<String> getInitParameterNames();
  
  boolean setInitParameter(String paramString1, String paramString2);
  
  Object getAttribute(String paramString);
  
  Enumeration<String> getAttributeNames();
  
  void setAttribute(String paramString, Object paramObject);
  
  void removeAttribute(String paramString);
  
  String getServletContextName();
  
  ServletRegistration.Dynamic addServlet(String paramString1, String paramString2);
  
  ServletRegistration.Dynamic addServlet(String paramString, Servlet paramServlet);
  
  ServletRegistration.Dynamic addServlet(String paramString, Class<? extends Servlet> paramClass);
  
  <T extends Servlet> T createServlet(Class<T> paramClass) throws ServletException;
  
  ServletRegistration getServletRegistration(String paramString);
  
  Map<String, ? extends ServletRegistration> getServletRegistrations();
  
  FilterRegistration.Dynamic addFilter(String paramString1, String paramString2);
  
  FilterRegistration.Dynamic addFilter(String paramString, Filter paramFilter);
  
  FilterRegistration.Dynamic addFilter(String paramString, Class<? extends Filter> paramClass);
  
  <T extends Filter> T createFilter(Class<T> paramClass) throws ServletException;
  
  FilterRegistration getFilterRegistration(String paramString);
  
  Map<String, ? extends FilterRegistration> getFilterRegistrations();
  
  SessionCookieConfig getSessionCookieConfig();
  
  void setSessionTrackingModes(Set<SessionTrackingMode> paramSet);
  
  Set<SessionTrackingMode> getDefaultSessionTrackingModes();
  
  Set<SessionTrackingMode> getEffectiveSessionTrackingModes();
  
  void addListener(String paramString);
  
  <T extends EventListener> void addListener(T paramT);
  
  void addListener(Class<? extends EventListener> paramClass);
  
  <T extends EventListener> T createListener(Class<T> paramClass) throws ServletException;
  
  JspConfigDescriptor getJspConfigDescriptor();
  
  ClassLoader getClassLoader();
  
  void declareRoles(String... paramVarArgs);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */