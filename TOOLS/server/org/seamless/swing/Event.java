package org.seamless.swing;

public interface Event<PAYLOAD> {
  PAYLOAD getPayload();
  
  void addFiredInController(Controller paramController);
  
  boolean alreadyFired(Controller paramController);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\Event.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */