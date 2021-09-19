/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlTransient;
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
/*     */ @XmlRootElement(name = "typesafeEnumClass")
/*     */ public final class BIEnum
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlAttribute(name = "map")
/*     */   private boolean map = true;
/*     */   @XmlAttribute(name = "name")
/*  75 */   public String className = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "ref")
/*     */   public String ref;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement
/*  88 */   public final String javadoc = null;
/*     */   
/*     */   public boolean isMapped() {
/*  91 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlTransient
/* 101 */   public final Map<String, BIEnumMember> members = new HashMap<String, BIEnumMember>();
/*     */   public QName getName() {
/* 103 */     return NAME;
/*     */   }
/*     */   public void setParent(BindInfo p) {
/* 106 */     super.setParent(p);
/* 107 */     for (BIEnumMember mem : this.members.values()) {
/* 108 */       mem.setParent(p);
/*     */     }
/*     */ 
/*     */     
/* 112 */     if (this.ref != null) {
/* 113 */       markAsAcknowledged();
/*     */     }
/*     */   }
/*     */   
/* 117 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "enum");
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(name = "typesafeEnumMember")
/*     */   private void setMembers(BIEnumMember2[] mems) {
/* 123 */     for (BIEnumMember2 e : mems)
/* 124 */       this.members.put(e.value, e); 
/*     */   }
/*     */   
/*     */   static class BIEnumMember2 extends BIEnumMember {
/*     */     @XmlAttribute(required = true)
/*     */     String value;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIEnum.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */