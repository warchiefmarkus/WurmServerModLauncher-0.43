/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
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
/*     */ public final class WcMgmtMessage
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(WcMgmtMessage.class.getName());
/*     */   
/*  40 */   private String sender = "unknown";
/*  41 */   private String message = "";
/*     */   private boolean emote = false;
/*     */   private boolean logit = false;
/*  44 */   private int colourR = -1;
/*  45 */   private int colourG = -1;
/*  46 */   private int colourB = -1;
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
/*     */   WcMgmtMessage(long aId, byte[] _data) {
/*  58 */     super(aId, (short)24, _data);
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
/*     */   
/*     */   public WcMgmtMessage(long aId, String _sender, String _message, boolean _emote, boolean logIt, int red, int green, int blue) {
/*  72 */     super(aId, (short)24);
/*  73 */     this.sender = _sender;
/*  74 */     this.message = _message;
/*  75 */     this.emote = _emote;
/*  76 */     this.logit = logIt;
/*  77 */     this.colourR = red;
/*  78 */     this.colourG = green;
/*  79 */     this.colourB = blue;
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
/*  90 */     return true;
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
/* 103 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 104 */     DataOutputStream dos = null;
/* 105 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 108 */       dos = new DataOutputStream(bos);
/* 109 */       dos.writeUTF(this.sender);
/* 110 */       dos.writeUTF(this.message);
/* 111 */       dos.writeBoolean(this.emote);
/* 112 */       dos.writeBoolean(this.logit);
/* 113 */       dos.writeInt(this.colourR);
/* 114 */       dos.writeInt(this.colourG);
/* 115 */       dos.writeInt(this.colourB);
/* 116 */       dos.flush();
/* 117 */       dos.close();
/*     */     }
/* 119 */     catch (Exception ex) {
/*     */       
/* 121 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 125 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 126 */       barr = bos.toByteArray();
/* 127 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 128 */       setData(barr);
/*     */     } 
/* 130 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 137 */     DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 142 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 143 */       this.sender = dis.readUTF();
/* 144 */       this.message = dis.readUTF();
/* 145 */       this.emote = dis.readBoolean();
/* 146 */       this.logit = dis.readBoolean();
/* 147 */       this.colourR = dis.readInt();
/* 148 */       this.colourG = dis.readInt();
/* 149 */       this.colourB = dis.readInt();
/*     */       
/* 151 */       Players.getInstance().sendMgmtMessage(null, this.sender, this.message, this.emote, this.logit, this.colourR, this.colourG, this.colourB);
/*     */     }
/* 153 */     catch (IOException ex) {
/*     */       
/* 155 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 159 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcMgmtMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */