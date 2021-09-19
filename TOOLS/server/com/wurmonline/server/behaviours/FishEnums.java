/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.server.Point;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.WurmCalendar;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTemplate;
/*      */ import com.wurmonline.server.items.ItemTemplateFactory;
/*      */ import com.wurmonline.server.items.NoSuchTemplateException;
/*      */ import com.wurmonline.server.zones.NoSuchZoneException;
/*      */ import com.wurmonline.server.zones.WaterType;
/*      */ import com.wurmonline.server.zones.Zones;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FishEnums
/*      */ {
/*   51 */   private static final Logger logger = Logger.getLogger(FishEnums.class.getName());
/*      */   
/*      */   private static final byte testTypeId = -1;
/*      */   public static final int MIN_DEPTH_SPECIAL_FISH = -100;
/*      */   
/*      */   public enum FeedHeight
/*      */   {
/*   58 */     NONE((byte)0),
/*   59 */     TOP((byte)1),
/*   60 */     BOTTOM((byte)2),
/*   61 */     ANY((byte)3),
/*   62 */     TIME((byte)4);
/*      */     
/*      */     private final byte typeId;
/*      */ 
/*      */     
/*      */     FeedHeight(byte id) {
/*   68 */       this.typeId = id;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTypeId() {
/*   73 */       return this.typeId;
/*      */     }
/*      */   }
/*      */   
/*      */   public enum TimeOfDay
/*      */   {
/*   79 */     MORNING((byte)0),
/*   80 */     AFTERNOON((byte)1),
/*   81 */     EVENING((byte)2),
/*   82 */     NIGHT((byte)3);
/*      */     
/*      */     private final byte typeId;
/*      */ 
/*      */     
/*      */     TimeOfDay(byte id) {
/*   88 */       this.typeId = id;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTypeId() {
/*   93 */       return this.typeId;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final TimeOfDay getTimeOfDay() {
/*  102 */       if (WurmCalendar.getHour() < 4 || (WurmCalendar.getHour() == 4 && WurmCalendar.getMinute() <= 30))
/*  103 */         return NIGHT; 
/*  104 */       if (WurmCalendar.getHour() < 10 || (WurmCalendar.getHour() == 10 && WurmCalendar.getMinute() <= 30))
/*  105 */         return MORNING; 
/*  106 */       if (WurmCalendar.getHour() < 16 || (WurmCalendar.getHour() == 16 && WurmCalendar.getMinute() <= 30))
/*  107 */         return AFTERNOON; 
/*  108 */       if (WurmCalendar.getHour() < 22 || (WurmCalendar.getHour() == 22 && WurmCalendar.getMinute() <= 30))
/*  109 */         return EVENING; 
/*  110 */       return NIGHT;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ReelType
/*      */   {
/*  120 */     NONE((byte)0, 0, 1347),
/*  121 */     LIGHT((byte)1, 1372, 1348),
/*  122 */     MEDIUM((byte)2, 1373, 1349),
/*  123 */     DEEP_WATER((byte)3, 1374, 1350),
/*  124 */     PROFESSIONAL((byte)4, 1375, 1351);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final byte typeId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int templateId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int associatedLine;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final ReelType[] types;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  158 */     private static final Map<Integer, ReelType> byTemplateId = new HashMap<>(); ReelType(byte id, int templateId, int associatedLine) { this.typeId = id; this.templateId = templateId; this.associatedLine = associatedLine; } public byte getTypeId() { return this.typeId; }
/*      */     public int getTemplateId() { return this.templateId; }
/*      */     public int getAssociatedLineTemplateId() { return this.associatedLine; }
/*  161 */     static { types = values();
/*  162 */       for (ReelType rt : types)
/*      */       {
/*  164 */         byTemplateId.put(Integer.valueOf(rt.getTemplateId()), rt);
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int getLength() {
/*  170 */       return types.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static ReelType fromInt(int id) {
/*  180 */       if (id >= getLength())
/*  181 */         return types[0]; 
/*  182 */       return types[id & 0xFF];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static ReelType fromItem(@Nullable Item reel) {
/*  192 */       if (reel == null) {
/*  193 */         return NONE;
/*      */       }
/*  195 */       ReelType reelType = byTemplateId.get(Integer.valueOf(reel.getTemplateId()));
/*      */       
/*  197 */       if (reelType == null) {
/*  198 */         return NONE;
/*      */       }
/*  200 */       return reelType;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum FloatType
/*      */   {
/*  210 */     NONE((byte)0, 0),
/*  211 */     FEATHER((byte)1, 1352),
/*  212 */     TWIG((byte)2, 1353),
/*  213 */     MOSS((byte)3, 1354),
/*  214 */     BARK((byte)4, 1355);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final byte typeId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int templateId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final FloatType[] types;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  237 */     private static final Map<Integer, FloatType> byTemplateId = new HashMap<>(); FloatType(byte id, int templateId) { this.typeId = id; this.templateId = templateId; }
/*      */     public byte getTypeId() { return this.typeId; }
/*      */     public int getTemplateId() { return this.templateId; }
/*  240 */     static { types = values();
/*  241 */       for (FloatType ft : types)
/*      */       {
/*  243 */         byTemplateId.put(Integer.valueOf(ft.getTemplateId()), ft);
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int getLength() {
/*  249 */       return types.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static FloatType fromInt(int id) {
/*  259 */       if (id >= getLength())
/*  260 */         return types[0]; 
/*  261 */       return types[id & 0xFF];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static FloatType fromItem(@Nullable Item afloat) {
/*  271 */       if (afloat == null) {
/*  272 */         return NONE;
/*      */       }
/*  274 */       FloatType floatType = byTemplateId.get(Integer.valueOf(afloat.getTemplateId()));
/*      */       
/*  276 */       if (floatType == null) {
/*  277 */         return NONE;
/*      */       }
/*  279 */       return floatType;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum HookType
/*      */   {
/*  289 */     NONE((byte)0, 0),
/*  290 */     WOOD((byte)1, 1356),
/*  291 */     METAL((byte)2, 1357),
/*  292 */     BONE((byte)3, 1358);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final byte typeId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int templateId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final HookType[] types;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  315 */     private static final Map<Integer, HookType> byTemplateId = new HashMap<>(); HookType(byte id, int templateId) { this.typeId = id; this.templateId = templateId; }
/*      */     public byte getTypeId() { return this.typeId; }
/*      */     public int getTemplateId() { return this.templateId; }
/*  318 */     static { types = values();
/*  319 */       for (HookType ht : types)
/*      */       {
/*  321 */         byTemplateId.put(Integer.valueOf(ht.getTemplateId()), ht);
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int getLength() {
/*  327 */       return types.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static HookType fromInt(int id) {
/*  337 */       if (id >= getLength())
/*  338 */         return types[0]; 
/*  339 */       return types[id & 0xFF];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static HookType fromItem(@Nullable Item hook) {
/*  349 */       if (hook == null) {
/*  350 */         return NONE;
/*      */       }
/*  352 */       for (HookType ft : types) {
/*      */         
/*  354 */         if (ft.getTemplateId() == hook.getTemplateId())
/*  355 */           return ft; 
/*      */       } 
/*  357 */       return NONE;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum BaitType
/*      */   {
/*  367 */     NONE((byte)0, -1, 1.0F),
/*  368 */     FLY((byte)1, 1359, 0.1F),
/*  369 */     CHEESE((byte)2, 1360, 1.5F),
/*  370 */     DOUGH((byte)3, 1361, 2.0F),
/*  371 */     WURM((byte)4, 1362, 1.0F),
/*  372 */     SARDINE((byte)5, 1337, 2.2F),
/*  373 */     ROACH((byte)6, 162, 2.8F),
/*  374 */     PERCH((byte)7, 163, 3.0F),
/*  375 */     MINNOW((byte)8, 1338, 2.5F),
/*  376 */     FISH_BAIT((byte)9, 1363, 0.2F),
/*  377 */     GRUB((byte)10, 1364, 0.5F),
/*  378 */     WHEAT((byte)11, 1365, 0.2F),
/*  379 */     CORN((byte)12, 1366, 0.1F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final byte typeId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int templateId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final float crumbles;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final BaitType[] types;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  409 */     private static final Map<Integer, BaitType> byTemplateId = new HashMap<>(); BaitType(byte id, int templateId, float crumbles) { this.typeId = id; this.templateId = templateId; this.crumbles = crumbles; } public byte getTypeId() { return this.typeId; }
/*      */     public int getTemplateId() { return this.templateId; }
/*      */     public float getCrumbleFactor() { return this.crumbles; }
/*  412 */     static { types = values();
/*  413 */       for (BaitType bt : types)
/*      */       {
/*  415 */         byTemplateId.put(Integer.valueOf(bt.getTemplateId()), bt);
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int getLength() {
/*  421 */       return types.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static BaitType fromInt(int id) {
/*  431 */       if (id >= getLength())
/*  432 */         return types[0]; 
/*  433 */       return types[id & 0xFF];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static BaitType fromItem(@Nullable Item bait) {
/*  443 */       if (bait == null) {
/*  444 */         return NONE;
/*      */       }
/*  446 */       BaitType baitType = byTemplateId.get(Integer.valueOf(bait.getTemplateId()));
/*      */       
/*  448 */       if (baitType == null) {
/*  449 */         return NONE;
/*      */       }
/*  451 */       return baitType;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum FishData
/*      */   {
/*  462 */     NONE((byte)0, "unknown", 0, true, FishEnums.FeedHeight.NONE, 0, 0, false, 0, 0, "model.creature.fish", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new byte[0], new int[] { 1, 1, 1, 1 }, (byte[])new int[0], new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  468 */     ROACH((byte)1, "roach", 162, true, FishEnums.FeedHeight.BOTTOM, 0, 30, false, 1, 30, "model.creature.fish.roach", 1.5F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new byte[] { 2, 3, 7 }, new int[] { 4, 4, 3, 1 }, (byte[])new int[] { 705, 1343, 1344, 1372 }, new int[] { 8, 5, 1, 5, 8, 1, 0, 0, 1, 5, 10, 5, 5
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  474 */     PERCH((byte)2, "perch", 163, true, FishEnums.FeedHeight.ANY, 0, 40, false, 1, 40, "model.creature.fish.perch", 1.5F, 1.0F, 2.0F, 2.0F, 2.0F, 2.0F, new byte[] { 2, 3, 7 }, new int[] { 4, 3, 3, 2 }, (byte[])new int[] { 705, 1343, 1344, 1372 }, new int[] { 8, 5, 5, 10, 5, 5, 1, 0, 5, 5, 8, 5, 5
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  480 */     TROUT((byte)3, "brook trout", 165, true, FishEnums.FeedHeight.BOTTOM, 60, 150, false, 2, 50, "model.creature.fish.trout", 1.0F, 1.0F, 4.0F, 4.0F, 4.0F, 4.0F, new byte[] { 2, 3 }, new int[] { 4, 2, 4, 2 }, (byte[])new int[] { 705, 1373, 1374 }, new int[] { 1, 10, 1, 5, 5, 5, 5, 5, 5, 1, 5, 1, 5
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  486 */     PIKE((byte)4, "pike", 157, true, FishEnums.FeedHeight.BOTTOM, 10, 100, false, 4, 50, "model.creature.fish.pike", 0.74F, 1.0F, 8.0F, 10.0F, 5.0F, 2.0F, new byte[] { 3 }, new int[] { 3, 2, 4, 4 }, (byte[])new int[] { 1372, 1373 }, new int[] { 1, 5, 1, 1, 1, 5, 5, 5, 5, 1, 1, 10, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  492 */     CATFISH((byte)5, "catfish", 160, true, FishEnums.FeedHeight.BOTTOM, 20, 100, false, 4, 50, "model.creature.fish.catfish", 0.85F, 1.0F, 20.0F, 22.0F, 13.0F, 5.0F, new byte[] { 3, 4 }, new int[] { 4, 4, 3, 2 }, (byte[])new int[] { 705, 1372, 1373, 1374 }, new int[] { 1, 5, 5, 5, 5, 5, 7, 5, 5, 10, 5, 5, 5
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  498 */     SNOOK((byte)6, "snook", 161, true, FishEnums.FeedHeight.TIME, 10, 250, false, 5, 50, "model.creature.fish.snook", 0.85F, 1.0F, 15.0F, 17.0F, 14.0F, 8.0F, new byte[] { 4 }, new int[] { 4, 2, 3, 4 }, (byte[])new int[] { 1372, 1373, 1374 }, new int[] { 0, 1, 1, 1, 1, 1, 1, 5, 5, 5, 1, 1, 10
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  504 */     HERRING((byte)7, "herring", 159, true, FishEnums.FeedHeight.TIME, 10, 150, false, 1, 50, "model.creature.fish.herring", 2.35F, 1.0F, 10.0F, 15.0F, 10.0F, 7.0F, new byte[] { 3, 4 }, new int[] { 2, 3, 4, 2 }, (byte[])new int[] { 1372, 1373 }, new int[] { 1, 5, 5, 5, 1, 5, 5, 1, 1, 1, 10, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  510 */     CARP((byte)8, "carp", 164, true, FishEnums.FeedHeight.ANY, 5, 200, false, 3, 50, "model.creature.fish.carp", 0.5F, 1.0F, 13.0F, 18.0F, 11.0F, 8.0F, new byte[] { 2, 3 }, new int[] { 4, 4, 3, 3 }, (byte[])new int[] { 705, 1344, 1372, 1373, 1374, 1375 }, new int[] { 10, 5, 5, 5, 5, 1, 1, 1, 1, 5, 5, 5, 5
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  517 */     BASS((byte)9, "smallmouth bass", 158, true, FishEnums.FeedHeight.BOTTOM, 0, 60, false, 2, 50, "model.creature.fish.bass", 1.03F, 1.0F, 15.0F, 21.0F, 14.0F, 11.0F, new byte[] { 3, 4 }, new int[] { 2, 2, 4, 4 }, (byte[])new int[] { 1344, 1372, 1373 }, new int[] { 1, 1, 1, 1, 10, 1, 1, 1, 1, 5, 1, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  523 */     SALMON((byte)10, "salmon", 1335, true, FishEnums.FeedHeight.TIME, 0, 75, false, 3, 50, "model.creature.fish.salmon", 1.0F, 1.0F, 25.0F, 30.0F, 30.0F, 15.0F, new byte[] { 3, 4 }, new int[] { 4, 1, 4, 2 }, (byte[])new int[] { 705, 1372, 1373 }, new int[] { 1, 10, 1, 1, 5, 5, 5, 5, 5, 1, 5, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  529 */     OCTOPUS((byte)11, "octopus", 572, true, FishEnums.FeedHeight.ANY, 200, 800, true, 3, 50, "model.creature.fish.octopus.black", 1.0F, 1.0F, 30.0F, 40.0F, 45.0F, 14.0F, new byte[] { 4 }, new int[] { 2, 4, 1, 4 }, (byte[])new int[] { 1374, 1375 }, new int[] { 0, 0, 10, 1, 1, 5, 1, 1, 1, 5, 1, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  535 */     MARLIN((byte)12, "marlin", 569, true, FishEnums.FeedHeight.TOP, 250, 1000, true, 6, 50, "model.creature.fish.marlin", 0.343F, 1.0F, 50.0F, 50.0F, 45.0F, 18.0F, new byte[] { 4 }, new int[] { 4, 2, 4, 3 }, (byte[])new int[] { 1375 }, new int[] { 0, 0, 1, 1, 1, 5, 5, 5, 10, 0, 1, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  541 */     BLUESHARK((byte)13, "blue shark", 570, true, FishEnums.FeedHeight.ANY, 250, 1000, true, 5, 50, "model.creature.fish.blueshark", 1.0F, 1.0F, 45.0F, 50.0F, 45.0F, 14.0F, new byte[] { 4 }, new int[] { 4, 3, 4, 2 }, (byte[])new int[] { 1375 }, new int[] { 0, 0, 1, 1, 1, 10, 5, 5, 5, 0, 1, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  547 */     DORADO((byte)14, "dorado", 574, true, FishEnums.FeedHeight.TOP, 150, 600, true, 4, 50, "model.creature.fish.dorado", 1.0F, 1.0F, 30.0F, 50.0F, 45.0F, 13.0F, new byte[] { 4 }, new int[] { 4, 2, 4, 3 }, (byte[])new int[] { 1374, 1375 }, new int[] { 0, 1, 1, 1, 10, 5, 5, 5, 5, 0, 5, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  553 */     SAILFISH((byte)15, "sailfish", 573, true, FishEnums.FeedHeight.TOP, 200, 800, true, 4, 50, "model.creature.fish.sailfish", 1.0F, 1.0F, 40.0F, 50.0F, 45.0F, 15.0F, new byte[] { 4 }, new int[] { 4, 2, 4, 3 }, (byte[])new int[] { 1375 }, new int[] { 0, 0, 0, 1, 1, 5, 5, 5, 5, 0, 10, 0, 0
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  559 */     WHITESHARK((byte)16, "white shark", 571, true, FishEnums.FeedHeight.ANY, 150, 1000, true, 5, 50, "model.creature.fish.whiteshark", 1.0F, 1.0F, 42.0F, 50.0F, 45.0F, 14.0F, new byte[] { 4 }, new int[] { 4, 3, 4, 2 }, (byte[])new int[] { 1375 }, new int[] { 0, 0, 1, 1, 1, 5, 8, 10, 5, 0, 1, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  565 */     TUNA((byte)17, "tuna", 575, true, FishEnums.FeedHeight.TOP, 150, 600, true, 2, 50, "model.creature.fish.tuna", 1.0F, 1.0F, 40.0F, 50.0F, 45.0F, 20.0F, new byte[] { 4 }, new int[] { 4, 2, 4, 3 }, (byte[])new int[] { 1374, 1375 }, new int[] { 0, 1, 1, 5, 1, 5, 10, 5, 5, 0, 1, 0, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  571 */     MINNOW((byte)18, "minnow", 1338, false, FishEnums.FeedHeight.ANY, 0, 20, false, 1, 10, "model.creature.fish.minnow", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new byte[] { 2, 3, 7 }, new int[] { 4, 2, 4, 1 }, (byte[])new int[] { 1344, 1343, 1372 }, new int[] { 5, 10, 1, 1, 1, 0, 0, 0, 0, 1, 5, 1, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  577 */     LOACH((byte)19, "loach", 1339, false, FishEnums.FeedHeight.ANY, 10, 50, false, 2, 40, "model.creature.fish.loach", 1.0F, 1.0F, 5.0F, 7.0F, 3.0F, 2.0F, new byte[] { 2, 3 }, new int[] { 1, 4, 2, 3 }, (byte[])new int[] { 705, 1344, 1372, 1373 }, new int[] { 5, 5, 1, 1, 5, 1, 0, 0, 1, 5, 1, 1, 10
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  583 */     WURMFISH((byte)20, "wurmfish", 1340, false, FishEnums.FeedHeight.BOTTOM, 40, 1000, false, 3, 50, "model.creature.fish.wurmfish", 1.0F, 1.0F, 21.0F, 33.0F, 11.0F, 4.0F, new byte[] { 2, 3 }, new int[] { 2, 3, 1, 4 }, (byte[])new int[] { 705, 1344, 1372, 1373 }, new int[] { 1, 5, 1, 1, 5, 5, 5, 1, 5, 5, 1, 10, 1
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  589 */     SARDINE((byte)21, "sardine", 1337, true, FishEnums.FeedHeight.NONE, 0, 20, false, 1, 10, "model.creature.fish.sardine", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new byte[] { 2, 1, 3, 7 }, new int[] { 4, 3, 4, 2 }, (byte[])new int[] { 1343 }, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  595 */     CLAM((byte)22, "clam", 1394, true, FishEnums.FeedHeight.ANY, 0, 0, false, 1, 0, "model.creature.fish.clam", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, new byte[0], new int[] { 1, 1, 1, 1 }, (byte[])new int[0], new int[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  611 */     private ItemTemplate template = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean inWater = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean inPond = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean inLake = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean inSea = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean inShallows = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean useFishingNet = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean useSpear = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean useReelBasic = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean useReelFine = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean useReelWater = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean useReelProfessional = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final byte typeId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int templateId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean onSurface;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final FishEnums.FeedHeight feedHeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int minDepth;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int maxDepth;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean isSpecialFish;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int damageMod;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int minWeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final String modelName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final float scale;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final float baseSpeed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final float bodyStrength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final float bodyStamina;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final float bodyControl;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final float mindSpeed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean useFishingPole;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int[] feeds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final int[] baits;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final FishData[] types;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1891 */     private static final Map<Integer, FishData> byTemplateId = new HashMap<>(); FishData(byte typeId, String name, int templateId, boolean onSurface, FishEnums.FeedHeight feedHeight, int minDepth, int maxDepth, boolean specialFish, int damageMod, int minWeight, String modelName, float scale, float baseSpeed, float bodyStrength, float bodyStamina, float bodyControl, float mindSpeed, byte[] depths, int[] feeds, int[] reels, int[] baits) { this.typeId = typeId; this.name = name; this.templateId = templateId; this.onSurface = onSurface; this.feedHeight = feedHeight; this.minDepth = minDepth; this.maxDepth = maxDepth; this.isSpecialFish = specialFish; this.damageMod = damageMod; this.minWeight = minWeight; this.modelName = modelName; this.scale = scale; this.baseSpeed = baseSpeed; this.bodyStrength = bodyStrength; this.bodyStamina = bodyStamina; this.bodyControl = bodyControl; this.mindSpeed = mindSpeed; assignDepths(depths); this.feeds = feeds; if (typeId > 0) assignReels(reels);  this.baits = baits; } public int getTypeId() { return this.typeId; } public String getName() { return this.name; } public int getTemplateId() { return this.templateId; } public boolean inCave() { return !this.onSurface; } public boolean onSurface() { return this.onSurface; } public boolean inWater() { return this.inWater; } public boolean inPond() { return this.inPond; } public boolean inLake() { return this.inLake; } public boolean inSea() { return this.inSea; } public boolean inShallows() { return this.inShallows; } public FishEnums.FeedHeight getFeedHeight() { return this.feedHeight; } public int getMinDepth() { return this.minDepth; } public int getMaxDepth() { return this.maxDepth; } public String getModelName() { return this.modelName; } public float getScaleMod() { return 1.0F / this.scale; } public float getBaseSpeed() { return this.baseSpeed; } public boolean isSpecialFish() { return this.isSpecialFish; } public float getBodyStrength() { return this.bodyStrength; } public float getBodyStamina() { return this.bodyStamina; } public float getBodyControl() { return this.bodyControl; } public float getMindSpeed() { return this.mindSpeed; } public int getDamageMod() { return this.damageMod; }
/*      */     public int getMinWeight() { return this.minWeight; }
/*      */     public boolean useFishingPole() { return this.useFishingPole; }
/* 1894 */     static { types = values();
/*      */ 
/*      */       
/* 1897 */       for (FishData fd : types)
/*      */       {
/* 1899 */         byTemplateId.put(Integer.valueOf(fd.getTemplateId()), fd); }  }
/*      */     public boolean useFishingNet() { return this.useFishingNet; }
/*      */     public boolean useSpear() { return this.useSpear; }
/*      */     public boolean useReelBasic() { return this.useReelBasic; }
/*      */     public boolean useReelFine() { return this.useReelFine; }
/*      */     public boolean useReelWater() { return this.useReelWater; }
/*      */     public boolean useReelProfessional() { return this.useReelProfessional; }
/*      */     public int[] feeds() { return this.feeds; }
/*      */     public int[] baits() { return this.baits; }
/*      */     private void assignDepths(byte[] depths) { for (int depth : depths) { switch (depth) { case 1: this.inWater = true; break;case 2: this.inPond = true; break;case 3: this.inLake = true; break;case 4: this.inSea = true; break;case 7: this.inShallows = true; break; }  }  }
/* 1909 */     private void assignReels(int[] reels) { for (int reel : reels) { switch (reel) { case 1343: this.useFishingNet = true; break;case 705: this.useSpear = true; break;case 1344: this.useFishingPole = true; break;case 1372: this.useReelBasic = true; break;case 1373: this.useReelFine = true; break;case 1374: this.useReelWater = true; break;case 1375: this.useReelProfessional = true; break; }  }  } @Nullable public ItemTemplate getTemplate() { if (this.templateId == 0) return null;  if (this.template != null) return this.template;  try { this.template = ItemTemplateFactory.getInstance().getTemplate(this.templateId); } catch (NoSuchTemplateException noSuchTemplateException) {} return this.template; } public float getTemplateDifficulty() { if (getTemplate() != null) return this.template.getDifficulty();  return 100.0F; } private float addDifficultyDepth(float posx, float posy, boolean isOnSurface) { int tilex = (int)posx >> 2; int tiley = (int)posy >> 2; byte waterType = WaterType.getWaterType(tilex, tiley, isOnSurface); float extraWaterTypeDifficulty = 0.0F; switch (waterType) { case 1: case 2: if (!inPond()) extraWaterTypeDifficulty = 10.0F;  break;case 3: if (!inLake()) extraWaterTypeDifficulty = 15.0F;  break;case 4: if (!inSea()) extraWaterTypeDifficulty = 20.0F;  break;case 5: case 6: if (!inShallows()) extraWaterTypeDifficulty = 10.0F;  break; }  int waterDepth = FishEnums.getWaterDepth(posx, posy, isOnSurface); int heightDiff = 0; if (waterDepth < this.minDepth) { heightDiff = Math.min(Math.abs(this.minDepth - waterDepth), 250); return extraWaterTypeDifficulty + Math.min(heightDiff / 10.0F, 1.0F); }  if (waterDepth > this.maxDepth) { heightDiff = Math.min(Math.abs(waterDepth - this.maxDepth), 250); return extraWaterTypeDifficulty + Math.min(heightDiff / 10.0F, 1.0F); }  return extraWaterTypeDifficulty; } private float addDifficultyFeeding(Item fishingFloat) { FishEnums.TimeOfDay tod; if (fishingFloat == null) return 10.0F;  switch (getFeedHeight()) { case TOP: switch (fishingFloat.getTemplateId()) { case 1352: return -5.0F;case 1354: return 8.0F;case 1353: return 0.0F;case 1355: return 8.0F; } case BOTTOM: switch (fishingFloat.getTemplateId()) { case 1352: return 8.0F;case 1354: return -5.0F;case 1353: return 0.0F;case 1355: return 8.0F; } case ANY: switch (fishingFloat.getTemplateId()) { case 1352: return 8.0F;case 1354: return 8.0F;case 1353: return -5.0F;case 1355: return 0.0F; } case TIME: tod = FishEnums.TimeOfDay.getTimeOfDay(); switch (tod) { case TOP: switch (fishingFloat.getTemplateId()) { case 1352: return -5.0F;case 1354: return 8.0F;case 1353: return 0.0F;case 1355: return -5.0F; } case BOTTOM: switch (fishingFloat.getTemplateId()) { case 1352: return 8.0F;case 1354: return 8.0F;case 1353: return -5.0F;case 1355: return -5.0F; } case ANY: switch (fishingFloat.getTemplateId()) { case 1352: return 8.0F;case 1354: return -5.0F;case 1353: return 0.0F;case 1355: return -5.0F; } case TIME: switch (fishingFloat.getTemplateId()) { case 1352: return 8.0F;case 1354: return 8.0F;case 1353: return -5.0F;case 1355: return -5.0F; }  break; }  break; }  return 15.0F; } private float addDifficultyTimeOfDay() { FishEnums.TimeOfDay tod = FishEnums.TimeOfDay.getTimeOfDay(); int feed = this.feeds[tod.typeId]; int diff = (4 - feed) * 5; return diff; } private float addDifficultyRod(Item rod, Item reel, Item line, Item hook) { if (rod.getTemplateId() == 1343) { if (this.useFishingNet) return 0.0F;  return 1000.0F; }  if (rod.getTemplateId() == 705 || rod.getTemplateId() == 707) { if (this.useSpear) return (rod.getTemplateId() == 705) ? 2.0F : 0.0F;  return 1000.0F; }  if (line == null || hook == null) return 1000.0F;  float diff = 1000.0F; if (rod.getTemplateId() == 1344) { if (this.useFishingPole) { diff = -10.0F; } else if (this.useReelBasic) { diff = 5.0F; } else if (this.useReelFine) { diff = 10.0F; } else { diff = 30.0F; }  } else { switch (reel.getTemplateId()) { case 1372: if (this.useReelBasic) { diff = -10.0F; break; }  if (this.useFishingPole) { diff = 5.0F; break; }  if (this.useReelFine) { diff = 10.0F; break; }  diff = 30.0F; break;case 1373: if (this.useReelFine) { diff = -10.0F; break; }  if (this.useReelBasic) { diff = 5.0F; break; }  if (this.useReelWater) { diff = 10.0F; break; }  diff = 30.0F; break;case 1374: if (this.useReelWater) { diff = -10.0F; break; }  if (this.useReelFine) { diff = 5.0F; break; }  if (this.useReelProfessional) { diff = 10.0F; break; }  if (this.useReelBasic) { diff = 15.0F; break; }  diff = 30.0F; break;case 1375: if (this.useReelProfessional) { diff = -10.0F; break; }  if (this.useReelWater) { diff = 5.0F; break; }  if (this.useReelFine) { diff = 15.0F; break; }  diff = 50.0F; break; }  }  if (diff > 0.0F) switch (hook.getTemplateId()) { case 1358: diff *= 1.1F; break;case 1356: diff *= 1.2F; break; }   return diff; } public static final int getLength() { return types.length; }
/*      */     private float addDifficultyBait(Item bait) { byte baitId = FishEnums.BaitType.fromItem(bait).getTypeId(); return 10.0F - this.baits[baitId]; }
/*      */     public float getDifficulty(float skill, float posX, float posY, boolean onSurface, Item rod, Item reel, Item line, Item fishingFloat, Item hook, Item bait) { if (getTypeId() == CLAM.getTypeId())
/*      */         return skill - 10.0F;  float difficulty = getTemplateDifficulty(); difficulty += addDifficultyDepth(posX, posY, onSurface); difficulty += addDifficultyFeeding(fishingFloat); difficulty += addDifficultyTimeOfDay(); difficulty += addDifficultyRod(rod, reel, line, hook); difficulty += addDifficultyBait(bait); difficulty = Math.min(Math.max(difficulty, -50.0F), 100.0F); return difficulty; }
/*      */     private float getChanceDefault(float skill) { float diff = 0.0F; if (getTemplate() != null)
/*      */         diff = this.template.getDifficulty();  if (diff > 0.0F) {
/*      */         float flip = 110.0F - diff; float smd = skill - diff; double rad = Math.toRadians(smd); float sin = (float)Math.sin(rad);
/*      */         float mult = 1.0F + sin;
/*      */         return flip * mult;
/*      */       } 
/* 1919 */       return 50.0F; } public static FishData fromInt(int id) { if (id >= getLength())
/* 1920 */         return types[0]; 
/* 1921 */       return types[id & 0xFF]; } private float multChanceDepth(float posx, float posy, boolean isOnSurface) { int tilex = (int)posx >> 2; int tiley = (int)posy >> 2; byte waterType = WaterType.getWaterType(tilex, tiley, isOnSurface); switch (waterType) { case 1: case 2: if (!inPond())
/*      */             return 0.0F;  break;case 3: if (!inLake())
/*      */             return 0.0F;  break;case 4: if (!inSea())
/*      */             return 0.0F;  break;case 6: if (!inLake() && !inShallows())
/*      */             return 0.0F;  break;case 5: if (!inSea() && !inShallows())
/*      */             return 0.0F;  break; }  int waterDepth = FishEnums.getWaterDepth(posx, posy, isOnSurface); int heightDiff = 0; if (waterDepth < this.minDepth) { heightDiff = Math.min(Math.abs(this.minDepth - waterDepth), 250); return 1.0F - Math.min(heightDiff / 300.0F, 1.0F); }  if (waterDepth > this.maxDepth) { heightDiff = Math.min(Math.abs(waterDepth - this.maxDepth), 250); return 1.0F - Math.min(heightDiff / 500.0F, 1.0F); }  return 1.0F; }
/*      */     private float multChanceFeeding(Item fishingFloat) { FishEnums.TimeOfDay tod; if (fishingFloat == null)
/*      */         return 0.5F;  switch (getFeedHeight()) { case TOP: switch (fishingFloat.getTemplateId()) { case 1352: return 1.2F;case 1354: return 0.8F;case 1353: return 1.0F;case 1355: return 0.8F; } case BOTTOM: switch (fishingFloat.getTemplateId()) { case 1352: return 0.8F;case 1354: return 1.2F;case 1353: return 1.0F;case 1355: return 0.8F; } case ANY: switch (fishingFloat.getTemplateId()) { case 1352: return 0.8F;case 1354: return 0.8F;case 1353: return 1.2F;case 1355: return 1.0F; } case TIME: tod = FishEnums.TimeOfDay.getTimeOfDay(); switch (tod) { case TOP: switch (fishingFloat.getTemplateId()) { case 1352: return 1.2F;case 1354: return 0.8F;case 1353: return 1.0F;case 1355: return 1.2F; } case BOTTOM: switch (fishingFloat.getTemplateId()) { case 1352: return 0.8F;case 1354: return 0.8F;case 1353: return 1.2F;case 1355: return 1.2F; } case ANY: switch (fishingFloat.getTemplateId()) { case 1352: return 0.8F;case 1354: return 1.2F;case 1353: return 1.0F;case 1355: return 1.2F; } case TIME: switch (fishingFloat.getTemplateId()) { case 1352: return 0.8F;case 1354: return 0.8F;
/*      */                 case 1353: return 1.2F;
/*      */                 case 1355: return 1.2F; }  break; }  break; }  return 0.5F; }
/*      */     private float multChanceTimeOfDay() { FishEnums.TimeOfDay tod = FishEnums.TimeOfDay.getTimeOfDay(); int feed = this.feeds[tod.typeId]; int chance = 1 + (feed - 4) / 8; return chance; }
/* 1932 */     @Nullable public static FishData fromName(String name) { for (FishData fd : types) {
/*      */         
/* 1934 */         if (fd.getName().equalsIgnoreCase(name))
/* 1935 */           return fd; 
/*      */       } 
/* 1937 */       return null; } private float multChanceRod(Item rod, Item reel, Item line, Item hook, Item bait) { if (rod.getTemplateId() == 1343) { if (this.useFishingNet) return 1.0F;  return 0.0F; }  if (rod.getTemplateId() == 705 || rod.getTemplateId() == 707) { if (this.useSpear) return (rod.getTemplateId() == 705) ? 0.8F : 1.0F;  return 0.0F; }  if (line == null || hook == null) return 0.0F;  float chance = 0.0F; if (rod.getTemplateId() == 1344) { if (this.useFishingPole) { chance = 1.0F; } else if (this.useReelBasic) { chance = 0.75F; } else if (this.useReelFine) { chance = 0.5F; } else { chance = 0.1F; }  } else { switch (reel.getTemplateId()) { case 1372: if (this.useReelBasic) { chance = 1.0F; break; }  if (this.useFishingPole) { chance = 0.75F; break; }  if (this.useReelFine) { chance = 0.75F; break; }  if (this.useReelWater) { chance = 0.25F; break; }  chance = 0.1F; break;case 1373: if (this.useReelFine) { chance = 1.0F; break; }  if (this.useReelBasic) { chance = 0.75F; break; }  if (this.useFishingPole) { chance = 0.25F; break; }  if (this.useReelWater) { chance = 0.5F; break; }  chance = 0.1F; break;case 1374: if (this.useReelWater) { chance = 1.0F; break; }  if (this.useReelFine) { chance = 0.75F; break; }  if (this.useReelProfessional) { chance = 0.5F; break; }  chance = 0.1F; break;case 1375: if (this.useReelProfessional) { chance = 1.0F; break; }  if (this.useReelWater) { chance = 0.85F; break; }  if (this.useReelFine) { chance = 0.45F; break; }  chance = 0.1F; break; }  }  if (chance > 0.0F) switch (hook.getTemplateId()) { case 1358: chance *= 0.9F; break;case 1356: chance *= 0.8F; break; }   return chance * multChanceBait(bait); }
/*      */     private float multChanceBait(Item bait) { byte baitId = FishEnums.BaitType.fromItem(bait).getTypeId(); if (this.typeId == -1) System.out.println(this.name + "(bait):" + baitId + " " + this.baits[baitId]);  switch (this.baits[baitId]) { case 0: return 0.0F;case 1: return 0.8F;case 2: return 0.82F;case 3: return 0.84F;case 4: return 0.86F;case 5: return 0.88F;case 6: return 0.9F;case 7: return 0.925F;case 8: return 0.95F;case 9: return 0.975F;case 10: return 1.0F; }  return 0.0F; }
/*      */     public Point getSpecialSpot(int zoneX, int zoneY, int season) { Random r = new Random((getTypeId() * 5 + Servers.localServer.id * 100 + season)); int rx = zoneX * 128 + 5 + r.nextInt(118); int ry = zoneY * 128 + 5 + r.nextInt(118); return new Point(rx, ry, getTemplateId()); }
/*      */     public float getChance(float skill, Item rod, Item reel, Item line, Item fishingFloat, Item hook, Item bait, float posX, float posY, boolean onSurface) { if (onSurface() != onSurface) return 0.0F;  float chance = getChanceDefault(skill); if (this.typeId == -1)
/*      */         System.out.println(this.name + "(default):" + chance);  chance *= multChanceDepth(posX, posY, onSurface); if (this.typeId == -1)
/*      */         System.out.println(this.name + "(depth):" + chance);  chance *= multChanceFeeding(fishingFloat); if (this.typeId == -1)
/*      */         System.out.println(this.name + "(feed):" + chance);  chance *= multChanceTimeOfDay(); if (this.typeId == -1)
/*      */         System.out.println(this.name + "(time):" + chance);  chance *= multChanceRod(rod, reel, line, hook, bait); if (this.typeId == -1)
/*      */         System.out.println(this.name + "(rod):" + chance + " " + ((bait == null) ? 1 : 0));  if (this.isSpecialFish && chance > 0.0F) { int tilex = (int)posX >> 2; int tiley = (int)posY >> 2; int season = WurmCalendar.getSeasonNumber(); int zoneX = tilex / 128; int zoneY = tiley / 128; Point specialSpot = getSpecialSpot(zoneX, zoneY, season); int farAwayX = Math.abs(specialSpot.getX() - tilex); int farAwayY = Math.abs(specialSpot.getY() - tiley); int farAway = Math.max(farAwayX, farAwayY); float rt2 = 15.0F; float nc = 0.0F; float ht = 0.0F; try { ht = Zones.calculateHeight(posX, posY, onSurface) * 10.0F; } catch (NoSuchZoneException noSuchZoneException) {} if (farAway <= 15.0F && ht < -100.0F) { float dpt = 6.0F; double rad = Math.toRadians((farAway * 6.0F)); nc = (float)Math.cos(rad); }  chance *= nc; if (this.typeId == -1)
/*      */           System.out.println(this.name + "(special):" + farAway + " " + chance + " " + nc);  }  return chance; }
/* 1947 */     public static FishData fromItem(@Nullable Item fish) { if (fish == null) {
/* 1948 */         return NONE;
/*      */       }
/* 1950 */       FishData fishData = byTemplateId.get(Integer.valueOf(fish.getTemplateId()));
/*      */       
/* 1952 */       if (fishData == null) {
/* 1953 */         return NONE;
/*      */       }
/* 1955 */       return fishData; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int getWaterDepth(float posx, float posy, boolean isOnSurface) {
/*      */     try {
/* 1970 */       return (int)(-Zones.calculateHeight(posx, posy, isOnSurface) * 10.0F);
/*      */     }
/* 1972 */     catch (NoSuchZoneException e) {
/*      */ 
/*      */ 
/*      */       
/* 1976 */       logger.log(Level.WARNING, e.getMessage(), (Throwable)e);
/*      */       
/* 1978 */       return 5;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\FishEnums.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */