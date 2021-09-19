/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSContentType;
/*    */ import com.sun.xml.xsom.XSDeclaration;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.XSTerm;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.ComponentImpl;
/*    */ import com.sun.xml.xsom.impl.ContentTypeImpl;
/*    */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*    */ import com.sun.xml.xsom.impl.ModelGroupImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*    */ import com.sun.xml.xsom.visitor.XSContentTypeFunction;
/*    */ import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class ParticleImpl
/*    */   extends ComponentImpl implements XSParticle, ContentTypeImpl {
/*    */   private Ref.Term term;
/*    */   private int maxOccurs;
/*    */   private int minOccurs;
/*    */   
/*    */   public ParticleImpl(SchemaImpl owner, AnnotationImpl _ann, Ref.Term _term, Locator _loc, int _maxOccurs, int _minOccurs) {
/* 29 */     super(owner, _ann, _loc);
/* 30 */     this.term = _term;
/* 31 */     this.maxOccurs = _maxOccurs;
/* 32 */     this.minOccurs = _minOccurs;
/*    */   }
/*    */   public ParticleImpl(SchemaImpl owner, AnnotationImpl _ann, Ref.Term _term, Locator _loc) {
/* 35 */     this(owner, _ann, _term, _loc, 1, 1);
/*    */   }
/*    */   
/*    */   public XSTerm getTerm() {
/* 39 */     return this.term.getTerm();
/*    */   }
/*    */   public int getMaxOccurs() {
/* 42 */     return this.maxOccurs;
/*    */   }
/*    */   public int getMinOccurs() {
/* 45 */     return this.minOccurs;
/*    */   }
/*    */   
/*    */   public void redefine(ModelGroupDeclImpl oldMG) {
/* 49 */     if (this.term instanceof ModelGroupImpl) {
/* 50 */       ((ModelGroupImpl)this.term).redefine(oldMG);
/*    */       return;
/*    */     } 
/* 53 */     if (this.term instanceof DelayedRef.ModelGroup) {
/* 54 */       ((DelayedRef)this.term).redefine((XSDeclaration)oldMG);
/*    */     }
/*    */   }
/*    */   
/*    */   public XSSimpleType asSimpleType() {
/* 59 */     return null; }
/* 60 */   public XSParticle asParticle() { return this; } public XSContentType asEmpty() {
/* 61 */     return null;
/*    */   }
/*    */   
/*    */   public final Object apply(XSFunction function) {
/* 65 */     return function.particle(this);
/*    */   }
/*    */   public final Object apply(XSContentTypeFunction function) {
/* 68 */     return function.particle(this);
/*    */   }
/*    */   public final void visit(XSVisitor visitor) {
/* 71 */     visitor.particle(this);
/*    */   }
/*    */   public final void visit(XSContentTypeVisitor visitor) {
/* 74 */     visitor.particle(this);
/*    */   }
/*    */   
/*    */   public XSContentType getContentType() {
/* 78 */     return (XSContentType)this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\ParticleImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */