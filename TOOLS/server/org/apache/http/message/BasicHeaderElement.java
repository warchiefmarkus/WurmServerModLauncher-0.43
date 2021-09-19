/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BasicHeaderElement
/*     */   implements HeaderElement, Cloneable
/*     */ {
/*     */   private final String name;
/*     */   private final String value;
/*     */   private final NameValuePair[] parameters;
/*     */   
/*     */   public BasicHeaderElement(String name, String value, NameValuePair[] parameters) {
/*  60 */     if (name == null) {
/*  61 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/*  63 */     this.name = name;
/*  64 */     this.value = value;
/*  65 */     if (parameters != null) {
/*  66 */       this.parameters = parameters;
/*     */     } else {
/*  68 */       this.parameters = new NameValuePair[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHeaderElement(String name, String value) {
/*  79 */     this(name, value, null);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  83 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getValue() {
/*  87 */     return this.value;
/*     */   }
/*     */   
/*     */   public NameValuePair[] getParameters() {
/*  91 */     return (NameValuePair[])this.parameters.clone();
/*     */   }
/*     */   
/*     */   public int getParameterCount() {
/*  95 */     return this.parameters.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public NameValuePair getParameter(int index) {
/* 100 */     return this.parameters[index];
/*     */   }
/*     */   
/*     */   public NameValuePair getParameterByName(String name) {
/* 104 */     if (name == null) {
/* 105 */       throw new IllegalArgumentException("Name may not be null");
/*     */     }
/* 107 */     NameValuePair found = null;
/* 108 */     for (int i = 0; i < this.parameters.length; i++) {
/* 109 */       NameValuePair current = this.parameters[i];
/* 110 */       if (current.getName().equalsIgnoreCase(name)) {
/* 111 */         found = current;
/*     */         break;
/*     */       } 
/*     */     } 
/* 115 */     return found;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 120 */     if (this == object) return true; 
/* 121 */     if (object instanceof HeaderElement) {
/* 122 */       BasicHeaderElement that = (BasicHeaderElement)object;
/* 123 */       return (this.name.equals(that.name) && LangUtils.equals(this.value, that.value) && LangUtils.equals((Object[])this.parameters, (Object[])that.parameters));
/*     */     } 
/*     */ 
/*     */     
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 133 */     int hash = 17;
/* 134 */     hash = LangUtils.hashCode(hash, this.name);
/* 135 */     hash = LangUtils.hashCode(hash, this.value);
/* 136 */     for (int i = 0; i < this.parameters.length; i++) {
/* 137 */       hash = LangUtils.hashCode(hash, this.parameters[i]);
/*     */     }
/* 139 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     StringBuilder buffer = new StringBuilder();
/* 145 */     buffer.append(this.name);
/* 146 */     if (this.value != null) {
/* 147 */       buffer.append("=");
/* 148 */       buffer.append(this.value);
/*     */     } 
/* 150 */     for (int i = 0; i < this.parameters.length; i++) {
/* 151 */       buffer.append("; ");
/* 152 */       buffer.append(this.parameters[i]);
/*     */     } 
/* 154 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 161 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHeaderElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */