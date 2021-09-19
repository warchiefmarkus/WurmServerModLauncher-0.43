/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAnnotation;
/*    */ import com.sun.xml.xsom.XSComponent;
/*    */ import com.sun.xml.xsom.XSSchema;
/*    */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*    */ import com.sun.xml.xsom.impl.SchemaImpl;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ComponentImpl
/*    */   implements XSComponent
/*    */ {
/*    */   protected final SchemaImpl ownerSchema;
/*    */   private final AnnotationImpl annotation;
/*    */   private final Locator locator;
/*    */   
/*    */   protected ComponentImpl(SchemaImpl _owner, AnnotationImpl _annon, Locator _loc) {
/* 21 */     this.ownerSchema = _owner;
/* 22 */     this.annotation = _annon;
/* 23 */     this.locator = _loc;
/*    */   }
/*    */   
/*    */   public final XSSchema getOwnerSchema() {
/* 27 */     return (XSSchema)this.ownerSchema;
/*    */   }
/*    */   public final XSAnnotation getAnnotation() {
/* 30 */     return (XSAnnotation)this.annotation;
/*    */   }
/*    */   public final Locator getLocator() {
/* 33 */     return this.locator;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\ComponentImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */