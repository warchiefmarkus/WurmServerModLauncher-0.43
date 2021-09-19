/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunctionWithParam;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ public class ModelGroupImpl
/*     */   extends ComponentImpl
/*     */   implements XSModelGroup, Ref.Term
/*     */ {
/*     */   private final ParticleImpl[] children;
/*     */   private final XSModelGroup.Compositor compositor;
/*     */   
/*     */   public ModelGroupImpl(SchemaDocumentImpl owner, AnnotationImpl _annon, Locator _loc, ForeignAttributesImpl _fa, XSModelGroup.Compositor _compositor, ParticleImpl[] _children) {
/*  44 */     super(owner, _annon, _loc, _fa);
/*  45 */     this.compositor = _compositor;
/*  46 */     this.children = _children;
/*     */     
/*  48 */     if (this.compositor == null)
/*  49 */       throw new IllegalArgumentException(); 
/*  50 */     for (int i = this.children.length - 1; i >= 0; i--) {
/*  51 */       if (this.children[i] == null)
/*  52 */         throw new IllegalArgumentException(); 
/*     */     } 
/*     */   }
/*     */   
/*  56 */   public ParticleImpl getChild(int idx) { return this.children[idx]; } public int getSize() {
/*  57 */     return this.children.length;
/*     */   } public ParticleImpl[] getChildren() {
/*  59 */     return this.children;
/*     */   }
/*     */   
/*     */   public XSModelGroup.Compositor getCompositor() {
/*  63 */     return this.compositor;
/*     */   }
/*     */   
/*     */   public void redefine(ModelGroupDeclImpl oldMG) {
/*  67 */     for (ParticleImpl p : this.children)
/*  68 */       p.redefine(oldMG); 
/*     */   }
/*     */   
/*     */   public Iterator<XSParticle> iterator() {
/*  72 */     return Arrays.<XSParticle>asList((XSParticle[])this.children).iterator();
/*     */   }
/*     */   
/*     */   public boolean isWildcard() {
/*  76 */     return false;
/*  77 */   } public boolean isModelGroupDecl() { return false; }
/*  78 */   public boolean isModelGroup() { return true; } public boolean isElementDecl() {
/*  79 */     return false;
/*     */   }
/*  81 */   public XSWildcard asWildcard() { return null; }
/*  82 */   public XSModelGroupDecl asModelGroupDecl() { return null; }
/*  83 */   public XSModelGroup asModelGroup() { return this; } public XSElementDecl asElementDecl() {
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(XSVisitor visitor) {
/*  89 */     visitor.modelGroup(this);
/*     */   }
/*     */   public void visit(XSTermVisitor visitor) {
/*  92 */     visitor.modelGroup(this);
/*     */   }
/*     */   public Object apply(XSTermFunction function) {
/*  95 */     return function.modelGroup(this);
/*     */   }
/*     */   
/*     */   public <T, P> T apply(XSTermFunctionWithParam<T, P> function, P param) {
/*  99 */     return (T)function.modelGroup(this, param);
/*     */   }
/*     */   
/*     */   public Object apply(XSFunction function) {
/* 103 */     return function.modelGroup(this);
/*     */   }
/*     */   
/*     */   public XSTerm getTerm() {
/* 107 */     return (XSTerm)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ModelGroupImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */