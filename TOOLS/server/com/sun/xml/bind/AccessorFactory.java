package com.sun.xml.bind;

import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.xml.bind.JAXBException;

public interface AccessorFactory {
  Accessor createFieldAccessor(Class paramClass, Field paramField, boolean paramBoolean) throws JAXBException;
  
  Accessor createPropertyAccessor(Class paramClass, Method paramMethod1, Method paramMethod2) throws JAXBException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\AccessorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */