/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
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
/*     */ @XmlRootElement(name = "class")
/*     */ public final class BIClass
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   @XmlAttribute(name = "name")
/*     */   private String className;
/*     */   @XmlAttribute(name = "implClass")
/*     */   private String userSpecifiedImplClass;
/*     */   @XmlAttribute(name = "ref")
/*     */   private String ref;
/*     */   @XmlElement
/*     */   private String javadoc;
/*     */   
/*     */   @Nullable
/*     */   public String getClassName() {
/*  77 */     if (this.className == null) return null;
/*     */     
/*  79 */     BIGlobalBinding gb = getBuilder().getGlobalBinding();
/*  80 */     NameConverter nc = (getBuilder()).model.getNameConverter();
/*     */     
/*  82 */     if (gb.isJavaNamingConventionEnabled()) return nc.toClassName(this.className);
/*     */ 
/*     */     
/*  85 */     return this.className;
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
/*     */   public String getUserSpecifiedImplClass() {
/*  97 */     return this.userSpecifiedImplClass;
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
/*     */   public String getExistingClassRef() {
/* 111 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJavadoc() {
/* 120 */     return this.javadoc;
/*     */   } public QName getName() {
/* 122 */     return NAME;
/*     */   }
/*     */   public void setParent(BindInfo p) {
/* 125 */     super.setParent(p);
/*     */ 
/*     */     
/* 128 */     if (this.ref != null) {
/* 129 */       markAsAcknowledged();
/*     */     }
/*     */   }
/*     */   
/* 133 */   public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "class");
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */