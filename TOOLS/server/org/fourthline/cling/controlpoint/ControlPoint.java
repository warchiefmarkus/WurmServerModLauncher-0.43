package org.fourthline.cling.controlpoint;

import java.util.concurrent.Future;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.model.message.header.UpnpHeader;
import org.fourthline.cling.protocol.ProtocolFactory;
import org.fourthline.cling.registry.Registry;

public interface ControlPoint {
  UpnpServiceConfiguration getConfiguration();
  
  ProtocolFactory getProtocolFactory();
  
  Registry getRegistry();
  
  void search();
  
  void search(UpnpHeader paramUpnpHeader);
  
  void search(int paramInt);
  
  void search(UpnpHeader paramUpnpHeader, int paramInt);
  
  Future execute(ActionCallback paramActionCallback);
  
  void execute(SubscriptionCallback paramSubscriptionCallback);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\controlpoint\ControlPoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */