/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
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
/*     */ public class WcDeleteKingdom
/*     */   extends WebCommand
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(WcDeleteKingdom.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private byte kingdomId;
/*     */ 
/*     */ 
/*     */   
/*     */   public WcDeleteKingdom(long aId, byte kingdomToDelete) {
/*  50 */     super(aId, (short)8);
/*  51 */     this.kingdomId = kingdomToDelete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcDeleteKingdom(long aId, byte[] aData) {
/*  60 */     super(aId, (short)8, aData);
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
/*  71 */     return true;
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
/*     */   byte[] encode() {
/*  84 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  85 */     DataOutputStream dos = null;
/*  86 */     byte[] barr = null;
/*     */     
/*     */     try {
/*  89 */       dos = new DataOutputStream(bos);
/*  90 */       dos.writeByte(this.kingdomId);
/*  91 */       dos.flush();
/*  92 */       dos.close();
/*     */     }
/*  94 */     catch (Exception ex) {
/*     */       
/*  96 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 100 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 101 */       barr = bos.toByteArray();
/* 102 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 103 */       setData(barr);
/*     */     } 
/* 105 */     return barr;
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
/* 116 */     DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 121 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 122 */       this.kingdomId = dis.readByte();
/* 123 */       Servers.removeKingdomInfo(this.kingdomId);
/* 124 */       Kingdom k = Kingdoms.getKingdomOrNull(this.kingdomId);
/* 125 */       if (k != null && k.isCustomKingdom())
/*     */       {
/* 127 */         k.delete();
/* 128 */         Kingdoms.removeKingdom(this.kingdomId);
/* 129 */         HistoryManager.addHistory(k.getName(), "has faded and is no more.");
/*     */       }
/*     */     
/* 132 */     } catch (IOException ex) {
/*     */       
/* 134 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 138 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcDeleteKingdom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */