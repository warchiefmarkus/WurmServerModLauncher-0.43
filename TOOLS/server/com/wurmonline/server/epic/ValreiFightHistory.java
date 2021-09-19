/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.ValreiConstants;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValreiFightHistory
/*     */ {
/*  22 */   private static final Logger logger = Logger.getLogger(ValreiFightHistoryManager.class.getName());
/*     */ 
/*     */   
/*     */   private static final String LOAD_FIGHT_ACTIONS = "SELECT * FROM ENTITYFIGHTACTIONS WHERE FIGHTID=?";
/*     */ 
/*     */   
/*     */   private static final String SAVE_FIGHT_ACTION = "INSERT INTO ENTITYFIGHTACTIONS(FIGHTID,FIGHTACTIONNUM,ACTIONID,ACTIONDATA) VALUES (?,?,?,?)";
/*     */ 
/*     */   
/*     */   private final long fightId;
/*     */ 
/*     */   
/*     */   private int mapHexId;
/*     */ 
/*     */   
/*     */   private String mapHexName;
/*     */ 
/*     */   
/*     */   private long fightTime;
/*     */   
/*     */   private HashMap<Long, ValreiFighter> fighters;
/*     */   
/*     */   private HashMap<Integer, ValreiConstants.ValreiFightAction> allActions;
/*     */   
/*     */   private int fightActionNum;
/*     */   
/*     */   private boolean fightCompleted;
/*     */ 
/*     */   
/*     */   public ValreiFightHistory(int mapHexId, String mapHexName) {
/*  52 */     this.mapHexId = mapHexId;
/*  53 */     this.mapHexName = mapHexName;
/*  54 */     this.fightId = ValreiFightHistoryManager.getNextFightId();
/*     */     
/*  56 */     this.fighters = new HashMap<>();
/*  57 */     this.allActions = new HashMap<>();
/*  58 */     this.fightActionNum = 0;
/*  59 */     this.fightCompleted = false;
/*  60 */     this.fightTime = WurmCalendar.currentTime;
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
/*     */   public ValreiFightHistory(long fightId, int mapHexId, String mapHexName, long fightTime) {
/*  72 */     this.fightId = fightId;
/*  73 */     this.mapHexId = mapHexId;
/*  74 */     this.mapHexName = mapHexName;
/*  75 */     this.fightTime = fightTime;
/*     */     
/*  77 */     this.fighters = new HashMap<>();
/*  78 */     this.allActions = new HashMap<>();
/*  79 */     this.fightCompleted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveActions() {
/*  88 */     Connection dbcon = null;
/*  89 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  92 */       dbcon = DbConnector.getDeityDbCon();
/*  93 */       for (ValreiConstants.ValreiFightAction fa : this.allActions.values())
/*     */       {
/*  95 */         ps = dbcon.prepareStatement("INSERT INTO ENTITYFIGHTACTIONS(FIGHTID,FIGHTACTIONNUM,ACTIONID,ACTIONDATA) VALUES (?,?,?,?)");
/*  96 */         ps.setLong(1, this.fightId);
/*  97 */         ps.setInt(2, fa.getActionNum());
/*  98 */         ps.setShort(3, fa.getActionId());
/*  99 */         ps.setBytes(4, fa.getActionData());
/*     */         
/* 101 */         ps.executeUpdate();
/* 102 */         ps.close();
/*     */       }
/*     */     
/* 105 */     } catch (SQLException sqx) {
/*     */       
/* 107 */       logger.log(Level.WARNING, "Failed to save actions for this valrei fight: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 111 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 112 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadActions() {
/* 121 */     Connection dbcon = null;
/* 122 */     PreparedStatement ps = null;
/* 123 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 126 */       dbcon = DbConnector.getDeityDbCon();
/* 127 */       ps = dbcon.prepareStatement("SELECT * FROM ENTITYFIGHTACTIONS WHERE FIGHTID=?");
/* 128 */       ps.setLong(1, this.fightId);
/*     */       
/* 130 */       rs = ps.executeQuery();
/* 131 */       while (rs.next()) {
/*     */         
/* 133 */         int actionNum = rs.getInt("FIGHTACTIONNUM");
/* 134 */         short action = rs.getShort("ACTIONID");
/* 135 */         byte[] actionData = rs.getBytes("ACTIONDATA");
/*     */         
/* 137 */         if (this.fightActionNum < actionNum) {
/* 138 */           this.fightActionNum = actionNum;
/*     */         }
/* 140 */         this.allActions.put(Integer.valueOf(actionNum), new ValreiConstants.ValreiFightAction(actionNum, action, actionData));
/*     */       } 
/* 142 */       rs.close();
/* 143 */       ps.close();
/*     */     }
/* 145 */     catch (SQLException sqx) {
/*     */       
/* 147 */       logger.log(Level.WARNING, "Failed to load all valrei fights: " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 151 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 152 */       DbConnector.returnConnection(dbcon);
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
/*     */   public void addFighter(long fighterId, String fighterName) {
/* 164 */     if (this.fighters.get(Long.valueOf(fighterId)) == null) {
/* 165 */       this.fighters.put(Long.valueOf(fighterId), new ValreiFighter(fighterId, fighterName));
/*     */     } else {
/*     */       
/* 168 */       ValreiFighter f = this.fighters.get(Long.valueOf(fighterId));
/* 169 */       f.setName(fighterName);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public HashMap<Long, ValreiFighter> getFighters() {
/* 175 */     return this.fighters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAction(short actionType, byte[] actionData) {
/* 186 */     this.allActions.put(Integer.valueOf(this.fightActionNum), new ValreiConstants.ValreiFightAction(this.fightActionNum, actionType, actionData));
/* 187 */     this.fightActionNum++;
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
/*     */   public ValreiConstants.ValreiFightAction getFightAction(int actionNum) {
/* 199 */     return this.allActions.get(Integer.valueOf(actionNum));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreviewString() {
/* 209 */     if (!this.fighters.isEmpty()) {
/*     */       
/* 211 */       ArrayList<String> fighters = new ArrayList<>();
/* 212 */       for (ValreiFighter vf : this.fighters.values()) {
/* 213 */         fighters.add(vf.fighterName);
/*     */       }
/* 215 */       return (String)fighters.get(0) + " vs " + (String)fighters.get(1) + " at " + getMapHexName() + " on " + WurmCalendar.getDateFor(this.fightTime);
/*     */     } 
/*     */     
/* 218 */     return "Unknown fight on " + WurmCalendar.getDateFor(this.fightTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFightWinner() {
/* 223 */     byte[] actionData = ((ValreiConstants.ValreiFightAction)this.allActions.get(Integer.valueOf(this.fightActionNum))).getActionData();
/*     */     
/* 225 */     return ValreiConstants.getEndFightWinner(actionData);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFightId() {
/* 230 */     return this.fightId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMapHexId() {
/* 235 */     return this.mapHexId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMapHexName() {
/* 240 */     return this.mapHexName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFightCompleted() {
/* 245 */     return this.fightCompleted;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFightCompleted(boolean isCompleted) {
/* 250 */     this.fightCompleted = isCompleted;
/* 251 */     this.fightActionNum--;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getFightTime() {
/* 256 */     return this.fightTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalActions() {
/* 261 */     return this.fightActionNum;
/*     */   }
/*     */ 
/*     */   
/*     */   public class ValreiFighter
/*     */   {
/*     */     private long fighterId;
/*     */     
/*     */     private String fighterName;
/*     */ 
/*     */     
/*     */     ValreiFighter(long id, String name) {
/* 273 */       setFighterId(id);
/* 274 */       this.fighterName = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getName() {
/* 279 */       return this.fighterName;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setName(String newName) {
/* 284 */       this.fighterName = newName;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getFighterId() {
/* 289 */       return this.fighterId;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setFighterId(long fighterId) {
/* 294 */       this.fighterId = fighterId;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\ValreiFightHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */