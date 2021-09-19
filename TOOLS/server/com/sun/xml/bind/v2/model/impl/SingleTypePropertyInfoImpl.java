/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class SingleTypePropertyInfoImpl<T, C, F, M>
/*     */   extends PropertyInfoImpl<T, C, F, M>
/*     */ {
/*     */   private NonElement<T, C> type;
/*     */   private final Accessor acc;
/*     */   private Transducer xducer;
/*     */   
/*     */   public SingleTypePropertyInfoImpl(ClassInfoImpl<T, C, F, M> classInfo, PropertySeed<T, C, F, M> seed) {
/*  71 */     super(classInfo, seed);
/*  72 */     if (this instanceof RuntimePropertyInfo) {
/*  73 */       Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed)seed).getAccessor();
/*  74 */       if (getAdapter() != null && !isCollection())
/*     */       {
/*     */         
/*  77 */         rawAcc = rawAcc.adapt(((RuntimePropertyInfo)this).getAdapter()); } 
/*  78 */       this.acc = rawAcc;
/*     */     } else {
/*  80 */       this.acc = null;
/*     */     } 
/*     */   }
/*     */   public List<? extends NonElement<T, C>> ref() {
/*  84 */     return Collections.singletonList(getTarget());
/*     */   }
/*     */   
/*     */   public NonElement<T, C> getTarget() {
/*  88 */     if (this.type == null) {
/*  89 */       assert this.parent.builder != null : "this method must be called during the build stage";
/*  90 */       this.type = this.parent.builder.getTypeInfo(getIndividualType(), this);
/*     */     } 
/*  92 */     return this.type;
/*     */   }
/*     */   
/*     */   public PropertyInfo<T, C> getSource() {
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public void link() {
/* 100 */     super.link();
/*     */     
/* 102 */     if (!this.type.isSimpleType() && id() != ID.IDREF) {
/* 103 */       this.parent.builder.reportError(new IllegalAnnotationException(Messages.SIMPLE_TYPE_IS_REQUIRED.format(new Object[0]), this.seed));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     if (!isCollection() && this.seed.hasAnnotation(XmlList.class)) {
/* 110 */       this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_ON_SINGLE_PROPERTY.format(new Object[0]), this));
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Accessor getAccessor() {
/* 130 */     return this.acc;
/*     */   }
/*     */ 
/*     */   
/*     */   public Transducer getTransducer() {
/* 135 */     if (this.xducer == null) {
/* 136 */       this.xducer = RuntimeModelBuilder.createTransducer((RuntimeNonElementRef)this);
/* 137 */       if (this.xducer == null)
/*     */       {
/*     */         
/* 140 */         this.xducer = RuntimeBuiltinLeafInfoImpl.STRING;
/*     */       }
/*     */     } 
/* 143 */     return this.xducer;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\SingleTypePropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */