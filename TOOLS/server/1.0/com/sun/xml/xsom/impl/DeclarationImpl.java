/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSDeclaration;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.ComponentImpl;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.util.NameGetter;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class DeclarationImpl
/*    */   extends ComponentImpl
/*    */   implements XSDeclaration
/*    */ {
/*    */   private final String name;
/*    */   private final String targetNamespace;
/*    */   private final boolean anonymous;
/*    */   
/*    */   DeclarationImpl(SchemaImpl owner, AnnotationImpl _annon, Locator loc, String _targetNamespace, String _name, boolean _anonymous) {
/* 23 */     super(owner, _annon, loc);
/* 24 */     this.targetNamespace = _targetNamespace;
/* 25 */     this.name = _name;
/* 26 */     this.anonymous = _anonymous;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */   public String getTargetNamespace() {
/* 33 */     return this.targetNamespace;
/*    */   }
/*    */   
/*    */   public boolean isAnonymous() {
/* 37 */     return this.anonymous;
/*    */   }
/* 39 */   public final boolean isGlobal() { return !isAnonymous(); } public final boolean isLocal() {
/* 40 */     return isAnonymous();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 44 */     return NameGetter.get((XSComponent)this) + " " + getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\DeclarationImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */