/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.fourthline.cling.model.ModelUtil;
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
/*     */ public enum StorageMedium
/*     */ {
/*  28 */   UNKNOWN,
/*  29 */   DV,
/*  30 */   MINI_DV("MINI-DV"),
/*  31 */   VHS,
/*  32 */   W_VHS("W-VHS"),
/*  33 */   S_VHS("S-VHS"),
/*  34 */   D_VHS("D-VHS"),
/*  35 */   VHSC,
/*  36 */   VIDEO8,
/*  37 */   HI8,
/*  38 */   CD_ROM("CD-ROM"),
/*  39 */   CD_DA("CD-DA"),
/*  40 */   CD_R("CD-R"),
/*  41 */   CD_RW("CD-RW"),
/*  42 */   VIDEO_CD("VIDEO-CD"),
/*  43 */   SACD,
/*  44 */   MD_AUDIO("M-AUDIO"),
/*  45 */   MD_PICTURE("MD-PICTURE"),
/*  46 */   DVD_ROM("DVD-ROM"),
/*  47 */   DVD_VIDEO("DVD-VIDEO"),
/*  48 */   DVD_R("DVD-R"),
/*  49 */   DVD_PLUS_RW("DVD+RW"),
/*  50 */   DVD_MINUS_RW("DVD-RW"),
/*  51 */   DVD_RAM("DVD-RAM"),
/*  52 */   DVD_AUDIO("DVD-AUDIO"),
/*  53 */   DAT,
/*  54 */   LD,
/*  55 */   HDD,
/*  56 */   MICRO_MV("MICRO_MV"),
/*  57 */   NETWORK,
/*  58 */   NONE,
/*  59 */   NOT_IMPLEMENTED,
/*  60 */   VENDOR_SPECIFIC;
/*     */   static {
/*  62 */     byProtocolString = new HashMap<String, StorageMedium>()
/*     */       {
/*     */       
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, StorageMedium> byProtocolString;
/*     */   
/*     */   private String protocolString;
/*     */ 
/*     */   
/*     */   StorageMedium(String protocolString) {
/*  75 */     this.protocolString = (protocolString == null) ? name() : protocolString;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     return this.protocolString;
/*     */   }
/*     */   
/*     */   public static StorageMedium valueOrExceptionOf(String s) {
/*  84 */     StorageMedium sm = byProtocolString.get(s);
/*  85 */     if (sm != null) return sm; 
/*  86 */     throw new IllegalArgumentException("Invalid storage medium string: " + s);
/*     */   }
/*     */   
/*     */   public static StorageMedium valueOrVendorSpecificOf(String s) {
/*  90 */     StorageMedium sm = byProtocolString.get(s);
/*  91 */     return (sm != null) ? sm : VENDOR_SPECIFIC;
/*     */   }
/*     */   
/*     */   public static StorageMedium[] valueOfCommaSeparatedList(String s) {
/*  95 */     String[] strings = ModelUtil.fromCommaSeparatedList(s);
/*  96 */     if (strings == null) return new StorageMedium[0]; 
/*  97 */     StorageMedium[] result = new StorageMedium[strings.length];
/*  98 */     for (int i = 0; i < strings.length; i++) {
/*  99 */       result[i] = valueOrVendorSpecificOf(strings[i]);
/*     */     }
/* 101 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\StorageMedium.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */