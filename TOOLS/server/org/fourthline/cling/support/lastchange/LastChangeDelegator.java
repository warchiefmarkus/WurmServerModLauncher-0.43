package org.fourthline.cling.support.lastchange;

import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;

public interface LastChangeDelegator {
  LastChange getLastChange();
  
  void appendCurrentState(LastChange paramLastChange, UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws Exception;
  
  UnsignedIntegerFourBytes[] getCurrentInstanceIds();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\LastChangeDelegator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */