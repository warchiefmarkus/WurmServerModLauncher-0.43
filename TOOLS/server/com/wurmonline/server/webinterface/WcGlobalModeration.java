/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.support.Trello;
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
/*     */ public final class WcGlobalModeration
/*     */   extends WebCommand
/*     */   implements MiscConstants, TimeConstants
/*     */ {
/*  49 */   private static final Logger logger = Logger.getLogger(WcGlobalModeration.class.getName());
/*     */   private boolean warning;
/*     */   private boolean ban;
/*     */   private boolean mute;
/*     */   private boolean unmute;
/*     */   private boolean muteWarn;
/*     */   private int hours;
/*     */   private int days;
/*  57 */   private String sender = "";
/*  58 */   private String reason = "";
/*  59 */   private String playerName = "";
/*  60 */   private byte senderPower = 0;
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
/*     */   public WcGlobalModeration(long id, byte[] data) {
/*  82 */     super(id, (short)14, data);
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
/*  93 */     return true;
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
/* 104 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 105 */     DataOutputStream dos = null;
/* 106 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 109 */       dos = new DataOutputStream(bos);
/* 110 */       dos.writeUTF(this.sender);
/* 111 */       dos.writeBoolean(this.ban);
/* 112 */       dos.writeBoolean(this.mute);
/* 113 */       dos.writeBoolean(this.unmute);
/* 114 */       dos.writeBoolean(this.muteWarn);
/* 115 */       dos.writeBoolean(this.warning);
/*     */       
/* 117 */       dos.writeUTF(this.playerName);
/* 118 */       dos.writeUTF(this.reason);
/* 119 */       dos.writeInt(this.days);
/* 120 */       dos.writeInt(this.hours);
/* 121 */       dos.writeByte(this.senderPower);
/* 122 */       dos.flush();
/* 123 */       dos.close();
/*     */     }
/* 125 */     catch (Exception ex) {
/*     */       
/* 127 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 131 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 132 */       barr = bos.toByteArray();
/* 133 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 134 */       setData(barr);
/*     */     } 
/* 136 */     return barr;
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
/* 147 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 152 */           DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 157 */             dis = new DataInputStream(new ByteArrayInputStream(WcGlobalModeration.this.getData()));
/* 158 */             WcGlobalModeration.this.sender = dis.readUTF();
/* 159 */             WcGlobalModeration.this.ban = dis.readBoolean();
/* 160 */             WcGlobalModeration.this.mute = dis.readBoolean();
/* 161 */             WcGlobalModeration.this.unmute = dis.readBoolean();
/* 162 */             WcGlobalModeration.this.muteWarn = dis.readBoolean();
/* 163 */             WcGlobalModeration.this.warning = dis.readBoolean();
/* 164 */             WcGlobalModeration.this.playerName = dis.readUTF();
/* 165 */             WcGlobalModeration.this.reason = dis.readUTF();
/* 166 */             WcGlobalModeration.this.days = dis.readInt();
/* 167 */             WcGlobalModeration.this.hours = dis.readInt();
/* 168 */             WcGlobalModeration.this.senderPower = dis.readByte();
/*     */             
/*     */             try {
/* 171 */               Player p = Players.getInstance().getPlayer(WcGlobalModeration.this.playerName);
/* 172 */               if (WcGlobalModeration.this.ban)
/*     */               {
/* 174 */                 if (p.getPower() < WcGlobalModeration.this.senderPower)
/*     */                   
/*     */                   try {
/*     */ 
/*     */ 
/*     */                     
/* 180 */                     Message mess = new Message(null, (byte)3, ":Event", "You have been banned for " + WcGlobalModeration.this.days + " days and thrown out from the game.");
/*     */                     
/* 182 */                     mess.setReceiver(p.getWurmId());
/* 183 */                     Server.getInstance().addMessage(mess);
/* 184 */                     p.ban(WcGlobalModeration.this.reason, System.currentTimeMillis() + WcGlobalModeration.this.days * 86400000L + WcGlobalModeration.this.hours * 3600000L);
/*     */                   }
/* 186 */                   catch (Exception ex) {
/*     */                     
/* 188 */                     WcGlobalModeration.logger.log(Level.WARNING, ex.getMessage());
/*     */                   }  
/*     */               }
/* 191 */               if (WcGlobalModeration.this.mute)
/*     */               {
/*     */                 
/* 194 */                 if (p.getPower() <= WcGlobalModeration.this.senderPower) {
/*     */                   
/* 196 */                   p.mute(true, WcGlobalModeration.this.reason, System.currentTimeMillis() + WcGlobalModeration.this.days * 86400000L + WcGlobalModeration.this.hours * 3600000L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 203 */                   Message mess = new Message(null, (byte)3, ":Event", "You have been muted by " + WcGlobalModeration.this.sender + " for " + WcGlobalModeration.this.hours + " hours and cannot shout anymore. Reason: " + WcGlobalModeration.this.reason);
/* 204 */                   mess.setReceiver(p.getWurmId());
/* 205 */                   Server.getInstance().addMessage(mess);
/*     */                 } 
/*     */               }
/* 208 */               if (WcGlobalModeration.this.unmute) {
/*     */                 
/* 210 */                 p.mute(false, "", 0L);
/*     */                 
/* 212 */                 Message mess = new Message(null, (byte)3, ":Event", "You have been given your voice back and can shout again.");
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 217 */                 mess.setReceiver(p.getWurmId());
/* 218 */                 Server.getInstance().addMessage(mess);
/*     */               } 
/* 220 */               if (WcGlobalModeration.this.muteWarn)
/*     */               {
/*     */                 
/* 223 */                 if (p.getPower() <= WcGlobalModeration.this.senderPower) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 229 */                   Message mess = new Message(null, (byte)3, ":Event", WcGlobalModeration.this.sender + " issues a warning that you may be muted. Be silent for a while and try to understand why or change the subject of your conversation please.");
/*     */                   
/* 231 */                   mess.setReceiver(p.getWurmId());
/* 232 */                   Server.getInstance().addMessage(mess);
/*     */                   
/* 234 */                   if (WcGlobalModeration.this.reason.length() > 0) {
/*     */ 
/*     */                     
/* 237 */                     Message mess2 = new Message(null, (byte)3, ":Event", "The reason for this is '" + WcGlobalModeration.this.reason + "'");
/*     */                     
/* 239 */                     mess2.setReceiver(p.getWurmId());
/* 240 */                     Server.getInstance().addMessage(mess2);
/*     */                   } 
/*     */                 } 
/*     */               }
/* 244 */               if (WcGlobalModeration.this.warning)
/*     */               {
/* 246 */                 if (p.getPower() < WcGlobalModeration.this.senderPower)
/*     */                 {
/* 248 */                   p.getSaveFile().warn();
/* 249 */                   Message mess = new Message(null, (byte)3, ":Event", "You have just received an official warning. Too many of these will get you banned from the game.");
/*     */                   
/* 251 */                   mess.setReceiver(p.getWurmId());
/* 252 */                   Server.getInstance().addMessage(mess);
/*     */                 }
/*     */               
/*     */               }
/* 256 */             } catch (NoSuchPlayerException nsp) {
/*     */ 
/*     */               
/* 259 */               if (WcGlobalModeration.this.unmute) {
/*     */                 
/*     */                 try {
/*     */                   
/* 263 */                   PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(WcGlobalModeration.this.playerName);
/* 264 */                   pinf.load();
/* 265 */                   if (pinf.wurmId > 0L)
/*     */                   {
/* 267 */                     pinf.setMuted(false, "", 0L);
/*     */                   }
/*     */                 }
/* 270 */                 catch (IOException ex) {
/*     */                   
/* 272 */                   if (Servers.isThisATestServer()) {
/* 273 */                     WcGlobalModeration.logger.log(Level.WARNING, "Unable to find player:" + WcGlobalModeration.this.playerName + "." + ex.getMessage(), ex);
/*     */                   }
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/* 279 */             if (WcGlobalModeration.this.mute) {
/*     */               
/* 281 */               Players.addMgmtMessage(WcGlobalModeration.this.sender, "mutes " + WcGlobalModeration.this.playerName + " for " + WcGlobalModeration.this.hours + " hours. Reason: " + WcGlobalModeration.this
/* 282 */                   .reason);
/*     */               
/* 284 */               Message mess = new Message(null, (byte)9, "MGMT", "<" + WcGlobalModeration.this.sender + "> mutes " + WcGlobalModeration.this.playerName + " for " + WcGlobalModeration.this.hours + " hours. Reason: " + WcGlobalModeration.this.reason);
/*     */               
/* 286 */               Server.getInstance().addMessage(mess);
/*     */             } 
/* 288 */             if (WcGlobalModeration.this.unmute) {
/*     */               
/* 290 */               Players.addMgmtMessage(WcGlobalModeration.this.sender, "unmutes " + WcGlobalModeration.this.playerName);
/*     */               
/* 292 */               Message mess = new Message(null, (byte)9, "MGMT", "<" + WcGlobalModeration.this.sender + "> unmutes " + WcGlobalModeration.this.playerName);
/*     */               
/* 294 */               Server.getInstance().addMessage(mess);
/*     */             } 
/* 296 */             if (WcGlobalModeration.this.muteWarn) {
/*     */               
/* 298 */               Players.addMgmtMessage(WcGlobalModeration.this.sender, "mutewarns " + WcGlobalModeration.this.playerName + " (" + WcGlobalModeration.this.reason + ")");
/*     */               
/* 300 */               Message mess = new Message(null, (byte)9, "MGMT", "<" + WcGlobalModeration.this.sender + "> mutewarns " + WcGlobalModeration.this.playerName + " (" + WcGlobalModeration.this.reason + ")");
/*     */               
/* 302 */               Server.getInstance().addMessage(mess);
/*     */             } 
/*     */ 
/*     */             
/* 306 */             if (Servers.isThisLoginServer())
/*     */             {
/* 308 */               if (WcGlobalModeration.this.mute || WcGlobalModeration.this.muteWarn || WcGlobalModeration.this.unmute) {
/* 309 */                 Trello.addMessage(WcGlobalModeration.this.sender, WcGlobalModeration.this.playerName, WcGlobalModeration.this.reason, WcGlobalModeration.this.hours);
/*     */               }
/*     */             }
/* 312 */           } catch (IOException ex) {
/*     */             
/* 314 */             WcGlobalModeration.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           }
/*     */           finally {
/*     */             
/* 318 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/* 321 */       }).start();
/*     */   }
/*     */   
/*     */   public WcGlobalModeration(long id, String _sender, byte _senderPower, boolean _mute, boolean _unmute, boolean _mutewarn, boolean _ban, boolean _warning, int _hours, int _days, String _playerName, String _reason) {
/*     */     super(id, (short)14);
/*     */     this.sender = _sender;
/*     */     this.warning = _warning;
/*     */     this.ban = _ban;
/*     */     this.mute = _mute;
/*     */     this.unmute = _unmute;
/*     */     this.muteWarn = _mutewarn;
/*     */     this.hours = _hours;
/*     */     this.days = _days;
/*     */     this.reason = _reason;
/*     */     this.playerName = _playerName;
/*     */     this.senderPower = _senderPower;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcGlobalModeration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */