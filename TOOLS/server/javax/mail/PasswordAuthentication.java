/*    */ package javax.mail;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PasswordAuthentication
/*    */ {
/*    */   private final String userName;
/*    */   private final String password;
/*    */   
/*    */   public PasswordAuthentication(String userName, String password) {
/* 66 */     this.userName = userName;
/* 67 */     this.password = password;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUserName() {
/* 74 */     return this.userName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPassword() {
/* 81 */     return this.password;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\mail\PasswordAuthentication.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */