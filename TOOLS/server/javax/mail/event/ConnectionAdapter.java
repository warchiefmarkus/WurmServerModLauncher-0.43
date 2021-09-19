package javax.mail.event;

public abstract class ConnectionAdapter implements ConnectionListener {
  public void opened(ConnectionEvent e) {}
  
  public void disconnected(ConnectionEvent e) {}
  
  public void closed(ConnectionEvent e) {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\event\ConnectionAdapter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */