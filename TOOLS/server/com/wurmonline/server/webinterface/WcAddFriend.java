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
/*     */ public final class WcAddFriend
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  46 */   private static final Logger logger = Logger.getLogger(WcAddFriend.class.getName());
/*     */   
/*     */   public static final byte ASKING = 0;
/*     */   
/*     */   public static final byte UNKNOWN = 1;
/*     */   
/*     */   public static final byte OFFLINE = 2;
/*     */   
/*     */   public static final byte TIMEDOUT = 3;
/*     */   
/*     */   public static final byte ISBUSY = 4;
/*     */   
/*     */   public static final byte SUCCESS = 5;
/*     */   
/*     */   public static final byte REPLYING = 6;
/*     */   
/*     */   public static final byte FINISHED = 7;
/*     */   
/*     */   public static final byte IGNORED = 8;
/*     */   
/*     */   public static final byte SENT = 9;
/*     */   
/*     */   private byte reply;
/*     */   private String playerName;
/*     */   private byte playerKingdom;
/*     */   private String friendsName;
/*     */   private boolean xkingdom;
/*     */   
/*     */   public WcAddFriend(String aPlayerName, byte aKingdom, String aFriendName, byte aReply, boolean crossKingdom) {
/*  75 */     super(WurmId.getNextWCCommandId(), (short)25);
/*  76 */     this.reply = aReply;
/*  77 */     this.playerName = aPlayerName;
/*  78 */     this.playerKingdom = aKingdom;
/*  79 */     this.friendsName = aFriendName;
/*  80 */     this.xkingdom = crossKingdom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcAddFriend(long aId, byte[] aData) {
/*  89 */     super(aId, (short)25, aData);
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
/* 100 */     return false;
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
/* 111 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 112 */     DataOutputStream dos = null;
/* 113 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 116 */       dos = new DataOutputStream(bos);
/* 117 */       dos.writeByte(this.reply);
/* 118 */       dos.writeUTF(this.playerName);
/* 119 */       dos.writeByte(this.playerKingdom);
/* 120 */       dos.writeUTF(this.friendsName);
/* 121 */       dos.writeBoolean(this.xkingdom);
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
/*     */           try {
/* 155 */             dis = new DataInputStream(new ByteArrayInputStream(WcAddFriend.this.getData()));
/* 156 */             WcAddFriend.this.reply = dis.readByte();
/* 157 */             WcAddFriend.this.playerName = dis.readUTF();
/* 158 */             WcAddFriend.this.playerKingdom = dis.readByte();
/* 159 */             WcAddFriend.this.friendsName = dis.readUTF();
/* 160 */             WcAddFriend.this.xkingdom = dis.readBoolean();
/*     */           }
/* 162 */           catch (IOException ex) {
/*     */             
/* 164 */             WcAddFriend.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 169 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */           
/* 172 */           byte newReply = 7;
/* 173 */           if (Servers.isThisLoginServer())
/*     */           {
/* 175 */             newReply = WcAddFriend.this.sendToPlayerServer(WcAddFriend.this.friendsName);
/*     */           }
/* 177 */           if (newReply == 7) {
/*     */             
/*     */             try {
/*     */ 
/*     */               
/* 182 */               Player p = Players.getInstance().getPlayer(WcAddFriend.this.friendsName);
/* 183 */               newReply = p.remoteAddFriend(WcAddFriend.this.playerName, WcAddFriend.this.playerKingdom, WcAddFriend.this.reply, true, WcAddFriend.this.xkingdom);
/*     */             }
/* 185 */             catch (NoSuchPlayerException e) {
/*     */               
/* 187 */               newReply = 2;
/*     */             } 
/*     */           }
/* 190 */           if (newReply != 7 && newReply != 9)
/*     */           {
/*     */             
/* 193 */             WcAddFriend waf = new WcAddFriend(WcAddFriend.this.friendsName, WcAddFriend.this.playerKingdom, WcAddFriend.this.playerName, newReply, true);
/* 194 */             waf.sendToServer(WurmId.getOrigin(WcAddFriend.this.getWurmId()));
/*     */           }
/*     */         
/*     */         }
/* 198 */       }).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte sendToPlayerServer(String aFriendsName) {
/* 203 */     PlayerInfo pInfo = PlayerInfoFactory.createPlayerInfo(aFriendsName);
/* 204 */     if (pInfo != null) {
/*     */       
/*     */       try {
/*     */         
/* 208 */         pInfo.load();
/* 209 */         if (pInfo.currentServer != Servers.getLocalServerId()) {
/*     */           
/* 211 */           sendToServer(pInfo.currentServer);
/* 212 */           return 9;
/*     */         } 
/*     */         
/* 215 */         return 7;
/*     */       }
/* 217 */       catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     return 1;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcAddFriend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */