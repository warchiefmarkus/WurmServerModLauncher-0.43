/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectionFeatureNotAvailableException
/*    */   extends CommunicationsException
/*    */ {
/*    */   public ConnectionFeatureNotAvailableException(ConnectionImpl conn, long lastPacketSentTimeMs, Exception underlyingException) {
/* 47 */     super(conn, lastPacketSentTimeMs, 0L, underlyingException);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 56 */     return "Feature not available in this distribution of Connector/J";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSQLState() {
/* 65 */     return "01S00";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\ConnectionFeatureNotAvailableException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */