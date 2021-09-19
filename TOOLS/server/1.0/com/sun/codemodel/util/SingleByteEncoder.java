/*     */ package 1.0.com.sun.codemodel.util;
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
/*     */ abstract class SingleByteEncoder
/*     */   extends CharsetEncoder
/*     */ {
/*     */   private final short[] index1;
/*     */   private final String index2;
/*     */   private final int mask1;
/*     */   private final int mask2;
/*     */   private final int shift;
/*  31 */   private final Surrogate.Parser sgp = new Surrogate.Parser();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleByteEncoder(Charset cs, short[] index1, String index2, int mask1, int mask2, int shift) {
/*  37 */     super(cs, 1.0F, 1.0F);
/*  38 */     this.index1 = index1;
/*  39 */     this.index2 = index2;
/*  40 */     this.mask1 = mask1;
/*  41 */     this.mask2 = mask2;
/*  42 */     this.shift = shift;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canEncode(char c) {
/*  47 */     char testEncode = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */     
/*  49 */     if (testEncode == '\000') {
/*  50 */       return false;
/*     */     }
/*  52 */     return true;
/*     */   }
/*     */   
/*     */   private CoderResult encodeArrayLoop(CharBuffer src, ByteBuffer dst) {
/*  56 */     char[] sa = src.array();
/*  57 */     int sp = src.arrayOffset() + src.position();
/*  58 */     int sl = src.arrayOffset() + src.limit();
/*  59 */     sp = (sp <= sl) ? sp : sl;
/*  60 */     byte[] da = dst.array();
/*  61 */     int dp = dst.arrayOffset() + dst.position();
/*  62 */     int dl = dst.arrayOffset() + dst.limit();
/*  63 */     dp = (dp <= dl) ? dp : dl;
/*     */     
/*     */     try {
/*  66 */       while (sp < sl) {
/*  67 */         char c = sa[sp];
/*  68 */         if (Surrogate.is(c)) {
/*  69 */           if (this.sgp.parse(c, sa, sp, sl) < 0)
/*  70 */             return this.sgp.error(); 
/*  71 */           return this.sgp.unmappableResult();
/*     */         } 
/*  73 */         if (c >= '￾')
/*  74 */           return CoderResult.unmappableForLength(1); 
/*  75 */         if (dl - dp < 1) {
/*  76 */           return CoderResult.OVERFLOW;
/*     */         }
/*  78 */         char e = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  83 */         if (e == '\000' && c != '\000') {
/*  84 */           return CoderResult.unmappableForLength(1);
/*     */         }
/*  86 */         sp++;
/*  87 */         da[dp++] = (byte)e;
/*     */       } 
/*  89 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/*  91 */       src.position(sp - src.arrayOffset());
/*  92 */       dst.position(dp - dst.arrayOffset());
/*     */     } 
/*     */   }
/*     */   
/*     */   private CoderResult encodeBufferLoop(CharBuffer src, ByteBuffer dst) {
/*  97 */     int mark = src.position();
/*     */     try {
/*  99 */       while (src.hasRemaining()) {
/* 100 */         char c = src.get();
/* 101 */         if (Surrogate.is(c)) {
/* 102 */           if (this.sgp.parse(c, src) < 0)
/* 103 */             return this.sgp.error(); 
/* 104 */           return this.sgp.unmappableResult();
/*     */         } 
/* 106 */         if (c >= '￾')
/* 107 */           return CoderResult.unmappableForLength(1); 
/* 108 */         if (!dst.hasRemaining()) {
/* 109 */           return CoderResult.OVERFLOW;
/*     */         }
/* 111 */         char e = this.index2.charAt(this.index1[(c & this.mask1) >> this.shift] + (c & this.mask2));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 116 */         if (e == '\000' && c != '\000') {
/* 117 */           return CoderResult.unmappableForLength(1);
/*     */         }
/* 119 */         mark++;
/* 120 */         dst.put((byte)e);
/*     */       } 
/* 122 */       return CoderResult.UNDERFLOW;
/*     */     } finally {
/* 124 */       src.position(mark);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected CoderResult encodeLoop(CharBuffer src, ByteBuffer dst) {
/* 129 */     if (src.hasArray() && dst.hasArray()) {
/* 130 */       return encodeArrayLoop(src, dst);
/*     */     }
/* 132 */     return encodeBufferLoop(src, dst);
/*     */   }
/*     */   
/*     */   public byte encode(char inputChar) {
/* 136 */     return (byte)this.index2.charAt(this.index1[(inputChar & this.mask1) >> this.shift] + (inputChar & this.mask2));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemode\\util\SingleByteEncoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */