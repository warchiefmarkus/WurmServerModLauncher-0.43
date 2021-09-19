/*     */ package com.wurmonline.server.bodys;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.FailedException;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.combat.CombatConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemFactory;
/*     */ import com.wurmonline.server.items.NoSpaceException;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ public final class Body
/*     */   implements CounterTypes, CombatConstants, MiscConstants
/*     */ {
/*     */   private Wounds wounds;
/*     */   private final Creature owner;
/*     */   private short centimetersHigh;
/*     */   private short centimetersLong;
/*     */   private short centimetersWide;
/*     */   private final BodyTemplate template;
/*  70 */   private static final Logger logger = Logger.getLogger(Body.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String GET_WOUNDS = "SELECT * FROM WOUNDS WHERE OWNER=?";
/*     */ 
/*     */ 
/*     */   
/*     */   private final Item[] spaces;
/*     */ 
/*     */   
/*     */   private boolean initialized = false;
/*     */ 
/*     */ 
/*     */   
/*     */   Body(BodyTemplate aTemplate, Creature aOwner, short aCentimetersHigh, short aCentimetersLong, short aCentimetersWide) {
/*  86 */     this.template = aTemplate;
/*  87 */     this.owner = aOwner;
/*  88 */     this.centimetersHigh = aCentimetersHigh;
/*  89 */     this.centimetersLong = aCentimetersLong;
/*  90 */     this.centimetersWide = aCentimetersWide;
/*  91 */     this.spaces = new Item[48];
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getType() {
/*  96 */     return this.template.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void loadWounds() {
/* 101 */     if (this.owner instanceof com.wurmonline.server.players.Player) {
/*     */       
/* 103 */       Connection dbcon = null;
/* 104 */       PreparedStatement ps = null;
/* 105 */       ResultSet rs = null;
/*     */       
/*     */       try {
/* 108 */         dbcon = DbConnector.getPlayerDbCon();
/* 109 */         ps = dbcon.prepareStatement("SELECT * FROM WOUNDS WHERE OWNER=?");
/* 110 */         ps.setLong(1, this.owner.getWurmId());
/* 111 */         rs = ps.executeQuery();
/* 112 */         while (rs.next())
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 117 */           DbWound wound = new DbWound(rs.getLong("ID"), rs.getByte("TYPE"), rs.getByte("LOCATION"), rs.getFloat("SEVERITY"), this.owner.getWurmId(), rs.getFloat("POISONSEVERITY"), rs.getFloat("INFECTIONSEVERITY"), rs.getLong("LASTPOLLED"), rs.getBoolean("BANDAGED"), rs.getByte("HEALEFF"));
/* 118 */           addWound(wound);
/*     */         }
/*     */       
/* 121 */       } catch (SQLException sqx) {
/*     */         
/* 123 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*     */       }
/*     */       finally {
/*     */         
/* 127 */         DbUtilities.closeDatabaseObjects(ps, rs);
/* 128 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendWounds() {
/* 135 */     if (this.owner instanceof com.wurmonline.server.players.Player)
/*     */     {
/* 137 */       if (this.wounds != null) {
/*     */         
/* 139 */         Wound[] w = this.wounds.getWounds();
/* 140 */         for (int x = 0; x < w.length; x++) {
/*     */ 
/*     */           
/*     */           try {
/* 144 */             this.owner.getCommunicator().sendAddWound(w[x], getBodyPartForWound(w[x]));
/*     */           }
/* 146 */           catch (NoSpaceException nsp) {
/*     */             
/* 148 */             logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*     */           } 
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
/*     */ 
/*     */   
/*     */   public Wounds getWounds() {
/* 164 */     return this.wounds;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addWound(Wound wound) {
/* 169 */     if (this.owner.isInvulnerable() && !this.owner.isPlayer()) {
/*     */       
/* 171 */       if (wound.getType() != 7)
/* 172 */         logger.log(Level.INFO, "Invulnerable " + this.owner.getName() + " receiving wound. Ignoring.", new Exception()); 
/* 173 */       wound.delete();
/* 174 */       return false;
/*     */     } 
/* 176 */     if (this.wounds == null)
/* 177 */       this.wounds = new Wounds(); 
/* 178 */     this.wounds.addWound(wound);
/* 179 */     this.owner.setWounded();
/* 180 */     return this.owner.getStatus().modifyWounds((int)wound.getSeverity());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWounded() {
/* 185 */     if (this.wounds == null) {
/* 186 */       return false;
/*     */     }
/* 188 */     return this.wounds.hasWounds();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeWound(Wound wound) {
/* 193 */     if (this.wounds != null) {
/* 194 */       this.wounds.remove(wound);
/*     */     }
/*     */   }
/*     */   
/*     */   public long getId() {
/* 199 */     if (this.spaces[0] != null)
/* 200 */       return this.spaces[0].getWurmId(); 
/* 201 */     logger.log(Level.INFO, "This should be looked into:", new Exception());
/* 202 */     return -10L;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getOwnerId() {
/* 207 */     if (this.owner == null)
/* 208 */       return -10L; 
/* 209 */     return this.owner.getWurmId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWoundLocationString(int location) {
/* 214 */     return this.template.typeString[location];
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBodyPartForWound(Wound wound) throws NoSpaceException {
/* 219 */     int pos = wound.getLocation();
/* 220 */     if (this.spaces == null || pos < 0)
/* 221 */       throw new NoSpaceException(String.valueOf(pos)); 
/* 222 */     Item toReturn = null;
/* 223 */     if (pos == 1 || pos == 17) {
/* 224 */       toReturn = this.spaces[1];
/* 225 */     } else if (pos == 29 || pos == 18 || pos == 19 || pos == 20) {
/*     */ 
/*     */       
/* 228 */       toReturn = this.spaces[29];
/*     */     }
/* 230 */     else if (pos == 36) {
/*     */       
/* 232 */       toReturn = this.spaces[1];
/*     */     }
/* 234 */     else if (pos == 2 || pos == 21 || pos == 27 || pos == 26 || pos == 32 || pos == 23 || pos == 24 || pos == 25 || pos == 22 || pos == 46 || pos == 47 || pos == 45 || pos == 42 || pos == 35 || pos == 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 240 */       toReturn = this.spaces[2];
/*     */     }
/* 242 */     else if (pos == 3 || pos == 5 || pos == 9 || pos == 44) {
/*     */ 
/*     */       
/* 245 */       toReturn = this.spaces[3];
/*     */     }
/* 247 */     else if (pos == 4 || pos == 6 || pos == 10) {
/*     */       
/* 249 */       toReturn = this.spaces[4];
/*     */     }
/* 251 */     else if (pos == 30 || pos == 7 || pos == 11 || pos == 31 || pos == 8 || pos == 12 || pos == 43 || pos == 41) {
/*     */ 
/*     */ 
/*     */       
/* 255 */       toReturn = this.spaces[34];
/*     */     }
/* 257 */     else if (pos == 37 || pos == 39) {
/*     */       
/* 259 */       toReturn = this.spaces[13];
/*     */     }
/* 261 */     else if (pos == 38 || pos == 40) {
/*     */       
/* 263 */       toReturn = this.spaces[14];
/*     */     }
/* 265 */     else if (pos < this.spaces.length) {
/* 266 */       toReturn = this.spaces[pos];
/*     */     } 
/* 268 */     if (toReturn == null)
/* 269 */       throw new NoSpaceException("No space for " + getWoundLocationString(pos)); 
/* 270 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void healFully() {
/* 275 */     if (this.wounds != null) {
/*     */       
/* 277 */       Wound[] w = this.wounds.getWounds();
/* 278 */       for (int x = 0; x < w.length; x++)
/*     */       {
/* 280 */         this.wounds.remove(w[x]);
/*     */       }
/*     */     } 
/* 283 */     this.owner.setDisease((byte)0);
/* 284 */     this.owner.getStatus().removeWounds();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCentimetersLong() {
/* 293 */     return this.centimetersLong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCentimetersHigh() {
/* 302 */     return this.centimetersHigh;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getCentimetersWide() {
/* 311 */     return this.centimetersWide;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWeight(byte weightLevel) {
/* 316 */     float modifier = 1.0F;
/* 317 */     if (weightLevel >= 50) {
/*     */       
/* 319 */       modifier = 1.0F + (weightLevel - 50) / 100.0F;
/*     */     }
/*     */     else {
/*     */       
/* 323 */       modifier = 0.5F * (1.0F + Math.max(1, weightLevel) / 50.0F);
/*     */     } 
/* 325 */     return (this.centimetersHigh * this.centimetersLong * this.centimetersWide) / 1.4F * modifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCentimetersLong(short aCentimetersLong) {
/* 334 */     this.centimetersLong = aCentimetersLong;
/* 335 */     this.owner.calculateSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCentimetersHigh(short aCentimetersHigh) {
/* 344 */     this.centimetersHigh = aCentimetersHigh;
/* 345 */     this.owner.calculateSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCentimetersWide(short aCentimetersWide) {
/* 354 */     this.centimetersWide = aCentimetersWide;
/* 355 */     this.owner.calculateSize();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Item getBodyPart(int pos) throws NoSpaceException {
/* 361 */     if (this.spaces == null || pos < 0)
/* 362 */       throw new NoSpaceException(String.valueOf(pos)); 
/* 363 */     Item toReturn = null;
/* 364 */     if (pos == 1 || pos == 17) {
/* 365 */       toReturn = this.spaces[1];
/* 366 */     } else if (pos == 29 || pos == 18 || pos == 19 || pos == 20) {
/*     */ 
/*     */       
/* 369 */       toReturn = this.spaces[29];
/*     */     }
/* 371 */     else if (pos == 2 || pos == 21 || pos == 27 || pos == 26 || pos == 32 || pos == 23 || pos == 24 || pos == 25 || pos == 22) {
/*     */ 
/*     */ 
/*     */       
/* 375 */       toReturn = this.spaces[2];
/*     */     }
/* 377 */     else if (pos == 3 || pos == 5 || pos == 9) {
/*     */       
/* 379 */       toReturn = this.spaces[3];
/*     */     }
/* 381 */     else if (pos == 4 || pos == 6 || pos == 10) {
/*     */       
/* 383 */       toReturn = this.spaces[4];
/*     */     }
/* 385 */     else if (pos == 30 || pos == 7 || pos == 11 || pos == 31 || pos == 8 || pos == 12) {
/*     */ 
/*     */       
/* 388 */       toReturn = this.spaces[34];
/*     */     }
/* 390 */     else if (pos == 38 || pos == 40) {
/*     */       
/* 392 */       if (this.template.type == 0) {
/*     */         
/* 394 */         toReturn = this.spaces[pos];
/* 395 */         if (toReturn == null) {
/* 396 */           toReturn = this.spaces[14];
/*     */         }
/*     */       } else {
/* 399 */         toReturn = this.spaces[14];
/*     */       } 
/* 401 */     } else if (pos == 37 || pos == 39) {
/*     */       
/* 403 */       if (this.template.type == 0) {
/*     */         
/* 405 */         toReturn = this.spaces[pos];
/* 406 */         if (toReturn == null) {
/* 407 */           toReturn = this.spaces[13];
/*     */         }
/*     */       } else {
/* 410 */         toReturn = this.spaces[13];
/*     */       } 
/* 412 */     } else if (pos == 44) {
/*     */       
/* 414 */       if (this.template.type == 0) {
/*     */         
/* 416 */         toReturn = this.spaces[pos];
/* 417 */         if (toReturn == null) {
/* 418 */           toReturn = this.spaces[3];
/*     */         }
/*     */       } else {
/* 421 */         toReturn = this.spaces[3];
/*     */       } 
/* 423 */     } else if (pos == 43 || pos == 41) {
/*     */       
/* 425 */       if (this.template.type == 0) {
/*     */         
/* 427 */         toReturn = this.spaces[pos];
/* 428 */         if (toReturn == null) {
/* 429 */           toReturn = this.spaces[34];
/*     */         }
/*     */       } else {
/* 432 */         toReturn = this.spaces[34];
/*     */       } 
/* 434 */     } else if (pos == 36) {
/*     */       
/* 436 */       if (this.template.type == 0) {
/*     */         
/* 438 */         toReturn = this.spaces[pos];
/* 439 */         if (toReturn == null) {
/* 440 */           toReturn = this.spaces[1];
/*     */         }
/*     */       } else {
/* 443 */         toReturn = this.spaces[1];
/*     */       } 
/* 445 */     } else if (pos == 35 || pos == 42 || pos == 45 || pos == 46 || pos == 47) {
/*     */ 
/*     */       
/* 448 */       if (this.template.type == 0) {
/*     */         
/* 450 */         toReturn = this.spaces[pos];
/* 451 */         if (toReturn == null) {
/* 452 */           toReturn = this.spaces[2];
/*     */         }
/*     */       } else {
/* 455 */         toReturn = this.spaces[2];
/*     */       } 
/* 457 */     } else if (pos < this.spaces.length) {
/* 458 */       toReturn = this.spaces[pos];
/*     */     } 
/* 460 */     if (toReturn == null)
/* 461 */       throw new NoSpaceException("No space for " + getWoundLocationString(pos)); 
/* 462 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item[] getSpaces() {
/* 471 */     return this.spaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Item[] getAllItems() {
/* 481 */     Set<Item> items = new HashSet<>();
/* 482 */     for (int x = 0; x < this.spaces.length; x++) {
/*     */       
/* 484 */       if (this.spaces[x] != null) {
/*     */         
/* 486 */         Item[] itemarr = this.spaces[x].getAllItems(false);
/* 487 */         for (int y = 0; y < itemarr.length; y++) {
/*     */           
/* 489 */           if (!itemarr[y].isBodyPart())
/* 490 */             items.add(itemarr[y]); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 494 */     Item[] toReturn = new Item[items.size()];
/* 495 */     toReturn = items.<Item>toArray(toReturn);
/* 496 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item[] getContainersAndWornItems() {
/* 501 */     Set<Item> items = getContainersAndWornItems(getBodyItem());
/* 502 */     return items.<Item>toArray(new Item[items.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Item> getContainersAndWornItems(Item item) {
/* 507 */     Set<Item> items = item.getItems();
/* 508 */     Set<Item> newItems = new HashSet<>();
/* 509 */     for (Iterator<Item> it = items.iterator(); it.hasNext(); ) {
/*     */       
/* 511 */       Item next = it.next();
/* 512 */       if (next.isBodyPart()) {
/* 513 */         newItems.addAll(getContainersAndWornItems(next)); continue;
/*     */       } 
/* 515 */       newItems.add(next);
/*     */     } 
/* 517 */     return newItems;
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
/*     */   public byte getRandomWoundPos() throws Exception {
/* 543 */     return this.template.getRandomWoundPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getRandomWoundPos(byte attackerStance) throws Exception {
/* 548 */     if (attackerStance == 7)
/* 549 */       return this.template.getHighWoundPos(); 
/* 550 */     if (attackerStance == 10)
/* 551 */       return this.template.getLowWoundPos(); 
/* 552 */     if (attackerStance == 6)
/* 553 */       return this.template.getUpperLeftWoundPos(); 
/* 554 */     if (attackerStance == 1)
/* 555 */       return this.template.getUpperRightWoundPos(); 
/* 556 */     if (attackerStance == 5)
/* 557 */       return this.template.getMidLeftWoundPos(); 
/* 558 */     if (attackerStance == 2)
/* 559 */       return this.template.getMidRightWoundPos(); 
/* 560 */     if (attackerStance == 3)
/* 561 */       return this.template.getLowerRightWoundPos(); 
/* 562 */     if (attackerStance == 4)
/* 563 */       return this.template.getLowerLeftWoundPos(); 
/* 564 */     return this.template.getCenterWoundPos();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getCenterWoundPos() throws Exception {
/* 569 */     return this.template.getCenterWoundPos();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createBodyPart(byte bodyConstant, int tempalteId, String partName, byte constData) throws FailedException, NoSuchTemplateException {
/* 575 */     this.spaces[bodyConstant] = ItemFactory.createBodyPart(this, (short)bodyConstant, tempalteId, partName, 50.0F);
/* 576 */     this.spaces[bodyConstant].setAuxData(constData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void createBodyParts() throws FailedException, NoSuchTemplateException {
/* 581 */     if (this.initialized)
/*     */       return; 
/* 583 */     createBodyPart((byte)0, 16, this.template.bodyS, (byte)24);
/* 584 */     createBodyPart((byte)1, 12, this.template.headS, (byte)2);
/* 585 */     createBodyPart((byte)13, 14, this.template.leftHandS, (byte)7);
/*     */     
/* 587 */     createBodyPart((byte)14, 14, this.template.rightHandS, (byte)8);
/*     */     
/* 589 */     createBodyPart((byte)15, 15, this.template.leftFootS, (byte)9);
/*     */     
/* 591 */     createBodyPart((byte)16, 15, this.template.rightFootS, (byte)10);
/*     */     
/* 593 */     createBodyPart((byte)2, 13, this.template.torsoS, (byte)3);
/* 594 */     createBodyPart((byte)29, 17, this.template.faceS, (byte)25);
/* 595 */     createBodyPart((byte)3, 11, this.template.leftArmS, (byte)5);
/* 596 */     createBodyPart((byte)4, 11, this.template.rightArmS, (byte)6);
/*     */     
/* 598 */     createBodyPart((byte)34, 19, this.template.legsS, (byte)4);
/*     */     
/* 600 */     if (this.template.type == 4) {
/* 601 */       this.spaces[28] = ItemFactory.createBodyPart(this, (short)28, 12, this.template.secondHeadS, 50.0F);
/*     */     }
/* 603 */     if (this.template.type == 8) {
/*     */       
/* 605 */       this.spaces[31] = ItemFactory.createBodyPart(this, (short)31, 10, this.template.rightLegS, 50.0F);
/*     */       
/* 607 */       this.spaces[30] = ItemFactory.createBodyPart(this, (short)30, 10, this.template.leftLegS, 50.0F);
/*     */     } 
/*     */ 
/*     */     
/* 611 */     if (this.template.type == 0 && this.owner.isPlayer()) {
/*     */       
/* 613 */       this.spaces[40] = createEquipmentSlot((byte)40, "right ring", (byte)16);
/*     */       
/* 615 */       this.spaces[14].insertItem(this.spaces[40]);
/* 616 */       Item rHeld = createEquipmentSlot((byte)38, "right held item", (byte)1);
/*     */       
/* 618 */       rHeld.setDescription("main weapon");
/* 619 */       this.spaces[14].insertItem(rHeld);
/* 620 */       this.spaces[13].insertItem(createEquipmentSlot((byte)39, "left ring", (byte)17));
/*     */       
/* 622 */       Item lHeld = createEquipmentSlot((byte)37, "left held item", (byte)0);
/*     */       
/* 624 */       lHeld.setDescription("off-hand weapon");
/* 625 */       this.spaces[13].insertItem(lHeld);
/* 626 */       this.spaces[3].insertItem(createEquipmentSlot((byte)44, "shield slot", (byte)11));
/*     */       
/* 628 */       this.spaces[2].insertItem(createEquipmentSlot((byte)45, "cape", (byte)14));
/*     */       
/* 630 */       this.spaces[2].insertItem(createEquipmentSlot((byte)46, "left shoulder", (byte)18));
/*     */       
/* 632 */       this.spaces[2].insertItem(createEquipmentSlot((byte)47, "right shoulder", (byte)19));
/*     */       
/* 634 */       this.spaces[2].insertItem(createEquipmentSlot((byte)42, "back", (byte)20));
/*     */       
/* 636 */       this.spaces[1].insertItem(createEquipmentSlot((byte)36, "neck", (byte)21));
/*     */       
/* 638 */       this.spaces[34].insertItem(createEquipmentSlot((byte)43, "belt", (byte)22));
/*     */       
/* 640 */       this.spaces[34].insertItem(createEquipmentSlot((byte)41, "hip slot", (byte)23));
/*     */       
/* 642 */       this.spaces[2].insertItem(createEquipmentSlot((byte)35, "tabard", (byte)15));
/*     */     } 
/*     */     
/* 645 */     buildBody();
/* 646 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Item createEquipmentSlot(byte space, String name, byte slotConstant) throws FailedException, NoSuchTemplateException {
/* 652 */     Item item = ItemFactory.createBodyPart(this, (short)space, 823, name, 50.0F);
/* 653 */     item.setAuxData(slotConstant);
/* 654 */     item.setOwnerId(this.owner.getWurmId());
/* 655 */     this.spaces[space] = item;
/* 656 */     return item;
/*     */   }
/*     */ 
/*     */   
/*     */   private void buildBody() {
/* 661 */     this.template.buildBody(this.spaces, this.owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getBodyItem() {
/* 666 */     return this.spaces[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public void load() throws Exception {
/* 671 */     createBodyParts();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sleep(Creature sleeper, boolean epicServer) throws IOException {
/* 677 */     this.spaces[0].sleep(sleeper, epicServer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void poll() {
/* 682 */     if (this.wounds != null)
/* 683 */       this.wounds.poll(this.owner); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\Body.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */