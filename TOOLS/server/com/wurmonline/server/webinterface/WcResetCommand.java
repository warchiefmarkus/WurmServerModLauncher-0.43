/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
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
/*     */ public class WcResetCommand
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(WcResetCommand.class.getName());
/*  40 */   private long pid = -10L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcResetCommand(long _id, long playerid) {
/*  47 */     super(_id, (short)6);
/*  48 */     this.pid = playerid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcResetCommand(long _id, byte[] _data) {
/*  56 */     super(_id, (short)6, _data);
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
/*  67 */     return true;
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
/*  78 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  79 */     DataOutputStream dos = null;
/*  80 */     byte[] barr = null;
/*     */     
/*     */     try {
/*  83 */       dos = new DataOutputStream(bos);
/*  84 */       dos.writeLong(this.pid);
/*  85 */       dos.flush();
/*  86 */       dos.close();
/*     */     }
/*  88 */     catch (Exception ex) {
/*     */       
/*  90 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/*  94 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/*  95 */       barr = bos.toByteArray();
/*  96 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/*  97 */       setData(barr);
/*     */     } 
/*  99 */     return barr;
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
/* 110 */     DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 115 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 116 */       this.pid = dis.readLong();
/* 117 */       Players.getInstance().resetPlayer(this.pid);
/*     */     }
/* 119 */     catch (IOException ex) {
/*     */       
/* 121 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 125 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcResetCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */