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
/*     */ 
/*     */ 
/*     */ public final class Flags
/*     */   implements MiscConstants
/*     */ {
/*  34 */   private static final String[] flagDescs = new String[64];
/*     */ 
/*     */   
/*  37 */   private static final Logger logger = Logger.getLogger(javax.mail.Flags.Flag.class.getName());
/*     */ 
/*     */   
/*     */   static {
/*  41 */     initialiseFlags();
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
/*  54 */     for (int x = 0; x < 64; x++) {
/*     */       
/*  56 */       flagDescs[x] = "";
/*  57 */       if (x == 0)
/*  58 */         flagDescs[x] = "Seen structure door warning"; 
/*  59 */       if (x == 1)
/*  60 */         flagDescs[x] = "Allow Incoming PMs"; 
/*  61 */       if (x == 2)
/*  62 */         flagDescs[x] = "Allow Incoming Cross-Kingdoms PMs"; 
/*  63 */       if (x == 3) {
/*  64 */         flagDescs[x] = "Allow Incoming Cross-Servers PMs";
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BitSet setFlagBits(long bits, BitSet toSet) {
/*  73 */     for (int x = 0; x < 64; x++) {
/*     */       
/*  75 */       if (x == 0) {
/*     */         
/*  77 */         if ((bits & 0x1L) == 1L) {
/*  78 */           toSet.set(x, true);
/*     */         } else {
/*  80 */           toSet.set(x, false);
/*     */         }
/*     */       
/*     */       }
/*  84 */       else if ((bits >> x & 0x1L) == 1L) {
/*  85 */         toSet.set(x, true);
/*     */       } else {
/*  87 */         toSet.set(x, false);
/*     */       } 
/*     */     } 
/*  90 */     return toSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getFlagBits(BitSet bitsprovided) {
/*  98 */     long ret = 0L;
/*  99 */     for (int x = 0; x <= 64; x++) {
/*     */       
/* 101 */       if (bitsprovided.get(x))
/* 102 */         ret += (1 << x); 
/*     */     } 
/* 104 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getFlagString(int flag) {
/* 109 */     if (flag >= 0 && flag < 64)
/* 110 */       return flagDescs[flag]; 
/* 111 */     return "";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Flags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */