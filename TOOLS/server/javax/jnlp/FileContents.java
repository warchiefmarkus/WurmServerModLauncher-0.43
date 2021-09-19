package javax.jnlp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileContents {
  String getName() throws IOException;
  
  InputStream getInputStream() throws IOException;
  
  OutputStream getOutputStream(boolean paramBoolean) throws IOException;
  
  long getLength() throws IOException;
  
  boolean canRead() throws IOException;
  
  boolean canWrite() throws IOException;
  
  JNLPRandomAccessFile getRandomAccessFile(String paramString) throws IOException;
  
  long getMaxLength() throws IOException;
  
  long setMaxLength(long paramLong) throws IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\FileContents.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */