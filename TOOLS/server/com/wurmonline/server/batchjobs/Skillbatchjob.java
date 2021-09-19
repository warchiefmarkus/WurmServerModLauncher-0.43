/*     */ package com.wurmonline.server.batchjobs;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Skillbatchjob
/*     */ {
/*     */   private static final String setSkillKnow = "UPDATE SKILLS SET VALUE=? WHERE ID=?";
/*     */   private static final String changeNumber = "UPDATE SKILLS SET NUMBER=10049 WHERE NUMBER=1004";
/*     */   private static final String getIdsForFarming = "SELECT OWNER FROM SKILLS WHERE NUMBER=10049";
/*     */   private static final String createCreatureSkill = "insert into SKILLS (VALUE, LASTUSED, MINVALUE, NUMBER, OWNER ) values(?,?,?,?,?)";
/*  44 */   private static final Logger logger = Logger.getLogger(Skillbatchjob.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void runbatch() {
/*  55 */     PreparedStatement ps = null;
/*  56 */     Connection dbcon = null;
/*     */     
/*     */     try {
/*  59 */       dbcon = DbConnector.getPlayerDbCon();
/*  60 */       ps = dbcon.prepareStatement("UPDATE SKILLS SET NUMBER=10049 WHERE NUMBER=1004");
/*  61 */       ps.executeUpdate();
/*  62 */       ps.close();
/*  63 */       DbConnector.returnConnection(dbcon);
/*  64 */       dbcon = DbConnector.getCreatureDbCon();
/*  65 */       ps = dbcon.prepareStatement("UPDATE SKILLS SET NUMBER=10049 WHERE NUMBER=1004");
/*  66 */       ps.executeUpdate();
/*  67 */       ps.close();
/*     */     }
/*  69 */     catch (SQLException sqx) {
/*     */       
/*  71 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  75 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  76 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*  78 */     addNature();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addNature() {
/*  83 */     PreparedStatement ps = null;
/*  84 */     ResultSet rs = null;
/*  85 */     Connection dbcon = null;
/*     */     
/*     */     try {
/*  88 */       dbcon = DbConnector.getPlayerDbCon();
/*  89 */       ps = dbcon.prepareStatement("SELECT OWNER FROM SKILLS WHERE NUMBER=10049");
/*  90 */       rs = ps.executeQuery();
/*  91 */       long time = System.currentTimeMillis();
/*  92 */       while (rs.next()) {
/*     */         
/*  94 */         long owner = rs.getLong("OWNER");
/*  95 */         PreparedStatement ps2 = dbcon.prepareStatement("insert into SKILLS (VALUE, LASTUSED, MINVALUE, NUMBER, OWNER ) values(?,?,?,?,?)");
/*  96 */         ps2.setDouble(1, 1.0D);
/*  97 */         ps2.setLong(2, time);
/*  98 */         ps2.setDouble(3, 1.0D);
/*  99 */         ps2.setInt(4, 1019);
/* 100 */         ps2.setLong(5, owner);
/* 101 */         ps2.executeUpdate();
/* 102 */         ps2.close();
/*     */       } 
/* 104 */       rs.close();
/* 105 */       ps.close();
/* 106 */       DbConnector.returnConnection(dbcon);
/* 107 */       dbcon = DbConnector.getCreatureDbCon();
/* 108 */       ps = dbcon.prepareStatement("SELECT OWNER FROM SKILLS WHERE NUMBER=10049");
/* 109 */       rs = ps.executeQuery();
/* 110 */       while (rs.next())
/*     */       {
/* 112 */         long owner = rs.getLong("OWNER");
/* 113 */         PreparedStatement ps2 = dbcon.prepareStatement("insert into SKILLS (VALUE, LASTUSED, MINVALUE, NUMBER, OWNER ) values(?,?,?,?,?)");
/* 114 */         ps2.setDouble(1, 1.0D);
/* 115 */         ps2.setLong(2, time);
/* 116 */         ps2.setDouble(3, 1.0D);
/* 117 */         ps2.setInt(4, 1019);
/* 118 */         ps2.setLong(5, owner);
/* 119 */         ps2.executeUpdate();
/* 120 */         ps2.close();
/*     */       }
/*     */     
/* 123 */     } catch (SQLException sqx) {
/*     */       
/* 125 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 129 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 130 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void fixPlayer(long wurmid) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void modifySkillKnowledge(int id, int number, double knowledge) {
/* 141 */     if (number == 104 || number == 103 || number == 102 || number == 100 || number == 101 || number == 106 || number == 105) {
/*     */ 
/*     */       
/* 144 */       PreparedStatement ps = null;
/* 145 */       Connection dbcon = null;
/*     */       
/*     */       try {
/* 148 */         knowledge += 10.0D;
/* 149 */         dbcon = DbConnector.getPlayerDbCon();
/* 150 */         ps = dbcon.prepareStatement("UPDATE SKILLS SET VALUE=? WHERE ID=?");
/* 151 */         ps.setDouble(1, knowledge);
/* 152 */         ps.setInt(2, id);
/* 153 */         ps.executeUpdate();
/* 154 */         ps.close();
/*     */       }
/* 156 */       catch (SQLException sqx) {
/*     */         
/* 158 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 162 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 163 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\batchjobs\Skillbatchjob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */