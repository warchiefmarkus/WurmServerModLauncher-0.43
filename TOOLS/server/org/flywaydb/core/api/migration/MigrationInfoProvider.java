package org.flywaydb.core.api.migration;

import org.flywaydb.core.api.MigrationVersion;

public interface MigrationInfoProvider {
  MigrationVersion getVersion();
  
  String getDescription();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\migration\MigrationInfoProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */