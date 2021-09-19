/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.epic.Effectuator;
/*     */ import com.wurmonline.server.epic.EpicEntity;
/*     */ import com.wurmonline.server.epic.EpicMission;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.epic.HexMap;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public final class WcEpicStatusReport
/*     */   extends WebCommand
/*     */ {
/*  52 */   private static final Logger logger = Logger.getLogger(WcEpicStatusReport.class.getName());
/*     */   private boolean success = false;
/*     */   private boolean entityStatusMessage = false;
/*  55 */   private long entityId = 0L;
/*  56 */   private byte missionType = -1;
/*  57 */   private int missionDifficulty = -1;
/*  58 */   private final Map<String, Integer> entityStatuses = new HashMap<>();
/*  59 */   private final Map<Integer, Integer> entityHexes = new HashMap<>();
/*     */   
/*  61 */   private final Map<Integer, String> entityMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcEpicStatusReport(long aId, boolean wasSuccess, int epicEntityId, byte type, int difficulty) {
/*  68 */     super(aId, (short)10);
/*  69 */     this.success = wasSuccess;
/*  70 */     this.entityId = epicEntityId;
/*  71 */     this.missionType = type;
/*  72 */     this.missionDifficulty = difficulty;
/*  73 */     this.isRestrictedEpic = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WcEpicStatusReport(long aId, byte[] _data) {
/*  81 */     super(aId, (short)10, _data);
/*  82 */     this.isRestrictedEpic = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addEntityStatus(String status, int statusEntityId) {
/*  87 */     this.entityStatuses.put(status, Integer.valueOf(statusEntityId));
/*  88 */     this.entityStatusMessage = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addEntityHex(int entity, int hexId) {
/*  93 */     this.entityHexes.put(Integer.valueOf(entity), Integer.valueOf(hexId));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void fillStatusReport(HexMap map) {
/*  99 */     EpicEntity[] entities = map.getAllEntities();
/* 100 */     for (EpicEntity entity : entities) {
/*     */ 
/*     */       
/* 103 */       if (entity.isDeity())
/* 104 */         this.entityMap.put(Integer.valueOf((int)entity.getId()), entity.getName()); 
/* 105 */       addEntityStatus(entity.getLocationStatus(), (int)entity.getId());
/* 106 */       addEntityStatus(entity.getEnemyStatus(), (int)entity.getId());
/* 107 */       int collsCarried = entity.countCollectables();
/* 108 */       if (collsCarried > 0)
/*     */       {
/* 110 */         addEntityStatus(entity.getName() + " is carrying " + collsCarried + " of the " + entity.getCollectibleName() + ".", 
/* 111 */             (int)entity.getId());
/*     */       }
/* 113 */       if (entity.isDeity())
/*     */       {
/* 115 */         if (entity.getMapHex() != null) {
/* 116 */           addEntityHex((int)entity.getId(), entity.getMapHex().getId());
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean autoForward() {
/* 129 */     return true;
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
/* 140 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 141 */     DataOutputStream dos = null;
/* 142 */     byte[] barr = null;
/*     */     
/*     */     try {
/* 145 */       dos = new DataOutputStream(bos);
/* 146 */       dos.writeBoolean(this.entityStatusMessage);
/* 147 */       if (this.entityStatusMessage) {
/*     */         
/* 149 */         dos.writeInt(this.entityMap.size());
/* 150 */         for (Map.Entry<Integer, String> entry : this.entityMap.entrySet()) {
/*     */           
/* 152 */           dos.writeInt(((Integer)entry.getKey()).intValue());
/* 153 */           dos.writeUTF(entry.getValue());
/*     */         } 
/* 155 */         dos.writeInt(this.entityStatuses.size());
/* 156 */         if (this.entityStatuses.size() > 0)
/*     */         {
/* 158 */           for (Map.Entry<String, Integer> entry : this.entityStatuses.entrySet()) {
/*     */             
/* 160 */             dos.writeUTF(entry.getKey());
/* 161 */             dos.writeInt(((Integer)entry.getValue()).intValue());
/*     */           } 
/*     */         }
/* 164 */         dos.writeInt(this.entityHexes.size());
/* 165 */         if (this.entityHexes.size() > 0)
/*     */         {
/* 167 */           for (Map.Entry<Integer, Integer> entry : this.entityHexes.entrySet())
/*     */           {
/* 169 */             dos.writeInt(((Integer)entry.getKey()).intValue());
/* 170 */             dos.writeInt(((Integer)entry.getValue()).intValue());
/*     */           }
/*     */         
/*     */         }
/*     */       } else {
/*     */         
/* 176 */         dos.writeBoolean(this.success);
/* 177 */         dos.writeLong(this.entityId);
/* 178 */         dos.writeByte(this.missionType);
/* 179 */         dos.writeInt(this.missionDifficulty);
/*     */       } 
/* 181 */       dos.flush();
/* 182 */       dos.close();
/*     */     }
/* 184 */     catch (Exception ex) {
/*     */       
/* 186 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 190 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 191 */       barr = bos.toByteArray();
/* 192 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 193 */       setData(barr);
/*     */     } 
/* 195 */     return barr;
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
/*     */   
/*     */   public void execute() {
/* 210 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 213 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/* 214 */       this.entityStatusMessage = dis.readBoolean();
/* 215 */       if (this.entityStatusMessage) {
/*     */         
/* 217 */         Deities.clearValreiStatuses();
/* 218 */         int numsx = dis.readInt();
/* 219 */         for (int x = 0; x < numsx; x++) {
/*     */           
/* 221 */           int entity = dis.readInt();
/* 222 */           String name = dis.readUTF();
/* 223 */           Deities.addEntity(entity, name);
/*     */         } 
/* 225 */         int nums = dis.readInt();
/* 226 */         for (int i = 0; i < nums; i++) {
/*     */           
/* 228 */           String status = dis.readUTF();
/* 229 */           int entity = dis.readInt();
/* 230 */           Deities.addStatus(status, entity);
/*     */         } 
/* 232 */         int numsPos = dis.readInt();
/* 233 */         for (int j = 0; j < numsPos; j++)
/*     */         {
/* 235 */           int entity = dis.readInt();
/* 236 */           int hexPos = dis.readInt();
/* 237 */           Deities.addPosition(entity, hexPos);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 242 */         this.success = dis.readBoolean();
/* 243 */         this.entityId = dis.readLong();
/* 244 */         this.missionType = dis.readByte();
/* 245 */         this.missionDifficulty = dis.readInt();
/* 246 */         ServerEntry entry = Servers.getServerWithId(WurmId.getOrigin(getWurmId()));
/* 247 */         if (entry != null)
/*     */         {
/* 249 */           if (Server.getEpicMap() != null)
/*     */           {
/* 251 */             if (entry.EPIC)
/*     */             {
/* 253 */               EpicMission mission = EpicServerStatus.getEpicMissionForEntity((int)this.entityId);
/* 254 */               if (mission != null) {
/*     */                 
/* 256 */                 if (!Servers.localServer.EPIC && this.success) {
/*     */ 
/*     */                   
/* 259 */                   float oldStatus = mission.getMissionProgress();
/* 260 */                   mission.updateProgress(oldStatus + 1.0F);
/*     */                 } 
/* 262 */                 EpicEntity entity = Server.getEpicMap().getEntity(this.entityId);
/* 263 */                 if (entity != null) {
/*     */                   
/* 265 */                   Date now = new Date();
/* 266 */                   DateFormat format = DateFormat.getDateInstance(3);
/* 267 */                   if (this.success) {
/*     */                     
/* 269 */                     Server.getEpicMap().broadCast(entity
/* 270 */                         .getName() + " received help from " + entry.name + ". " + format
/* 271 */                         .format(now) + " " + Server.rand.nextInt(1000));
/* 272 */                     Server.getEpicMap().setEntityHelped(this.entityId, this.missionType, this.missionDifficulty);
/*     */                   }
/*     */                   else {
/*     */                     
/* 276 */                     Server.getEpicMap().broadCast(entity
/* 277 */                         .getName() + " never received help from " + entry.name + ". " + format
/* 278 */                         .format(now) + " " + Server.rand.nextInt(1000));
/* 279 */                     if (entity.isDeity()) {
/* 280 */                       entity.setShouldCreateMission(true, false);
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */               } else {
/*     */                 
/* 286 */                 EpicEntity entity = Server.getEpicMap().getEntity(this.entityId);
/* 287 */                 Date now = new Date();
/* 288 */                 DateFormat format = DateFormat.getDateInstance(3);
/* 289 */                 Server.getEpicMap().broadCast(entity
/* 290 */                     .getName() + " did not have an active mission when receiving help from " + entry.name + ". " + format
/*     */                     
/* 292 */                     .format(now) + " " + Server.rand.nextInt(1000));
/* 293 */                 entity.setShouldCreateMission(true, false);
/*     */               
/*     */               }
/*     */             
/*     */             }
/* 298 */             else if (this.success)
/*     */             {
/*     */               
/* 301 */               EpicEntity entity = Server.getEpicMap().getEntity(this.entityId);
/* 302 */               if (entity != null)
/*     */               {
/* 304 */                 int effect = Server.rand.nextInt(4) + 1;
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 309 */                 WcEpicEvent wce = new WcEpicEvent(WurmId.getNextWCCommandId(), 0, this.entityId, 0, effect, entity.getName() + "s followers now have the attention of the " + Effectuator.getSpiritType(effect) + " spirits.", false);
/* 310 */                 wce.sendToServer(entry.id);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           }
/*     */         }
/*     */       } 
/* 318 */     } catch (IOException ex) {
/*     */       
/* 320 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 324 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WcEpicStatusReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */