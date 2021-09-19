/*    */ package com.wurmonline.server.items;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CreationCategories
/*    */ {
/* 25 */   UNKNOWN("Unknown"),
/* 26 */   SHIPBUILDING("Shipbuilding resources"),
/* 27 */   STATUES("Statues"),
/* 28 */   STORAGE("Storage"),
/* 29 */   TOOLS("Tools"),
/* 30 */   CONSTRUCTION_MATERIAL("Construction material"),
/* 31 */   WEAPONS("Weapons"),
/* 32 */   BOWS("Bows"),
/* 33 */   KINDLINGS("Kindlings"),
/* 34 */   FIRE("Furnaces"),
/* 35 */   POTTERY("Pottery"),
/* 36 */   CONTAINER("Container"),
/* 37 */   BLADES("Weapon blades"),
/* 38 */   TOOL_PARTS("Tool parts"),
/* 39 */   FLETCHING("Fletching"),
/* 40 */   WEAPON_HEADS("Weapon heads"),
/* 41 */   RESOURCES("Resources"),
/* 42 */   ARMOUR("Armour"),
/* 43 */   SAILS("Sails"),
/* 44 */   CLOTHES("Clothes"),
/* 45 */   COOKING_UTENSILS("Cooking utensils"),
/* 46 */   DYES("Dyes"),
/* 47 */   HEALING("Healing"),
/* 48 */   LIGHTS_AND_LAMPS("Lights and lamps"),
/* 49 */   RUGS("Rugs"),
/* 50 */   FLAGS("Flags"),
/* 51 */   DECORATION("Decoration"),
/* 52 */   BAGS("Bags"),
/* 53 */   SHIELDS("Shields"),
/* 54 */   STATUETTES("Statuettes"),
/* 55 */   JEWELRY("Jewelry"),
/* 56 */   TOYS("Toys"),
/* 57 */   FOUNTAINS_AND_WELLS("Fountains and wells"),
/* 58 */   MINE_DOORS("Mine doors and support beams"),
/* 59 */   LOCKS("Locks"),
/* 60 */   ROPES("Ropes"),
/* 61 */   FOOD("Food"),
/* 62 */   ALTAR("Altar"),
/* 63 */   MAGIC("Magic"),
/* 64 */   EPIC("Epic"),
/* 65 */   FURNITURE("Furniture"),
/* 66 */   CARTS("Carts"),
/* 67 */   WARMACHINES("Warmachines"),
/* 68 */   SIGNS("Signs"),
/* 69 */   TOWERS("Towers"),
/* 70 */   MAILBOXES("Mailboxes"),
/* 71 */   TRAPS("Traps"),
/* 72 */   ANIMAL_EQUIPMENT("Animal equipment"),
/* 73 */   ANIMAL_EQUIPMENT_PART("Animal equipment parts"),
/* 74 */   ALCHEMY("Alchemy"),
/* 75 */   WRITING("Writing"),
/* 76 */   SHIPS("Ships"),
/* 77 */   CART_PARTS("Cart parts"),
/* 78 */   COMBAT_TRAINING("Combat training"),
/* 79 */   PRODUCTION("Production"),
/* 80 */   TENTS("Tents"),
/* 81 */   HIGHWAY("Highway");
/*    */ 
/*    */   
/*    */   private final String categoryName;
/*    */ 
/*    */   
/*    */   CreationCategories(String categoryName) {
/* 88 */     this.categoryName = categoryName;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getCategoryName() {
/* 93 */     return this.categoryName;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\CreationCategories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */