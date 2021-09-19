/*    */ package 1.0.com.sun.relaxng.javadt;
/*    */ 
/*    */ import org.relaxng.datatype.Datatype;
/*    */ import org.relaxng.datatype.DatatypeException;
/*    */ import org.relaxng.datatype.DatatypeStreamingValidator;
/*    */ import org.relaxng.datatype.ValidationContext;
/*    */ import org.relaxng.datatype.helpers.StreamingValidatorImpl;
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
/*    */ public abstract class AbstractDatatypeImpl
/*    */   implements Datatype
/*    */ {
/*    */   public void checkValid(String name, ValidationContext context) throws DatatypeException {
/* 31 */     if (isValid(name, context)) {
/* 32 */       throw new DatatypeException();
/*    */     }
/*    */   }
/*    */   
/*    */   public DatatypeStreamingValidator createStreamingValidator(ValidationContext context) {
/* 37 */     return (DatatypeStreamingValidator)new StreamingValidatorImpl(this, context);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object createValue(String text, ValidationContext context) {
/* 43 */     if (!isValid(text, context)) {
/* 44 */       return null;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     return text.trim();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean sameValue(Object obj1, Object obj2) {
/* 65 */     return obj1.equals(obj2);
/*    */   }
/*    */   
/*    */   public int valueHashCode(Object obj) {
/* 69 */     return obj.hashCode();
/*    */   }
/*    */   
/*    */   public int getIdType() {
/* 73 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean isContextDependent() {
/* 77 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\relaxng\javadt\AbstractDatatypeImpl.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */