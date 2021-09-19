/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.bodys.BodyHuman;
/*     */ import com.wurmonline.server.combat.CombatEngine;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.shared.exceptions.WurmServerException;
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
/*     */ public final class PracticeDollBehaviour
/*     */   extends ItemBehaviour
/*     */ {
/*  36 */   private static String[] typeString = (new BodyHuman()).typeString;
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
/*     */   PracticeDollBehaviour() {
/*  49 */     super((short)31);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
/*  55 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, target);
/*  56 */     if (target.getParentId() == -10L)
/*  57 */       toReturn.add(Actions.actionEntrys[211]); 
/*  58 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
/*  64 */     List<ActionEntry> toReturn = super.getBehavioursFor(performer, source, target);
/*  65 */     if (target.getParentId() == -10L)
/*  66 */       toReturn.add(Actions.actionEntrys[211]); 
/*  67 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
/*  74 */     boolean done = true;
/*     */     
/*  76 */     if (action == 211) {
/*     */       
/*  78 */       if (target.getParentId() == -10L) {
/*  79 */         done = CombatEngine.attack(performer, target, counter, act);
/*     */       } else {
/*  81 */         performer.getCommunicator().sendNormalServerMessage("The practice doll must be on the ground.");
/*     */       } 
/*  83 */     } else if (action == 1) {
/*     */       
/*  85 */       done = true;
/*     */       
/*  87 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*     */     } else {
/*     */       
/*  90 */       done = super.action(act, performer, source, target, action, counter);
/*  91 */     }  return done;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item target, short action, float counter) {
/*  97 */     boolean done = true;
/*  98 */     if (action == 211) {
/*     */       
/* 100 */       if (target.getParentId() == -10L) {
/* 101 */         done = CombatEngine.attack(performer, target, counter, act);
/*     */       } else {
/* 103 */         performer.getCommunicator().sendNormalServerMessage("The practice doll must be on the ground.");
/*     */       } 
/* 105 */     } else if (action == 1) {
/*     */       
/* 107 */       done = true;
/* 108 */       performer.getCommunicator().sendNormalServerMessage(target.examine(performer));
/* 109 */       target.sendEnchantmentStrings(performer.getCommunicator());
/*     */     } else {
/*     */       
/* 112 */       done = super.action(act, performer, target, action, counter);
/* 113 */     }  return done;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getWoundLocationString(int location) {
/* 118 */     return typeString[location];
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getRandomWoundPos() throws Exception {
/* 123 */     int rand = Server.rand.nextInt(100);
/* 124 */     if (rand < 3)
/* 125 */       return 1; 
/* 126 */     if (rand < 8)
/* 127 */       return 5; 
/* 128 */     if (rand < 13)
/* 129 */       return 6; 
/* 130 */     if (rand < 18)
/* 131 */       return 7; 
/* 132 */     if (rand < 23)
/* 133 */       return 8; 
/* 134 */     if (rand < 28)
/* 135 */       return 9; 
/* 136 */     if (rand < 32)
/* 137 */       return 10; 
/* 138 */     if (rand < 37)
/* 139 */       return 11; 
/* 140 */     if (rand < 42)
/* 141 */       return 12; 
/* 142 */     if (rand < 46)
/* 143 */       return 13; 
/* 144 */     if (rand < 50)
/* 145 */       return 14; 
/* 146 */     if (rand < 54)
/* 147 */       return 15; 
/* 148 */     if (rand < 58)
/* 149 */       return 16; 
/* 150 */     if (rand < 60)
/* 151 */       return 17; 
/* 152 */     if (rand < 61)
/* 153 */       return 18; 
/* 154 */     if (rand < 62)
/* 155 */       return 19; 
/* 156 */     if (rand < 73)
/* 157 */       return 21; 
/* 158 */     if (rand < 78)
/* 159 */       return 22; 
/* 160 */     if (rand < 83)
/* 161 */       return 23; 
/* 162 */     if (rand < 89)
/* 163 */       return 24; 
/* 164 */     if (rand < 90)
/* 165 */       return 25; 
/* 166 */     if (rand < 95)
/* 167 */       return 26; 
/* 168 */     if (rand < 100)
/* 169 */       return 27; 
/* 170 */     throw new WurmServerException("Bad randomizer");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\PracticeDollBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */