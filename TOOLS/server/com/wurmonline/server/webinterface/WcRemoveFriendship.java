/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
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
/*     */ 
/*     */ public final class WcRemoveFriendship
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(WcRemoveFriendship.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private String playerName;
/*     */ 
/*     */   
/*     */   private long playerWurmId;
/*     */ 
/*     */   
/*     */   private String friendName;
/*     */ 
/*     */   
/*     */   private long friendWurmId;
/*     */ 
/*     */ 
/*     */   
/*     */   public WcRemoveFriendship(String aPlayerName, long aPlayerWurmId, String aFriendName, long aFriendWurmId) {
/*  61 */     super(WurmId.getNextWCCommandId(), (short)4);
/*  62 */     this.playerName = aPlayerName;
/*  63 */     this.playerWurmId = aPlayerWurmId;
/*  64 */     this.friendName = aFriendName;
/*  65 */     this.friendWurmId = aFriendWurmId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcRemoveFriendship(long aId, byte[] aData) {
/*  74 */     super(aId, (short)4, aData);
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
/*  85 */     return true;
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
/*  96 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  97 */     DataOutputStream dos = null;
/*  98 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 101 */       dos = new DataOutputStream(bos);
/* 102 */       dos.writeUTF(this.playerName);
/* 103 */       dos.writeLong(this.playerWurmId);
/* 104 */       dos.writeUTF(this.friendName);
/* 105 */       dos.writeLong(this.friendWurmId);
/* 106 */       dos.flush();
/* 107 */       dos.close();
/*     */     }
/* 109 */     catch (Exception ex) {
/*     */       
/* 111 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 115 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 116 */       barr = bos.toByteArray();
/* 117 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 118 */       setData(barr);
/*     */     } 
/* 120 */     return barr;
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
/* 131 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 136 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 139 */             dis = new DataInputStream(new ByteArrayInputStream(WcRemoveFriendship.this.getData()));
/* 140 */             WcRemoveFriendship.this.playerName = dis.readUTF();
/* 141 */             WcRemoveFriendship.this.playerWurmId = dis.readLong();
/* 142 */             WcRemoveFriendship.this.friendName = dis.readUTF();
/* 143 */             WcRemoveFriendship.this.friendWurmId = dis.readLong();
/*     */           }
/* 145 */           catch (IOException ex) {
/*     */             
/* 147 */             WcRemoveFriendship.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */ 
/*     */             
/*     */             return;
/*     */           } finally {
/* 152 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */           
/* 155 */           if (Servers.isThisLoginServer()) {
/*     */ 
/*     */             
/* 158 */             if (WcRemoveFriendship.this.friendWurmId == -10L) {
/*     */               
/* 160 */               PlayerInfo fInfo = PlayerInfoFactory.getPlayerInfoWithName(WcRemoveFriendship.this.friendName);
/* 161 */               if (fInfo != null)
/*     */               {
/*     */ 
/*     */ 
/*     */                 
/* 166 */                 WcRemoveFriendship.this.friendWurmId = fInfo.wurmId;
/*     */               }
/*     */             } 
/* 169 */             WcRemoveFriendship.this.sendFromLoginServer();
/*     */           } 
/* 171 */           PlayerInfoFactory.breakFriendship(WcRemoveFriendship.this.playerName, WcRemoveFriendship.this.playerWurmId, WcRemoveFriendship.this.friendName, WcRemoveFriendship.this.friendWurmId);
/*     */         }
/* 173 */       }).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcRemoveFriendship.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */