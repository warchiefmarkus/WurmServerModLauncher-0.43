/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSNotation;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.DeclarationImpl;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
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
/*    */ 
/*    */ public class NotationImpl
/*    */   extends DeclarationImpl
/*    */   implements XSNotation
/*    */ {
/*    */   private final String publicId;
/*    */   private final String systemId;
/*    */   
/*    */   public NotationImpl(SchemaImpl owner, AnnotationImpl _annon, Locator _loc, String _tns, String _name, String _publicId, String _systemId) {
/* 28 */     super(owner, _annon, _loc, _tns, _name, false);
/*    */     
/* 30 */     this.publicId = _publicId;
/* 31 */     this.systemId = _systemId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPublicId() {
/* 37 */     return this.publicId; } public String getSystemId() {
/* 38 */     return this.systemId;
/*    */   }
/*    */   public void visit(XSVisitor visitor) {
/* 41 */     visitor.notation(this);
/*    */   }
/*    */   
/*    */   public Object apply(XSFunction function) {
/* 45 */     return function.notation(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\NotationImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */