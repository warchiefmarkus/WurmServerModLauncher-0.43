package org.relaxng.datatype;

public interface DatatypeLibrary {
  DatatypeBuilder createDatatypeBuilder(String paramString) throws DatatypeException;
  
  Datatype createDatatype(String paramString) throws DatatypeException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\relaxng\datatype\DatatypeLibrary.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */