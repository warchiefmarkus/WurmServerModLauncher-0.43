package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public interface ConnectionPropertiesTransform {
  Properties transformProperties(Properties paramProperties) throws SQLException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ConnectionPropertiesTransform.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */