package org.fourthline.cling.support.avtransport.impl;

import java.net.URI;
import org.fourthline.cling.support.avtransport.impl.state.AbstractState;
import org.fourthline.cling.support.model.SeekMode;
import org.seamless.statemachine.StateMachine;

public interface AVTransportStateMachine extends StateMachine<AbstractState> {
  void setTransportURI(URI paramURI, String paramString);
  
  void setNextTransportURI(URI paramURI, String paramString);
  
  void stop();
  
  void play(String paramString);
  
  void pause();
  
  void record();
  
  void seek(SeekMode paramSeekMode, String paramString);
  
  void next();
  
  void previous();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\impl\AVTransportStateMachine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */