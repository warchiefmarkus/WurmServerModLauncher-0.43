package org.fourthline.cling.binding;

import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.ServiceId;
import org.fourthline.cling.model.types.ServiceType;

public interface LocalServiceBinder {
  LocalService read(Class<?> paramClass) throws LocalServiceBindingException;
  
  LocalService read(Class<?> paramClass, ServiceId paramServiceId, ServiceType paramServiceType, boolean paramBoolean, Class[] paramArrayOfClass) throws LocalServiceBindingException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\LocalServiceBinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */