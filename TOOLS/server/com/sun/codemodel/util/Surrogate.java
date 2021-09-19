/*     */ package com.sun.codemodel.util;
/*     */ 
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CoderResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Surrogate
/*     */ {
/*     */   public static final char MIN_HIGH = '?';
/*     */   public static final char MAX_HIGH = '?';
/*     */   public static final char MIN_LOW = '?';
/*     */   public static final char MAX_LOW = '?';
/*     */   public static final char MIN = '?';
/*     */   public static final char MAX = '?';
/*     */   public static final int UCS4_MIN = 65536;
/*     */   public static final int UCS4_MAX = 1114111;
/*     */   
/*     */   public static boolean isHigh(int c) {
/*  56 */     return (55296 <= c && c <= 56319);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLow(int c) {
/*  63 */     return (56320 <= c && c <= 57343);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean is(int c) {
/*  70 */     return (55296 <= c && c <= 57343);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean neededFor(int uc) {
/*  78 */     return (uc >= 65536 && uc <= 1114111);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char high(int uc) {
/*  85 */     return (char)(0xD800 | uc - 65536 >> 10 & 0x3FF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char low(int uc) {
/*  92 */     return (char)(0xDC00 | uc - 65536 & 0x3FF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int toUCS4(char c, char d) {
/*  99 */     return ((c & 0x3FF) << 10 | d & 0x3FF) + 65536;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Parser
/*     */   {
/*     */     private int character;
/*     */ 
/*     */ 
/*     */     
/* 111 */     private CoderResult error = CoderResult.UNDERFLOW;
/*     */ 
/*     */     
/*     */     private boolean isPair;
/*     */ 
/*     */     
/*     */     public int character() {
/* 118 */       return this.character;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isPair() {
/* 126 */       return this.isPair;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int increment() {
/* 134 */       return this.isPair ? 2 : 1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CoderResult error() {
/* 142 */       return this.error;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CoderResult unmappableResult() {
/* 150 */       return CoderResult.unmappableForLength(this.isPair ? 2 : 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int parse(char c, CharBuffer in) {
/* 167 */       if (Surrogate.isHigh(c)) {
/* 168 */         if (!in.hasRemaining()) {
/* 169 */           this.error = CoderResult.UNDERFLOW;
/* 170 */           return -1;
/*     */         } 
/* 172 */         char d = in.get();
/* 173 */         if (Surrogate.isLow(d)) {
/* 174 */           this.character = Surrogate.toUCS4(c, d);
/* 175 */           this.isPair = true;
/* 176 */           this.error = null;
/* 177 */           return this.character;
/*     */         } 
/* 179 */         this.error = CoderResult.malformedForLength(1);
/* 180 */         return -1;
/*     */       } 
/* 182 */       if (Surrogate.isLow(c)) {
/* 183 */         this.error = CoderResult.malformedForLength(1);
/* 184 */         return -1;
/*     */       } 
/* 186 */       this.character = c;
/* 187 */       this.isPair = false;
/* 188 */       this.error = null;
/* 189 */       return this.character;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int parse(char c, char[] ia, int ip, int il) {
/* 208 */       if (Surrogate.isHigh(c)) {
/* 209 */         if (il - ip < 2) {
/* 210 */           this.error = CoderResult.UNDERFLOW;
/* 211 */           return -1;
/*     */         } 
/* 213 */         char d = ia[ip + 1];
/* 214 */         if (Surrogate.isLow(d)) {
/* 215 */           this.character = Surrogate.toUCS4(c, d);
/* 216 */           this.isPair = true;
/* 217 */           this.error = null;
/* 218 */           return this.character;
/*     */         } 
/* 220 */         this.error = CoderResult.malformedForLength(1);
/* 221 */         return -1;
/*     */       } 
/* 223 */       if (Surrogate.isLow(c)) {
/* 224 */         this.error = CoderResult.malformedForLength(1);
/* 225 */         return -1;
/*     */       } 
/* 227 */       this.character = c;
/* 228 */       this.isPair = false;
/* 229 */       this.error = null;
/* 230 */       return this.character;
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
/*     */   public static class Generator
/*     */   {
/* 244 */     private CoderResult error = CoderResult.OVERFLOW;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CoderResult error() {
/* 251 */       return this.error;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int generate(int uc, int len, CharBuffer dst) {
/* 269 */       if (uc <= 65535) {
/* 270 */         if (Surrogate.is(uc)) {
/* 271 */           this.error = CoderResult.malformedForLength(len);
/* 272 */           return -1;
/*     */         } 
/* 274 */         if (dst.remaining() < 1) {
/* 275 */           this.error = CoderResult.OVERFLOW;
/* 276 */           return -1;
/*     */         } 
/* 278 */         dst.put((char)uc);
/* 279 */         this.error = null;
/* 280 */         return 1;
/*     */       } 
/* 282 */       if (uc < 65536) {
/* 283 */         this.error = CoderResult.malformedForLength(len);
/* 284 */         return -1;
/*     */       } 
/* 286 */       if (uc <= 1114111) {
/* 287 */         if (dst.remaining() < 2) {
/* 288 */           this.error = CoderResult.OVERFLOW;
/* 289 */           return -1;
/*     */         } 
/* 291 */         dst.put(Surrogate.high(uc));
/* 292 */         dst.put(Surrogate.low(uc));
/* 293 */         this.error = null;
/* 294 */         return 2;
/*     */       } 
/* 296 */       this.error = CoderResult.unmappableForLength(len);
/* 297 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int generate(int uc, int len, char[] da, int dp, int dl) {
/* 317 */       if (uc <= 65535) {
/* 318 */         if (Surrogate.is(uc)) {
/* 319 */           this.error = CoderResult.malformedForLength(len);
/* 320 */           return -1;
/*     */         } 
/* 322 */         if (dl - dp < 1) {
/* 323 */           this.error = CoderResult.OVERFLOW;
/* 324 */           return -1;
/*     */         } 
/* 326 */         da[dp] = (char)uc;
/* 327 */         this.error = null;
/* 328 */         return 1;
/*     */       } 
/* 330 */       if (uc < 65536) {
/* 331 */         this.error = CoderResult.malformedForLength(len);
/* 332 */         return -1;
/*     */       } 
/* 334 */       if (uc <= 1114111) {
/* 335 */         if (dl - dp < 2) {
/* 336 */           this.error = CoderResult.OVERFLOW;
/* 337 */           return -1;
/*     */         } 
/* 339 */         da[dp] = Surrogate.high(uc);
/* 340 */         da[dp + 1] = Surrogate.low(uc);
/* 341 */         this.error = null;
/* 342 */         return 2;
/*     */       } 
/* 344 */       this.error = CoderResult.unmappableForLength(len);
/* 345 */       return -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemode\\util\Surrogate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */