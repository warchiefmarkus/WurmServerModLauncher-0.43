/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
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
/*     */ public final class EpicScenario
/*     */   implements MiscConstants
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(EpicScenario.class.getName());
/*  39 */   private int collectiblesToWin = 5;
/*  40 */   private int collectiblesForWurmToWin = 8;
/*     */   private boolean spawnPointRequiredToWin = true;
/*  42 */   private int hexNumRequiredToWin = 0;
/*  43 */   private int scenarioNumber = 0;
/*  44 */   private int reasonPlusEffect = 0;
/*  45 */   private String scenarioName = "";
/*  46 */   private String scenarioQuest = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean current = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String INSERTSCENARIO = "INSERT INTO SCENARIOS (NAME,REASONEFF,COLLREQ,COLLWURMREQ,SPAWNREQ,HEXREQ,QUESTSTRING,CURRENT,NUMBER) VALUES (?,?,?,?,?,?,?,?,?)";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String UPDATESCENARIO = "UPDATE SCENARIOS SET NAME=?,REASONEFF=?,COLLREQ=?,COLLWURMREQ=?,SPAWNREQ=?,HEXREQ=?,QUESTSTRING=?,CURRENT=? WHERE NUMBER=?";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LOADCURRENTSCENARIO = "SELECT * FROM SCENARIOS WHERE CURRENT=1";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean loadCurrentScenario() {
/*  72 */     boolean toReturn = false;
/*  73 */     Connection dbcon = null;
/*  74 */     PreparedStatement ps = null;
/*  75 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  78 */       dbcon = DbConnector.getDeityDbCon();
/*  79 */       ps = dbcon.prepareStatement("SELECT * FROM SCENARIOS WHERE CURRENT=1");
/*  80 */       rs = ps.executeQuery();
/*  81 */       while (rs.next())
/*     */       {
/*  83 */         this.scenarioName = rs.getString("NAME");
/*  84 */         this.scenarioNumber = rs.getInt("NUMBER");
/*  85 */         this.reasonPlusEffect = rs.getInt("REASONEFF");
/*  86 */         this.collectiblesToWin = rs.getInt("COLLREQ");
/*  87 */         this.collectiblesForWurmToWin = rs.getInt("COLLWURMREQ");
/*  88 */         this.spawnPointRequiredToWin = rs.getBoolean("SPAWNREQ");
/*  89 */         this.hexNumRequiredToWin = rs.getInt("HEXREQ");
/*  90 */         this.scenarioQuest = rs.getString("QUESTSTRING");
/*  91 */         this.current = true;
/*  92 */         logger.log(Level.INFO, "Loaded current scenario " + this.scenarioName);
/*  93 */         toReturn = true;
/*     */       }
/*     */     
/*  96 */     } catch (SQLException sqx) {
/*     */       
/*  98 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 102 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 103 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 105 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveScenario(boolean _current) {
/* 110 */     Connection dbcon = null;
/* 111 */     PreparedStatement ps = null;
/* 112 */     this.current = _current;
/*     */     
/*     */     try {
/* 115 */       dbcon = DbConnector.getDeityDbCon();
/* 116 */       if (this.current) {
/* 117 */         ps = dbcon.prepareStatement("INSERT INTO SCENARIOS (NAME,REASONEFF,COLLREQ,COLLWURMREQ,SPAWNREQ,HEXREQ,QUESTSTRING,CURRENT,NUMBER) VALUES (?,?,?,?,?,?,?,?,?)");
/*     */       } else {
/* 119 */         ps = dbcon.prepareStatement("UPDATE SCENARIOS SET NAME=?,REASONEFF=?,COLLREQ=?,COLLWURMREQ=?,SPAWNREQ=?,HEXREQ=?,QUESTSTRING=?,CURRENT=? WHERE NUMBER=?");
/* 120 */       }  ps.setString(1, this.scenarioName);
/* 121 */       ps.setInt(2, this.reasonPlusEffect);
/* 122 */       ps.setInt(3, this.collectiblesToWin);
/* 123 */       ps.setInt(4, this.collectiblesForWurmToWin);
/* 124 */       ps.setBoolean(5, this.spawnPointRequiredToWin);
/* 125 */       ps.setInt(6, this.hexNumRequiredToWin);
/* 126 */       ps.setString(7, this.scenarioQuest);
/* 127 */       ps.setBoolean(8, this.current);
/* 128 */       ps.setInt(9, this.scenarioNumber);
/* 129 */       ps.executeUpdate();
/*     */     }
/* 131 */     catch (SQLException sqx) {
/*     */       
/* 133 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 137 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 138 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCollectiblesToWin() {
/* 149 */     return this.collectiblesToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCollectiblesToWin(int aCollectiblesToWin) {
/* 160 */     this.collectiblesToWin = aCollectiblesToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCollectiblesForWurmToWin() {
/* 170 */     return this.collectiblesForWurmToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCollectiblesForWurmToWin(int aCollectiblesForWurmToWin) {
/* 181 */     this.collectiblesForWurmToWin = aCollectiblesForWurmToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSpawnPointRequiredToWin() {
/* 191 */     if (getHexNumRequiredToWin() <= 0)
/* 192 */       return true; 
/* 193 */     return this.spawnPointRequiredToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawnPointRequiredToWin(boolean aSpawnPointRequiredToWin) {
/* 204 */     this.spawnPointRequiredToWin = aSpawnPointRequiredToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHexNumRequiredToWin() {
/* 214 */     return this.hexNumRequiredToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHexNumRequiredToWin(int aHexNumRequiredToWin) {
/* 225 */     this.hexNumRequiredToWin = aHexNumRequiredToWin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getScenarioNumber() {
/* 235 */     return this.scenarioNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScenarioNumber(int aScenarioNumber) {
/* 246 */     this.scenarioNumber = aScenarioNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void incrementScenarioNumber() {
/* 254 */     this.scenarioNumber++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReasonPlusEffect() {
/* 264 */     return this.reasonPlusEffect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReasonPlusEffect(int aReasonPlusEffect) {
/* 275 */     this.reasonPlusEffect = aReasonPlusEffect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScenarioName() {
/* 285 */     return this.scenarioName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScenarioName(String aScenarioName) {
/* 296 */     this.scenarioName = aScenarioName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getScenarioQuest() {
/* 306 */     return this.scenarioQuest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScenarioQuest(String aScenarioQuest) {
/* 317 */     this.scenarioQuest = aScenarioQuest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCurrent() {
/* 327 */     return this.current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrent(boolean aCurrent) {
/* 338 */     this.current = aCurrent;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicScenario.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */