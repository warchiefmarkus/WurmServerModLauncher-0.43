/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class Achievement
/*     */   implements MiscConstants
/*     */ {
/*     */   private final Timestamp dateAchieved;
/*     */   private final int achievement;
/*  49 */   private int counter = 0;
/*  50 */   private long holder = 0L;
/*  51 */   private int localId = -1;
/*  52 */   private static final String INSERT = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO ACHIEVEMENTS (PLAYER,ACHIEVEMENT,COUNTER) VALUES (?,?,?)" : "INSERT IGNORE INTO ACHIEVEMENTS (PLAYER,ACHIEVEMENT,COUNTER) VALUES (?,?,?)";
/*     */ 
/*     */   
/*  55 */   private static final String INSERT_TRANSFER = DbConnector.isUseSqlite() ? "INSERT OR IGNORE INTO ACHIEVEMENTS (PLAYER,ACHIEVEMENT,COUNTER,ADATE) VALUES (?,?,?,?)" : "INSERT IGNORE INTO ACHIEVEMENTS (PLAYER,ACHIEVEMENT,COUNTER,ADATE) VALUES (?,?,?,?)";
/*     */ 
/*     */   
/*     */   private static final String UPDATE_COUNTER = "UPDATE ACHIEVEMENTS SET COUNTER=? WHERE ID=?";
/*     */   
/*  60 */   private static final Logger logger = Logger.getLogger(Achievement.class.getName());
/*     */   
/*  62 */   private static final ConcurrentHashMap<Integer, AchievementTemplate> templates = new ConcurrentHashMap<>();
/*  63 */   protected static final List<AchievementTemplate> personalGoalSilverTemplates = new LinkedList<>();
/*  64 */   protected static final List<AchievementTemplate> personalGoalGoldTemplates = new LinkedList<>();
/*  65 */   protected static final List<AchievementTemplate> personalGoalDiamondTemplates = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Achievement(int aAchievement, Timestamp date, long achiever, int timesTriggered, int localid) {
/*  73 */     this.achievement = aAchievement;
/*  74 */     setHolder(achiever);
/*  75 */     this.dateAchieved = date;
/*  76 */     this.counter = timesTriggered;
/*  77 */     this.localId = localid;
/*  78 */     if (getTemplate(aAchievement) == null)
/*     */     {
/*  80 */       addTemplate(new AchievementTemplate(aAchievement, "Unknown", false));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Timestamp getDateAchieved() {
/*  91 */     return this.dateAchieved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getAchievement() {
/* 101 */     return this.achievement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getCounter() {
/* 111 */     return this.counter;
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
/*     */   final int[] setCounter(int aCounter) {
/* 123 */     if (this.counter != aCounter) {
/*     */       
/* 125 */       this.counter = aCounter;
/* 126 */       Connection dbcon = null;
/* 127 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 130 */         dbcon = DbConnector.getPlayerDbCon();
/* 131 */         ps = dbcon.prepareStatement("UPDATE ACHIEVEMENTS SET COUNTER=? WHERE ID=?");
/* 132 */         ps.setInt(1, this.counter);
/* 133 */         ps.setInt(2, this.localId);
/* 134 */         ps.executeUpdate();
/*     */       }
/* 136 */       catch (SQLException ex) {
/*     */         
/* 138 */         logger.log(Level.WARNING, "Failed to save achievement " + this.achievement + " counter " + aCounter + " for " + this.holder);
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 143 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 144 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/* 146 */       return getTriggeredAchievements();
/*     */     } 
/* 148 */     return EMPTY_INT_ARRAY;
/*     */   }
/*     */ 
/*     */   
/*     */   final int[] getTriggeredAchievements() {
/* 153 */     return getTemplate().getAchievementsTriggered();
/*     */   }
/*     */ 
/*     */   
/*     */   public final AchievementTemplate getTemplate() {
/* 158 */     return getTemplate(this.achievement);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getHolder() {
/* 168 */     return this.holder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void setHolder(long aHolder) {
/* 179 */     this.holder = aHolder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void create(boolean transfer) {
/* 187 */     Connection dbcon = null;
/* 188 */     PreparedStatement ps = null;
/* 189 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 192 */       dbcon = DbConnector.getPlayerDbCon();
/* 193 */       if (transfer) {
/* 194 */         ps = dbcon.prepareStatement(INSERT_TRANSFER, 1);
/*     */       } else {
/* 196 */         ps = dbcon.prepareStatement(INSERT, 1);
/* 197 */       }  ps.setLong(1, this.holder);
/* 198 */       ps.setInt(2, this.achievement);
/* 199 */       ps.setInt(3, this.counter);
/* 200 */       if (transfer)
/*     */       {
/* 202 */         if (DbConnector.isUseSqlite()) {
/*     */           
/* 204 */           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 205 */           String ts = sdf.format(getDateAchieved());
/* 206 */           ps.setString(4, ts);
/*     */         } else {
/*     */           
/* 209 */           ps.setTimestamp(4, getDateAchieved());
/*     */         }  } 
/* 211 */       ps.executeUpdate();
/* 212 */       rs = ps.getGeneratedKeys();
/* 213 */       if (rs.next())
/*     */       {
/* 215 */         this.localId = rs.getInt(1);
/*     */       }
/*     */     }
/* 218 */     catch (SQLException ex) {
/*     */       
/* 220 */       logger.log(Level.WARNING, "Failed to save achievement " + this.achievement + " for " + this.holder, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 224 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 225 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 227 */     Achievements.addAchievement(this, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendNewAchievement(Creature creature) {
/* 232 */     creature.getCommunicator().sendAchievement(this, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendUpdateAchievement(Creature creature) {
/* 237 */     creature.getCommunicator().sendAchievement(this, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendUpdatePersonalGoal(Creature creature) {
/* 242 */     creature.getCommunicator().updatePersonalGoal(this, true);
/*     */   }
/*     */ 
/*     */   
/*     */   final int getTriggerOnCounter() {
/* 247 */     return getTemplate().getTriggerOnCounter();
/*     */   }
/*     */ 
/*     */   
/*     */   static final void addTemplate(AchievementTemplate template) {
/* 252 */     templates.put(Integer.valueOf(template.getNumber()), template);
/* 253 */     if (template.isPersonalGoal())
/*     */     {
/* 255 */       if (template.getType() == 3) {
/* 256 */         personalGoalSilverTemplates.add(template);
/* 257 */       } else if (template.getType() == 4) {
/* 258 */         personalGoalGoldTemplates.add(template);
/* 259 */       } else if (template.getType() == 5) {
/* 260 */         personalGoalDiamondTemplates.add(template);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final void removeTemplate(AchievementTemplate template) {
/* 266 */     templates.remove(Integer.valueOf(template.getNumber()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static final AchievementTemplate getTemplate(int number) {
/* 272 */     return templates.get(Integer.valueOf(number));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static final AchievementTemplate getTemplate(String name) {
/* 283 */     for (AchievementTemplate achievement : templates.values()) {
/*     */       
/* 285 */       if (achievement.getName().equalsIgnoreCase(name))
/*     */       {
/* 287 */         return achievement;
/*     */       }
/*     */     } 
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isInVisible() {
/* 295 */     return getTemplate().isInvisible();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPlaySoundOnUpdate() {
/* 300 */     return getTemplate().isPlaySoundOnUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isOneTimer() {
/* 305 */     return getTemplate().isOneTimer();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final LinkedList<AchievementTemplate> getSteelAchievements(Creature creature) {
/* 310 */     LinkedList<AchievementTemplate> toReturn = new LinkedList<>();
/* 311 */     for (AchievementTemplate achievement : templates.values()) {
/*     */       
/* 313 */       if (achievement.getType() == 2)
/*     */       {
/* 315 */         if (creature.getPower() > 0 || creature
/* 316 */           .getName().equalsIgnoreCase(achievement.getCreator().toLowerCase()))
/*     */         {
/* 318 */           toReturn.add(achievement);
/*     */         }
/*     */       }
/*     */     } 
/* 322 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final AchievementTemplate getRandomPersonalGoldAchievement(Random personalRandom) {
/* 327 */     int num = personalRandom.nextInt(personalGoalGoldTemplates.size());
/* 328 */     return personalGoalGoldTemplates.get(num);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final AchievementTemplate getRandomPersonalSilverAchievement(Random personalRandom) {
/* 333 */     int num = personalRandom.nextInt(personalGoalSilverTemplates.size());
/* 334 */     return personalGoalSilverTemplates.get(num);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final AchievementTemplate getRandomPersonalDiamondAchievement(Random personalRandom) {
/* 339 */     int num = personalRandom.nextInt(personalGoalDiamondTemplates.size());
/* 340 */     return personalGoalDiamondTemplates.get(num);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConcurrentHashMap<Integer, AchievementTemplate> getAllTemplates() {
/* 345 */     return templates;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getAchievementProgress(long wurmId, int achievementNum) {
/* 350 */     AchievementTemplate t = getTemplate(achievementNum);
/* 351 */     if (t == null) {
/* 352 */       return 0.0F;
/*     */     }
/* 354 */     return t.getProgressFor(wurmId);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Achievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */