/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EscapeTokenizer
/*     */ {
/*  36 */   private int bracesLevel = 0;
/*     */   
/*     */   private boolean emittingEscapeCode = false;
/*     */   
/*     */   private boolean inComment = false;
/*     */   
/*     */   private boolean inQuotes = false;
/*     */   
/*  44 */   private char lastChar = Character.MIN_VALUE;
/*     */   
/*  46 */   private char lastLastChar = Character.MIN_VALUE;
/*     */   
/*  48 */   private int pos = 0;
/*     */   
/*  50 */   private char quoteChar = Character.MIN_VALUE;
/*     */   
/*     */   private boolean sawVariableUse = false;
/*     */   
/*  54 */   private String source = null;
/*     */   
/*  56 */   private int sourceLength = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EscapeTokenizer(String s) {
/*  68 */     this.source = s;
/*  69 */     this.sourceLength = s.length();
/*  70 */     this.pos = 0;
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
/*     */   public synchronized boolean hasMoreTokens() {
/*  82 */     return (this.pos < this.sourceLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String nextToken() {
/*  91 */     StringBuffer tokenBuf = new StringBuffer();
/*     */     
/*  93 */     if (this.emittingEscapeCode) {
/*  94 */       tokenBuf.append("{");
/*  95 */       this.emittingEscapeCode = false;
/*     */     } 
/*     */     
/*  98 */     for (; this.pos < this.sourceLength; this.pos++) {
/*  99 */       char c = this.source.charAt(this.pos);
/*     */ 
/*     */ 
/*     */       
/* 103 */       if (!this.inQuotes && c == '@') {
/* 104 */         this.sawVariableUse = true;
/*     */       }
/*     */       
/* 107 */       if ((c == '\'' || c == '"') && !this.inComment) {
/* 108 */         if (this.inQuotes && c == this.quoteChar && 
/* 109 */           this.pos + 1 < this.sourceLength && 
/* 110 */           this.source.charAt(this.pos + 1) == this.quoteChar)
/*     */         {
/* 112 */           if (this.lastChar != '\\') {
/* 113 */             tokenBuf.append(this.quoteChar);
/* 114 */             tokenBuf.append(this.quoteChar);
/* 115 */             this.pos++;
/*     */             
/*     */             continue;
/*     */           } 
/*     */         }
/*     */         
/* 121 */         if (this.lastChar != '\\') {
/* 122 */           if (this.inQuotes) {
/* 123 */             if (this.quoteChar == c) {
/* 124 */               this.inQuotes = false;
/*     */             }
/*     */           } else {
/* 127 */             this.inQuotes = true;
/* 128 */             this.quoteChar = c;
/*     */           } 
/* 130 */         } else if (this.lastLastChar == '\\') {
/* 131 */           if (this.inQuotes) {
/* 132 */             if (this.quoteChar == c) {
/* 133 */               this.inQuotes = false;
/*     */             }
/*     */           } else {
/* 136 */             this.inQuotes = true;
/* 137 */             this.quoteChar = c;
/*     */           } 
/*     */         } 
/*     */         
/* 141 */         tokenBuf.append(c);
/* 142 */       } else if (c == '-') {
/* 143 */         if (this.lastChar == '-' && this.lastLastChar != '\\' && !this.inQuotes)
/*     */         {
/* 145 */           this.inComment = true;
/*     */         }
/*     */         
/* 148 */         tokenBuf.append(c);
/* 149 */       } else if (c == '\n' || c == '\r') {
/* 150 */         this.inComment = false;
/*     */         
/* 152 */         tokenBuf.append(c);
/* 153 */       } else if (c == '{') {
/* 154 */         if (this.inQuotes || this.inComment) {
/* 155 */           tokenBuf.append(c);
/*     */         } else {
/* 157 */           this.bracesLevel++;
/*     */           
/* 159 */           if (this.bracesLevel == 1) {
/* 160 */             this.pos++;
/* 161 */             this.emittingEscapeCode = true;
/*     */             
/* 163 */             return tokenBuf.toString();
/*     */           } 
/*     */           
/* 166 */           tokenBuf.append(c);
/*     */         } 
/* 168 */       } else if (c == '}') {
/* 169 */         tokenBuf.append(c);
/*     */         
/* 171 */         if (!this.inQuotes && !this.inComment) {
/* 172 */           this.lastChar = c;
/*     */           
/* 174 */           this.bracesLevel--;
/*     */           
/* 176 */           if (this.bracesLevel == 0) {
/* 177 */             this.pos++;
/*     */             
/* 179 */             return tokenBuf.toString();
/*     */           } 
/*     */         } 
/*     */       } else {
/* 183 */         tokenBuf.append(c);
/*     */       } 
/*     */       
/* 186 */       this.lastLastChar = this.lastChar;
/* 187 */       this.lastChar = c;
/*     */       continue;
/*     */     } 
/* 190 */     return tokenBuf.toString();
/*     */   }
/*     */   
/*     */   boolean sawVariableUse() {
/* 194 */     return this.sawVariableUse;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\EscapeTokenizer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */