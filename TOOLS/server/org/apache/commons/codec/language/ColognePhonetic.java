/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColognePhonetic
/*     */   implements StringEncoder
/*     */ {
/*     */   private abstract class CologneBuffer
/*     */   {
/*     */     protected final char[] data;
/* 189 */     protected int length = 0;
/*     */     
/*     */     public CologneBuffer(char[] data) {
/* 192 */       this.data = data;
/* 193 */       this.length = data.length;
/*     */     }
/*     */     
/*     */     public CologneBuffer(int buffSize) {
/* 197 */       this.data = new char[buffSize];
/* 198 */       this.length = 0;
/*     */     }
/*     */     
/*     */     protected abstract char[] copyData(int param1Int1, int param1Int2);
/*     */     
/*     */     public int length() {
/* 204 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 209 */       return new String(copyData(0, this.length));
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneOutputBuffer
/*     */     extends CologneBuffer {
/*     */     public CologneOutputBuffer(int buffSize) {
/* 216 */       super(buffSize);
/*     */     }
/*     */     
/*     */     public void addRight(char chr) {
/* 220 */       this.data[this.length] = chr;
/* 221 */       this.length++;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] copyData(int start, int length) {
/* 226 */       char[] newData = new char[length];
/* 227 */       System.arraycopy(this.data, start, newData, 0, length);
/* 228 */       return newData;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneInputBuffer
/*     */     extends CologneBuffer {
/*     */     public CologneInputBuffer(char[] data) {
/* 235 */       super(data);
/*     */     }
/*     */     
/*     */     public void addLeft(char ch) {
/* 239 */       this.length++;
/* 240 */       this.data[getNextPos()] = ch;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] copyData(int start, int length) {
/* 245 */       char[] newData = new char[length];
/* 246 */       System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
/* 247 */       return newData;
/*     */     }
/*     */     
/*     */     public char getNextChar() {
/* 251 */       return this.data[getNextPos()];
/*     */     }
/*     */     
/*     */     protected int getNextPos() {
/* 255 */       return this.data.length - this.length;
/*     */     }
/*     */     
/*     */     public char removeNext() {
/* 259 */       char ch = getNextChar();
/* 260 */       this.length--;
/* 261 */       return ch;
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
/* 274 */   private static final char[][] PREPROCESS_MAP = new char[][] { { 'Ä', 'A' }, { 'Ü', 'U' }, { 'Ö', 'O' }, { 'ß', 'S' } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean arrayContains(char[] arr, char key) {
/* 285 */     for (char element : arr) {
/* 286 */       if (element == key) {
/* 287 */         return true;
/*     */       }
/*     */     } 
/* 290 */     return false;
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
/*     */   public String colognePhonetic(String text) {
/* 305 */     if (text == null) {
/* 306 */       return null;
/*     */     }
/*     */     
/* 309 */     text = preprocess(text);
/*     */     
/* 311 */     CologneOutputBuffer output = new CologneOutputBuffer(text.length() * 2);
/* 312 */     CologneInputBuffer input = new CologneInputBuffer(text.toCharArray());
/*     */ 
/*     */ 
/*     */     
/* 316 */     char lastChar = '-';
/* 317 */     char lastCode = '/';
/*     */ 
/*     */ 
/*     */     
/* 321 */     int rightLength = input.length();
/*     */     
/* 323 */     while (rightLength > 0) {
/* 324 */       char nextChar, code, chr = input.removeNext();
/*     */       
/* 326 */       if ((rightLength = input.length()) > 0) {
/* 327 */         nextChar = input.getNextChar();
/*     */       } else {
/* 329 */         nextChar = '-';
/*     */       } 
/*     */       
/* 332 */       if (arrayContains(new char[] { 'A', 'E', 'I', 'J', 'O', 'U', 'Y' }, chr)) {
/* 333 */         code = '0';
/* 334 */       } else if (chr == 'H' || chr < 'A' || chr > 'Z') {
/* 335 */         if (lastCode == '/') {
/*     */           continue;
/*     */         }
/* 338 */         code = '-';
/* 339 */       } else if (chr == 'B' || (chr == 'P' && nextChar != 'H')) {
/* 340 */         code = '1';
/* 341 */       } else if ((chr == 'D' || chr == 'T') && !arrayContains(new char[] { 'S', 'C', 'Z' }, nextChar)) {
/* 342 */         code = '2';
/* 343 */       } else if (arrayContains(new char[] { 'W', 'F', 'P', 'V' }, chr)) {
/* 344 */         code = '3';
/* 345 */       } else if (arrayContains(new char[] { 'G', 'K', 'Q' }, chr)) {
/* 346 */         code = '4';
/* 347 */       } else if (chr == 'X' && !arrayContains(new char[] { 'C', 'K', 'Q' }, lastChar)) {
/* 348 */         code = '4';
/* 349 */         input.addLeft('S');
/* 350 */         rightLength++;
/* 351 */       } else if (chr == 'S' || chr == 'Z') {
/* 352 */         code = '8';
/* 353 */       } else if (chr == 'C') {
/* 354 */         if (lastCode == '/') {
/* 355 */           if (arrayContains(new char[] { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X' }, nextChar)) {
/* 356 */             code = '4';
/*     */           } else {
/* 358 */             code = '8';
/*     */           }
/*     */         
/* 361 */         } else if (arrayContains(new char[] { 'S', 'Z' }, lastChar) || !arrayContains(new char[] { 'A', 'H', 'O', 'U', 'K', 'Q', 'X' }, nextChar)) {
/*     */           
/* 363 */           code = '8';
/*     */         } else {
/* 365 */           code = '4';
/*     */         }
/*     */       
/* 368 */       } else if (arrayContains(new char[] { 'T', 'D', 'X' }, chr)) {
/* 369 */         code = '8';
/* 370 */       } else if (chr == 'R') {
/* 371 */         code = '7';
/* 372 */       } else if (chr == 'L') {
/* 373 */         code = '5';
/* 374 */       } else if (chr == 'M' || chr == 'N') {
/* 375 */         code = '6';
/*     */       } else {
/* 377 */         code = chr;
/*     */       } 
/*     */       
/* 380 */       if (code != '-' && ((lastCode != code && (code != '0' || lastCode == '/')) || code < '0' || code > '8')) {
/* 381 */         output.addRight(code);
/*     */       }
/*     */       
/* 384 */       lastChar = chr;
/* 385 */       lastCode = code;
/*     */     } 
/* 387 */     return output.toString();
/*     */   }
/*     */   
/*     */   public Object encode(Object object) throws EncoderException {
/* 391 */     if (!(object instanceof String)) {
/* 392 */       throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 398 */     return encode((String)object);
/*     */   }
/*     */   
/*     */   public String encode(String text) {
/* 402 */     return colognePhonetic(text);
/*     */   }
/*     */   
/*     */   public boolean isEncodeEqual(String text1, String text2) {
/* 406 */     return colognePhonetic(text1).equals(colognePhonetic(text2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String preprocess(String text) {
/* 413 */     text = text.toUpperCase(Locale.GERMAN);
/*     */     
/* 415 */     char[] chrs = text.toCharArray();
/*     */     
/* 417 */     for (int index = 0; index < chrs.length; index++) {
/* 418 */       if (chrs[index] > 'Z') {
/* 419 */         for (char[] element : PREPROCESS_MAP) {
/* 420 */           if (chrs[index] == element[0]) {
/* 421 */             chrs[index] = element[1];
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 427 */     return new String(chrs);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\commons\codec\language\ColognePhonetic.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */