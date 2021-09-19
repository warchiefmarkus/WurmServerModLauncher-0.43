package org.flywaydb.core.api.resolver;

import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;

public interface ResolvedMigration {
  MigrationVersion getVersion();
  
  String getDescription();
  
  String getScript();
  
  Integer getChecksum();
  
  MigrationType getType();
  
  String getPhysicalLocation();
  
  MigrationExecutor getExecutor();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\resolver\ResolvedMigration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */