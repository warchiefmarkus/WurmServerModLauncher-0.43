/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TagName
/*     */ {
/*     */   public String uri;
/*     */   public String local;
/*     */   public Attributes atts;
/*     */   
/*     */   public final boolean matches(String nsUri, String local) {
/*  96 */     return (this.uri == nsUri && this.local == local);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean matches(Name name) {
/* 103 */     return (this.local == name.localName && this.uri == name.nsUri);
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
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return '{' + this.uri + '}' + this.local;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getQname();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 133 */     String qname = getQname();
/* 134 */     int idx = qname.indexOf(':');
/* 135 */     if (idx < 0) return ""; 
/* 136 */     return qname.substring(0, idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName createQName() {
/* 143 */     return new QName(this.uri, this.local, getPrefix());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\TagName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */