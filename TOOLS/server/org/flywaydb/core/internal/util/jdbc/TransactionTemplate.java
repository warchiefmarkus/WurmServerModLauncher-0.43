/*    */ package org.flywaydb.core.internal.util.jdbc;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogFactory;
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
/*    */ public class TransactionTemplate
/*    */ {
/* 29 */   private static final Log LOG = LogFactory.getLog(TransactionTemplate.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final Connection connection;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean rollbackOnException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TransactionTemplate(Connection connection) {
/* 47 */     this(connection, true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TransactionTemplate(Connection connection, boolean rollbackOnException) {
/* 57 */     this.connection = connection;
/* 58 */     this.rollbackOnException = rollbackOnException;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T execute(TransactionCallback<T> transactionCallback) {
/* 68 */     boolean oldAutocommit = true;
/*    */     try {
/* 70 */       oldAutocommit = this.connection.getAutoCommit();
/* 71 */       this.connection.setAutoCommit(false);
/* 72 */       T result = transactionCallback.doInTransaction();
/* 73 */       this.connection.commit();
/* 74 */       return result;
/* 75 */     } catch (SQLException e) {
/* 76 */       throw new FlywayException("Unable to commit transaction", e);
/* 77 */     } catch (RuntimeException e) {
/* 78 */       if (this.rollbackOnException) {
/*    */         try {
/* 80 */           LOG.debug("Rolling back transaction...");
/* 81 */           this.connection.rollback();
/* 82 */           LOG.debug("Transaction rolled back");
/* 83 */         } catch (SQLException se) {
/* 84 */           LOG.error("Unable to rollback transaction", se);
/*    */         } 
/*    */       } else {
/*    */         try {
/* 88 */           this.connection.commit();
/* 89 */         } catch (SQLException se) {
/* 90 */           LOG.error("Unable to commit transaction", se);
/*    */         } 
/*    */       } 
/* 93 */       throw e;
/*    */     } finally {
/*    */       try {
/* 96 */         this.connection.setAutoCommit(oldAutocommit);
/* 97 */       } catch (SQLException e) {
/* 98 */         LOG.error("Unable to restore autocommit to original value for connection", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\jdbc\TransactionTemplate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */