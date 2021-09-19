package org.seamless.swing;

public interface DefaultEventListener<PAYLOAD> extends EventListener<DefaultEvent<PAYLOAD>> {
  void handleEvent(DefaultEvent<PAYLOAD> paramDefaultEvent);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\DefaultEventListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */