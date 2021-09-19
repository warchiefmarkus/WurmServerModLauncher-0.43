/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.Items;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchItemException;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.Creatures;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.RuneUtilities;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Achievements;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.questions.QuestionParser;
/*      */ import com.wurmonline.server.spells.Cooldowns;
/*      */ import com.wurmonline.server.spells.RiteEvent;
/*      */ import com.wurmonline.server.structures.Blocker;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BlockingResult;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import com.wurmonline.shared.constants.ProtoConstants;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Vehicle
/*      */   implements MiscConstants, ProtoConstants, TimeConstants
/*      */ {
/*   68 */   private static final Logger logger = Logger.getLogger(Vehicle.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   73 */   static final Seat[] EMPTYSEATS = new Seat[0];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   78 */   public Seat[] seats = EMPTYSEATS;
/*   79 */   public Seat[] hitched = EMPTYSEATS;
/*   80 */   private float maxSpeed = 1.0F;
/*      */   
/*   82 */   private byte windImpact = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean creature = false;
/*      */ 
/*      */   
/*   89 */   String pilotName = "driver";
/*   90 */   public long pilotId = -10L;
/*   91 */   String embarkString = "embark";
/*   92 */   String embarksString = "embarks";
/*      */   
/*   94 */   public String name = "vehicle";
/*      */   
/*      */   public final long wurmid;
/*      */   
/*   98 */   public float maxDepth = -2500.0F;
/*   99 */   public float maxHeight = 2500.0F;
/*  100 */   public float maxHeightDiff = 2000.0F;
/*  101 */   public float skillNeeded = 20.1F;
/*  102 */   private int maxAllowedLoadDistance = 4;
/*      */   private boolean unmountable = false;
/*  104 */   private byte maxPassengers = 0;
/*  105 */   public Set<Creature> draggers = null;
/*      */   
/*      */   private boolean chair = false;
/*      */   
/*      */   private boolean bed = false;
/*      */   
/*  111 */   public byte commandType = 0;
/*      */ 
/*      */   
/*      */   boolean canHaveEquipment = false;
/*      */ 
/*      */   
/*      */   private ServerEntry destinationServer;
/*      */   
/*      */   public static final long plotCoursePvPCooldown = 1800000L;
/*      */ 
/*      */   
/*      */   Vehicle(long aWurmId) {
/*  123 */     this.wurmid = aWurmId;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addDragger(Creature aCreature) {
/*  128 */     if (this.hitched.length > 0) {
/*      */       
/*  130 */       if (this.draggers == null)
/*  131 */         this.draggers = new HashSet<>(); 
/*  132 */       if (this.draggers.size() < this.hitched.length) {
/*      */ 
/*      */ 
/*      */         
/*  136 */         if (this.draggers.add(aCreature)) {
/*      */           
/*  138 */           for (int x = 0; x < this.hitched.length; x++) {
/*      */             
/*  140 */             if ((this.hitched[x]).occupant == -10L) {
/*      */ 
/*      */               
/*  143 */               this.hitched[x].setOccupant(aCreature.getWurmId());
/*      */               
/*  145 */               if (getPilotId() > -10L) {
/*      */                 
/*      */                 try {
/*  148 */                   Creature c = Server.getInstance().getCreature(getPilotId());
/*  149 */                   c.getMovementScheme().addMountSpeed((short)calculateNewVehicleSpeed(true));
/*      */                 }
/*  151 */                 catch (Exception exception) {}
/*      */               }
/*      */ 
/*      */               
/*  155 */               return true;
/*      */             } 
/*      */           } 
/*      */           
/*  159 */           logger.log(Level.WARNING, "error when adding to hitched seat - no free space.");
/*  160 */           this.draggers.remove(aCreature);
/*      */         } 
/*      */       } else {
/*      */         
/*  164 */         logger.log(Level.WARNING, "draggers.size=" + this.draggers.size() + ", hitched.length=" + this.hitched.length + " - no space");
/*      */       } 
/*      */     } 
/*  167 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void purgeDraggers() {
/*  172 */     if (this.draggers != null) {
/*      */       
/*  174 */       for (Creature dragger : this.draggers) {
/*      */         
/*  176 */         for (int x = 0; x < this.hitched.length; x++) {
/*      */           
/*  178 */           if ((this.hitched[x]).occupant == dragger.getWurmId()) {
/*      */             
/*  180 */             this.hitched[x].setOccupant(-10L);
/*      */             break;
/*      */           } 
/*      */         } 
/*  184 */         dragger.setHitched(null, false);
/*  185 */         Server.getInstance().broadCastMessage(dragger.getName() + " stops dragging a " + getName() + ".", dragger
/*  186 */             .getTileX(), dragger.getTileY(), dragger.isOnSurface(), 5);
/*      */       } 
/*  188 */       if (getPilotId() > -10L) {
/*      */         
/*      */         try {
/*  191 */           Creature c = Server.getInstance().getCreature(getPilotId());
/*  192 */           c.getMovementScheme().addMountSpeed((short)calculateNewVehicleSpeed(true));
/*      */         }
/*  194 */         catch (Exception exception) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeDragger(Creature aCreature) {
/*  203 */     if (this.hitched.length > 0)
/*      */     {
/*  205 */       if (this.draggers != null)
/*      */       {
/*  207 */         if (this.draggers.remove(aCreature)) {
/*      */           
/*  209 */           for (int x = 0; x < this.hitched.length; x++) {
/*      */             
/*  211 */             if ((this.hitched[x]).occupant == aCreature.getWurmId()) {
/*      */               
/*  213 */               this.hitched[x].setOccupant(-10L);
/*      */               break;
/*      */             } 
/*      */           } 
/*  217 */           aCreature.setHitched(null, false);
/*  218 */           String hitchedType = "stop dragging";
/*  219 */           if (!this.creature) {
/*      */             
/*      */             try {
/*      */               
/*  223 */               Item dragged = Items.getItem(getWurmid());
/*  224 */               if (dragged.isTent())
/*      */               {
/*  226 */                 hitchedType = "is no longer hitched to";
/*      */               }
/*      */             }
/*  229 */             catch (NoSuchItemException noSuchItemException) {}
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  234 */           Server.getInstance().broadCastMessage(aCreature
/*  235 */               .getName() + " " + hitchedType + " a " + getName() + ".", aCreature
/*  236 */               .getTileX(), aCreature.getTileY(), aCreature.isOnSurface(), 5);
/*      */           
/*  238 */           if (getPilotId() > -10L) {
/*      */             
/*      */             try {
/*  241 */               Creature c = Server.getInstance().getCreature(getPilotId());
/*  242 */               c.getMovementScheme().addMountSpeed((short)calculateNewVehicleSpeed(true));
/*      */             }
/*  244 */             catch (Exception exception) {}
/*      */           }
/*      */ 
/*      */           
/*  248 */           return true;
/*      */         } 
/*      */       }
/*      */     }
/*  252 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDraggedSpeed(boolean hitching) {
/*  257 */     if (this.hitched.length > 0)
/*      */     {
/*  259 */       if (this.draggers != null)
/*      */       {
/*  261 */         if (getPilotId() > -10L) {
/*      */           
/*      */           try {
/*      */             
/*  265 */             Creature c = Server.getInstance().getCreature(getPilotId());
/*  266 */             c.getMovementScheme().addMountSpeed((short)calculateNewVehicleSpeed(hitching));
/*      */           }
/*  268 */           catch (Exception exception) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Seat[] getHitched() {
/*  284 */     return this.hitched;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHitched(Seat[] aHitched) {
/*  295 */     this.hitched = aHitched;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxDepth() {
/*  305 */     return this.maxDepth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxDepth(float aMaxDepth) {
/*  316 */     this.maxDepth = aMaxDepth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxHeight() {
/*  326 */     return this.maxHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxHeight(float aMaxHeight) {
/*  337 */     this.maxHeight = aMaxHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getMaxHeightDiff() {
/*  347 */     return this.maxHeightDiff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxHeightDiff(float aMaxHeightDiff) {
/*  358 */     this.maxHeightDiff = aMaxHeightDiff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getSkillNeeded() {
/*  368 */     return this.skillNeeded;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getCanHaveEquipment() {
/*  373 */     return this.canHaveEquipment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillNeeded(float aSkillNeeded) {
/*  384 */     this.skillNeeded = aSkillNeeded;
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Creature> getDraggers() {
/*  389 */     return this.draggers;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDragger(Creature aCreature) {
/*  394 */     if (this.hitched.length > 0)
/*      */     {
/*  396 */       if (this.draggers != null)
/*  397 */         return this.draggers.contains(aCreature); 
/*      */     }
/*  399 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasHumanDragger() {
/*  404 */     if (this.draggers != null)
/*  405 */       for (Creature dragger : this.draggers) {
/*  406 */         if (dragger.isPlayer())
/*  407 */           return true; 
/*  408 */       }   return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mayAddDragger() {
/*  413 */     return (this.hitched.length > 0 && (this.draggers == null || this.draggers.size() < this.hitched.length));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addHitchSeats(Seat[] hitchSeats) {
/*  418 */     if (hitchSeats == null) {
/*  419 */       this.hitched = EMPTYSEATS;
/*      */     } else {
/*  421 */       this.hitched = hitchSeats;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void createPassengerSeats(int aNumber) {
/*  430 */     this.maxPassengers = (byte)aNumber;
/*  431 */     if (aNumber >= 0) {
/*      */       
/*  433 */       this.seats = new Seat[aNumber + 1];
/*  434 */       this.seats[0] = new Seat((byte)0);
/*  435 */       for (int x = 1; x <= aNumber; x++) {
/*  436 */         this.seats[x] = new Seat((byte)1);
/*      */       }
/*      */     } else {
/*      */       
/*  440 */       logger.warning("Can only create a positive number of seats not " + aNumber);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void createOnlyPassengerSeats(int aNumber) {
/*  450 */     if (aNumber >= 0) {
/*      */       
/*  452 */       this.seats = new Seat[aNumber];
/*  453 */       for (int x = 0; x < aNumber; x++) {
/*  454 */         this.seats[x] = new Seat((byte)1);
/*      */       }
/*      */     } else {
/*      */       
/*  458 */       logger.warning("Can only create a positive number of seats not " + aNumber);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getMaxPassengers() {
/*  464 */     return this.maxPassengers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setSeatOffset(int aNumber, float aOffx, float aOffy, float aOffz) {
/*  478 */     if (aNumber > this.seats.length - 1 || aNumber < 0)
/*      */     {
/*  480 */       return false;
/*      */     }
/*  482 */     (this.seats[aNumber]).offx = aOffx;
/*  483 */     (this.seats[aNumber]).offy = aOffy;
/*  484 */     (this.seats[aNumber]).offz = aOffz;
/*  485 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setSeatOffset(int aNumber, float aOffx, float aOffy, float aOffz, float aAltOffz) {
/*  500 */     if (aNumber > this.seats.length - 1 || aNumber < 0)
/*      */     {
/*  502 */       return false;
/*      */     }
/*  504 */     (this.seats[aNumber]).offx = aOffx;
/*  505 */     (this.seats[aNumber]).offy = aOffy;
/*  506 */     (this.seats[aNumber]).offz = aOffz;
/*  507 */     this.seats[aNumber].setAltOffz(aAltOffz);
/*  508 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setSeatFightMod(int aNumber, float aCover, float aManouvre) {
/*  519 */     if (aNumber > this.seats.length - 1 || aNumber < 0)
/*      */     {
/*  521 */       return false;
/*      */     }
/*  523 */     (this.seats[aNumber]).cover = aCover;
/*  524 */     (this.seats[aNumber]).manouvre = aManouvre;
/*  525 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Seat getPilotSeat() {
/*  534 */     if (this.seats.length != 0 && (this.seats[0]).type == 0)
/*  535 */       return this.seats[0]; 
/*  536 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Seat getSeatFor(long aCreatureId) {
/*  546 */     for (int x = 0; x < this.seats.length; x++) {
/*      */       
/*  548 */       if ((this.seats[x]).occupant == aCreatureId)
/*  549 */         return this.seats[x]; 
/*      */     } 
/*  551 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getSeatNumberFor(Seat seat) {
/*  556 */     for (int i = 0; i < this.seats.length; i++) {
/*      */       
/*  558 */       if (this.seats[i].getId() == seat.getId())
/*  559 */         return i; 
/*      */     } 
/*  561 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Seat getHitchSeatFor(long aCreatureId) {
/*  571 */     for (int x = 0; x < this.hitched.length; x++) {
/*      */       
/*  573 */       if ((this.hitched[x]).occupant == aCreatureId)
/*  574 */         return this.hitched[x]; 
/*      */     } 
/*  576 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void kickAll() {
/*  584 */     for (int x = 0; x < this.seats.length; x++)
/*  585 */       this.seats[x].leave(this); 
/*  586 */     this.pilotId = -10L;
/*  587 */     this.pilotName = "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Seat[] getSeats() {
/*  597 */     return this.seats;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setSeats(Seat[] aSeats) {
/*  608 */     this.seats = aSeats;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAnySeatOccupied() {
/*  613 */     return isAnySeatOccupied(true);
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAnySeatOccupied(boolean countOffline) {
/*  618 */     if (this.seats != null)
/*      */     {
/*  620 */       for (int i = 0; i < this.seats.length; i++) {
/*      */         
/*  622 */         if (this.seats[i].isOccupied())
/*      */         {
/*  624 */           if (!countOffline) {
/*      */ 
/*      */             
/*      */             try {
/*  628 */               long occupantId = this.seats[i].getOccupant();
/*  629 */               Player p = Players.getInstance().getPlayer(occupantId);
/*  630 */               if (p.isOffline());
/*      */             
/*      */             }
/*  633 */             catch (NoSuchPlayerException e) {}
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/*  639 */             return true;
/*      */           }  } 
/*      */       } 
/*      */     }
/*  643 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isAnythingHitched() {
/*  648 */     if (this.hitched != null)
/*      */     {
/*  650 */       for (int i = 0; i < this.hitched.length; i++) {
/*      */         
/*  652 */         if (this.hitched[i].isOccupied())
/*  653 */           return true; 
/*      */       } 
/*      */     }
/*  656 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte calculateNewBoatSpeed(boolean disembarking) {
/*  661 */     int numsOccupied = 0;
/*      */ 
/*      */     
/*  664 */     float qlMod = 0.0F;
/*  665 */     for (int x = 0; x < this.seats.length; x++) {
/*      */       
/*  667 */       if (this.seats[x].isOccupied()) {
/*      */         
/*  669 */         numsOccupied++;
/*      */ 
/*      */         
/*      */         try {
/*  673 */           long occupantId = this.seats[x].getOccupant();
/*  674 */           Player p = Players.getInstance().getPlayer(occupantId);
/*  675 */           if (p.isOffline()) {
/*  676 */             numsOccupied--;
/*      */           }
/*  678 */         } catch (NoSuchPlayerException e) {
/*      */           
/*  680 */           numsOccupied--;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/*  686 */       Item itemVehicle = Items.getItem(this.wurmid);
/*  687 */       numsOccupied = Math.min(this.seats.length, numsOccupied + itemVehicle.getRarity());
/*  688 */       qlMod = Math.max(0.0F, itemVehicle.getCurrentQualityLevel() - 10.0F) / 90.0F;
/*  689 */       if (qlMod > 0.0F) {
/*  690 */         qlMod++;
/*      */       }
/*  692 */     } catch (NoSuchItemException nsi) {
/*      */       
/*  694 */       return 0;
/*      */     } 
/*  696 */     if (disembarking) {
/*  697 */       numsOccupied--;
/*      */     }
/*  699 */     float percentOccupied = 1.0F;
/*      */     
/*  701 */     percentOccupied = 1.0F + numsOccupied / this.seats.length;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  707 */     float maxSpeed = getMaxSpeed();
/*      */ 
/*      */     
/*  710 */     if (RiteEvent.isActive(403))
/*      */     {
/*  712 */       maxSpeed *= 2.0F;
/*      */     }
/*      */     
/*  715 */     if (Servers.localServer.PVPSERVER) {
/*  716 */       return (byte)(int)Math.min(127.0F, percentOccupied * 9.0F * maxSpeed + qlMod * 3.0F * maxSpeed);
/*      */     }
/*  718 */     return (byte)(int)Math.min(127.0F, percentOccupied * 3.0F * maxSpeed + qlMod * 9.0F * maxSpeed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int getMinimumDraggers(Item vehicleItem) {
/*  729 */     if (vehicleItem == null)
/*  730 */       return 0; 
/*  731 */     if (vehicleItem.getTemplateId() == 850)
/*  732 */       return 2; 
/*  733 */     if (!vehicleItem.isBoat())
/*  734 */       return 1; 
/*  735 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte calculateNewVehicleSpeed(boolean hitching) {
/*  740 */     if (isChair()) {
/*  741 */       return 0;
/*      */     }
/*  743 */     if (this.hitched.length > 0) {
/*      */       
/*  745 */       boolean isWagon = false;
/*  746 */       int bisonCount = 0;
/*  747 */       if (this.draggers == null) {
/*  748 */         return 0;
/*      */       }
/*      */       
/*  751 */       double strength = 0.0D;
/*      */       
/*      */       try {
/*  754 */         Item itemVehicle = Items.getItem(this.wurmid);
/*  755 */         strength = (itemVehicle.getRarity() * 0.1F);
/*  756 */         if (getDraggers().size() < getMinimumDraggers(itemVehicle))
/*  757 */           return 0; 
/*  758 */         if (itemVehicle.getTemplateId() == 850) {
/*  759 */           isWagon = true;
/*      */         }
/*      */       }
/*  762 */       catch (NoSuchItemException nsi) {
/*      */         
/*  764 */         return 0;
/*      */       } 
/*  766 */       for (Creature next : this.draggers) {
/*      */         
/*  768 */         if (isWagon && next.getTemplate().getTemplateId() == 82)
/*  769 */           bisonCount++; 
/*  770 */         strength += next.getStrengthSkill() / (this.hitched.length * 10) * next.getMountSpeedPercent(hitching);
/*      */       } 
/*      */ 
/*      */       
/*  774 */       return (byte)(int)Math.min(127.0D, 10.0D * strength * getMaxSpeed() + ((1 * bisonCount) * getMaxSpeed()));
/*      */     } 
/*      */ 
/*      */     
/*  778 */     return (byte)(int)Math.min(127.0F, 10.0F * getMaxSpeed());
/*      */   }
/*      */ 
/*      */   
/*      */   public byte calculateNewMountSpeed(Creature mount, boolean mounting) {
/*  783 */     double strength = mount.getMountSpeedPercent(mounting);
/*      */ 
/*      */ 
/*      */     
/*  787 */     if (mount.getTemplateId() == 64 && strength * getMaxSpeed() >= 42.0D && getPilotId() != -10L) {
/*  788 */       Achievements.triggerAchievement(getPilotId(), 584);
/*      */     }
/*  790 */     return (byte)(int)Math.max(0.0D, Math.min(127.0D, strength * getMaxSpeed()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   float getMaxSpeed() {
/*      */     try {
/*  809 */       Item itemVehicle = Items.getItem(this.wurmid);
/*  810 */       if (itemVehicle != null && itemVehicle.getSpellEffects() != null)
/*      */       {
/*  812 */         float modifier = itemVehicle.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_VEHCSPEED);
/*  813 */         return this.maxSpeed * modifier;
/*      */       }
/*      */     
/*  816 */     } catch (NoSuchItemException nsi) {
/*      */       
/*  818 */       return this.maxSpeed;
/*      */     } 
/*  820 */     return this.maxSpeed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setMaxSpeed(float aMaxSpeed) {
/*  831 */     this.maxSpeed = aMaxSpeed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getWindImpact() {
/*  841 */     float modifier = 1.0F;
/*      */     
/*      */     try {
/*  844 */       Item itemVehicle = Items.getItem(this.wurmid);
/*  845 */       if (itemVehicle != null && itemVehicle.getSpellEffects() != null)
/*      */       {
/*  847 */         modifier = itemVehicle.getSpellEffects().getRuneEffect(RuneUtilities.ModifierEffect.ENCH_WIND);
/*      */       }
/*      */     }
/*  850 */     catch (NoSuchItemException noSuchItemException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  855 */     return (byte)(int)Math.min(127.0F, this.windImpact * modifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setWindImpact(byte impact) {
/*  866 */     this.windImpact = (byte)Math.min(127, impact);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCreature() {
/*  876 */     return this.creature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setCreature(boolean aCreature) {
/*  887 */     this.creature = aCreature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getPilotName() {
/*  897 */     return this.pilotName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setPilotName(String aPilotName) {
/*  908 */     this.pilotName = aPilotName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getPilotId() {
/*  918 */     return this.pilotId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setPilotId(long aPilotId) {
/*  929 */     this.pilotId = aPilotId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getEmbarkString() {
/*  939 */     return this.embarkString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setEmbarkString(String aEmbarkString) {
/*  950 */     this.embarkString = aEmbarkString;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getName() {
/*  960 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String getVehicleName(Vehicle vehicle) {
/*  973 */     if (vehicle.isCreature()) {
/*      */       
/*      */       try {
/*      */         
/*  977 */         Creature mount = Creatures.getInstance().getCreature(vehicle.getWurmid());
/*  978 */         return mount.getName();
/*      */       }
/*  980 */       catch (NoSuchCreatureException nsc) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  985 */         logger.log(Level.WARNING, 
/*  986 */             StringUtil.format("Unable to find creature with id: %d.", new Object[] {
/*  987 */                 Long.valueOf(vehicle.getWurmid())
/*      */               }), (Throwable)nsc);
/*      */       } 
/*      */     }
/*  991 */     if (vehicle.isChair())
/*      */       
/*      */       try {
/*      */         
/*  995 */         Item chair = Items.getItem(vehicle.getWurmid());
/*  996 */         return chair.getName();
/*      */       }
/*  998 */       catch (NoSuchItemException nsi) {
/*      */         
/* 1000 */         logger.log(Level.WARNING, StringUtil.format("Unable to find item with id: %d.", new Object[] {
/* 1001 */                 Long.valueOf(vehicle.getWurmid())
/*      */               }), (Throwable)nsi);
/*      */       }  
/* 1004 */     return vehicle.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setName(String aName) {
/* 1015 */     this.name = aName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   long getWurmid() {
/* 1025 */     return this.wurmid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1036 */     StringBuilder lBuilder = new StringBuilder(200);
/* 1037 */     lBuilder.append("Vehicle [id: ").append(this.wurmid);
/* 1038 */     lBuilder.append(", Name: ").append(this.name);
/* 1039 */     lBuilder.append(", PilotId: ").append(this.pilotId);
/* 1040 */     lBuilder.append(", PilotName: ").append(this.pilotName);
/* 1041 */     lBuilder.append(", MaxSpeed: ").append(getMaxSpeed());
/* 1042 */     lBuilder.append(", EmbarkString: ").append(this.embarkString);
/* 1043 */     lBuilder.append(", Creature: ").append(this.creature);
/* 1044 */     lBuilder.append(']');
/* 1045 */     return lBuilder.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSeatPosForPassenger(long _wurmid) {
/* 1067 */     for (int x = 0; x < this.seats.length; x++) {
/*      */       
/* 1069 */       if ((this.seats[x]).occupant == _wurmid)
/* 1070 */         return x; 
/*      */     } 
/* 1072 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFloorLevel() {
/* 1095 */     if (this.creature) {
/*      */       
/*      */       try {
/*      */         
/* 1099 */         return Server.getInstance().getCreature(this.wurmid).getFloorLevel();
/*      */       }
/* 1101 */       catch (Exception ex) {
/*      */         
/* 1103 */         return 0;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1109 */       return Items.getItem(this.wurmid).getFloorLevel();
/*      */     }
/* 1111 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1113 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final float getPosZ() {
/* 1119 */     if (this.creature) {
/*      */       
/*      */       try {
/*      */         
/* 1123 */         return Server.getInstance().getCreature(this.wurmid).getPositionZ();
/*      */       }
/* 1125 */       catch (Exception ex) {
/*      */         
/* 1127 */         return 0.0F;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/* 1133 */       return Items.getItem(this.wurmid).getPosZ();
/*      */     }
/* 1135 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1137 */       return 0.0F;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean positionDragger(Creature dragger, Creature performer) {
/* 1142 */     Item itemVehicle = null;
/*      */     
/*      */     try {
/* 1145 */       itemVehicle = Items.getItem(this.wurmid);
/*      */     
/*      */     }
/* 1148 */     catch (NoSuchItemException nsi) {
/*      */       
/* 1150 */       return false;
/*      */     } 
/* 1152 */     for (int x = 0; x < this.hitched.length; x++) {
/*      */       
/* 1154 */       if ((this.hitched[x]).type == 2 && this.hitched[x].getOccupant() == dragger.getWurmId()) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1159 */         float r = (-itemVehicle.getRotation() + 180.0F) * 3.1415927F / 180.0F;
/* 1160 */         float s = (float)Math.sin(r);
/* 1161 */         float c = (float)Math.cos(r);
/* 1162 */         Seat pilotSeat = getPilotSeat();
/* 1163 */         float xo2 = (pilotSeat == null) ? 0.0F : (s * -pilotSeat.offx - c * -pilotSeat.offy);
/* 1164 */         float yo2 = (pilotSeat == null) ? 0.0F : (c * -pilotSeat.offx + s * -pilotSeat.offy);
/* 1165 */         float origposx = itemVehicle.getPosX() + xo2;
/* 1166 */         float origposy = itemVehicle.getPosY() + yo2;
/* 1167 */         origposx = Math.max(3.0F, origposx);
/* 1168 */         origposx = Math.min(Zones.worldMeterSizeX - 3.0F, origposx);
/* 1169 */         origposy = Math.max(3.0F, origposy);
/* 1170 */         origposy = Math.min(Zones.worldMeterSizeY - 3.0F, origposy);
/*      */ 
/*      */ 
/*      */         
/* 1174 */         float xo = s * -(this.hitched[x]).offx - c * -(this.hitched[x]).offy;
/* 1175 */         float yo = c * -(this.hitched[x]).offx + s * -(this.hitched[x]).offy;
/* 1176 */         float newposx = itemVehicle.getPosX() + xo;
/* 1177 */         float newposy = itemVehicle.getPosY() + yo;
/* 1178 */         if (itemVehicle.isTent()) {
/*      */           
/* 1180 */           newposx = performer.getPosX();
/* 1181 */           newposy = performer.getPosY();
/*      */         } 
/*      */         
/* 1184 */         newposx = Math.max(3.0F, newposx);
/* 1185 */         newposx = Math.min(Zones.worldMeterSizeX - 3.0F, newposx);
/* 1186 */         newposy = Math.max(3.0F, newposy);
/* 1187 */         newposy = Math.min(Zones.worldMeterSizeY - 3.0F, newposy);
/* 1188 */         int diffx = ((int)newposx >> 2) - ((int)origposx >> 2);
/* 1189 */         int diffy = ((int)newposy >> 2) - ((int)origposy >> 2);
/*      */         
/* 1191 */         boolean move = true;
/* 1192 */         if (!itemVehicle.isTent())
/*      */         {
/* 1194 */           if (diffy != 0 || diffx != 0) {
/*      */             
/* 1196 */             BlockingResult result = Blocking.getBlockerBetween(dragger, origposx, origposy, newposx, newposy, dragger
/* 1197 */                 .getPositionZ(), dragger.getPositionZ(), (dragger.getLayer() >= 0), 
/* 1198 */                 (dragger.getLayer() >= 0), false, 6, -1L, itemVehicle.getBridgeId(), itemVehicle
/* 1199 */                 .getBridgeId(), false);
/*      */             
/* 1201 */             if (result != null) {
/*      */               
/* 1203 */               Blocker first = result.getFirstBlocker();
/* 1204 */               if (!first.isDoor() || !first.canBeOpenedBy(dragger, false))
/*      */               {
/* 1206 */                 move = false;
/*      */               }
/*      */             } 
/* 1209 */             if (move)
/*      */             {
/* 1211 */               if (dragger.getLayer() < 0)
/*      */               {
/* 1213 */                 if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh
/* 1214 */                       .getTile((int)newposx >> 2, (int)newposy >> 2))))
/*      */                 {
/*      */ 
/*      */ 
/*      */                   
/* 1219 */                   move = false;
/*      */                 }
/*      */               }
/*      */             }
/*      */           } 
/*      */         }
/* 1225 */         if (!move) {
/*      */           
/* 1227 */           newposx = origposx;
/* 1228 */           newposy = origposy;
/*      */         } 
/*      */         
/*      */         try {
/* 1232 */           Zones.getZone((dragger.getCurrentTile()).tilex, (dragger.getCurrentTile()).tiley, dragger.isOnSurface())
/* 1233 */             .removeCreature(dragger, true, false);
/*      */         }
/* 1235 */         catch (Exception ex) {
/*      */           
/* 1237 */           logger.log(Level.WARNING, dragger.getWurmId() + "," + ex.getMessage(), ex);
/*      */         } 
/* 1239 */         dragger.getStatus().setPositionX(newposx);
/* 1240 */         dragger.getStatus().setPositionY(newposy);
/* 1241 */         dragger.setBridgeId(itemVehicle.getBridgeId());
/* 1242 */         float z = Zones.calculatePosZ(newposx, newposy, Zones.getTileOrNull(dragger.getTilePos(), dragger.isOnSurface()), dragger
/* 1243 */             .isOnSurface(), false, dragger.getStatus().getPositionZ(), dragger, dragger.getBridgeId());
/*      */         
/* 1245 */         dragger.getMovementScheme().setPosition(dragger.getStatus().getPositionX(), dragger.getStatus().getPositionY(), z, dragger
/* 1246 */             .getStatus().getRotation(), dragger.getLayer());
/* 1247 */         dragger.destroyVisionArea();
/*      */         
/*      */         try {
/* 1250 */           Zones.getZone(dragger.getTileX(), dragger.getTileY(), dragger.isOnSurface()).addCreature(dragger
/* 1251 */               .getWurmId());
/* 1252 */           dragger.createVisionArea();
/*      */         }
/* 1254 */         catch (Exception ex) {
/*      */           
/* 1256 */           logger.log(Level.WARNING, dragger.getWurmId() + "," + ex.getMessage(), ex);
/*      */         } 
/* 1258 */         return true;
/*      */       } 
/*      */     } 
/* 1261 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUnmountable() {
/* 1271 */     return this.unmountable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUnmountable(boolean aUnmountable) {
/* 1282 */     this.unmountable = aUnmountable;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isChair() {
/* 1287 */     return this.chair;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setChair(boolean isChair) {
/* 1292 */     this.chair = isChair;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBed() {
/* 1297 */     return this.bed;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBed(boolean isBed) {
/* 1302 */     this.bed = isBed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxAllowedLoadDistance() {
/* 1310 */     return this.maxAllowedLoadDistance;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMaxAllowedLoadDistance(int newMaxDist) {
/* 1315 */     this.maxAllowedLoadDistance = newMaxDist;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ServerEntry getDestinationServer() {
/* 1324 */     return this.destinationServer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasDestinationSet() {
/* 1333 */     if (this.destinationServer != null)
/* 1334 */       return true; 
/* 1335 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestination(ServerEntry entry) {
/* 1344 */     this.destinationServer = entry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearDestination() {
/* 1353 */     this.destinationServer = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void alertPassengerOfEnemies(Creature performer, ServerEntry entry, boolean embarking) {
/* 1364 */     if ((entry.PVPSERVER && (!entry.EPIC || Server.getInstance().isPS())) || entry.isChaosServer()) {
/*      */ 
/*      */       
/* 1367 */       byte pKingdom = (((Player)performer).getSaveFile().getChaosKingdom() == 0) ? 4 : ((Player)performer).getSaveFile().getChaosKingdom();
/* 1368 */       for (Seat lSeat : this.seats) {
/*      */         
/* 1370 */         PlayerInfo oInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(lSeat.getOccupant());
/* 1371 */         if (oInfo != null) {
/*      */           
/* 1373 */           byte oKingdom = (oInfo.getChaosKingdom() == 0) ? 4 : oInfo.getChaosKingdom();
/* 1374 */           if (oKingdom != pKingdom) {
/*      */             
/* 1376 */             performer.getCommunicator().sendAlertServerMessage("Warning: " + oInfo.getName() + " will be an enemy when you cross into " + entry
/* 1377 */                 .getName() + "!");
/* 1378 */             if (embarking) {
/*      */               
/*      */               try {
/*      */                 
/* 1382 */                 Player oPlayer = Players.getInstance().getPlayer(oInfo.wurmId);
/* 1383 */                 oPlayer.getCommunicator().sendAlertServerMessage("Warning: " + performer.getName() + " will be an enemy when you cross into " + entry
/* 1384 */                     .getName() + "!");
/*      */               }
/* 1386 */               catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void alertAllPassengersOfEnemies(ServerEntry entry) {
/* 1403 */     for (Seat lSeat : this.seats) {
/*      */       
/* 1405 */       PlayerInfo oInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(lSeat.getOccupant());
/* 1406 */       if (oInfo != null) {
/*      */         
/*      */         try {
/*      */           
/* 1410 */           Player oPlayer = Players.getInstance().getPlayer(oInfo.wurmId);
/* 1411 */           alertPassengerOfEnemies((Creature)oPlayer, entry, false);
/*      */         }
/* 1413 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void notifyAllPassengers(String message, boolean includeDriver, boolean alert) {
/* 1430 */     for (Seat lSeat : this.seats) {
/*      */       
/* 1432 */       if (includeDriver || lSeat != getPilotSeat()) {
/*      */         
/* 1434 */         PlayerInfo oInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(lSeat.getOccupant());
/* 1435 */         if (oInfo != null) {
/*      */           
/*      */           try {
/*      */             
/* 1439 */             Player oPlayer = Players.getInstance().getPlayer(oInfo.wurmId);
/* 1440 */             if (alert) {
/* 1441 */               oPlayer.getCommunicator().sendAlertServerMessage(message);
/*      */             } else {
/* 1443 */               oPlayer.getCommunicator().sendNormalServerMessage(message);
/*      */             } 
/* 1445 */           } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void alertPassengersOfKingdom(ServerEntry entry, boolean includeDriver) {
/* 1455 */     for (Seat lSeat : this.seats) {
/*      */       
/* 1457 */       if (includeDriver || lSeat != getPilotSeat()) {
/*      */         
/* 1459 */         PlayerInfo oInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(lSeat.getOccupant());
/* 1460 */         if (oInfo != null) {
/*      */           
/* 1462 */           byte oKingdom = (oInfo.getChaosKingdom() == 0) ? 4 : oInfo.getChaosKingdom();
/*      */           
/*      */           try {
/* 1465 */             Player oPlayer = Players.getInstance().getPlayer(oInfo.wurmId);
/* 1466 */             if ((!Server.getInstance().isPS() && entry.isChaosServer()) || (entry.PVPSERVER && !Servers.localServer.PVPSERVER)) {
/*      */ 
/*      */               
/* 1469 */               String kingdomMsg = "This course will take you into hostile territory";
/* 1470 */               if (oKingdom != oPlayer.getKingdomId())
/* 1471 */                 kingdomMsg = kingdomMsg + ", and you will join the " + Kingdoms.getNameFor(oKingdom) + " kingdom until you return"; 
/* 1472 */               oPlayer.getCommunicator().sendAlertServerMessage(kingdomMsg + ".");
/*      */             }
/* 1474 */             else if ((!Server.getInstance().isPS() && Servers.localServer.isChaosServer()) || (Servers.localServer.PVPSERVER && entry.HOMESERVER && !entry.PVPSERVER)) {
/*      */ 
/*      */               
/* 1477 */               String kingdomMsg = "This course will take you into friendly territory";
/* 1478 */               if (oKingdom != entry.getKingdom())
/* 1479 */                 kingdomMsg = kingdomMsg + ", and you will join the " + Kingdoms.getNameFor(entry.getKingdom()) + " kingdom until you return"; 
/* 1480 */               oPlayer.getCommunicator().sendNormalServerMessage(kingdomMsg + ".");
/*      */             } 
/*      */             
/* 1483 */             if (entry.PVPSERVER && !Servers.localServer.PVPSERVER && oPlayer.getDeity() != null)
/*      */             {
/* 1485 */               if (!QuestionParser.doesKingdomTemplateAcceptDeity(Kingdoms.getKingdomTemplateFor(oKingdom), oPlayer.getDeity())) {
/* 1486 */                 oPlayer.getCommunicator().sendAlertServerMessage("Warning: " + oPlayer.getDeity().getName() + " does not align with your kingdom of " + 
/* 1487 */                     Kingdoms.getNameFor(oKingdom) + ". If you continue travel to " + entry.getName() + " you will lose all faith and abilities granted by your deity.");
/*      */               }
/*      */             }
/*      */           }
/* 1491 */           catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkPassengerPermissions(Creature performer) {
/* 1506 */     boolean toReturn = false;
/* 1507 */     if (!Servers.localServer.PVPSERVER) {
/*      */       
/*      */       try {
/*      */         
/* 1511 */         Item ivehic = Items.getItem(this.wurmid);
/*      */ 
/*      */         
/* 1514 */         if (!ivehic.isGuest(performer) || !ivehic.mayCommand(performer))
/*      */         {
/* 1516 */           performer.getCommunicator().sendNormalServerMessage("You may not leave the server with this boat. You need to be explicitly specified in the boat's permissions.");
/*      */           
/* 1518 */           toReturn = true;
/*      */         
/*      */         }
/*      */         else
/*      */         {
/* 1523 */           for (Seat seat : getSeats())
/*      */           {
/* 1525 */             if (seat.isOccupied() && seat.type == 1 && 
/* 1526 */               !ivehic.isGuest(seat.getOccupant())) {
/*      */               
/*      */               try {
/*      */                 
/* 1530 */                 Creature c = Server.getInstance().getCreature(seat.occupant);
/* 1531 */                 if (!ivehic.mayPassenger(c)) {
/*      */                   
/* 1533 */                   performer.getCommunicator().sendNormalServerMessage("You may not leave the server with this boat as one of your passengers will not have passenger permission on new server.");
/*      */ 
/*      */                   
/* 1536 */                   toReturn = true;
/*      */                   
/*      */                   break;
/*      */                 } 
/* 1540 */               } catch (NoSuchCreatureException|NoSuchPlayerException noSuchCreatureException) {}
/*      */             
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1548 */       catch (NoSuchItemException noSuchItemException) {}
/*      */     }
/*      */ 
/*      */     
/* 1552 */     return !toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public void touchPlotCourseCooldowns() {
/* 1557 */     touchPlotCourseCooldowns(1800000L);
/*      */   }
/*      */ 
/*      */   
/*      */   public void touchPlotCourseCooldowns(long cooldown) {
/* 1562 */     for (Seat seat : getSeats()) {
/*      */       
/* 1564 */       Cooldowns cd = Cooldowns.getCooldownsFor(seat.getOccupant(), true);
/* 1565 */       cd.addCooldown(717, System.currentTimeMillis() + cooldown, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public long getPlotCourseCooldowns() {
/* 1571 */     long currentTimer = 0L;
/* 1572 */     for (Seat seat : getSeats()) {
/*      */       
/* 1574 */       Cooldowns cd = Cooldowns.getCooldownsFor(seat.getOccupant(), false);
/* 1575 */       if (cd != null) {
/*      */         
/* 1577 */         long remain = cd.isAvaibleAt(717);
/* 1578 */         if (remain > currentTimer)
/* 1579 */           currentTimer = remain; 
/*      */       } 
/*      */     } 
/* 1582 */     return currentTimer;
/*      */   }
/*      */ 
/*      */   
/*      */   public String checkCourseRestrictions() {
/* 1587 */     long currentTimer = 0L;
/* 1588 */     for (Seat seat : getSeats()) {
/*      */ 
/*      */       
/*      */       try {
/* 1592 */         Player p = Players.getInstance().getPlayer(seat.getOccupant());
/* 1593 */         if (p.isFighting() || p.getEnemyPresense() > 0)
/*      */         {
/*      */ 
/*      */           
/* 1597 */           if (p.getSecondsPlayed() > 300.0F) {
/* 1598 */             return "There are enemies in the vicinity. You fail to focus on a course.";
/*      */           }
/*      */         }
/* 1601 */       } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1606 */       Cooldowns cd = Cooldowns.getCooldownsFor(seat.getOccupant(), false);
/* 1607 */       if (cd != null) {
/*      */         
/* 1609 */         long remain = cd.isAvaibleAt(717);
/* 1610 */         if (remain > currentTimer)
/* 1611 */           currentTimer = remain; 
/*      */       } 
/*      */     } 
/* 1614 */     if (currentTimer > 0L)
/* 1615 */       return "You must wait another " + Server.getTimeFor(currentTimer) + " to plot a course."; 
/* 1616 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPvPBlocking() {
/* 1621 */     for (Seat lSeat : this.seats) {
/*      */ 
/*      */       
/*      */       try {
/* 1625 */         Player oPlayer = Players.getInstance().getPlayer(lSeat.getOccupant());
/* 1626 */         if (oPlayer.isBlockingPvP()) {
/* 1627 */           return true;
/*      */         }
/* 1629 */       } catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1634 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Vehicle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */