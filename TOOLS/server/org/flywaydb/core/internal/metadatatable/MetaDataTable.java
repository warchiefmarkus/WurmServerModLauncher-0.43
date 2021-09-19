package org.flywaydb.core.internal.metadatatable;

import java.util.List;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.internal.dbsupport.Schema;

public interface MetaDataTable {
  void lock();
  
  void addAppliedMigration(AppliedMigration paramAppliedMigration);
  
  boolean hasAppliedMigrations();
  
  List<AppliedMigration> allAppliedMigrations();
  
  void addBaselineMarker(MigrationVersion paramMigrationVersion, String paramString);
  
  boolean hasBaselineMarker();
  
  AppliedMigration getBaselineMarker();
  
  void removeFailedMigrations();
  
  void addSchemasMarker(Schema[] paramArrayOfSchema);
  
  boolean hasSchemasMarker();
  
  void updateChecksum(MigrationVersion paramMigrationVersion, Integer paramInteger);
  
  boolean upgradeIfNecessary();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\metadatatable\MetaDataTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */