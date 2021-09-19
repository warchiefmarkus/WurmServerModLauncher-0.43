/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.TabData;
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
/*     */ public final class WcTabLists
/*     */   extends WebCommand
/*     */   implements MiscConstants
/*     */ {
/*  40 */   private static final Logger logger = Logger.getLogger(WcTabLists.class.getName());
/*     */ 
/*     */   
/*     */   public static final byte TAB_GM = 0;
/*     */ 
/*     */   
/*     */   public static final byte TAB_MGMT = 1;
/*     */ 
/*     */   
/*     */   public static final byte REMOVE = 2;
/*     */ 
/*     */   
/*     */   private byte tab;
/*     */ 
/*     */   
/*     */   private TabData tabData;
/*     */ 
/*     */   
/*     */   WcTabLists(long aId, byte[] _data) {
/*  59 */     super(aId, (short)31, _data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcTabLists(byte tab, TabData tabData) {
/*  70 */     super(WurmId.getNextWCCommandId(), (short)31);
/*  71 */     this.tab = tab;
/*  72 */     this.tabData = tabData;
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
/*  83 */     return true;
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
/* 100 */       dos.writeByte(this.tab);
/* 101 */       this.tabData.pack(dos);
/* 102 */       dos.flush();
/* 103 */       dos.close();
/*     */     }
/* 105 */     catch (Exception ex) {
/*     */       
/* 107 */       logger.log(Level.WARNING, "Pack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 111 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 112 */       barr = bos.toByteArray();
/* 113 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 114 */       setData(barr);
/*     */     } 
/* 116 */     return barr;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 122 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 125 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 126 */       this.tab = dis.readByte();
/* 127 */       this.tabData = new TabData(dis);
/* 128 */       Players.getInstance().updateTabs(this.tab, this.tabData);
/*     */     }
/* 130 */     catch (IOException ex) {
/*     */       
/* 132 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 136 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcTabLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */