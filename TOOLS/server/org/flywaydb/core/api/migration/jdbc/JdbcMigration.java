package org.flywaydb.core.api.migration.jdbc;

import java.sql.Connection;

public interface JdbcMigration {
  void migrate(Connection paramConnection) throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\migration\jdbc\JdbcMigration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */