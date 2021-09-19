/*    */ package 1.0.com.sun.xml.xsom.impl;
/*    */ 
/*    */ import com.sun.xml.xsom.XSAnnotation;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationImpl
/*    */   implements XSAnnotation
/*    */ {
/*    */   private final Object annotation;
/*    */   private final Locator locator;
/*    */   
/*    */   public Object getAnnotation() {
/* 19 */     return this.annotation;
/*    */   }
/*    */   public Locator getLocator() {
/* 22 */     return this.locator;
/*    */   }
/*    */   public AnnotationImpl(Object o, Locator _loc) {
/* 25 */     this.annotation = o;
/* 26 */     this.locator = _loc;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\AnnotationImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */