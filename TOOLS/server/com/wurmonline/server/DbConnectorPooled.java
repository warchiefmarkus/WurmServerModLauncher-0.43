/*    */ package com.wurmonline.server;
/*    */ 
/*    */ import com.wurmonline.server.database.WurmDatabaseSchema;
/*    */ import java.sql.Connection;
/*    */ import java.util.logging.Logger;
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
/*    */ public final class DbConnectorPooled
/*    */   extends DbConnector
/*    */ {
/* 32 */   private static final Logger logger = Logger.getLogger(DbConnectorPooled.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private DbConnectorPooled(String driver, String host, String port, WurmDatabaseSchema schema, String user, String password, String loggingName) {
/* 40 */     super(driver, host, port, schema, user, password, loggingName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void returnConnection(Connection aConnection) {
/* 48 */     logger.warning("The DbConnectorPooled is just a place holder and should not be used yet. Check the wurm.ini. Make sure that USE_POOLED_DB=false is there.");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\DbConnectorPooled.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */