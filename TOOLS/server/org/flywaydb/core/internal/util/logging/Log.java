package org.flywaydb.core.internal.util.logging;

public interface Log {
  void debug(String paramString);
  
  void info(String paramString);
  
  void warn(String paramString);
  
  void error(String paramString);
  
  void error(String paramString, Exception paramException);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\Log.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */