/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
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
/*     */ 
/*     */ public final class WarDeclaration
/*     */ {
/*     */   private static final String DELETE = "DELETE FROM VILLAGEWARDECLARATIONS WHERE VILLONE=? AND VILLTWO=?";
/*     */   private static final String CREATE = "INSERT INTO VILLAGEWARDECLARATIONS (VILLONE, VILLTWO,DECLARETIME ) VALUES (?,?,?)";
/*     */   public final Village receiver;
/*     */   public final Village declarer;
/*     */   public final long time;
/*  41 */   private static final Logger logger = Logger.getLogger(WarDeclaration.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   WarDeclaration(Village aDeclarer, Village aReceiver) {
/*  50 */     this.declarer = aDeclarer;
/*  51 */     this.receiver = aReceiver;
/*  52 */     this.time = System.currentTimeMillis();
/*     */     
/*  54 */     save();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   WarDeclaration(Village aDeclarer, Village aReceiver, long aTime) {
/*  65 */     this.declarer = aDeclarer;
/*  66 */     this.receiver = aReceiver;
/*  67 */     this.time = aTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept() {
/*  72 */     if (this.receiver.warDeclarations != null) {
/*  73 */       this.receiver.warDeclarations.remove(this.declarer);
/*     */     }
/*  75 */     if (this.declarer.warDeclarations != null) {
/*  76 */       this.declarer.warDeclarations.remove(this.receiver);
/*     */     }
/*  78 */     Villages.createWar(this.declarer, this.receiver);
/*  79 */     delete();
/*     */   }
/*     */ 
/*     */   
/*     */   public void dissolve(boolean expire) {
/*  84 */     if (this.receiver.warDeclarations != null) {
/*     */       
/*  86 */       if (expire) {
/*  87 */         this.receiver.broadCastSafe("The declaration of war from " + this.declarer.getName() + " expires.");
/*     */       } else {
/*  89 */         this.receiver.broadCastSafe(this.declarer.getName() + " has withdrawn their declaration of war.");
/*  90 */       }  this.receiver.warDeclarations.remove(this.declarer);
/*     */     } 
/*     */     
/*  93 */     if (this.declarer.warDeclarations != null) {
/*     */       
/*  95 */       if (expire) {
/*  96 */         this.declarer.broadCastSafe(this.receiver.getName() + " lets your declaration of war expire.");
/*     */       } else {
/*  98 */         this.declarer.broadCastSafe("You withdraw your declaration of war with " + this.receiver.getName() + ".");
/*  99 */       }  this.declarer.warDeclarations.remove(this.receiver);
/*     */     } 
/* 101 */     delete();
/*     */   }
/*     */ 
/*     */   
/*     */   private void save() {
/* 106 */     Connection dbcon = null;
/* 107 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 110 */       dbcon = DbConnector.getZonesDbCon();
/* 111 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGEWARDECLARATIONS (VILLONE, VILLTWO,DECLARETIME ) VALUES (?,?,?)");
/* 112 */       ps.setInt(1, this.declarer.getId());
/* 113 */       ps.setInt(2, this.receiver.getId());
/* 114 */       ps.setLong(3, this.time);
/* 115 */       ps.executeUpdate();
/*     */     }
/* 117 */     catch (SQLException sqx) {
/*     */       
/* 119 */       logger.log(Level.WARNING, "Failed to create war between " + this.declarer.getName() + " and " + this.receiver.getName(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 123 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 124 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void delete() {
/* 130 */     Connection dbcon = null;
/* 131 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 134 */       dbcon = DbConnector.getZonesDbCon();
/* 135 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGEWARDECLARATIONS WHERE VILLONE=? AND VILLTWO=?");
/* 136 */       ps.setInt(1, this.declarer.getId());
/* 137 */       ps.setInt(2, this.receiver.getId());
/* 138 */       ps.executeUpdate();
/*     */     }
/* 140 */     catch (SQLException sqx) {
/*     */       
/* 142 */       logger.log(Level.WARNING, "Failed to delete war between " + this.declarer.getName() + " and " + this.receiver.getName(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 146 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 147 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\WarDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */