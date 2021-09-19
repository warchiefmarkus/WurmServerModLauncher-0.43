/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.Creatures;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
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
/*     */ 
/*     */ 
/*     */ public final class WcKillCommand
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(WcKillCommand.class.getName());
/*     */   
/*     */   private long wurmID;
/*     */   
/*     */   public WcKillCommand(long _id, long _wurmID) {
/*  49 */     super(_id, (short)36);
/*  50 */     this.wurmID = _wurmID;
/*     */   }
/*     */ 
/*     */   
/*     */   WcKillCommand(long _id, byte[] _data) {
/*  55 */     super(_id, (short)36, _data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] encode() {
/*     */     byte[] barr;
/*  75 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  76 */     DataOutputStream dos = null;
/*     */ 
/*     */     
/*     */     try {
/*  80 */       dos = new DataOutputStream(bos);
/*  81 */       dos.writeLong(this.wurmID);
/*  82 */       dos.flush();
/*  83 */       dos.close();
/*     */     }
/*  85 */     catch (Exception ex) {
/*     */       
/*  87 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/*  91 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/*  92 */       barr = bos.toByteArray();
/*  93 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/*  94 */       setData(barr);
/*     */     } 
/*  96 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 106 */     (new Thread(() -> {
/*     */           DataInputStream dis = null;
/*     */ 
/*     */           
/*     */           try {
/*     */             dis = new DataInputStream(new ByteArrayInputStream(getData()));
/*     */             
/*     */             this.wurmID = dis.readLong();
/* 114 */           } catch (IOException ex) {
/*     */             logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */ 
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/*     */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */ 
/*     */           
/*     */           try {
/*     */             Creature animal = Creatures.getInstance().getCreature(this.wurmID);
/*     */ 
/*     */             
/*     */             animal.die(true, "Died on another server.", true);
/* 130 */           } catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */           
/*     */           if (Servers.isThisLoginServer()) {
/*     */             WcKillCommand wkc = new WcKillCommand(getWurmId(), this.wurmID);
/*     */ 
/*     */             
/*     */             wkc.sendFromLoginServer();
/*     */           } 
/* 139 */         })).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcKillCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */