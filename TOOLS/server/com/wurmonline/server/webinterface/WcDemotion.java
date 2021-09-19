/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
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
/*     */ public class WcDemotion
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(WcDemotion.class.getName());
/*     */ 
/*     */   
/*     */   private long senderWurmId;
/*     */ 
/*     */   
/*     */   private long targetWurmId;
/*     */   
/*     */   private String responseMsg;
/*     */   
/*     */   private short demoteType;
/*     */   
/*     */   public static final short CA = 1;
/*     */   
/*     */   public static final short CM = 2;
/*     */   
/*     */   public static final short GM = 3;
/*     */ 
/*     */   
/*     */   public WcDemotion(long _id, long senderId, long targetId, short demotionType) {
/*  64 */     super(_id, (short)3);
/*  65 */     this.senderWurmId = senderId;
/*  66 */     this.targetWurmId = targetId;
/*  67 */     this.demoteType = demotionType;
/*  68 */     this.responseMsg = "";
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
/*     */   public WcDemotion(long _id, long senderId, long targetId, short demotionType, String response) {
/*  81 */     super(_id, (short)3);
/*  82 */     this.senderWurmId = senderId;
/*  83 */     this.targetWurmId = targetId;
/*  84 */     this.demoteType = demotionType;
/*  85 */     this.responseMsg = response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcDemotion(long _id, byte[] _data) {
/*  95 */     super(_id, (short)3, _data);
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
/* 106 */     return true;
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
/* 117 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 118 */     DataOutputStream dos = null;
/* 119 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 122 */       dos = new DataOutputStream(bos);
/* 123 */       dos.writeShort(this.demoteType);
/* 124 */       dos.writeLong(this.senderWurmId);
/* 125 */       dos.writeLong(this.targetWurmId);
/* 126 */       dos.writeUTF(this.responseMsg);
/* 127 */       dos.flush();
/* 128 */       dos.close();
/*     */     }
/* 130 */     catch (Exception ex) {
/*     */       
/* 132 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 136 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 137 */       barr = bos.toByteArray();
/* 138 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 139 */       setData(barr);
/*     */     } 
/* 141 */     return barr;
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
/* 152 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 157 */           WcDemotion.logger.log(Level.INFO, "Demoting Player.");
/* 158 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 161 */             dis = new DataInputStream(new ByteArrayInputStream(WcDemotion.this.getData()));
/* 162 */             WcDemotion.this.demoteType = dis.readShort();
/* 163 */             WcDemotion.this.senderWurmId = dis.readLong();
/* 164 */             WcDemotion.this.targetWurmId = dis.readLong();
/* 165 */             WcDemotion.this.responseMsg = dis.readUTF();
/*     */             
/* 167 */             WcDemotion.logger.log(Level.INFO, WcDemotion.this.senderWurmId + " attempting demotion of " + WcDemotion.this.targetWurmId + ", response=" + WcDemotion.this
/* 168 */                 .responseMsg);
/* 169 */             if (WcDemotion.this.responseMsg.length() == 0) {
/*     */               
/* 171 */               PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(WcDemotion.this.targetWurmId);
/* 172 */               if (pinf != null && pinf.loaded)
/*     */               {
/* 174 */                 WcDemotion.logger.log(Level.INFO, WcDemotion.this.senderWurmId + " triggered demotion for " + ((WcDemotion.this.demoteType == 1) ? "CA" : "CM") + " id " + WcDemotion.this
/* 175 */                     .targetWurmId);
/*     */                 
/* 177 */                 String msg = "[" + Servers.getLocalServerName() + "] " + pinf.getName();
/* 178 */                 if (WcDemotion.this.demoteType == 1) {
/*     */                   
/* 180 */                   pinf.setIsPlayerAssistant(false);
/* 181 */                   msg = msg + " no longer have the duties of being a community assistant.";
/*     */                 }
/* 183 */                 else if (WcDemotion.this.demoteType == 2) {
/*     */                   
/* 185 */                   pinf.setMayMute(false);
/* 186 */                   msg = msg + " may no longer mute other players.";
/*     */                 }
/* 188 */                 else if (WcDemotion.this.demoteType == 3) {
/*     */                   
/* 190 */                   pinf.setDevTalk(false);
/* 191 */                   msg = msg + " may no longer see GM tab.";
/*     */                 } else {
/*     */                   
/* 194 */                   msg = msg + " unknown demotion.";
/*     */                 } 
/*     */                 
/* 197 */                 WcDemotion wgi = new WcDemotion(WurmId.getNextWCCommandId(), WcDemotion.this.senderWurmId, pinf.wurmId, WcDemotion.this.demoteType, msg);
/* 198 */                 wgi.sendToServer(WurmId.getOrigin(WcDemotion.this.getWurmId()));
/*     */                 
/* 200 */                 WcDemotion.logger.log(Level.INFO, WcDemotion.this
/* 201 */                     .senderWurmId + " sending response back to server " + WurmId.getOrigin(WcDemotion.this.getWurmId()));
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 206 */               WcDemotion.logger.log(Level.INFO, WcDemotion.this.senderWurmId + " receiving demotion response for " + WcDemotion.this.targetWurmId);
/*     */               
/* 208 */               Message mess = new Message(null, (byte)3, ":Event", WcDemotion.this.responseMsg);
/* 209 */               mess.setReceiver(WcDemotion.this.senderWurmId);
/* 210 */               Server.getInstance().addMessage(mess);
/*     */             }
/*     */           
/* 213 */           } catch (IOException ex) {
/*     */             
/* 215 */             WcDemotion.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           }
/*     */           finally {
/*     */             
/* 219 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/* 222 */       }).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcDemotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */