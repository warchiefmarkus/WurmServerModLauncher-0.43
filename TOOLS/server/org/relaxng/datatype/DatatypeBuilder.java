package org.relaxng.datatype;

public interface DatatypeBuilder {
  void addParameter(String paramString1, String paramString2, ValidationContext paramValidationContext) throws DatatypeException;
  
  Datatype createDatatype() throws DatatypeException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\relaxng\datatype\DatatypeBuilder.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */