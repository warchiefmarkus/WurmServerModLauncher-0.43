/*     */ package com.sun.xml.xsom.impl.scd;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractAxisImpl<T extends XSComponent>
/*     */   implements Axis<T>, XSFunction<Iterator<T>>
/*     */ {
/*     */   protected final Iterator<T> singleton(T t) {
/*  40 */     return Iterators.singleton(t);
/*     */   }
/*     */   
/*     */   protected final Iterator<T> union(T... items) {
/*  44 */     return new Iterators.Array<T>(items);
/*     */   }
/*     */   
/*     */   protected final Iterator<T> union(Iterator<? extends T> first, Iterator<? extends T> second) {
/*  48 */     return new Iterators.Union<T>(first, second);
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator(XSComponent contextNode) {
/*  52 */     return (Iterator<T>)contextNode.apply(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  59 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator(Iterator<? extends XSComponent> contextNodes) {
/*  66 */     return new Iterators.Map<T, XSComponent>(contextNodes) {
/*     */         protected Iterator<? extends T> apply(XSComponent u) {
/*  68 */           return AbstractAxisImpl.this.iterator(u);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public boolean isModelGroup() {
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   public Iterator<T> annotation(XSAnnotation ann) {
/*  78 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> attGroupDecl(XSAttGroupDecl decl) {
/*  82 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> attributeDecl(XSAttributeDecl decl) {
/*  86 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> attributeUse(XSAttributeUse use) {
/*  90 */     return empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> complexType(XSComplexType type) {
/*  95 */     XSParticle p = type.getContentType().asParticle();
/*  96 */     if (p != null) {
/*  97 */       return particle(p);
/*     */     }
/*  99 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> schema(XSSchema schema) {
/* 103 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> facet(XSFacet facet) {
/* 107 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> notation(XSNotation notation) {
/* 111 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> identityConstraint(XSIdentityConstraint decl) {
/* 115 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> xpath(XSXPath xpath) {
/* 119 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> simpleType(XSSimpleType simpleType) {
/* 123 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> particle(XSParticle particle) {
/* 127 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> empty(XSContentType empty) {
/* 131 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> wildcard(XSWildcard wc) {
/* 135 */     return empty();
/*     */   }
/*     */   
/*     */   public Iterator<T> modelGroupDecl(XSModelGroupDecl decl) {
/* 139 */     return empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> modelGroup(XSModelGroup group) {
/* 144 */     return new Iterators.Map<T, XSParticle>(group.iterator()) {
/*     */         protected Iterator<? extends T> apply(XSParticle p) {
/* 146 */           return AbstractAxisImpl.this.particle(p);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Iterator<T> elementDecl(XSElementDecl decl) {
/* 152 */     return empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Iterator<T> empty() {
/* 159 */     return Iterators.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\scd\AbstractAxisImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */