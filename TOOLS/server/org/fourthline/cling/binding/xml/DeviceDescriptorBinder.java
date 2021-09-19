package org.fourthline.cling.binding.xml;

import org.fourthline.cling.model.Namespace;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.profile.RemoteClientInfo;
import org.w3c.dom.Document;

public interface DeviceDescriptorBinder {
  <T extends Device> T describe(T paramT, String paramString) throws DescriptorBindingException, ValidationException;
  
  <T extends Device> T describe(T paramT, Document paramDocument) throws DescriptorBindingException, ValidationException;
  
  String generate(Device paramDevice, RemoteClientInfo paramRemoteClientInfo, Namespace paramNamespace) throws DescriptorBindingException;
  
  Document buildDOM(Device paramDevice, RemoteClientInfo paramRemoteClientInfo, Namespace paramNamespace) throws DescriptorBindingException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\DeviceDescriptorBinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */