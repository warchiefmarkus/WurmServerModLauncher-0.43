package javax.servlet.http;

import java.util.Enumeration;

public interface HttpSessionContext {
  HttpSession getSession(String paramString);
  
  Enumeration<String> getIds();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpSessionContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */