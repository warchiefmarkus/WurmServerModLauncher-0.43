package javax.servlet.http;

import java.util.Enumeration;
import javax.servlet.ServletContext;

public interface HttpSession {
  long getCreationTime();
  
  String getId();
  
  long getLastAccessedTime();
  
  ServletContext getServletContext();
  
  void setMaxInactiveInterval(int paramInt);
  
  int getMaxInactiveInterval();
  
  HttpSessionContext getSessionContext();
  
  Object getAttribute(String paramString);
  
  Object getValue(String paramString);
  
  Enumeration<String> getAttributeNames();
  
  String[] getValueNames();
  
  void setAttribute(String paramString, Object paramObject);
  
  void putValue(String paramString, Object paramObject);
  
  void removeAttribute(String paramString);
  
  void removeValue(String paramString);
  
  void invalidate();
  
  boolean isNew();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */