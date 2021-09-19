package javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public interface ServletResponse {
  String getCharacterEncoding();
  
  String getContentType();
  
  ServletOutputStream getOutputStream() throws IOException;
  
  PrintWriter getWriter() throws IOException;
  
  void setCharacterEncoding(String paramString);
  
  void setContentLength(int paramInt);
  
  void setContentType(String paramString);
  
  void setBufferSize(int paramInt);
  
  int getBufferSize();
  
  void flushBuffer() throws IOException;
  
  void resetBuffer();
  
  boolean isCommitted();
  
  void reset();
  
  void setLocale(Locale paramLocale);
  
  Locale getLocale();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\ServletResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */