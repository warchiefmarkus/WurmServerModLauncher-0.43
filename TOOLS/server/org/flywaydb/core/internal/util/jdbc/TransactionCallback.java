package org.flywaydb.core.internal.util.jdbc;

import java.sql.SQLException;

public interface TransactionCallback<T> {
  T doInTransaction() throws SQLException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\jdbc\TransactionCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */