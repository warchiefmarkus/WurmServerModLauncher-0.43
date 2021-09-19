/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.support.Trello;
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
/*     */ public final class WcTrelloDeaths
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  42 */   private static final Logger logger = Logger.getLogger(WcTrelloDeaths.class.getName());
/*     */   
/*  44 */   private String server = "";
/*  45 */   private String title = "";
/*  46 */   private String description = "";
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
/*     */   WcTrelloDeaths(long aId, byte[] _data) {
/*  58 */     super(aId, (short)35, _data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcTrelloDeaths(String title, String description) {
/*  69 */     super(WurmId.getNextWCCommandId(), (short)35);
/*  70 */     this.server = Servers.localServer.getAbbreviation();
/*  71 */     this.title = title;
/*  72 */     this.description = description;
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
/*  83 */     return false;
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
/*  94 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  95 */     DataOutputStream dos = null;
/*  96 */     byte[] barr = null;
/*     */     
/*     */     try {
/*  99 */       dos = new DataOutputStream(bos);
/* 100 */       dos.writeUTF(this.server);
/* 101 */       dos.writeUTF(this.title);
/* 102 */       dos.writeUTF(this.description);
/* 103 */       dos.flush();
/* 104 */       dos.close();
/*     */     }
/* 106 */     catch (Exception ex) {
/*     */       
/* 108 */       logger.log(Level.WARNING, "Pack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 112 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 113 */       barr = bos.toByteArray();
/* 114 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 115 */       setData(barr);
/*     */     } 
/* 117 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 123 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 126 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 127 */       this.server = dis.readUTF();
/* 128 */       this.title = dis.readUTF();
/* 129 */       this.description = dis.readUTF();
/* 130 */       Trello.addImportantDeathsMessage(this.server, this.title, this.description);
/*     */     }
/* 132 */     catch (IOException ex) {
/*     */       
/* 134 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 138 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcTrelloDeaths.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */