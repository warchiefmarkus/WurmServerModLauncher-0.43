/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.utils.StringUtil;
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
/*     */ public final class Seat
/*     */   implements MiscConstants
/*     */ {
/*  35 */   private static Logger logger = Logger.getLogger(Seat.class.getName());
/*     */ 
/*     */   
/*     */   public static final byte TYPE_DRIVER = 0;
/*     */ 
/*     */   
/*     */   public static final byte TYPE_PASSENGER = 1;
/*     */   
/*     */   public static final byte TYPE_HITCHED = 2;
/*     */   
/*     */   public final byte type;
/*     */   
/*  47 */   public long occupant = -10L;
/*     */   
/*  49 */   public float offx = 0.0F;
/*     */   
/*  51 */   public float offy = 0.0F;
/*     */   
/*  53 */   public float offz = 0.0F;
/*     */ 
/*     */   
/*  56 */   private float altOffz = 0.0F;
/*     */   
/*  58 */   public float cover = 0.5F;
/*     */   
/*  60 */   public float manouvre = 0.5F;
/*     */   
/*  62 */   private static int allids = 0;
/*  63 */   int id = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Seat(byte _type) {
/*  73 */     this.id = allids++;
/*  74 */     this.type = _type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOccupied() {
/*  84 */     return (this.occupant != -10L);
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
/*     */   public boolean occupy(Vehicle vehicle, Creature creature) {
/*  97 */     if (this.occupant == -10L) {
/*     */       
/*  99 */       if (creature != null) {
/*     */         
/* 101 */         this.occupant = creature.getWurmId();
/* 102 */         String vehicleName = Vehicle.getVehicleName(vehicle);
/* 103 */         if (this.type == 0) {
/*     */           
/* 105 */           creature.getCommunicator().sendNormalServerMessage(
/* 106 */               StringUtil.format("You %s on the %s as the %s.", new Object[] { vehicle.embarkString, vehicleName, vehicle.pilotName }));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 111 */           Server.getInstance().broadCastAction(
/* 112 */               StringUtil.format("%s %s on the %s as the %s.", new Object[] {
/* 113 */                   creature.getName(), vehicle.embarksString, vehicleName, vehicle.pilotName
/*     */                 }), creature, 5);
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 119 */         else if (vehicle.isChair() || vehicle.isBed()) {
/*     */           
/* 121 */           creature.getCommunicator().sendNormalServerMessage(
/* 122 */               StringUtil.format("You %s on the %s.", new Object[] { vehicle.embarkString, vehicleName }));
/*     */ 
/*     */ 
/*     */           
/* 126 */           Server.getInstance().broadCastAction(
/* 127 */               StringUtil.format("%s %s on the %s.", new Object[] {
/* 128 */                   creature.getName(), vehicle.embarksString, vehicleName
/*     */                 }), creature, 5);
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 135 */           creature.getCommunicator().sendNormalServerMessage(
/* 136 */               StringUtil.format("You %s on the %s as a passenger.", new Object[] { vehicle.embarkString, vehicleName }));
/*     */ 
/*     */ 
/*     */           
/* 140 */           Server.getInstance().broadCastAction(
/* 141 */               StringUtil.format("%s %s on the %s as a passenger.", new Object[] {
/* 142 */                   creature.getName(), vehicle.embarksString, vehicleName
/*     */                 }), creature, 5);
/*     */         } 
/*     */ 
/*     */         
/* 147 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 151 */       logger.warning("A null Creature cannot occupy a seat (" + this + ") in a Vehicle (" + vehicle + ')');
/* 152 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 156 */     return false;
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
/*     */   boolean leave(Vehicle vehicle) {
/* 168 */     if (this.occupant != -10L) {
/*     */ 
/*     */       
/*     */       try {
/* 172 */         Creature cret = Server.getInstance().getCreature(this.occupant);
/* 173 */         cret.disembark(true);
/*     */       }
/* 175 */       catch (NoSuchPlayerException noSuchPlayerException) {
/*     */ 
/*     */       
/* 178 */       } catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */       
/* 181 */       this.occupant = -10L;
/* 182 */       return true;
/*     */     } 
/*     */     
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCover() {
/* 195 */     return this.cover;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCover(float aCover) {
/* 206 */     this.cover = aCover;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getManouvre() {
/* 216 */     return this.manouvre;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManouvre(float aManouvre) {
/* 227 */     this.manouvre = aManouvre;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/* 237 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(int aId) {
/* 248 */     this.id = aId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getType() {
/* 258 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOccupant() {
/* 268 */     return this.occupant;
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
/*     */   void setOccupant(long aOccupant) {
/* 280 */     this.occupant = aOccupant;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getOffx() {
/* 290 */     return this.offx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setOffx(float aOffx) {
/* 301 */     this.offx = aOffx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getOffy() {
/* 311 */     return this.offy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setOffy(float aOffy) {
/* 322 */     this.offy = aOffy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getOffz() {
/* 332 */     return this.offz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setOffz(float aOffz) {
/* 343 */     this.offz = aOffz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 354 */     StringBuilder lBuilder = new StringBuilder(200);
/* 355 */     lBuilder.append("Seat [Type: ").append(this.type);
/* 356 */     lBuilder.append(", Occupant: ").append(this.occupant);
/* 357 */     lBuilder.append(", OffsetX: ").append(this.offx);
/* 358 */     lBuilder.append(", OffsetY: ").append(this.offy);
/* 359 */     lBuilder.append(", OffsetZ: ").append(this.offz);
/* 360 */     lBuilder.append(']');
/* 361 */     return lBuilder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAltOffz() {
/* 371 */     return this.altOffz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAltOffz(float aAltOffz) {
/* 382 */     this.altOffz = aAltOffz;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Seat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */