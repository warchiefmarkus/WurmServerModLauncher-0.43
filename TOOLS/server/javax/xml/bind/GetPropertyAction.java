/*    */ package javax.xml.bind;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ final class GetPropertyAction
/*    */   implements PrivilegedAction<String>
/*    */ {
/*    */   private final String propertyName;
/*    */   
/*    */   public GetPropertyAction(String propertyName) {
/* 13 */     this.propertyName = propertyName;
/*    */   }
/*    */   
/*    */   public String run() {
/* 17 */     return System.getProperty(this.propertyName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\GetPropertyAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */