/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
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
/*     */ public final class WcEpicKarmaCommand
/*     */   extends WebCommand
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(WcEpicKarmaCommand.class.getName());
/*     */ 
/*     */   
/*     */   private long[] pids;
/*     */ 
/*     */   
/*     */   private int[] karmas;
/*     */ 
/*     */   
/*     */   private int deity;
/*     */   
/*     */   private static final String CLEAR_KARMA = "DELETE FROM HELPERS";
/*     */ 
/*     */   
/*     */   public WcEpicKarmaCommand(long _id, long[] playerids, int[] karmaValues, int _deity) {
/*  59 */     super(_id, (short)16);
/*  60 */     this.pids = playerids;
/*  61 */     this.karmas = karmaValues;
/*  62 */     this.deity = _deity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcEpicKarmaCommand(long _id, byte[] _data) {
/*  72 */     super(_id, (short)16, _data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] encode() {
/*  94 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  95 */     DataOutputStream dos = null;
/*  96 */     byte[] barr = null;
/*     */     
/*     */     try {
/*  99 */       dos = new DataOutputStream(bos);
/* 100 */       dos.writeInt(this.pids.length);
/* 101 */       dos.writeInt(this.deity);
/* 102 */       for (int x = 0; x < this.pids.length; x++) {
/*     */         
/* 104 */         dos.writeLong(this.pids[x]);
/* 105 */         dos.writeInt(this.karmas[x]);
/*     */       } 
/* 107 */       dos.flush();
/* 108 */       dos.close();
/*     */     }
/* 110 */     catch (Exception ex) {
/*     */       
/* 112 */       logger.log(Level.WARNING, "Problem encoding for Deity " + this.deity + " - " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 116 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 117 */       barr = bos.toByteArray();
/* 118 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 119 */       setData(barr);
/*     */     } 
/* 121 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 132 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 137 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 140 */             dis = new DataInputStream(new ByteArrayInputStream(WcEpicKarmaCommand.this.getData()));
/* 141 */             int nums = dis.readInt();
/* 142 */             int lDeity = dis.readInt();
/*     */ 
/*     */             
/* 145 */             Deity d = Deities.getDeity((lDeity == 3) ? 1 : lDeity);
/*     */             
/* 147 */             for (int x = 0; x < nums; x++) {
/*     */               
/* 149 */               long pid = dis.readLong();
/* 150 */               int val = dis.readInt();
/* 151 */               if (d != null) {
/* 152 */                 d.setPlayerKarma(pid, val);
/*     */               }
/*     */             } 
/* 155 */           } catch (IOException ex) {
/*     */             
/* 157 */             WcEpicKarmaCommand.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           }
/*     */           finally {
/*     */             
/* 161 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/* 164 */       }).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clearKarma() {
/* 169 */     for (Deity deity : Deities.getDeities())
/* 170 */       deity.clearKarma(); 
/* 171 */     Connection dbcon = null;
/* 172 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 175 */       dbcon = DbConnector.getDeityDbCon();
/* 176 */       ps = dbcon.prepareStatement("DELETE FROM HELPERS");
/* 177 */       ps.executeUpdate();
/* 178 */       ps.close();
/*     */     }
/* 180 */     catch (SQLException sqx) {
/*     */       
/* 182 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 186 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 187 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void loadAllKarmaHelpers() {
/* 193 */     for (Deity deity : Deities.getDeities())
/*     */     {
/* 195 */       deity.loadAllKarmaHelpers();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcEpicKarmaCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */