/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.xml.bind.JAXBAssertionError;
/*    */ import com.sun.xml.xsom.XSAnnotation;
/*    */ import com.sun.xml.xsom.XSFacet;
/*    */ import com.sun.xml.xsom.XSNotation;
/*    */ import com.sun.xml.xsom.XSSchema;
/*    */ import com.sun.xml.xsom.visitor.XSFunction;
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
/*    */ 
/*    */ public abstract class AbstractXSFunctionImpl
/*    */   implements XSFunction
/*    */ {
/*    */   public Object annotation(XSAnnotation ann) {
/* 24 */     _assert(false);
/* 25 */     return null;
/*    */   }
/*    */   public Object schema(XSSchema schema) {
/* 28 */     _assert(false);
/* 29 */     return null;
/*    */   }
/*    */   
/*    */   public Object facet(XSFacet facet) {
/* 33 */     _assert(false);
/* 34 */     return null;
/*    */   }
/*    */   
/*    */   public Object notation(XSNotation not) {
/* 38 */     _assert(false);
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected static void _assert(boolean b) {
/* 44 */     if (!b)
/* 45 */       throw new JAXBAssertionError(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\AbstractXSFunctionImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */