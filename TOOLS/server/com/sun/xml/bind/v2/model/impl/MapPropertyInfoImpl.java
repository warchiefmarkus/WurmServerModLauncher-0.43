/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.core.MapPropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlElementWrapper;
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
/*     */ class MapPropertyInfoImpl<T, C, F, M>
/*     */   extends PropertyInfoImpl<T, C, F, M>
/*     */   implements MapPropertyInfo<T, C>
/*     */ {
/*     */   private final QName xmlName;
/*     */   private boolean nil;
/*     */   private final T keyType;
/*     */   private final T valueType;
/*     */   private NonElement<T, C> keyTypeInfo;
/*     */   private NonElement<T, C> valueTypeInfo;
/*     */   
/*     */   public MapPropertyInfoImpl(ClassInfoImpl<T, C, F, M> ci, PropertySeed<T, C, F, M> seed) {
/*  67 */     super(ci, seed);
/*     */     
/*  69 */     XmlElementWrapper xe = (XmlElementWrapper)seed.readAnnotation(XmlElementWrapper.class);
/*  70 */     this.xmlName = calcXmlName(xe);
/*  71 */     this.nil = (xe != null && xe.nillable());
/*     */     
/*  73 */     T raw = getRawType();
/*  74 */     T bt = (T)nav().getBaseClass(raw, nav().asDecl(Map.class));
/*  75 */     assert bt != null;
/*     */     
/*  77 */     if (nav().isParameterizedType(bt)) {
/*  78 */       this.keyType = (T)nav().getTypeArgument(bt, 0);
/*  79 */       this.valueType = (T)nav().getTypeArgument(bt, 1);
/*     */     } else {
/*  81 */       this.keyType = this.valueType = (T)nav().ref(Object.class);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Collection<? extends TypeInfo<T, C>> ref() {
/*  86 */     return (Collection)Arrays.asList((Object[])new NonElement[] { getKeyType(), getValueType() });
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/*  90 */     return PropertyKind.MAP;
/*     */   }
/*     */   
/*     */   public QName getXmlName() {
/*  94 */     return this.xmlName;
/*     */   }
/*     */   
/*     */   public boolean isCollectionNillable() {
/*  98 */     return this.nil;
/*     */   }
/*     */   
/*     */   public NonElement<T, C> getKeyType() {
/* 102 */     if (this.keyTypeInfo == null)
/* 103 */       this.keyTypeInfo = getTarget(this.keyType); 
/* 104 */     return this.keyTypeInfo;
/*     */   }
/*     */   
/*     */   public NonElement<T, C> getValueType() {
/* 108 */     if (this.valueTypeInfo == null)
/* 109 */       this.valueTypeInfo = getTarget(this.valueType); 
/* 110 */     return this.valueTypeInfo;
/*     */   }
/*     */   
/*     */   public NonElement<T, C> getTarget(T type) {
/* 114 */     assert this.parent.builder != null : "this method must be called during the build stage";
/* 115 */     return this.parent.builder.getTypeInfo(type, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\MapPropertyInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */