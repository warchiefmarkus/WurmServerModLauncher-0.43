/*     */ package com.wurmonline.server.utils.logging;
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
/*     */ public final class ItemTransfer
/*     */   implements WurmLoggable
/*     */ {
/*     */   private long itemId;
/*     */   private String itemName;
/*     */   private long oldOwnerId;
/*     */   private String oldOwnerName;
/*     */   private long newOwnerId;
/*     */   private String newOwnerName;
/*     */   private long transferTime;
/*     */   private static final String INSERT_ITEM_TRANSFER = "INSERT INTO ITEM_TRANSFER_LOG (ITEMID, ITEMNAME, OLDOWNERID, OLDOWNERNAME, NEWOWNERID, NEWOWNERNAME, TRANSFERTIME) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
/*     */   
/*     */   public ItemTransfer(long aItemId, String aItemName, long aOldOwnerId, String aOldOwnerName, long aNewOwnerId, String aNewOwnerName, long aTransferTime) {
/*  80 */     this.itemId = aItemId;
/*  81 */     this.itemName = aItemName;
/*  82 */     this.oldOwnerId = aOldOwnerId;
/*  83 */     this.oldOwnerName = aOldOwnerName;
/*  84 */     this.newOwnerId = aNewOwnerId;
/*  85 */     this.newOwnerName = aNewOwnerName;
/*  86 */     this.transferTime = aTransferTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getItemId() {
/*  96 */     return this.itemId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemId(long aItemId) {
/* 107 */     this.itemId = aItemId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getItemName() {
/* 117 */     return this.itemName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemName(String aItemName) {
/* 128 */     this.itemName = aItemName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOldOwnerId() {
/* 138 */     return this.oldOwnerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOldOwnerId(long aOldOwnerId) {
/* 149 */     this.oldOwnerId = aOldOwnerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOldOwnerName() {
/* 159 */     return this.oldOwnerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOldOwnerName(String aOldOwnerName) {
/* 170 */     this.oldOwnerName = aOldOwnerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNewOwnerId() {
/* 180 */     return this.newOwnerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNewOwnerId(long aNewOwnerId) {
/* 191 */     this.newOwnerId = aNewOwnerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNewOwnerName() {
/* 201 */     return this.newOwnerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNewOwnerName(String aNewOwnerName) {
/* 212 */     this.newOwnerName = aNewOwnerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTransferTime() {
/* 222 */     return this.transferTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransferTime(long aTransferTime) {
/* 233 */     this.transferTime = aTransferTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatabaseInsertStatement() {
/* 244 */     return "INSERT INTO ITEM_TRANSFER_LOG (ITEMID, ITEMNAME, OLDOWNERID, OLDOWNERNAME, NEWOWNERID, NEWOWNERNAME, TRANSFERTIME) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 255 */     return "ItemTransfer [itemId=" + this.itemId + ", itemName=" + this.itemName + ", newOwnerId=" + this.newOwnerId + ", newOwnerName=" + this.newOwnerName + ", oldOwnerId=" + this.oldOwnerId + ", oldOwnerName=" + this.oldOwnerName + ", transferTime=" + this.transferTime + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\logging\ItemTransfer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */