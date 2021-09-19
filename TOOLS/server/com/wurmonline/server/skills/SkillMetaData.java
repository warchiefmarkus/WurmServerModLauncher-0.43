/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ public class SkillMetaData
/*     */ {
/*     */   private final long id;
/*     */   private final int number;
/*     */   private double knowledge;
/*     */   private double minvalue;
/*     */   private final long owner;
/*     */   private final long lastused;
/*     */   private static final String CREATE_SKILL = "INSERT INTO SKILLS( ID, OWNER,NUMBER,VALUE,MINVALUE,LASTUSED) VALUES(?,?,?,?,?,?)";
/*     */   private static final String QUERY_SKILL = "SELECT VALUE,MINVALUE FROM SKILLS WHERE OWNER=? AND NUMBER=?";
/*     */   private static final String DELETE_SKILL = "DELETE FROM SKILLS WHERE OWNER=? AND NUMBER=?";
/*     */   
/*     */   public SkillMetaData(long aId, long aOwner, int aNumber, double aKnowledge, double aMinvalue, long aLastused) {
/*  53 */     this.id = aId;
/*  54 */     this.owner = aOwner;
/*  55 */     this.number = aNumber;
/*  56 */     this.knowledge = aKnowledge;
/*  57 */     this.minvalue = aMinvalue;
/*  58 */     this.lastused = aLastused;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setChallenge() {
/*  63 */     if (this.number >= 100 && this.knowledge < 21.0D) {
/*     */       
/*  65 */       this.knowledge = 21.0D;
/*  66 */       if (this.number == 100)
/*     */       {
/*  68 */         this.knowledge = 30.0D;
/*     */       }
/*  70 */       this.minvalue = this.knowledge;
/*     */     } 
/*  72 */     if (this.number == 1023) {
/*     */       
/*  74 */       this.knowledge = 70.0D;
/*  75 */       this.minvalue = this.knowledge;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/*  81 */     Connection dbcon = null;
/*  82 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  85 */       dbcon = DbConnector.getPlayerDbCon();
/*  86 */       ps = dbcon.prepareStatement("INSERT INTO SKILLS( ID, OWNER,NUMBER,VALUE,MINVALUE,LASTUSED) VALUES(?,?,?,?,?,?)");
/*  87 */       ps.setLong(1, this.id);
/*  88 */       ps.setLong(2, this.owner);
/*  89 */       ps.setInt(3, this.number);
/*  90 */       ps.setDouble(4, this.knowledge);
/*  91 */       ps.setDouble(5, this.minvalue);
/*  92 */       ps.setLong(6, this.lastused);
/*  93 */       ps.executeUpdate();
/*     */     }
/*  95 */     catch (SQLException sqex) {
/*     */       
/*  97 */       throw new IOException(this.id + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 101 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 102 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void deleteSkill(long wurmId, int skillNumber) throws IOException {
/* 108 */     Connection dbcon = null;
/* 109 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 112 */       dbcon = DbConnector.getPlayerDbCon();
/* 113 */       ps = dbcon.prepareStatement("DELETE FROM SKILLS WHERE OWNER=? AND NUMBER=?");
/* 114 */       ps.setLong(1, wurmId);
/* 115 */       ps.setInt(2, skillNumber);
/* 116 */       ps.executeUpdate();
/*     */       
/* 118 */       DbUtilities.closeDatabaseObjects(ps, null);
/*     */     }
/* 120 */     catch (SQLException sqex) {
/*     */       
/* 122 */       throw new IOException(wurmId + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 126 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 127 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SkillMetaData copyToEpicSkill(long skillId, long wurmId, int skillNumber, double skillValue, double skillMinimum, long skillLastUsed) throws IOException {
/* 135 */     double lastVal = 0.0D;
/* 136 */     double lastMin = 0.0D;
/*     */     
/* 138 */     Connection dbcon = null;
/* 139 */     PreparedStatement ps = null;
/* 140 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 143 */       dbcon = DbConnector.getPlayerDbCon();
/* 144 */       ps = dbcon.prepareStatement("SELECT VALUE,MINVALUE FROM SKILLS WHERE OWNER=? AND NUMBER=?");
/* 145 */       ps.setLong(1, wurmId);
/* 146 */       ps.setInt(2, skillNumber);
/* 147 */       rs = ps.executeQuery();
/*     */       
/* 149 */       if (rs.first())
/*     */       {
/* 151 */         lastVal = rs.getDouble("VALUE");
/* 152 */         lastMin = rs.getDouble("MINVALUE");
/*     */       }
/*     */     
/* 155 */     } catch (SQLException sqex) {
/*     */       
/* 157 */       throw new IOException(skillId + " " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 161 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 162 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 167 */     boolean useNewSkill = true;
/* 168 */     if (lastVal < lastMin || lastVal > skillValue) {
/* 169 */       useNewSkill = false;
/*     */     }
/* 171 */     return new SkillMetaData(skillId, wurmId, skillNumber, useNewSkill ? skillValue : lastVal, (skillMinimum > lastMin && useNewSkill) ? skillMinimum : lastMin, skillLastUsed);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\SkillMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */