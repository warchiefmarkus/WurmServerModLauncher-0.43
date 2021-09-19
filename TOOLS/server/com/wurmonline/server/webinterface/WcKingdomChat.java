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
/*     */ 
/*     */ public class WcKingdomChat
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(WcKingdomChat.class.getName());
/*  41 */   private String sender = "unknown";
/*  42 */   private long senderId = -10L;
/*  43 */   private String message = "";
/*  44 */   private byte kingdom = 0;
/*     */   private boolean emote = false;
/*  46 */   private int colorR = 0;
/*  47 */   private int colorG = 0;
/*  48 */   private int colorB = 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcKingdomChat(long _id, byte[] _data) {
/*  81 */     super(_id, (short)13, _data);
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
/*     */   public boolean autoForward() {
/*  93 */     return true;
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
/* 106 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 107 */     DataOutputStream dos = null;
/* 108 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 111 */       dos = new DataOutputStream(bos);
/* 112 */       dos.writeUTF(this.sender);
/* 113 */       dos.writeLong(this.senderId);
/* 114 */       dos.writeUTF(this.message);
/* 115 */       dos.writeBoolean(this.emote);
/* 116 */       dos.writeByte(this.kingdom);
/* 117 */       dos.writeInt(this.colorR);
/* 118 */       dos.writeInt(this.colorG);
/* 119 */       dos.writeInt(this.colorB);
/* 120 */       dos.flush();
/* 121 */       dos.close();
/*     */     }
/* 123 */     catch (Exception ex) {
/*     */       
/* 125 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 129 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 130 */       barr = bos.toByteArray();
/* 131 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 132 */       setData(barr);
/*     */     } 
/* 134 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 140 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 145 */           DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 150 */             dis = new DataInputStream(new ByteArrayInputStream(WcKingdomChat.this.getData()));
/* 151 */             WcKingdomChat.this.sender = dis.readUTF();
/* 152 */             WcKingdomChat.this.senderId = dis.readLong();
/* 153 */             WcKingdomChat.this.message = dis.readUTF();
/* 154 */             WcKingdomChat.this.emote = dis.readBoolean();
/* 155 */             WcKingdomChat.this.kingdom = dis.readByte();
/* 156 */             WcKingdomChat.this.colorR = dis.readInt();
/* 157 */             WcKingdomChat.this.colorG = dis.readInt();
/* 158 */             WcKingdomChat.this.colorB = dis.readInt();
/* 159 */             Players.getInstance().sendGlobalKingdomMessage(null, WcKingdomChat.this.senderId, WcKingdomChat.this.sender, WcKingdomChat.this.message, WcKingdomChat.this.emote, WcKingdomChat.this.kingdom, WcKingdomChat.this.colorR, WcKingdomChat.this
/* 160 */                 .colorG, WcKingdomChat.this.colorB);
/*     */           }
/* 162 */           catch (IOException ex) {
/*     */             
/* 164 */             WcKingdomChat.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           }
/*     */           finally {
/*     */             
/* 168 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/* 171 */       }).start();
/*     */   }
/*     */   
/*     */   public WcKingdomChat(long aId, long _senderId, String _sender, String _message, boolean _emote, byte _kingdom, int r, int g, int b) {
/*     */     super(aId, (short)13);
/*     */     this.sender = _sender;
/*     */     this.senderId = _senderId;
/*     */     this.message = _message;
/*     */     this.emote = _emote;
/*     */     this.kingdom = _kingdom;
/*     */     this.colorR = r;
/*     */     this.colorG = g;
/*     */     this.colorB = b;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcKingdomChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */