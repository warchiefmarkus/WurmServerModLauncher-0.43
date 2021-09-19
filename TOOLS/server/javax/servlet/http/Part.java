package javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface Part {
  InputStream getInputStream() throws IOException;
  
  String getContentType();
  
  String getName();
  
  long getSize();
  
  void write(String paramString) throws IOException;
  
  void delete() throws IOException;
  
  String getHeader(String paramString);
  
  Collection<String> getHeaders(String paramString);
  
  Collection<String> getHeaderNames();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\Part.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */