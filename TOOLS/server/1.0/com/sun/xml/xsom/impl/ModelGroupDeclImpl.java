/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSElementDecl;
/*    */ import com.sun.xml.xsom.XSModelGroup;
/*    */ import com.sun.xml.xsom.XSModelGroupDecl;
/*    */ import com.sun.xml.xsom.XSTerm;
/*    */ import com.sun.xml.xsom.XSWildcard;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.DeclarationImpl;
/*    */ import com.sun.xml.xsom.impl.ModelGroupImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*    */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModelGroupDeclImpl
/*    */   extends DeclarationImpl
/*    */   implements XSModelGroupDecl, Ref.Term
/*    */ {
/*    */   private final ModelGroupImpl modelGroup;
/*    */   
/*    */   public ModelGroupDeclImpl(SchemaImpl owner, AnnotationImpl _annon, Locator _loc, String _targetNamespace, String _name, ModelGroupImpl _modelGroup) {
/* 31 */     super(owner, _annon, _loc, _targetNamespace, _name, false);
/* 32 */     this.modelGroup = _modelGroup;
/*    */     
/* 34 */     if (this.modelGroup == null)
/* 35 */       throw new IllegalArgumentException(); 
/*    */   }
/*    */   
/*    */   public XSModelGroup getModelGroup() {
/* 39 */     return (XSModelGroup)this.modelGroup;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void redefine(com.sun.xml.xsom.impl.ModelGroupDeclImpl oldMG) {
/* 46 */     this.modelGroup.redefine(oldMG);
/*    */   }
/*    */ 
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 51 */     visitor.modelGroupDecl(this);
/*    */   }
/*    */   public void visit(XSTermVisitor visitor) {
/* 54 */     visitor.modelGroupDecl(this);
/*    */   }
/*    */   public Object apply(XSTermFunction function) {
/* 57 */     return function.modelGroupDecl(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 60 */     return function.modelGroupDecl(this);
/*    */   }
/*    */   
/*    */   public boolean isWildcard() {
/* 64 */     return false;
/* 65 */   } public boolean isModelGroupDecl() { return true; }
/* 66 */   public boolean isModelGroup() { return false; } public boolean isElementDecl() {
/* 67 */     return false;
/*    */   }
/* 69 */   public XSWildcard asWildcard() { return null; }
/* 70 */   public XSModelGroupDecl asModelGroupDecl() { return this; }
/* 71 */   public XSModelGroup asModelGroup() { return null; } public XSElementDecl asElementDecl() {
/* 72 */     return null;
/*    */   }
/*    */   
/*    */   public XSTerm getTerm() {
/* 76 */     return (XSTerm)this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\ModelGroupDeclImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */