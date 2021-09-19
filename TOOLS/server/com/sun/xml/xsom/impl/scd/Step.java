/*     */ package com.sun.xml.xsom.impl.scd;
/*     */ 
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.impl.UName;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Step<T extends XSComponent>
/*     */ {
/*     */   public final Axis<? extends T> axis;
/*  29 */   int predicate = -1;
/*     */   
/*     */   protected Step(Axis<? extends T> axis) {
/*  32 */     this.axis = axis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Iterator<? extends T> filter(Iterator<? extends T> paramIterator);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Iterator<T> evaluate(Iterator<XSComponent> nodeSet) {
/*  46 */     Iterator<T> r = new Iterators.Map<T, XSComponent>(nodeSet) {
/*     */         protected Iterator<? extends T> apply(XSComponent contextNode) {
/*  48 */           return Step.this.filter(Step.this.axis.iterator(contextNode));
/*     */         }
/*     */       };
/*     */ 
/*     */     
/*  53 */     r = new Iterators.Unique<T>(r);
/*     */     
/*  55 */     if (this.predicate >= 0) {
/*  56 */       XSComponent xSComponent; T item = null;
/*  57 */       for (int i = this.predicate; i > 0; i--) {
/*  58 */         if (!r.hasNext())
/*  59 */           return Iterators.empty(); 
/*  60 */         xSComponent = (XSComponent)r.next();
/*     */       } 
/*  62 */       return new Iterators.Singleton<T>((T)xSComponent);
/*     */     } 
/*     */     
/*  65 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Any
/*     */     extends Step<XSComponent>
/*     */   {
/*     */     public Any(Axis<? extends XSComponent> axis) {
/*  73 */       super(axis);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Iterator<? extends XSComponent> filter(Iterator<? extends XSComponent> base) {
/*  78 */       return base;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class Filtered<T extends XSComponent> extends Step<T> {
/*     */     protected Filtered(Axis<? extends T> axis) {
/*  84 */       super(axis);
/*     */     }
/*     */     
/*     */     protected Iterator<T> filter(Iterator<? extends T> base) {
/*  88 */       return new Iterators.Filter<T>(base) {
/*     */           protected boolean matches(T d) {
/*  90 */             return Step.Filtered.this.match(d);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract boolean match(T param1T);
/*     */   }
/*     */   
/*     */   static final class Named
/*     */     extends Filtered<XSDeclaration>
/*     */   {
/*     */     private final String nsUri;
/*     */     private final String localName;
/*     */     
/*     */     public Named(Axis<? extends XSDeclaration> axis, UName n) {
/* 106 */       this(axis, n.getNamespaceURI(), n.getName());
/*     */     }
/*     */     
/*     */     public Named(Axis<? extends XSDeclaration> axis, String nsUri, String localName) {
/* 110 */       super(axis);
/* 111 */       this.nsUri = nsUri;
/* 112 */       this.localName = localName;
/*     */     }
/*     */     
/*     */     protected boolean match(XSDeclaration d) {
/* 116 */       return (d.getName().equals(this.localName) && d.getTargetNamespace().equals(this.nsUri));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AnonymousType
/*     */     extends Filtered<XSType>
/*     */   {
/*     */     public AnonymousType(Axis<? extends XSType> axis) {
/* 125 */       super(axis);
/*     */     }
/*     */     
/*     */     protected boolean match(XSType node) {
/* 129 */       return node.isLocal();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Facet
/*     */     extends Filtered<XSFacet>
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     public Facet(Axis<XSFacet> axis, String facetName) {
/* 139 */       super(axis);
/* 140 */       this.name = facetName;
/*     */     }
/*     */     
/*     */     protected boolean match(XSFacet f) {
/* 144 */       return f.getName().equals(this.name);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Schema
/*     */     extends Filtered<XSSchema>
/*     */   {
/*     */     private final String uri;
/*     */     
/*     */     public Schema(Axis<XSSchema> axis, String uri) {
/* 154 */       super(axis);
/* 155 */       this.uri = uri;
/*     */     }
/*     */     
/*     */     protected boolean match(XSSchema d) {
/* 159 */       return d.getTargetNamespace().equals(this.uri);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\scd\Step.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */