/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.players.Spawnpoint;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public final class WcSpawnPoints
/*     */   extends WebCommand
/*     */ {
/*  43 */   private static final Logger logger = Logger.getLogger(WcSpawnPoints.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private Spawnpoint[] spawns;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcSpawnPoints(long _id) {
/*  53 */     super(_id, (short)21);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setSpawns(Spawnpoint[] spawnpoints) {
/*  58 */     this.spawns = spawnpoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcSpawnPoints(long _id, byte[] _data) {
/*  68 */     super(_id, (short)21, _data);
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
/*  79 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] encode() {
/*  90 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  91 */     DataOutputStream dos = null;
/*  92 */     byte[] barr = null;
/*     */     
/*     */     try {
/*  95 */       dos = new DataOutputStream(bos);
/*  96 */       if (this.spawns == null) {
/*  97 */         dos.writeInt(0);
/*     */       } else {
/*     */         
/* 100 */         dos.writeInt(this.spawns.length);
/* 101 */         for (Spawnpoint spawn : this.spawns) {
/*     */           
/* 103 */           dos.writeShort(spawn.tilex);
/* 104 */           dos.writeShort(spawn.tiley);
/* 105 */           dos.writeUTF(spawn.name);
/* 106 */           dos.writeUTF(spawn.description);
/* 107 */           dos.writeByte(spawn.kingdom);
/*     */         } 
/*     */       } 
/* 110 */       dos.flush();
/* 111 */       dos.close();
/*     */     }
/* 113 */     catch (Exception ex) {
/*     */       
/* 115 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 119 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 120 */       barr = bos.toByteArray();
/* 121 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 122 */       setData(barr);
/*     */     } 
/* 124 */     return barr;
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
/* 135 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 138 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 139 */       int nums = dis.readInt();
/* 140 */       if (nums > 0)
/*     */       {
/* 142 */         Set<Spawnpoint> lspawns = new HashSet<>();
/* 143 */         for (int x = 0; x < nums; x++) {
/*     */           
/* 145 */           short tilex = dis.readShort();
/* 146 */           short tiley = dis.readShort();
/* 147 */           String name = dis.readUTF();
/* 148 */           String desc = dis.readUTF();
/* 149 */           byte kingdom = dis.readByte();
/* 150 */           lspawns.add(new Spawnpoint(name, (byte)x, desc, tilex, tiley, true, kingdom));
/*     */         } 
/* 152 */         if (lspawns.size() > 0)
/*     */         {
/* 154 */           ServerEntry entry = Servers.getServerWithId(WurmId.getOrigin(getWurmId()));
/* 155 */           if (entry != null)
/*     */           {
/* 157 */             entry.setSpawns(lspawns.<Spawnpoint>toArray(new Spawnpoint[lspawns.size()]));
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 165 */     catch (IOException ex) {
/*     */       
/* 167 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 171 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcSpawnPoints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */