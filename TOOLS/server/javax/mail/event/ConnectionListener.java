package javax.mail.event;

import java.util.EventListener;

public interface ConnectionListener extends EventListener {
  void opened(ConnectionEvent paramConnectionEvent);
  
  void disconnected(ConnectionEvent paramConnectionEvent);
  
  void closed(ConnectionEvent paramConnectionEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\event\ConnectionListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */