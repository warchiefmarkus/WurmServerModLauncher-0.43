package org.relaxng.datatype;

public interface ValidationContext {
  String resolveNamespacePrefix(String paramString);
  
  String getBaseUri();
  
  boolean isUnparsedEntity(String paramString);
  
  boolean isNotation(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\relaxng\datatype\ValidationContext.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */