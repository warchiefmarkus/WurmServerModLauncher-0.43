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
/*    */ public final class ItemDamageDatabaseUpdater
/*    */   extends DatabaseUpdater<ItemDamageDatabaseUpdatable>
/*    */ {
/* 39 */   private static final Logger logger = Logger.getLogger(ItemDamageDatabaseUpdater.class.getName());
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
/*    */   public ItemDamageDatabaseUpdater(String aUpdaterDescription, int aMaxUpdatablesToRemovePerCycle) {
/* 51 */     super(aUpdaterDescription, ItemDamageDatabaseUpdatable.class, aMaxUpdatablesToRemovePerCycle);
/* 52 */     logger.info("Creating Item Damage Updater.");
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
/* 64 */     return DbConnector.getItemDbCon();
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
/*    */   void addUpdatableToBatch(PreparedStatement updateStatement, ItemDamageDatabaseUpdatable aDbUpdatable) throws SQLException {
/* 76 */     if (logger.isLoggable(Level.FINEST))
/*    */     {
/* 78 */       logger.finest("Adding to batch: " + aDbUpdatable);
/*    */     }
/* 80 */     updateStatement.setFloat(1, aDbUpdatable.getDamage());
/* 81 */     updateStatement.setLong(2, aDbUpdatable.getLastMaintained());
/* 82 */     updateStatement.setLong(3, aDbUpdatable.getId());
/* 83 */     updateStatement.addBatch();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\ItemDamageDatabaseUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */