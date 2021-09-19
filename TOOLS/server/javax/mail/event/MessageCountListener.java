package javax.mail.event;

import java.util.EventListener;

public interface MessageCountListener extends EventListener {
  void messagesAdded(MessageCountEvent paramMessageCountEvent);
  
  void messagesRemoved(MessageCountEvent paramMessageCountEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\event\MessageCountListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */