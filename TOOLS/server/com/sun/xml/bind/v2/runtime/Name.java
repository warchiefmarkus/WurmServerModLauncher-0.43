/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Name
/*     */   implements Comparable<Name>
/*     */ {
/*     */   public final String nsUri;
/*     */   public final String localName;
/*     */   public final short nsUriIndex;
/*     */   public final short localNameIndex;
/*     */   public final short qNameIndex;
/*     */   public final boolean isAttribute;
/*     */   
/*     */   Name(int qNameIndex, int nsUriIndex, String nsUri, int localIndex, String localName, boolean isAttribute) {
/*  81 */     this.qNameIndex = (short)qNameIndex;
/*  82 */     this.nsUri = nsUri;
/*  83 */     this.localName = localName;
/*  84 */     this.nsUriIndex = (short)nsUriIndex;
/*  85 */     this.localNameIndex = (short)localIndex;
/*  86 */     this.isAttribute = isAttribute;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  90 */     return '{' + this.nsUri + '}' + this.localName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName toQName() {
/*  97 */     return new QName(this.nsUri, this.localName);
/*     */   }
/*     */   
/*     */   public boolean equals(String nsUri, String localName) {
/* 101 */     return (localName.equals(this.localName) && nsUri.equals(this.nsUri));
/*     */   }
/*     */   
/*     */   public int compareTo(Name that) {
/* 105 */     int r = this.nsUri.compareTo(that.nsUri);
/* 106 */     if (r != 0) return r; 
/* 107 */     return this.localName.compareTo(that.localName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\Name.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */