package org.fourthline.cling.android;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.registry.Registry;

public interface AndroidUpnpService {
  UpnpService get();
  
  UpnpServiceConfiguration getConfiguration();
  
  Registry getRegistry();
  
  ControlPoint getControlPoint();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\android\AndroidUpnpService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */