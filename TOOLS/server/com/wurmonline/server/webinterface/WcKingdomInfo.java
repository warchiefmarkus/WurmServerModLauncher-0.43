/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
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
/*     */ public class WcKingdomInfo
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(WcKingdomInfo.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sendSingleKingdom = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte singleKingdomId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcKingdomInfo(long aId, byte[] aData) {
/*  65 */     super(aId, (short)7, aData);
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
/*  76 */     return true;
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
/*  87 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  88 */     DataOutputStream dos = null;
/*  89 */     byte[] barr = null;
/*     */     
/*     */     try {
/*  92 */       dos = new DataOutputStream(bos);
/*  93 */       dos.writeBoolean(this.sendSingleKingdom);
/*  94 */       if (this.sendSingleKingdom) {
/*     */         
/*  96 */         dos.writeInt(1);
/*  97 */         Kingdom k = Kingdoms.getKingdom(this.singleKingdomId);
/*     */         
/*  99 */         dos.writeByte(k.getId());
/* 100 */         dos.writeUTF(k.getName());
/* 101 */         dos.writeUTF(k.getPassword());
/* 102 */         dos.writeUTF(k.getChatName());
/* 103 */         dos.writeUTF(k.getSuffix());
/* 104 */         dos.writeUTF(k.getFirstMotto());
/* 105 */         dos.writeUTF(k.getSecondMotto());
/* 106 */         dos.writeByte(k.getTemplate());
/* 107 */         dos.writeBoolean(k.acceptsTransfers());
/*     */       }
/*     */       else {
/*     */         
/* 111 */         Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 112 */         dos.writeInt(kingdoms.length);
/* 113 */         for (Kingdom k : kingdoms) {
/*     */           
/* 115 */           dos.writeByte(k.getId());
/* 116 */           dos.writeUTF(k.getName());
/* 117 */           dos.writeUTF(k.getPassword());
/* 118 */           dos.writeUTF(k.getChatName());
/* 119 */           dos.writeUTF(k.getSuffix());
/* 120 */           dos.writeUTF(k.getFirstMotto());
/* 121 */           dos.writeUTF(k.getSecondMotto());
/* 122 */           dos.writeByte(k.getTemplate());
/* 123 */           dos.writeBoolean(k.acceptsTransfers());
/*     */         } 
/*     */       } 
/* 126 */       dos.flush();
/* 127 */       dos.close();
/*     */     }
/* 129 */     catch (Exception ex) {
/*     */       
/* 131 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 135 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 136 */       barr = bos.toByteArray();
/* 137 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 138 */       setData(barr);
/*     */     } 
/* 140 */     return barr;
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
/* 151 */     (new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/* 156 */           DataInputStream dis = null;
/*     */           
/*     */           try {
/* 159 */             dis = new DataInputStream(new ByteArrayInputStream(WcKingdomInfo.this.getData()));
/* 160 */             WcKingdomInfo.this.sendSingleKingdom = dis.readBoolean();
/* 161 */             int numKingdoms = dis.readInt();
/* 162 */             if (!WcKingdomInfo.this.sendSingleKingdom)
/*     */             {
/*     */ 
/*     */               
/* 166 */               Kingdoms.markAllKingdomsForDeletion();
/*     */             }
/* 168 */             for (int x = 0; x < numKingdoms; x++) {
/*     */               
/* 170 */               byte id = dis.readByte();
/* 171 */               String name = dis.readUTF();
/* 172 */               String password = dis.readUTF();
/* 173 */               String chatName = dis.readUTF();
/* 174 */               String suffix = dis.readUTF();
/* 175 */               String firstMotto = dis.readUTF();
/* 176 */               String secondMotto = dis.readUTF();
/* 177 */               byte templateKingdom = dis.readByte();
/* 178 */               boolean acceptsTransfers = dis.readBoolean();
/* 179 */               Kingdom kingdom = new Kingdom(id, templateKingdom, name, password, chatName, suffix, firstMotto, secondMotto, acceptsTransfers);
/*     */               
/* 181 */               if (Kingdoms.addKingdom(kingdom))
/* 182 */                 WcKingdomInfo.logger.log(Level.INFO, "Received " + name + " in WcKingdomInfo."); 
/*     */             } 
/* 184 */             if (!WcKingdomInfo.this.sendSingleKingdom) {
/* 185 */               Kingdoms.trimKingdoms();
/*     */             }
/* 187 */           } catch (IOException ex) {
/*     */             
/* 189 */             WcKingdomInfo.logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */           }
/*     */           finally {
/*     */             
/* 193 */             StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */           } 
/*     */         }
/* 196 */       }).start();
/*     */   }
/*     */   
/*     */   public WcKingdomInfo(long aId, boolean singleKingdom, byte kingdomId) {
/*     */     super(aId, (short)7);
/*     */     this.sendSingleKingdom = singleKingdom;
/*     */     this.singleKingdomId = kingdomId;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcKingdomInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */