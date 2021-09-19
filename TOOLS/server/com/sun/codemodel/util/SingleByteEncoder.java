/*     */ package com.sun.codemodel.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import sun.nio.cs.Surrogate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class SingleByteEncoder
/*     */   extends CharsetEncoder
/*     */ {
/*     */   private final short[] index1;
/*     */   private final String index2;
/*     */   private final int mask1;
/*     */   private final int mask2;
/*     */   private final int shift;
/*  46 */   private final Surrogate.Parser sgp = new Surrogate.Parser();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleByteEncoder(Charset cs, short[] index1, String index2, int mask1, int mask2, int shift) {
/*  52 */     super(cs, 1.0F, 1.0F);
/*  53 */     this.index1 = index1;
/*  54 */     this.index2 = index2;
/*  55 */     this.mask1 = mask1;
/*  56 */     this.mask2 = mask2;
/*  57 */     this.shift = shift;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canEncode(char c) {
/*  62 */     char testEncode = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */     
/*  64 */     if (testEncode == '\000') {
/*  65 */       return false;
/*     */     }
/*  67 */     return true;
/*     */   }
/*     */   
/*     */   private CoderResult encodeArrayLoop(CharBuffer src, ByteBuffer dst) {
/*  71 */     char[] sa = src.array();
/*  72 */     int sp = src.arrayOffset() + src.position();
/*  73 */     int sl = src.arrayOffset() + src.limit();
/*  74 */     sp = (sp <= sl) ? sp : sl;
/*  75 */     byte[] da = dst.array();
/*  76 */     int dp = dst.arrayOffset() + dst.position();
/*  77 */     int dl = dst.arrayOffset() + dst.limit();
/*  78 */     dp = (dp <= dl) ? dp : dl;
/*     */     
/*     */     try {
/*  81 */       while (sp < sl) {
/*  82 */         char c = sa[sp];
/*  83 */         if (Surrogate.is(c)) {
/*  84 */           if (this.sgp.parse(c, sa, sp, sl) < 0)
/*  85 */             return this.sgp.error(); 
/*  86 */           return this.sgp.unmappableResult();
/*     */         } 
/*  88 */         if (c >= '￾')
/*  89 */           return CoderResult.unmappableForLength(1); 
/*  90 */         if (dl - dp < 1) {
/*  91 */           return CoderResult.OVERFLOW;
/*     */         }
/*  93 */         char e = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  98 */         if (e == '\000' && c != '\000') {
/*  99 */           return CoderResult.unmappableForLength(1);
/*     */         }
/* 101 */         sp++;
/* 102 */         da[dp++] = (byte)e;
/*     */       } 
/* 104 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 106 */       src.position(sp - src.arrayOffset());
/* 107 */       dst.position(dp - dst.arrayOffset());
/*     */     } 
/*     */   }
/*     */   
/*     */   private CoderResult encodeBufferLoop(CharBuffer src, ByteBuffer dst) {
/* 112 */     int mark = src.position();
/*     */     try {
/* 114 */       while (src.hasRemaining()) {
/* 115 */         char c = src.get();
/* 116 */         if (Surrogate.is(c)) {
/* 117 */           if (this.sgp.parse(c, src) < 0)
/* 118 */             return this.sgp.error(); 
/* 119 */           return this.sgp.unmappableResult();
/*     */         } 
/* 121 */         if (c >= '￾')
/* 122 */           return CoderResult.unmappableForLength(1); 
/* 123 */         if (!dst.hasRemaining()) {
/* 124 */           return CoderResult.OVERFLOW;
/*     */         }
/* 126 */         char e = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 131 */         if (e == '\000' && c != '\000') {
/* 132 */           return CoderResult.unmappableForLength(1);
/*     */         }
/* 134 */         mark++;
/* 135 */         dst.put((byte)e);
/*     */       } 
/* 137 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 139 */       src.position(mark);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected CoderResult encodeLoop(CharBuffer src, ByteBuffer dst) {
/* 144 */     if (src.hasArray() && dst.hasArray()) {
/* 145 */       return encodeArrayLoop(src, dst);
/*     */     }
/* 147 */     return encodeBufferLoop(src, dst);
/*     */   }
/*     */   
/*     */   public byte encode(char inputChar) {
/* 151 */     return (byte)this.index2.charAt(this.index1[(inputChar & this.mask1) >> this.shift] + (inputChar & this.mask2));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemode\\util\SingleByteEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */