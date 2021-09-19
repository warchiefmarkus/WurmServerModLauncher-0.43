/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import javax.activation.MimeType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractCTypeInfoImpl
/*     */   implements CTypeInfo
/*     */ {
/*     */   private final CCustomizations customizations;
/*     */   private final XSComponent source;
/*     */   
/*     */   protected AbstractCTypeInfoImpl(Model model, XSComponent source, CCustomizations customizations) {
/*  66 */     if (customizations == null) {
/*  67 */       customizations = CCustomizations.EMPTY;
/*     */     } else {
/*  69 */       customizations.setParent(model, this);
/*  70 */     }  this.customizations = customizations;
/*  71 */     this.source = source;
/*     */   }
/*     */   
/*     */   public final boolean isCollection() {
/*  75 */     return false;
/*     */   }
/*     */   
/*     */   public final CAdapter getAdapterUse() {
/*  79 */     return null;
/*     */   }
/*     */   
/*     */   public final ID idUse() {
/*  83 */     return ID.NONE;
/*     */   }
/*     */   
/*     */   public final XSComponent getSchemaComponent() {
/*  87 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean canBeReferencedByIDREF() {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   public CCustomizations getCustomizations() {
/* 107 */     return this.customizations;
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression createConstant(Outline outline, XmlString lexical) {
/* 112 */     return null;
/*     */   }
/*     */   
/*     */   public final Locatable getUpstream() {
/* 116 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public final Location getLocation() {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\AbstractCTypeInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */