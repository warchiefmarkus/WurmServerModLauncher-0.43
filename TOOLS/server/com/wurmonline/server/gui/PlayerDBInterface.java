/*     */ package com.wurmonline.server.gui;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ public class PlayerDBInterface
/*     */ {
/*     */   private static final String GET_ALL_PLAYERS = "SELECT * FROM PLAYERS";
/*     */   private static final String GET_ALL_POSITION = "SELECT * FROM POSITION";
/*  43 */   private static final Logger logger = Logger.getLogger(PlayerDBInterface.class.getName());
/*  44 */   private static final ConcurrentHashMap<String, PlayerData> playerDatas = new ConcurrentHashMap<>();
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
/*     */   public static final void loadAllData() {
/*  56 */     playerDatas.clear();
/*  57 */     Connection dbcon = null;
/*  58 */     PreparedStatement ps = null;
/*  59 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  62 */       dbcon = DbConnector.getPlayerDbCon();
/*  63 */       ps = dbcon.prepareStatement("SELECT * FROM PLAYERS");
/*  64 */       rs = ps.executeQuery();
/*  65 */       while (rs.next())
/*     */       {
/*  67 */         String name = rs.getString("NAME");
/*  68 */         name = LoginHandler.raiseFirstLetter(name);
/*  69 */         PlayerData pinf = new PlayerData();
/*  70 */         pinf.setName(name);
/*  71 */         pinf.setWurmid(rs.getLong("WURMID"));
/*  72 */         pinf.setPower(rs.getByte("POWER"));
/*  73 */         pinf.setServer(rs.getInt("CURRENTSERVER"));
/*  74 */         pinf.setUndeadType(rs.getByte("UNDEADTYPE"));
/*  75 */         playerDatas.put(name, pinf);
/*     */       }
/*     */     
/*  78 */     } catch (SQLException ex) {
/*     */       
/*  80 */       logger.log(Level.WARNING, "Failed to load all player data.", ex);
/*     */     }
/*     */     finally {
/*     */       
/*  84 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  85 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final PlayerData getFromWurmId(long wurmid) {
/*  91 */     for (PlayerData pd : playerDatas.values()) {
/*     */       
/*  93 */       if (pd.getWurmid() == wurmid)
/*  94 */         return pd; 
/*     */     } 
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllPositionData() {
/* 101 */     Connection dbcon = null;
/* 102 */     PreparedStatement ps = null;
/* 103 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 106 */       dbcon = DbConnector.getPlayerDbCon();
/* 107 */       ps = dbcon.prepareStatement("SELECT * FROM POSITION");
/* 108 */       rs = ps.executeQuery();
/* 109 */       while (rs.next()) {
/*     */         
/* 111 */         long wurmid = rs.getLong("WURMID");
/* 112 */         PlayerData pd = getFromWurmId(wurmid);
/* 113 */         if (pd != null)
/*     */         {
/* 115 */           pd.setPosx(rs.getFloat("POSX"));
/* 116 */           pd.setPosy(rs.getFloat("POSY"));
/*     */         }
/*     */       
/*     */       } 
/* 120 */     } catch (SQLException ex) {
/*     */       
/* 122 */       logger.log(Level.WARNING, "Failed to load all player data.", ex);
/*     */     }
/*     */     finally {
/*     */       
/* 126 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 127 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final PlayerData[] getAllData() {
/* 133 */     return (PlayerData[])playerDatas.values().toArray((Object[])new PlayerData[playerDatas.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final PlayerData getPlayerData(String name) {
/* 138 */     return playerDatas.get(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\PlayerDBInterface.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */