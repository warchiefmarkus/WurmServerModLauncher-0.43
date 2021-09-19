/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.XSListSimpleType;
/*    */ import com.sun.xml.xsom.XSSimpleType;
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
/*    */ public class ListSimpleTypeImpl
/*    */   extends SimpleTypeImpl
/*    */   implements XSListSimpleType
/*    */ {
/*    */   private final Ref.SimpleType itemType;
/*    */   
/*    */   public ListSimpleTypeImpl(SchemaImpl _parent, AnnotationImpl _annon, Locator _loc, String _name, boolean _anonymous, Ref.SimpleType _itemType) {
/* 28 */     super(_parent, _annon, _loc, _name, _anonymous, (Ref.SimpleType)_parent.parent.anySimpleType);
/*    */ 
/*    */     
/* 31 */     this.itemType = _itemType;
/*    */   }
/*    */   
/*    */   public XSSimpleType getItemType() {
/* 35 */     return this.itemType.getSimpleType();
/*    */   }
/*    */   public void visit(XSSimpleTypeVisitor visitor) {
/* 38 */     visitor.listSimpleType(this);
/*    */   }
/*    */   public Object apply(XSSimpleTypeFunction function) {
/* 41 */     return function.listSimpleType(this);
/*    */   }
/*    */   
/*    */   public XSFacet getFacet(String name) {
/* 45 */     return null;
/*    */   } public XSVariety getVariety() {
/* 47 */     return XSVariety.LIST;
/*    */   }
/* 49 */   public boolean isList() { return true; } public XSListSimpleType asList() {
/* 50 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\ListSimpleTypeImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */