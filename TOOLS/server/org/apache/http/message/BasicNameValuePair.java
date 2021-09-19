/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class BasicNameValuePair
/*     */   implements NameValuePair, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6437800749411518984L;
/*     */   private final String name;
/*     */   private final String value;
/*     */   
/*     */   public BasicNameValuePair(String name, String value) {
/*  57 */     if (name == null) {
/*  58 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/*  60 */     this.name = name;
/*  61 */     this.value = value;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  65 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  69 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  76 */     if (this.value == null) {
/*  77 */       return this.name;
/*     */     }
/*  79 */     int len = this.name.length() + 1 + this.value.length();
/*  80 */     StringBuilder buffer = new StringBuilder(len);
/*  81 */     buffer.append(this.name);
/*  82 */     buffer.append("=");
/*  83 */     buffer.append(this.value);
/*  84 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  90 */     if (this == object) return true; 
/*  91 */     if (object instanceof NameValuePair) {
/*  92 */       BasicNameValuePair that = (BasicNameValuePair)object;
/*  93 */       return (this.name.equals(that.name) && LangUtils.equals(this.value, that.value));
/*     */     } 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     int hash = 17;
/* 103 */     hash = LangUtils.hashCode(hash, this.name);
/* 104 */     hash = LangUtils.hashCode(hash, this.value);
/* 105 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 110 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicNameValuePair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */