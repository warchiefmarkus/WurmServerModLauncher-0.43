/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ final class ExamineBehaviour
/*     */   extends Behaviour
/*     */ {
/*     */   ExamineBehaviour() {
/*  34 */     super((short)11);
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
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile) {
/*  46 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  47 */     toReturn.add(Actions.actionEntrys[1]);
/*  48 */     return toReturn;
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
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int tilex, int tiley, boolean onSurface, int tile) {
/*  60 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  61 */     toReturn.add(Actions.actionEntrys[1]);
/*  62 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
/*  73 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  74 */     toReturn.add(Actions.actionEntrys[1]);
/*  75 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Item object) {
/*  86 */     List<ActionEntry> toReturn = new LinkedList<>();
/*  87 */     toReturn.add(Actions.actionEntrys[1]);
/*  88 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Creature object) {
/*  99 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 100 */     toReturn.add(Actions.actionEntrys[1]);
/* 101 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Creature object) {
/* 112 */     List<ActionEntry> toReturn = new LinkedList<>();
/* 113 */     toReturn.add(Actions.actionEntrys[1]);
/* 114 */     return toReturn;
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
/*     */   public boolean action(Action act, Creature performer, int tilex, int tiley, boolean onSurface, int tile, short action, float counter) {
/* 126 */     if (action == 1)
/*     */     {
/* 128 */       performer.getCommunicator().sendNormalServerMessage("You see a part of the lands of Wurm.");
/*     */     }
/* 130 */     return true;
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
/*     */   public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
/* 143 */     if (action == 1)
/*     */     {
/* 145 */       performer.getCommunicator().sendNormalServerMessage("You see a part of the lands of Wurm.");
/*     */     }
/* 147 */     return true;
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
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/* 159 */     if (action == 1) {
/*     */       
/* 161 */       performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/* 162 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*     */     } 
/* 164 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/* 175 */     if (action == 1) {
/*     */       
/* 177 */       performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/* 178 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*     */     } 
/* 180 */     return true;
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
/*     */   public boolean action(Action act, Creature performer, Item source, Creature target, short action, float counter) {
/* 192 */     if (action == 1) {
/*     */       
/* 194 */       performer.getCommunicator().sendNormalServerMessage(target.examine());
/* 195 */       target.getCommunicator().sendNormalServerMessage(source.getName() + " takes a long, good look at you.");
/*     */     } 
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Creature target, short action, float counter) {
/* 208 */     if (action == 1) {
/*     */       
/* 210 */       performer.getCommunicator().sendNormalServerMessage(target.examine());
/* 211 */       target.getCommunicator().sendNormalServerMessage(performer.getNameWithGenus() + " takes a long, good look at you.");
/*     */     } 
/*     */     
/* 214 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\ExamineBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */