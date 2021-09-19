/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunctionWithParam;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSWildcardFunction;
/*     */ import com.sun.xml.xsom.visitor.XSWildcardVisitor;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
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
/*     */ public abstract class WildcardImpl
/*     */   extends ComponentImpl
/*     */   implements XSWildcard, Ref.Term
/*     */ {
/*     */   private final int mode;
/*     */   
/*     */   protected WildcardImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, int _mode) {
/*  46 */     super(owner, _annon, _loc, _fa);
/*  47 */     this.mode = _mode;
/*     */   }
/*     */   
/*     */   public int getMode() {
/*  51 */     return this.mode;
/*     */   } public WildcardImpl union(SchemaDocumentImpl owner, WildcardImpl rhs) {
/*     */     Other o;
/*     */     Finite f;
/*  55 */     if (this instanceof Any || rhs instanceof Any) {
/*  56 */       return new Any(owner, null, null, null, this.mode);
/*     */     }
/*  58 */     if (this instanceof Finite && rhs instanceof Finite) {
/*  59 */       Set<String> values = new HashSet<String>();
/*  60 */       values.addAll(((Finite)this).names);
/*  61 */       values.addAll(((Finite)rhs).names);
/*  62 */       return new Finite(owner, null, null, null, values, this.mode);
/*     */     } 
/*     */     
/*  65 */     if (this instanceof Other && rhs instanceof Other) {
/*  66 */       if (((Other)this).otherNamespace.equals(((Other)rhs).otherNamespace))
/*     */       {
/*  68 */         return new Other(owner, null, null, null, ((Other)this).otherNamespace, this.mode);
/*     */       }
/*     */       
/*  71 */       return new Other(owner, null, null, null, "", this.mode);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     if (this instanceof Other) {
/*  78 */       o = (Other)this; f = (Finite)rhs;
/*     */     } else {
/*  80 */       o = (Other)rhs; f = (Finite)this;
/*     */     } 
/*     */     
/*  83 */     if (f.names.contains(o.otherNamespace)) {
/*  84 */       return new Any(owner, null, null, null, this.mode);
/*     */     }
/*  86 */     return new Other(owner, null, null, null, o.otherNamespace, this.mode);
/*     */   }
/*     */   
/*     */   public static final class Any
/*     */     extends WildcardImpl
/*     */     implements XSWildcard.Any {
/*     */     public Any(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, int _mode) {
/*  93 */       super(owner, _annon, _loc, _fa, _mode);
/*     */     }
/*     */     
/*     */     public boolean acceptsNamespace(String namespaceURI) {
/*  97 */       return true;
/*     */     }
/*     */     public void visit(XSWildcardVisitor visitor) {
/* 100 */       visitor.any(this);
/*     */     }
/*     */     public Object apply(XSWildcardFunction function) {
/* 103 */       return function.any(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Other extends WildcardImpl implements XSWildcard.Other { private final String otherNamespace;
/*     */     
/*     */     public Other(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, String otherNamespace, int _mode) {
/* 110 */       super(owner, _annon, _loc, _fa, _mode);
/* 111 */       this.otherNamespace = otherNamespace;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getOtherNamespace() {
/* 116 */       return this.otherNamespace;
/*     */     }
/*     */     public boolean acceptsNamespace(String namespaceURI) {
/* 119 */       return !namespaceURI.equals(this.otherNamespace);
/*     */     }
/*     */     
/*     */     public void visit(XSWildcardVisitor visitor) {
/* 123 */       visitor.other(this);
/*     */     }
/*     */     public Object apply(XSWildcardFunction function) {
/* 126 */       return function.other(this);
/*     */     } }
/*     */   
/*     */   public static final class Finite extends WildcardImpl implements XSWildcard.Union { private final Set<String> names;
/*     */     private final Set<String> namesView;
/*     */     
/*     */     public Finite(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, Set<String> ns, int _mode) {
/* 133 */       super(owner, _annon, _loc, _fa, _mode);
/* 134 */       this.names = ns;
/* 135 */       this.namesView = Collections.unmodifiableSet(this.names);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Iterator<String> iterateNamespaces() {
/* 142 */       return this.names.iterator();
/*     */     }
/*     */     
/*     */     public Collection<String> getNamespaces() {
/* 146 */       return this.namesView;
/*     */     }
/*     */     
/*     */     public boolean acceptsNamespace(String namespaceURI) {
/* 150 */       return this.names.contains(namespaceURI);
/*     */     }
/*     */     
/*     */     public void visit(XSWildcardVisitor visitor) {
/* 154 */       visitor.union(this);
/*     */     }
/*     */     public Object apply(XSWildcardFunction function) {
/* 157 */       return function.union(this);
/*     */     } }
/*     */ 
/*     */   
/*     */   public final void visit(XSVisitor visitor) {
/* 162 */     visitor.wildcard(this);
/*     */   }
/*     */   public final void visit(XSTermVisitor visitor) {
/* 165 */     visitor.wildcard(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/* 168 */     return function.wildcard(this);
/*     */   }
/*     */   
/*     */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/* 172 */     return (T)function.wildcard(this, param);
/*     */   }
/*     */   
/*     */   public Object apply(XSFunction function) {
/* 176 */     return function.wildcard(this);
/*     */   }
/*     */   
/*     */   public boolean isWildcard() {
/* 180 */     return true;
/* 181 */   } public boolean isModelGroupDecl() { return false; }
/* 182 */   public boolean isModelGroup() { return false; } public boolean isElementDecl() {
/* 183 */     return false;
/*     */   }
/* 185 */   public XSWildcard asWildcard() { return this; }
/* 186 */   public XSModelGroupDecl asModelGroupDecl() { return null; }
/* 187 */   public XSModelGroup asModelGroup() { return null; } public XSElementDecl asElementDecl() {
/* 188 */     return null;
/*     */   }
/*     */   
/*     */   public XSTerm getTerm() {
/* 192 */     return (XSTerm)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\WildcardImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */