package org.fourthline.cling.model.message;

import java.net.InetAddress;

public interface Connection {
  boolean isOpen();
  
  InetAddress getRemoteAddress();
  
  InetAddress getLocalAddress();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\Connection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */