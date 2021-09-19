/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class VillageMessages
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(VillageMessages.class.getName());
/*     */   
/*  42 */   private static final Map<Integer, VillageMessages> villagesMessages = new ConcurrentHashMap<>();
/*     */   
/*     */   private static final String LOAD_ALL_MSGS = "SELECT * FROM VILLAGEMESSAGES";
/*     */   
/*     */   private static final String DELETE_VILLAGE_MSGS = "DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=?";
/*     */   private static final String CREATE_MSG = "INSERT INTO VILLAGEMESSAGES (VILLAGEID,FROMID,TOID,MESSAGE,POSTED,PENCOLOR,EVERYONE) VALUES (?,?,?,?,?,?,?);";
/*     */   private static final String DELETE_MSG = "DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=? AND TOID=? AND POSTED=?";
/*     */   private static final String DELETE_PLAYER_MSGS = "DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=? AND TOID=?";
/*  50 */   private Map<Long, Map<Long, VillageMessage>> villageMsgs = new ConcurrentHashMap<>();
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
/*     */   public VillageMessage put(long toId, VillageMessage value) {
/*  65 */     Map<Long, VillageMessage> msgs = this.villageMsgs.get(Long.valueOf(toId));
/*  66 */     if (msgs == null) {
/*     */       
/*  68 */       msgs = new ConcurrentHashMap<>();
/*  69 */       this.villageMsgs.put(Long.valueOf(toId), msgs);
/*     */     } 
/*  71 */     return msgs.put(Long.valueOf(value.getPostedTime()), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Long, VillageMessage> get(long toId) {
/*  76 */     Map<Long, VillageMessage> msgs = this.villageMsgs.get(Long.valueOf(toId));
/*  77 */     if (msgs == null)
/*     */     {
/*  79 */       return new ConcurrentHashMap<>();
/*     */     }
/*  81 */     return msgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(long playerId, long posted) {
/*  91 */     Map<Long, VillageMessage> msgs = this.villageMsgs.get(Long.valueOf(playerId));
/*  92 */     if (msgs != null) {
/*  93 */       msgs.remove(Long.valueOf(posted));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(long playerId) {
/* 102 */     this.villageMsgs.remove(Long.valueOf(playerId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadVillageMessages() {
/* 110 */     long start = System.nanoTime();
/* 111 */     int loadedMsgs = 0;
/* 112 */     Connection dbcon = null;
/* 113 */     PreparedStatement ps = null;
/* 114 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 117 */       dbcon = DbConnector.getZonesDbCon();
/* 118 */       ps = dbcon.prepareStatement("SELECT * FROM VILLAGEMESSAGES");
/* 119 */       rs = ps.executeQuery();
/* 120 */       while (rs.next())
/*     */       {
/*     */ 
/*     */         
/* 124 */         VillageMessage villageMsg = new VillageMessage(rs.getInt("VILLAGEID"), rs.getLong("FROMID"), rs.getLong("TOID"), rs.getString("MESSAGE"), rs.getInt("PENCOLOR"), rs.getLong("POSTED"), rs.getBoolean("EVERYONE"));
/* 125 */         add(villageMsg);
/* 126 */         loadedMsgs++;
/*     */       }
/*     */     
/* 129 */     } catch (SQLException sqex) {
/*     */       
/* 131 */       logger.log(Level.WARNING, "Failed to load village messages due to " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 135 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 136 */       DbConnector.returnConnection(dbcon);
/* 137 */       long end = System.nanoTime();
/* 138 */       logger.info("Loaded " + loadedMsgs + " village messages from the database took " + ((float)(end - start) / 1000000.0F) + " ms");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void add(VillageMessage villageMsg) {
/* 149 */     VillageMessages villageMsgs = villagesMessages.get(Integer.valueOf(villageMsg.getVillageId()));
/* 150 */     if (villageMsgs == null) {
/*     */       
/* 152 */       villageMsgs = new VillageMessages();
/* 153 */       villagesMessages.put(Integer.valueOf(villageMsg.getVillageId()), villageMsgs);
/*     */     } 
/* 155 */     villageMsgs.put(villageMsg.getToId(), villageMsg);
/*     */   }
/*     */ 
/*     */   
/*     */   public static VillageMessage[] getVillageMessages(int villageId, long toId) {
/* 160 */     VillageMessages villageMsgs = villagesMessages.get(Integer.valueOf(villageId));
/* 161 */     if (villageMsgs == null)
/* 162 */       return new VillageMessage[0]; 
/* 163 */     return (VillageMessage[])villageMsgs.get(toId).values().toArray((Object[])new VillageMessage[villageMsgs.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int size() {
/* 169 */     return 0;
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
/*     */   public static final VillageMessage create(int villageId, long fromId, long toId, String message, int penColour, boolean everyone) {
/* 184 */     long posted = System.currentTimeMillis();
/* 185 */     dbCreate(villageId, fromId, toId, message, posted, penColour, everyone);
/* 186 */     VillageMessage villageMsg = new VillageMessage(villageId, fromId, toId, message, penColour, posted, everyone);
/* 187 */     add(villageMsg);
/* 188 */     return villageMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void delete(int villageId) {
/* 197 */     dbDelete(villageId);
/* 198 */     villagesMessages.remove(Integer.valueOf(villageId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final void delete(int villageId, long toId) {
/* 208 */     dbDelete(villageId, toId);
/* 209 */     VillageMessages villageMsgs = villagesMessages.get(Integer.valueOf(villageId));
/* 210 */     if (villageMsgs != null)
/*     */     {
/* 212 */       villageMsgs.remove(toId);
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
/*     */   public static final void delete(int villageId, long toId, long posted) {
/* 224 */     dbDelete(villageId, toId, posted);
/* 225 */     VillageMessages villageMsgs = villagesMessages.get(Integer.valueOf(villageId));
/* 226 */     if (villageMsgs != null)
/*     */     {
/* 228 */       villageMsgs.remove(toId, posted);
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
/*     */   private static final void dbCreate(int villageId, long fromId, long toId, String message, long posted, int penColour, boolean everyone) {
/* 243 */     Connection dbcon = null;
/* 244 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 247 */       dbcon = DbConnector.getZonesDbCon();
/* 248 */       ps = dbcon.prepareStatement("INSERT INTO VILLAGEMESSAGES (VILLAGEID,FROMID,TOID,MESSAGE,POSTED,PENCOLOR,EVERYONE) VALUES (?,?,?,?,?,?,?);");
/* 249 */       ps.setInt(1, villageId);
/* 250 */       ps.setLong(2, fromId);
/* 251 */       ps.setLong(3, toId);
/* 252 */       ps.setString(4, message);
/* 253 */       ps.setLong(5, posted);
/* 254 */       ps.setInt(6, penColour);
/* 255 */       ps.setBoolean(7, everyone);
/* 256 */       ps.executeUpdate();
/*     */     }
/* 258 */     catch (SQLException sqx) {
/*     */       
/* 260 */       logger.log(Level.WARNING, "Failed to create new message for village: " + villageId + ": " + sqx
/* 261 */           .getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 265 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 266 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void dbDelete(int villageId) {
/* 277 */     Connection dbcon = null;
/* 278 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 281 */       dbcon = DbConnector.getZonesDbCon();
/* 282 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=?");
/* 283 */       ps.setInt(1, villageId);
/* 284 */       ps.executeUpdate();
/*     */     }
/* 286 */     catch (SQLException sqx) {
/*     */       
/* 288 */       logger.log(Level.WARNING, "Failed to delete all messages for village: " + villageId + ": " + sqx
/* 289 */           .getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 293 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 294 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void dbDelete(int villageId, long toId) {
/* 305 */     Connection dbcon = null;
/* 306 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 309 */       dbcon = DbConnector.getZonesDbCon();
/* 310 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=? AND TOID=?");
/* 311 */       ps.setInt(1, villageId);
/* 312 */       ps.setLong(2, toId);
/* 313 */       ps.executeUpdate();
/*     */     }
/* 315 */     catch (SQLException sqx) {
/*     */       
/* 317 */       logger.log(Level.WARNING, "Failed to delete message for village " + villageId + ", and player " + toId + " : " + sqx
/* 318 */           .getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 322 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 323 */       DbConnector.returnConnection(dbcon);
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
/*     */   private static final void dbDelete(int villageId, long toId, long posted) {
/* 335 */     Connection dbcon = null;
/* 336 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 339 */       dbcon = DbConnector.getZonesDbCon();
/* 340 */       ps = dbcon.prepareStatement("DELETE FROM VILLAGEMESSAGES WHERE VILLAGEID=? AND TOID=? AND POSTED=?");
/* 341 */       ps.setInt(1, villageId);
/* 342 */       ps.setLong(2, toId);
/* 343 */       ps.setLong(3, posted);
/* 344 */       ps.executeUpdate();
/*     */     }
/* 346 */     catch (SQLException sqx) {
/*     */       
/* 348 */       logger.log(Level.WARNING, "Failed to delete message for village " + villageId + ", and player " + toId + " and posted " + posted + ": " + sqx
/* 349 */           .getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 353 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 354 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\VillageMessages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */