/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
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
/*     */ 
/*     */ 
/*     */ public final class WcExpelMember
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(WcRemoveFriendship.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long playerId;
/*     */ 
/*     */ 
/*     */   
/*     */   private byte fromKingdomId;
/*     */ 
/*     */ 
/*     */   
/*     */   private byte toKingdomId;
/*     */ 
/*     */ 
/*     */   
/*     */   private int originServer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcExpelMember(long aPlayerId, byte aFromKingdomId, byte aToKingdomId, int aOriginServer) {
/*  67 */     super(WurmId.getNextWCCommandId(), (short)30);
/*  68 */     this.playerId = aPlayerId;
/*  69 */     this.fromKingdomId = aFromKingdomId;
/*  70 */     this.toKingdomId = aToKingdomId;
/*  71 */     this.originServer = aOriginServer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcExpelMember(long aId, byte[] aData) {
/*  80 */     super(aId, (short)30, aData);
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
/*  91 */     return true;
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
/* 102 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 103 */     DataOutputStream dos = null;
/* 104 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 107 */       dos = new DataOutputStream(bos);
/* 108 */       dos.writeLong(this.playerId);
/* 109 */       dos.writeByte(this.fromKingdomId);
/* 110 */       dos.writeByte(this.toKingdomId);
/* 111 */       dos.writeInt(this.originServer);
/* 112 */       dos.flush();
/* 113 */       dos.close();
/*     */     }
/* 115 */     catch (Exception ex) {
/*     */       
/* 117 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 121 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 122 */       barr = bos.toByteArray();
/* 123 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 124 */       setData(barr);
/*     */     } 
/* 126 */     return barr;
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
/* 137 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 142 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 145 */             dis = new DataInputStream(new ByteArrayInputStream(WcExpelMember.this.getData()));
/* 146 */             WcExpelMember.this.playerId = dis.readLong();
/* 147 */             WcExpelMember.this.fromKingdomId = dis.readByte();
/* 148 */             WcExpelMember.this.toKingdomId = dis.readByte();
/* 149 */             WcExpelMember.this.originServer = dis.readInt();
/*     */           }
/* 151 */           catch (IOException ex) {
/*     */             
/* 153 */             WcExpelMember.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 158 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */           
/* 161 */           if (Servers.isThisLoginServer())
/* 162 */             WcExpelMember.this.sendFromLoginServer(); 
/* 163 */           PlayerInfoFactory.expelMember(WcExpelMember.this.playerId, WcExpelMember.this.fromKingdomId, WcExpelMember.this.toKingdomId, WcExpelMember.this.originServer);
/*     */         }
/* 165 */       }).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcExpelMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */