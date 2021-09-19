package org.flywaydb.core.api;

public interface MigrationInfoService {
  MigrationInfo[] all();
  
  MigrationInfo current();
  
  MigrationInfo[] pending();
  
  MigrationInfo[] applied();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\MigrationInfoService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */