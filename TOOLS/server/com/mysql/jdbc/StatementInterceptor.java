package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public interface StatementInterceptor extends Extension {
  void init(Connection paramConnection, Properties paramProperties) throws SQLException;
  
  ResultSetInternalMethods preProcess(String paramString, Statement paramStatement, Connection paramConnection) throws SQLException;
  
  ResultSetInternalMethods postProcess(String paramString, Statement paramStatement, ResultSetInternalMethods paramResultSetInternalMethods, Connection paramConnection) throws SQLException;
  
  boolean executeTopLevelOnly();
  
  void destroy();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\StatementInterceptor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */