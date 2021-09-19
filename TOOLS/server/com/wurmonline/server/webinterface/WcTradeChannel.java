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
/*     */ public class WcTradeChannel
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(WcTradeChannel.class.getName());
/*  39 */   private String sender = "unknown";
/*  40 */   private long senderId = -10L;
/*  41 */   private String message = "";
/*  42 */   private byte kingdom = 0;
/*  43 */   private int colorR = 0;
/*  44 */   private int colorG = 0;
/*  45 */   private int colorB = 0;
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
/*     */   public WcTradeChannel(long _id, byte[] _data) {
/*  77 */     super(_id, (short)28, _data);
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
/*  88 */     return true;
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
/*  99 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 100 */     DataOutputStream dos = null;
/* 101 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 104 */       dos = new DataOutputStream(bos);
/* 105 */       dos.writeUTF(this.sender);
/* 106 */       dos.writeLong(this.senderId);
/* 107 */       dos.writeUTF(this.message);
/* 108 */       dos.writeByte(this.kingdom);
/* 109 */       dos.writeInt(this.colorR);
/* 110 */       dos.writeInt(this.colorG);
/* 111 */       dos.writeInt(this.colorB);
/* 112 */       dos.flush();
/* 113 */       dos.close();
/*     */     }
/* 115 */     catch (Exception ex) {
/*     */       
/* 117 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 121 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 122 */       barr = bos.toByteArray();
/* 123 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 124 */       setData(barr);
/*     */     } 
/* 126 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 132 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 137 */           DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 142 */             dis = new DataInputStream(new ByteArrayInputStream(WcTradeChannel.this.getData()));
/* 143 */             WcTradeChannel.this.sender = dis.readUTF();
/* 144 */             WcTradeChannel.this.senderId = dis.readLong();
/* 145 */             WcTradeChannel.this.message = dis.readUTF();
/* 146 */             WcTradeChannel.this.kingdom = dis.readByte();
/* 147 */             WcTradeChannel.this.colorR = dis.readInt();
/* 148 */             WcTradeChannel.this.colorG = dis.readInt();
/* 149 */             WcTradeChannel.this.colorB = dis.readInt();
/* 150 */             Players.getInstance().sendGlobalTradeMessage(null, WcTradeChannel.this.senderId, WcTradeChannel.this.sender, WcTradeChannel.this.message, WcTradeChannel.this
/* 151 */                 .kingdom, WcTradeChannel.this.colorR, WcTradeChannel.this.colorG, WcTradeChannel.this.colorB);
/*     */           }
/* 153 */           catch (IOException ex) {
/*     */             
/* 155 */             WcTradeChannel.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           }
/*     */           finally {
/*     */             
/* 159 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/* 162 */       }).start();
/*     */   }
/*     */   
/*     */   public WcTradeChannel(long aId, long _senderId, String _sender, String _message, byte _kingdom, int r, int g, int b) {
/*     */     super(aId, (short)28);
/*     */     this.sender = _sender;
/*     */     this.senderId = _senderId;
/*     */     this.message = _message;
/*     */     this.kingdom = _kingdom;
/*     */     this.colorR = r;
/*     */     this.colorG = g;
/*     */     this.colorB = b;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcTradeChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */