/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.ComponentImpl;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
/*    */ import com.sun.xml.xsom.visitor.XSVisitor;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FacetImpl
/*    */   extends ComponentImpl
/*    */   implements XSFacet
/*    */ {
/*    */   private final String name;
/*    */   private final String value;
/*    */   private final ValidationContext context;
/*    */   private boolean fixed;
/*    */   
/*    */   public FacetImpl(SchemaImpl owner, AnnotationImpl _annon, Locator _loc, String _name, String _value, ValidationContext _context, boolean _fixed) {
/* 24 */     super(owner, _annon, _loc);
/*    */     
/* 26 */     this.name = _name;
/* 27 */     this.value = _value;
/* 28 */     this.context = _context;
/* 29 */     this.fixed = _fixed;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 33 */     return this.name;
/*    */   }
/*    */   public String getValue() {
/* 36 */     return this.value;
/*    */   }
/*    */   public ValidationContext getContext() {
/* 39 */     return this.context;
/*    */   }
/*    */   public boolean isFixed() {
/* 42 */     return this.fixed;
/*    */   }
/*    */   
/*    */   public void visit(XSVisitor visitor) {
/* 46 */     visitor.facet(this);
/*    */   }
/*    */   public Object apply(XSFunction function) {
/* 49 */     return function.facet(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\FacetImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */