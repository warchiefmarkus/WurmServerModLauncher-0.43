/*     */ package com.wurmonline.server.gui;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
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
/*     */ public class PlayerData
/*     */   implements MiscConstants
/*     */ {
/*     */   private static final String saveData = "UPDATE PLAYERS SET CURRENTSERVER=?, POWER=?, REIMBURSED=?, UNDEADTYPE=? WHERE WURMID=?";
/*     */   private static final String savePosition = "UPDATE POSITION SET POSX=?,POSY=? WHERE WURMID=?";
/*     */   private String name;
/*     */   private long wurmid;
/*     */   private int power;
/*     */   private float posx;
/*     */   private float posy;
/*     */   private int server;
/*     */   private boolean reimbursed;
/*  43 */   private byte undeadType = 0;
/*     */ 
/*     */   
/*  46 */   private static final Logger logger = Logger.getLogger(PlayerData.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void save() throws SQLException {
/*  57 */     Connection dbcon = null;
/*  58 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  61 */       dbcon = DbConnector.getPlayerDbCon();
/*  62 */       ps = dbcon.prepareStatement("UPDATE PLAYERS SET CURRENTSERVER=?, POWER=?, REIMBURSED=?, UNDEADTYPE=? WHERE WURMID=?");
/*  63 */       ps.setInt(1, getServer());
/*  64 */       ps.setByte(2, (byte)getPower());
/*  65 */       ps.setBoolean(3, isReimbursed());
/*  66 */       ps.setByte(4, this.undeadType);
/*  67 */       ps.setLong(5, getWurmid());
/*     */       
/*  69 */       ps.executeUpdate();
/*     */     }
/*     */     finally {
/*     */       
/*  73 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  74 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*  76 */     savePosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void savePosition() throws SQLException {
/*  81 */     Connection dbcon = null;
/*  82 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  85 */       dbcon = DbConnector.getPlayerDbCon();
/*  86 */       ps = dbcon.prepareStatement("UPDATE POSITION SET POSX=?,POSY=? WHERE WURMID=?");
/*  87 */       ps.setFloat(1, getPosx());
/*  88 */       ps.setFloat(2, getPosy());
/*  89 */       ps.setLong(3, getWurmid());
/*  90 */       ps.executeUpdate();
/*     */     }
/*     */     finally {
/*     */       
/*  94 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  95 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 105 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 116 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWurmid() {
/* 126 */     return this.wurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWurmid(long wurmid) {
/* 137 */     this.wurmid = wurmid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPower() {
/* 147 */     return this.power;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPower(int power) {
/* 158 */     if (power < 0)
/* 159 */       power = 0; 
/* 160 */     if (power > 5)
/* 161 */       power = 5; 
/* 162 */     if (power > this.power)
/* 163 */       setReimbursed(false); 
/* 164 */     this.power = power;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPosx() {
/* 174 */     return this.posx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosx(float posx) {
/* 185 */     this.posx = posx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPosy() {
/* 195 */     return this.posy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosy(float posy) {
/* 206 */     this.posy = posy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getServer() {
/* 216 */     return this.server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServer(int server) {
/* 227 */     this.server = server;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReimbursed() {
/* 237 */     return this.reimbursed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReimbursed(boolean reimbursed) {
/* 248 */     this.reimbursed = reimbursed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getUndeadType() {
/* 258 */     return this.undeadType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUndeadType(byte undeadType) {
/* 269 */     this.undeadType = undeadType;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUndead() {
/* 274 */     return (this.undeadType != 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\PlayerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */