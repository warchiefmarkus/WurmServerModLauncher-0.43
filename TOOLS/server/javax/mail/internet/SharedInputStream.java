package javax.mail.internet;

import java.io.InputStream;

public interface SharedInputStream {
  long getPosition();
  
  InputStream newStream(long paramLong1, long paramLong2);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\internet\SharedInputStream.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */