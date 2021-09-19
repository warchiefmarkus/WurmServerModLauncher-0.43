/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public class RandomBalanceStrategy
/*     */   implements BalanceStrategy
/*     */ {
/*     */   public void destroy() {}
/*     */   
/*     */   public void init(Connection conn, Properties props) throws SQLException {}
/*     */   
/*     */   public Connection pickConnection(LoadBalancingConnectionProxy proxy, List configuredHosts, Map liveConnections, long[] responseTimes, int numRetries) throws SQLException {
/*  48 */     int numHosts = configuredHosts.size();
/*     */     
/*  50 */     SQLException ex = null;
/*     */     
/*  52 */     List whiteList = new ArrayList(numHosts);
/*  53 */     whiteList.addAll(configuredHosts);
/*     */     
/*  55 */     Map blackList = proxy.getGlobalBlacklist();
/*     */     
/*  57 */     whiteList.removeAll(blackList.keySet());
/*     */     
/*  59 */     Map whiteListMap = getArrayIndexMap(whiteList);
/*     */ 
/*     */     
/*  62 */     for (int attempts = 0; attempts < numRetries; ) {
/*  63 */       int random = (int)Math.floor(Math.random() * whiteList.size());
/*     */       
/*  65 */       String hostPortSpec = whiteList.get(random);
/*     */       
/*  67 */       Connection conn = (Connection)liveConnections.get(hostPortSpec);
/*     */       
/*  69 */       if (conn == null) {
/*     */         try {
/*  71 */           conn = proxy.createConnectionForHost(hostPortSpec);
/*  72 */         } catch (SQLException sqlEx) {
/*  73 */           ex = sqlEx;
/*     */           
/*  75 */           if (sqlEx instanceof CommunicationsException || "08S01".equals(sqlEx.getSQLState())) {
/*     */ 
/*     */             
/*  78 */             Integer whiteListIndex = (Integer)whiteListMap.get(hostPortSpec);
/*     */ 
/*     */ 
/*     */             
/*  82 */             if (whiteListIndex != null) {
/*  83 */               whiteList.remove(whiteListIndex.intValue());
/*  84 */               whiteListMap = getArrayIndexMap(whiteList);
/*     */             } 
/*  86 */             proxy.addToGlobalBlacklist(hostPortSpec);
/*     */             
/*  88 */             if (whiteList.size() == 0) {
/*  89 */               attempts++;
/*     */               try {
/*  91 */                 Thread.sleep(250L);
/*  92 */               } catch (InterruptedException e) {}
/*     */ 
/*     */ 
/*     */               
/*  96 */               whiteListMap = new HashMap(numHosts);
/*  97 */               whiteList.addAll(configuredHosts);
/*  98 */               blackList = proxy.getGlobalBlacklist();
/*     */               
/* 100 */               whiteList.removeAll(blackList.keySet());
/* 101 */               whiteListMap = getArrayIndexMap(whiteList);
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/* 106 */           throw sqlEx;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 111 */       return conn;
/*     */     } 
/*     */     
/* 114 */     if (ex != null) {
/* 115 */       throw ex;
/*     */     }
/*     */     
/* 118 */     return null;
/*     */   }
/*     */   
/*     */   private Map getArrayIndexMap(List l) {
/* 122 */     Map m = new HashMap(l.size());
/* 123 */     for (int i = 0; i < l.size(); i++) {
/* 124 */       m.put(l.get(i), new Integer(i));
/*     */     }
/* 126 */     return m;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\RandomBalanceStrategy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */