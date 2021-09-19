/*     */ package org.kohsuke.rngom.parse.compact;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import org.kohsuke.rngom.ast.builder.BuildException;
/*     */ import org.kohsuke.rngom.util.Utf16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UCode_UCodeESC_CharStream
/*     */ {
/*     */   public static final boolean staticFlag = false;
/*     */   
/*     */   static final int hexval(char c) {
/*  19 */     switch (c) {
/*     */       case '0':
/*  21 */         return 0;
/*     */       case '1':
/*  23 */         return 1;
/*     */       case '2':
/*  25 */         return 2;
/*     */       case '3':
/*  27 */         return 3;
/*     */       case '4':
/*  29 */         return 4;
/*     */       case '5':
/*  31 */         return 5;
/*     */       case '6':
/*  33 */         return 6;
/*     */       case '7':
/*  35 */         return 7;
/*     */       case '8':
/*  37 */         return 8;
/*     */       case '9':
/*  39 */         return 9;
/*     */       
/*     */       case 'A':
/*     */       case 'a':
/*  43 */         return 10;
/*     */       case 'B':
/*     */       case 'b':
/*  46 */         return 11;
/*     */       case 'C':
/*     */       case 'c':
/*  49 */         return 12;
/*     */       case 'D':
/*     */       case 'd':
/*  52 */         return 13;
/*     */       case 'E':
/*     */       case 'e':
/*  55 */         return 14;
/*     */       case 'F':
/*     */       case 'f':
/*  58 */         return 15;
/*     */     } 
/*  60 */     return -1;
/*     */   }
/*     */   
/*  63 */   public int bufpos = -1;
/*     */   
/*     */   int bufsize;
/*     */   int available;
/*     */   int tokenBegin;
/*     */   private int[] bufline;
/*     */   private int[] bufcolumn;
/*  70 */   private int column = 0;
/*  71 */   private int line = 1;
/*     */   
/*     */   private Reader inputStream;
/*     */   
/*     */   private boolean closed = false;
/*     */   
/*     */   private boolean prevCharIsLF = false;
/*     */   private char[] nextCharBuf;
/*     */   private char[] buffer;
/*  80 */   private int maxNextCharInd = 0;
/*  81 */   private int nextCharInd = -1;
/*  82 */   private int inBuf = 0;
/*     */   
/*     */   private final void ExpandBuff(boolean wrapAround) {
/*  85 */     char[] newbuffer = new char[this.bufsize + 2048];
/*  86 */     int[] newbufline = new int[this.bufsize + 2048];
/*  87 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */     
/*  89 */     if (wrapAround) {
/*  90 */       System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/*  91 */       System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
/*     */       
/*  93 */       this.buffer = newbuffer;
/*     */       
/*  95 */       System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/*  96 */       System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/*  97 */       this.bufline = newbufline;
/*     */       
/*  99 */       System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 100 */       System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/* 101 */       this.bufcolumn = newbufcolumn;
/*     */       
/* 103 */       this.bufpos += this.bufsize - this.tokenBegin;
/*     */     } else {
/*     */       
/* 106 */       System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 107 */       this.buffer = newbuffer;
/*     */       
/* 109 */       System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 110 */       this.bufline = newbufline;
/*     */       
/* 112 */       System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 113 */       this.bufcolumn = newbufcolumn;
/*     */       
/* 115 */       this.bufpos -= this.tokenBegin;
/*     */     } 
/*     */     
/* 118 */     this.available = this.bufsize += 2048;
/* 119 */     this.tokenBegin = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private final void FillBuff() throws EOFException {
/* 124 */     if (this.maxNextCharInd == 4096) {
/* 125 */       this.maxNextCharInd = this.nextCharInd = 0;
/*     */     }
/* 127 */     if (this.closed)
/* 128 */       throw new EOFException();  try {
/*     */       int i;
/* 130 */       if ((i = this.inputStream.read(this.nextCharBuf, this.maxNextCharInd, 4096 - this.maxNextCharInd)) == -1) {
/* 131 */         this.closed = true;
/* 132 */         this.inputStream.close();
/* 133 */         throw new EOFException();
/*     */       } 
/*     */       
/* 136 */       this.maxNextCharInd += i;
/*     */     }
/* 138 */     catch (IOException e) {
/* 139 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private final char ReadChar() throws EOFException {
/* 144 */     if (++this.nextCharInd >= this.maxNextCharInd) {
/* 145 */       FillBuff();
/*     */     }
/* 147 */     return this.nextCharBuf[this.nextCharInd];
/*     */   }
/*     */   
/*     */   private final char PeekChar() throws EOFException {
/* 151 */     char c = ReadChar();
/* 152 */     this.nextCharInd--;
/* 153 */     return c;
/*     */   }
/*     */   
/*     */   public final char BeginToken() throws EOFException {
/* 157 */     if (this.inBuf > 0) {
/* 158 */       this.inBuf--;
/* 159 */       return this.buffer[this.tokenBegin = (this.bufpos == this.bufsize - 1) ? (this.bufpos = 0) : ++this.bufpos];
/*     */     } 
/*     */ 
/*     */     
/* 163 */     this.tokenBegin = 0;
/* 164 */     this.bufpos = -1;
/*     */     
/* 166 */     return readChar();
/*     */   }
/*     */   
/*     */   private final void AdjustBuffSize() {
/* 170 */     if (this.available == this.bufsize) {
/* 171 */       if (this.tokenBegin > 2048) {
/* 172 */         this.bufpos = 0;
/* 173 */         this.available = this.tokenBegin;
/*     */       } else {
/*     */         
/* 176 */         ExpandBuff(false);
/*     */       } 
/* 178 */     } else if (this.available > this.tokenBegin) {
/* 179 */       this.available = this.bufsize;
/* 180 */     } else if (this.tokenBegin - this.available < 2048) {
/* 181 */       ExpandBuff(true);
/*     */     } else {
/* 183 */       this.available = this.tokenBegin;
/*     */     } 
/*     */   }
/*     */   private final void UpdateLineColumn(char c) {
/* 187 */     this.column++;
/*     */     
/* 189 */     if (this.prevCharIsLF) {
/* 190 */       this.prevCharIsLF = false;
/* 191 */       this.line += this.column = 1;
/*     */     } 
/*     */     
/* 194 */     switch (c) {
/*     */       case '\000':
/* 196 */         this.prevCharIsLF = true;
/*     */         break;
/*     */       case '\t':
/* 199 */         this.column--;
/* 200 */         this.column += 8 - (this.column & 0x7);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 206 */     this.bufline[this.bufpos] = this.line;
/* 207 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */   
/* 210 */   private final char NEWLINE_MARKER = Character.MIN_VALUE; private static final char BOM = '﻿';
/*     */   public final char readChar() throws EOFException {
/*     */     char c;
/* 213 */     if (this.inBuf > 0) {
/* 214 */       this.inBuf--;
/* 215 */       return this.buffer[(this.bufpos == this.bufsize - 1) ? (this.bufpos = 0) : ++this.bufpos];
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 220 */       c = ReadChar();
/* 221 */       switch (c) {
/*     */         case '\r':
/* 223 */           c = Character.MIN_VALUE;
/*     */           try {
/* 225 */             if (PeekChar() == '\n') {
/* 226 */               ReadChar();
/*     */             }
/* 228 */           } catch (EOFException e) {}
/*     */           break;
/*     */         
/*     */         case '\n':
/* 232 */           c = Character.MIN_VALUE;
/*     */           break;
/*     */         case '\t':
/*     */           break;
/*     */         default:
/* 237 */           if (c >= ' ') {
/* 238 */             if (Utf16.isSurrogate(c)) {
/* 239 */               if (Utf16.isSurrogate2(c))
/* 240 */                 throw new EscapeSyntaxException("illegal_surrogate_pair", this.line, this.column + 1); 
/* 241 */               if (++this.bufpos == this.available)
/* 242 */                 AdjustBuffSize(); 
/* 243 */               this.buffer[this.bufpos] = c;
/*     */               
/*     */               try {
/* 246 */                 c = ReadChar();
/*     */               }
/* 248 */               catch (EOFException e) {
/* 249 */                 throw new EscapeSyntaxException("illegal_surrogate_pair", this.line, this.column + 1);
/*     */               } 
/* 251 */               if (!Utf16.isSurrogate2(c)) {
/* 252 */                 throw new EscapeSyntaxException("illegal_surrogate_pair", this.line, this.column + 2);
/*     */               }
/*     */             } 
/*     */             break;
/*     */           } 
/*     */         case '￾':
/*     */         case '￿':
/* 259 */           throw new EscapeSyntaxException("illegal_char_code", this.line, this.column + 1);
/*     */       } 
/*     */     
/* 262 */     } catch (EOFException e) {
/* 263 */       if (this.bufpos == -1) {
/* 264 */         if (++this.bufpos == this.available)
/* 265 */           AdjustBuffSize(); 
/* 266 */         this.bufline[this.bufpos] = this.line;
/* 267 */         this.bufcolumn[this.bufpos] = this.column;
/*     */       } 
/* 269 */       throw e;
/*     */     } 
/* 271 */     if (++this.bufpos == this.available)
/* 272 */       AdjustBuffSize(); 
/* 273 */     this.buffer[this.bufpos] = c;
/* 274 */     UpdateLineColumn(c);
/*     */     try {
/* 276 */       if (c != '\\' || PeekChar() != 'x') {
/* 277 */         return c;
/*     */       }
/* 279 */     } catch (EOFException e) {
/* 280 */       return c;
/*     */     } 
/*     */     
/* 283 */     int xCnt = 1;
/*     */     while (true) {
/* 285 */       ReadChar();
/* 286 */       if (++this.bufpos == this.available)
/* 287 */         AdjustBuffSize(); 
/* 288 */       this.buffer[this.bufpos] = 'x';
/* 289 */       UpdateLineColumn('x');
/*     */       try {
/* 291 */         c = PeekChar();
/*     */       }
/* 293 */       catch (EOFException e) {
/* 294 */         backup(xCnt);
/* 295 */         return '\\';
/*     */       } 
/* 297 */       if (c == '{') {
/* 298 */         ReadChar();
/* 299 */         this.column++;
/*     */         
/* 301 */         this.bufpos -= xCnt;
/* 302 */         if (this.bufpos < 0)
/* 303 */           this.bufpos += this.bufsize; 
/*     */         break;
/*     */       } 
/* 306 */       if (c != 'x') {
/* 307 */         backup(xCnt);
/* 308 */         return '\\';
/*     */       } 
/* 310 */       xCnt++;
/*     */     } 
/*     */     try {
/* 313 */       int scalarValue = hexval(ReadChar());
/* 314 */       this.column++;
/* 315 */       if (scalarValue < 0)
/* 316 */         throw new EscapeSyntaxException("illegal_hex_digit", this.line, this.column); 
/* 317 */       while ((c = ReadChar()) != '}') {
/* 318 */         this.column++;
/* 319 */         int n = hexval(c);
/* 320 */         if (n < 0)
/* 321 */           throw new EscapeSyntaxException("illegal_hex_digit", this.line, this.column); 
/* 322 */         scalarValue <<= 4;
/* 323 */         scalarValue |= n;
/* 324 */         if (scalarValue >= 1114112)
/* 325 */           throw new EscapeSyntaxException("char_code_too_big", this.line, this.column); 
/*     */       } 
/* 327 */       this.column++;
/* 328 */       if (scalarValue <= 65535)
/* 329 */       { c = (char)scalarValue;
/* 330 */         switch (c)
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           case '\t':
/*     */           case '\n':
/*     */           case '\r':
/* 343 */             this.buffer[this.bufpos] = c;
/* 344 */             return c;default: if (c >= ' ' && !Utf16.isSurrogate(c)) { this.buffer[this.bufpos] = c; return c; }  break;
/*     */           case '￾': case '￿':
/* 346 */             break; }  throw new EscapeSyntaxException("illegal_char_code_ref", this.line, this.column); }  c = Utf16.surrogate1(scalarValue);
/* 347 */       this.buffer[this.bufpos] = c;
/* 348 */       int bufpos1 = this.bufpos;
/* 349 */       if (++this.bufpos == this.bufsize)
/* 350 */         this.bufpos = 0; 
/* 351 */       this.buffer[this.bufpos] = Utf16.surrogate2(scalarValue);
/* 352 */       this.bufline[this.bufpos] = this.bufline[bufpos1];
/* 353 */       this.bufcolumn[this.bufpos] = this.bufcolumn[bufpos1];
/* 354 */       backup(1);
/* 355 */       return c;
/*     */     }
/* 357 */     catch (EOFException e) {
/* 358 */       throw new EscapeSyntaxException("incomplete_escape", this.line, this.column);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getColumn() {
/* 368 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getLine() {
/* 377 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndColumn() {
/* 381 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getEndLine() {
/* 385 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public final int getBeginColumn() {
/* 389 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public final int getBeginLine() {
/* 393 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */ 
/*     */   
/*     */   public final void backup(int amount) {
/* 398 */     this.inBuf += amount;
/* 399 */     if ((this.bufpos -= amount) < 0) {
/* 400 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */   
/*     */   public UCode_UCodeESC_CharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 405 */     this.inputStream = dstream;
/* 406 */     this.line = startline;
/* 407 */     this.column = startcolumn - 1;
/*     */     
/* 409 */     this.available = this.bufsize = buffersize;
/* 410 */     this.buffer = new char[buffersize];
/* 411 */     this.bufline = new int[buffersize];
/* 412 */     this.bufcolumn = new int[buffersize];
/* 413 */     this.nextCharBuf = new char[4096];
/* 414 */     skipBOM();
/*     */   }
/*     */ 
/*     */   
/*     */   public UCode_UCodeESC_CharStream(Reader dstream, int startline, int startcolumn) {
/* 419 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 424 */     this.inputStream = dstream;
/* 425 */     this.closed = false;
/* 426 */     this.line = startline;
/* 427 */     this.column = startcolumn - 1;
/*     */     
/* 429 */     if (this.buffer == null || buffersize != this.buffer.length) {
/* 430 */       this.available = this.bufsize = buffersize;
/* 431 */       this.buffer = new char[buffersize];
/* 432 */       this.bufline = new int[buffersize];
/* 433 */       this.bufcolumn = new int[buffersize];
/* 434 */       this.nextCharBuf = new char[4096];
/*     */     } 
/* 436 */     this.prevCharIsLF = false;
/* 437 */     this.tokenBegin = this.inBuf = this.maxNextCharInd = 0;
/* 438 */     this.nextCharInd = this.bufpos = -1;
/* 439 */     skipBOM();
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(Reader dstream, int startline, int startcolumn) {
/* 444 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public UCode_UCodeESC_CharStream(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 449 */     this(new InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public UCode_UCodeESC_CharStream(InputStream dstream, int startline, int startcolumn) {
/* 454 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn, int buffersize) {
/* 459 */     ReInit(new InputStreamReader(dstream), startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(InputStream dstream, int startline, int startcolumn) {
/* 464 */     ReInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void skipBOM() {
/*     */     try {
/* 471 */       if (PeekChar() == '﻿') {
/* 472 */         ReadChar();
/*     */       }
/* 474 */     } catch (EOFException e) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public final String GetImage() {
/* 479 */     if (this.bufpos >= this.tokenBegin) {
/* 480 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/* 482 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public final char[] GetSuffix(int len) {
/* 487 */     char[] ret = new char[len];
/*     */     
/* 489 */     if (this.bufpos + 1 >= len) {
/* 490 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     } else {
/* 492 */       System.arraycopy(this.buffer, this.bufsize - len - this.bufpos - 1, ret, 0, len - this.bufpos - 1);
/*     */       
/* 494 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     } 
/*     */     
/* 497 */     return ret;
/*     */   }
/*     */   
/*     */   public void Done() {
/* 501 */     this.nextCharBuf = null;
/* 502 */     this.buffer = null;
/* 503 */     this.bufline = null;
/* 504 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustBeginLineColumn(int newLine, int newCol) {
/* 511 */     int len, start = this.tokenBegin;
/*     */ 
/*     */     
/* 514 */     if (this.bufpos >= this.tokenBegin) {
/* 515 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     } else {
/*     */       
/* 518 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     } 
/*     */     
/* 521 */     int i = 0, j = 0, k = 0;
/* 522 */     int nextColDiff = 0, columnDiff = 0;
/*     */ 
/*     */     
/* 525 */     while (i < len && this.bufline[j = start % this.bufsize] == this.bufline[k = ++start % this.bufsize]) {
/* 526 */       this.bufline[j] = newLine;
/* 527 */       nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 528 */       this.bufcolumn[j] = newCol + columnDiff;
/* 529 */       columnDiff = nextColDiff;
/* 530 */       i++;
/*     */     } 
/*     */     
/* 533 */     if (i < len) {
/* 534 */       this.bufline[j] = newLine++;
/* 535 */       this.bufcolumn[j] = newCol + columnDiff;
/*     */       
/* 537 */       while (i++ < len) {
/* 538 */         if (this.bufline[j = start % this.bufsize] != this.bufline[++start % this.bufsize]) {
/* 539 */           this.bufline[j] = newLine++; continue;
/*     */         } 
/* 541 */         this.bufline[j] = newLine;
/*     */       } 
/*     */     } 
/*     */     
/* 545 */     this.line = this.bufline[j];
/* 546 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\compact\UCode_UCodeESC_CharStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */