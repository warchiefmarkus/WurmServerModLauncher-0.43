/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.behaviours.ItemBehaviour;
/*     */ import com.wurmonline.server.behaviours.Vehicle;
/*     */ import com.wurmonline.server.behaviours.Vehicles;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.CreationEntry;
/*     */ import com.wurmonline.server.items.CreationMatrix;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.BitSet;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.SimpleTimeZone;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GeneralUtilities
/*     */   implements MiscConstants
/*     */ {
/*     */   public static boolean isValidTileLocation(int tilex, int tiley) {
/*  62 */     return (tilex >= 0 && tilex < 1 << Constants.meshSize && tiley >= 0 && tiley < 1 << Constants.meshSize);
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
/*     */ 
/*     */   
/*     */   public static float calcOreRareQuality(double power, int actionBonus, int toolBonus) {
/*  77 */     return calcRareQuality(power, actionBonus, toolBonus, 0, 2, 108.428F);
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
/*     */   public static float calcRareQuality(double power, int actionBonus, int toolBonus) {
/*  89 */     return calcRareQuality(power, actionBonus, toolBonus, 0, 2, 100.0F);
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
/*     */ 
/*     */   
/*     */   public static float calcRareQuality(double power, int actionBonus, int toolBonus, int targetBonus) {
/* 104 */     return calcRareQuality(power, actionBonus, toolBonus, targetBonus, 3, 100.0F);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float calcRareQuality(double power, int actionBonus, int toolBonus, int targetBonus, int numbBonus, float fiddleFactor) {
/* 121 */     float rPower = (float)power;
/* 122 */     int totalBonus = toolBonus + targetBonus + actionBonus;
/* 123 */     float bonus = 0.0F;
/* 124 */     if (totalBonus > 0) {
/*     */       
/* 126 */       float val = fiddleFactor - rPower;
/* 127 */       float square = val * val;
/* 128 */       float n = square / 1000.0F;
/* 129 */       float mod = Math.min(n * 1.25F, 1.0F);
/* 130 */       bonus = totalBonus * 3.0F / numbBonus * mod;
/*     */     } 
/* 132 */     return Math.max(Math.min(99.999F, rPower + bonus), 1.0F);
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
/*     */   public static final Map<String, Map<CreationEntry, Integer>> getCreationList(Item source, Item target, Player player) {
/* 144 */     CreationEntry[] entries = CreationMatrix.getInstance().getCreationOptionsFor(source, target);
/* 145 */     Map<String, Map<CreationEntry, Integer>> map = ItemBehaviour.generateMapfromOptions((Creature)player, source, target, entries);
/* 146 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toGMTString(long aDate) {
/* 157 */     SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
/* 158 */     sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
/*     */     
/* 160 */     return sdf.format(new Date(aDate));
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
/*     */   public static void setSettingsBits(BitSet bits, int value) {
/* 172 */     for (int x = 0; x < 32; x++)
/*     */     {
/* 174 */       bits.set(x, ((value & 0x1) == 1));
/*     */     }
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
/*     */   public static int getIntSettingsFrom(BitSet bits) {
/* 188 */     int ret = 0;
/* 189 */     for (int x = 0; x < 32; x++) {
/*     */       
/* 191 */       if (bits.get(x))
/*     */       {
/* 193 */         ret = (int)(ret + (1L << x));
/*     */       }
/*     */     } 
/* 196 */     return ret;
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
/*     */   public static boolean isOnSameLevel(Creature creature1, Creature creature2) {
/* 209 */     float difference = Math.abs(creature1.getStatus().getPositionZ() - creature2.getStatus().getPositionZ()) * 10.0F;
/* 210 */     return (difference < 30.0F);
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
/*     */   public static boolean mayAttackSameLevel(Creature creature1, Creature creature2) {
/* 222 */     float difference = Math.abs(creature1.getStatus().getPositionZ() - creature2.getStatus().getPositionZ()) * 10.0F;
/*     */     
/* 224 */     return (difference < 29.7F);
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
/*     */   public static boolean isOnSameLevel(Creature creature, Item item) {
/* 236 */     float pz = creature.getStatus().getPositionZ();
/* 237 */     if (creature.getVehicle() != -10L) {
/*     */       
/* 239 */       Vehicle vehicle = Vehicles.getVehicleForId(creature.getVehicle());
/* 240 */       if (vehicle != null) {
/* 241 */         pz = vehicle.getPosZ();
/*     */       }
/*     */     } 
/* 244 */     float difference = Math.abs(Math.max(0.0F, pz) - Math.max(0.0F, item.getPosZ())) * 10.0F;
/* 245 */     return (difference < 30.0F);
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
/*     */   public static short getHeight(int tilex, int tiley, boolean onSurface) {
/* 258 */     if (onSurface) {
/* 259 */       return Tiles.decodeHeight(Server.surfaceMesh.getTile(tilex, tiley));
/*     */     }
/* 261 */     return Tiles.decodeHeight(Server.caveMesh.getTile(tilex, tiley));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\GeneralUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */