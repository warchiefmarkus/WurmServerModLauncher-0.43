package org.relaxng.datatype;

public interface DatatypeStreamingValidator {
  void addCharacters(char[] paramArrayOfchar, int paramInt1, int paramInt2);
  
  boolean isValid();
  
  void checkValid() throws DatatypeException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\relaxng\datatype\DatatypeStreamingValidator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */