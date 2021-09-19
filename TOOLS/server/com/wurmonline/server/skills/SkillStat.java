/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
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
/*     */ public final class SkillStat
/*     */   implements TimeConstants
/*     */ {
/*  41 */   private static Logger logger = Logger.getLogger(SkillStat.class.getName());
/*     */   
/*  43 */   public final Map<Long, Double> stats = new HashMap<>();
/*     */   @GuardedBy("RW_LOCK")
/*  45 */   private static final Map<Integer, SkillStat> allStats = new HashMap<>();
/*     */   
/*  47 */   private static final ReentrantReadWriteLock RW_LOCK = new ReentrantReadWriteLock();
/*     */ 
/*     */ 
/*     */   
/*     */   private final String skillName;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int skillnum;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String loadAllPlayerSkills = "select NUMBER,OWNER,VALUE from SKILLS sk INNER JOIN PLAYERS p ON p.WURMID=sk.OWNER AND p.CURRENTSERVER=? WHERE sk.VALUE>25 ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SkillStat(int num, String name) {
/*  65 */     this.skillName = name;
/*  66 */     this.skillnum = num;
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
/*     */   private static int loadAllStats() {
/*  87 */     Connection dbcon = null;
/*  88 */     int numberSkillsLoaded = 0;
/*  89 */     PreparedStatement ps = null;
/*  90 */     ResultSet rs = null;
/*     */ 
/*     */     
/*     */     try {
/*  94 */       dbcon = DbConnector.getPlayerDbCon();
/*  95 */       ps = dbcon.prepareStatement("select NUMBER,OWNER,VALUE from SKILLS sk INNER JOIN PLAYERS p ON p.WURMID=sk.OWNER AND p.CURRENTSERVER=? WHERE sk.VALUE>25 ");
/*  96 */       ps.setInt(1, Servers.localServer.id);
/*  97 */       rs = ps.executeQuery();
/*  98 */       while (rs.next())
/*     */       {
/* 100 */         SkillStat sk = getSkillStatForSkill(rs.getInt("NUMBER"));
/* 101 */         if (sk != null)
/* 102 */           sk.stats.put(new Long(rs.getLong("OWNER")), new Double(rs.getDouble("VALUE"))); 
/* 103 */         numberSkillsLoaded++;
/*     */       }
/*     */     
/* 106 */     } catch (SQLException sqx) {
/*     */       
/* 108 */       logger.log(Level.WARNING, "Problem loading the Skill stats due to " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 112 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 113 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 115 */     return numberSkillsLoaded;
/*     */   }
/*     */ 
/*     */   
/*     */   static final void addSkill(int skillNum, String name) {
/* 120 */     RW_LOCK.writeLock().lock();
/*     */     
/*     */     try {
/* 123 */       allStats.put(Integer.valueOf(skillNum), new SkillStat(skillNum, name));
/*     */     }
/*     */     finally {
/*     */       
/* 127 */       RW_LOCK.writeLock().unlock();
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
/*     */   public static final void pollSkills() {
/* 141 */     Thread statsPoller = new Thread("StatsPoller")
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void run()
/*     */         {
/*     */           try {
/* 152 */             long now = System.currentTimeMillis();
/* 153 */             int numberSkillsLoaded = SkillStat.loadAllStats();
/* 154 */             SkillStat.logger.log(Level.WARNING, "Polling " + numberSkillsLoaded + " skills for stats v2 took " + (System.currentTimeMillis() - now) + " ms.");
/*     */           }
/* 156 */           catch (RuntimeException e) {
/*     */             
/* 158 */             SkillStat.logger.log(Level.WARNING, e.getMessage(), e);
/*     */           } 
/*     */         }
/*     */       };
/* 162 */     statsPoller.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final SkillStat getSkillStatForSkill(int num) {
/* 167 */     RW_LOCK.readLock().lock();
/*     */     
/*     */     try {
/* 170 */       return allStats.get(Integer.valueOf(num));
/*     */     }
/*     */     finally {
/*     */       
/* 174 */       RW_LOCK.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\SkillStat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */