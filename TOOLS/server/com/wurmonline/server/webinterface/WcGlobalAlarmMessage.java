/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
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
/*     */ 
/*     */ public final class WcGlobalAlarmMessage
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(WcGlobalAlarmMessage.class.getName());
/*     */   
/*  42 */   private String alertMessage3 = "";
/*  43 */   private long timeBetweenAlertMess3 = Long.MAX_VALUE;
/*  44 */   private String alertMessage4 = "";
/*  45 */   private long timeBetweenAlertMess4 = Long.MAX_VALUE;
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
/*     */   public WcGlobalAlarmMessage(String aMess3, long aInterval3, String aMess4, long aInterval4) {
/*  58 */     super(WurmId.getNextWCCommandId(), (short)22);
/*  59 */     this.alertMessage3 = aMess3;
/*  60 */     this.timeBetweenAlertMess3 = aInterval3;
/*  61 */     this.alertMessage4 = aMess4;
/*  62 */     this.timeBetweenAlertMess4 = aInterval4;
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
/*     */   WcGlobalAlarmMessage(long aId, byte[] _data) {
/*  75 */     super(aId, (short)22, _data);
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
/*  86 */     return true;
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
/*  99 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 100 */     DataOutputStream dos = null;
/* 101 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 104 */       dos = new DataOutputStream(bos);
/* 105 */       dos.writeUTF(this.alertMessage3);
/* 106 */       dos.writeLong(this.timeBetweenAlertMess3);
/* 107 */       dos.writeUTF(this.alertMessage4);
/* 108 */       dos.writeLong(this.timeBetweenAlertMess4);
/* 109 */       dos.flush();
/* 110 */       dos.close();
/*     */     }
/* 112 */     catch (Exception ex) {
/*     */       
/* 114 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 118 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 119 */       barr = bos.toByteArray();
/* 120 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 121 */       setData(barr);
/*     */     } 
/* 123 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 130 */     DataInputStream dis = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 135 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 136 */       this.alertMessage3 = dis.readUTF();
/* 137 */       this.timeBetweenAlertMess3 = dis.readLong();
/* 138 */       this.alertMessage4 = dis.readUTF();
/* 139 */       this.timeBetweenAlertMess4 = dis.readLong();
/*     */       
/* 141 */       Server.alertMessage3 = this.alertMessage3;
/* 142 */       Server.timeBetweenAlertMess3 = this.timeBetweenAlertMess3;
/* 143 */       if (this.alertMessage3.length() == 0) {
/* 144 */         Server.lastAlertMess3 = Long.MAX_VALUE;
/*     */       }
/* 146 */       Server.alertMessage4 = this.alertMessage4;
/* 147 */       Server.timeBetweenAlertMess4 = this.timeBetweenAlertMess4;
/* 148 */       if (this.alertMessage4.length() == 0) {
/* 149 */         Server.lastAlertMess4 = Long.MAX_VALUE;
/*     */       }
/* 151 */     } catch (IOException ex) {
/*     */       
/* 153 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 157 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcGlobalAlarmMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */