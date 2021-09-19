/*    */ package com.sun.jnlp;
/*    */ 
/*    */ import com.sun.javaws.BrowserSupport;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import javax.jnlp.BasicService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BasicServiceImpl
/*    */   implements BasicService
/*    */ {
/* 18 */   private URL _codebase = null;
/*    */   
/*    */   private boolean _isWebBrowserSupported;
/*    */   
/*    */   private boolean _isOffline;
/* 23 */   private static BasicServiceImpl _sharedInstance = null;
/*    */ 
/*    */   
/*    */   private BasicServiceImpl(URL paramURL, boolean paramBoolean1, boolean paramBoolean2) {
/* 27 */     this._codebase = paramURL;
/* 28 */     this._isWebBrowserSupported = paramBoolean2;
/* 29 */     this._isOffline = paramBoolean1;
/*    */   }
/*    */   
/*    */   public static BasicServiceImpl getInstance() {
/* 33 */     return _sharedInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void initialize(URL paramURL, boolean paramBoolean1, boolean paramBoolean2) {
/* 40 */     if (_sharedInstance == null) {
/* 41 */       _sharedInstance = new BasicServiceImpl(paramURL, paramBoolean1, paramBoolean2);
/*    */     }
/*    */   }
/*    */   
/*    */   public URL getCodeBase() {
/* 46 */     return this._codebase;
/*    */   }
/*    */   public boolean isOffline() {
/* 49 */     return this._isOffline;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean showDocument(URL paramURL) {
/* 59 */     if (!isWebBrowserSupported()) return false;
/*    */ 
/*    */ 
/*    */     
/* 63 */     Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this, paramURL) { private final URL val$url;
/*    */           
/*    */           public Object run() {
/* 66 */             URL uRL = this.val$url; try {
/* 67 */               uRL = new URL(this.this$0._codebase, this.val$url.toString());
/* 68 */             } catch (MalformedURLException malformedURLException) {}
/* 69 */             return new Boolean(BrowserSupport.showDocument(uRL));
/*    */           } private final BasicServiceImpl this$0; }
/*    */       );
/* 72 */     return (bool == null) ? false : bool.booleanValue();
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
/*    */   public boolean isWebBrowserSupported() {
/* 84 */     return this._isWebBrowserSupported;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\BasicServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */