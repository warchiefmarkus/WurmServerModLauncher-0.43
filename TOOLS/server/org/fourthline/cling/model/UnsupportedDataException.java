/*    */ package org.fourthline.cling.model;
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
/*    */ public class UnsupportedDataException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 661795454401413339L;
/*    */   protected Object data;
/*    */   
/*    */   public UnsupportedDataException(String s) {
/* 35 */     super(s);
/*    */   }
/*    */   
/*    */   public UnsupportedDataException(String s, Throwable throwable) {
/* 39 */     super(s, throwable);
/*    */   }
/*    */   
/*    */   public UnsupportedDataException(String s, Throwable throwable, Object data) {
/* 43 */     super(s, throwable);
/* 44 */     this.data = data;
/*    */   }
/*    */   
/*    */   public Object getData() {
/* 48 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\UnsupportedDataException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */