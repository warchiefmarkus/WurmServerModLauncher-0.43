package com.wurmonline.communication;

public interface ServerListener {
  void clientConnected(SocketConnection paramSocketConnection);
  
  void clientException(SocketConnection paramSocketConnection, Exception paramException);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\communication\ServerListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */