/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
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
/*     */ public class PlayerState
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(PlayerState.class.getName());
/*     */   
/*     */   private int serverId;
/*     */   
/*     */   private long playerId;
/*     */   
/*     */   private String playerName;
/*     */   
/*     */   private long lastLogin;
/*     */   
/*     */   private long lastLogout;
/*     */   
/*     */   private PlayerOnlineStatus state;
/*     */ 
/*     */   
/*     */   public PlayerState(long aWurmId) {
/*  57 */     PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(aWurmId);
/*  58 */     this.playerId = aWurmId;
/*  59 */     if (pInfo != null) {
/*     */       
/*     */       try {
/*     */         
/*  63 */         pInfo.load();
/*     */       }
/*  65 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (pInfo != null && pInfo.loaded) {
/*     */       
/*  72 */       this.serverId = pInfo.currentServer;
/*  73 */       this.lastLogin = pInfo.getLastLogin();
/*  74 */       this.lastLogout = pInfo.lastLogout;
/*  75 */       this.playerName = pInfo.getName();
/*  76 */       if (pInfo.currentServer != Servers.getLocalServerId()) {
/*     */         
/*  78 */         this.state = PlayerOnlineStatus.OTHER_SERVER;
/*     */       } else {
/*     */         PlayerOnlineStatus onoff;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  87 */           Player p = Players.getInstance().getPlayer(pInfo.wurmId);
/*  88 */           onoff = PlayerOnlineStatus.ONLINE;
/*     */         }
/*  90 */         catch (NoSuchPlayerException e) {
/*     */           
/*  92 */           onoff = PlayerOnlineStatus.OFFLINE;
/*     */         } 
/*  94 */         this.state = onoff;
/*     */       }
/*     */     
/*  97 */     } else if (pInfo != null) {
/*     */ 
/*     */       
/* 100 */       this.serverId = -1;
/* 101 */       this.lastLogin = 0L;
/* 102 */       this.lastLogout = 0L;
/* 103 */       this.playerName = "Error Loading";
/* 104 */       this.state = PlayerOnlineStatus.UNKNOWN;
/*     */     }
/*     */     else {
/*     */       
/* 108 */       this.serverId = -1;
/* 109 */       this.lastLogin = 0L;
/* 110 */       this.lastLogout = 0L;
/* 111 */       this.playerName = "Deleted";
/* 112 */       this.state = PlayerOnlineStatus.DELETE_ME;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerState(Player player, long aWhenStateChanged, PlayerOnlineStatus aState) {
/* 118 */     this(Servers.getLocalServerId(), player.getWurmId(), player.getName(), aWhenStateChanged, aState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerState(long aPlayerId, String aPlayerName, long aWhenStateChanged, PlayerOnlineStatus aState) {
/* 124 */     this(Servers.getLocalServerId(), aPlayerId, aPlayerName, aWhenStateChanged, aState);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerState(int aServerId, long aPlayerId, String aPlayerName, long aWhenStateChanged, PlayerOnlineStatus aState) {
/* 129 */     this.serverId = aServerId;
/* 130 */     this.playerId = aPlayerId;
/* 131 */     this.playerName = aPlayerName;
/* 132 */     this.state = aState;
/* 133 */     if (aState == PlayerOnlineStatus.ONLINE) {
/*     */       
/* 135 */       this.lastLogin = aWhenStateChanged;
/* 136 */       this.lastLogout = 0L;
/*     */     }
/*     */     else {
/*     */       
/* 140 */       this.lastLogin = 0L;
/* 141 */       this.lastLogout = aWhenStateChanged;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerState(long aPlayerId, String aPlayerName, long aLastLogin, long aLastLogout, PlayerOnlineStatus aState) {
/* 147 */     this(Servers.getLocalServerId(), aPlayerId, aPlayerName, aLastLogin, aLastLogout, aState);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerState(int aServerId, long aPlayerId, String aPlayerName, long aLastLogin, long aLastLogout, PlayerOnlineStatus aState) {
/* 152 */     this.serverId = aServerId;
/* 153 */     this.playerId = aPlayerId;
/* 154 */     this.playerName = aPlayerName;
/* 155 */     this.lastLogin = aLastLogin;
/* 156 */     this.lastLogout = aLastLogout;
/* 157 */     this.state = aState;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerState(byte[] aData) {
/* 162 */     decode(aData);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getServerId() {
/* 167 */     return this.serverId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getServerName() {
/* 172 */     ServerEntry server = Servers.getServerWithId(this.serverId);
/* 173 */     if (server == null) {
/* 174 */       return "Unknown";
/*     */     }
/* 176 */     return server.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/* 181 */     return this.playerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPlayerName() {
/* 186 */     return this.playerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastLogin() {
/* 191 */     return this.lastLogin;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastLogout() {
/* 196 */     return this.lastLogout;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getWhenStateChanged() {
/* 201 */     return Math.max(this.lastLogin, this.lastLogout);
/*     */   }
/*     */ 
/*     */   
/*     */   public PlayerOnlineStatus getState() {
/* 206 */     return this.state;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setState(PlayerOnlineStatus aState) {
/* 211 */     this.state = aState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatus(int aServerId, PlayerOnlineStatus aState, long aWhenStateChanged) {
/* 216 */     this.serverId = aServerId;
/* 217 */     this.state = aState;
/* 218 */     if (aState == PlayerOnlineStatus.ONLINE) {
/* 219 */       this.lastLogin = aWhenStateChanged;
/*     */     } else {
/* 221 */       this.lastLogout = aWhenStateChanged;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void decode(byte[] aData) {
/* 228 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 231 */       dis = new DataInputStream(new ByteArrayInputStream(aData));
/* 232 */       this.serverId = dis.readInt();
/* 233 */       this.playerId = dis.readLong();
/* 234 */       this.playerName = dis.readUTF();
/* 235 */       this.lastLogin = dis.readLong();
/* 236 */       this.lastLogout = dis.readLong();
/* 237 */       this.state = PlayerOnlineStatus.playerOnlineStatusFromId(dis.readByte());
/*     */     }
/* 239 */     catch (IOException ex) {
/*     */       
/* 241 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */       
/*     */       return;
/*     */     } finally {
/* 246 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final byte[] encode() {
/* 253 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 254 */     DataOutputStream dos = null;
/* 255 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 258 */       dos = new DataOutputStream(bos);
/* 259 */       dos.writeInt(this.serverId);
/* 260 */       dos.writeLong(this.playerId);
/* 261 */       dos.writeUTF(this.playerName);
/* 262 */       dos.writeLong(this.lastLogin);
/* 263 */       dos.writeLong(this.lastLogout);
/* 264 */       dos.writeByte(this.state.getId());
/* 265 */       dos.flush();
/* 266 */       dos.close();
/*     */     }
/* 268 */     catch (Exception ex) {
/*     */       
/* 270 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 274 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 275 */       barr = bos.toByteArray();
/* 276 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/*     */     } 
/* 278 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 289 */     return "PlayerState [ServerId=" + this.serverId + ", playerId=" + this.playerId + ", playerName=" + this.playerName + ", whenStateChanged=" + 
/* 290 */       getWhenStateChanged() + ", state=" + this.state + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PlayerState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */