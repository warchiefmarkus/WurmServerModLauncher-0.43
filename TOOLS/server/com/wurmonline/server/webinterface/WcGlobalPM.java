/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.Player;
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
/*     */ public class WcGlobalPM
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  46 */   private static final Logger logger = Logger.getLogger(WcGlobalPM.class.getName());
/*     */ 
/*     */   
/*     */   public static final byte GETID = 0;
/*     */   
/*     */   public static final byte THE_ID = 2;
/*     */   
/*     */   public static final byte SEND = 3;
/*     */   
/*     */   public static final byte IGNORED = 5;
/*     */   
/*     */   public static final byte NA = 6;
/*     */   
/*     */   public static final byte AFK = 7;
/*     */   
/*  61 */   private byte action = 3;
/*  62 */   private byte power = 0;
/*  63 */   private long senderId = -10L;
/*  64 */   private String senderName = "unknown";
/*  65 */   private byte kingdom = 0;
/*  66 */   private int targetServerId = 0;
/*  67 */   private long targetId = -10L;
/*  68 */   private String targetName = "unknown";
/*     */   private boolean friend = false;
/*  70 */   private String message = "";
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
/*     */   private boolean emote = false;
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
/*     */   private boolean override = false;
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
/*     */   public WcGlobalPM(long _id, byte[] _data) {
/* 117 */     super(_id, (short)17, _data);
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
/* 128 */     return false;
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
/*     */   public byte[] encode() {
/* 141 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 142 */     DataOutputStream dos = null;
/* 143 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 146 */       dos = new DataOutputStream(bos);
/* 147 */       dos.writeByte(this.action);
/* 148 */       dos.writeByte(this.power);
/* 149 */       dos.writeLong(this.senderId);
/* 150 */       dos.writeUTF(this.senderName);
/* 151 */       dos.writeByte(this.kingdom);
/* 152 */       dos.writeInt(this.targetServerId);
/* 153 */       dos.writeLong(this.targetId);
/* 154 */       dos.writeUTF(this.targetName);
/* 155 */       dos.writeBoolean(this.friend);
/* 156 */       dos.writeUTF(this.message);
/* 157 */       dos.writeBoolean(this.emote);
/* 158 */       dos.writeBoolean(this.override);
/* 159 */       dos.flush();
/* 160 */       dos.close();
/*     */     }
/* 162 */     catch (Exception ex) {
/*     */       
/* 164 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 168 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 169 */       barr = bos.toByteArray();
/* 170 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 171 */       setData(barr);
/*     */     } 
/* 173 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 179 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 184 */           DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 189 */             dis = new DataInputStream(new ByteArrayInputStream(WcGlobalPM.this.getData()));
/* 190 */             WcGlobalPM.this.action = dis.readByte();
/* 191 */             WcGlobalPM.this.power = dis.readByte();
/* 192 */             WcGlobalPM.this.senderId = dis.readLong();
/* 193 */             WcGlobalPM.this.senderName = dis.readUTF();
/* 194 */             WcGlobalPM.this.kingdom = dis.readByte();
/* 195 */             WcGlobalPM.this.targetServerId = dis.readInt();
/* 196 */             WcGlobalPM.this.targetId = dis.readLong();
/* 197 */             WcGlobalPM.this.targetName = dis.readUTF();
/* 198 */             WcGlobalPM.this.friend = dis.readBoolean();
/* 199 */             WcGlobalPM.this.message = dis.readUTF();
/* 200 */             WcGlobalPM.this.emote = dis.readBoolean();
/* 201 */             WcGlobalPM.this.override = dis.readBoolean();
/*     */           }
/* 203 */           catch (IOException ex) {
/*     */             
/* 205 */             WcGlobalPM.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 210 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */           
/* 213 */           if (WcGlobalPM.this.action == 0) {
/*     */ 
/*     */             
/* 216 */             PlayerInfo pInfo = PlayerInfoFactory.createPlayerInfo(WcGlobalPM.this.targetName);
/* 217 */             if (pInfo != null) {
/*     */               
/*     */               try {
/*     */                 
/* 221 */                 pInfo.load();
/* 222 */                 WcGlobalPM.this.targetId = pInfo.wurmId;
/* 223 */                 WcGlobalPM.this.targetServerId = pInfo.currentServer;
/*     */               }
/* 225 */               catch (IOException ex) {
/*     */ 
/*     */                 
/* 228 */                 WcGlobalPM.this.targetId = -10L;
/*     */               } 
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 234 */             WcGlobalPM wgi = new WcGlobalPM(WurmId.getNextWCCommandId(), (byte)2, WcGlobalPM.this.power, WcGlobalPM.this.senderId, WcGlobalPM.this.senderName, WcGlobalPM.this.kingdom, WcGlobalPM.this.targetServerId, WcGlobalPM.this.targetId, WcGlobalPM.this.targetName, WcGlobalPM.this.friend, WcGlobalPM.this.message, WcGlobalPM.this.emote, WcGlobalPM.this.override);
/* 235 */             wgi.sendToServer(WurmId.getOrigin(WcGlobalPM.this.getWurmId()));
/*     */           }
/* 237 */           else if (WcGlobalPM.this.action == 3) {
/*     */             
/* 239 */             PlayerInfo pInfo = PlayerInfoFactory.createPlayerInfo(WcGlobalPM.this.targetName);
/* 240 */             if (pInfo == null) {
/*     */               
/* 242 */               WcGlobalPM.logger.log(Level.WARNING, "no player '" + WcGlobalPM.this.targetName + "' Info?");
/*     */               return;
/*     */             } 
/* 245 */             WcGlobalPM.this.targetServerId = pInfo.currentServer;
/* 246 */             if (pInfo.currentServer == Servers.getLocalServerId()) {
/*     */ 
/*     */               
/*     */               try {
/*     */                 
/* 251 */                 Player p = Players.getInstance().getPlayer(WcGlobalPM.this.targetName);
/* 252 */                 if (!p.sendPM(WcGlobalPM.this.power, WcGlobalPM.this.senderName, WcGlobalPM.this.senderId, WcGlobalPM.this.friend, WcGlobalPM.this
/* 253 */                     .message, WcGlobalPM.this.emote, WcGlobalPM.this.kingdom, WurmId.getOrigin(WcGlobalPM.this.getWurmId()), WcGlobalPM.this.override))
/*     */                 {
/*     */ 
/*     */ 
/*     */                   
/* 258 */                   WcGlobalPM wgi = new WcGlobalPM(WurmId.getNextWCCommandId(), (byte)6, WcGlobalPM.this.power, WcGlobalPM.this.senderId, WcGlobalPM.this.senderName, WcGlobalPM.this.kingdom, WcGlobalPM.this.targetServerId, WcGlobalPM.this.targetId, WcGlobalPM.this.targetName, WcGlobalPM.this.friend, WcGlobalPM.this.message, WcGlobalPM.this.emote, WcGlobalPM.this.override);
/* 259 */                   wgi.sendToServer(WurmId.getOrigin(WcGlobalPM.this.getWurmId()));
/*     */                 }
/* 261 */                 else if (p.isAFK())
/*     */                 {
/*     */ 
/*     */ 
/*     */                   
/* 266 */                   WcGlobalPM wgi = new WcGlobalPM(WurmId.getNextWCCommandId(), (byte)7, WcGlobalPM.this.power, WcGlobalPM.this.senderId, WcGlobalPM.this.senderName, WcGlobalPM.this.kingdom, WcGlobalPM.this.targetServerId, WcGlobalPM.this.targetId, WcGlobalPM.this.targetName, WcGlobalPM.this.friend, p.getAFKMessage(), true, WcGlobalPM.this.override);
/* 267 */                   wgi.sendToServer(WurmId.getOrigin(WcGlobalPM.this.getWurmId()));
/*     */                 }
/*     */               
/* 270 */               } catch (NoSuchPlayerException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 277 */                 WcGlobalPM wgi = new WcGlobalPM(WurmId.getNextWCCommandId(), (byte)6, WcGlobalPM.this.power, WcGlobalPM.this.senderId, WcGlobalPM.this.senderName, WcGlobalPM.this.kingdom, WcGlobalPM.this.targetServerId, WcGlobalPM.this.targetId, WcGlobalPM.this.targetName, WcGlobalPM.this.friend, WcGlobalPM.this.message, WcGlobalPM.this.emote, WcGlobalPM.this.override);
/* 278 */                 wgi.sendToServer(WurmId.getOrigin(WcGlobalPM.this.getWurmId()));
/*     */               }
/*     */             
/* 281 */             } else if (Servers.isThisLoginServer()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 287 */               WcGlobalPM wgi = new WcGlobalPM(WcGlobalPM.this.getWurmId(), WcGlobalPM.this.action, WcGlobalPM.this.power, WcGlobalPM.this.senderId, WcGlobalPM.this.senderName, WcGlobalPM.this.kingdom, WcGlobalPM.this.targetServerId, WcGlobalPM.this.targetId, WcGlobalPM.this.targetName, WcGlobalPM.this.friend, WcGlobalPM.this.message, WcGlobalPM.this.emote, WcGlobalPM.this.override);
/* 288 */               wgi.sendToServer(pInfo.currentServer);
/*     */             
/*     */             }
/*     */             else {
/*     */ 
/*     */               
/* 294 */               WcGlobalPM.logger.log(Level.WARNING, "not on login or " + WcGlobalPM.this.targetName + "'s server!");
/*     */             } 
/*     */           } else {
/*     */ 
/*     */             
/*     */             try {
/*     */ 
/*     */               
/* 302 */               Player p = Players.getInstance().getPlayer(WcGlobalPM.this.senderName);
/* 303 */               p.sendPM(WcGlobalPM.this.action, WcGlobalPM.this.targetName, WcGlobalPM.this.targetId, WcGlobalPM.this.message, WcGlobalPM.this.emote, WcGlobalPM.this.override);
/*     */             }
/* 305 */             catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 312 */       }).start();
/*     */   }
/*     */   
/*     */   public WcGlobalPM(long aId, byte _action, byte _power, long _senderId, String _senderName, byte _kingdom, int _targetServerId, long _targetId, String _targetName, boolean _friend, String _message, boolean _emote, boolean aOverride) {
/*     */     super(aId, (short)17);
/*     */     this.action = _action;
/*     */     this.power = _power;
/*     */     this.senderId = _senderId;
/*     */     this.senderName = _senderName;
/*     */     this.kingdom = _kingdom;
/*     */     this.targetServerId = _targetServerId;
/*     */     this.targetId = _targetId;
/*     */     this.targetName = _targetName;
/*     */     this.friend = _friend;
/*     */     this.message = _message;
/*     */     this.emote = _emote;
/*     */     this.override = aOverride;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcGlobalPM.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */