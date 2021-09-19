package org.flywaydb.core.api;

import java.util.Date;

public interface MigrationInfo extends Comparable<MigrationInfo> {
  MigrationType getType();
  
  Integer getChecksum();
  
  MigrationVersion getVersion();
  
  String getDescription();
  
  String getScript();
  
  MigrationState getState();
  
  Date getInstalledOn();
  
  String getInstalledBy();
  
  Integer getInstalledRank();
  
  Integer getExecutionTime();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\MigrationInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */