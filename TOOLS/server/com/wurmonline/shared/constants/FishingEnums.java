/*     */ package com.wurmonline.shared.constants;
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
/*     */ public class FishingEnums
/*     */ {
/*     */   public static final int FISH_SPOT_RADIUS = 5;
/*     */   public static final int FISH_SPOT_ZONE_SIZE = 128;
/*     */   public static final byte FISH_TYPE_NONE = 0;
/*     */   public static final byte FISH_TYPE_ROACH = 1;
/*     */   public static final byte FISH_TYPE_PERCH = 2;
/*     */   public static final byte FISH_TYPE_TROUT = 3;
/*     */   public static final byte FISH_TYPE_PIKE = 4;
/*     */   public static final byte FISH_TYPE_CATFISH = 5;
/*     */   public static final byte FISH_TYPE_SNOOK = 6;
/*     */   public static final byte FISH_TYPE_HERRING = 7;
/*     */   public static final byte FISH_TYPE_CARP = 8;
/*     */   public static final byte FISH_TYPE_BASS = 9;
/*     */   public static final byte FISH_TYPE_SALMON = 10;
/*     */   public static final byte FISH_TYPE_OCTOPUS = 11;
/*     */   public static final byte FISH_TYPE_MARLIN = 12;
/*     */   public static final byte FISH_TYPE_BLUESHARK = 13;
/*     */   public static final byte FISH_TYPE_DORADO = 14;
/*     */   public static final byte FISH_TYPE_SAILFISH = 15;
/*     */   public static final byte FISH_TYPE_WHITESHARK = 16;
/*     */   public static final byte FISH_TYPE_TUNA = 17;
/*     */   public static final byte FISH_TYPE_MINNOW = 18;
/*     */   public static final byte FISH_TYPE_LOACH = 19;
/*     */   public static final byte FISH_TYPE_WURMFISH = 20;
/*     */   public static final byte FISH_TYPE_SARDINE = 21;
/*     */   public static final byte FISH_TYPE_CLAM = 22;
/*     */   public static final byte ROD_TYPE_FISHING_POLE = 0;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_BASIC = 1;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_FINE = 2;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_DEEP_WATER = 3;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_DEEP_SEA = 4;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_BASIC_WITH_LINE = 5;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_FINE_WITH_LINE = 6;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_DEEP_WATER_WITH_LINE = 7;
/*     */   public static final byte ROD_TYPE_FISHING_ROD_DEEP_SEA_WITH_LINE = 8;
/*     */   public static final byte FLOAT_TYPE_NONE = 0;
/*     */   public static final byte FLOAT_TYPE_FEATHER = 1;
/*     */   public static final byte FLOAT_TYPE_TWIG = 2;
/*     */   public static final byte FLOAT_TYPE_MOSS = 3;
/*     */   public static final byte FLOAT_TYPE_BARK = 4;
/*     */   public static final byte BAIT_TYPE_NONE = 0;
/*     */   public static final byte BAIT_TYPE_FLY = 1;
/*     */   public static final byte BAIT_TYPE_CHEESE = 2;
/*     */   public static final byte BAIT_TYPE_DOUGH = 3;
/*     */   public static final byte BAIT_TYPE_WURM = 4;
/*     */   public static final byte BAIT_TYPE_SARDINE = 5;
/*     */   public static final byte BAIT_TYPE_ROACH = 6;
/*     */   public static final byte BAIT_TYPE_PERCH = 7;
/*     */   public static final byte BAIT_TYPE_MINNOW = 8;
/*     */   public static final byte BAIT_TYPE_FISH_BAIT = 9;
/*     */   public static final byte BAIT_TYPE_GRUB = 10;
/*     */   public static final byte BAIT_TYPE_WHEAT = 11;
/*     */   public static final byte BAIT_TYPE_CORN = 12;
/*     */   public static final byte REEL_TYPE_NONE = 0;
/*     */   public static final byte REEL_TYPE_LIGHT = 1;
/*     */   public static final byte REEL_TYPE_MEDIUM = 2;
/*     */   public static final byte REEL_TYPE_DEEP_WATER = 3;
/*     */   public static final byte REEL_TYPE_PROFESSIONAL = 4;
/*     */   public static final byte HOOK_TYPE_NONE = 0;
/*     */   public static final byte HOOK_TYPE_WOOD = 1;
/*     */   public static final byte HOOK_TYPE_METAL = 2;
/*     */   public static final byte HOOK_TYPE_BONE = 3;
/*     */   
/*     */   public enum FishType
/*     */   {
/* 103 */     NONE((byte)0, ""),
/* 104 */     ROACH((byte)1, "model.fish.roach."),
/* 105 */     PERCH((byte)2, "model.fish.perch."),
/* 106 */     TROUT((byte)3, "model.fish.trout."),
/* 107 */     PIKE((byte)4, "model.fish.pike."),
/* 108 */     CATFISH((byte)5, "model.fish.catfish."),
/* 109 */     SNOOK((byte)6, "model.fish.snook."),
/* 110 */     HERRING((byte)7, "model.fish.herring."),
/* 111 */     CARP((byte)8, "model.fish.carp."),
/* 112 */     BASS((byte)9, "model.fish.bass."),
/* 113 */     SALMON((byte)10, "model.fish.salmon."),
/* 114 */     OCTOPUS((byte)11, "model.fish.octopus."),
/* 115 */     MARLIN((byte)12, "model.fish.marlin."),
/* 116 */     BLUESHARK((byte)13, "model.fish.blueshark."),
/* 117 */     DORADO((byte)14, "model.fish.dorado."),
/* 118 */     SAILFISH((byte)15, "model.fish.sailfish."),
/* 119 */     WHITESHARK((byte)16, "model.fish.whiteshark."),
/* 120 */     TUNA((byte)17, "model.fish.tuna."),
/* 121 */     MINNOW((byte)18, "model.fish.minnow."),
/* 122 */     LOACH((byte)19, "model.fish.loach."),
/* 123 */     WURMFISH((byte)20, "model.fish.wurmfish."),
/* 124 */     SARDINE((byte)21, "model.fish.sardine."),
/* 125 */     CLAM((byte)22, "model.fish.clam.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String modelName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     FishType(byte typeId, String modelName) {
/*     */       this.typeId = typeId;
/*     */       this.modelName = modelName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     private static final FishType[] types = values(); public int getTypeId() {
/*     */       return this.typeId;
/*     */     } public String getModelName() {
/*     */       return this.modelName;
/*     */     } public static final int getLength() {
/* 155 */       return types.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public static FishType fromInt(int id) {
/* 160 */       if (id >= getLength())
/* 161 */         return types[0]; 
/* 162 */       return types[id & 0xFF];
/*     */     } static {
/*     */     
/*     */     }
/*     */     public String getModelName(byte id) {
/* 167 */       return fromInt(id).getModelName();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum RodType
/*     */   {
/* 178 */     FISHING_POLE((byte)0, "model.fish.pole.", (short)786),
/* 179 */     FISHING_ROD_BASIC((byte)1, "model.fish.rod.basic.", (short)866),
/* 180 */     FISHING_ROD_FINE((byte)2, "model.fish.rod.fine.", (short)866),
/* 181 */     FISHING_ROD_DEEP_WATER((byte)3, "model.fish.rod.water.", (short)866),
/* 182 */     FISHING_ROD_DEEP_SEA((byte)4, "model.fish.rod.sea.", (short)866),
/* 183 */     FISHING_ROD_BASIC_WITH_LINE((byte)5, "model.fish.rod.basic.", (short)886),
/* 184 */     FISHING_ROD_FINE_WITH_LINE((byte)6, "model.fish.rod.fine.", (short)886),
/* 185 */     FISHING_ROD_DEEP_WATER_WITH_LINE((byte)7, "model.fish.rod.water.", (short)886),
/* 186 */     FISHING_ROD_DEEP_SEA_WITH_LINE((byte)8, "model.fish.rod.sea.", (short)886);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String modelName;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final short icon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     RodType(byte id, String modelName, short icon) {
/*     */       this.typeId = id;
/*     */       this.modelName = modelName;
/*     */       this.icon = icon;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     private static final RodType[] types = values();
/*     */     public byte getTypeId() {
/*     */       return this.typeId;
/*     */     }
/*     */     public static final int getLength() {
/* 223 */       return types.length;
/*     */     }
/*     */     public String getModelName() {
/*     */       return this.modelName;
/*     */     }
/* 228 */     public static RodType fromInt(int id) { if (id >= getLength())
/* 229 */         return types[0]; 
/* 230 */       return types[id & 0xFF]; }
/*     */      public int getIcon() {
/*     */       return this.icon;
/*     */     }
/*     */     public String getModelName(byte id) {
/* 235 */       return fromInt(id).getModelName();
/*     */     }
/*     */ 
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   public enum FloatType
/*     */   {
/* 246 */     NONE((byte)0, ""),
/* 247 */     FEATHER((byte)1, "model.float.feather."),
/* 248 */     TWIG((byte)2, "model.float.twig."),
/* 249 */     MOSS((byte)3, "model.float.moss."),
/* 250 */     BARK((byte)4, "model.float.bark.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String modelName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     FloatType(byte id, String modelName) {
/*     */       this.typeId = id;
/*     */       this.modelName = modelName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 275 */     private static final FloatType[] types = values();
/*     */     public byte getTypeId() {
/*     */       return this.typeId;
/*     */     }
/*     */     public static final int getLength() {
/* 280 */       return types.length;
/*     */     } public String getModelName() {
/*     */       return this.modelName;
/*     */     }
/*     */     public static FloatType fromInt(int id) {
/* 285 */       if (id >= getLength())
/* 286 */         return types[0]; 
/* 287 */       return types[id & 0xFF];
/*     */     } static {
/*     */     
/*     */     }
/*     */     public String getModelName(byte id) {
/* 292 */       return fromInt(id).getModelName();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum BaitType
/*     */   {
/* 303 */     NONE((byte)0, ""),
/* 304 */     FLY((byte)1, "model.bait.fly."),
/* 305 */     CHEESE((byte)2, "model.bait.cheese."),
/* 306 */     DOUGH((byte)3, "model.bait.dough."),
/* 307 */     WURM((byte)4, "model.bait.wurm."),
/* 308 */     SARDINE((byte)5, "model.fish.sardine."),
/* 309 */     ROACH((byte)6, "model.fish.roach."),
/* 310 */     PERCH((byte)7, "model.fish.perch."),
/* 311 */     MINNOW((byte)8, "model.fish.cave.minnow."),
/* 312 */     FISH_BAIT((byte)9, "model.bait.fish."),
/* 313 */     GRUB((byte)10, "model.bait.grub."),
/* 314 */     WHEAT((byte)11, "model.bait.wheat."),
/* 315 */     CORN((byte)12, "model.bait.corn.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String modelName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     BaitType(byte id, String modelName) {
/*     */       this.typeId = id;
/*     */       this.modelName = modelName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     private static final BaitType[] types = values();
/*     */     public byte getTypeId() {
/*     */       return this.typeId;
/*     */     }
/*     */     public static final int getLength() {
/* 345 */       return types.length;
/*     */     } public String getModelName() {
/*     */       return this.modelName;
/*     */     }
/*     */     public static BaitType fromInt(int id) {
/* 350 */       if (id >= getLength())
/* 351 */         return types[0]; 
/* 352 */       return types[id & 0xFF];
/*     */     } static {
/*     */     
/*     */     }
/*     */     public String getModelName(byte id) {
/* 357 */       return fromInt(id).getModelName();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum ReelType
/*     */   {
/* 368 */     NONE((byte)0, ""),
/* 369 */     LIGHT((byte)1, "model.fishingreel.light."),
/* 370 */     MEDIUM((byte)2, "model.fishingreel.medium."),
/* 371 */     DEEP_WATER((byte)3, "model.fishingreel.deepwater."),
/* 372 */     PROFESSIONAL((byte)4, "model.fishingreel.professional.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String modelName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ReelType(byte id, String modelName) {
/*     */       this.typeId = id;
/*     */       this.modelName = modelName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 397 */     private static final ReelType[] types = values();
/*     */     public byte getTypeId() {
/*     */       return this.typeId;
/*     */     }
/*     */     public static final int getLength() {
/* 402 */       return types.length;
/*     */     } public String getModelName() {
/*     */       return this.modelName;
/*     */     }
/*     */     public static ReelType fromInt(int id) {
/* 407 */       if (id >= getLength())
/* 408 */         return types[0]; 
/* 409 */       return types[id & 0xFF];
/*     */     } static {
/*     */     
/*     */     }
/*     */     public String getModelName(byte id) {
/* 414 */       return fromInt(id).getModelName();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum HookType
/*     */   {
/* 425 */     NONE((byte)0, ""),
/* 426 */     WOOD((byte)1, "model.tool.fish.hook."),
/* 427 */     METAL((byte)2, "model.tool.fish.hook."),
/* 428 */     BONE((byte)3, "model.tool.fish.hook.");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final byte typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String modelName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     HookType(byte id, String modelName) {
/*     */       this.typeId = id;
/*     */       this.modelName = modelName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 453 */     private static final HookType[] types = values();
/*     */     public byte getTypeId() {
/*     */       return this.typeId;
/*     */     }
/*     */     public static final int getLength() {
/* 458 */       return types.length;
/*     */     } public String getModelName() {
/*     */       return this.modelName;
/*     */     }
/*     */     public static HookType fromInt(int id) {
/* 463 */       if (id >= getLength())
/* 464 */         return types[0]; 
/* 465 */       return types[id & 0xFF];
/*     */     } static {
/*     */     
/*     */     }
/*     */     public String getModelName(byte id) {
/* 470 */       return fromInt(id).getModelName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\FishingEnums.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */