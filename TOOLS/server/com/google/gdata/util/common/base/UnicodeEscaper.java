/*     */ package com.google.gdata.util.common.base;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UnicodeEscaper
/*     */   implements Escaper
/*     */ {
/*     */   private static final int DEST_PAD = 32;
/*     */   
/*     */   protected abstract char[] escape(int paramInt);
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int start, int end) {
/* 108 */     int index = start;
/* 109 */     while (index < end) {
/* 110 */       int cp = codePointAt(csq, index, end);
/* 111 */       if (cp < 0 || escape(cp) != null) {
/*     */         break;
/*     */       }
/* 114 */       index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
/*     */     } 
/* 116 */     return index;
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
/*     */   public String escape(String string) {
/* 143 */     int end = string.length();
/* 144 */     int index = nextEscapeIndex(string, 0, end);
/* 145 */     return (index == end) ? string : escapeSlow(string, index);
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
/*     */   protected final String escapeSlow(String s, int index) {
/* 166 */     int end = s.length();
/*     */ 
/*     */     
/* 169 */     char[] dest = DEST_TL.get();
/* 170 */     int destIndex = 0;
/* 171 */     int unescapedChunkStart = 0;
/*     */     
/* 173 */     while (index < end) {
/* 174 */       int cp = codePointAt(s, index, end);
/* 175 */       if (cp < 0) {
/* 176 */         throw new IllegalArgumentException("Trailing high surrogate at end of input");
/*     */       }
/*     */       
/* 179 */       char[] escaped = escape(cp);
/* 180 */       if (escaped != null) {
/* 181 */         int i = index - unescapedChunkStart;
/*     */ 
/*     */ 
/*     */         
/* 185 */         int sizeNeeded = destIndex + i + escaped.length;
/* 186 */         if (dest.length < sizeNeeded) {
/* 187 */           int destLength = sizeNeeded + end - index + 32;
/* 188 */           dest = growBuffer(dest, destIndex, destLength);
/*     */         } 
/*     */         
/* 191 */         if (i > 0) {
/* 192 */           s.getChars(unescapedChunkStart, index, dest, destIndex);
/* 193 */           destIndex += i;
/*     */         } 
/* 195 */         if (escaped.length > 0) {
/* 196 */           System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
/* 197 */           destIndex += escaped.length;
/*     */         } 
/*     */       } 
/* 200 */       unescapedChunkStart = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
/*     */       
/* 202 */       index = nextEscapeIndex(s, unescapedChunkStart, end);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 207 */     int charsSkipped = end - unescapedChunkStart;
/* 208 */     if (charsSkipped > 0) {
/* 209 */       int endIndex = destIndex + charsSkipped;
/* 210 */       if (dest.length < endIndex) {
/* 211 */         dest = growBuffer(dest, destIndex, endIndex);
/*     */       }
/* 213 */       s.getChars(unescapedChunkStart, end, dest, destIndex);
/* 214 */       destIndex = endIndex;
/*     */     } 
/* 216 */     return new String(dest, 0, destIndex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Appendable escape(final Appendable out) {
/* 254 */     Preconditions.checkNotNull(out);
/*     */     
/* 256 */     return new Appendable() {
/* 257 */         int pendingHighSurrogate = -1;
/* 258 */         char[] decodedChars = new char[2];
/*     */         
/*     */         public Appendable append(CharSequence csq) throws IOException {
/* 261 */           return append(csq, 0, csq.length());
/*     */         }
/*     */ 
/*     */         
/*     */         public Appendable append(CharSequence csq, int start, int end) throws IOException {
/* 266 */           int index = start;
/* 267 */           if (index < end) {
/*     */ 
/*     */ 
/*     */             
/* 271 */             int unescapedChunkStart = index;
/* 272 */             if (this.pendingHighSurrogate != -1) {
/*     */ 
/*     */               
/* 275 */               char c = csq.charAt(index++);
/* 276 */               if (!Character.isLowSurrogate(c)) {
/* 277 */                 throw new IllegalArgumentException("Expected low surrogate character but got " + c);
/*     */               }
/*     */               
/* 280 */               char[] escaped = UnicodeEscaper.this.escape(Character.toCodePoint((char)this.pendingHighSurrogate, c));
/*     */               
/* 282 */               if (escaped != null) {
/*     */ 
/*     */                 
/* 285 */                 outputChars(escaped, escaped.length);
/* 286 */                 unescapedChunkStart++;
/*     */               }
/*     */               else {
/*     */                 
/* 290 */                 out.append((char)this.pendingHighSurrogate);
/*     */               } 
/* 292 */               this.pendingHighSurrogate = -1;
/*     */             } 
/*     */             
/*     */             while (true) {
/* 296 */               index = UnicodeEscaper.this.nextEscapeIndex(csq, index, end);
/* 297 */               if (index > unescapedChunkStart) {
/* 298 */                 out.append(csq, unescapedChunkStart, index);
/*     */               }
/* 300 */               if (index == end) {
/*     */                 break;
/*     */               }
/*     */               
/* 304 */               int cp = UnicodeEscaper.codePointAt(csq, index, end);
/* 305 */               if (cp < 0) {
/*     */ 
/*     */                 
/* 308 */                 this.pendingHighSurrogate = -cp;
/*     */                 
/*     */                 break;
/*     */               } 
/* 312 */               char[] escaped = UnicodeEscaper.this.escape(cp);
/* 313 */               if (escaped != null) {
/* 314 */                 outputChars(escaped, escaped.length);
/*     */               }
/*     */               else {
/*     */                 
/* 318 */                 int len = Character.toChars(cp, this.decodedChars, 0);
/* 319 */                 outputChars(this.decodedChars, len);
/*     */               } 
/*     */               
/* 322 */               index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
/* 323 */               unescapedChunkStart = index;
/*     */             } 
/*     */           } 
/* 326 */           return this;
/*     */         }
/*     */         
/*     */         public Appendable append(char c) throws IOException {
/* 330 */           if (this.pendingHighSurrogate != -1) {
/*     */ 
/*     */             
/* 333 */             if (!Character.isLowSurrogate(c)) {
/* 334 */               throw new IllegalArgumentException("Expected low surrogate character but got '" + c + "' with value " + c);
/*     */             }
/*     */ 
/*     */             
/* 338 */             char[] escaped = UnicodeEscaper.this.escape(Character.toCodePoint((char)this.pendingHighSurrogate, c));
/*     */             
/* 340 */             if (escaped != null) {
/* 341 */               outputChars(escaped, escaped.length);
/*     */             } else {
/* 343 */               out.append((char)this.pendingHighSurrogate);
/* 344 */               out.append(c);
/*     */             } 
/* 346 */             this.pendingHighSurrogate = -1;
/* 347 */           } else if (Character.isHighSurrogate(c)) {
/*     */             
/* 349 */             this.pendingHighSurrogate = c;
/*     */           } else {
/* 351 */             if (Character.isLowSurrogate(c)) {
/* 352 */               throw new IllegalArgumentException("Unexpected low surrogate character '" + c + "' with value " + c);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 357 */             char[] escaped = UnicodeEscaper.this.escape(c);
/* 358 */             if (escaped != null) {
/* 359 */               outputChars(escaped, escaped.length);
/*     */             } else {
/* 361 */               out.append(c);
/*     */             } 
/*     */           } 
/* 364 */           return this;
/*     */         }
/*     */         
/*     */         private void outputChars(char[] chars, int len) throws IOException {
/* 368 */           for (int n = 0; n < len; n++) {
/* 369 */             out.append(chars[n]);
/*     */           }
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final int codePointAt(CharSequence seq, int index, int end) {
/* 408 */     if (index < end) {
/* 409 */       char c1 = seq.charAt(index++);
/* 410 */       if (c1 < '?' || c1 > '?')
/*     */       {
/*     */         
/* 413 */         return c1; } 
/* 414 */       if (c1 <= '?') {
/*     */         
/* 416 */         if (index == end) {
/* 417 */           return -c1;
/*     */         }
/*     */         
/* 420 */         char c2 = seq.charAt(index);
/* 421 */         if (Character.isLowSurrogate(c2)) {
/* 422 */           return Character.toCodePoint(c1, c2);
/*     */         }
/* 424 */         throw new IllegalArgumentException("Expected low surrogate but got char '" + c2 + "' with value " + c2 + " at index " + index);
/*     */       } 
/*     */ 
/*     */       
/* 428 */       throw new IllegalArgumentException("Unexpected low surrogate character '" + c1 + "' with value " + c1 + " at index " + (index - 1));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 433 */     throw new IndexOutOfBoundsException("Index exceeds specified range");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char[] growBuffer(char[] dest, int index, int size) {
/* 442 */     char[] copy = new char[size];
/* 443 */     if (index > 0) {
/* 444 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 446 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 454 */   private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>()
/*     */     {
/*     */       protected char[] initialValue() {
/* 457 */         return new char[1024];
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\gdat\\util\common\base\UnicodeEscaper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */