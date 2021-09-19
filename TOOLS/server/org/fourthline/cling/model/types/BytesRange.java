/*     */ package org.fourthline.cling.model.types;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BytesRange
/*     */ {
/*     */   public static final String PREFIX = "bytes=";
/*     */   private Long firstByte;
/*     */   private Long lastByte;
/*     */   private Long byteLength;
/*     */   
/*     */   public BytesRange(Long firstByte, Long lastByte) {
/*  30 */     this.firstByte = firstByte;
/*  31 */     this.lastByte = lastByte;
/*  32 */     this.byteLength = null;
/*     */   }
/*     */   
/*     */   public BytesRange(Long firstByte, Long lastByte, Long byteLength) {
/*  36 */     this.firstByte = firstByte;
/*  37 */     this.lastByte = lastByte;
/*  38 */     this.byteLength = byteLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getFirstByte() {
/*  45 */     return this.firstByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getLastByte() {
/*  52 */     return this.lastByte;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getByteLength() {
/*  59 */     return this.byteLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString() {
/*  67 */     return getString(false, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(boolean includeDuration) {
/*  75 */     return getString(includeDuration, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(boolean includeDuration, String rangePrefix) {
/*  83 */     String s = (rangePrefix != null) ? rangePrefix : "bytes=";
/*     */     
/*  85 */     if (this.firstByte != null)
/*  86 */       s = s + this.firstByte.toString(); 
/*  87 */     s = s + "-";
/*  88 */     if (this.lastByte != null)
/*  89 */       s = s + this.lastByte.toString(); 
/*  90 */     if (includeDuration) {
/*  91 */       s = s + "/" + ((this.byteLength != null) ? this.byteLength.toString() : "*");
/*     */     }
/*     */     
/*  94 */     return s;
/*     */   }
/*     */   
/*     */   public static BytesRange valueOf(String s) throws InvalidValueException {
/*  98 */     return valueOf(s, null);
/*     */   }
/*     */   
/*     */   public static BytesRange valueOf(String s, String rangePrefix) throws InvalidValueException {
/* 102 */     if (s.startsWith((rangePrefix != null) ? rangePrefix : "bytes=")) {
/* 103 */       Long firstByte = null, lastByte = null, byteLength = null;
/* 104 */       String[] params = s.substring(((rangePrefix != null) ? rangePrefix : "bytes=").length()).split("[-/]");
/* 105 */       switch (params.length) {
/*     */         case 3:
/* 107 */           if (params[2].length() != 0 && !params[2].equals("*")) {
/* 108 */             byteLength = Long.valueOf(Long.parseLong(params[2]));
/*     */           }
/*     */         case 2:
/* 111 */           if (params[1].length() != 0) {
/* 112 */             lastByte = Long.valueOf(Long.parseLong(params[1]));
/*     */           }
/*     */         case 1:
/* 115 */           if (params[0].length() != 0) {
/* 116 */             firstByte = Long.valueOf(Long.parseLong(params[0]));
/*     */           }
/* 118 */           if (firstByte != null || lastByte != null) {
/* 119 */             return new BytesRange(firstByte, lastByte, byteLength);
/*     */           }
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 125 */     throw new InvalidValueException("Can't parse Bytes Range: " + s);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\BytesRange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */