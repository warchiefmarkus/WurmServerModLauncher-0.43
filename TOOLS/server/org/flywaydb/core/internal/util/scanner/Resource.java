package org.flywaydb.core.internal.util.scanner;

public interface Resource {
  String getLocation();
  
  String getLocationOnDisk();
  
  String loadAsString(String paramString);
  
  byte[] loadAsBytes();
  
  String getFilename();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\scanner\Resource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */