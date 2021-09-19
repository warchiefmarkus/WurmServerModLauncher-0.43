/*     */ package com.wurmonline.mesh;
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
/*     */ public enum FoliageAge
/*     */ {
/*  22 */   YOUNG_ONE(0, "young", 0, false),
/*  23 */   YOUNG_TWO(1, "young", 0, false),
/*  24 */   YOUNG_THREE(2, "young", 1, false),
/*  25 */   YOUNG_FOUR(3, "young", 2, true),
/*  26 */   MATURE_ONE(4, "mature", 3, true),
/*  27 */   MATURE_TWO(5, "mature", 4, false),
/*  28 */   MATURE_THREE(6, "mature", 5, false),
/*  29 */   MATURE_SPROUTING(7, "mature, sprouting", 6, false),
/*  30 */   OLD_ONE(8, "old", 6, false),
/*  31 */   OLD_ONE_SPROUTING(9, "old, sprouting", 8, false),
/*  32 */   OLD_TWO(10, "old", 8, false),
/*  33 */   OLD_TWO_SPROUTING(11, "old, sprouting", 10, false),
/*  34 */   VERY_OLD(12, "very old", 10, false),
/*  35 */   VERY_OLD_SPROUTING(13, "very old, sprouting", 12, true),
/*  36 */   OVERAGED(14, "overaged", 12, true),
/*  37 */   SHRIVELLED(15, "shriveled", 14, false);
/*     */   
/*     */   private byte ageId;
/*     */   private String name;
/*     */   private byte prunedAge;
/*     */   private boolean isPrunable;
/*     */   private static final FoliageAge[] ages;
/*     */   
/*     */   FoliageAge(int id, String name, int prunedAge, boolean isPrunable) {
/*  46 */     this.ageId = (byte)(id & 0xFF);
/*  47 */     this.name = name;
/*  48 */     this.prunedAge = (byte)(prunedAge & 0xFF);
/*  49 */     this.isPrunable = isPrunable;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAgeId() {
/*  54 */     return this.ageId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAgeName() {
/*  59 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrunable() {
/*  64 */     return this.isPrunable;
/*     */   }
/*     */ 
/*     */   
/*     */   public FoliageAge getPrunedAge() {
/*  69 */     return fromByte(this.prunedAge);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte encodeAsData() {
/*  74 */     return (byte)(this.ageId << 4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  80 */     ages = values();
/*     */   }
/*     */ 
/*     */   
/*     */   public static FoliageAge fromByte(byte i) {
/*  85 */     return ages[i];
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
/*     */   public static FoliageAge getFoliageAge(byte tileData) {
/*  97 */     return fromByte(getAgeAsByte(tileData));
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
/*     */   public static byte getAgeAsByte(byte tileData) {
/* 109 */     return (byte)(tileData >> 4 & 0xF);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\mesh\FoliageAge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */