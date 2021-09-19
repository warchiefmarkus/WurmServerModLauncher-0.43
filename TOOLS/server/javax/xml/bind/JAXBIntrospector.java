/*    */ package javax.xml.bind;
/*    */ 
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class JAXBIntrospector
/*    */ {
/*    */   public abstract boolean isElement(Object paramObject);
/*    */   
/*    */   public abstract QName getElementName(Object paramObject);
/*    */   
/*    */   public static Object getValue(Object jaxbElement) {
/* 64 */     if (jaxbElement instanceof JAXBElement) {
/* 65 */       return ((JAXBElement)jaxbElement).getValue();
/*    */     }
/*    */ 
/*    */     
/* 69 */     return jaxbElement;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\JAXBIntrospector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */