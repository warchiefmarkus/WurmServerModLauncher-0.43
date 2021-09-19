/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.players.PlayerState;
/*     */ import com.wurmonline.shared.constants.PlayerOnlineStatus;
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
/*     */ public final class WcPlayerStatus
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  44 */   private static final Logger logger = Logger.getLogger(WcPlayerStatus.class.getName());
/*     */ 
/*     */   
/*     */   public static final byte DO_NOTHING = 0;
/*     */ 
/*     */   
/*     */   public static final byte WHOS_ONLINE = 1;
/*     */   
/*     */   public static final byte STATUS_CHANGE = 2;
/*     */   
/*  54 */   private byte type = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String playerName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long playerWurmId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long lastLogin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long lastLogout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int currentServerId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PlayerOnlineStatus status;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcPlayerStatus(long aId, byte[] aData) {
/* 117 */     super(aId, (short)19, aData);
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
/*     */   byte[] encode() {
/* 139 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 140 */     DataOutputStream dos = null;
/* 141 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 144 */       dos = new DataOutputStream(bos);
/* 145 */       dos.writeByte(this.type);
/* 146 */       switch (this.type) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/* 152 */           dos.writeUTF(this.playerName);
/* 153 */           dos.writeLong(this.playerWurmId);
/* 154 */           dos.writeLong(this.lastLogin);
/* 155 */           dos.writeLong(this.lastLogout);
/* 156 */           dos.writeInt(this.currentServerId);
/* 157 */           dos.writeByte(this.status.getId());
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 163 */       dos.flush();
/* 164 */       dos.close();
/*     */     }
/* 166 */     catch (Exception ex) {
/*     */       
/* 168 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 172 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 173 */       barr = bos.toByteArray();
/* 174 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 175 */       setData(barr);
/*     */     } 
/* 177 */     return barr;
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
/* 188 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 193 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 196 */             dis = new DataInputStream(new ByteArrayInputStream(WcPlayerStatus.this.getData()));
/* 197 */             WcPlayerStatus.this.type = dis.readByte();
/* 198 */             switch (WcPlayerStatus.this.type) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               case 2:
/* 204 */                 WcPlayerStatus.this.playerName = dis.readUTF();
/* 205 */                 WcPlayerStatus.this.playerWurmId = dis.readLong();
/* 206 */                 WcPlayerStatus.this.lastLogin = dis.readLong();
/* 207 */                 WcPlayerStatus.this.lastLogout = dis.readLong();
/* 208 */                 WcPlayerStatus.this.currentServerId = dis.readInt();
/* 209 */                 WcPlayerStatus.this.status = PlayerOnlineStatus.playerOnlineStatusFromId(dis.readByte());
/*     */                 break;
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 216 */           } catch (IOException ex) {
/*     */             
/* 218 */             WcPlayerStatus.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 223 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */           
/* 226 */           if (WcPlayerStatus.this.type == 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 232 */             if (!Servers.isThisLoginServer()) {
/* 233 */               PlayerInfoFactory.whosOnline();
/*     */             }
/* 235 */           } else if (WcPlayerStatus.this.type == 2) {
/*     */ 
/*     */             
/* 238 */             PlayerState pState = new PlayerState(WcPlayerStatus.this.currentServerId, WcPlayerStatus.this.playerWurmId, WcPlayerStatus.this.playerName, WcPlayerStatus.this.lastLogin, WcPlayerStatus.this.lastLogout, WcPlayerStatus.this.status);
/* 239 */             PlayerInfoFactory.updatePlayerState(pState);
/* 240 */             if (Servers.isThisLoginServer())
/*     */             {
/*     */               
/* 243 */               WcPlayerStatus.this.sendFromLoginServer();
/*     */             }
/*     */           } 
/*     */         }
/* 247 */       }).start();
/*     */   }
/*     */   
/*     */   public WcPlayerStatus() {
/*     */     super(WurmId.getNextWCCommandId(), (short)19);
/*     */     this.type = 1;
/*     */   }
/*     */   
/*     */   public WcPlayerStatus(PlayerState pState) {
/*     */     super(WurmId.getNextWCCommandId(), (short)19);
/*     */     this.type = 2;
/*     */     this.playerName = pState.getPlayerName();
/*     */     this.playerWurmId = pState.getPlayerId();
/*     */     this.lastLogin = pState.getLastLogin();
/*     */     this.lastLogout = pState.getLastLogout();
/*     */     this.currentServerId = pState.getServerId();
/*     */     this.status = pState.getState();
/*     */   }
/*     */   
/*     */   public WcPlayerStatus(String aPlayerName, long aPlayerWurmId, long aLastLogin, long aLastLogout, int aCurrentServerId, PlayerOnlineStatus aStatus) {
/*     */     super(WurmId.getNextWCCommandId(), (short)19);
/*     */     this.type = 2;
/*     */     this.playerName = aPlayerName;
/*     */     this.playerWurmId = aPlayerWurmId;
/*     */     this.lastLogin = aLastLogin;
/*     */     this.lastLogout = aLastLogout;
/*     */     this.currentServerId = aCurrentServerId;
/*     */     this.status = aStatus;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcPlayerStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */