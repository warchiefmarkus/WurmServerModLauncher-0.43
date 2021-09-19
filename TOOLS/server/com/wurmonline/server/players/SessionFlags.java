/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import java.util.BitSet;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class SessionFlags
/*     */   implements MiscConstants
/*     */ {
/*  32 */   private static final String[] flagDescs = new String[64];
/*     */ 
/*     */   
/*  35 */   private static final Logger logger = Logger.getLogger(SessionFlags.class.getName());
/*     */ 
/*     */   
/*     */   static {
/*  39 */     initialiseFlags();
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
/*     */   static void initialiseFlags() {
/*  52 */     for (int x = 0; x < 64; x++) {
/*     */       
/*  54 */       flagDescs[x] = "";
/*  55 */       if (x == 0) {
/*  56 */         flagDescs[x] = "Player has signed in";
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BitSet setFlagBits(long bits, BitSet toSet) {
/*  65 */     for (int x = 0; x < 64; x++) {
/*     */       
/*  67 */       if (x == 0) {
/*     */         
/*  69 */         if ((bits & 0x1L) == 1L) {
/*  70 */           toSet.set(x, true);
/*     */         } else {
/*  72 */           toSet.set(x, false);
/*     */         }
/*     */       
/*     */       }
/*  76 */       else if ((bits >> x & 0x1L) == 1L) {
/*  77 */         toSet.set(x, true);
/*     */       } else {
/*  79 */         toSet.set(x, false);
/*     */       } 
/*     */     } 
/*  82 */     return toSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getFlagBits(BitSet bitsprovided) {
/*  90 */     long ret = 0L;
/*  91 */     for (int x = 0; x <= 64; x++) {
/*     */       
/*  93 */       if (bitsprovided.get(x))
/*  94 */         ret += (1 << x); 
/*     */     } 
/*  96 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getFlagString(int flag) {
/* 101 */     if (flag >= 0 && flag < 64)
/* 102 */       return flagDescs[flag]; 
/* 103 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\SessionFlags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */