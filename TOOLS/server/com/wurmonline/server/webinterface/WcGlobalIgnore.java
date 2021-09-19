/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.LoginHandler;
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Communicator;
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
/*     */ public final class WcGlobalIgnore
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  47 */   private static final Logger logger = Logger.getLogger(WcGlobalIgnore.class.getName());
/*     */   private long senderWurmId;
/*     */   private String ignorerName;
/*     */   private long targetWurmId;
/*     */   private String ignoreTarget;
/*     */   private boolean response = false;
/*     */   private boolean cant = false;
/*     */   private boolean triggerMute = false;
/*     */   private boolean startIgnore = true;
/*     */   private boolean startUnIgnore = false;
/*  57 */   private byte ignorerKingdom = 0;
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
/*     */   public WcGlobalIgnore(long _id, byte[] _data) {
/*  86 */     super(_id, (short)15, _data);
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
/*  97 */     return false;
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
/* 108 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 109 */     DataOutputStream dos = null;
/* 110 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 113 */       dos = new DataOutputStream(bos);
/* 114 */       dos.writeBoolean(this.response);
/* 115 */       dos.writeBoolean(this.cant);
/* 116 */       dos.writeLong(this.senderWurmId);
/* 117 */       dos.writeUTF(this.ignorerName);
/* 118 */       dos.writeLong(this.targetWurmId);
/* 119 */       dos.writeUTF(this.ignoreTarget);
/* 120 */       dos.writeBoolean(this.triggerMute);
/* 121 */       dos.writeBoolean(this.startIgnore);
/* 122 */       dos.writeBoolean(this.startUnIgnore);
/* 123 */       dos.writeByte(this.ignorerKingdom);
/* 124 */       dos.flush();
/* 125 */       dos.close();
/*     */     }
/* 127 */     catch (Exception ex) {
/*     */       
/* 129 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 133 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 134 */       barr = bos.toByteArray();
/* 135 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 136 */       setData(barr);
/*     */     } 
/* 138 */     return barr;
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
/* 149 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 154 */           if (Servers.isThisATestServer())
/* 155 */             WcGlobalIgnore.logger.log(Level.INFO, "Starting a global ignore."); 
/* 156 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 159 */             dis = new DataInputStream(new ByteArrayInputStream(WcGlobalIgnore.this.getData()));
/* 160 */             WcGlobalIgnore.this.response = dis.readBoolean();
/* 161 */             WcGlobalIgnore.this.cant = dis.readBoolean();
/* 162 */             WcGlobalIgnore.this.senderWurmId = dis.readLong();
/* 163 */             WcGlobalIgnore.this.ignorerName = dis.readUTF();
/* 164 */             WcGlobalIgnore.this.targetWurmId = dis.readLong();
/* 165 */             WcGlobalIgnore.this.ignoreTarget = dis.readUTF();
/* 166 */             WcGlobalIgnore.this.triggerMute = dis.readBoolean();
/* 167 */             WcGlobalIgnore.this.startIgnore = dis.readBoolean();
/* 168 */             WcGlobalIgnore.this.startUnIgnore = dis.readBoolean();
/* 169 */             WcGlobalIgnore.this.ignorerKingdom = dis.readByte();
/*     */           }
/* 171 */           catch (IOException ex) {
/*     */             
/* 173 */             WcGlobalIgnore.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 178 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/* 180 */           if (Servers.isThisATestServer())
/* 181 */             WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this.senderWurmId + "(" + WcGlobalIgnore.this.ignorerName + ") attempting ignore for " + WcGlobalIgnore.this.targetWurmId + " (" + WcGlobalIgnore.this.ignoreTarget + "), response=" + WcGlobalIgnore.this
/* 182 */                 .response + ", Ignore=" + WcGlobalIgnore.this.startIgnore + ", UnIgnore=" + WcGlobalIgnore.this.startUnIgnore); 
/* 183 */           if (!WcGlobalIgnore.this.response) {
/*     */             
/* 185 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(WcGlobalIgnore.this.targetWurmId);
/*     */             
/*     */             try {
/* 188 */               if (pinf == null)
/* 189 */                 pinf = PlayerInfoFactory.createPlayerInfo(WcGlobalIgnore.this.ignoreTarget); 
/* 190 */               pinf.load();
/*     */             }
/* 192 */             catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */             
/* 196 */             if (pinf.loaded && (!Servers.isThisLoginServer() || pinf.getCurrentServer() == Servers.getLocalServerId()))
/*     */             {
/* 198 */               if (WcGlobalIgnore.this.startUnIgnore) {
/*     */                 
/* 200 */                 WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this
/* 201 */                     .senderWurmId + " (" + WcGlobalIgnore.this.ignorerName + ") unignoring " + WcGlobalIgnore.this.targetWurmId + " (" + WcGlobalIgnore.this.ignoreTarget + ")");
/*     */                 
/* 203 */                 WcGlobalIgnore wcGlobalIgnore = new WcGlobalIgnore(WurmId.getNextWCCommandId(), WcGlobalIgnore.this.senderWurmId, WcGlobalIgnore.this.ignorerName, pinf.wurmId, WcGlobalIgnore.this.ignoreTarget, true, false, WcGlobalIgnore.this.triggerMute, WcGlobalIgnore.this.startIgnore, WcGlobalIgnore.this.startUnIgnore, WcGlobalIgnore.this.ignorerKingdom);
/* 204 */                 wcGlobalIgnore.sendToServer(WurmId.getOrigin(WcGlobalIgnore.this.getWurmId()));
/*     */                 return;
/*     */               } 
/* 207 */               if (pinf.getPower() > 1 || pinf.mayMute) {
/*     */                 
/* 209 */                 WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this
/* 210 */                     .senderWurmId + " (" + WcGlobalIgnore.this.ignorerName + ") Cannot ignore " + WcGlobalIgnore.this.targetWurmId + " (" + WcGlobalIgnore.this.ignoreTarget + ") as they can mute.");
/*     */ 
/*     */                 
/* 213 */                 WcGlobalIgnore wcGlobalIgnore = new WcGlobalIgnore(WurmId.getNextWCCommandId(), WcGlobalIgnore.this.senderWurmId, WcGlobalIgnore.this.ignorerName, pinf.wurmId, WcGlobalIgnore.this.ignoreTarget, true, true, WcGlobalIgnore.this.triggerMute, WcGlobalIgnore.this.startIgnore, WcGlobalIgnore.this.startUnIgnore, WcGlobalIgnore.this.ignorerKingdom);
/* 214 */                 wcGlobalIgnore.sendToServer(WurmId.getOrigin(WcGlobalIgnore.this.getWurmId()));
/*     */                 return;
/*     */               } 
/* 217 */               if (WcGlobalIgnore.this.triggerMute) {
/*     */                 
/* 219 */                 WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this.senderWurmId + " (" + WcGlobalIgnore.this.ignorerName + ") triggered muting for " + WcGlobalIgnore.this.ignoreTarget + "=" + WcGlobalIgnore.this.triggerMute);
/* 220 */                 Communicator.attemptMuting(WcGlobalIgnore.this.ignorerKingdom, pinf);
/*     */               } 
/*     */ 
/*     */               
/* 224 */               WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this
/* 225 */                   .senderWurmId + " (" + WcGlobalIgnore.this.ignorerName + ") sending response back to server " + WurmId.getOrigin(WcGlobalIgnore.this.getWurmId()));
/*     */               
/* 227 */               WcGlobalIgnore wgi = new WcGlobalIgnore(WurmId.getNextWCCommandId(), WcGlobalIgnore.this.senderWurmId, WcGlobalIgnore.this.ignorerName, pinf.wurmId, WcGlobalIgnore.this.ignoreTarget, true, false, WcGlobalIgnore.this.triggerMute, WcGlobalIgnore.this.startIgnore, WcGlobalIgnore.this.startUnIgnore, WcGlobalIgnore.this.ignorerKingdom);
/* 228 */               wgi.sendToServer(WurmId.getOrigin(WcGlobalIgnore.this.getWurmId()));
/*     */             }
/* 230 */             else if (Servers.isThisLoginServer() && pinf.loaded)
/*     */             {
/* 232 */               WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this
/* 233 */                   .senderWurmId + " (" + WcGlobalIgnore.this.ignorerName + ") redirecting of  " + WcGlobalIgnore.this.targetWurmId + " (" + WcGlobalIgnore.this.ignoreTarget + ") to " + pinf
/* 234 */                   .getCurrentServer());
/*     */ 
/*     */               
/* 237 */               WcGlobalIgnore wgi = new WcGlobalIgnore(WcGlobalIgnore.this.getWurmId(), WcGlobalIgnore.this.senderWurmId, WcGlobalIgnore.this.ignorerName, WcGlobalIgnore.this.targetWurmId, WcGlobalIgnore.this.ignoreTarget, false, false, WcGlobalIgnore.this.triggerMute, WcGlobalIgnore.this.startIgnore, WcGlobalIgnore.this.startUnIgnore, WcGlobalIgnore.this.ignorerKingdom);
/* 238 */               wgi.sendToServer(pinf.getCurrentServer());
/*     */             }
/* 240 */             else if (!pinf.loaded)
/*     */             {
/* 242 */               WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this
/* 243 */                   .senderWurmId + " (" + WcGlobalIgnore.this.ignorerName + ") sending response back to server as cannot find player " + WurmId.getOrigin(WcGlobalIgnore.this.getWurmId()));
/*     */ 
/*     */ 
/*     */               
/* 247 */               WcGlobalIgnore wgi = new WcGlobalIgnore(WurmId.getNextWCCommandId(), WcGlobalIgnore.this.senderWurmId, WcGlobalIgnore.this.ignorerName, WcGlobalIgnore.this.targetWurmId, WcGlobalIgnore.this.ignoreTarget, true, false, WcGlobalIgnore.this.triggerMute, WcGlobalIgnore.this.startUnIgnore, WcGlobalIgnore.this.startUnIgnore, WcGlobalIgnore.this.ignorerKingdom);
/* 248 */               wgi.sendToServer(WurmId.getOrigin(WcGlobalIgnore.this.getWurmId()));
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 253 */             WcGlobalIgnore.logger.log(Level.INFO, WcGlobalIgnore.this.senderWurmId + " (" + WcGlobalIgnore.this.ignorerName + ") receiving ignore response for " + WcGlobalIgnore.this.targetWurmId);
/*     */             
/* 255 */             PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(WcGlobalIgnore.this.senderWurmId);
/* 256 */             String toSend = "";
/* 257 */             if (WcGlobalIgnore.this.cant) {
/*     */ 
/*     */               
/* 260 */               if (WcGlobalIgnore.this.startIgnore) {
/* 261 */                 toSend = "You may not ignore " + WcGlobalIgnore.this.ignoreTarget + '.';
/*     */               } else {
/* 263 */                 toSend = "You may not snipe " + WcGlobalIgnore.this.ignoreTarget + '.';
/*     */               } 
/* 265 */             } else if (WcGlobalIgnore.this.startIgnore) {
/*     */               
/*     */               try
/*     */               {
/* 269 */                 pinf.addIgnored(WcGlobalIgnore.this.targetWurmId, false);
/* 270 */                 toSend = "You now ignore " + WcGlobalIgnore.this.ignoreTarget + '.';
/*     */               }
/* 272 */               catch (IOException e)
/*     */               {
/* 274 */                 WcGlobalIgnore.logger.log(Level.WARNING, "Failed to add ignored for " + WcGlobalIgnore.this.ignoreTarget, e);
/* 275 */                 toSend = "Failed to add ignored for " + WcGlobalIgnore.this.ignoreTarget + '.';
/*     */               }
/*     */             
/* 278 */             } else if (WcGlobalIgnore.this.startUnIgnore) {
/*     */               
/* 280 */               pinf.removeIgnored(WcGlobalIgnore.this.targetWurmId);
/* 281 */               toSend = "You no longer ignore " + WcGlobalIgnore.this.ignoreTarget + '.';
/*     */             } else {
/*     */               
/* 284 */               toSend = "You have sniped " + WcGlobalIgnore.this.ignoreTarget + '.';
/*     */             } 
/* 286 */             if (Servers.isThisATestServer()) {
/* 287 */               toSend = "(from:" + WurmId.getOrigin(WcGlobalIgnore.this.getWurmId()) + ") " + toSend;
/*     */             }
/* 289 */             Message mess = new Message(null, (byte)17, ":Event", toSend);
/* 290 */             mess.setReceiver(WcGlobalIgnore.this.senderWurmId);
/* 291 */             Server.getInstance().addMessage(mess);
/*     */           } 
/*     */         }
/* 294 */       }).start();
/*     */   }
/*     */   
/*     */   public WcGlobalIgnore(long _id, long senderId, String ignorer, long targetId, String ignored, boolean isResponseCommand, boolean cannot, boolean muting, boolean ignoring, boolean unIgnoring, byte kingdomId) {
/*     */     super(_id, (short)15);
/*     */     this.ignorerName = LoginHandler.raiseFirstLetter(ignorer);
/*     */     this.ignoreTarget = LoginHandler.raiseFirstLetter(ignored);
/*     */     this.senderWurmId = senderId;
/*     */     this.targetWurmId = targetId;
/*     */     this.response = isResponseCommand;
/*     */     this.cant = cannot;
/*     */     this.triggerMute = muting;
/*     */     this.startIgnore = ignoring;
/*     */     this.startUnIgnore = unIgnoring;
/*     */     this.ignorerKingdom = kingdomId;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcGlobalIgnore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */