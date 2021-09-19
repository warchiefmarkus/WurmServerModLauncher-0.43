/*     */ package com.wurmonline.server.webinterface;
/*     */ 
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.epic.CollectedValreiItem;
/*     */ import com.wurmonline.server.epic.EpicEntity;
/*     */ import com.wurmonline.server.epic.EpicServerStatus;
/*     */ import com.wurmonline.server.epic.ValreiFightHistory;
/*     */ import com.wurmonline.server.epic.ValreiFightHistoryManager;
/*     */ import com.wurmonline.server.epic.ValreiMapData;
/*     */ import com.wurmonline.shared.constants.ValreiConstants;
/*     */ import com.wurmonline.shared.util.StreamUtilities;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class WCValreiMapUpdater
/*     */   extends WebCommand
/*     */ {
/*  48 */   private static final Logger logger = Logger.getLogger(WCValreiMapUpdater.class.getName());
/*     */   
/*     */   public static final byte INITIAL_REQUEST = 0;
/*     */   
/*     */   public static final byte INITIAL_REQUEST_RESPONCE = 1;
/*     */   public static final byte UPDATE = 2;
/*     */   public static final byte REQUEST_TIME_UPDATE = 3;
/*     */   public static final byte SEND_TIME = 4;
/*     */   public static final byte NEW_FIGHT = 5;
/*     */   private byte messageType;
/*  58 */   private ValreiMapData toUpdate = null;
/*  59 */   private final List<EpicEntity> dataList = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public WCValreiMapUpdater(long aid, byte _messageType) {
/*  63 */     super(aid, (short)27);
/*  64 */     this.messageType = _messageType;
/*     */   }
/*     */ 
/*     */   
/*     */   public WCValreiMapUpdater(long aid, byte[] data) {
/*  69 */     super(aid, (short)27, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void collectData() {
/*  74 */     if (this.dataList != null && this.dataList.size() > 0) {
/*  75 */       this.dataList.clear();
/*     */     }
/*  77 */     for (EpicEntity ent : EpicServerStatus.getValrei().getAllEntities())
/*     */     {
/*  79 */       this.dataList.add(ent);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityToUpdate(EpicEntity entity) {
/*  85 */     long eId = entity.getId();
/*  86 */     int hexId = (entity.getMapHex() != null) ? entity.getMapHex().getId() : -1;
/*  87 */     int type = entity.getType();
/*  88 */     int targetHex = entity.getTargetHex();
/*  89 */     String name = entity.getName();
/*  90 */     long now = System.currentTimeMillis();
/*  91 */     long remaining = entity.getTimeToNextHex() - now;
/*  92 */     float attack = entity.getAttack();
/*  93 */     float vitality = entity.getVitality();
/*  94 */     List<EpicEntity> list = entity.getAllCollectedItems();
/*  95 */     List<CollectedValreiItem> valList = CollectedValreiItem.fromList(list);
/*     */     
/*  97 */     float bodyStr = entity.getCurrentSkill(102);
/*  98 */     float bodySta = entity.getCurrentSkill(103);
/*  99 */     float bodyCon = entity.getCurrentSkill(104);
/* 100 */     float mindLog = entity.getCurrentSkill(100);
/* 101 */     float mindSpe = entity.getCurrentSkill(101);
/* 102 */     float soulStr = entity.getCurrentSkill(105);
/* 103 */     float soulDep = entity.getCurrentSkill(106);
/*     */     
/* 105 */     this.toUpdate = new ValreiMapData(eId, hexId, type, targetHex, name, remaining, bodyStr, bodySta, bodyCon, mindLog, mindSpe, soulStr, soulDep, valList);
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
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] encode() {
/* 122 */     if (this.messageType == 2) {
/*     */       
/* 124 */       if (this.toUpdate != null)
/* 125 */         return createUpdateMessage(); 
/*     */     } else {
/* 127 */       if (this.messageType == 0)
/*     */       {
/* 129 */         return createInitialRequestMessage();
/*     */       }
/* 131 */       if (this.messageType == 1) {
/*     */         
/* 133 */         collectData();
/* 134 */         return createInitialResponceMessage();
/*     */       } 
/* 136 */       if (this.messageType == 3)
/*     */       {
/* 138 */         return createTimeUpdateRequest();
/*     */       }
/* 140 */       if (this.messageType == 4) {
/*     */         
/* 142 */         collectData();
/* 143 */         return createTimeUpdateMessage();
/*     */       } 
/* 145 */       if (this.messageType == 5)
/*     */       {
/* 147 */         return createFightDetails();
/*     */       }
/*     */     } 
/* 150 */     return new byte[0];
/*     */   }
/*     */ 
/*     */   
/*     */   private final byte[] createTimeUpdateRequest() {
/* 155 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 156 */     DataOutputStream dos = null;
/* 157 */     byte[] byteData = null;
/*     */ 
/*     */     
/*     */     try {
/* 161 */       dos = new DataOutputStream(bos);
/* 162 */       dos.writeByte(3);
/*     */     }
/* 164 */     catch (Exception ex) {
/*     */       
/* 166 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 170 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 171 */       byteData = bos.toByteArray();
/* 172 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 173 */       setData(byteData);
/*     */     } 
/*     */     
/* 176 */     return byteData;
/*     */   }
/*     */ 
/*     */   
/*     */   private final byte[] createTimeUpdateMessage() {
/* 181 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 182 */     DataOutputStream dos = null;
/* 183 */     byte[] byteData = null;
/*     */ 
/*     */     
/*     */     try {
/* 187 */       dos = new DataOutputStream(bos);
/* 188 */       dos.writeByte(4);
/* 189 */       int count = 0; Iterator<EpicEntity> it;
/* 190 */       for (it = this.dataList.iterator(); it.hasNext(); ) {
/*     */         
/* 192 */         EpicEntity entity = it.next();
/* 193 */         if (entity.isCollectable() || entity.isSource())
/*     */           continue; 
/* 195 */         count++;
/*     */       } 
/*     */       
/* 198 */       dos.writeInt(count);
/* 199 */       for (it = this.dataList.iterator(); it.hasNext(); )
/*     */       {
/* 201 */         EpicEntity entity = it.next();
/* 202 */         if (entity.isCollectable() || entity.isSource())
/*     */           continue; 
/* 204 */         dos.writeLong(entity.getId());
/* 205 */         long now = System.currentTimeMillis();
/* 206 */         long remaining = entity.getTimeUntilLeave() - now;
/* 207 */         dos.writeLong(remaining);
/*     */       }
/*     */     
/* 210 */     } catch (Exception ex) {
/*     */       
/* 212 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 216 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 217 */       byteData = bos.toByteArray();
/* 218 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 219 */       setData(byteData);
/*     */     } 
/*     */     
/* 222 */     return byteData;
/*     */   }
/*     */ 
/*     */   
/*     */   private final byte[] createUpdateMessage() {
/* 227 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 228 */     DataOutputStream dos = null;
/* 229 */     byte[] byteData = null;
/*     */ 
/*     */     
/*     */     try {
/* 233 */       dos = new DataOutputStream(bos);
/* 234 */       dos.writeByte(2);
/* 235 */       ValreiMapData entity = this.toUpdate;
/*     */       
/* 237 */       dos.writeLong(entity.getEntityId());
/* 238 */       dos.writeInt(entity.getType());
/* 239 */       dos.writeInt(entity.getHexId());
/* 240 */       dos.writeInt(entity.getTargetHexId());
/* 241 */       dos.writeUTF(entity.getName());
/*     */       
/* 243 */       dos.writeLong(entity.getTimeRemaining());
/*     */       
/* 245 */       if (!entity.isCollectable() && !entity.isSourceItem())
/*     */       {
/*     */ 
/*     */         
/* 249 */         dos.writeFloat(entity.getBodyStr());
/* 250 */         dos.writeFloat(entity.getBodySta());
/* 251 */         dos.writeFloat(entity.getBodyCon());
/* 252 */         dos.writeFloat(entity.getMindLog());
/* 253 */         dos.writeFloat(entity.getMindSpe());
/* 254 */         dos.writeFloat(entity.getSoulStr());
/* 255 */         dos.writeFloat(entity.getSoulDep());
/*     */         
/* 257 */         List<CollectedValreiItem> list = entity.getCarried();
/* 258 */         int collected = list.size();
/* 259 */         dos.writeInt(collected);
/* 260 */         for (int i = 0; i < collected; i++)
/*     */         {
/* 262 */           dos.writeUTF(((CollectedValreiItem)list.get(i)).getName());
/* 263 */           dos.writeInt(((CollectedValreiItem)list.get(i)).getType());
/* 264 */           dos.writeLong(((CollectedValreiItem)list.get(i)).getId());
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 270 */     catch (Exception ex) {
/*     */       
/* 272 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 276 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 277 */       byteData = bos.toByteArray();
/* 278 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 279 */       setData(byteData);
/*     */     } 
/*     */     
/* 282 */     return byteData;
/*     */   }
/*     */ 
/*     */   
/*     */   private final byte[] createFightDetails() {
/* 287 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 288 */     DataOutputStream dos = null;
/* 289 */     byte[] byteData = null;
/* 290 */     ValreiFightHistory vf = ValreiFightHistoryManager.getInstance().getLatestFight();
/*     */ 
/*     */     
/*     */     try {
/* 294 */       dos = new DataOutputStream(bos);
/* 295 */       dos.writeByte(5);
/* 296 */       writeFightDetails(dos, vf);
/*     */     }
/* 298 */     catch (Exception ex) {
/*     */       
/* 300 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 304 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 305 */       byteData = bos.toByteArray();
/* 306 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 307 */       setData(byteData);
/*     */     } 
/*     */     
/* 310 */     return byteData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] createInitialRequestMessage() {
/* 316 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 317 */     DataOutputStream dos = null;
/* 318 */     byte[] byteData = null;
/*     */ 
/*     */     
/*     */     try {
/* 322 */       dos = new DataOutputStream(bos);
/* 323 */       dos.writeByte(0);
/*     */     }
/* 325 */     catch (Exception ex) {
/*     */       
/* 327 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 331 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 332 */       byteData = bos.toByteArray();
/* 333 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 334 */       setData(byteData);
/*     */     } 
/*     */     
/* 337 */     return byteData;
/*     */   }
/*     */ 
/*     */   
/*     */   private final byte[] createInitialResponceMessage() {
/* 342 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 343 */     DataOutputStream dos = null;
/* 344 */     byte[] byteData = null;
/*     */ 
/*     */     
/*     */     try {
/* 348 */       dos = new DataOutputStream(bos);
/* 349 */       dos.writeByte(1);
/* 350 */       dos.writeInt(this.dataList.size());
/* 351 */       for (EpicEntity entity : this.dataList) {
/*     */         
/* 353 */         dos.writeLong(entity.getId());
/* 354 */         dos.writeInt(entity.getType());
/* 355 */         dos.writeInt((entity.getMapHex() == null) ? -1 : entity.getMapHex().getId());
/* 356 */         dos.writeInt(entity.getTargetHex());
/* 357 */         dos.writeUTF(entity.getName());
/*     */         
/* 359 */         long now = System.currentTimeMillis();
/* 360 */         long remaining = entity.getTimeUntilLeave() - now;
/* 361 */         dos.writeLong(remaining);
/*     */         
/* 363 */         if (!entity.isCollectable() && !entity.isSource()) {
/*     */ 
/*     */ 
/*     */           
/* 367 */           dos.writeFloat(entity.getCurrentSkill(102));
/* 368 */           dos.writeFloat(entity.getCurrentSkill(103));
/* 369 */           dos.writeFloat(entity.getCurrentSkill(104));
/* 370 */           dos.writeFloat(entity.getCurrentSkill(100));
/* 371 */           dos.writeFloat(entity.getCurrentSkill(101));
/* 372 */           dos.writeFloat(entity.getCurrentSkill(105));
/* 373 */           dos.writeFloat(entity.getCurrentSkill(106));
/*     */           
/* 375 */           List<EpicEntity> list = entity.getAllCollectedItems();
/* 376 */           int collected = list.size();
/* 377 */           dos.writeInt(collected);
/* 378 */           for (int i = 0; i < collected; i++) {
/*     */             
/* 380 */             dos.writeUTF(((EpicEntity)list.get(i)).getName());
/* 381 */             dos.writeInt(((EpicEntity)list.get(i)).getType());
/* 382 */             dos.writeLong(((EpicEntity)list.get(i)).getId());
/*     */           } 
/*     */         } 
/*     */       } 
/* 386 */       ArrayList<ValreiFightHistory> allFights = ValreiFightHistoryManager.getInstance().getAllFights();
/* 387 */       dos.writeInt(allFights.size());
/* 388 */       for (ValreiFightHistory vf : allFights)
/*     */       {
/* 390 */         writeFightDetails(dos, vf);
/*     */       }
/*     */     }
/* 393 */     catch (Exception ex) {
/*     */       
/* 395 */       logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 399 */       StreamUtilities.closeOutputStreamIgnoreExceptions(dos);
/* 400 */       byteData = bos.toByteArray();
/* 401 */       StreamUtilities.closeOutputStreamIgnoreExceptions(bos);
/* 402 */       setData(byteData);
/*     */     } 
/*     */     
/* 405 */     return byteData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeFightDetails(DataOutputStream dos, ValreiFightHistory vf) throws IOException {
/* 410 */     dos.writeLong(vf.getFightId());
/* 411 */     dos.writeInt(vf.getMapHexId());
/* 412 */     dos.writeUTF(vf.getMapHexName());
/* 413 */     dos.writeLong(vf.getFightTime());
/* 414 */     dos.writeInt(vf.getFighters().size());
/* 415 */     for (ValreiFightHistory.ValreiFighter v : vf.getFighters().values()) {
/*     */       
/* 417 */       dos.writeLong(v.getFighterId());
/* 418 */       dos.writeUTF(v.getName());
/*     */     } 
/* 420 */     dos.writeInt(vf.getTotalActions());
/* 421 */     for (int i = 0; i <= vf.getTotalActions(); i++) {
/*     */       
/* 423 */       ValreiConstants.ValreiFightAction act = vf.getFightAction(i);
/* 424 */       dos.writeInt(act.getActionNum());
/* 425 */       dos.writeShort(act.getActionId());
/* 426 */       dos.writeInt((act.getActionData()).length);
/* 427 */       dos.write(act.getActionData());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/* 434 */     DataInputStream dis = null;
/* 435 */     byte type = -1;
/*     */     
/*     */     try {
/* 438 */       dis = new DataInputStream(new ByteArrayInputStream(getData()));
/*     */       
/* 440 */       type = dis.readByte();
/*     */       
/* 442 */       if (type == 2)
/*     */       {
/* 444 */         readUpdateRequest(dis);
/*     */       }
/* 446 */       else if (type == 0)
/*     */       {
/* 448 */         handleInitialRequest();
/*     */       }
/* 450 */       else if (type == 1)
/*     */       {
/* 452 */         readFullRequestResponce(dis);
/*     */       }
/* 454 */       else if (type == 3)
/*     */       {
/* 456 */         handleTimeUpdateRequest();
/*     */       }
/* 458 */       else if (type == 4)
/*     */       {
/* 460 */         readTimeUpdateMessage(dis);
/*     */       }
/* 462 */       else if (type == 5)
/*     */       {
/* 464 */         readFightDetails(dis);
/*     */       }
/*     */     
/*     */     }
/* 468 */     catch (IOException ex) {
/*     */       
/* 470 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage() + " messageType " + type, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 474 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readTimeUpdateMessage(DataInputStream dis) throws IOException {
/* 480 */     int count = dis.readInt();
/* 481 */     for (int i = 0; i < count; i++) {
/*     */       
/* 483 */       long id = dis.readLong();
/* 484 */       long remaining = dis.readLong();
/*     */       
/* 486 */       ValreiMapData.updateEntityTime(id, remaining);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleTimeUpdateRequest() {
/* 492 */     WCValreiMapUpdater updater = new WCValreiMapUpdater(WurmId.getNextWCCommandId(), (byte)4);
/* 493 */     updater.sendFromLoginServer();
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleInitialRequest() {
/* 498 */     WCValreiMapUpdater updater = new WCValreiMapUpdater(WurmId.getNextWCCommandId(), (byte)1);
/* 499 */     updater.sendFromLoginServer();
/*     */   }
/*     */ 
/*     */   
/*     */   public void testDataEncoding() {
/* 504 */     collectData();
/* 505 */     byte[] responce = createInitialResponceMessage();
/* 506 */     DataInputStream dis = null;
/*     */     
/*     */     try {
/* 509 */       dis = new DataInputStream(new ByteArrayInputStream(responce));
/* 510 */       byte type = dis.readByte();
/* 511 */       if (type == 1)
/*     */       {
/* 513 */         readFullRequestResponce(dis);
/*     */       }
/*     */     }
/* 516 */     catch (IOException ex) {
/*     */       
/* 518 */       logger.log(Level.WARNING, "Unpack exception " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 522 */       StreamUtilities.closeInputStreamIgnoreExceptions(dis);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readUpdateRequest(DataInputStream dis) throws IOException {
/* 530 */     long id = dis.readLong();
/* 531 */     int type = dis.readInt();
/* 532 */     int hexId = dis.readInt();
/* 533 */     int targetHex = dis.readInt();
/* 534 */     String name = dis.readUTF();
/* 535 */     long remainingTime = dis.readLong();
/* 536 */     float attack = 0.0F;
/* 537 */     float vitality = 0.0F;
/* 538 */     float bodyStr = 0.0F;
/* 539 */     float bodySta = 0.0F;
/* 540 */     float bodyCon = 0.0F;
/* 541 */     float mindLog = 0.0F;
/* 542 */     float mindSpe = 0.0F;
/* 543 */     float soulStr = 0.0F;
/* 544 */     float soulDep = 0.0F;
/*     */     
/* 546 */     List<CollectedValreiItem> list = new ArrayList<>();
/* 547 */     if (type != 2 && type != 1) {
/*     */ 
/*     */ 
/*     */       
/* 551 */       bodyStr = dis.readFloat();
/* 552 */       bodySta = dis.readFloat();
/* 553 */       bodyCon = dis.readFloat();
/* 554 */       mindLog = dis.readFloat();
/* 555 */       mindSpe = dis.readFloat();
/* 556 */       soulStr = dis.readFloat();
/* 557 */       soulDep = dis.readFloat();
/*     */       
/* 559 */       int count = dis.readInt();
/* 560 */       for (int j = 0; j < count; j++) {
/*     */         
/* 562 */         String carriedName = dis.readUTF();
/* 563 */         int collType = dis.readInt();
/* 564 */         long collId = dis.readLong();
/* 565 */         list.add(new CollectedValreiItem(collId, carriedName, collType));
/*     */       } 
/*     */     } 
/* 568 */     ValreiMapData.updateEntity(id, hexId, type, targetHex, name, remainingTime, bodyStr, bodySta, bodyCon, mindLog, mindSpe, soulStr, soulDep, list);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readFullRequestResponce(DataInputStream dis) throws IOException {
/* 574 */     int size = dis.readInt();
/* 575 */     for (int i = 0; i < size; i++) {
/*     */       
/* 577 */       long id = dis.readLong();
/* 578 */       int type = dis.readInt();
/* 579 */       int hexId = dis.readInt();
/* 580 */       int targetHex = dis.readInt();
/* 581 */       String name = dis.readUTF();
/* 582 */       long remainingTime = dis.readLong();
/* 583 */       float attack = 0.0F;
/* 584 */       float vitality = 0.0F;
/* 585 */       float bodyStr = 0.0F;
/* 586 */       float bodySta = 0.0F;
/* 587 */       float bodyCon = 0.0F;
/* 588 */       float mindLog = 0.0F;
/* 589 */       float mindSpe = 0.0F;
/* 590 */       float soulStr = 0.0F;
/* 591 */       float soulDep = 0.0F;
/* 592 */       List<CollectedValreiItem> list = new ArrayList<>();
/* 593 */       if (type != 2 && type != 1) {
/*     */ 
/*     */ 
/*     */         
/* 597 */         bodyStr = dis.readFloat();
/* 598 */         bodySta = dis.readFloat();
/* 599 */         bodyCon = dis.readFloat();
/* 600 */         mindLog = dis.readFloat();
/* 601 */         mindSpe = dis.readFloat();
/* 602 */         soulStr = dis.readFloat();
/* 603 */         soulDep = dis.readFloat();
/*     */         
/* 605 */         int count = dis.readInt();
/* 606 */         for (int k = 0; k < count; k++) {
/*     */           
/* 608 */           String carriedName = dis.readUTF();
/* 609 */           int carriedType = dis.readInt();
/* 610 */           long collId = dis.readLong();
/* 611 */           list.add(new CollectedValreiItem(collId, carriedName, carriedType));
/*     */         } 
/*     */       } 
/* 614 */       ValreiMapData.updateEntity(id, hexId, type, targetHex, name, remainingTime, bodyStr, bodySta, bodyCon, mindLog, mindSpe, soulStr, soulDep, list);
/*     */     } 
/*     */     
/* 617 */     int fightsSize = dis.readInt();
/* 618 */     for (int j = 0; j < fightsSize; j++)
/*     */     {
/* 620 */       readFightDetails(dis);
/*     */     }
/*     */     
/* 623 */     ValreiMapData.setHasInitialData();
/*     */   }
/*     */ 
/*     */   
/*     */   private void readFightDetails(DataInputStream dis) throws IOException {
/* 628 */     long fightId = dis.readLong();
/* 629 */     int mapHexId = dis.readInt();
/* 630 */     String mapHexName = dis.readUTF();
/* 631 */     long fightTime = dis.readLong();
/*     */     
/* 633 */     ValreiFightHistory vf = new ValreiFightHistory(fightId, mapHexId, mapHexName, fightTime);
/*     */     
/* 635 */     int fightersSize = dis.readInt();
/* 636 */     for (int j = 0; j < fightersSize; j++) {
/*     */       
/* 638 */       long fighterId = dis.readLong();
/* 639 */       String fighterName = dis.readUTF();
/* 640 */       vf.addFighter(fighterId, fighterName);
/*     */     } 
/*     */     
/* 643 */     int actionsSize = dis.readInt();
/* 644 */     for (int i = 0; i <= actionsSize; i++) {
/*     */       
/* 646 */       int actionNum = dis.readInt();
/* 647 */       short actionId = dis.readShort();
/* 648 */       int dataLen = dis.readInt();
/* 649 */       byte[] actionData = new byte[dataLen];
/* 650 */       dis.read(actionData);
/*     */       
/* 652 */       vf.addAction(actionId, actionData);
/*     */     } 
/*     */     
/* 655 */     vf.setFightCompleted(true);
/* 656 */     ValreiFightHistoryManager.getInstance().addFight(fightId, vf, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\webinterface\WCValreiMapUpdater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */