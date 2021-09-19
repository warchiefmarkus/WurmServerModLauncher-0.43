package org.relaxng.datatype.helpers;

import org.relaxng.datatype.Datatype;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.DatatypeStreamingValidator;
import org.relaxng.datatype.ValidationContext;

public final class StreamingValidatorImpl implements DatatypeStreamingValidator {
  private final StringBuffer buffer = new StringBuffer();
  
  private final Datatype baseType;
  
  private final ValidationContext context;
  
  public void addCharacters(char[] paramArrayOfchar, int paramInt1, int paramInt2) {
    this.buffer.append(paramArrayOfchar, paramInt1, paramInt2);
  }
  
  public boolean isValid() {
    return this.baseType.isValid(this.buffer.toString(), this.context);
  }
  
  public void checkValid() throws DatatypeException {
    this.baseType.checkValid(this.buffer.toString(), this.context);
  }
  
  public StreamingValidatorImpl(Datatype paramDatatype, ValidationContext paramValidationContext) {
    this.baseType = paramDatatype;
    this.context = paramValidationContext;
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\relaxng\datatype\helpers\StreamingValidatorImpl.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */