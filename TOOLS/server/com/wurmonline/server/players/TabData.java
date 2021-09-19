/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public class TabData
/*     */   implements MiscConstants
/*     */ {
/*     */   private final long wurmId;
/*     */   private final String name;
/*     */   private final byte power;
/*     */   private final boolean isVisible;
/*     */   private final int serverId;
/*     */   
/*     */   public TabData(long wurmid, String name, byte power, boolean isVisible) {
/*  51 */     this.wurmId = wurmid;
/*  52 */     this.name = name;
/*  53 */     this.isVisible = isVisible;
/*  54 */     this.power = power;
/*  55 */     this.serverId = Servers.getLocalServerId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TabData(DataInputStream dis) throws IOException {
/*  65 */     this.wurmId = dis.readLong();
/*  66 */     this.name = dis.readUTF();
/*  67 */     this.isVisible = dis.readBoolean();
/*  68 */     this.power = dis.readByte();
/*  69 */     this.serverId = dis.readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pack(DataOutputStream dos) throws IOException {
/*  79 */     dos.writeLong(this.wurmId);
/*  80 */     dos.writeUTF(this.name);
/*  81 */     dos.writeBoolean(this.isVisible);
/*  82 */     dos.writeByte(this.power);
/*  83 */     dos.writeInt(this.serverId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getWurmId() {
/*  92 */     return this.wurmId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 101 */     if (this.serverId != Servers.getLocalServerId()) {
/*     */       
/* 103 */       ServerEntry se = Servers.getServerWithId(this.serverId);
/* 104 */       if (se != null) {
/* 105 */         return this.name + " (" + se.getAbbreviation() + ")";
/*     */       }
/* 107 */       return this.name + " (" + this.serverId + ")";
/*     */     } 
/* 109 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getPower() {
/* 118 */     return this.power;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVisible() {
/* 127 */     return this.isVisible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getServerId() {
/* 136 */     return this.serverId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     return "(TabData:" + this.wurmId + "," + this.name + "," + this.power + "," + this.isVisible + "," + this.serverId + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\TabData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */