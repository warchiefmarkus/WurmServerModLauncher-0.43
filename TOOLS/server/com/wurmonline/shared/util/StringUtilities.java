/*     */ package com.wurmonline.shared.util;
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
/*     */ public final class StringUtilities
/*     */ {
/*     */   public static final long SECOND_MILLIS = 1000L;
/*     */   public static final long MINUTE_MILLIS = 60000L;
/*     */   public static final long HOUR_MILLIS = 3600000L;
/*     */   public static final long DAY_MILLIS = 86400000L;
/*     */   private static final String UPPER_LOWER_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*     */   private static final String VILLAGE_LEGAL_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- ";
/*     */   private static final String SENTENCE_LEGAL_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,";
/*     */   private static final String EMPTY_STRING = "";
/*     */   private static final String SPACE_AND_SPACE = " and ";
/*  54 */   private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
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
/*     */   public static String raiseFirstLetter(String oldString) {
/*  74 */     if (oldString != null && !oldString.isEmpty())
/*  75 */       return oldString.substring(0, 1).toUpperCase() + oldString.substring(1).toLowerCase(); 
/*  76 */     return oldString;
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
/*     */   public static String raiseFirstLetterOnly(String oldString) {
/*  89 */     if (oldString != null && !oldString.isEmpty())
/*  90 */       return oldString.substring(0, 1).toUpperCase() + oldString.substring(1); 
/*  91 */     return oldString;
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
/*     */   public static boolean containsIllegalPlayerNameCharacters(String name) {
/* 106 */     return containsIllegalCharacters(name, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
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
/*     */   public static boolean containsNonSentenceCharacters(String name) {
/* 123 */     return containsIllegalCharacters(name, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- 1234567890.,");
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
/*     */   public static boolean containsIllegalVillageCharacters(String name) {
/* 138 */     return containsIllegalCharacters(name, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'- ");
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
/*     */   private static boolean containsIllegalCharacters(String name, String illegalCharacters) {
/* 154 */     char[] chars = name.toCharArray();
/*     */     
/* 156 */     for (int x = 0; x < chars.length; x++) {
/*     */       
/* 158 */       if (illegalCharacters.indexOf(chars[x]) < 0)
/*     */       {
/* 160 */         return true;
/*     */       }
/*     */     } 
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTimeString(long timeleft) {
/* 172 */     String times = "";
/* 173 */     if (timeleft < 60000L) {
/*     */       
/* 175 */       long secs = timeleft / 1000L;
/* 176 */       times = times + secs + " seconds";
/*     */     }
/*     */     else {
/*     */       
/* 180 */       long daysleft = timeleft / 86400000L;
/* 181 */       long hoursleft = (timeleft - daysleft * 86400000L) / 3600000L;
/* 182 */       long minutesleft = (timeleft - daysleft * 86400000L - hoursleft * 3600000L) / 60000L;
/*     */       
/* 184 */       if (daysleft > 0L)
/*     */       {
/* 186 */         times = times + daysleft + " days";
/*     */       }
/* 188 */       if (hoursleft > 0L) {
/*     */         
/* 190 */         String aft = "";
/* 191 */         if (daysleft > 0L && minutesleft > 0L) {
/*     */           
/* 193 */           times = times + ", ";
/* 194 */           aft = aft + " and ";
/*     */         }
/* 196 */         else if (daysleft > 0L) {
/*     */           
/* 198 */           times = times + " and ";
/*     */         }
/* 200 */         else if (minutesleft > 0L) {
/*     */           
/* 202 */           aft = aft + " and ";
/*     */         } 
/* 204 */         times = times + hoursleft + " hours" + aft;
/*     */       } 
/* 206 */       if (minutesleft > 0L) {
/*     */         
/* 208 */         String aft = "";
/* 209 */         if (daysleft > 0L && hoursleft == 0L)
/*     */         {
/* 211 */           aft = " and ";
/*     */         }
/* 213 */         times = times + aft + minutesleft + " minutes";
/*     */       } 
/*     */     } 
/* 216 */     if (times.length() == 0)
/*     */     {
/* 218 */       times = "nothing";
/*     */     }
/* 220 */     return times;
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
/*     */   public static String getWordForNumber(int number) {
/* 235 */     switch (number)
/*     */     
/*     */     { case 1:
/* 238 */         toReturn = "one";
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
/* 271 */         return toReturn;case 2: toReturn = "two"; return toReturn;case 3: toReturn = "three"; return toReturn;case 4: toReturn = "four"; return toReturn;case 5: toReturn = "five"; return toReturn;case 6: toReturn = "six"; return toReturn;case 7: toReturn = "seven"; return toReturn;case 8: toReturn = "eight"; return toReturn;case 9: toReturn = "nine"; return toReturn;case 10: toReturn = "ten"; return toReturn; }  String toReturn = String.valueOf(number); return toReturn;
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
/*     */   public static String replace(String target, String from, String to) {
/* 286 */     int start = target.indexOf(from);
/* 287 */     if (start == -1)
/*     */     {
/* 289 */       return target;
/*     */     }
/* 291 */     int lf = from.length();
/* 292 */     char[] targetChars = target.toCharArray();
/* 293 */     StringBuilder buffer = new StringBuilder();
/* 294 */     int copyFrom = 0;
/* 295 */     while (start != -1) {
/*     */       
/* 297 */       buffer.append(targetChars, copyFrom, start - copyFrom);
/* 298 */       buffer.append(to);
/* 299 */       copyFrom = start + lf;
/* 300 */       start = target.indexOf(from, copyFrom);
/*     */     } 
/* 302 */     buffer.append(targetChars, copyFrom, targetChars.length - copyFrom);
/* 303 */     return buffer.toString();
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
/*     */   public static boolean isVowel(char aLetter) {
/* 316 */     return ("aeiouAEIOU".indexOf(aLetter) != -1);
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
/*     */   public static boolean isConsonant(char aLetter) {
/* 329 */     return !isVowel(aLetter);
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
/*     */   public static String htmlify(String aLine) {
/* 345 */     String lLine = aLine.replaceAll("&", "&amp;");
/* 346 */     lLine = lLine.replaceAll("<", "&lt;");
/* 347 */     lLine = lLine.replaceAll(">", "&gt;");
/*     */     
/* 349 */     return lLine;
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
/*     */   public static String toHexString(byte[] bytes) {
/* 361 */     StringBuilder sb = new StringBuilder(bytes.length * 3);
/* 362 */     for (int b : bytes) {
/*     */       
/* 364 */       b &= 0xFF;
/* 365 */       sb.append(HEX_DIGITS[b >> 4]);
/* 366 */       sb.append(HEX_DIGITS[b & 0xF]);
/* 367 */       sb.append(' ');
/*     */     } 
/* 369 */     return sb.toString();
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
/*     */   public static String addGenus(String name) {
/* 384 */     char firstLetter = name.charAt(0);
/* 385 */     char lastLetter = name.charAt(name.length() - 1);
/* 386 */     StringBuilder builder2 = new StringBuilder(name.length() + 5);
/* 387 */     builder2.setLength(0);
/* 388 */     if (lastLetter == 's') {
/* 389 */       builder2.append("some ");
/* 390 */     } else if (isVowel(firstLetter)) {
/* 391 */       builder2.append("an ");
/*     */     } else {
/* 393 */       builder2.append("a ");
/* 394 */     }  builder2.append(name);
/* 395 */     return builder2.toString();
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
/*     */   public static String addGenus(String name, boolean plural) {
/* 410 */     char firstLetter = name.charAt(0);
/* 411 */     StringBuilder builder2 = new StringBuilder(name.length() + 5);
/* 412 */     builder2.setLength(0);
/* 413 */     if (plural) {
/* 414 */       builder2.append("some ");
/* 415 */     } else if (isVowel(firstLetter)) {
/* 416 */       builder2.append("an ");
/*     */     } else {
/* 418 */       builder2.append("a ");
/* 419 */     }  builder2.append(name);
/* 420 */     return builder2.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\StringUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */