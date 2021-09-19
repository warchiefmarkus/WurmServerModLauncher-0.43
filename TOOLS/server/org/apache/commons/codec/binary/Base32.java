/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base32
/*     */   extends BaseNCodec
/*     */ {
/*     */   private static final int BITS_PER_ENCODED_BYTE = 5;
/*     */   private static final int BYTES_PER_ENCODED_BLOCK = 8;
/*     */   private static final int BYTES_PER_UNENCODED_BLOCK = 5;
/*  60 */   private static final byte[] CHUNK_SEPARATOR = new byte[] { 13, 10 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private static final byte[] DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 63, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private static final byte[] ENCODE_TABLE = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final byte[] HEX_DECODE_TABLE = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 63, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private static final byte[] HEX_ENCODE_TABLE = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MASK_5BITS = 31;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long bitWorkArea;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int decodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] decodeTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int encodeSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] encodeTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[] lineSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base32() {
/* 162 */     this(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base32(boolean useHex) {
/* 173 */     this(0, (byte[])null, useHex);
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
/*     */   public Base32(int lineLength) {
/* 187 */     this(lineLength, CHUNK_SEPARATOR);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator) {
/* 208 */     this(lineLength, lineSeparator, false);
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
/*     */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex) {
/* 231 */     super(5, 8, lineLength, (lineSeparator == null) ? 0 : lineSeparator.length);
/*     */ 
/*     */     
/* 234 */     if (useHex) {
/* 235 */       this.encodeTable = HEX_ENCODE_TABLE;
/* 236 */       this.decodeTable = HEX_DECODE_TABLE;
/*     */     } else {
/* 238 */       this.encodeTable = ENCODE_TABLE;
/* 239 */       this.decodeTable = DECODE_TABLE;
/*     */     } 
/* 241 */     if (lineLength > 0) {
/* 242 */       if (lineSeparator == null) {
/* 243 */         throw new IllegalArgumentException("lineLength " + lineLength + " > 0, but lineSeparator is null");
/*     */       }
/*     */       
/* 246 */       if (containsAlphabetOrPad(lineSeparator)) {
/* 247 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/* 248 */         throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + sep + "]");
/*     */       } 
/* 250 */       this.encodeSize = 8 + lineSeparator.length;
/* 251 */       this.lineSeparator = new byte[lineSeparator.length];
/* 252 */       System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*     */     } else {
/* 254 */       this.encodeSize = 8;
/* 255 */       this.lineSeparator = null;
/*     */     } 
/* 257 */     this.decodeSize = this.encodeSize - 1;
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
/*     */   void decode(byte[] in, int inPos, int inAvail) {
/* 283 */     if (this.eof) {
/*     */       return;
/*     */     }
/* 286 */     if (inAvail < 0) {
/* 287 */       this.eof = true;
/*     */     }
/* 289 */     for (int i = 0; i < inAvail; i++) {
/* 290 */       byte b = in[inPos++];
/* 291 */       if (b == 61) {
/*     */         
/* 293 */         this.eof = true;
/*     */         break;
/*     */       } 
/* 296 */       ensureBufferSize(this.decodeSize);
/* 297 */       if (b >= 0 && b < this.decodeTable.length) {
/* 298 */         int result = this.decodeTable[b];
/* 299 */         if (result >= 0) {
/* 300 */           this.modulus = (this.modulus + 1) % 8;
/* 301 */           this.bitWorkArea = (this.bitWorkArea << 5L) + result;
/* 302 */           if (this.modulus == 0) {
/* 303 */             this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 32L & 0xFFL);
/* 304 */             this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 24L & 0xFFL);
/* 305 */             this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 16L & 0xFFL);
/* 306 */             this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 8L & 0xFFL);
/* 307 */             this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea & 0xFFL);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     if (this.eof && this.modulus >= 2) {
/* 318 */       ensureBufferSize(this.decodeSize);
/*     */ 
/*     */       
/* 321 */       switch (this.modulus) {
/*     */         case 2:
/* 323 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 2L & 0xFFL);
/*     */           break;
/*     */         case 3:
/* 326 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 7L & 0xFFL);
/*     */           break;
/*     */         case 4:
/* 329 */           this.bitWorkArea >>= 4L;
/* 330 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 8L & 0xFFL);
/* 331 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea & 0xFFL);
/*     */           break;
/*     */         case 5:
/* 334 */           this.bitWorkArea >>= 1L;
/* 335 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 16L & 0xFFL);
/* 336 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 8L & 0xFFL);
/* 337 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea & 0xFFL);
/*     */           break;
/*     */         case 6:
/* 340 */           this.bitWorkArea >>= 6L;
/* 341 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 16L & 0xFFL);
/* 342 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 8L & 0xFFL);
/* 343 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea & 0xFFL);
/*     */           break;
/*     */         case 7:
/* 346 */           this.bitWorkArea >>= 3L;
/* 347 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 24L & 0xFFL);
/* 348 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 16L & 0xFFL);
/* 349 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea >> 8L & 0xFFL);
/* 350 */           this.buffer[this.pos++] = (byte)(int)(this.bitWorkArea & 0xFFL);
/*     */           break;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void encode(byte[] in, int inPos, int inAvail) {
/* 372 */     if (this.eof) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 377 */     if (inAvail < 0) {
/* 378 */       this.eof = true;
/* 379 */       if (0 == this.modulus && this.lineLength == 0) {
/*     */         return;
/*     */       }
/* 382 */       ensureBufferSize(this.encodeSize);
/* 383 */       int savedPos = this.pos;
/* 384 */       switch (this.modulus) {
/*     */         case 1:
/* 386 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 3L) & 0x1F];
/* 387 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea << 2L) & 0x1F];
/* 388 */           this.buffer[this.pos++] = 61;
/* 389 */           this.buffer[this.pos++] = 61;
/* 390 */           this.buffer[this.pos++] = 61;
/* 391 */           this.buffer[this.pos++] = 61;
/* 392 */           this.buffer[this.pos++] = 61;
/* 393 */           this.buffer[this.pos++] = 61;
/*     */           break;
/*     */         
/*     */         case 2:
/* 397 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 11L) & 0x1F];
/* 398 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 6L) & 0x1F];
/* 399 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 1L) & 0x1F];
/* 400 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea << 4L) & 0x1F];
/* 401 */           this.buffer[this.pos++] = 61;
/* 402 */           this.buffer[this.pos++] = 61;
/* 403 */           this.buffer[this.pos++] = 61;
/* 404 */           this.buffer[this.pos++] = 61;
/*     */           break;
/*     */         case 3:
/* 407 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 19L) & 0x1F];
/* 408 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 14L) & 0x1F];
/* 409 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 9L) & 0x1F];
/* 410 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 4L) & 0x1F];
/* 411 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea << 1L) & 0x1F];
/* 412 */           this.buffer[this.pos++] = 61;
/* 413 */           this.buffer[this.pos++] = 61;
/* 414 */           this.buffer[this.pos++] = 61;
/*     */           break;
/*     */         case 4:
/* 417 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 27L) & 0x1F];
/* 418 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 22L) & 0x1F];
/* 419 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 17L) & 0x1F];
/* 420 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 12L) & 0x1F];
/* 421 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 7L) & 0x1F];
/* 422 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 2L) & 0x1F];
/* 423 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea << 3L) & 0x1F];
/* 424 */           this.buffer[this.pos++] = 61;
/*     */           break;
/*     */       } 
/* 427 */       this.currentLinePos += this.pos - savedPos;
/*     */       
/* 429 */       if (this.lineLength > 0 && this.currentLinePos > 0) {
/* 430 */         System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
/* 431 */         this.pos += this.lineSeparator.length;
/*     */       } 
/*     */     } else {
/* 434 */       for (int i = 0; i < inAvail; i++) {
/* 435 */         ensureBufferSize(this.encodeSize);
/* 436 */         this.modulus = (this.modulus + 1) % 5;
/* 437 */         int b = in[inPos++];
/* 438 */         if (b < 0) {
/* 439 */           b += 256;
/*     */         }
/* 441 */         this.bitWorkArea = (this.bitWorkArea << 8L) + b;
/* 442 */         if (0 == this.modulus) {
/* 443 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 35L) & 0x1F];
/* 444 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 30L) & 0x1F];
/* 445 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 25L) & 0x1F];
/* 446 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 20L) & 0x1F];
/* 447 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 15L) & 0x1F];
/* 448 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 10L) & 0x1F];
/* 449 */           this.buffer[this.pos++] = this.encodeTable[(int)(this.bitWorkArea >> 5L) & 0x1F];
/* 450 */           this.buffer[this.pos++] = this.encodeTable[(int)this.bitWorkArea & 0x1F];
/* 451 */           this.currentLinePos += 8;
/* 452 */           if (this.lineLength > 0 && this.lineLength <= this.currentLinePos) {
/* 453 */             System.arraycopy(this.lineSeparator, 0, this.buffer, this.pos, this.lineSeparator.length);
/* 454 */             this.pos += this.lineSeparator.length;
/* 455 */             this.currentLinePos = 0;
/*     */           } 
/*     */         } 
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
/*     */   public boolean isInAlphabet(byte octet) {
/* 471 */     return (octet >= 0 && octet < this.decodeTable.length && this.decodeTable[octet] != -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\commons\codec\binary\Base32.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */