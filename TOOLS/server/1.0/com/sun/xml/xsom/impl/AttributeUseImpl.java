/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAttributeDecl;
/*    */ import com.sun.xml.xsom.XSAttributeUse;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.ComponentImpl;
/*    */ import com.sun.xml.xsom.impl.Ref;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class AttributeUseImpl
/*    */   extends ComponentImpl
/*    */   implements XSAttributeUse
/*    */ {
/*    */   private final Ref.Attribute att;
/*    */   private final String defaultValue;
/*    */   private final String fixedValue;
/*    */   private final ValidationContext context;
/*    */   private final boolean required;
/*    */   
/*    */   public AttributeUseImpl(SchemaImpl owner, AnnotationImpl ann, Locator loc, Ref.Attribute _decl, String def, String fixed, ValidationContext _context, boolean req) {
/* 25 */     super(owner, ann, loc);
/*    */     
/* 27 */     this.att = _decl;
/* 28 */     this.defaultValue = def;
/* 29 */     this.fixedValue = fixed;
/* 30 */     this.context = _context;
/* 31 */     this.required = req;
/*    */   }
/*    */   
/*    */   public XSAttributeDecl getDecl() {
/* 35 */     return this.att.getAttribute();
/*    */   }
/*    */   
/*    */   public String getDefaultValue() {
/* 39 */     if (this.defaultValue != null) return this.defaultValue; 
/* 40 */     return getDecl().getDefaultValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getFixedValue() {
/* 45 */     if (this.fixedValue != null) return this.fixedValue; 
/* 46 */     return getDecl().getFixedValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public ValidationContext getContext() {
/* 51 */     if (this.fixedValue != null || this.defaultValue != null) return this.context; 
/* 52 */     return getDecl().getContext();
/*    */   }
/*    */   
/*    */   public boolean isRequired() {
/* 56 */     return this.required;
/*    */   }
/*    */   public Object apply(XSFunction f) {
/* 59 */     return f.attributeUse(this);
/*    */   }
/*    */   public void visit(XSVisitor v) {
/* 62 */     v.attributeUse(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\AttributeUseImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */