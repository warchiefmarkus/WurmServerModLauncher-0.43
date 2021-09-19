package org.flywaydb.core.api.migration.spring;

import org.springframework.jdbc.core.JdbcTemplate;

public interface SpringJdbcMigration {
  void migrate(JdbcTemplate paramJdbcTemplate) throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\migration\spring\SpringJdbcMigration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */