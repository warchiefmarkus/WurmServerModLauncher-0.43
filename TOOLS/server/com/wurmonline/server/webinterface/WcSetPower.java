/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
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
/*     */ public class WcSetPower extends WebCommand {
/*  19 */   private static final Logger logger = Logger.getLogger(WcSetPower.class.getName());
/*     */   private String playerName;
/*     */   private int newPower;
/*     */   private String senderName;
/*     */   private int senderPower;
/*     */   private String response;
/*     */   
/*     */   public WcSetPower(String playerName, int newPower, String senderName, int senderPower, String response) {
/*  27 */     this();
/*  28 */     this.playerName = playerName;
/*  29 */     this.newPower = newPower;
/*  30 */     this.senderName = senderName;
/*  31 */     this.senderPower = senderPower;
/*  32 */     this.response = response;
/*     */   }
/*     */   
/*     */   WcSetPower(WcSetPower copy) {
/*  36 */     this();
/*  37 */     this.playerName = copy.playerName;
/*  38 */     this.newPower = copy.newPower;
/*  39 */     this.senderName = copy.senderName;
/*  40 */     this.senderPower = copy.senderPower;
/*  41 */     this.response = copy.response;
/*     */   }
/*     */   
/*     */   WcSetPower() {
/*  45 */     super(WurmId.getNextWCCommandId(), (short)33);
/*     */   }
/*     */ 
/*     */   
/*     */   public WcSetPower(long aId, byte[] _data) {
/*  50 */     super(aId, (short)33, _data);
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] encode() {
/*  55 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  56 */     DataOutputStream dos = null;
/*  57 */     byte[] byteArr = null;
/*     */     try {
/*  59 */       dos = new DataOutputStream(bos);
/*  60 */       dos.writeUTF(this.playerName);
/*  61 */       dos.writeInt(this.newPower);
/*  62 */       dos.writeUTF(this.senderName);
/*  63 */       dos.writeInt(this.senderPower);
/*  64 */       dos.writeUTF(this.response);
/*  65 */       dos.flush();
/*  66 */       dos.close();
/*  67 */     } catch (Exception ex) {
/*  68 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } finally {
/*     */       
/*  71 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/*  72 */       byteArr = bos.toByteArray();
/*  73 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/*  74 */       setData(byteArr);
/*     */     } 
/*     */     
/*  77 */     return byteArr;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/*  87 */     (new Thread()
/*     */       {
/*     */         public void run() {
/*  90 */           DataInputStream dis = null;
/*     */           try {
/*  92 */             dis = new DataInputStream(new ByteArrayInputStream(WcSetPower.this.getData()));
/*  93 */             WcSetPower.this.playerName = dis.readUTF();
/*  94 */             WcSetPower.this.newPower = dis.readInt();
/*  95 */             WcSetPower.this.senderName = dis.readUTF();
/*  96 */             WcSetPower.this.senderPower = dis.readInt();
/*  97 */             WcSetPower.this.response = dis.readUTF();
/*  98 */             if (!WcSetPower.this.response.equals("")) {
/*     */               
/*     */               try {
/* 101 */                 Player sender = Players.getInstance().getPlayer(WcSetPower.this.senderName);
/* 102 */                 sender.getCommunicator().sendSafeServerMessage(WcSetPower.this.response);
/*     */                 return;
/* 104 */               } catch (Exception exception) {}
/*     */             } else {
/*     */ 
/*     */               
/*     */               try {
/*     */                 
/* 110 */                 Player p = Players.getInstance().getPlayer(WcSetPower.this.playerName);
/* 111 */                 if (p.getPower() > WcSetPower.this.senderPower) {
/* 112 */                   WcSetPower.this.response = "They are more powerful than you. You cannot set their power.";
/*     */                 }
/*     */                 else {
/*     */                   
/* 116 */                   p.setPower((byte)WcSetPower.this.newPower);
/*     */                   
/* 118 */                   String powerName = getPowerName(WcSetPower.this.newPower);
/* 119 */                   p.getCommunicator().sendSafeServerMessage("Your status has been set by " + WcSetPower.this.senderName + " to " + powerName + "!");
/* 120 */                   WcSetPower.this.response = "You set the power of " + WcSetPower.this.playerName + " to the status of " + powerName;
/*     */                 }
/*     */               
/* 123 */               } catch (NoSuchPlayerException playerEx) {
/* 124 */                 PlayerInfo pinf = PlayerInfoFactory.createPlayerInfo(WcSetPower.this.playerName);
/*     */                 try {
/* 126 */                   pinf.load();
/* 127 */                   if (pinf.getPower() > WcSetPower.this.senderPower) {
/* 128 */                     WcSetPower.this.response = "They are more powerful than you. You cannot set their power.";
/*     */                   } else {
/*     */                     
/* 131 */                     pinf.setPower((byte)WcSetPower.this.newPower);
/* 132 */                     pinf.save();
/*     */                     
/* 134 */                     String powerName = getPowerName(WcSetPower.this.newPower);
/* 135 */                     WcSetPower.this.response = "You set the power of " + WcSetPower.this.playerName + " to the power of " + powerName;
/*     */                   } 
/* 137 */                 } catch (IOException ioEx) {
/* 138 */                   WcSetPower.this.response = "Error trying load or save player information who is currently offline.";
/*     */                 }
/*     */               
/*     */               }
/* 142 */               catch (IOException ioEx) {
/* 143 */                 WcSetPower.this.response = "Error trying to set the power on the player who is currently online.";
/*     */               
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 150 */           catch (IOException ex) {
/* 151 */             WcSetPower.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/* 152 */             WcSetPower.this.response = "Something went terribly wrong trying to set the power.";
/*     */           } finally {
/*     */             
/* 155 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */           
/* 158 */           if (WcSetPower.this.response.equals("")) {
/*     */             return;
/*     */           }
/*     */           try {
/* 162 */             WcSetPower wsp = new WcSetPower(WcSetPower.this);
/* 163 */             wsp.sendToServer(WurmId.getOrigin(WcSetPower.this.getWurmId()));
/* 164 */           } catch (Exception e) {
/* 165 */             WcSetPower.logger.log(Level.WARNING, "Could not send response back after setting power", e);
/*     */           } 
/*     */         }
/*     */         
/*     */         private String getPowerName(int power) {
/* 170 */           String powString = "normal adventurer";
/*     */           
/* 172 */           if (WcSetPower.this.newPower == 1) {
/* 173 */             powString = "hero";
/* 174 */           } else if (WcSetPower.this.newPower == 2) {
/* 175 */             powString = "demigod";
/* 176 */           } else if (WcSetPower.this.newPower == 3) {
/* 177 */             powString = "high god";
/* 178 */           } else if (WcSetPower.this.newPower == 4) {
/* 179 */             powString = "arch angel";
/* 180 */           } else if (WcSetPower.this.newPower == 5) {
/* 181 */             powString = "implementor";
/*     */           } 
/* 183 */           return powString;
/*     */         }
/* 185 */       }).start();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcSetPower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */