/*    */ package 1.0.com.sun.xml.xsom.impl.util;
/*    */ 
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   private final Class base;
/*    */   
/*    */   public ResourceEntityResolver(Class _base) {
/* 17 */     this.base = _base;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) {
/* 23 */     return new InputSource(this.base.getResourceAsStream(systemId));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\imp\\util\ResourceEntityResolver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */