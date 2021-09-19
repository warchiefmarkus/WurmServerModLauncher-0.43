package org.flywaydb.core.api.configuration;

import java.util.Map;
import javax.sql.DataSource;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.flywaydb.core.api.resolver.MigrationResolver;

public interface FlywayConfiguration {
  ClassLoader getClassLoader();
  
  DataSource getDataSource();
  
  MigrationVersion getBaselineVersion();
  
  String getBaselineDescription();
  
  MigrationResolver[] getResolvers();
  
  boolean isSkipDefaultResolvers();
  
  FlywayCallback[] getCallbacks();
  
  boolean isSkipDefaultCallbacks();
  
  String getSqlMigrationSuffix();
  
  String getRepeatableSqlMigrationPrefix();
  
  String getSqlMigrationSeparator();
  
  String getSqlMigrationPrefix();
  
  boolean isPlaceholderReplacement();
  
  String getPlaceholderSuffix();
  
  String getPlaceholderPrefix();
  
  Map<String, String> getPlaceholders();
  
  MigrationVersion getTarget();
  
  String getTable();
  
  String[] getSchemas();
  
  String getEncoding();
  
  String[] getLocations();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\configuration\FlywayConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */