package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BalanceStrategy extends Extension {
  Connection pickConnection(LoadBalancingConnectionProxy paramLoadBalancingConnectionProxy, List paramList, Map paramMap, long[] paramArrayOflong, int paramInt) throws SQLException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\BalanceStrategy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */