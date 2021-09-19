/*     */ package com.wurmonline.server.bodys;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.NoSpaceException;
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
/*     */ public final class TempWound
/*     */   extends Wound
/*     */ {
/*  36 */   private static final Logger logger = Logger.getLogger(TempWound.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -7813873321822326094L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TempWound(byte aType, byte aLocation, float aSeverity, long aOwner, float aPoisonSeverity, float aInfectionSeverity, boolean spell) {
/*  51 */     super(aType, aLocation, aSeverity, aOwner, aPoisonSeverity, aInfectionSeverity, true, false, spell);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void create() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void setSeverity(float sev) {
/*  63 */     this.severity = sev;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPoisonSeverity(float sev) {
/*  69 */     if (this.poisonSeverity != sev) {
/*     */       
/*  71 */       this.poisonSeverity = Math.max(0.0F, sev);
/*  72 */       this.poisonSeverity = Math.min(100.0F, this.poisonSeverity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setInfectionSeverity(float sev) {
/*  79 */     if (this.infectionSeverity != sev) {
/*     */       
/*  81 */       this.infectionSeverity = Math.max(0.0F, sev);
/*  82 */       this.infectionSeverity = Math.min(100.0F, this.infectionSeverity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBandaged(boolean aBandaged) {
/*  89 */     if (this.isBandaged != aBandaged)
/*     */     {
/*  91 */       this.isBandaged = aBandaged;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void setLastPolled(long lp) {
/*  98 */     if (this.lastPolled != lp)
/*     */     {
/* 100 */       this.lastPolled = lp;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHealeff(byte healeff) {
/* 107 */     if (this.healEff < healeff) {
/*     */       
/* 109 */       this.healEff = healeff;
/*     */       
/*     */       try {
/* 112 */         if (getCreature().getBody() != null) {
/*     */           
/* 114 */           Item bodypart = getCreature().getBody().getBodyPartForWound(this);
/*     */ 
/*     */           
/*     */           try {
/* 118 */             Creature[] watchers = bodypart.getWatchers();
/* 119 */             for (int x = 0; x < watchers.length; x++) {
/* 120 */               watchers[x].getCommunicator().sendUpdateWound(this, bodypart);
/*     */             }
/* 122 */           } catch (NoSuchCreatureException noSuchCreatureException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 129 */         else if (getCreature() != null) {
/* 130 */           logger.log(Level.WARNING, getCreature().getName() + " body is null.", new Exception());
/*     */         } else {
/* 132 */           logger.log(Level.WARNING, "Wound: creature==null", new Exception());
/*     */         }
/*     */       
/* 135 */       } catch (NoSpaceException nsp) {
/*     */         
/* 137 */         logger.log(Level.INFO, nsp.getMessage(), (Throwable)nsp);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   final void delete() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\bodys\TempWound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */