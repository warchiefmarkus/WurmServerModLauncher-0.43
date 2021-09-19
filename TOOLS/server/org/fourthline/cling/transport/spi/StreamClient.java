package org.fourthline.cling.transport.spi;

import org.fourthline.cling.model.message.StreamRequestMessage;
import org.fourthline.cling.model.message.StreamResponseMessage;

public interface StreamClient<C extends StreamClientConfiguration> {
  StreamResponseMessage sendRequest(StreamRequestMessage paramStreamRequestMessage) throws InterruptedException;
  
  void stop();
  
  C getConfiguration();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\StreamClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */