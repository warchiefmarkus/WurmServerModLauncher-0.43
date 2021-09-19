/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.bind.v2.model.annotation.MethodLocatable;
/*     */ import com.sun.xml.bind.v2.model.core.RegistryInfo;
/*     */ import com.sun.xml.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlElementDecl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RegistryInfoImpl<T, C, F, M>
/*     */   implements Locatable, RegistryInfo<T, C>
/*     */ {
/*     */   final C registryClass;
/*     */   private final Locatable upstream;
/*     */   private final Navigator<T, C, F, M> nav;
/*  70 */   private final Set<TypeInfo<T, C>> references = new LinkedHashSet<TypeInfo<T, C>>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   RegistryInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C registryClass) {
/*  76 */     this.nav = builder.nav;
/*  77 */     this.registryClass = registryClass;
/*  78 */     this.upstream = upstream;
/*  79 */     builder.registries.put(getPackageName(), this);
/*     */     
/*  81 */     if (this.nav.getDeclaredField(registryClass, "_useJAXBProperties") != null) {
/*     */ 
/*     */       
/*  84 */       builder.reportError(new IllegalAnnotationException(Messages.MISSING_JAXB_PROPERTIES.format(new Object[] { getPackageName() }, ), this));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/*  92 */     for (M m : this.nav.getDeclaredMethods(registryClass)) {
/*  93 */       ElementInfoImpl<T, C, F, M> ei; XmlElementDecl em = (XmlElementDecl)builder.reader.getMethodAnnotation(XmlElementDecl.class, m, this);
/*     */ 
/*     */       
/*  96 */       if (em == null) {
/*  97 */         if (this.nav.getMethodName(m).startsWith("create"))
/*     */         {
/*  99 */           this.references.add((TypeInfo<T, C>)builder.getTypeInfo((T)this.nav.getReturnType(m), (Locatable)new MethodLocatable(this, m, this.nav)));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/* 109 */         ei = builder.createElementInfo(this, m);
/* 110 */       } catch (IllegalAnnotationException e) {
/* 111 */         builder.reportError(e);
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 117 */       builder.typeInfoSet.add(ei, builder);
/* 118 */       this.references.add(ei);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Locatable getUpstream() {
/* 123 */     return this.upstream;
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 127 */     return this.nav.getClassLocation(this.registryClass);
/*     */   }
/*     */   
/*     */   public Set<TypeInfo<T, C>> getReferences() {
/* 131 */     return this.references;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPackageName() {
/* 138 */     return this.nav.getPackageName(this.registryClass);
/*     */   }
/*     */   
/*     */   public C getClazz() {
/* 142 */     return this.registryClass;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RegistryInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */