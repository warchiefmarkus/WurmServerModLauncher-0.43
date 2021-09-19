/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.TreeMap;
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
/*     */ public class DbSkills
/*     */   extends Skills
/*     */   implements MiscConstants, CounterTypes
/*     */ {
/*  42 */   private static Logger logger = Logger.getLogger(DbSkills.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String loadPlayerSkills2 = "select * FROM SKILLS where OWNER=?";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String loadCreatureSkills2 = "select * FROM SKILLS where OWNER=?";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String deleteCreatureSkills = "delete from SKILLS where OWNER=?";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DbSkills(long aId) {
/*  70 */     this.id = aId;
/*  71 */     if (aId != -10L)
/*     */     {
/*  73 */       if (WurmId.getType(aId) == 0) {
/*     */         
/*  75 */         PlayerInfo p = PlayerInfoFactory.getPlayerInfoWithWurmId(aId);
/*  76 */         if (p != null) {
/*     */           
/*  78 */           if (!p.isPaying())
/*  79 */             this.paying = false; 
/*  80 */           if (!p.hasSkillGain)
/*  81 */             this.hasSkillGain = false; 
/*  82 */           if (Servers.localServer.isChallengeOrEpicServer() && p.realdeath == 0) {
/*  83 */             this.priest = p.isPriest;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   DbSkills(String aTemplateName) {
/*  92 */     this.templateName = aTemplateName;
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
/*     */   public void load() throws Exception {
/* 105 */     if (this.id != -10L) {
/*     */       
/* 107 */       this.skills = new TreeMap<>();
/* 108 */       Connection dbcon = null;
/* 109 */       PreparedStatement ps = null;
/* 110 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 113 */         if (WurmId.getType(this.id) == 0) {
/*     */           
/* 115 */           dbcon = DbConnector.getPlayerDbCon();
/* 116 */           ps = dbcon.prepareStatement("select * FROM SKILLS where OWNER=?");
/* 117 */           ps.setLong(1, this.id);
/* 118 */           rs = ps.executeQuery();
/* 119 */           while (rs.next()) {
/*     */ 
/*     */             
/* 122 */             DbSkill skill = new DbSkill(rs.getLong("ID"), this, rs.getInt("NUMBER"), rs.getDouble("VALUE"), rs.getDouble("MINVALUE"), rs.getLong("LASTUSED"));
/*     */             
/* 124 */             if (!this.skills.containsKey(Integer.valueOf(skill.getNumber())) || skill
/* 125 */               .getMinimumValue() > ((Skill)this.skills.get(Integer.valueOf(skill.getNumber()))).getMinimumValue()) {
/* 126 */               this.skills.put(Integer.valueOf(skill.getNumber()), skill);
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           
/* 131 */           dbcon = DbConnector.getCreatureDbCon();
/* 132 */           ps = dbcon.prepareStatement("select * FROM SKILLS where OWNER=?");
/* 133 */           ps.setLong(1, this.id);
/* 134 */           rs = ps.executeQuery();
/* 135 */           while (rs.next()) {
/*     */ 
/*     */             
/* 138 */             DbSkill skill = new DbSkill(rs.getLong("ID"), this, rs.getInt("NUMBER"), rs.getDouble("VALUE"), rs.getDouble("MINVALUE"), rs.getLong("LASTUSED"));
/* 139 */             this.skills.put(Integer.valueOf(skill.getNumber()), skill);
/*     */           } 
/*     */         } 
/* 142 */         addTempSkills();
/*     */       }
/*     */       finally {
/*     */         
/* 146 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 147 */         DbConnector.returnConnection(dbcon);
/*     */       }
/*     */     
/* 150 */     } else if (this.templateName != null) {
/*     */       return;
/*     */     } 
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
/*     */   public void delete() throws SQLException {
/* 191 */     Connection dbcon = null;
/* 192 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 195 */       if (this.id != -10L)
/*     */       {
/* 197 */         if (WurmId.getType(this.id) == 0) {
/* 198 */           dbcon = DbConnector.getPlayerDbCon();
/* 199 */         } else if (WurmId.getType(this.id) == 1) {
/* 200 */           dbcon = DbConnector.getCreatureDbCon();
/*     */         } else {
/*     */           
/* 203 */           logger.warning("Unexpected Counter Type: " + WurmId.getType(this.id) + " for WurmID: " + this.id);
/*     */           return;
/*     */         } 
/* 206 */         ps = dbcon.prepareStatement("delete from SKILLS where OWNER=?");
/* 207 */         ps.setLong(1, this.id);
/* 208 */         ps.executeUpdate();
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */       
/* 220 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 221 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\DbSkills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */