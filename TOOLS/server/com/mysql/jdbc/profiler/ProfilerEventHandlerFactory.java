/*    */ package com.mysql.jdbc.profiler;
/*    */ 
/*    */ import com.mysql.jdbc.Connection;
/*    */ import com.mysql.jdbc.ConnectionImpl;
/*    */ import com.mysql.jdbc.Util;
/*    */ import com.mysql.jdbc.log.Log;
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class ProfilerEventHandlerFactory
/*    */ {
/* 40 */   private static final Map CONNECTIONS_TO_SINKS = new HashMap();
/*    */   
/* 42 */   private Connection ownerConnection = null;
/*    */   
/* 44 */   private Log log = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static synchronized ProfilerEventHandler getInstance(ConnectionImpl conn) throws SQLException {
/* 55 */     ProfilerEventHandler handler = (ProfilerEventHandler)CONNECTIONS_TO_SINKS.get(conn);
/*    */ 
/*    */     
/* 58 */     if (handler == null) {
/* 59 */       handler = (ProfilerEventHandler)Util.getInstance(conn.getProfilerEventHandler(), new Class[0], new Object[0], conn.getExceptionInterceptor());
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 64 */       conn.initializeExtension(handler);
/*    */       
/* 66 */       CONNECTIONS_TO_SINKS.put(conn, handler);
/*    */     } 
/*    */     
/* 69 */     return handler;
/*    */   }
/*    */   
/*    */   public static synchronized void removeInstance(Connection conn) {
/* 73 */     ProfilerEventHandler handler = (ProfilerEventHandler)CONNECTIONS_TO_SINKS.remove(conn);
/*    */     
/* 75 */     if (handler != null) {
/* 76 */       handler.destroy();
/*    */     }
/*    */   }
/*    */   
/*    */   private ProfilerEventHandlerFactory(Connection conn) {
/* 81 */     this.ownerConnection = conn;
/*    */     
/*    */     try {
/* 84 */       this.log = this.ownerConnection.getLog();
/* 85 */     } catch (SQLException sqlEx) {
/* 86 */       throw new RuntimeException("Unable to get logger from connection");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\profiler\ProfilerEventHandlerFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */