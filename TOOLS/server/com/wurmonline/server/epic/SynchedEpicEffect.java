/*     */ package com.wurmonline.server.epic;
/*     */ 
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
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
/*     */ public class SynchedEpicEffect
/*     */ {
/*     */   public static final int TYPE_SUMMON_CREATURES = 1;
/*     */   public static final int TYPE_EFFECT = 2;
/*     */   public static final int TYPE_EFFECT_CONTROLLER = 3;
/*     */   public static final int TYPE_EFFECT_DEAL_ITEM = 4;
/*     */   public static final int TYPE_EFFECT_DEAL_ITEM_FRAGMENT = 5;
/*     */   private final int type;
/*     */   private long deityNumber;
/*     */   private int creatureTemplateId;
/*     */   private int effectNumber;
/*     */   private int bonusEffectNum;
/*     */   private String eventString;
/*     */   private boolean resetKarma;
/*  46 */   private static final Logger logger = Logger.getLogger(SynchedEpicEffect.class.getName());
/*     */ 
/*     */   
/*     */   public SynchedEpicEffect(int _type) {
/*  50 */     this.type = _type;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void run() {
/*  55 */     if (this.type == 1) {
/*     */       
/*  57 */       Effectuator.spawnOwnCreatures(this.deityNumber, this.creatureTemplateId, false);
/*     */     }
/*  59 */     else if (this.type == 2) {
/*     */       
/*  61 */       Effectuator.doEvent(this.effectNumber, this.deityNumber, this.creatureTemplateId, this.bonusEffectNum, this.eventString);
/*     */     }
/*  63 */     else if (this.type == 3) {
/*     */       
/*  65 */       Effectuator.setEffectController(getEffectNumber(), getDeityNumber());
/*     */     } 
/*  67 */     if (isResetKarma()) {
/*     */       
/*  69 */       logger.log(Level.INFO, "Resetting scenario karma");
/*  70 */       PlayerInfoFactory.resetScenarioKarma();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDeityNumber() {
/*  81 */     return this.deityNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeityNumber(long aDeityNumber) {
/*  92 */     this.deityNumber = aDeityNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCreatureTemplateId() {
/* 102 */     return this.creatureTemplateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreatureTemplateId(int aCreatureTemplateId) {
/* 113 */     this.creatureTemplateId = aCreatureTemplateId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEffectNumber() {
/* 123 */     return this.effectNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEffectNumber(int aEffectNumber) {
/* 134 */     this.effectNumber = aEffectNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBonusEffectNum() {
/* 144 */     return this.bonusEffectNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBonusEffectNum(int aBonusEffectNum) {
/* 155 */     this.bonusEffectNum = aBonusEffectNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEventString() {
/* 165 */     return this.eventString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEventString(String aEventString) {
/* 176 */     this.eventString = aEventString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isResetKarma() {
/* 186 */     return this.resetKarma;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setResetKarma(boolean aResetKarma) {
/* 197 */     this.resetKarma = aResetKarma;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\SynchedEpicEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */