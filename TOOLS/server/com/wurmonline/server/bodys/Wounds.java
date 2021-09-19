/*     */ package com.wurmonline.server.bodys;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.NoSpaceException;
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
/*     */ public final class Wounds
/*     */   implements MiscConstants
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(Wounds.class.getName());
/*     */ 
/*     */   
/*     */   private Map<Long, Wound> wounds;
/*     */ 
/*     */   
/*  44 */   private static final Map<Long, Wound> allWounds = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final Wound[] emptyWounds = new Wound[0];
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
/*     */   public Wound[] getWounds() {
/*  65 */     Wound[] toReturn = null;
/*  66 */     if (this.wounds == null || this.wounds.size() == 0) {
/*  67 */       toReturn = emptyWounds;
/*     */     } else {
/*  69 */       toReturn = (Wound[])this.wounds.values().toArray((Object[])new Wound[this.wounds.size()]);
/*  70 */     }  return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean hasWounds() {
/*  75 */     return (this.wounds != null && !this.wounds.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   void addWound(Wound wound) {
/*  80 */     if (this.wounds == null)
/*  81 */       this.wounds = new HashMap<>(); 
/*  82 */     this.wounds.put(new Long(wound.getWurmId()), wound);
/*  83 */     allWounds.put(new Long(wound.getWurmId()), wound);
/*     */     
/*  85 */     if (wound.getCreature() != null) {
/*     */       
/*     */       try {
/*     */         
/*  89 */         Item bodypart = wound.getCreature().getBody().getBodyPartForWound(wound);
/*     */         
/*     */         try {
/*  92 */           Creature[] watchers = bodypart.getWatchers();
/*  93 */           for (int x = 0; x < watchers.length; x++) {
/*  94 */             watchers[x].getCommunicator().sendAddWound(wound, bodypart);
/*     */           }
/*  96 */         } catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 102 */       catch (NoSpaceException nsp) {
/*     */         
/* 104 */         logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Wound getWound(long id) {
/* 111 */     Wound toReturn = null;
/* 112 */     if (this.wounds != null) {
/* 113 */       toReturn = this.wounds.get(new Long(id));
/*     */     }
/* 115 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Wound getWoundAtLocation(byte location) {
/* 120 */     if (this.wounds != null) {
/*     */       
/* 122 */       Wound[] w = getWounds();
/* 123 */       for (int x = 0; x < w.length; x++) {
/*     */         
/* 125 */         if (w[x].getLocation() == location)
/* 126 */           return w[x]; 
/*     */       } 
/*     */     } 
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Wound getWoundTypeAtLocation(byte location, byte type) {
/* 134 */     if (this.wounds != null) {
/*     */       
/* 136 */       Wound[] w = getWounds();
/* 137 */       for (int x = 0; x < w.length; x++) {
/*     */         
/* 139 */         if (w[x].getLocation() == location && w[x].getType() == type)
/* 140 */           return w[x]; 
/*     */       } 
/*     */     } 
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Wound getAnyWound(long id) {
/* 148 */     return allWounds.get(new Long(id));
/*     */   }
/*     */ 
/*     */   
/*     */   void remove(Wound wound) {
/* 153 */     if (this.wounds != null) {
/*     */       
/* 155 */       wound.removeAllModifiers();
/* 156 */       this.wounds.remove(new Long(wound.getWurmId()));
/* 157 */       allWounds.remove(new Long(wound.getWurmId()));
/* 158 */       wound.delete();
/* 159 */       if (this.wounds.size() == 0) {
/* 160 */         this.wounds = null;
/*     */       }
/* 162 */       if (wound.getCreature() != null) {
/*     */ 
/*     */         
/*     */         try {
/* 166 */           Item bodypart = wound.getCreature().getBody().getBodyPartForWound(wound);
/* 167 */           Creature[] watchers = bodypart.getWatchers();
/* 168 */           for (int x = 0; x < watchers.length; x++) {
/* 169 */             watchers[x].getCommunicator().sendRemoveWound(wound);
/*     */           }
/* 171 */         } catch (NoSuchCreatureException noSuchCreatureException) {
/*     */ 
/*     */         
/*     */         }
/* 175 */         catch (NoSpaceException nsp) {
/*     */           
/* 177 */           logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*     */         } 
/* 179 */         wound.removeCreature();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void poll(Creature holder) {
/* 187 */     if (this.wounds != null) {
/*     */       
/* 189 */       boolean woundPrevention = (holder != null && holder.hasFingerOfFoBonus());
/* 190 */       Wound[] w = getWounds();
/* 191 */       for (int x = 0; x < w.length; x++) {
/*     */ 
/*     */         
/*     */         try {
/* 195 */           w[x].poll(woundPrevention);
/*     */         }
/* 197 */         catch (Exception ex) {
/*     */           
/* 199 */           logger.log(Level.WARNING, ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getModifiedSkill(int woundPos) {
/* 207 */     return getModifiedSkill(woundPos, (byte)0);
/*     */   }
/*     */ 
/*     */   
/*     */   static final int getModifiedSkill(int woundPos, byte woundType) {
/* 212 */     if (woundPos == 1) {
/*     */       
/* 214 */       if (woundType == 9)
/* 215 */         return 10067; 
/* 216 */       return 100;
/*     */     } 
/* 218 */     if (woundPos == 21)
/* 219 */       return 10073; 
/* 220 */     if (woundPos == 13 || woundPos == 14)
/* 221 */       return 10056; 
/* 222 */     if (woundPos == 9 || woundPos == 10)
/* 223 */       return 1030; 
/* 224 */     if (woundPos == 3)
/* 225 */       return 1002; 
/* 226 */     if (woundPos == 22 || woundPos == 24)
/* 227 */       return 102; 
/* 228 */     if (woundPos == 25)
/* 229 */       return 104; 
/* 230 */     if (woundPos == 29)
/* 231 */       return 101; 
/* 232 */     if (woundPos == 33 || woundPos == 17)
/* 233 */       return 10067; 
/* 234 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\Wounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */