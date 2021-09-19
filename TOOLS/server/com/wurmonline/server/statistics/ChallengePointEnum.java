/*     */ package com.wurmonline.server.statistics;
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
/*     */ public class ChallengePointEnum
/*     */ {
/*     */   public enum ChallengeScenario
/*     */   {
/*  28 */     UNUSED(0, "Unused", "http://www.wurmonline.com", "Placeholder"),
/*  29 */     BUILDBATTLE(1, "Build The Battlefield", "http://www.wurmonline.com/images/challenge/midbb.png", "The First Ever"),
/*  30 */     DOMINANCE(2, "Dominance", "http://www.wurmonline.com/images/challenge/dd64.png", "Rush to the front");
/*     */     
/*  32 */     public static final ChallengeScenario current = DOMINANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int num;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String url;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String desc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ChallengeScenario(int enumber, String ename, String eurl, String edescription) {
/*     */       this.num = enumber;
/*     */       this.name = ename;
/*     */       this.url = eurl;
/*     */       this.desc = edescription;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     private static final ChallengeScenario[] types = values();
/*     */     public int getNum() {
/*     */       return this.num;
/*     */     }
/*     */     public static final ChallengeScenario[] getScenarios() {
/*  95 */       return types;
/*     */     }
/*     */     public String getName() {
/*     */       return this.name;
/*     */     }
/*     */     public static ChallengeScenario fromInt(int typeByte) {
/*     */       try {
/* 102 */         return types[typeByte];
/*     */       }
/* 104 */       catch (Exception ex) {
/*     */         
/* 106 */         return UNUSED;
/*     */       } 
/*     */     } public String getUrl() {
/*     */       return this.url;
/*     */     } public String getDesc() {
/*     */       return this.desc;
/*     */     } }
/* 113 */   public enum ChallengePoint { UNUSED(0, "Unused", "This should not be used", Float.MAX_VALUE),
/* 114 */     OVERALL(1, "Overall", "Most overall points", Float.MAX_VALUE),
/* 115 */     ITEMSLOOTED(2, "Items Looted", "Most items looted from resource points", Float.MAX_VALUE),
/* 116 */     BATTLEPOINTS(3, "Battle Points", "Most Battle Points", Float.MAX_VALUE),
/* 117 */     HOTAWINS(4, "Hota Wins", "Player with most HOTA wins", Float.MAX_VALUE),
/* 118 */     PLAYERKILLS(5, "Player Kills", "Most Unique player kills", Float.MAX_VALUE),
/* 119 */     KARMAGAINED(6, "Karma Gained", "Most Karma gained", Float.MAX_VALUE),
/* 120 */     ACHIEVEMENTS(7, "Achievements", "Most Unique Achievements accomplished", Float.MAX_VALUE),
/* 121 */     PERSONALACHIEVEMENTS(8, "Personal Achievements", "Most Personal Achievements accomplished", Float.MAX_VALUE),
/* 122 */     LAND(9, "Most Land", "Ruler with most land", Float.MAX_VALUE),
/* 123 */     VILLAGEHOTA(10, "Village Hota", "Village with most HOTA wins (Mayor receives prize to share)", Float.MAX_VALUE),
/* 124 */     HOTAPILLARS(11, "Hota pillars", "Player who conquers most pillars first in a HOTA round", Float.MAX_VALUE),
/* 125 */     TREASURE_CHESTS(12, "Treasure Chests", "Player who found and opened most treasure chests", Float.MAX_VALUE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isDirty = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int enumtype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String description;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final float maxvalue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ChallengePoint(int type, String _description, String _name, float maxValue) {
/*     */       this.name = _name;
/*     */       this.maxvalue = maxValue;
/*     */       this.enumtype = type;
/*     */       this.description = _description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getEnumtype() {
/*     */       return this.enumtype;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     private static final ChallengePoint[] types = values();
/*     */     public String getDescription() {
/*     */       return this.description;
/*     */     }
/*     */     
/*     */     public static ChallengePoint fromInt(int typeByte) {
/*     */       try {
/* 202 */         return types[typeByte];
/*     */       }
/* 204 */       catch (Exception ex) {
/*     */         
/* 206 */         return UNUSED;
/*     */       } 
/*     */     } public String getName() {
/*     */       return this.name;
/*     */     } public float getMaxvalue() {
/*     */       return this.maxvalue;
/*     */     } public static ChallengePoint[] getTypes() {
/*     */       return types;
/*     */     } static {
/*     */     
/*     */     } public boolean isDirty() {
/* 217 */       return this.isDirty;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void setDirty(boolean dirty) {
/* 228 */       this.isDirty = dirty;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\statistics\ChallengePointEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */