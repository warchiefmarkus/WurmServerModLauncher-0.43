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
/*     */ 
/*     */ public final class WCGmMessage
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(WCGmMessage.class.getName());
/*     */   
/*  41 */   private String sender = "unknown";
/*  42 */   private String message = "";
/*     */   private boolean emote = false;
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
/*     */   WCGmMessage(long aId, byte[] _data) {
/*  58 */     super(aId, (short)1, _data);
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
/*     */   public WCGmMessage(long aId, String _sender, String _message, boolean _emote) {
/*  71 */     super(aId, (short)1);
/*  72 */     this.sender = _sender;
/*  73 */     this.message = _message;
/*  74 */     this.emote = _emote;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public WCGmMessage(long aId, String _sender, String _message, boolean _emote, int red, int green, int blue) {
/*  80 */     super(aId, (short)1);
/*  81 */     this.sender = _sender;
/*  82 */     this.message = _message;
/*  83 */     this.emote = _emote;
/*  84 */     this.colourR = red;
/*  85 */     this.colourG = green;
/*  86 */     this.colourB = blue;
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
/*  97 */     return true;
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
/* 110 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 111 */     DataOutputStream dos = null;
/* 112 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 115 */       dos = new DataOutputStream(bos);
/* 116 */       dos.writeUTF(this.sender);
/* 117 */       dos.writeUTF(this.message);
/* 118 */       dos.writeBoolean(this.emote);
/* 119 */       dos.writeInt(this.colourR);
/* 120 */       dos.writeInt(this.colourG);
/* 121 */       dos.writeInt(this.colourB);
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
/*     */   public void execute() {
/* 143 */     DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 148 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 149 */       this.sender = dis.readUTF();
/* 150 */       this.message = dis.readUTF();
/* 151 */       this.emote = dis.readBoolean();
/* 152 */       this.colourR = dis.readInt();
/* 153 */       this.colourG = dis.readInt();
/* 154 */       this.colourB = dis.readInt();
/*     */       
/* 156 */       Players.getInstance().sendGmMessage(null, this.sender, this.message, this.emote, this.colourR, this.colourG, this.colourB);
/*     */     }
/* 158 */     catch (IOException ex) {
/*     */       
/* 160 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 164 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WCGmMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */