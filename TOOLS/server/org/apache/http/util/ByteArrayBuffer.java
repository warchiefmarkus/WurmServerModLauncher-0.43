/*     */ package org.apache.http.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ByteArrayBuffer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4359112959524048036L;
/*     */   private byte[] buffer;
/*     */   private int len;
/*     */   
/*     */   public ByteArrayBuffer(int capacity) {
/*  55 */     if (capacity < 0) {
/*  56 */       throw new IllegalArgumentException("Buffer capacity may not be negative");
/*     */     }
/*  58 */     this.buffer = new byte[capacity];
/*     */   }
/*     */   
/*     */   private void expand(int newlen) {
/*  62 */     byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
/*  63 */     System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
/*  64 */     this.buffer = newbuffer;
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
/*     */   public void append(byte[] b, int off, int len) {
/*  80 */     if (b == null) {
/*     */       return;
/*     */     }
/*  83 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  85 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  87 */     if (len == 0) {
/*     */       return;
/*     */     }
/*  90 */     int newlen = this.len + len;
/*  91 */     if (newlen > this.buffer.length) {
/*  92 */       expand(newlen);
/*     */     }
/*  94 */     System.arraycopy(b, off, this.buffer, this.len, len);
/*  95 */     this.len = newlen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(int b) {
/* 105 */     int newlen = this.len + 1;
/* 106 */     if (newlen > this.buffer.length) {
/* 107 */       expand(newlen);
/*     */     }
/* 109 */     this.buffer[this.len] = (byte)b;
/* 110 */     this.len = newlen;
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
/*     */   public void append(char[] b, int off, int len) {
/* 128 */     if (b == null) {
/*     */       return;
/*     */     }
/* 131 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/* 133 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/* 135 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 138 */     int oldlen = this.len;
/* 139 */     int newlen = oldlen + len;
/* 140 */     if (newlen > this.buffer.length) {
/* 141 */       expand(newlen);
/*     */     }
/* 143 */     for (int i1 = off, i2 = oldlen; i2 < newlen; i1++, i2++) {
/* 144 */       this.buffer[i2] = (byte)b[i1];
/*     */     }
/* 146 */     this.len = newlen;
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
/*     */   public void append(CharArrayBuffer b, int off, int len) {
/* 165 */     if (b == null) {
/*     */       return;
/*     */     }
/* 168 */     append(b.buffer(), off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 175 */     this.len = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray() {
/* 184 */     byte[] b = new byte[this.len];
/* 185 */     if (this.len > 0) {
/* 186 */       System.arraycopy(this.buffer, 0, b, 0, this.len);
/*     */     }
/* 188 */     return b;
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
/*     */   public int byteAt(int i) {
/* 202 */     return this.buffer[i];
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
/* 213 */     return this.buffer.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 222 */     return this.len;
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
/*     */   public void ensureCapacity(int required) {
/* 236 */     if (required <= 0) {
/*     */       return;
/*     */     }
/* 239 */     int available = this.buffer.length - this.len;
/* 240 */     if (required > available) {
/* 241 */       expand(this.len + required);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] buffer() {
/* 251 */     return this.buffer;
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
/* 265 */     if (len < 0 || len > this.buffer.length) {
/* 266 */       throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
/*     */     }
/* 268 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 278 */     return (this.len == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 288 */     return (this.len == this.buffer.length);
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
/*     */   public int indexOf(byte b, int beginIndex, int endIndex) {
/* 315 */     if (beginIndex < 0) {
/* 316 */       beginIndex = 0;
/*     */     }
/* 318 */     if (endIndex > this.len) {
/* 319 */       endIndex = this.len;
/*     */     }
/* 321 */     if (beginIndex > endIndex) {
/* 322 */       return -1;
/*     */     }
/* 324 */     for (int i = beginIndex; i < endIndex; i++) {
/* 325 */       if (this.buffer[i] == b) {
/* 326 */         return i;
/*     */       }
/*     */     } 
/* 329 */     return -1;
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
/*     */   public int indexOf(byte b) {
/* 345 */     return indexOf(b, 0, this.len);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\htt\\util\ByteArrayBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */