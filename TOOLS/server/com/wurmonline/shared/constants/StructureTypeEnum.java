/*     */ package com.wurmonline.shared.constants;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Optional;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum StructureTypeEnum
/*     */ {
/*     */   public final String typeName;
/*     */   public final String modelShortName;
/*  17 */   SOLID((byte)0, "wall", ""),
/*  18 */   WINDOW((byte)1, "window", "window"),
/*  19 */   DOOR((byte)2, "door", "door"),
/*  20 */   DOUBLE_DOOR((byte)3, "double door", "doubledoor"),
/*  21 */   ARCHED((byte)4, "arched", "arched"),
/*  22 */   NARROW_WINDOW((byte)5, "narrow window", "windownarrow"),
/*  23 */   PORTCULLIS((byte)6, "portcullis", "portcullis"),
/*  24 */   BARRED((byte)7, "barred", "bars1"),
/*  25 */   RUBBLE((byte)8, "rubble", "rubble"),
/*  26 */   BALCONY((byte)9, "balcony", "balcony"),
/*  27 */   JETTY((byte)10, "jetty", "jetty"),
/*  28 */   ORIEL((byte)11, "oriel", "oriel2"),
/*  29 */   CANOPY_DOOR((byte)12, "canopy", "canopy"),
/*  30 */   WIDE_WINDOW((byte)13, "wide window", "widewindow"),
/*  31 */   ARCHED_LEFT((byte)14, "left arch", "archleft"),
/*  32 */   ARCHED_RIGHT((byte)15, "right arch", "archright"),
/*  33 */   ARCHED_T((byte)16, "T arch", "archt"),
/*     */   
/*  35 */   SCAFFOLDING((byte)17, "scaffolding", "scaffolding"),
/*  36 */   FENCE((byte)0, "fence", ""),
/*  37 */   PARAPET((byte)0, "parapet", "parapet"),
/*  38 */   PALISADE((byte)0, "palisade", "palisade"),
/*  39 */   FENCE_WALL((byte)0, "stone wall", ""),
/*  40 */   GATE((byte)0, "gate", "gate"),
/*  41 */   FENCE_TALL((byte)0, "tall wall", ""),
/*  42 */   WOVEN((byte)0, "woven", ""),
/*  43 */   NO_WALL((byte)0, "missing wall", ""),
/*  44 */   FENCE_IRON_BARS((byte)0, "iron fence", ""),
/*  45 */   FENCE_IRON_BARS_GATE((byte)0, "iron fence gate", ""),
/*  46 */   FENCE_IRON_BARS_TALL((byte)0, "tall iron fence", ""),
/*  47 */   FENCE_IRON_BARS_TALL_GATE((byte)0, "tall iron fence gate", ""),
/*  48 */   ROPE_LOW((byte)0, "low rope fence", ""),
/*  49 */   ROPE_HIGH((byte)0, "tall rope fence", ""),
/*  50 */   GARDESGARD_LOW((byte)0, "low roundpole fence", ""),
/*  51 */   GARDESGARD_HIGH((byte)0, "tall roundpole fence", ""),
/*  52 */   GARDESGARD_GATE((byte)0, "roundpole fence gate", ""),
/*  53 */   CURB((byte)0, "curb", ""),
/*  54 */   HEDGE_LOW((byte)0, "", ""),
/*  55 */   HEDGE_MEDIUM((byte)0, "", ""),
/*  56 */   HEDGE_HIGH((byte)0, "", ""),
/*  57 */   MAGIC_FENCE((byte)0, "", ""),
/*  58 */   FLOWERBED((byte)0, "", ""),
/*  59 */   MEDIUM_CHAIN((byte)0, "", ""),
/*  60 */   SIEGWALL((byte)0, "", ""),
/*     */ 
/*     */ 
/*     */   
/*  64 */   FENCE_PLAN_WOODEN((byte)0, "", ""),
/*  65 */   FENCE_PLAN_WOODEN_GATE((byte)0, "", ""),
/*  66 */   FENCE_PLAN_PALISADE((byte)0, "", ""),
/*  67 */   FENCE_PLAN_PALISADE_GATE((byte)0, "", ""),
/*  68 */   FENCE_PLAN_STONEWALL((byte)0, "", ""),
/*  69 */   FENCE_PLAN_STONEWALL_HIGH((byte)0, "", ""),
/*  70 */   FENCE_PLAN_IRON_BARS((byte)0, "", ""),
/*  71 */   FENCE_PLAN_IRON_BARS_GATE((byte)0, "", ""),
/*  72 */   FENCE_PLAN_IRON_BARS_TALL((byte)0, "", ""),
/*  73 */   FENCE_PLAN_IRON_BARS_TALL_GATE((byte)0, "", ""),
/*  74 */   FENCE_PLAN_STONE_PARAPET((byte)0, "", ""),
/*  75 */   FENCE_PLAN_WOODEN_PARAPET((byte)0, "", ""),
/*  76 */   FENCE_PLAN_IRON_BARS_PARAPET((byte)0, "", ""),
/*  77 */   FENCE_PLAN_CRUDE((byte)0, "", ""),
/*  78 */   FENCE_PLAN_CRUDE_GATE((byte)0, "", ""),
/*  79 */   FENCE_PLAN_WOVEN((byte)0, "", ""),
/*  80 */   FENCE_PLAN_ROPE_LOW((byte)0, "", ""),
/*  81 */   FENCE_PLAN_ROPE_HIGH((byte)0, "", ""),
/*  82 */   FENCE_PLAN_CURB((byte)0, "", ""),
/*  83 */   FENCE_PLAN_GARDESGARD_LOW((byte)0, "", ""),
/*  84 */   FENCE_PLAN_GARDESGARD_HIGH((byte)0, "", ""),
/*  85 */   FENCE_PLAN_GARDESGARD_GATE((byte)0, "", ""),
/*  86 */   FENCE_PLAN_STONE_FENCE((byte)0, "", ""),
/*  87 */   FENCE_PLAN_MEDIUM_CHAIN((byte)0, "", ""),
/*  88 */   FENCE_PLAN_PORTCULLIS((byte)0, "", ""),
/*     */ 
/*     */ 
/*     */   
/*  92 */   HOUSE_PLAN_SOLID((byte)0, "", ""),
/*  93 */   HOUSE_PLAN_DOOR((byte)0, "", ""),
/*  94 */   HOUSE_PLAN_DOUBLE_DOOR((byte)0, "", ""),
/*  95 */   HOUSE_PLAN_WINDOW((byte)0, "", ""),
/*  96 */   HOUSE_PLAN_BARRED((byte)0, "", ""),
/*  97 */   HOUSE_PLAN_ORIEL((byte)0, "", ""),
/*  98 */   HOUSE_PLAN_ARCHED((byte)0, "", ""),
/*  99 */   HOUSE_PLAN_ARCH_LEFT((byte)0, "", ""),
/* 100 */   HOUSE_PLAN_ARCH_RIGHT((byte)0, "", ""),
/* 101 */   HOUSE_PLAN_ARCH_T((byte)0, "", ""),
/* 102 */   HOUSE_PLAN_PORTCULLIS((byte)0, "", ""),
/* 103 */   HOUSE_PLAN_NARROW_WINDOW((byte)0, "", ""),
/* 104 */   HOUSE_PLAN_BALCONY((byte)0, "", ""),
/* 105 */   HOUSE_PLAN_JETTY((byte)0, "", ""),
/* 106 */   HOUSE_PLAN_CANOPY((byte)0, "", ""),
/* 107 */   PLAN(127, "plan", "plan");
/*     */   
/*     */   public final byte value;
/*     */   private static HashMap<String, StructureTypeEnum> lookupMap;
/*     */   
/*     */   static {
/* 113 */     lookupMap = null;
/*     */   }
/*     */   StructureTypeEnum(byte _value, String _typeName, String _modelShortName) {
/* 116 */     this.value = _value;
/* 117 */     this.typeName = _typeName;
/* 118 */     this.modelShortName = _modelShortName;
/*     */   }
/*     */   
/*     */   public static StructureTypeEnum getTypeByINDEX(int id) {
/* 122 */     if (id >= 0 && id <= (values()).length) {
/* 123 */       return values()[id];
/*     */     }
/* 125 */     if (id != 127 && id != -1) {
/* 126 */       Logger.getGlobal().warning("Value not a valid array position: " + id + " RETURNING PLAN(VAL=40)!");
/*     */     }
/* 128 */     return PLAN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Optional<StructureTypeEnum> lookup(String name) {
/* 133 */     Optional<StructureTypeEnum> optional = Optional.empty();
/*     */     
/* 135 */     if (lookupMap == null) {
/*     */       
/* 137 */       lookupMap = new HashMap<>();
/*     */       
/* 139 */       for (StructureTypeEnum stt : values())
/*     */       {
/* 141 */         lookupMap.put(stt.name(), stt);
/*     */       }
/*     */     } 
/*     */     
/* 145 */     StructureTypeEnum temp = lookupMap.get(name);
/*     */     
/* 147 */     if (temp != null) {
/* 148 */       optional = Optional.of(temp);
/*     */     }
/* 150 */     else if (Logger.getGlobal().isLoggable(Level.FINE)) {
/* 151 */       Logger.getGlobal().fine(name + " not found in lookup!");
/*     */     } 
/* 153 */     return optional;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\StructureTypeEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */