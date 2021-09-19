/*    */ package com.wurmonline.server.utils;
/*    */ 
/*    */ import com.wurmonline.server.DbConnector;
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ public class ItemOwnerDatabaseUpdater
/*    */   extends DatabaseUpdater<ItemOwnerDatabaseUpdatable>
/*    */ {
/* 14 */   private static final Logger logger = Logger.getLogger(ItemOwnerDatabaseUpdater.class.getName());
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
/*    */   public ItemOwnerDatabaseUpdater(String aUpdaterDescription, int aMaxUpdatablesToRemovePerCycle) {
/* 26 */     super(aUpdaterDescription, ItemOwnerDatabaseUpdatable.class, aMaxUpdatablesToRemovePerCycle);
/* 27 */     logger.info("Creating Item Owner Updater.");
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
/*    */   Connection getDatabaseConnection() throws SQLException {
/* 39 */     return DbConnector.getItemDbCon();
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
/*    */   void addUpdatableToBatch(PreparedStatement updateStatement, ItemOwnerDatabaseUpdatable aDbUpdatable) throws SQLException {
/* 51 */     if (logger.isLoggable(Level.FINEST))
/*    */     {
/* 53 */       logger.finest("Adding to batch: " + aDbUpdatable);
/*    */     }
/* 55 */     updateStatement.setLong(1, aDbUpdatable.getOwner());
/* 56 */     updateStatement.setLong(2, aDbUpdatable.getId());
/* 57 */     updateStatement.addBatch();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\ItemOwnerDatabaseUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */