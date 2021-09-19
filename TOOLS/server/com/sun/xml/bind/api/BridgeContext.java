package com.sun.xml.bind.api;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;

public abstract class BridgeContext {
  public abstract void setErrorHandler(ValidationEventHandler paramValidationEventHandler);
  
  public abstract void setAttachmentMarshaller(AttachmentMarshaller paramAttachmentMarshaller);
  
  public abstract void setAttachmentUnmarshaller(AttachmentUnmarshaller paramAttachmentUnmarshaller);
  
  public abstract AttachmentMarshaller getAttachmentMarshaller();
  
  public abstract AttachmentUnmarshaller getAttachmentUnmarshaller();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\BridgeContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */