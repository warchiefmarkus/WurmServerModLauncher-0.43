/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.Message;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
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
/*     */ public final class WcGVHelpMessage
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(WcGVHelpMessage.class.getName());
/*     */   
/*  45 */   private String name = "";
/*  46 */   private String msg = "";
/*     */   private boolean emote = false;
/*  48 */   private int colourR = -1;
/*  49 */   private int colourG = -1;
/*  50 */   private int colourB = -1;
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
/*     */   public WcGVHelpMessage(String playerName, String message, boolean aEmote, int red, int green, int blue) {
/*  65 */     super(WurmId.getNextWCCommandId(), (short)29);
/*  66 */     this.name = playerName;
/*  67 */     this.msg = message;
/*  68 */     this.emote = aEmote;
/*  69 */     this.colourR = red;
/*  70 */     this.colourG = green;
/*  71 */     this.colourB = blue;
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
/*     */   WcGVHelpMessage(long aId, byte[] _data) {
/*  84 */     super(aId, (short)29, _data);
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
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode() {
/* 106 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 107 */     DataOutputStream dos = null;
/* 108 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 111 */       dos = new DataOutputStream(bos);
/* 112 */       dos.writeUTF(this.name);
/* 113 */       dos.writeUTF(this.msg);
/* 114 */       dos.writeBoolean(this.emote);
/* 115 */       dos.write((byte)this.colourR);
/* 116 */       dos.write((byte)this.colourG);
/* 117 */       dos.write((byte)this.colourB);
/* 118 */       dos.flush();
/* 119 */       dos.close();
/*     */     }
/* 121 */     catch (Exception ex) {
/*     */       
/* 123 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 127 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 128 */       barr = bos.toByteArray();
/* 129 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 130 */       setData(barr);
/*     */     } 
/* 132 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 138 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 141 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 142 */       this.name = dis.readUTF();
/* 143 */       this.msg = dis.readUTF();
/* 144 */       this.emote = dis.readBoolean();
/* 145 */       this.colourR = dis.read();
/* 146 */       this.colourG = dis.read();
/* 147 */       this.colourB = dis.read();
/*     */       
/* 149 */       if (Servers.isThisLoginServer() && !Server.getInstance().isPS())
/*     */       {
/*     */         
/* 152 */         for (ServerEntry se : Servers.getAllServers()) {
/*     */ 
/*     */           
/* 155 */           if (se.getId() != Servers.getLocalServerId() && se
/* 156 */             .getId() != WurmId.getOrigin(getWurmId())) {
/*     */             
/* 158 */             WcGVHelpMessage wchgm = new WcGVHelpMessage(this.name, this.msg, this.emote, this.colourR, this.colourG, this.colourB);
/*     */             
/* 160 */             wchgm.sendToServer(se.getId());
/*     */           } 
/*     */         } 
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 167 */       if (Servers.isThisLoginServer()) {
/*     */         Message mess;
/* 169 */         if (this.emote) {
/* 170 */           mess = new Message(null, (byte)6, "CA HELP", this.msg, this.colourR, this.colourG, this.colourB);
/*     */         } else {
/* 172 */           mess = new Message(null, (byte)12, "CA HELP", "<" + this.name + "> " + this.msg, this.colourR, this.colourG, this.colourB);
/*     */         } 
/* 174 */         Players.getInstance().sendPaMessage(mess);
/*     */       } else {
/*     */         Message mess;
/*     */         
/* 178 */         if (this.emote) {
/* 179 */           mess = new Message(null, (byte)6, "GV HELP", this.msg, this.colourR, this.colourG, this.colourB);
/*     */         } else {
/* 181 */           mess = new Message(null, (byte)12, "GV HELP", "<" + this.name + "> " + this.msg, this.colourR, this.colourG, this.colourB);
/*     */         } 
/* 183 */         Players.getInstance().sendGVMessage(mess);
/*     */       }
/*     */     
/* 186 */     } catch (IOException ex) {
/*     */       
/* 188 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 192 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcGVHelpMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */