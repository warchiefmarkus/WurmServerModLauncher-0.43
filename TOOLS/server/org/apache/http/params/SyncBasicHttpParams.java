/*    */ package org.apache.http.params;
/*    */ 
/*    */ import org.apache.http.annotation.ThreadSafe;
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
/*    */ @ThreadSafe
/*    */ public class SyncBasicHttpParams
/*    */   extends BasicHttpParams
/*    */ {
/*    */   private static final long serialVersionUID = 5387834869062660642L;
/*    */   
/*    */   public synchronized boolean removeParameter(String name) {
/* 47 */     return super.removeParameter(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized HttpParams setParameter(String name, Object value) {
/* 52 */     return super.setParameter(name, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Object getParameter(String name) {
/* 57 */     return super.getParameter(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean isParameterSet(String name) {
/* 62 */     return super.isParameterSet(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean isParameterSetLocally(String name) {
/* 67 */     return super.isParameterSetLocally(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void setParameters(String[] names, Object value) {
/* 72 */     super.setParameters(names, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void clear() {
/* 77 */     super.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Object clone() throws CloneNotSupportedException {
/* 82 */     return super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\params\SyncBasicHttpParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */