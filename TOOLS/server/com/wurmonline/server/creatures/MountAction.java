/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.behaviours.Vehicle;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class MountAction
/*     */   implements MiscConstants
/*     */ {
/*  29 */   private Creature creature = null;
/*  30 */   private Item item = null;
/*  31 */   private Vehicle vehicle = null;
/*  32 */   private int seatNum = 0;
/*     */   private boolean asDriver = false;
/*  34 */   private float offz = 0.0F;
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
/*     */   public MountAction(@Nullable Creature aCreature, @Nullable Item aItem, Vehicle aVehicle, int aSeatNum, boolean aAsDriver, float aOffz) {
/*  53 */     this.creature = aCreature;
/*  54 */     this.item = aItem;
/*  55 */     this.vehicle = aVehicle;
/*  56 */     this.seatNum = aSeatNum;
/*  57 */     this.asDriver = aAsDriver;
/*  58 */     this.offz = aOffz;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isBoat() {
/*  63 */     return (this.item != null && this.item.isBoat());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Creature getCreature() {
/*  73 */     return this.creature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Item getItem() {
/*  83 */     return this.item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vehicle getVehicle() {
/*  93 */     return this.vehicle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSeatNum() {
/* 103 */     return this.seatNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isAsDriver() {
/* 113 */     return this.asDriver;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getOffZ() {
/* 118 */     return this.offz;
/*     */   }
/*     */ 
/*     */   
/*     */   void sendData(Creature performer) {
/* 123 */     if (this.item != null) {
/*     */       
/* 125 */       if (this.asDriver) {
/*     */         
/* 127 */         performer.getCurrentTile().sendAttachCreature(performer.getWurmId(), this.item.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum, true);
/*     */         
/* 129 */         VolaTile t = Zones.getTileOrNull(this.item.getTileX(), this.item.getTileY(), this.item.isOnSurface());
/* 130 */         if (t != null) {
/* 131 */           t.sendAttachCreature(performer.getWurmId(), this.item.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum, true);
/*     */         }
/*     */         
/* 134 */         performer.getMovementScheme().setVehicleRotation(this.item.getRotation());
/* 135 */         performer.getCommunicator().setVehicleController(-1L, this.item.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.vehicle.maxDepth, this.vehicle.maxHeight, this.vehicle.maxHeightDiff, this.item
/*     */             
/* 137 */             .getRotation(), this.seatNum);
/*     */         
/* 139 */         if (this.item.isBoat())
/*     */         {
/* 141 */           performer.getMovementScheme().addMountSpeed((short)this.vehicle.calculateNewBoatSpeed(false));
/* 142 */           (performer.getMovementScheme()).commandingBoat = true;
/* 143 */           if (this.item.isMooredBoat()) {
/*     */             
/* 145 */             performer.getCommunicator().sendNormalServerMessage("The " + this.item
/* 146 */                 .getName() + " is currently moored and won't move.");
/* 147 */             performer.getMovementScheme().setMooredMod(true);
/* 148 */             performer.getMovementScheme().addWindImpact((byte)0);
/*     */             
/*     */             return;
/*     */           } 
/* 152 */           performer.getMovementScheme().addWindImpact(this.vehicle.getWindImpact());
/*     */         }
/*     */         else
/*     */         {
/* 156 */           performer.getMovementScheme().addMountSpeed((short)this.vehicle.calculateNewVehicleSpeed(true));
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 161 */         if (performer.hasLink()) {
/* 162 */           performer.getCommunicator().attachCreature(-1L, this.item.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         }
/* 164 */         performer.getCurrentTile().sendAttachCreature(performer.getWurmId(), this.item.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         
/* 166 */         VolaTile t = Zones.getTileOrNull(this.item.getTileX(), this.item.getTileY(), this.item.isOnSurface());
/* 167 */         t.sendAttachCreature(performer.getWurmId(), this.item.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         
/* 169 */         if (this.vehicle.pilotId != -10L) {
/*     */           
/*     */           try {
/*     */             
/* 173 */             Creature pilot = Server.getInstance().getCreature(this.vehicle.pilotId);
/* 174 */             if (this.item.isBoat())
/*     */             {
/* 176 */               pilot.getMovementScheme().addMountSpeed((short)this.vehicle.calculateNewBoatSpeed(false));
/*     */             }
/*     */             else
/*     */             {
/* 180 */               pilot.getMovementScheme().addMountSpeed((short)this.vehicle.calculateNewVehicleSpeed(true));
/*     */             }
/*     */           
/* 183 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/* 187 */         performer.getMovementScheme().resetBm();
/*     */       }
/*     */     
/* 190 */     } else if (this.creature != null) {
/*     */       
/* 192 */       if (this.asDriver) {
/*     */         
/* 194 */         performer.getCurrentTile().sendAttachCreature(performer.getWurmId(), this.creature.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         
/* 196 */         VolaTile t = Zones.getTileOrNull(this.creature.getTileX(), this.creature.getTileY(), this.creature.isOnSurface());
/* 197 */         if (t != null) {
/* 198 */           t.sendAttachCreature(performer.getWurmId(), this.creature.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         }
/* 200 */         performer.getMovementScheme().setVehicleRotation(this.creature.getStatus().getRotation());
/*     */         
/* 202 */         performer.getCommunicator().setVehicleController(-1L, this.creature.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.vehicle.maxDepth, this.vehicle.maxHeight, this.vehicle.maxHeightDiff, this.creature
/*     */             
/* 204 */             .getStatus().getRotation(), this.seatNum);
/* 205 */         performer.getMovementScheme().addMountSpeed((short)this.vehicle.calculateNewMountSpeed(this.creature, true));
/*     */       }
/*     */       else {
/*     */         
/* 209 */         if (performer.hasLink()) {
/* 210 */           performer.getCommunicator().attachCreature(-1L, this.creature.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         }
/* 212 */         performer.getCurrentTile().sendAttachCreature(performer.getWurmId(), this.creature.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         
/* 214 */         VolaTile t = Zones.getTileOrNull(this.creature.getTileX(), this.creature.getTileY(), this.creature.isOnSurface());
/* 215 */         if (t != null) {
/* 216 */           t.sendAttachCreature(performer.getWurmId(), this.creature.getWurmId(), (this.vehicle.seats[this.seatNum]).offx, (this.vehicle.seats[this.seatNum]).offy, (this.vehicle.seats[this.seatNum]).offz, this.seatNum);
/*     */         }
/* 218 */         if (this.vehicle.pilotId != -10L) {
/*     */           
/*     */           try {
/*     */             
/* 222 */             Creature pilot = Server.getInstance().getCreature(this.vehicle.pilotId);
/* 223 */             pilot.getMovementScheme().addMountSpeed((short)this.vehicle.calculateNewMountSpeed(this.creature, true));
/*     */           }
/* 225 */           catch (Exception exception) {}
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     performer.removeCarriedWeight(0);
/* 234 */     clear();
/*     */   }
/*     */ 
/*     */   
/*     */   private void clear() {
/* 239 */     this.creature = null;
/* 240 */     this.item = null;
/* 241 */     this.vehicle = null;
/* 242 */     this.seatNum = 0;
/* 243 */     this.asDriver = false;
/* 244 */     this.offz = 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\MountAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */