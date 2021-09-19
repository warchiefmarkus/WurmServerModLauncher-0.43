/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.PortalQuestion;
/*     */ import com.wurmonline.server.sounds.SoundPlayer;
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
/*     */ public class WcOpenEpicPortal
/*     */   extends WebCommand
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(WcOpenEpicPortal.class.getName());
/*     */ 
/*     */   
/*     */   private boolean open = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public WcOpenEpicPortal(long _id, boolean toggleOpen) {
/*  53 */     super(_id, (short)12);
/*  54 */     this.open = toggleOpen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcOpenEpicPortal(long _id, byte[] _data) {
/*  64 */     super(_id, (short)12, _data);
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
/*  75 */     return true;
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
/*  86 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  87 */     DataOutputStream dos = null;
/*  88 */     byte[] barr = null;
/*     */     
/*     */     try {
/*  91 */       dos = new DataOutputStream(bos);
/*  92 */       dos.writeBoolean(this.open);
/*  93 */       dos.flush();
/*  94 */       dos.close();
/*     */     }
/*  96 */     catch (Exception ex) {
/*     */       
/*  98 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 102 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 103 */       barr = bos.toByteArray();
/* 104 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 105 */       setData(barr);
/*     */     } 
/* 107 */     return barr;
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
/* 118 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 121 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 122 */       this.open = dis.readBoolean();
/* 123 */       PortalQuestion.epicPortalsEnabled = this.open;
/* 124 */       Player[] players = Players.getInstance().getPlayers();
/* 125 */       for (Player p : players)
/*     */       {
/* 127 */         SoundPlayer.playSound("sound.music.song.mountaintop", (Creature)p, 2.0F);
/*     */       }
/* 129 */       if (Servers.localServer.LOGINSERVER)
/*     */       {
/* 131 */         WcOpenEpicPortal wccom = new WcOpenEpicPortal(WurmId.getNextWCCommandId(), PortalQuestion.epicPortalsEnabled);
/*     */         
/* 133 */         wccom.sendFromLoginServer();
/*     */       }
/*     */     
/* 136 */     } catch (IOException ex) {
/*     */       
/* 138 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 142 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcOpenEpicPortal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */