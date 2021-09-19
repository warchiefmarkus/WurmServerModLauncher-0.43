/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSElementDecl;
/*    */ import com.sun.xml.xsom.XSModelGroup;
/*    */ import com.sun.xml.xsom.XSModelGroupDecl;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ import com.sun.xml.xsom.XSTerm;
/*    */ import com.sun.xml.xsom.XSWildcard;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.ComponentImpl;
/*    */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*    */ import com.sun.xml.xsom.impl.ParticleImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*    */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ public class ModelGroupImpl
/*    */   extends ComponentImpl
/*    */   implements XSModelGroup, Ref.Term
/*    */ {
/*    */   private final XSParticle[] children;
/*    */   private final XSModelGroup.Compositor compositor;
/*    */   
/*    */   public ModelGroupImpl(SchemaImpl owner, AnnotationImpl _annon, Locator _loc, XSModelGroup.Compositor _compositor, XSParticle[] _children) {
/* 30 */     super(owner, _annon, _loc);
/* 31 */     this.compositor = _compositor;
/* 32 */     this.children = _children;
/*    */     
/* 34 */     if (this.compositor == null)
/* 35 */       throw new IllegalArgumentException(); 
/* 36 */     for (int i = this.children.length - 1; i >= 0; i--) {
/* 37 */       if (this.children[i] == null)
/* 38 */         throw new IllegalArgumentException(); 
/*    */     } 
/*    */   }
/*    */   
/* 42 */   public XSParticle getChild(int idx) { return this.children[idx]; } public int getSize() {
/* 43 */     return this.children.length;
/*    */   } public XSParticle[] getChildren() {
/* 45 */     return this.children;
/*    */   }
/*    */   
/*    */   public XSModelGroup.Compositor getCompositor() {
/* 49 */     return this.compositor;
/*    */   }
/*    */   
/*    */   public void redefine(ModelGroupDeclImpl oldMG) {
/* 53 */     for (int i = 0; i < this.children.length; i++) {
/* 54 */       ParticleImpl p = (ParticleImpl)this.children[i];
/* 55 */       p.redefine(oldMG);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWildcard() {
/* 61 */     return false; }
/* 62 */   public boolean isModelGroupDecl() { return false; }
/* 63 */   public boolean isModelGroup() { return true; } public boolean isElementDecl() {
/* 64 */     return false;
/*    */   }
/* 66 */   public XSWildcard asWildcard() { return null; }
/* 67 */   public XSModelGroupDecl asModelGroupDecl() { return null; }
/* 68 */   public XSModelGroup asModelGroup() { return this; } public XSElementDecl asElementDecl() {
/* 69 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 74 */     visitor.modelGroup(this);
/*    */   }
/*    */   public void visit(XSTermVisitor visitor) {
/* 77 */     visitor.modelGroup(this);
/*    */   }
/*    */   public Object apply(XSTermFunction function) {
/* 80 */     return function.modelGroup(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 83 */     return function.modelGroup(this);
/*    */   }
/*    */   
/*    */   public XSTerm getTerm() {
/* 87 */     return (XSTerm)this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\ModelGroupImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */