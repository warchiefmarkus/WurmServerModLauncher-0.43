/*     */ package com.sun.activation.registries;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LineTokenizer
/*     */ {
/*     */   private int currentPosition;
/*     */   private int maxPosition;
/*     */   private String str;
/* 233 */   private Vector stack = new Vector();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String singles = "=";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineTokenizer(String str) {
/* 243 */     this.currentPosition = 0;
/* 244 */     this.str = str;
/* 245 */     this.maxPosition = str.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void skipWhiteSpace() {
/* 252 */     while (this.currentPosition < this.maxPosition && Character.isWhitespace(this.str.charAt(this.currentPosition)))
/*     */     {
/* 254 */       this.currentPosition++;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMoreTokens() {
/* 265 */     if (this.stack.size() > 0)
/* 266 */       return true; 
/* 267 */     skipWhiteSpace();
/* 268 */     return (this.currentPosition < this.maxPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String nextToken() {
/* 279 */     int size = this.stack.size();
/* 280 */     if (size > 0) {
/* 281 */       String t = this.stack.elementAt(size - 1);
/* 282 */       this.stack.removeElementAt(size - 1);
/* 283 */       return t;
/*     */     } 
/* 285 */     skipWhiteSpace();
/*     */     
/* 287 */     if (this.currentPosition >= this.maxPosition) {
/* 288 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/* 291 */     int start = this.currentPosition;
/* 292 */     char c = this.str.charAt(start);
/* 293 */     if (c == '"') {
/* 294 */       this.currentPosition++;
/* 295 */       boolean filter = false;
/* 296 */       while (this.currentPosition < this.maxPosition) {
/* 297 */         c = this.str.charAt(this.currentPosition++);
/* 298 */         if (c == '\\') {
/* 299 */           this.currentPosition++;
/* 300 */           filter = true; continue;
/* 301 */         }  if (c == '"') {
/*     */           String s;
/*     */           
/* 304 */           if (filter) {
/* 305 */             StringBuffer sb = new StringBuffer();
/* 306 */             for (int i = start + 1; i < this.currentPosition - 1; i++) {
/* 307 */               c = this.str.charAt(i);
/* 308 */               if (c != '\\')
/* 309 */                 sb.append(c); 
/*     */             } 
/* 311 */             s = sb.toString();
/*     */           } else {
/* 313 */             s = this.str.substring(start + 1, this.currentPosition - 1);
/* 314 */           }  return s;
/*     */         } 
/*     */       } 
/* 317 */     } else if ("=".indexOf(c) >= 0) {
/* 318 */       this.currentPosition++;
/*     */     } else {
/*     */       
/* 321 */       while (this.currentPosition < this.maxPosition && "=".indexOf(this.str.charAt(this.currentPosition)) < 0 && !Character.isWhitespace(this.str.charAt(this.currentPosition)))
/*     */       {
/* 323 */         this.currentPosition++;
/*     */       }
/*     */     } 
/* 326 */     return this.str.substring(start, this.currentPosition);
/*     */   }
/*     */   
/*     */   public void pushToken(String token) {
/* 330 */     this.stack.addElement(token);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\activation\registries\LineTokenizer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */