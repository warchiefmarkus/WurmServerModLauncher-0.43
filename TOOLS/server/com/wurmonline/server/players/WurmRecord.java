/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
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
/*     */ public class WurmRecord
/*     */ {
/*     */   private static final String GET_CHAMPIONRECORDS = "SELECT * FROM CHAMPIONS";
/*  37 */   private int value = 0;
/*  38 */   private String holder = "";
/*     */   
/*     */   private boolean current = true;
/*     */   
/*  42 */   private static final Logger logger = Logger.getLogger(WurmRecord.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WurmRecord(int val, String name, boolean isCurrent) {
/*  49 */     this.value = val;
/*  50 */     this.holder = name;
/*  51 */     this.current = isCurrent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*  61 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(int aValue) {
/*  72 */     this.value = aValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHolder() {
/*  82 */     return this.holder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHolder(String aHolder) {
/*  93 */     this.holder = aHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCurrent() {
/* 103 */     return this.current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrent(boolean aCurrent) {
/* 114 */     this.current = aCurrent;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllChampRecords() {
/* 119 */     long now = System.currentTimeMillis();
/* 120 */     Connection dbcon = null;
/* 121 */     PreparedStatement ps = null;
/* 122 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 125 */       dbcon = DbConnector.getPlayerDbCon();
/* 126 */       ps = dbcon.prepareStatement("SELECT * FROM CHAMPIONS");
/* 127 */       rs = ps.executeQuery();
/* 128 */       while (rs.next())
/*     */       {
/* 130 */         WurmRecord record = new WurmRecord(rs.getInt("VALUE"), rs.getString("NAME"), rs.getBoolean("CURRENT"));
/* 131 */         PlayerInfoFactory.addChampRecord(record);
/*     */       }
/*     */     
/* 134 */     } catch (SQLException sqex) {
/*     */       
/* 136 */       logger.log(Level.WARNING, "Failed to load champ records.");
/*     */     }
/*     */     finally {
/*     */       
/* 140 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 141 */       DbConnector.returnConnection(dbcon);
/* 142 */       long end = System.currentTimeMillis();
/* 143 */       logger.info("Loaded " + (PlayerInfoFactory.getChampionRecords()).length + " champ records from the database took " + (end - now) + " ms");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\WurmRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */