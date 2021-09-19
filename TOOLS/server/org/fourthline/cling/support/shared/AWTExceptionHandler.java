/*    */ package org.fourthline.cling.support.shared;
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
/*    */ public class AWTExceptionHandler
/*    */ {
/*    */   public void handle(Throwable ex) {
/* 24 */     System.err.println("============= The application encountered an unrecoverable error, exiting... =============");
/* 25 */     ex.printStackTrace(System.err);
/* 26 */     System.err.println("==========================================================================================");
/* 27 */     System.exit(1);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\AWTExceptionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */