package org.flywaydb.core.api.callback;

import java.sql.Connection;
import org.flywaydb.core.api.MigrationInfo;

public interface FlywayCallback {
  void beforeClean(Connection paramConnection);
  
  void afterClean(Connection paramConnection);
  
  void beforeMigrate(Connection paramConnection);
  
  void afterMigrate(Connection paramConnection);
  
  void beforeEachMigrate(Connection paramConnection, MigrationInfo paramMigrationInfo);
  
  void afterEachMigrate(Connection paramConnection, MigrationInfo paramMigrationInfo);
  
  void beforeValidate(Connection paramConnection);
  
  void afterValidate(Connection paramConnection);
  
  void beforeBaseline(Connection paramConnection);
  
  void afterBaseline(Connection paramConnection);
  
  void beforeRepair(Connection paramConnection);
  
  void afterRepair(Connection paramConnection);
  
  void beforeInfo(Connection paramConnection);
  
  void afterInfo(Connection paramConnection);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\callback\FlywayCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */