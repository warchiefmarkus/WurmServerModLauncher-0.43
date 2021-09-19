/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class ActionStack
/*     */   implements TimeConstants
/*     */ {
/*     */   private final LinkedList<Action> quickActions;
/*     */   private final LinkedList<Action> slowActions;
/*     */   private boolean clearing = false;
/*     */   private long lastPolledStunned;
/*     */   
/*     */   public void addAction(Action action) {
/*  53 */     int maxPrio = 1;
/*  54 */     if (action.isQuick()) {
/*     */       
/*  56 */       if (this.quickActions.size() < 10) {
/*  57 */         this.quickActions.addLast(action);
/*     */       } else {
/*  59 */         action.getPerformer().getCommunicator()
/*  60 */           .sendSafeServerMessage("You can't remember that many things to do in advance.");
/*     */       }
/*     */     
/*     */     }
/*  64 */     else if (!this.slowActions.isEmpty()) {
/*     */       
/*  66 */       if (!Action.isStackable(action.getNumber())) {
/*     */         
/*  68 */         action.getPerformer().getCommunicator().sendNormalServerMessage("You're too busy.");
/*     */         return;
/*     */       } 
/*  71 */       if (this.slowActions.size() > 1 && !Action.isStackableFight(action.getNumber()))
/*     */       {
/*  73 */         for (ListIterator<Action> listIterator = this.slowActions.listIterator(); listIterator.hasNext(); ) {
/*     */           
/*  75 */           Action curr = listIterator.next();
/*  76 */           if (curr.getNumber() == action.getNumber()) {
/*     */             
/*  78 */             action.getPerformer().getCommunicator().sendNormalServerMessage("You're too busy.");
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       }
/*  83 */       boolean insertedAndShouldPoll = false;
/*     */       
/*  85 */       for (ListIterator<Action> it = this.slowActions.listIterator(); it.hasNext(); ) {
/*     */         
/*  87 */         Action curr = it.next();
/*  88 */         if (maxPrio < curr.getPriority()) {
/*     */           
/*  90 */           maxPrio = curr.getPriority();
/*  91 */           if (action.getPriority() > maxPrio) {
/*     */             
/*  93 */             it.previous();
/*     */ 
/*     */ 
/*     */             
/*  97 */             if (action.getNumber() == 114) {
/*     */               
/*  99 */               insertedAndShouldPoll = true;
/* 100 */               it.add(action);
/*     */               
/*     */               break;
/*     */             } 
/*     */             
/* 105 */             it.add(action);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/* 111 */       if (insertedAndShouldPoll) {
/*     */         
/* 113 */         action.poll();
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 118 */       if (action.getPerformer().isPlayer() && this.slowActions.size() > action.getPerformer().getMaxNumActions()) {
/*     */         
/* 120 */         if (!Action.isActionAttack(action.getNumber())) {
/* 121 */           action.getPerformer().getCommunicator().sendNormalServerMessage("You're too busy.");
/*     */         }
/*     */       } else {
/*     */         
/* 125 */         if (Actions.actionEntrys[((Action)this.slowActions.getLast()).getNumber()] == Actions.actionEntrys[action.getNumber()] && 
/* 126 */           !Action.isActionAttack(action.getNumber())) {
/* 127 */           action.getPerformer()
/* 128 */             .getCommunicator()
/* 129 */             .sendNormalServerMessage("After you " + Actions.actionEntrys[((Action)this.slowActions
/* 130 */                 .getLast()).getNumber()].getVerbFinishString() + " you will " + Actions.actionEntrys[action
/* 131 */                 .getNumber()].getVerbStartString() + " again.");
/*     */         }
/* 133 */         else if (!((Action)this.slowActions.getLast()).isOffensive() && !Action.isActionAttack(action.getNumber())) {
/* 134 */           action.getPerformer()
/* 135 */             .getCommunicator()
/* 136 */             .sendNormalServerMessage("After you " + Actions.actionEntrys[((Action)this.slowActions
/* 137 */                 .getLast()).getNumber()].getVerbFinishString() + " you will " + Actions.actionEntrys[action
/* 138 */                 .getNumber()].getVerbStartString() + ".");
/*     */         }
/* 140 */         else if (((Action)this.slowActions.getLast()).isOffensive() && action.isSpell()) {
/*     */           
/* 142 */           action.getPerformer()
/* 143 */             .getCommunicator()
/* 144 */             .sendCombatNormalMessage("After you " + Actions.actionEntrys[((Action)this.slowActions
/* 145 */                 .getLast()).getNumber()].getVerbFinishString() + " you will " + Actions.actionEntrys[action
/* 146 */                 .getNumber()].getVerbStartString() + ".");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 155 */         this.slowActions.addLast(action);
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 161 */     else if (action.getNumber() == 114) {
/*     */       
/* 163 */       if (!action.poll())
/*     */       {
/*     */         
/* 166 */         this.slowActions.add(action);
/*     */       }
/*     */     } else {
/*     */       
/* 170 */       this.slowActions.add(action);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeAction(Action action) {
/* 177 */     this.quickActions.remove(action);
/* 178 */     this.slowActions.remove(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public String stopCurrentAction(boolean farAway) throws NoSuchActionException {
/* 183 */     String toReturn = "";
/* 184 */     Action current = getCurrentAction();
/* 185 */     if (current.getNumber() == 136)
/* 186 */       current.getPerformer().setStealth(current.getPerformer().isStealth()); 
/* 187 */     toReturn = current.stop(farAway);
/* 188 */     if (current.getNumber() == 160)
/*     */     {
/* 190 */       MethodsFishing.playerOutOfRange(current.getPerformer(), current);
/*     */     }
/* 192 */     if (current.getNumber() == 925 || current.getNumber() == 926) {
/*     */       
/* 194 */       current.getPerformer().getCommunicator().sendCancelPlacingItem();
/* 195 */       toReturn = "";
/*     */     } 
/* 197 */     removeAction(current);
/* 198 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getCurrentAction() throws NoSuchActionException {
/* 203 */     if (!this.quickActions.isEmpty())
/* 204 */       return this.quickActions.getFirst(); 
/* 205 */     if (!this.slowActions.isEmpty())
/* 206 */       return this.slowActions.getFirst(); 
/* 207 */     throw new NoSuchActionException("No Current Action");
/*     */   }
/*     */   public ActionStack() {
/* 210 */     this.lastPolledStunned = 0L;
/*     */     this.quickActions = new LinkedList<>();
/*     */     this.slowActions = new LinkedList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean poll(Creature owner) {
/* 219 */     boolean toReturn = true;
/*     */     
/* 221 */     if (owner.getStatus().getStunned() > 0.0F && !owner.isDead()) {
/*     */       
/* 223 */       if (this.lastPolledStunned == 0L)
/* 224 */         this.lastPolledStunned = System.currentTimeMillis(); 
/* 225 */       toReturn = false;
/* 226 */       float delta = (float)(System.currentTimeMillis() - this.lastPolledStunned) / 1000.0F;
/* 227 */       owner.getStatus().setStunned(owner.getStatus().getStunned() - delta, false);
/* 228 */       if (owner.getStatus().getStunned() <= 0.0F) {
/* 229 */         this.lastPolledStunned = 0L;
/*     */       } else {
/* 231 */         this.lastPolledStunned = System.currentTimeMillis();
/*     */       } 
/* 233 */     } else if (!this.quickActions.isEmpty()) {
/*     */       
/* 235 */       while (!this.quickActions.isEmpty())
/*     */       {
/* 237 */         if (((Action)this.quickActions.getFirst()).poll())
/*     */         {
/* 239 */           this.quickActions.removeFirst();
/*     */         }
/*     */       }
/*     */     
/* 243 */     } else if (!this.slowActions.isEmpty()) {
/*     */       
/* 245 */       Action first = this.slowActions.getFirst();
/*     */       
/* 247 */       if (first.poll()) {
/*     */ 
/*     */         
/* 250 */         if (!this.slowActions.isEmpty())
/* 251 */           this.slowActions.removeFirst(); 
/* 252 */         if (!this.slowActions.isEmpty()) {
/*     */           
/* 254 */           first = this.slowActions.getFirst();
/*     */ 
/*     */           
/* 257 */           if (first.getCounterAsFloat() >= 1.0F && first.getNumber() != 114 && first
/* 258 */             .getNumber() != 160) {
/* 259 */             owner.sendActionControl(first.getActionString(), true, first.getTimeLeft());
/* 260 */           } else if (first.getNumber() != 160) {
/* 261 */             owner.sendActionControl("", false, 0);
/*     */           } 
/*     */         } else {
/* 264 */           owner.sendActionControl("", false, 0);
/*     */         } 
/*     */       } else {
/* 267 */         toReturn = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttacks(Creature owner) {
/* 279 */     if (!this.clearing)
/*     */     {
/* 281 */       for (ListIterator<Action> lit = this.slowActions.listIterator(); lit.hasNext(); ) {
/*     */         
/* 283 */         Action act = lit.next();
/* 284 */         if (act.getNumber() == 114)
/*     */         {
/*     */           
/* 287 */           lit.remove();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeTarget(long wurmid) {
/* 295 */     if (!this.clearing)
/*     */     {
/* 297 */       for (ListIterator<Action> lit = this.slowActions.listIterator(); lit.hasNext(); ) {
/*     */         
/* 299 */         Action act = lit.next();
/* 300 */         if (act.getTarget() == wurmid) {
/*     */ 
/*     */           
/*     */           try {
/* 304 */             if (act == getCurrentAction())
/*     */             {
/* 306 */               act.getPerformer().getCommunicator().sendNormalServerMessage(act.stop(false));
/* 307 */               act.getPerformer().sendActionControl("", false, 0);
/*     */             }
/*     */           
/* 310 */           } catch (NoSuchActionException noSuchActionException) {}
/*     */ 
/*     */ 
/*     */           
/* 314 */           lit.remove();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceTarget(long wurmid) {
/* 322 */     if (!this.clearing)
/*     */     {
/* 324 */       for (ListIterator<Action> lit = this.slowActions.listIterator(); lit.hasNext(); ) {
/*     */         
/* 326 */         Action act = lit.next();
/* 327 */         if (act.isOffensive())
/*     */         {
/* 329 */           act.setTarget(wurmid);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 337 */     this.clearing = true;
/* 338 */     this.quickActions.clear();
/* 339 */     for (Action actionToStop : this.slowActions)
/*     */     {
/* 341 */       actionToStop.stop(false);
/*     */     }
/* 343 */     this.slowActions.clear();
/* 344 */     this.clearing = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getLastSlowAction() {
/* 349 */     if (!this.clearing)
/*     */     {
/* 351 */       if (!this.slowActions.isEmpty()) {
/*     */         
/*     */         try {
/*     */           
/* 355 */           return this.slowActions.getLast();
/*     */         }
/* 357 */         catch (NoSuchElementException nse) {
/*     */           
/* 359 */           return null;
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/* 364 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ActionStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */