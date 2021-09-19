/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadBalancingConnectionProxy
/*     */   implements InvocationHandler, PingTarget
/*     */ {
/*     */   private static Method getLocalTimeMethod;
/*     */   public static final String BLACKLIST_TIMEOUT_PROPERTY_KEY = "loadBalanceBlacklistTimeout";
/*     */   private Connection currentConn;
/*     */   private List hostList;
/*     */   private Map liveConnections;
/*     */   private Map connectionsToHostsMap;
/*     */   private long[] responseTimes;
/*     */   private Map hostsToListIndexMap;
/*     */   
/*     */   static {
/*     */     try {
/*  68 */       getLocalTimeMethod = System.class.getMethod("nanoTime", new Class[0]);
/*     */     }
/*  70 */     catch (SecurityException e) {
/*     */     
/*  72 */     } catch (NoSuchMethodException e) {}
/*     */   }
/*     */ 
/*     */   
/*     */   protected class ConnectionErrorFiringInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     Object invokeOn;
/*     */     private final LoadBalancingConnectionProxy this$0;
/*     */     
/*     */     public ConnectionErrorFiringInvocationHandler(LoadBalancingConnectionProxy this$0, Object toInvokeOn) {
/*  83 */       this.this$0 = this$0; this.invokeOn = null;
/*  84 */       this.invokeOn = toInvokeOn;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  89 */       Object result = null;
/*     */       
/*     */       try {
/*  92 */         result = method.invoke(this.invokeOn, args);
/*     */         
/*  94 */         if (result != null) {
/*  95 */           result = this.this$0.proxyIfInterfaceIsJdbc(result, result.getClass());
/*     */         }
/*  97 */       } catch (InvocationTargetException e) {
/*  98 */         this.this$0.dealWithInvocationException(e);
/*     */       } 
/*     */       
/* 101 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inTransaction = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   private long transactionStartTime = 0L;
/*     */   
/*     */   private Properties localProps;
/*     */   
/*     */   private boolean isClosed = false;
/*     */   
/*     */   private BalanceStrategy balancer;
/*     */   
/*     */   private int retriesAllDown;
/*     */   
/* 129 */   private static Map globalBlacklist = new HashMap();
/*     */   
/* 131 */   private int globalBlacklistTimeout = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LoadBalancingConnectionProxy(List hosts, Properties props) throws SQLException {
/* 144 */     this.hostList = hosts;
/*     */     
/* 146 */     int numHosts = this.hostList.size();
/*     */     
/* 148 */     this.liveConnections = new HashMap(numHosts);
/* 149 */     this.connectionsToHostsMap = new HashMap(numHosts);
/* 150 */     this.responseTimes = new long[numHosts];
/* 151 */     this.hostsToListIndexMap = new HashMap(numHosts);
/*     */     
/* 153 */     for (int i = 0; i < numHosts; i++) {
/* 154 */       this.hostsToListIndexMap.put(this.hostList.get(i), new Integer(i));
/*     */     }
/*     */     
/* 157 */     this.localProps = (Properties)props.clone();
/* 158 */     this.localProps.remove("HOST");
/* 159 */     this.localProps.remove("PORT");
/* 160 */     this.localProps.setProperty("useLocalSessionState", "true");
/*     */     
/* 162 */     String strategy = this.localProps.getProperty("loadBalanceStrategy", "random");
/*     */ 
/*     */     
/* 165 */     String retriesAllDownAsString = this.localProps.getProperty("retriesAllDown", "120");
/*     */     
/*     */     try {
/* 168 */       this.retriesAllDown = Integer.parseInt(retriesAllDownAsString);
/* 169 */     } catch (NumberFormatException nfe) {
/* 170 */       throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForRetriesAllDown", new Object[] { retriesAllDownAsString }), "S1009", null);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 175 */     String blacklistTimeoutAsString = this.localProps.getProperty("loadBalanceBlacklistTimeout", "0");
/*     */     
/*     */     try {
/* 178 */       this.globalBlacklistTimeout = Integer.parseInt(blacklistTimeoutAsString);
/* 179 */     } catch (NumberFormatException nfe) {
/* 180 */       throw SQLError.createSQLException(Messages.getString("LoadBalancingConnectionProxy.badValueForLoadBalanceBlacklistTimeout", new Object[] { retriesAllDownAsString }), "S1009", null);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     if ("random".equals(strategy)) {
/* 187 */       this.balancer = Util.loadExtensions(null, props, "com.mysql.jdbc.RandomBalanceStrategy", "InvalidLoadBalanceStrategy", null).get(0);
/*     */     
/*     */     }
/* 190 */     else if ("bestResponseTime".equals(strategy)) {
/* 191 */       this.balancer = Util.loadExtensions(null, props, "com.mysql.jdbc.BestResponseTimeBalanceStrategy", "InvalidLoadBalanceStrategy", null).get(0);
/*     */     }
/*     */     else {
/*     */       
/* 195 */       this.balancer = Util.loadExtensions(null, props, strategy, "InvalidLoadBalanceStrategy", null).get(0);
/*     */     } 
/*     */ 
/*     */     
/* 199 */     this.balancer.init(null, props);
/*     */     
/* 201 */     pickNewConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Connection createConnectionForHost(String hostPortSpec) throws SQLException {
/* 214 */     Properties connProps = (Properties)this.localProps.clone();
/*     */     
/* 216 */     String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(hostPortSpec);
/*     */ 
/*     */     
/* 219 */     if (hostPortPair[1] == null) {
/* 220 */       hostPortPair[1] = "3306";
/*     */     }
/*     */     
/* 223 */     connProps.setProperty("HOST", hostPortSpec);
/*     */     
/* 225 */     connProps.setProperty("PORT", hostPortPair[1]);
/*     */ 
/*     */     
/* 228 */     Connection conn = ConnectionImpl.getInstance(hostPortSpec, Integer.parseInt(hostPortPair[1]), connProps, connProps.getProperty("DBNAME"), "jdbc:mysql://" + hostPortPair[0] + ":" + hostPortPair[1] + "/");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     this.liveConnections.put(hostPortSpec, conn);
/* 234 */     this.connectionsToHostsMap.put(conn, hostPortSpec);
/*     */     
/* 236 */     return conn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void dealWithInvocationException(InvocationTargetException e) throws SQLException, Throwable, InvocationTargetException {
/* 247 */     Throwable t = e.getTargetException();
/*     */     
/* 249 */     if (t != null) {
/* 250 */       if (t instanceof SQLException) {
/* 251 */         String sqlState = ((SQLException)t).getSQLState();
/*     */         
/* 253 */         if (sqlState != null && 
/* 254 */           sqlState.startsWith("08"))
/*     */         {
/*     */           
/* 257 */           invalidateCurrentConnection();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 262 */       throw t;
/*     */     } 
/*     */     
/* 265 */     throw e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void invalidateCurrentConnection() throws SQLException {
/*     */     try {
/* 275 */       if (!this.currentConn.isClosed()) {
/* 276 */         this.currentConn.close();
/*     */       }
/*     */     }
/*     */     finally {
/*     */       
/* 281 */       if (isGlobalBlacklistEnabled()) {
/* 282 */         addToGlobalBlacklist((String)this.connectionsToHostsMap.get(this.currentConn));
/*     */       }
/*     */ 
/*     */       
/* 286 */       this.liveConnections.remove(this.connectionsToHostsMap.get(this.currentConn));
/*     */ 
/*     */       
/* 289 */       int hostIndex = ((Integer)this.hostsToListIndexMap.get(this.connectionsToHostsMap.get(this.currentConn))).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 294 */       synchronized (this.responseTimes) {
/* 295 */         this.responseTimes[hostIndex] = 0L;
/*     */       } 
/*     */       
/* 298 */       this.connectionsToHostsMap.remove(this.currentConn);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 310 */     String methodName = method.getName();
/*     */     
/* 312 */     if ("equals".equals(methodName) && args.length == 1) {
/* 313 */       if (args[0] instanceof Proxy) {
/* 314 */         return Boolean.valueOf(((Proxy)args[0]).equals(this));
/*     */       }
/*     */       
/* 317 */       return Boolean.valueOf(equals(args[0]));
/*     */     } 
/*     */     
/* 320 */     if ("close".equals(methodName)) {
/* 321 */       synchronized (this) {
/*     */         
/* 323 */         Iterator allConnections = this.liveConnections.values().iterator();
/*     */ 
/*     */         
/* 326 */         while (allConnections.hasNext()) {
/* 327 */           ((Connection)allConnections.next()).close();
/*     */         }
/*     */         
/* 330 */         if (!this.isClosed) {
/* 331 */           this.balancer.destroy();
/*     */         }
/*     */         
/* 334 */         this.liveConnections.clear();
/* 335 */         this.connectionsToHostsMap.clear();
/*     */       } 
/*     */       
/* 338 */       return null;
/*     */     } 
/*     */     
/* 341 */     if ("isClosed".equals(methodName)) {
/* 342 */       return Boolean.valueOf(this.isClosed);
/*     */     }
/*     */     
/* 345 */     if (this.isClosed) {
/* 346 */       throw SQLError.createSQLException("No operations allowed after connection closed.", "08003", null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 351 */     if (!this.inTransaction) {
/* 352 */       this.inTransaction = true;
/* 353 */       this.transactionStartTime = getLocalTimeBestResolution();
/*     */     } 
/*     */     
/* 356 */     Object result = null;
/*     */     
/*     */     try {
/* 359 */       result = method.invoke(this.currentConn, args);
/*     */       
/* 361 */       if (result != null) {
/* 362 */         if (result instanceof Statement) {
/* 363 */           ((Statement)result).setPingTarget(this);
/*     */         }
/*     */         
/* 366 */         result = proxyIfInterfaceIsJdbc(result, result.getClass());
/*     */       } 
/* 368 */     } catch (InvocationTargetException e) {
/* 369 */       dealWithInvocationException(e);
/*     */     } finally {
/* 371 */       if ("commit".equals(methodName) || "rollback".equals(methodName)) {
/* 372 */         this.inTransaction = false;
/*     */ 
/*     */         
/* 375 */         String host = (String)this.connectionsToHostsMap.get(this.currentConn);
/*     */ 
/*     */         
/* 378 */         if (host != null) {
/* 379 */           int hostIndex = ((Integer)this.hostsToListIndexMap.get(host)).intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 384 */           synchronized (this.responseTimes) {
/* 385 */             this.responseTimes[hostIndex] = getLocalTimeBestResolution() - this.transactionStartTime;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 390 */         pickNewConnection();
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void pickNewConnection() throws SQLException {
/* 404 */     if (this.currentConn == null) {
/* 405 */       this.currentConn = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[])this.responseTimes.clone(), this.retriesAllDown);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 414 */     Connection newConn = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[])this.responseTimes.clone(), this.retriesAllDown);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 420 */     newConn.setTransactionIsolation(this.currentConn.getTransactionIsolation());
/*     */     
/* 422 */     newConn.setAutoCommit(this.currentConn.getAutoCommit());
/* 423 */     this.currentConn = newConn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object proxyIfInterfaceIsJdbc(Object toProxy, Class clazz) {
/* 436 */     Class[] interfaces = clazz.getInterfaces();
/*     */     
/* 438 */     int i = 0; if (i < interfaces.length) {
/* 439 */       String packageName = interfaces[i].getPackage().getName();
/*     */       
/* 441 */       if ("java.sql".equals(packageName) || "javax.sql".equals(packageName))
/*     */       {
/* 443 */         return Proxy.newProxyInstance(toProxy.getClass().getClassLoader(), interfaces, new ConnectionErrorFiringInvocationHandler(this, toProxy));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 448 */       return proxyIfInterfaceIsJdbc(toProxy, interfaces[i]);
/*     */     } 
/*     */     
/* 451 */     return toProxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long getLocalTimeBestResolution() {
/* 459 */     if (getLocalTimeMethod != null) {
/*     */       try {
/* 461 */         return ((Long)getLocalTimeMethod.invoke(null, null)).longValue();
/*     */       }
/* 463 */       catch (IllegalArgumentException e) {
/*     */       
/* 465 */       } catch (IllegalAccessException e) {
/*     */       
/* 467 */       } catch (InvocationTargetException e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 472 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public synchronized void doPing() throws SQLException {
/* 476 */     if (isGlobalBlacklistEnabled()) {
/* 477 */       SQLException se = null;
/* 478 */       boolean foundHost = false;
/* 479 */       synchronized (this) {
/* 480 */         for (Iterator i = this.hostList.iterator(); i.hasNext(); ) {
/* 481 */           String host = i.next();
/* 482 */           Connection conn = (Connection)this.liveConnections.get(host);
/* 483 */           if (conn == null) {
/*     */             continue;
/*     */           }
/*     */           try {
/* 487 */             conn.ping();
/* 488 */             foundHost = true;
/* 489 */           } catch (SQLException e) {
/* 490 */             se = e;
/* 491 */             addToGlobalBlacklist(host);
/*     */           } 
/*     */         } 
/*     */       } 
/* 495 */       if (!foundHost) {
/* 496 */         throw se;
/*     */       }
/*     */     } else {
/* 499 */       Iterator allConns = this.liveConnections.values().iterator();
/* 500 */       while (allConns.hasNext()) {
/* 501 */         ((Connection)allConns.next()).ping();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToGlobalBlacklist(String host) {
/* 508 */     if (isGlobalBlacklistEnabled()) {
/* 509 */       synchronized (globalBlacklist) {
/* 510 */         globalBlacklist.put(host, new Long(System.currentTimeMillis() + this.globalBlacklistTimeout));
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGlobalBlacklistEnabled() {
/* 517 */     return (this.globalBlacklistTimeout > 0);
/*     */   }
/*     */   
/*     */   public Map getGlobalBlacklist() {
/* 521 */     if (!isGlobalBlacklistEnabled()) {
/* 522 */       return new HashMap(1);
/*     */     }
/*     */ 
/*     */     
/* 526 */     Map blacklistClone = new HashMap(globalBlacklist.size());
/*     */     
/* 528 */     synchronized (globalBlacklist) {
/* 529 */       blacklistClone.putAll(globalBlacklist);
/*     */     } 
/* 531 */     Set keys = blacklistClone.keySet();
/*     */ 
/*     */     
/* 534 */     keys.retainAll(this.hostList);
/* 535 */     if (keys.size() == this.hostList.size())
/*     */     {
/*     */ 
/*     */       
/* 539 */       return new HashMap(1);
/*     */     }
/*     */ 
/*     */     
/* 543 */     for (Iterator i = keys.iterator(); i.hasNext(); ) {
/* 544 */       String host = i.next();
/*     */       
/* 546 */       Long timeout = (Long)globalBlacklist.get(host);
/* 547 */       if (timeout != null && timeout.longValue() < System.currentTimeMillis()) {
/*     */         
/* 549 */         synchronized (globalBlacklist) {
/* 550 */           globalBlacklist.remove(host);
/*     */         } 
/* 552 */         i.remove();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 557 */     return blacklistClone;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\LoadBalancingConnectionProxy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */