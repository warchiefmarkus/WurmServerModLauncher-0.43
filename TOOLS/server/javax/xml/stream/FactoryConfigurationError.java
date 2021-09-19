/*    */ package javax.xml.stream;
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
/*    */ public class FactoryConfigurationError
/*    */   extends Error
/*    */ {
/*    */   Exception nested;
/*    */   
/*    */   public FactoryConfigurationError() {}
/*    */   
/*    */   public FactoryConfigurationError(Exception e) {
/* 24 */     this.nested = e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FactoryConfigurationError(Exception e, String msg) {
/* 35 */     super(msg);
/* 36 */     this.nested = e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FactoryConfigurationError(String msg, Exception e) {
/* 47 */     super(msg);
/* 48 */     this.nested = e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FactoryConfigurationError(String msg) {
/* 58 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Exception getException() {
/* 67 */     return this.nested;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 77 */     String msg = super.getMessage();
/* 78 */     if (msg != null)
/* 79 */       return msg; 
/* 80 */     if (this.nested != null) {
/* 81 */       msg = this.nested.getMessage();
/* 82 */       if (msg == null)
/* 83 */         msg = this.nested.getClass().toString(); 
/*    */     } 
/* 85 */     return msg;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\FactoryConfigurationError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */