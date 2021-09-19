/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValreiFightHistoryManager
/*     */ {
/*  22 */   private static final Logger logger = Logger.getLogger(ValreiFightHistoryManager.class.getName());
/*     */ 
/*     */   
/*     */   private static final String GET_MAX_FIGHTID = "SELECT MAX(FIGHTID) FROM ENTITYFIGHTS";
/*     */   
/*     */   private static final String LOAD_FIGHTS = "SELECT * FROM ENTITYFIGHTS";
/*     */   
/*     */   private static final String SAVE_FIGHT = "INSERT INTO ENTITYFIGHTS(FIGHTID,MAPHEXID,MAPHEXNAME,FIGHTTIME,FIGHTER1ID,FIGHTER1NAME,FIGHTER2ID,FIGHTER2NAME) VALUES (?,?,?,?,?,?,?,?)";
/*     */   
/*     */   private static ValreiFightHistoryManager instance;
/*     */   
/*  33 */   private static long fightIdCounter = 1L;
/*     */   private TreeMap<Long, ValreiFightHistory> allFights;
/*     */   
/*     */   static {
/*  37 */     Connection dbcon = null;
/*  38 */     PreparedStatement ps = null;
/*  39 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  42 */       dbcon = DbConnector.getDeityDbCon();
/*  43 */       ps = dbcon.prepareStatement("SELECT MAX(FIGHTID) FROM ENTITYFIGHTS");
/*     */       
/*  45 */       rs = ps.executeQuery();
/*  46 */       if (rs.next())
/*  47 */         fightIdCounter = rs.getLong("MAX(FIGHTID)"); 
/*  48 */       rs.close();
/*  49 */       ps.close();
/*     */     }
/*  51 */     catch (SQLException sqx) {
/*     */       
/*  53 */       logger.log(Level.WARNING, "Failed to load max fight id: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  57 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  58 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getNextFightId() {
/*  64 */     return ++fightIdCounter;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ValreiFightHistoryManager getInstance() {
/*  69 */     if (instance == null) {
/*  70 */       instance = new ValreiFightHistoryManager();
/*     */     }
/*  72 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ValreiFightHistoryManager() {
/*  79 */     loadAllFights();
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveFight(long fightId, ValreiFightHistory newFight) {
/*  84 */     Connection dbcon = null;
/*  85 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  88 */       dbcon = DbConnector.getDeityDbCon();
/*  89 */       ps = dbcon.prepareStatement("INSERT INTO ENTITYFIGHTS(FIGHTID,MAPHEXID,MAPHEXNAME,FIGHTTIME,FIGHTER1ID,FIGHTER1NAME,FIGHTER2ID,FIGHTER2NAME) VALUES (?,?,?,?,?,?,?,?)");
/*     */       
/*  91 */       ps.setLong(1, fightId);
/*  92 */       ps.setInt(2, newFight.getMapHexId());
/*  93 */       ps.setString(3, newFight.getMapHexName());
/*  94 */       ps.setLong(4, newFight.getFightTime());
/*     */       
/*  96 */       HashMap<Long, ValreiFightHistory.ValreiFighter> fighters = newFight.getFighters();
/*  97 */       if (fighters.size() >= 2) {
/*     */         
/*  99 */         int val = 5;
/* 100 */         for (ValreiFightHistory.ValreiFighter f : fighters.values()) {
/*     */           
/* 102 */           ps.setLong(val++, f.getFighterId());
/* 103 */           ps.setString(val++, f.getName());
/*     */           
/* 105 */           if (val >= 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 111 */         ps.setLong(5, -1L);
/* 112 */         ps.setString(6, "Unknown");
/* 113 */         ps.setLong(7, -1L);
/* 114 */         ps.setString(8, "Unknown");
/*     */       } 
/*     */       
/* 117 */       ps.executeUpdate();
/* 118 */       ps.close();
/*     */     }
/* 120 */     catch (SQLException sqx) {
/*     */       
/* 122 */       logger.log(Level.WARNING, "Failed to save valrei fight: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 126 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 127 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadAllFights() {
/* 133 */     this.allFights = new TreeMap<>();
/*     */     
/* 135 */     Connection dbcon = null;
/* 136 */     PreparedStatement ps = null;
/* 137 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 140 */       dbcon = DbConnector.getDeityDbCon();
/* 141 */       ps = dbcon.prepareStatement("SELECT * FROM ENTITYFIGHTS");
/*     */       
/* 143 */       rs = ps.executeQuery();
/* 144 */       while (rs.next()) {
/*     */         
/* 146 */         long fightId = rs.getLong("FIGHTID");
/* 147 */         int mapHexId = rs.getInt("MAPHEXID");
/* 148 */         String mapHexName = rs.getString("MAPHEXNAME");
/* 149 */         long fightTime = rs.getLong("FIGHTTIME");
/* 150 */         long fighter1 = rs.getLong("FIGHTER1ID");
/* 151 */         String fighter1Name = rs.getString("FIGHTER1NAME");
/* 152 */         long fighter2 = rs.getLong("FIGHTER2ID");
/* 153 */         String fighter2Name = rs.getString("FIGHTER2NAME");
/*     */         
/* 155 */         ValreiFightHistory oldFight = new ValreiFightHistory(fightId, mapHexId, mapHexName, fightTime);
/* 156 */         oldFight.addFighter(fighter1, fighter1Name);
/* 157 */         oldFight.addFighter(fighter2, fighter2Name);
/* 158 */         oldFight.loadActions();
/*     */         
/* 160 */         this.allFights.put(Long.valueOf(fightId), oldFight);
/*     */       } 
/* 162 */       rs.close();
/* 163 */       ps.close();
/*     */     }
/* 165 */     catch (SQLException sqx) {
/*     */       
/* 167 */       logger.log(Level.WARNING, "Failed to load all valrei fights: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 171 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 172 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumberOfFights() {
/* 178 */     return this.allFights.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFight(long fightId, ValreiFightHistory newFight) {
/* 189 */     addFight(fightId, newFight, true);
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
/*     */   public void addFight(long fightId, ValreiFightHistory newFight, boolean save) {
/* 201 */     this.allFights.put(Long.valueOf(fightId), newFight);
/*     */     
/* 203 */     if (save) {
/* 204 */       saveFight(fightId, newFight);
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
/*     */   public ValreiFightHistory getFight(long fightId) {
/* 216 */     return this.allFights.get(Long.valueOf(fightId));
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
/*     */   public ArrayList<ValreiFightHistory> get10Fights(int listPage) {
/* 228 */     if (this.allFights.size() / 10 < listPage) {
/* 229 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 233 */       ArrayList<ValreiFightHistory> toReturn = new ArrayList<>();
/* 234 */       long finalKey = ((Long)this.allFights.lastKey()).longValue(); int i;
/* 235 */       for (i = 0; i < listPage; i++) {
/* 236 */         for (int k = 0; k < 10 && 
/* 237 */           this.allFights.lowerKey(Long.valueOf(finalKey)) != null; k++) {
/* 238 */           finalKey = ((Long)this.allFights.lowerKey(Long.valueOf(finalKey))).longValue();
/*     */         }
/*     */       } 
/*     */       
/* 242 */       for (i = 0; i < 10; ) {
/*     */         
/* 244 */         toReturn.add(this.allFights.get(Long.valueOf(finalKey)));
/* 245 */         if (this.allFights.lowerKey(Long.valueOf(finalKey)) != null) {
/* 246 */           finalKey = ((Long)this.allFights.lowerKey(Long.valueOf(finalKey))).longValue();
/*     */           
/*     */           i++;
/*     */         } 
/*     */       } 
/* 251 */       return toReturn;
/*     */     }
/* 253 */     catch (NoSuchElementException ne) {
/*     */       
/* 255 */       logger.log(Level.WARNING, "Unable to load 10 fights for page " + listPage + ". No key exists in allFights map.");
/*     */ 
/*     */       
/* 258 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public ArrayList<ValreiFightHistory> getAllFights() {
/* 263 */     return new ArrayList<>(this.allFights.values());
/*     */   }
/*     */ 
/*     */   
/*     */   public ValreiFightHistory getLatestFight() {
/* 268 */     return this.allFights.get(this.allFights.lastKey());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\ValreiFightHistoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */