package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface DataSource {
  InputStream getInputStream() throws IOException;
  
  OutputStream getOutputStream() throws IOException;
  
  String getContentType();
  
  String getName();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\DataSource.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */