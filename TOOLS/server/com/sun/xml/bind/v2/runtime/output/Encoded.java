/*     */ package com.sun.xml.bind.v2.runtime.output;
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
/*     */ public final class Encoded
/*     */ {
/*     */   public byte[] buf;
/*     */   public int len;
/*     */   
/*     */   public Encoded() {}
/*     */   
/*     */   public Encoded(String text) {
/*  56 */     set(text);
/*     */   }
/*     */   
/*     */   public void ensureSize(int size) {
/*  60 */     if (this.buf == null || this.buf.length < size)
/*  61 */       this.buf = new byte[size]; 
/*     */   }
/*     */   
/*     */   public final void set(String text) {
/*  65 */     int length = text.length();
/*     */     
/*  67 */     ensureSize(length * 3 + 1);
/*     */     
/*  69 */     int ptr = 0;
/*     */     
/*  71 */     for (int i = 0; i < length; i++) {
/*  72 */       char chr = text.charAt(i);
/*  73 */       if (chr > '')
/*  74 */       { if (chr > '߿')
/*  75 */         { if ('?' <= chr && chr <= '?')
/*     */           
/*  77 */           { int uc = ((chr & 0x3FF) << 10 | text.charAt(++i) & 0x3FF) + 65536;
/*     */             
/*  79 */             this.buf[ptr++] = (byte)(0xF0 | uc >> 18);
/*  80 */             this.buf[ptr++] = (byte)(0x80 | uc >> 12 & 0x3F);
/*  81 */             this.buf[ptr++] = (byte)(0x80 | uc >> 6 & 0x3F);
/*  82 */             this.buf[ptr++] = (byte)(128 + (uc & 0x3F)); }
/*     */           else
/*     */           
/*  85 */           { this.buf[ptr++] = (byte)(224 + (chr >> 12));
/*  86 */             this.buf[ptr++] = (byte)(128 + (chr >> 6 & 0x3F));
/*     */ 
/*     */ 
/*     */             
/*  90 */             this.buf[ptr++] = (byte)(128 + (chr & 0x3F)); }  } else { this.buf[ptr++] = (byte)(192 + (chr >> 6)); this.buf[ptr++] = (byte)(128 + (chr & 0x3F)); }
/*     */          }
/*  92 */       else { this.buf[ptr++] = (byte)chr; }
/*     */     
/*     */     } 
/*     */     
/*  96 */     this.len = ptr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setEscape(String text, boolean isAttribute) {
/* 107 */     int length = text.length();
/* 108 */     ensureSize(length * 6 + 1);
/*     */     
/* 110 */     int ptr = 0;
/*     */     
/* 112 */     for (int i = 0; i < length; i++) {
/* 113 */       char chr = text.charAt(i);
/*     */       
/* 115 */       int ptr1 = ptr;
/* 116 */       if (chr > '') {
/* 117 */         if (chr > '߿')
/* 118 */         { if ('?' <= chr && chr <= '?')
/*     */           
/* 120 */           { int uc = ((chr & 0x3FF) << 10 | text.charAt(++i) & 0x3FF) + 65536;
/*     */             
/* 122 */             this.buf[ptr++] = (byte)(0xF0 | uc >> 18);
/* 123 */             this.buf[ptr++] = (byte)(0x80 | uc >> 12 & 0x3F);
/* 124 */             this.buf[ptr++] = (byte)(0x80 | uc >> 6 & 0x3F);
/* 125 */             this.buf[ptr++] = (byte)(128 + (uc & 0x3F)); }
/*     */           else
/*     */           
/* 128 */           { this.buf[ptr1++] = (byte)(224 + (chr >> 12));
/* 129 */             this.buf[ptr1++] = (byte)(128 + (chr >> 6 & 0x3F));
/*     */ 
/*     */ 
/*     */             
/* 133 */             this.buf[ptr1++] = (byte)(128 + (chr & 0x3F)); }  } else { this.buf[ptr1++] = (byte)(192 + (chr >> 6)); this.buf[ptr1++] = (byte)(128 + (chr & 0x3F)); }
/*     */       
/*     */       } else {
/*     */         byte[] ent;
/* 137 */         if ((ent = attributeEntities[chr]) != null)
/*     */         
/*     */         { 
/*     */ 
/*     */           
/* 142 */           if (isAttribute || entities[chr] != null) {
/* 143 */             ptr1 = writeEntity(ent, ptr1);
/*     */           } else {
/* 145 */             this.buf[ptr1++] = (byte)chr;
/*     */           }  }
/* 147 */         else { this.buf[ptr1++] = (byte)chr; }
/*     */         
/* 149 */         ptr = ptr1;
/*     */       } 
/* 151 */     }  this.len = ptr;
/*     */   }
/*     */   
/*     */   private int writeEntity(byte[] entity, int ptr) {
/* 155 */     System.arraycopy(entity, 0, this.buf, ptr, entity.length);
/* 156 */     return ptr + entity.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void write(UTF8XmlOutput out) throws IOException {
/* 163 */     out.write(this.buf, 0, this.len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(char b) {
/* 171 */     this.buf[this.len++] = (byte)b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void compact() {
/* 179 */     byte[] b = new byte[this.len];
/* 180 */     System.arraycopy(this.buf, 0, b, 0, this.len);
/* 181 */     this.buf = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   private static final byte[][] entities = new byte[128][];
/* 191 */   private static final byte[][] attributeEntities = new byte[128][];
/*     */   
/*     */   static {
/* 194 */     add('&', "&amp;", false);
/* 195 */     add('<', "&lt;", false);
/* 196 */     add('>', "&gt;", false);
/* 197 */     add('"', "&quot;", false);
/* 198 */     add('\t', "&#x9;", true);
/* 199 */     add('\r', "&#xD;", false);
/* 200 */     add('\n', "&#xA;", true);
/*     */   }
/*     */   
/*     */   private static void add(char c, String s, boolean attOnly) {
/* 204 */     byte[] image = UTF8XmlOutput.toBytes(s);
/* 205 */     attributeEntities[c] = image;
/* 206 */     if (!attOnly)
/* 207 */       entities[c] = image; 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\Encoded.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */