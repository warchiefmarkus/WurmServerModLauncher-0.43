/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Level;
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
/*     */ public final class Traits
/*     */   implements MiscConstants
/*     */ {
/*  36 */   private static final String[] treatDescs = new String[64];
/*  37 */   private static final boolean[] negativeTraits = new boolean[64];
/*  38 */   private static final boolean[] neutralTraits = new boolean[64];
/*     */ 
/*     */   
/*  41 */   private static final Logger logger = Logger.getLogger(Traits.class.getName());
/*     */   
/*     */   static {
/*  44 */     initialiseTraits();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void initialiseTraits() {
/*  52 */     for (int x = 0; x < 64; x++) {
/*     */       
/*  54 */       treatDescs[x] = "";
/*  55 */       if (x == 0) {
/*  56 */         treatDescs[x] = "It will fight fiercely.";
/*  57 */       } else if (x == 1) {
/*  58 */         treatDescs[x] = "It has fleeter movement than normal.";
/*  59 */       } else if (x == 2) {
/*  60 */         treatDescs[x] = "It is a tough bugger.";
/*  61 */       } else if (x == 3) {
/*  62 */         treatDescs[x] = "It has a strong body.";
/*  63 */       } else if (x == 4) {
/*  64 */         treatDescs[x] = "It has lightning movement.";
/*  65 */       } else if (x == 5) {
/*  66 */         treatDescs[x] = "It can carry more than average.";
/*  67 */       } else if (x == 6) {
/*  68 */         treatDescs[x] = "It has very strong leg muscles.";
/*  69 */       } else if (x == 7) {
/*  70 */         treatDescs[x] = "It has keen senses.";
/*  71 */       } else if (x == 8) {
/*     */         
/*  73 */         treatDescs[x] = "It has malformed hindlegs.";
/*  74 */         negativeTraits[x] = true;
/*     */       }
/*  76 */       else if (x == 9) {
/*     */         
/*  78 */         treatDescs[x] = "The legs are of different length.";
/*  79 */         negativeTraits[x] = true;
/*     */       }
/*  81 */       else if (x == 10) {
/*     */         
/*  83 */         treatDescs[x] = "It seems overly aggressive.";
/*  84 */         negativeTraits[x] = true;
/*     */       }
/*  86 */       else if (x == 11) {
/*     */         
/*  88 */         treatDescs[x] = "It looks very unmotivated.";
/*  89 */         negativeTraits[x] = true;
/*     */       }
/*  91 */       else if (x == 12) {
/*     */         
/*  93 */         treatDescs[x] = "It is unusually strong willed.";
/*  94 */         negativeTraits[x] = true;
/*     */       }
/*  96 */       else if (x == 13) {
/*     */         
/*  98 */         treatDescs[x] = "It has some illness.";
/*  99 */         negativeTraits[x] = true;
/*     */       }
/* 101 */       else if (x == 14) {
/*     */         
/* 103 */         treatDescs[x] = "It looks constantly hungry.";
/* 104 */         negativeTraits[x] = true;
/*     */       }
/* 106 */       else if (x == 19) {
/*     */         
/* 108 */         treatDescs[x] = "It looks feeble and unhealthy.";
/* 109 */         negativeTraits[x] = true;
/*     */       }
/* 111 */       else if (x == 20) {
/*     */         
/* 113 */         treatDescs[x] = "It looks unusually strong and healthy.";
/* 114 */         negativeTraits[x] = false;
/*     */       }
/* 116 */       else if (x == 21) {
/*     */         
/* 118 */         treatDescs[x] = "It has a certain spark in its eyes.";
/* 119 */         negativeTraits[x] = false;
/*     */       }
/* 121 */       else if (x == 22) {
/*     */         
/* 123 */         treatDescs[x] = "It has been corrupted.";
/* 124 */         neutralTraits[x] = true;
/*     */       }
/* 126 */       else if (x == 27) {
/*     */         
/* 128 */         treatDescs[x] = "It bears the mark of the rift.";
/* 129 */         neutralTraits[x] = true;
/*     */       }
/* 131 */       else if (x == 28) {
/*     */         
/* 133 */         treatDescs[x] = "It bears the mark of a traitor.";
/* 134 */         neutralTraits[x] = true;
/*     */       }
/* 136 */       else if (x == 63) {
/*     */         
/* 138 */         treatDescs[x] = "It has been bred in captivity.";
/* 139 */         neutralTraits[x] = true;
/*     */       }
/* 141 */       else if (x == 29) {
/*     */         
/* 143 */         treatDescs[x] = "It has a mark of Valrei.";
/* 144 */         neutralTraits[x] = true;
/*     */       }
/* 146 */       else if (x == 15 || x == 16 || x == 17 || x == 18 || x == 24 || x == 25 || x == 23 || x == 30 || x == 31 || x == 32 || x == 33 || x == 34) {
/*     */ 
/*     */ 
/*     */         
/* 150 */         neutralTraits[x] = true;
/*     */       } 
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
/*     */   
/*     */   static long calcNewTraits(boolean inbred, long mothertraits, long fathertraits) {
/* 166 */     Random rand = new Random();
/* 167 */     BitSet motherSet = new BitSet(64);
/* 168 */     BitSet fatherSet = new BitSet(64);
/* 169 */     BitSet childSet = new BitSet(64);
/* 170 */     setTraitBits(fathertraits, fatherSet);
/* 171 */     setTraitBits(mothertraits, motherSet);
/* 172 */     for (int bitIndex = 0; bitIndex < 64; bitIndex++)
/*     */     {
/* 174 */       calcOneNewTrait(inbred, rand, motherSet, fatherSet, childSet, bitIndex);
/*     */     }
/* 176 */     return getTraitBits(childSet);
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
/*     */   static void calcOneNewTrait(boolean inbred, Random rand, BitSet motherSet, BitSet fatherSet, BitSet childSet, int bitIndex) {
/* 203 */     if (bitIndex == 27 || bitIndex == 28 || bitIndex == 29)
/*     */       return; 
/* 205 */     if (motherSet.get(bitIndex) && fatherSet.get(bitIndex)) {
/*     */       
/* 207 */       int chance = 66;
/*     */       
/* 209 */       if (negativeTraits[bitIndex]) {
/*     */         
/* 211 */         chance = 10;
/* 212 */         if (inbred)
/*     */         {
/* 214 */           chance = 20;
/*     */         }
/*     */       } 
/* 217 */       childSet.set(bitIndex, (rand.nextInt(100) < chance));
/*     */     }
/* 219 */     else if (motherSet.get(bitIndex)) {
/*     */       
/* 221 */       int chance = 25;
/* 222 */       if (negativeTraits[bitIndex]) {
/*     */         
/* 224 */         chance = 8;
/* 225 */         if (inbred)
/*     */         {
/* 227 */           chance = 12;
/*     */         }
/*     */       } 
/* 230 */       childSet.set(bitIndex, (rand.nextInt(100) < chance));
/*     */     }
/* 232 */     else if (fatherSet.get(bitIndex)) {
/*     */       
/* 234 */       int chance = 25;
/* 235 */       if (negativeTraits[bitIndex]) {
/*     */         
/* 237 */         chance = 8;
/* 238 */         if (inbred)
/*     */         {
/* 240 */           chance = 12;
/*     */         }
/*     */       } 
/* 243 */       childSet.set(bitIndex, (rand.nextInt(100) < chance));
/*     */     }
/*     */     else {
/*     */       
/* 247 */       if (bitIndex == 22)
/*     */         return; 
/* 249 */       int chance = 7;
/* 250 */       if (negativeTraits[bitIndex]) {
/*     */         
/* 252 */         chance = 5;
/* 253 */         if (inbred)
/*     */         {
/* 255 */           chance = 10;
/*     */         }
/*     */       } 
/*     */       
/* 259 */       childSet.set(bitIndex, (rand.nextInt(100) < chance));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static long calcNewTraits(double breederSkill, boolean inbred, long mothertraits, long fathertraits) {
/* 266 */     Random rand = new Random();
/* 267 */     BitSet motherSet = new BitSet(64);
/* 268 */     BitSet fatherSet = new BitSet(64);
/* 269 */     BitSet childSet = new BitSet(64);
/* 270 */     int maxTraits = Math.min(8, Math.max(1, (int)(breederSkill / 10.0D)));
/* 271 */     int maxPoints = maxTraits * 60;
/* 272 */     int allocated = 0;
/* 273 */     Map<Integer, Integer> newSet = new HashMap<>();
/* 274 */     List<Integer> availableTraits = new ArrayList<>();
/* 275 */     setTraitBits(fathertraits, fatherSet);
/* 276 */     setTraitBits(mothertraits, motherSet);
/*     */ 
/*     */     
/* 279 */     for (int bitIndex = 0; bitIndex <= 34; bitIndex++) {
/*     */       
/* 281 */       if (bitIndex != 22 && bitIndex != 27 && bitIndex != 28 && bitIndex != 29) {
/*     */         
/* 283 */         availableTraits.add(Integer.valueOf(bitIndex));
/* 284 */         if (motherSet.get(bitIndex) && fatherSet.get(bitIndex)) {
/*     */           
/* 286 */           int num = 50;
/* 287 */           if (inbred && negativeTraits[bitIndex])
/*     */           {
/* 289 */             num += 10;
/*     */           }
/* 291 */           newSet.put(Integer.valueOf(bitIndex), Integer.valueOf(num));
/* 292 */           if (!isTraitNeutral(bitIndex))
/* 293 */             allocated += 50; 
/* 294 */           availableTraits.remove(Integer.valueOf(bitIndex));
/*     */         }
/* 296 */         else if (motherSet.get(bitIndex)) {
/*     */           
/* 298 */           int num = 30;
/* 299 */           if (inbred && negativeTraits[bitIndex])
/*     */           {
/* 301 */             num += 10;
/*     */           }
/* 303 */           newSet.put(Integer.valueOf(bitIndex), Integer.valueOf(num));
/* 304 */           if (!isTraitNeutral(bitIndex))
/* 305 */             allocated += 30; 
/* 306 */           availableTraits.remove(Integer.valueOf(bitIndex));
/*     */         }
/* 308 */         else if (fatherSet.get(bitIndex)) {
/*     */           
/* 310 */           int num = 20;
/* 311 */           if (inbred && negativeTraits[bitIndex])
/*     */           {
/* 313 */             num += 10;
/*     */           }
/* 315 */           newSet.put(Integer.valueOf(bitIndex), Integer.valueOf(num));
/* 316 */           if (!isTraitNeutral(bitIndex))
/* 317 */             allocated += 20; 
/* 318 */           availableTraits.remove(Integer.valueOf(bitIndex));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     int left = maxPoints - allocated;
/*     */ 
/*     */     
/* 330 */     float traitsLeft = 0.0F;
/* 331 */     if (left > 0) {
/*     */       
/* 333 */       traitsLeft = left / 50.0F;
/*     */       
/* 335 */       if (traitsLeft - (int)traitsLeft > 0.0F)
/* 336 */         traitsLeft++; 
/* 337 */       for (int x = 0; x < (int)traitsLeft; x++) {
/*     */         
/* 339 */         if (rand.nextBoolean()) {
/*     */           
/* 341 */           int num = 20;
/* 342 */           Integer newTrait = availableTraits.remove(rand.nextInt(availableTraits.size()));
/* 343 */           if (negativeTraits[newTrait.intValue()]) {
/*     */ 
/*     */ 
/*     */             
/* 347 */             num -= maxTraits;
/* 348 */             if (inbred)
/* 349 */               num += 10; 
/*     */           } 
/* 351 */           if (isTraitNeutral(newTrait.intValue()))
/* 352 */             x--; 
/* 353 */           newSet.put(newTrait, Integer.valueOf(num));
/*     */         } 
/*     */       } 
/*     */       
/* 357 */       traitsLeft = maxTraits;
/*     */     }
/*     */     else {
/*     */       
/* 361 */       traitsLeft = Math.max(Math.min(newSet.size(), maxTraits), 3 + Server.rand.nextInt(3));
/*     */     } 
/* 363 */     for (int t = 0; t < traitsLeft; t++) {
/*     */       
/* 365 */       if (newSet.isEmpty())
/*     */         break; 
/* 367 */       Integer selected = pickOneTrait(rand, newSet);
/* 368 */       if (selected.intValue() >= 0) {
/*     */         
/* 370 */         if (selected.intValue() != 22 && selected.intValue() != 27 && selected
/* 371 */           .intValue() != 28 && selected.intValue() != 29) {
/*     */           
/* 373 */           childSet.set(selected.intValue(), true);
/* 374 */           newSet.remove(selected);
/* 375 */           if (isTraitNeutral(selected.intValue()))
/*     */           {
/* 377 */             t--;
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 382 */         logger.log(Level.WARNING, "Failed to select a trait from a map of size " + newSet.size());
/*     */       } 
/* 384 */     }  if (!Servers.isThisAPvpServer()) {
/* 385 */       childSet.clear(22);
/* 386 */     } else if (fatherSet.get(22) || motherSet.get(22)) {
/* 387 */       childSet.set(22);
/* 388 */     }  childSet.set(63, true);
/*     */     
/* 390 */     return getTraitBits(childSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Integer pickOneTrait(Random rand, Map<Integer, Integer> traitMap) {
/* 399 */     int chance = 0;
/* 400 */     for (Map.Entry<Integer, Integer> entry : traitMap.entrySet())
/*     */     {
/* 402 */       chance += ((Integer)entry.getValue()).intValue();
/*     */     }
/* 404 */     if (chance == 0 || chance < 0) {
/*     */       
/* 406 */       logger.log(Level.INFO, "Trait rand=" + chance + " should not be <=0! Size of map is " + traitMap.size());
/* 407 */       return Integer.valueOf(-1);
/*     */     } 
/* 409 */     int selectedTrait = rand.nextInt(chance);
/*     */     
/* 411 */     chance = 0;
/* 412 */     for (Map.Entry<Integer, Integer> entry : traitMap.entrySet()) {
/*     */       
/* 414 */       chance += ((Integer)entry.getValue()).intValue();
/* 415 */       if (chance > selectedTrait)
/*     */       {
/* 417 */         return entry.getKey();
/*     */       }
/*     */     } 
/*     */     
/* 421 */     return Integer.valueOf(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BitSet setTraitBits(long bits, BitSet toSet) {
/* 429 */     for (int x = 0; x < 64; x++) {
/*     */       
/* 431 */       if (x == 0) {
/*     */         
/* 433 */         if ((bits & 0x1L) == 1L) {
/* 434 */           toSet.set(x, true);
/*     */         } else {
/* 436 */           toSet.set(x, false);
/*     */         }
/*     */       
/*     */       }
/* 440 */       else if ((bits >> x & 0x1L) == 1L) {
/* 441 */         toSet.set(x, true);
/*     */       } else {
/* 443 */         toSet.set(x, false);
/*     */       } 
/*     */     } 
/* 446 */     return toSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long getTraitBits(BitSet bitsprovided) {
/* 454 */     long ret = 0L;
/* 455 */     for (int x = 0; x < 64; x++) {
/*     */       
/* 457 */       if (bitsprovided.get(x))
/* 458 */         ret += 1L << x; 
/*     */     } 
/* 460 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getTraitString(int trait) {
/* 465 */     if (trait >= 0 && trait < 64)
/* 466 */       return treatDescs[trait]; 
/* 467 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isTraitNegative(int trait) {
/* 472 */     return (trait >= 0 && trait <= 64 && negativeTraits[trait]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isTraitNeutral(int trait) {
/* 477 */     return (trait >= 0 && trait <= 64 && neutralTraits[trait]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Traits.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */