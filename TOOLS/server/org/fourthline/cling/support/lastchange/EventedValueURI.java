/*    */ package org.fourthline.cling.support.lastchange;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.Map;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.model.types.Datatype;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.seamless.util.Exceptions;
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
/*    */ public class EventedValueURI
/*    */   extends EventedValue<URI>
/*    */ {
/* 31 */   private static final Logger log = Logger.getLogger(EventedValueURI.class.getName());
/*    */   
/*    */   public EventedValueURI(URI value) {
/* 34 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueURI(Map.Entry<String, String>[] attributes) {
/* 38 */     super(attributes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected URI valueOf(String s) throws InvalidValueException {
/*    */     try {
/* 46 */       return super.valueOf(s);
/* 47 */     } catch (InvalidValueException ex) {
/* 48 */       log.info("Ignoring invalid URI in evented value '" + s + "': " + Exceptions.unwrap((Throwable)ex));
/* 49 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 55 */     return Datatype.Builtin.URI.getDatatype();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\EventedValueURI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */