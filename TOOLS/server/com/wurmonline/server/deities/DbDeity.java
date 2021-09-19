/*     */ package com.wurmonline.server.deities;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ final class DbDeity
/*     */   extends Deity
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(DbDeity.class.getName());
/*     */   
/*     */   private static final String CREATE_DEITY = "INSERT INTO DEITIES (ID,NAME,FAITH,ALIGNMENT,POWER,SEX,HOLYITEM,ATTACK,VITALITY) VALUES(?,?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static final String SET_FAVOR = "UPDATE DEITIES SET FAVOR=? WHERE ID=?";
/*     */   
/*     */   private static final String SET_POWER = "UPDATE DEITIES SET POWER=? WHERE ID=?";
/*     */ 
/*     */   
/*     */   DbDeity(int num, String nam, byte align, byte aSex, byte pow, double aFaith, int aHolyItem, int _favor, float _attack, float _vitality, boolean create) {
/*  49 */     super(num, nam, align, aSex, pow, aFaith, aHolyItem, _favor, _attack, _vitality, create);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/*  60 */     Connection dbcon = null;
/*  61 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  64 */       dbcon = DbConnector.getDeityDbCon();
/*  65 */       ps = dbcon.prepareStatement("INSERT INTO DEITIES (ID,NAME,FAITH,ALIGNMENT,POWER,SEX,HOLYITEM,ATTACK,VITALITY) VALUES(?,?,?,?,?,?,?,?,?)");
/*  66 */       ps.setByte(1, (byte)this.number);
/*  67 */       ps.setString(2, this.name);
/*  68 */       ps.setDouble(3, this.faith);
/*  69 */       ps.setByte(4, (byte)this.alignment);
/*  70 */       ps.setByte(5, this.power);
/*  71 */       ps.setByte(6, this.sex);
/*  72 */       ps.setInt(7, this.holyItem);
/*     */       
/*  74 */       ps.setFloat(8, this.attack);
/*     */       
/*  76 */       ps.setFloat(9, this.vitality);
/*  77 */       ps.executeUpdate();
/*     */     }
/*  79 */     catch (SQLException sqx) {
/*     */       
/*  81 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*  82 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  86 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  87 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setFaith(double aFaith) throws IOException {
/*  94 */     this.faith = aFaith;
/*     */   }
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
/*     */   public void setFavor(int newfavor) {
/* 114 */     this.favor = newfavor;
/* 115 */     if (this.favor % 20 == 0) {
/*     */       
/* 117 */       Connection dbcon = null;
/* 118 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 121 */         dbcon = DbConnector.getDeityDbCon();
/* 122 */         ps = dbcon.prepareStatement("UPDATE DEITIES SET FAVOR=? WHERE ID=?");
/* 123 */         ps.setDouble(1, this.favor);
/* 124 */         ps.setByte(2, (byte)this.number);
/* 125 */         ps.executeUpdate();
/*     */       }
/* 127 */       catch (SQLException sqx) {
/*     */         
/* 129 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 133 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 134 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPower(byte newPower) {
/* 142 */     this.power = newPower;
/* 143 */     Connection dbcon = null;
/* 144 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 147 */       dbcon = DbConnector.getDeityDbCon();
/* 148 */       ps = dbcon.prepareStatement("UPDATE DEITIES SET POWER=? WHERE ID=?");
/* 149 */       logger.log(Level.INFO, "Changing power for deity " + this.name + " " + (byte)this.number + " to power " + this.power);
/* 150 */       ps.setByte(1, this.power);
/* 151 */       ps.setByte(2, (byte)this.number);
/* 152 */       ps.executeUpdate();
/*     */     }
/* 154 */     catch (SQLException sqx) {
/*     */       
/* 156 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 160 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 161 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\deities\DbDeity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */