package com.wurmonline.server.intra;

import java.nio.ByteBuffer;

public interface IntraServerConnectionListener {
  void reschedule(IntraClient paramIntraClient);
  
  void remove(IntraClient paramIntraClient);
  
  void commandExecuted(IntraClient paramIntraClient);
  
  void commandFailed(IntraClient paramIntraClient);
  
  void dataReceived(IntraClient paramIntraClient);
  
  void receivingData(ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\intra\IntraServerConnectionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */