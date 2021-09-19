/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BestResponseTimeBalanceStrategy
/*     */   implements BalanceStrategy
/*     */ {
/*     */   public void destroy() {}
/*     */   
/*     */   public void init(Connection conn, Properties props) throws SQLException {}
/*     */   
/*     */   public Connection pickConnection(LoadBalancingConnectionProxy proxy, List configuredHosts, Map liveConnections, long[] responseTimes, int numRetries) throws SQLException {
/*  53 */     Map blackList = proxy.getGlobalBlacklist();
/*     */     
/*  55 */     SQLException ex = null;
/*     */     
/*  57 */     for (int attempts = 0; attempts < numRetries; ) {
/*  58 */       long minResponseTime = Long.MAX_VALUE;
/*     */       
/*  60 */       int bestHostIndex = 0;
/*     */ 
/*     */       
/*  63 */       if (blackList.size() == configuredHosts.size()) {
/*  64 */         blackList = proxy.getGlobalBlacklist();
/*     */       }
/*     */       
/*  67 */       for (int i = 0; i < responseTimes.length; i++) {
/*  68 */         long candidateResponseTime = responseTimes[i];
/*     */         
/*  70 */         if (candidateResponseTime < minResponseTime && !blackList.containsKey(configuredHosts.get(i))) {
/*     */           
/*  72 */           if (candidateResponseTime == 0L) {
/*  73 */             bestHostIndex = i;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/*  78 */           bestHostIndex = i;
/*  79 */           minResponseTime = candidateResponseTime;
/*     */         } 
/*     */       } 
/*     */       
/*  83 */       String bestHost = configuredHosts.get(bestHostIndex);
/*     */       
/*  85 */       Connection conn = (Connection)liveConnections.get(bestHost);
/*     */       
/*  87 */       if (conn == null) {
/*     */         try {
/*  89 */           conn = proxy.createConnectionForHost(bestHost);
/*  90 */         } catch (SQLException sqlEx) {
/*  91 */           ex = sqlEx;
/*     */           
/*  93 */           if (sqlEx instanceof CommunicationsException || "08S01".equals(sqlEx.getSQLState())) {
/*     */             
/*  95 */             proxy.addToGlobalBlacklist(bestHost);
/*  96 */             blackList.put(bestHost, null);
/*     */ 
/*     */             
/*  99 */             if (blackList.size() == configuredHosts.size()) {
/* 100 */               attempts++;
/*     */               try {
/* 102 */                 Thread.sleep(250L);
/* 103 */               } catch (InterruptedException e) {}
/*     */               
/* 105 */               blackList = proxy.getGlobalBlacklist();
/*     */             } 
/*     */             
/*     */             continue;
/*     */           } 
/* 110 */           throw sqlEx;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 115 */       return conn;
/*     */     } 
/*     */     
/* 118 */     if (ex != null) {
/* 119 */       throw ex;
/*     */     }
/*     */     
/* 122 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\BestResponseTimeBalanceStrategy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */