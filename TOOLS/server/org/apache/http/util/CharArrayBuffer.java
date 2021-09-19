/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public final class CharArrayBuffer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6208952725094867135L;
/*     */   private char[] buffer;
/*     */   private int len;
/*     */   
/*     */   public CharArrayBuffer(int capacity) {
/*  56 */     if (capacity < 0) {
/*  57 */       throw new IllegalArgumentException("Buffer capacity may not be negative");
/*     */     }
/*  59 */     this.buffer = new char[capacity];
/*     */   }
/*     */   
/*     */   private void expand(int newlen) {
/*  63 */     char[] newbuffer = new char[Math.max(this.buffer.length << 1, newlen)];
/*  64 */     System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
/*  65 */     this.buffer = newbuffer;
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
/*     */   public void append(char[] b, int off, int len) {
/*  81 */     if (b == null) {
/*     */       return;
/*     */     }
/*  84 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  86 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  88 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  91 */     int newlen = this.len + len;
/*  92 */     if (newlen > this.buffer.length) {
/*  93 */       expand(newlen);
/*     */     }
/*  95 */     System.arraycopy(b, off, this.buffer, this.len, len);
/*  96 */     this.len = newlen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(String str) {
/* 106 */     if (str == null) {
/* 107 */       str = "null";
/*     */     }
/* 109 */     int strlen = str.length();
/* 110 */     int newlen = this.len + strlen;
/* 111 */     if (newlen > this.buffer.length) {
/* 112 */       expand(newlen);
/*     */     }
/* 114 */     str.getChars(0, strlen, this.buffer, this.len);
/* 115 */     this.len = newlen;
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
/*     */   public void append(CharArrayBuffer b, int off, int len) {
/* 132 */     if (b == null) {
/*     */       return;
/*     */     }
/* 135 */     append(b.buffer, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(CharArrayBuffer b) {
/* 146 */     if (b == null) {
/*     */       return;
/*     */     }
/* 149 */     append(b.buffer, 0, b.len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(char ch) {
/* 159 */     int newlen = this.len + 1;
/* 160 */     if (newlen > this.buffer.length) {
/* 161 */       expand(newlen);
/*     */     }
/* 163 */     this.buffer[this.len] = ch;
/* 164 */     this.len = newlen;
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
/*     */   public void append(byte[] b, int off, int len) {
/* 182 */     if (b == null) {
/*     */       return;
/*     */     }
/* 185 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 187 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 189 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 192 */     int oldlen = this.len;
/* 193 */     int newlen = oldlen + len;
/* 194 */     if (newlen > this.buffer.length) {
/* 195 */       expand(newlen);
/*     */     }
/* 197 */     for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++) {
/* 198 */       this.buffer[i2] = (char)(b[i1] & 0xFF);
/*     */     }
/* 200 */     this.len = newlen;
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
/*     */   public void append(ByteArrayBuffer b, int off, int len) {
/* 218 */     if (b == null) {
/*     */       return;
/*     */     }
/* 221 */     append(b.buffer(), off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(Object obj) {
/* 232 */     append(String.valueOf(obj));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 239 */     this.len = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] toCharArray() {
/* 248 */     char[] b = new char[this.len];
/* 249 */     if (this.len > 0) {
/* 250 */       System.arraycopy(this.buffer, 0, b, 0, this.len);
/*     */     }
/* 252 */     return b;
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
/*     */   public char charAt(int i) {
/* 266 */     return this.buffer[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] buffer() {
/* 275 */     return this.buffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 286 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 295 */     return this.len;
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
/*     */   public void ensureCapacity(int required) {
/* 307 */     if (required <= 0) {
/*     */       return;
/*     */     }
/* 310 */     int available = this.buffer.length - this.len;
/* 311 */     if (required > available) {
/* 312 */       expand(this.len + required);
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
/*     */   public void setLength(int len) {
/* 327 */     if (len < 0 || len > this.buffer.length) {
/* 328 */       throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
/*     */     }
/* 330 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 340 */     return (this.len == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 350 */     return (this.len == this.buffer.length);
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
/*     */   public int indexOf(int ch, int beginIndex, int endIndex) {
/* 375 */     if (beginIndex < 0) {
/* 376 */       beginIndex = 0;
/*     */     }
/* 378 */     if (endIndex > this.len) {
/* 379 */       endIndex = this.len;
/*     */     }
/* 381 */     if (beginIndex > endIndex) {
/* 382 */       return -1;
/*     */     }
/* 384 */     for (int i = beginIndex; i < endIndex; i++) {
/* 385 */       if (this.buffer[i] == ch) {
/* 386 */         return i;
/*     */       }
/*     */     } 
/* 389 */     return -1;
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
/*     */   public int indexOf(int ch) {
/* 403 */     return indexOf(ch, 0, this.len);
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
/*     */   public String substring(int beginIndex, int endIndex) {
/* 421 */     return new String(this.buffer, beginIndex, endIndex - beginIndex);
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
/*     */   public String substringTrimmed(int beginIndex, int endIndex) {
/* 441 */     if (beginIndex < 0) {
/* 442 */       throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
/*     */     }
/* 444 */     if (endIndex > this.len) {
/* 445 */       throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
/*     */     }
/* 447 */     if (beginIndex > endIndex) {
/* 448 */       throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
/*     */     }
/* 450 */     while (beginIndex < endIndex && HTTP.isWhitespace(this.buffer[beginIndex])) {
/* 451 */       beginIndex++;
/*     */     }
/* 453 */     while (endIndex > beginIndex && HTTP.isWhitespace(this.buffer[endIndex - 1])) {
/* 454 */       endIndex--;
/*     */     }
/* 456 */     return new String(this.buffer, beginIndex, endIndex - beginIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 461 */     return new String(this.buffer, 0, this.len);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\htt\\util\CharArrayBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */