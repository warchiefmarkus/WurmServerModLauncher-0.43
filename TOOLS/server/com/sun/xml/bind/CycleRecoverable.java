package com.sun.xml.bind;

import javax.xml.bind.Marshaller;

public interface CycleRecoverable {
  Object onCycleDetected(Context paramContext);
  
  public static interface Context {
    Marshaller getMarshaller();
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\CycleRecoverable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */