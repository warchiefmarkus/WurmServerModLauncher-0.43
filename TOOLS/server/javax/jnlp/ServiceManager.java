/*    */ package javax.jnlp;
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
/*    */ public final class ServiceManager
/*    */ {
/* 23 */   private static ServiceManagerStub _stub = null;
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
/*    */   public static Object lookup(String paramString) throws UnavailableServiceException {
/* 41 */     if (_stub != null) {
/* 42 */       return _stub.lookup(paramString);
/*    */     }
/* 44 */     throw new UnavailableServiceException("uninitialized");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String[] getServiceNames() {
/* 52 */     if (_stub != null) {
/* 53 */       return _stub.getServiceNames();
/*    */     }
/* 55 */     return null;
/*    */   }
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
/*    */   public static synchronized void setServiceManagerStub(ServiceManagerStub paramServiceManagerStub) {
/* 70 */     if (_stub == null)
/* 71 */       _stub = paramServiceManagerStub; 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\ServiceManager.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */