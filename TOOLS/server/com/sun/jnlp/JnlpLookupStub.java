/*    */ package com.sun.jnlp;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import javax.jnlp.ServiceManagerStub;
/*    */ import javax.jnlp.UnavailableServiceException;
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
/*    */ public final class JnlpLookupStub
/*    */   implements ServiceManagerStub
/*    */ {
/*    */   public Object lookup(String paramString) throws UnavailableServiceException {
/* 22 */     Object object = AccessController.doPrivileged(new PrivilegedAction(this, paramString) { private final String val$name;
/*    */           
/*    */           public Object run() {
/* 25 */             return this.this$0.findService(this.val$name);
/*    */           }
/*    */           private final JnlpLookupStub this$0; }
/*    */       );
/* 29 */     if (object == null) throw new UnavailableServiceException(paramString);
/*    */     
/* 31 */     return object;
/*    */   }
/*    */ 
/*    */   
/*    */   private Object findService(String paramString) {
/* 36 */     if (paramString != null) {
/* 37 */       if (paramString.equals("javax.jnlp.BasicService"))
/*    */       {
/* 39 */         return BasicServiceImpl.getInstance(); } 
/* 40 */       if (paramString.equals("javax.jnlp.FileOpenService"))
/*    */       {
/* 42 */         return FileOpenServiceImpl.getInstance(); } 
/* 43 */       if (paramString.equals("javax.jnlp.FileSaveService"))
/*    */       {
/* 45 */         return FileSaveServiceImpl.getInstance(); } 
/* 46 */       if (paramString.equals("javax.jnlp.ExtensionInstallerService"))
/*    */       {
/*    */         
/* 49 */         return ExtensionInstallerServiceImpl.getInstance(); } 
/* 50 */       if (paramString.equals("javax.jnlp.DownloadService"))
/*    */       {
/* 52 */         return DownloadServiceImpl.getInstance(); } 
/* 53 */       if (paramString.equals("javax.jnlp.ClipboardService"))
/*    */       {
/* 55 */         return ClipboardServiceImpl.getInstance(); } 
/* 56 */       if (paramString.equals("javax.jnlp.PrintService"))
/*    */       {
/* 58 */         return PrintServiceImpl.getInstance(); } 
/* 59 */       if (paramString.equals("javax.jnlp.PersistenceService"))
/*    */       {
/* 61 */         return PersistenceServiceImpl.getInstance(); } 
/* 62 */       if (paramString.equals("javax.jnlp.ExtendedService"))
/*    */       {
/* 64 */         return ExtendedServiceImpl.getInstance(); } 
/* 65 */       if (paramString.equals("javax.jnlp.SingleInstanceService"))
/*    */       {
/* 67 */         return SingleInstanceServiceImpl.getInstance();
/*    */       }
/*    */     } 
/*    */     
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getServiceNames() {
/* 76 */     if (ExtensionInstallerServiceImpl.getInstance() != null) {
/* 77 */       return new String[] { "javax.jnlp.BasicService", "javax.jnlp.FileOpenService", "javax.jnlp.FileSaveService", "javax.jnlp.ExtensionInstallerService", "javax.jnlp.DownloadService", "javax.jnlp.ClipboardService", "javax.jnlp.PersistenceService", "javax.jnlp.PrintService", "javax.jnlp.ExtendedService", "javax.jnlp.SingleInstanceService" };
/*    */     }
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
/* 90 */     return new String[] { "javax.jnlp.BasicService", "javax.jnlp.FileOpenService", "javax.jnlp.FileSaveService", "javax.jnlp.DownloadService", "javax.jnlp.ClipboardService", "javax.jnlp.PersistenceService", "javax.jnlp.PrintService", "javax.jnlp.ExtendedService", "javax.jnlp.SingleInstanceService" };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\JnlpLookupStub.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */