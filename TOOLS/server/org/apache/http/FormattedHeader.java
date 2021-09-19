package org.apache.http;

import org.apache.http.util.CharArrayBuffer;

public interface FormattedHeader extends Header {
  CharArrayBuffer getBuffer();
  
  int getValuePos();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\FormattedHeader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */