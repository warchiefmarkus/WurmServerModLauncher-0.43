/*     */ package com.wurmonline.server.utils;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class PlayerPositionDatabaseUpdater
/*     */   extends DatabaseUpdater<PlayerPositionDbUpdatable>
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(PlayerPositionDatabaseUpdater.class.getName());
/*  42 */   private final Map<Long, PlayerPositionDbUpdatable> updatesMap = new ConcurrentHashMap<>();
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
/*     */   public PlayerPositionDatabaseUpdater(String aUpdaterDescription, int aMaxUpdatablesToRemovePerCycle) {
/*  54 */     super(aUpdaterDescription, PlayerPositionDbUpdatable.class, aMaxUpdatablesToRemovePerCycle);
/*  55 */     logger.info("Creating Player Position Updater.");
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
/*     */   Connection getDatabaseConnection() throws SQLException {
/*  67 */     return DbConnector.getPlayerDbCon();
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
/*     */   public void addToQueue(PlayerPositionDbUpdatable updatable) {
/*  79 */     if (updatable != null) {
/*     */       
/*  81 */       PlayerPositionDbUpdatable waiting = this.updatesMap.get(Long.valueOf(updatable.getId()));
/*     */ 
/*     */       
/*  84 */       if (waiting != null)
/*     */       {
/*  86 */         this.queue.remove(waiting);
/*     */       }
/*  88 */       this.updatesMap.put(Long.valueOf(updatable.getId()), updatable);
/*  89 */       this.queue.add(updatable);
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
/*     */   void addUpdatableToBatch(PreparedStatement updateStatement, PlayerPositionDbUpdatable aDbUpdatable) throws SQLException {
/* 102 */     this.updatesMap.remove(Long.valueOf(aDbUpdatable.getId()));
/* 103 */     updateStatement.setFloat(1, aDbUpdatable.getPositionX());
/* 104 */     updateStatement.setFloat(2, aDbUpdatable.getPositionY());
/* 105 */     updateStatement.setFloat(3, aDbUpdatable.getPositionZ());
/* 106 */     float rot = Player.normalizeAngle(aDbUpdatable.getRotation());
/* 107 */     updateStatement.setFloat(4, rot);
/* 108 */     updateStatement.setInt(5, aDbUpdatable.getZoneid());
/* 109 */     updateStatement.setInt(6, aDbUpdatable.getLayer());
/* 110 */     updateStatement.setLong(7, aDbUpdatable.getBridgeId());
/* 111 */     updateStatement.setLong(8, aDbUpdatable.getId());
/* 112 */     updateStatement.addBatch();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\PlayerPositionDatabaseUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */