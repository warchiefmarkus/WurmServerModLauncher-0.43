/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
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
/*     */ public final class DbCitizen
/*     */   extends Citizen
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(DbCitizen.class.getName());
/*     */   
/*     */   private static final String SET_ROLEID = "UPDATE CITIZENS SET ROLEID=? WHERE WURMID=?";
/*     */   
/*     */   private static final String CREATE_CITIZEN = "INSERT INTO CITIZENS (WURMID, VILLAGEID, ROLEID, VOTEDATE, VOTEDFOR) VALUES (?,?,?,?,?)";
/*     */   
/*     */   private static final String GET_CITIZEN = "SELECT * FROM CITIZENS WHERE WURMID=?";
/*     */   
/*     */   private static final String SET_VOTEDATE = "UPDATE CITIZENS SET VOTEDATE=? WHERE WURMID=?";
/*     */   private static final String SET_VOTEDFOR = "UPDATE CITIZENS SET VOTEDFOR=? WHERE WURMID=?";
/*     */   
/*     */   DbCitizen(long aWurmId, String aName, VillageRole aRole, long aVotedate, long aVotedfor) {
/*  47 */     super(aWurmId, aName, aRole, aVotedate, aVotedfor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void create(Creature creature, int villageId) throws IOException {
/*  57 */     Connection dbcon = null;
/*  58 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  61 */       dbcon = DbConnector.getZonesDbCon();
/*  62 */       int num = exists(dbcon);
/*  63 */       if (num >= 0) {
/*     */         
/*     */         try {
/*     */           
/*  67 */           Village village = Villages.getVillage(num);
/*  68 */           creature.getCommunicator().sendSafeServerMessage("Removing your " + village.getName() + " citizenship.");
/*  69 */           village.removeCitizen(creature);
/*     */         }
/*  71 */         catch (NoSuchVillageException nsv) {
/*     */           
/*  73 */           logger.log(Level.WARNING, "Citizens have village id " + num + " but it can't be found.", (Throwable)nsv);
/*     */         } 
/*     */       }
/*  76 */       ps = dbcon.prepareStatement("INSERT INTO CITIZENS (WURMID, VILLAGEID, ROLEID, VOTEDATE, VOTEDFOR) VALUES (?,?,?,?,?)");
/*  77 */       ps.setLong(1, this.wurmId);
/*  78 */       ps.setInt(2, villageId);
/*  79 */       ps.setInt(3, this.role.getId());
/*  80 */       ps.setLong(4, this.voteDate);
/*  81 */       ps.setLong(5, this.votedFor);
/*  82 */       ps.executeUpdate();
/*     */     }
/*  84 */     catch (SQLException sqx) {
/*     */       
/*  86 */       logger.log(Level.WARNING, "Failed to set status for citizen " + this.name + ": " + sqx.getMessage(), sqx);
/*  87 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  91 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  92 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRole(VillageRole aRole) throws IOException {
/* 103 */     if (this.role != aRole) {
/*     */       
/* 105 */       Connection dbcon = null;
/* 106 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 109 */         this.role = aRole;
/* 110 */         dbcon = DbConnector.getZonesDbCon();
/* 111 */         ps = dbcon.prepareStatement("UPDATE CITIZENS SET ROLEID=? WHERE WURMID=?");
/* 112 */         ps.setInt(1, aRole.getId());
/* 113 */         ps.setLong(2, this.wurmId);
/* 114 */         ps.executeUpdate();
/*     */       }
/* 116 */       catch (SQLException sqx) {
/*     */         
/* 118 */         logger.log(Level.WARNING, "Failed to set status for citizen " + this.name + ": " + sqx.getMessage(), sqx);
/* 119 */         throw new IOException(sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 123 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 124 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setVoteDate(long votedate) throws IOException {
/* 136 */     if (this.voteDate != votedate) {
/*     */       
/* 138 */       Connection dbcon = null;
/* 139 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 142 */         this.voteDate = votedate;
/* 143 */         dbcon = DbConnector.getZonesDbCon();
/* 144 */         ps = dbcon.prepareStatement("UPDATE CITIZENS SET VOTEDATE=? WHERE WURMID=?");
/* 145 */         ps.setLong(1, this.voteDate);
/* 146 */         ps.setLong(2, this.wurmId);
/* 147 */         ps.executeUpdate();
/*     */       }
/* 149 */       catch (SQLException sqx) {
/*     */         
/* 151 */         logger.log(Level.WARNING, "Failed to set vote date for citizen " + this.name + ": " + sqx.getMessage(), sqx);
/* 152 */         throw new IOException(sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 156 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 157 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setVotedFor(long votedfor) throws IOException {
/* 169 */     if (this.votedFor != votedfor) {
/*     */       
/* 171 */       Connection dbcon = null;
/* 172 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 175 */         this.votedFor = votedfor;
/* 176 */         dbcon = DbConnector.getZonesDbCon();
/* 177 */         ps = dbcon.prepareStatement("UPDATE CITIZENS SET VOTEDFOR=? WHERE WURMID=?");
/* 178 */         ps.setLong(1, this.votedFor);
/* 179 */         ps.setLong(2, this.wurmId);
/* 180 */         ps.executeUpdate();
/*     */       }
/* 182 */       catch (SQLException sqx) {
/*     */         
/* 184 */         logger.log(Level.WARNING, "Failed to set votedFor for citizen " + this.name + ": " + sqx.getMessage(), sqx);
/* 185 */         throw new IOException(sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 189 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 190 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int exists(Connection dbcon) throws IOException {
/* 197 */     PreparedStatement ps = null;
/* 198 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 201 */       ps = dbcon.prepareStatement("SELECT * FROM CITIZENS WHERE WURMID=?");
/* 202 */       ps.setLong(1, this.wurmId);
/* 203 */       rs = ps.executeQuery();
/* 204 */       if (rs.next()) {
/*     */         
/* 206 */         int village = rs.getInt("VILLAGEID");
/* 207 */         return village;
/*     */       } 
/*     */ 
/*     */       
/* 211 */       return -1;
/*     */     
/*     */     }
/* 214 */     catch (SQLException sqx) {
/*     */       
/* 216 */       logger.log(Level.WARNING, "Failed to check if citizen " + this.name + " exists: " + sqx.getMessage(), sqx);
/* 217 */       throw new IOException(sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 221 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DbCitizen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */