package org.fourthline.cling.binding.xml;

import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.Service;
import org.w3c.dom.Document;

public interface ServiceDescriptorBinder {
  <T extends Service> T describe(T paramT, String paramString) throws DescriptorBindingException, ValidationException;
  
  <T extends Service> T describe(T paramT, Document paramDocument) throws DescriptorBindingException, ValidationException;
  
  String generate(Service paramService) throws DescriptorBindingException;
  
  Document buildDOM(Service paramService) throws DescriptorBindingException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\xml\ServiceDescriptorBinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */