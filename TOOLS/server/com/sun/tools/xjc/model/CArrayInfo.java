/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.outline.Aspect;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.v2.model.core.ArrayInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.impl.ArrayInfoImpl;
/*     */ import com.sun.xml.xsom.XSComponent;
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
/*     */ public final class CArrayInfo
/*     */   extends AbstractCTypeInfoImpl
/*     */   implements ArrayInfo<NType, NClass>, CNonElement, NType
/*     */ {
/*     */   private final CNonElement itemType;
/*     */   private final QName typeName;
/*     */   
/*     */   public CArrayInfo(Model model, CNonElement itemType, XSComponent source, CCustomizations customizations) {
/*  68 */     super(model, source, customizations);
/*  69 */     this.itemType = itemType;
/*  70 */     assert itemType.getTypeName() != null;
/*  71 */     this.typeName = ArrayInfoImpl.calcArrayTypeName(itemType.getTypeName());
/*     */   }
/*     */   
/*     */   public CNonElement getItemType() {
/*  75 */     return this.itemType;
/*     */   }
/*     */   
/*     */   public QName getTypeName() {
/*  79 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public boolean isSimpleType() {
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public CNonElement getInfo() {
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public JType toType(Outline o, Aspect aspect) {
/*  92 */     return (JType)this.itemType.toType(o, aspect).array();
/*     */   }
/*     */   
/*     */   public NType getType() {
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isBoxedType() {
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   public String fullName() {
/* 104 */     return ((NType)this.itemType.getType()).fullName() + "[]";
/*     */   }
/*     */   
/*     */   public Locator getLocator() {
/* 108 */     return Model.EMPTY_LOCATOR;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CArrayInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */