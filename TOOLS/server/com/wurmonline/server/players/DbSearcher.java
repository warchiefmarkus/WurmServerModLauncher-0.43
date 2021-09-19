/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
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
/*     */ public final class DbSearcher
/*     */ {
/*     */   private static final String getPlayerName = "select * from PLAYERS where WURMID=?";
/*     */   private static final String getPlayerId = "select * from PLAYERS where NAME=?";
/*     */   
/*     */   public static String getNameForPlayer(long wurmId) throws IOException, NoSuchPlayerException {
/*  61 */     Connection dbcon = null;
/*  62 */     PreparedStatement ps = null;
/*  63 */     ResultSet rs = null;
/*     */     
/*     */     try {
/*  66 */       dbcon = DbConnector.getPlayerDbCon();
/*  67 */       ps = dbcon.prepareStatement("select * from PLAYERS where WURMID=?");
/*  68 */       ps.setLong(1, wurmId);
/*  69 */       rs = ps.executeQuery();
/*  70 */       if (rs.next()) {
/*     */         
/*  72 */         String name = rs.getString("NAME");
/*  73 */         return name;
/*     */       } 
/*     */ 
/*     */       
/*  77 */       throw new NoSuchPlayerException("No player with id " + wurmId);
/*     */     
/*     */     }
/*  80 */     catch (SQLException sqx) {
/*     */       
/*  82 */       throw new IOException("Problem finding Player ID " + wurmId, sqx);
/*     */     }
/*     */     finally {
/*     */       
/*  86 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  87 */       DbConnector.returnConnection(dbcon);
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
/*     */ 
/*     */   
/*     */   public static long getWurmIdForPlayer(String name) throws IOException, NoSuchPlayerException {
/* 104 */     Connection dbcon = null;
/* 105 */     PreparedStatement ps = null;
/* 106 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 109 */       dbcon = DbConnector.getPlayerDbCon();
/* 110 */       ps = dbcon.prepareStatement("select * from PLAYERS where NAME=?");
/* 111 */       ps.setString(1, name);
/* 112 */       rs = ps.executeQuery();
/* 113 */       if (rs.next()) {
/*     */         
/* 115 */         long id = rs.getLong("WURMID");
/* 116 */         return id;
/*     */       } 
/*     */ 
/*     */       
/* 120 */       throw new NoSuchPlayerException("No player with name " + name);
/*     */     
/*     */     }
/* 123 */     catch (SQLException sqx) {
/*     */       
/* 125 */       throw new IOException("Problem finding Player name " + name, sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 129 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 130 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\DbSearcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */