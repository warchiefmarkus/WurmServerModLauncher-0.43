/*    */ package winterwell.json;
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
/*    */ public class JSONException
/*    */   extends RuntimeException
/*    */ {
/*    */   private Throwable cause;
/*    */   
/*    */   public JSONException(String message) {
/* 18 */     super(message);
/*    */   }
/*    */   
/*    */   public JSONException(Throwable t) {
/* 22 */     super(t.getMessage());
/* 23 */     this.cause = t;
/*    */   }
/*    */ 
/*    */   
/*    */   public Throwable getCause() {
/* 28 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\json\JSONException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */