package javax.mail.event;

import java.util.EventListener;

public interface TransportListener extends EventListener {
  void messageDelivered(TransportEvent paramTransportEvent);
  
  void messageNotDelivered(TransportEvent paramTransportEvent);
  
  void messagePartiallyDelivered(TransportEvent paramTransportEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\event\TransportListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */