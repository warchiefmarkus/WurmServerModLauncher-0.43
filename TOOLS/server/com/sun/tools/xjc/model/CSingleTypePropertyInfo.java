/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class CSingleTypePropertyInfo
/*     */   extends CPropertyInfo
/*     */ {
/*     */   protected final TypeUse type;
/*     */   private final QName schemaType;
/*     */   
/*     */   protected CSingleTypePropertyInfo(String name, TypeUse type, QName typeName, XSComponent source, CCustomizations customizations, Locator locator) {
/*  69 */     super(name, type.isCollection(), source, customizations, locator);
/*  70 */     this.type = type;
/*     */     
/*  72 */     if (needsExplicitTypeName(type, typeName)) {
/*  73 */       this.schemaType = typeName;
/*     */     } else {
/*  75 */       this.schemaType = null;
/*     */     } 
/*     */   }
/*     */   public QName getSchemaType() {
/*  79 */     return this.schemaType;
/*     */   }
/*     */   
/*     */   public final ID id() {
/*  83 */     return this.type.idUse();
/*     */   }
/*     */   
/*     */   public final MimeType getExpectedMimeType() {
/*  87 */     return this.type.getExpectedMimeType();
/*     */   }
/*     */   
/*     */   public final List<? extends CTypeInfo> ref() {
/*  91 */     return Collections.singletonList(getTarget());
/*     */   }
/*     */   
/*     */   public final CNonElement getTarget() {
/*  95 */     CNonElement r = this.type.getInfo();
/*  96 */     assert r != null;
/*  97 */     return r;
/*     */   }
/*     */   
/*     */   public final CAdapter getAdapter() {
/* 101 */     return this.type.getAdapterUse();
/*     */   }
/*     */   
/*     */   public final CSingleTypePropertyInfo getSource() {
/* 105 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CSingleTypePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */