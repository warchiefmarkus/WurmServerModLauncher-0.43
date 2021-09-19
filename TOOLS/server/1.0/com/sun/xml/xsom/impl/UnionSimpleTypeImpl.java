/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.XSUnionSimpleType;
/*    */ import com.sun.xml.xsom.XSVariety;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*    */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnionSimpleTypeImpl
/*    */   extends SimpleTypeImpl
/*    */   implements XSUnionSimpleType
/*    */ {
/*    */   private final Ref.SimpleType[] memberTypes;
/*    */   
/*    */   public UnionSimpleTypeImpl(SchemaImpl _parent, AnnotationImpl _annon, Locator _loc, String _name, boolean _anonymous, Ref.SimpleType[] _members) {
/* 28 */     super(_parent, _annon, _loc, _name, _anonymous, (Ref.SimpleType)_parent.parent.anySimpleType);
/*    */ 
/*    */     
/* 31 */     this.memberTypes = _members;
/*    */   }
/*    */   
/*    */   public XSSimpleType getMember(int idx) {
/* 35 */     return this.memberTypes[idx].getSimpleType(); } public int getMemberSize() {
/* 36 */     return this.memberTypes.length;
/*    */   }
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 39 */     visitor.unionSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 42 */     return function.unionSimpleType(this);
/*    */   }
/*    */   
/*    */   public XSFacet getFacet(String name) {
/* 46 */     return null;
/*    */   } public XSVariety getVariety() {
/* 48 */     return XSVariety.LIST;
/*    */   }
/* 50 */   public boolean isUnion() { return true; } public XSUnionSimpleType asUnion() {
/* 51 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\UnionSimpleTypeImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */