package com.mysql.jdbc;

import java.sql.SQLException;

public interface ExceptionInterceptor extends Extension {
  SQLException interceptException(SQLException paramSQLException);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ExceptionInterceptor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */