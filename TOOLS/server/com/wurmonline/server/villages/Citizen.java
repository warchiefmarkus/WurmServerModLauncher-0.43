/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
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
/*     */ public abstract class Citizen
/*     */   implements MiscConstants, TimeConstants, Comparable<Citizen>, CounterTypes
/*     */ {
/*     */   public final long wurmId;
/*     */   final String name;
/*  42 */   private static final Logger logger = Logger.getLogger(Citizen.class.getName());
/*     */   
/*     */   private static final String DELETE = "DELETE FROM CITIZENS WHERE WURMID=?";
/*  45 */   VillageRole role = null;
/*  46 */   long voteDate = -10L;
/*  47 */   long votedFor = -10L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Citizen(long aWurmId, String aName, VillageRole aRole, long aVotedate, long aVotedfor) {
/*  54 */     this.wurmId = aWurmId;
/*  55 */     this.name = aName;
/*  56 */     this.role = aRole;
/*  57 */     this.voteDate = aVotedate;
/*  58 */     this.votedFor = aVotedfor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final VillageRole getRole() {
/*  67 */     return this.role;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  76 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getId() {
/*  81 */     return this.wurmId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPlayer() {
/*  86 */     return (WurmId.getType(this.wurmId) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasVoted() {
/*  91 */     return (System.currentTimeMillis() - this.voteDate < 604800000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getVoteDate() {
/* 100 */     return this.voteDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getVotedFor() {
/* 109 */     return this.votedFor;
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
/*     */   static final void delete(long wid) throws IOException {
/* 137 */     Connection dbcon = null;
/* 138 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 141 */       dbcon = DbConnector.getZonesDbCon();
/* 142 */       ps = dbcon.prepareStatement("DELETE FROM CITIZENS WHERE WURMID=?");
/* 143 */       ps.setLong(1, wid);
/* 144 */       ps.executeUpdate();
/*     */     }
/* 146 */     catch (SQLException sqx) {
/*     */       
/* 148 */       logger.log(Level.WARNING, "Failed to delete citizen " + wid + ": " + sqx.getMessage(), sqx);
/* 149 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 153 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 154 */       DbConnector.returnConnection(dbcon);
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
/*     */   public int compareTo(Citizen aCitizen) {
/* 169 */     return getName().compareTo(aCitizen.getName());
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
/*     */   public String toString() {
/* 182 */     return "Citizen [wurmId=" + this.wurmId + ", name=" + this.name + ", role=" + this.role + "]";
/*     */   }
/*     */   
/*     */   public abstract void setRole(VillageRole paramVillageRole) throws IOException;
/*     */   
/*     */   abstract void setVoteDate(long paramLong) throws IOException;
/*     */   
/*     */   abstract void setVotedFor(long paramLong) throws IOException;
/*     */   
/*     */   abstract void create(Creature paramCreature, int paramInt) throws IOException;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\Citizen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */