/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.output.Pcdata;
/*     */ import com.sun.xml.bind.v2.runtime.output.UTF8XmlOutput;
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
/*     */ public final class IntArrayData
/*     */   extends Pcdata
/*     */ {
/*     */   private int[] data;
/*     */   private int start;
/*     */   private int len;
/*     */   private StringBuilder literal;
/*     */   
/*     */   public IntArrayData(int[] data, int start, int len) {
/*  71 */     set(data, start, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IntArrayData() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(int[] data, int start, int len) {
/*  87 */     this.data = data;
/*  88 */     this.start = start;
/*  89 */     this.len = len;
/*  90 */     this.literal = null;
/*     */   }
/*     */   
/*     */   public int length() {
/*  94 */     return getLiteral().length();
/*     */   }
/*     */   
/*     */   public char charAt(int index) {
/*  98 */     return getLiteral().charAt(index);
/*     */   }
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 102 */     return getLiteral().subSequence(start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringBuilder getLiteral() {
/* 109 */     if (this.literal != null) return this.literal;
/*     */     
/* 111 */     this.literal = new StringBuilder();
/* 112 */     int p = this.start;
/* 113 */     for (int i = this.len; i > 0; i--) {
/* 114 */       if (this.literal.length() > 0) this.literal.append(' '); 
/* 115 */       this.literal.append(this.data[p++]);
/*     */     } 
/*     */     
/* 118 */     return this.literal;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 122 */     return this.literal.toString();
/*     */   }
/*     */   
/*     */   public void writeTo(UTF8XmlOutput output) throws IOException {
/* 126 */     int p = this.start;
/* 127 */     for (int i = this.len; i > 0; i--) {
/* 128 */       if (i != this.len)
/* 129 */         output.write(32); 
/* 130 */       output.text(this.data[p++]);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\IntArrayData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */