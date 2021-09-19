package org.flywaydb.core.internal.util.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
  T mapRow(ResultSet paramResultSet) throws SQLException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\jdbc\RowMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */