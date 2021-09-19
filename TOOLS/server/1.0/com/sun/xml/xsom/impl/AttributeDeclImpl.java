/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.xsom.XSSimpleType;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.DeclarationImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeDeclImpl
/*    */   extends DeclarationImpl
/*    */   implements XSAttributeDecl, Ref.Attribute
/*    */ {
/*    */   private final Ref.SimpleType type;
/*    */   private final ValidationContext context;
/*    */   private final String defaultValue;
/*    */   private final String fixedValue;
/*    */   
/*    */   public AttributeDeclImpl(SchemaImpl owner, String _targetNamespace, String _name, AnnotationImpl _annon, Locator _loc, boolean _anonymous, String _defValue, String _fixedValue, ValidationContext _context, Ref.SimpleType _type) {
/* 28 */     super(owner, _annon, _loc, _targetNamespace, _name, _anonymous);
/*    */     
/* 30 */     if (_name == null) {
/* 31 */       throw new IllegalArgumentException();
/*    */     }
/* 33 */     this.defaultValue = _defValue;
/* 34 */     this.fixedValue = _fixedValue;
/* 35 */     this.context = _context;
/* 36 */     this.type = _type;
/*    */   }
/*    */   
/*    */   public XSSimpleType getType() {
/* 40 */     return this.type.getSimpleType();
/*    */   }
/*    */   public ValidationContext getContext() {
/* 43 */     return this.context;
/*    */   }
/*    */   public String getDefaultValue() {
/* 46 */     return this.defaultValue;
/*    */   }
/*    */   public String getFixedValue() {
/* 49 */     return this.fixedValue;
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 52 */     visitor.attributeDecl(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 55 */     return function.attributeDecl(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public XSAttributeDecl getAttribute() {
/* 60 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\AttributeDeclImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */