/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.utils.BMLBuilder;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TutorialStage
/*     */ {
/*  11 */   private static final String ERROR_NOSUBSTAGE = BMLBuilder.createText("Error while loading tutorial stage: no sub stages found.");
/*     */   
/*  13 */   private static final String ERROR_OVERSUBSTAGE = BMLBuilder.createText("Error while loading tutorial stage: not enough sub stages found.");
/*     */   
/*  15 */   private static final String ERROR_NOSUBSTAGE_UPDATE = BMLBuilder.createText("Error while updating tutorial stage: no sub stages found.");
/*     */   
/*  17 */   private static final String ERROR_OVERSUBSTAGE_UPDATE = BMLBuilder.createText("Error while updating tutorial stage: not enough sub stages found.");
/*     */   
/*     */   private final long playerId;
/*     */   
/*     */   private int currentSubStage;
/*     */   
/*     */   private boolean forceOpened = false;
/*     */   
/*  25 */   protected ArrayList<TutorialSubStage> subStages = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public TutorialStage(long playerId) {
/*  29 */     this.playerId = playerId;
/*     */     
/*  31 */     buildSubStages();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract TutorialStage getNextStage();
/*     */   
/*     */   public abstract TutorialStage getLastStage();
/*     */   
/*     */   public abstract void buildSubStages();
/*     */   
/*     */   public abstract short getWindowId();
/*     */   
/*     */   public String getCurrentBML() {
/*  44 */     if (this.subStages == null) {
/*  45 */       return ERROR_NOSUBSTAGE;
/*     */     }
/*  47 */     if (this.subStages.size() < getCurrentSubStage()) {
/*  48 */       return ERROR_OVERSUBSTAGE;
/*     */     }
/*  50 */     return ((TutorialSubStage)this.subStages.get(this.currentSubStage)).getBMLString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBML() {
/*  55 */     if (this.subStages == null) {
/*  56 */       return ERROR_NOSUBSTAGE_UPDATE;
/*     */     }
/*  58 */     if (this.subStages.size() < getCurrentSubStage()) {
/*  59 */       return ERROR_OVERSUBSTAGE_UPDATE;
/*     */     }
/*  61 */     return ((TutorialSubStage)this.subStages.get(this.currentSubStage)).getBMLUpdateString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAwaitingAnyTrigger() {
/*  66 */     return ((TutorialSubStage)this.subStages.get(this.currentSubStage)).awaitingTrigger();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldSkipTrigger() {
/*  71 */     return (!isAwaitingAnyTrigger() && ((TutorialSubStage)this.subStages.get(this.currentSubStage)).hadNextTrigger());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean awaitingTrigger(PlayerTutorial.PlayerTrigger trigger) {
/*  76 */     return ((TutorialSubStage)this.subStages.get(this.currentSubStage)).hasNextTrigger(trigger);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearTrigger() {
/*  81 */     ((TutorialSubStage)this.subStages.get(this.currentSubStage)).clearNextTrigger();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCurrentSubStage() {
/*  86 */     return this.currentSubStage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setForceOpened(boolean forceOpened) {
/*  91 */     this.forceOpened = forceOpened;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isForceOpened() {
/*  96 */     return this.forceOpened;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean increaseSubStage() {
/* 106 */     this.currentSubStage++;
/* 107 */     if (this.currentSubStage < this.subStages.size()) {
/* 108 */       return true;
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean decreaseSubStage() {
/* 118 */     if (this.currentSubStage == 0) {
/* 119 */       return true;
/*     */     }
/* 121 */     this.currentSubStage = Math.max(0, this.currentSubStage - 1);
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toLastSubStage() {
/* 128 */     this.currentSubStage = this.subStages.size() - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetSubStage() {
/* 133 */     this.subStages.clear();
/* 134 */     this.currentSubStage = 0;
/* 135 */     buildSubStages();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/* 140 */     return this.playerId;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract class TutorialSubStage
/*     */   {
/*     */     protected final long playerId;
/*     */     
/* 148 */     protected String bmlString = null;
/*     */     
/*     */     protected boolean hadNextTrigger = false;
/*     */     
/* 152 */     protected PlayerTutorial.PlayerTrigger enableNextTrigger = PlayerTutorial.PlayerTrigger.NONE;
/*     */ 
/*     */     
/*     */     public TutorialSubStage(long playerId) {
/* 156 */       this.playerId = playerId;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getPlayerId() {
/* 161 */       return this.playerId;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean awaitingTrigger() {
/* 166 */       return (this.enableNextTrigger != PlayerTutorial.PlayerTrigger.NONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNextTrigger(PlayerTutorial.PlayerTrigger trigger) {
/* 171 */       return (this.enableNextTrigger == trigger);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setNextTrigger(PlayerTutorial.PlayerTrigger trigger) {
/* 176 */       this.enableNextTrigger = trigger;
/* 177 */       if (trigger != PlayerTutorial.PlayerTrigger.NONE) {
/* 178 */         this.hadNextTrigger = true;
/*     */       }
/*     */     }
/*     */     
/*     */     public void clearNextTrigger() {
/* 183 */       setNextTrigger(PlayerTutorial.PlayerTrigger.NONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hadNextTrigger() {
/* 188 */       return this.hadNextTrigger;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getBMLString() {
/* 193 */       if (this.bmlString == null) {
/*     */         
/* 195 */         buildBMLString();
/* 196 */         triggerOnView();
/*     */       } 
/*     */       
/* 199 */       return this.bmlString;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void triggerOnView() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public String getBMLUpdateString() {
/* 209 */       return BMLBuilder.createBMLUpdate(new String[] { BMLBuilder.createButton("next", "Next", 80, 20, true) }).toString();
/*     */     }
/*     */     
/*     */     protected abstract void buildBMLString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\TutorialStage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */