/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSComplexType;
/*    */ import com.sun.xml.xsom.XSContentType;
/*    */ import com.sun.xml.xsom.XSListSimpleType;
/*    */ import com.sun.xml.xsom.XSParticle;
/*    */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.XSType;
/*    */ import com.sun.xml.xsom.XSUnionSimpleType;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.ContentTypeImpl;
/*    */ import com.sun.xml.xsom.impl.DeclarationImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.impl.Util;
/*    */ import com.sun.xml.xsom.visitor.XSContentTypeFunction;
/*    */ import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SimpleTypeImpl
/*    */   extends DeclarationImpl
/*    */   implements XSSimpleType, ContentTypeImpl, Ref.SimpleType
/*    */ {
/*    */   private Ref.SimpleType baseType;
/*    */   
/*    */   SimpleTypeImpl(SchemaImpl _parent, AnnotationImpl _annon, Locator _loc, String _name, boolean _anonymous, Ref.SimpleType _baseType) {
/* 39 */     super(_parent, _annon, _loc, _parent.getTargetNamespace(), _name, _anonymous);
/*    */     
/* 41 */     this.baseType = _baseType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public XSType[] listSubstitutables() {
/* 47 */     return Util.listSubstitutables((XSType)this);
/*    */   }
/*    */   
/*    */   public void redefine(com.sun.xml.xsom.impl.SimpleTypeImpl st) {
/* 51 */     this.baseType = st;
/*    */   }
/*    */   
/* 54 */   public XSType getBaseType() { return (XSType)this.baseType.getSimpleType(); } public XSSimpleType getSimpleBaseType() {
/* 55 */     return this.baseType.getSimpleType();
/*    */   }
/*    */   public final int getDerivationMethod() {
/* 58 */     return 2;
/*    */   }
/*    */   
/* 61 */   public final XSSimpleType asSimpleType() { return this; }
/* 62 */   public final XSComplexType asComplexType() { return null; }
/* 63 */   public final boolean isSimpleType() { return true; }
/* 64 */   public final boolean isComplexType() { return false; }
/* 65 */   public final XSParticle asParticle() { return null; } public final XSContentType asEmpty() {
/* 66 */     return null;
/*    */   }
/*    */   
/* 69 */   public boolean isRestriction() { return false; }
/* 70 */   public boolean isList() { return false; }
/* 71 */   public boolean isUnion() { return false; }
/* 72 */   public XSRestrictionSimpleType asRestriction() { return null; }
/* 73 */   public XSListSimpleType asList() { return null; } public XSUnionSimpleType asUnion() {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final void visit(XSVisitor visitor) {
/* 80 */     visit((XSSimpleTypeVisitor)visitor);
/*    */   }
/*    */   public final void visit(XSContentTypeVisitor visitor) {
/* 83 */     visit((XSSimpleTypeVisitor)visitor);
/*    */   }
/*    */   public final Object apply(XSFunction function) {
/* 86 */     return function.simpleType(this);
/*    */   }
/*    */   public final Object apply(XSContentTypeFunction function) {
/* 89 */     return function.simpleType(this);
/*    */   }
/*    */   
/*    */   public XSType getType() {
/* 93 */     return (XSType)this;
/*    */   } public XSContentType getContentType() {
/* 95 */     return (XSContentType)this;
/*    */   } public XSSimpleType getSimpleType() {
/* 97 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\SimpleTypeImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */