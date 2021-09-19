package org.fourthline.cling.transport.spi;

import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.control.ActionRequestMessage;
import org.fourthline.cling.model.message.control.ActionResponseMessage;

public interface SOAPActionProcessor {
  void writeBody(ActionRequestMessage paramActionRequestMessage, ActionInvocation paramActionInvocation) throws UnsupportedDataException;
  
  void writeBody(ActionResponseMessage paramActionResponseMessage, ActionInvocation paramActionInvocation) throws UnsupportedDataException;
  
  void readBody(ActionRequestMessage paramActionRequestMessage, ActionInvocation paramActionInvocation) throws UnsupportedDataException;
  
  void readBody(ActionResponseMessage paramActionResponseMessage, ActionInvocation paramActionInvocation) throws UnsupportedDataException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\SOAPActionProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */