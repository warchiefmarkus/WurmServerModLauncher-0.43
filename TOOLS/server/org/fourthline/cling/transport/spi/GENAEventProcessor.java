package org.fourthline.cling.transport.spi;

import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.message.gena.IncomingEventRequestMessage;
import org.fourthline.cling.model.message.gena.OutgoingEventRequestMessage;

public interface GENAEventProcessor {
  void writeBody(OutgoingEventRequestMessage paramOutgoingEventRequestMessage) throws UnsupportedDataException;
  
  void readBody(IncomingEventRequestMessage paramIncomingEventRequestMessage) throws UnsupportedDataException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\GENAEventProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */