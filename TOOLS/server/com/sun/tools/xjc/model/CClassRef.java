/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIClass;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIEnum;
/*     */ import com.sun.xml.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.bind.v2.model.core.Element;
/*     */ import com.sun.xml.xsom.XSComponent;
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
/*     */ public final class CClassRef
/*     */   extends AbstractCElement
/*     */   implements NClass, CClass
/*     */ {
/*     */   private final String fullyQualifiedClassName;
/*     */   private JClass clazz;
/*     */   
/*     */   public CClassRef(Model model, XSComponent source, BIClass decl, CCustomizations customizations) {
/*  65 */     super(model, source, decl.getLocation(), customizations);
/*  66 */     this.fullyQualifiedClassName = decl.getExistingClassRef();
/*  67 */     assert this.fullyQualifiedClassName != null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CClassRef(Model model, XSComponent source, BIEnum decl, CCustomizations customizations) {
/*  76 */     super(model, source, decl.getLocation(), customizations);
/*  77 */     this.fullyQualifiedClassName = decl.ref;
/*  78 */     assert this.fullyQualifiedClassName != null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAbstract() {}
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   public NType getType() {
/*  91 */     return (NType)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass toType(Outline o, Aspect aspect) {
/* 100 */     if (this.clazz == null)
/* 101 */       this.clazz = o.getCodeModel().ref(this.fullyQualifiedClassName); 
/* 102 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public String fullName() {
/* 106 */     return this.fullyQualifiedClassName;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public CNonElement getInfo() {
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CElement getSubstitutionHead() {
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public CClassInfo getScope() {
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   public QName getElementName() {
/* 133 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isBoxedType() {
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/* 141 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CClassRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */