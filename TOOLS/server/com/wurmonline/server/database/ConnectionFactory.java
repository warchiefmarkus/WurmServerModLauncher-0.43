/*    */ package com.wurmonline.server.database;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
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
/*    */ public abstract class ConnectionFactory
/*    */ {
/*    */   private final String url;
/*    */   private final WurmDatabaseSchema schema;
/*    */   
/*    */   ConnectionFactory(@Nonnull String url, @Nonnull WurmDatabaseSchema schema) {
/* 27 */     this.schema = schema;
/* 28 */     this.url = url;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String getUrl() {
/* 36 */     return this.url;
/*    */   }
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
/*    */   public abstract Connection createConnection() throws SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean isValid(@Nullable Connection paramConnection) throws SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract boolean isStale(long paramLong, @Nullable Connection paramConnection) throws SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WurmDatabaseSchema getSchema() {
/* 79 */     return this.schema;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\ConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */