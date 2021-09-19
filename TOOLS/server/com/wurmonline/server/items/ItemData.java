/*     */ package com.wurmonline.server.items;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class ItemData
/*     */ {
/*     */   int data1;
/*     */   int data2;
/*     */   int extra1;
/*     */   int extra2;
/*     */   public final long wurmid;
/*  40 */   private static final Logger logger = Logger.getLogger(ItemData.class.getName());
/*     */ 
/*     */   
/*     */   public ItemData(long wid, int d1, int d2, int e1, int e2) {
/*  44 */     this.wurmid = wid;
/*  45 */     this.data1 = d1;
/*  46 */     this.data2 = d2;
/*  47 */     this.extra1 = e1;
/*  48 */     this.extra2 = e2;
/*  49 */     Items.addData(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createDataEntry(Connection dbcon) {
/*  54 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  57 */       ps = dbcon.prepareStatement(ItemDbStrings.getInstance().createData());
/*  58 */       ps.setInt(1, this.data1);
/*  59 */       ps.setInt(2, this.data2);
/*  60 */       ps.setInt(3, this.extra1);
/*  61 */       ps.setInt(4, this.extra2);
/*  62 */       ps.setLong(5, this.wurmid);
/*  63 */       ps.executeUpdate();
/*     */     }
/*  65 */     catch (SQLException sqx) {
/*     */       
/*  67 */       logger.log(Level.WARNING, "Failed to save item data " + this.wurmid, sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  71 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getData1() {
/*  82 */     return this.data1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setData1(int aData1) {
/*  93 */     this.data1 = aData1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getData2() {
/* 103 */     return this.data2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setData2(int aData2) {
/* 114 */     this.data2 = aData2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getExtra1() {
/* 124 */     return this.extra1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setExtra1(int aExtra1) {
/* 135 */     this.extra1 = aExtra1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getExtra2() {
/* 145 */     return this.extra2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setExtra2(int aExtra2) {
/* 156 */     this.extra2 = aExtra2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWurmid() {
/* 166 */     return this.wurmid;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */