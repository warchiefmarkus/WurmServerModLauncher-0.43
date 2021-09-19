/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class WcGetHeroes extends WebCommand {
/*  17 */   private static final Logger logger = Logger.getLogger(WcSetPower.class.getName());
/*     */   
/*     */   private long sender;
/*     */   private byte powerToCheck;
/*     */   private String response;
/*     */   
/*     */   public WcGetHeroes() {
/*  24 */     super(WurmId.getNextWCCommandId(), (short)34);
/*     */   }
/*     */   
/*     */   public WcGetHeroes(WcGetHeroes copy) {
/*  28 */     this();
/*  29 */     this.sender = copy.sender;
/*  30 */     this.powerToCheck = copy.powerToCheck;
/*  31 */     this.response = copy.response;
/*     */   }
/*     */   
/*     */   public WcGetHeroes(long _id, byte[] _data) {
/*  35 */     super(_id, (short)34, _data);
/*     */   }
/*     */   
/*     */   public WcGetHeroes(long sender, byte powerToCheck) {
/*  39 */     this();
/*  40 */     this.sender = sender;
/*  41 */     this.powerToCheck = powerToCheck;
/*  42 */     this.response = "";
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] encode() {
/*  47 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  48 */     DataOutputStream dos = null;
/*  49 */     byte[] byteArr = null;
/*     */     try {
/*  51 */       dos = new DataOutputStream(bos);
/*  52 */       dos.writeLong(this.sender);
/*  53 */       dos.writeByte(this.powerToCheck);
/*  54 */       dos.writeUTF(this.response);
/*     */     }
/*  56 */     catch (Exception ex) {
/*  57 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     } finally {
/*     */       
/*  60 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/*  61 */       byteArr = bos.toByteArray();
/*  62 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/*  63 */       setData(byteArr);
/*     */     } 
/*  65 */     return byteArr;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/*  75 */     (new Thread()
/*     */       {
/*     */         public void run() {
/*  78 */           DataInputStream dis = null;
/*     */           try {
/*  80 */             dis = new DataInputStream(new ByteArrayInputStream(WcGetHeroes.this.getData()));
/*  81 */             WcGetHeroes.this.sender = dis.readLong();
/*  82 */             WcGetHeroes.this.powerToCheck = dis.readByte();
/*  83 */             WcGetHeroes.this.response = dis.readUTF();
/*     */             
/*  85 */             if (isResponse(WcGetHeroes.this.response)) {
/*  86 */               sendResponseToPlayer();
/*     */             } else {
/*     */               
/*  89 */               WcGetHeroes.this.response = WcGetHeroes.getHeroes(WcGetHeroes.this.powerToCheck);
/*     */               
/*  91 */               WcGetHeroes wcg = new WcGetHeroes(WcGetHeroes.this);
/*  92 */               wcg.sendToServer(WurmId.getOrigin(WcGetHeroes.this.getWurmId()));
/*     */             } 
/*  94 */           } catch (IOException ex) {
/*  95 */             WcGetHeroes.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           } finally {
/*     */             
/*  98 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/*     */         
/*     */         private boolean isResponse(String response) {
/* 103 */           return !response.equals("");
/*     */         }
/*     */         
/*     */         private void sendResponseToPlayer() {
/*     */           try {
/* 108 */             Player senderPlayer = Players.getInstance().getPlayer(WcGetHeroes.this.sender);
/* 109 */             senderPlayer.getCommunicator().sendSafeServerMessage(WcGetHeroes.this.response);
/* 110 */           } catch (Exception exception) {}
/*     */         }
/* 114 */       }).start();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getPowerName(byte power) {
/* 119 */     String powerName = "heroes";
/* 120 */     if (power == 2) {
/* 121 */       powerName = "demigods";
/* 122 */     } else if (power == 3) {
/* 123 */       powerName = "high gods";
/* 124 */     } else if (power == 4) {
/* 125 */       powerName = "archangels";
/* 126 */     } else if (power == 5) {
/* 127 */       powerName = "implementors";
/*     */     } 
/* 129 */     return powerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getHeroes(byte powerToCheck) {
/* 134 */     String[] result = Players.getInstance().getHeros(powerToCheck);
/* 135 */     if (result.length == 0) {
/* 136 */       return Servers.localServer.getName() + " reports no " + getPowerName(powerToCheck);
/*     */     }
/*     */ 
/*     */     
/* 140 */     StringBuilder sb = new StringBuilder(Servers.localServer.getName() + " reports the following " + getPowerName(powerToCheck) + ": ");
/*     */     
/* 142 */     for (int i = 0; i < result.length - 1; i++) {
/* 143 */       sb.append(result[i]);
/* 144 */       sb.append(", ");
/*     */     } 
/* 146 */     sb.append(result[result.length - 1]);
/*     */     
/* 148 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcGetHeroes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */