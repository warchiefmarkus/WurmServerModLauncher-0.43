/*     */ package com.wurmonline;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import sun.misc.BASE64Encoder;
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
/*     */ public final class BatchEncrypter
/*     */ {
/*     */   private static final String getPlayers = "select * from PLAYERS";
/*     */   private static final String updatePw = "update PLAYERS set PASSWORD=? where NAME=?";
/*  39 */   private static Logger logger = Logger.getLogger(BatchEncrypter.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String destroyString = "ALTER TABLE PLAYERS DROP COLUMN PASSWORD";
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String createString = "ALTER TABLE PLAYERS ADD PASSWORD VARCHAR(30)";
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encrypt(String plaintext) throws Exception {
/*  52 */     MessageDigest md = null;
/*     */     
/*     */     try {
/*  55 */       md = MessageDigest.getInstance("SHA");
/*     */     }
/*  57 */     catch (NoSuchAlgorithmException e) {
/*     */       
/*  59 */       throw new WurmServerException("No such algorithm 'SHA'");
/*     */     } 
/*     */     
/*     */     try {
/*  63 */       md.update(plaintext.getBytes("UTF-8"));
/*     */     }
/*  65 */     catch (UnsupportedEncodingException e) {
/*     */       
/*  67 */       throw new WurmServerException("No such encoding: UTF-8");
/*     */     } 
/*  69 */     byte[] raw = md.digest();
/*  70 */     String hash = (new BASE64Encoder()).encode(raw);
/*  71 */     return hash;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void encryptPasswords() {
/*     */     try {
/*  78 */       Connection dbcon = DbConnector.getPlayerDbCon();
/*  79 */       PreparedStatement ps = dbcon.prepareStatement("select * from PLAYERS");
/*  80 */       ResultSet rs = ps.executeQuery();
/*  81 */       PreparedStatement destroy = dbcon.prepareStatement("ALTER TABLE PLAYERS DROP COLUMN PASSWORD");
/*  82 */       destroy.execute();
/*  83 */       destroy.close();
/*  84 */       PreparedStatement create = dbcon.prepareStatement("ALTER TABLE PLAYERS ADD PASSWORD VARCHAR(30)");
/*  85 */       create.execute();
/*  86 */       create.close();
/*  87 */       while (rs.next()) {
/*     */         
/*  89 */         String password = rs.getString("PASSWORD");
/*  90 */         String name = rs.getString("NAME");
/*  91 */         String newPw = "";
/*     */         
/*     */         try {
/*  94 */           newPw = encrypt(name + password);
/*     */         }
/*  96 */         catch (Exception ex) {
/*     */           
/*  98 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */         } 
/* 100 */         PreparedStatement ps2 = dbcon.prepareStatement("update PLAYERS set PASSWORD=? where NAME=?");
/*     */         
/* 102 */         ps2.setString(1, newPw);
/* 103 */         ps2.setString(2, name);
/* 104 */         ps2.executeUpdate();
/* 105 */         ps2.close();
/*     */       } 
/* 107 */       ps.close();
/* 108 */       DbConnector.closeAll();
/*     */     }
/* 110 */     catch (Exception ex) {
/*     */       
/* 112 */       logger.log(Level.INFO, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\BatchEncrypter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */