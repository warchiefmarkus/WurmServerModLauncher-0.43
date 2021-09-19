/*     */ package com.sun.xml.xsom.impl;
/*     */ 
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.SchemaDocumentImpl;
/*     */ import com.sun.xml.xsom.visitor.XSContentTypeFunction;
/*     */ import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.List;
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
/*     */ public class ParticleImpl
/*     */   extends ComponentImpl
/*     */   implements XSParticle, ContentTypeImpl
/*     */ {
/*     */   private Ref.Term term;
/*     */   private int maxOccurs;
/*     */   private int minOccurs;
/*     */   
/*     */   public ParticleImpl(SchemaDocumentImpl owner, AnnotationImpl _ann, Ref.Term _term, Locator _loc, int _maxOccurs, int _minOccurs) {
/*  42 */     super(owner, _ann, _loc, null);
/*  43 */     this.term = _term;
/*  44 */     this.maxOccurs = _maxOccurs;
/*  45 */     this.minOccurs = _minOccurs;
/*     */   }
/*     */   public ParticleImpl(SchemaDocumentImpl owner, AnnotationImpl _ann, Ref.Term _term, Locator _loc) {
/*  48 */     this(owner, _ann, _term, _loc, 1, 1);
/*     */   }
/*     */   
/*     */   public XSTerm getTerm() {
/*  52 */     return this.term.getTerm();
/*     */   }
/*     */   public int getMaxOccurs() {
/*  55 */     return this.maxOccurs;
/*     */   }
/*     */   public boolean isRepeated() {
/*  58 */     return (this.maxOccurs != 0 && this.maxOccurs != 1);
/*     */   }
/*     */   
/*     */   public int getMinOccurs() {
/*  62 */     return this.minOccurs;
/*     */   }
/*     */   
/*     */   public void redefine(ModelGroupDeclImpl oldMG) {
/*  66 */     if (this.term instanceof ModelGroupImpl) {
/*  67 */       ((ModelGroupImpl)this.term).redefine(oldMG);
/*     */       return;
/*     */     } 
/*  70 */     if (this.term instanceof DelayedRef.ModelGroup) {
/*  71 */       ((DelayedRef)this.term).redefine(oldMG);
/*     */     }
/*     */   }
/*     */   
/*     */   public XSSimpleType asSimpleType() {
/*  76 */     return null; }
/*  77 */   public XSParticle asParticle() { return this; } public XSContentType asEmpty() {
/*  78 */     return null;
/*     */   }
/*     */   
/*     */   public final Object apply(XSFunction function) {
/*  82 */     return function.particle(this);
/*     */   }
/*     */   public final Object apply(XSContentTypeFunction function) {
/*  85 */     return function.particle(this);
/*     */   }
/*     */   public final void visit(XSVisitor visitor) {
/*  88 */     visitor.particle(this);
/*     */   }
/*     */   public final void visit(XSContentTypeVisitor visitor) {
/*  91 */     visitor.particle(this);
/*     */   }
/*     */   
/*     */   public XSContentType getContentType() {
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getForeignAttributes() {
/* 103 */     return getTerm().getForeignAttributes();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\ParticleImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */