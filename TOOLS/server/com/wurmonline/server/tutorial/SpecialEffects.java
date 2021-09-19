/*     */ package com.wurmonline.server.tutorial;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.combat.CombatEngine;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.zones.VolaTile;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
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
/*     */ public final class SpecialEffects
/*     */   implements CounterTypes, MiscConstants
/*     */ {
/*     */   private final String name;
/*     */   private final int id;
/*  37 */   private byte requiredPower = 0;
/*     */   private static final int numEffects = 6;
/*  39 */   private static final SpecialEffects[] effects = new SpecialEffects[6];
/*     */   public static final int NO_EFFECT = 0;
/*     */   public static final int OPEN_DOOR = 1;
/*     */   public static final int HEAL = 2;
/*     */   public static final int WOUND = 3;
/*     */   public static final int DELETE_TILE_ITEMS = 4;
/*     */   public static final int SEND_PLONK = 5;
/*     */   
/*     */   static {
/*  48 */     effects[0] = new SpecialEffects(0, "Do nothing");
/*  49 */     (effects[0]).requiredPower = 0;
/*  50 */     effects[1] = new SpecialEffects(1, "Open door or gate");
/*  51 */     (effects[1]).requiredPower = 2;
/*  52 */     effects[2] = new SpecialEffects(2, "Heal all wounds");
/*  53 */     (effects[2]).requiredPower = 2;
/*  54 */     effects[3] = new SpecialEffects(3, "Create a wound");
/*  55 */     (effects[3]).requiredPower = 2;
/*  56 */     effects[4] = new SpecialEffects(4, "Delete items on tile");
/*  57 */     (effects[4]).requiredPower = 2;
/*  58 */     effects[5] = new SpecialEffects(5, "Send a notification");
/*  59 */     (effects[5]).requiredPower = 2;
/*     */   }
/*     */ 
/*     */   
/*     */   private SpecialEffects(int _id, String _name) {
/*  64 */     this.id = _id;
/*  65 */     this.name = _name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPowerRequired(byte power) {
/*  70 */     this.requiredPower = power;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getPowerRequired() {
/*  75 */     return this.requiredPower;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  80 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId() {
/*  85 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final SpecialEffects[] getEffects() {
/*  90 */     return effects;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final SpecialEffects getEffect(int number) {
/*     */     try {
/*  97 */       return effects[number];
/*     */     }
/*  99 */     catch (Exception ex) {
/*     */       
/* 101 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean run(Creature performer, int tilex, int tiley, int layer) {
/*     */     VolaTile t, t12, t22;
/* 110 */     boolean toReturn = false;
/* 111 */     switch (this.id) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 118 */         t = Zones.getTileOrNull(tilex, tiley, (layer >= 0));
/* 119 */         if (t != null) {
/*     */           
/* 121 */           Creature[] creatures = t.getCreatures();
/* 122 */           for (Creature c : creatures)
/*     */           {
/* 124 */             c.getBody().healFully();
/*     */           }
/*     */         } 
/*     */         break;
/*     */       case 3:
/* 129 */         t12 = Zones.getTileOrNull(tilex, tiley, (layer >= 0));
/* 130 */         if (t12 != null) {
/*     */           
/* 132 */           Creature[] creatures = t12.getCreatures();
/* 133 */           for (Creature c : creatures)
/*     */           {
/* 135 */             CombatEngine.addWound(c, c, (byte)3, 13, 1000.0D, 1.0F, "bite", null, 0.0F, 0.0F, false, false, false, false);
/*     */           }
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 4:
/* 141 */         t22 = Zones.getTileOrNull(tilex, tiley, (layer >= 0));
/* 142 */         if (t22 != null) {
/*     */           
/* 144 */           Item[] items = t22.getItems();
/* 145 */           for (Item i : items) {
/*     */             
/* 147 */             if (!i.isIndestructible()) {
/* 148 */               Items.destroyItem(i.getWurmId());
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/*     */     
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean run(Creature performer, long target) {
/* 164 */     boolean toReturn = false;
/* 165 */     switch (this.id) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 172 */         performer.getBody().healFully();
/*     */         break;
/*     */       case 3:
/* 175 */         CombatEngine.addWound(performer, performer, (byte)3, 13, 1000.0D, 1.0F, "bite", null, 0.0F, 0.0F, false, false, false, false);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean run(Creature performer, long target, int numbers) {
/* 190 */     boolean toReturn = false;
/* 191 */     switch (this.id) {
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
/*     */       case 0:
/* 204 */         return false;
/*     */       case 5:
/*     */         performer.getCommunicator().sendPlonk((short)numbers);
/*     */     } 
/*     */     return run(performer, target);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\tutorial\SpecialEffects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */