package org.fourthline.cling;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.protocol.ProtocolFactory;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.transport.Router;

public interface UpnpService {
  UpnpServiceConfiguration getConfiguration();
  
  ControlPoint getControlPoint();
  
  ProtocolFactory getProtocolFactory();
  
  Registry getRegistry();
  
  Router getRouter();
  
  void shutdown();
  
  public static class Start {}
  
  public static class Shutdown {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\UpnpService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */