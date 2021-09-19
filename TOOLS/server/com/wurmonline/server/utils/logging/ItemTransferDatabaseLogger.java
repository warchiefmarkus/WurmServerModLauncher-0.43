/*    */ package com.wurmonline.server.utils.logging;
/*    */ 
/*    */ import com.wurmonline.server.Constants;
/*    */ import java.sql.Date;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
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
/*    */ public final class ItemTransferDatabaseLogger
/*    */   extends DatabaseLogger<ItemTransfer>
/*    */ {
/* 31 */   private static Logger logger = Logger.getLogger(ItemTransferDatabaseLogger.class.getName());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ItemTransferDatabaseLogger(String aLoggerDescription, int aMaxLoggablesToRemovePerCycle) {
/* 39 */     super(aLoggerDescription, ItemTransfer.class, aMaxLoggablesToRemovePerCycle);
/* 40 */     logger.info("Creating Item Transfer logger, System useItemTransferLog option: " + Constants.useItemTransferLog);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void addLoggableToBatch(PreparedStatement logsStatement, ItemTransfer object) throws SQLException {
/* 58 */     ItemTransfer itemTransfer = object;
/* 59 */     logsStatement.setLong(1, itemTransfer.getItemId());
/* 60 */     logsStatement.setString(2, itemTransfer.getItemName());
/* 61 */     logsStatement.setLong(3, itemTransfer.getOldOwnerId());
/* 62 */     logsStatement.setString(4, itemTransfer.getOldOwnerName());
/* 63 */     logsStatement.setLong(5, itemTransfer.getNewOwnerId());
/* 64 */     logsStatement.setString(6, itemTransfer.getNewOwnerName());
/* 65 */     logsStatement.setDate(7, new Date(itemTransfer.getTransferTime()));
/* 66 */     logsStatement.addBatch();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\logging\ItemTransferDatabaseLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */