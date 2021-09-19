/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
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
/*     */ public final class WcRefreshCommand
/*     */   extends WebCommand
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(WcRefreshCommand.class.getName());
/*     */ 
/*     */   
/*     */   private String nameToReload;
/*     */ 
/*     */ 
/*     */   
/*     */   public WcRefreshCommand(long aId, String _nameToReload) {
/*  47 */     super(aId, (short)5);
/*  48 */     this.nameToReload = _nameToReload;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcRefreshCommand(long aId, byte[] _data) {
/*  56 */     super(aId, (short)5, _data);
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
/*  84 */       dos.writeUTF(this.nameToReload);
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
/*     */     try {
/* 113 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 114 */       this.nameToReload = dis.readUTF();
/* 115 */       PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(this.nameToReload);
/* 116 */       pinf.loaded = false;
/* 117 */       pinf.load();
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


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcRefreshCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */