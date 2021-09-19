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
/*     */ public final class CommonConstantsUtility
/*     */ {
/*     */   public static String getAttitudeDescription(byte aAttitudeTypeCode) {
/*  64 */     switch (aAttitudeTypeCode)
/*     */     
/*     */     { case 1:
/*  67 */         lDescription = "Ally";
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
/*  91 */         return lDescription;case 2: lDescription = "Hostile"; return lDescription;case 0: lDescription = "Neutral"; return lDescription;case 5: lDescription = "Good"; return lDescription;case 4: lDescription = "Evil"; return lDescription;case 3: lDescription = "GM"; return lDescription;case 6: lDescription = "Dev"; return lDescription; }  String lDescription = "Unknown attitude: " + aAttitudeTypeCode; return lDescription;
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
/*     */   public static String getEffectDescription(short aEffectTypeCode) {
/* 106 */     switch (aEffectTypeCode)
/*     */     
/*     */     { case 0:
/* 109 */         lDescription = "Campfire";
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
/* 130 */         return lDescription;case 1: lDescription = "Lightning Bolt"; return lDescription;case 2: lDescription = "Altar Light Beam Holy"; return lDescription;case 3: lDescription = "Altar Light Beam Unholy"; return lDescription;case 4: lDescription = "Christmas Lights"; return lDescription;case 19: lDescription = "Item Spawn"; return lDescription; }  String lDescription = "Unknown effect: " + aEffectTypeCode; return lDescription;
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
/*     */   public static String getAttachEffectDescription(short aEffectTypeCode) {
/* 145 */     switch (aEffectTypeCode)
/*     */     
/*     */     { case 0:
/* 148 */         lDescription = "Light";
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
/* 166 */         return lDescription;case 1: lDescription = "FireEffect"; return lDescription;case 2: lDescription = "Transparent"; return lDescription;case 3: lDescription = "Glow"; return lDescription;case 4: lDescription = "Flames"; return lDescription; }  String lDescription = "Unknown effect: " + aEffectTypeCode; return lDescription;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\CommonConstantsUtility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */