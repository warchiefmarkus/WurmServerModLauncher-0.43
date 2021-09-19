/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.si.DeploySIListener;
/*     */ import com.sun.deploy.si.SingleInstanceImpl;
/*     */ import com.sun.deploy.si.SingleInstanceManager;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.Main;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.XMLFormat;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.jnlp.SingleInstanceListener;
/*     */ import javax.jnlp.SingleInstanceService;
/*     */ 
/*     */ 
/*     */ public final class SingleInstanceServiceImpl
/*     */   extends SingleInstanceImpl
/*     */   implements SingleInstanceService
/*     */ {
/*  23 */   private static SingleInstanceServiceImpl _sharedInstance = null;
/*     */   
/*     */   public static synchronized SingleInstanceServiceImpl getInstance() {
/*  26 */     if (_sharedInstance == null) {
/*  27 */       _sharedInstance = new SingleInstanceServiceImpl();
/*     */     }
/*  29 */     return _sharedInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSingleInstanceListener(SingleInstanceListener paramSingleInstanceListener) {
/*  34 */     if (paramSingleInstanceListener == null) {
/*     */       return;
/*     */     }
/*  37 */     LaunchDesc launchDesc = JNLPClassLoader.getInstance().getLaunchDesc();
/*  38 */     URL uRL = launchDesc.getCanonicalHome();
/*  39 */     String str = uRL.toString();
/*     */     
/*  41 */     AccessController.doPrivileged(new PrivilegedAction(this, str, launchDesc) { private final String val$jnlpUrlString;
/*     */           public Object run() {
/*  43 */             if (SingleInstanceManager.isServerRunning(this.val$jnlpUrlString)) {
/*     */ 
/*     */               
/*  46 */               String[] arrayOfString = Globals.getApplicationArgs();
/*     */               
/*  48 */               if (arrayOfString != null) {
/*  49 */                 this.val$ld.getApplicationDescriptor().setArguments(arrayOfString);
/*     */               }
/*     */               
/*  52 */               if (SingleInstanceManager.connectToServer(this.val$ld.toString()))
/*     */               {
/*     */ 
/*     */                 
/*  56 */                 Main.systemExit(0);
/*     */               }
/*     */             } 
/*     */ 
/*     */             
/*  61 */             return null;
/*     */           }
/*     */           private final LaunchDesc val$ld; private final SingleInstanceServiceImpl this$0; }
/*     */       );
/*  65 */     addSingleInstanceListener(new TransferListener(this, paramSingleInstanceListener), str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeSingleInstanceListener(SingleInstanceListener paramSingleInstanceListener) {
/*  70 */     removeSingleInstanceListener(new TransferListener(this, paramSingleInstanceListener));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSame(String paramString1, String paramString2) {
/*  76 */     LaunchDesc launchDesc = null;
/*     */     
/*     */     try {
/*  79 */       launchDesc = XMLFormat.parse(paramString1.getBytes());
/*  80 */     } catch (Exception exception) {
/*  81 */       Trace.ignoredException(exception);
/*     */     } 
/*     */     
/*  84 */     if (launchDesc != null) {
/*  85 */       URL uRL = launchDesc.getCanonicalHome();
/*  86 */       Trace.println("GOT: " + uRL.toString(), TraceLevel.BASIC);
/*     */       
/*  88 */       if (paramString2.equals(uRL.toString())) {
/*  89 */         return true;
/*     */       }
/*     */     } 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getArguments(String paramString1, String paramString2) {
/*  98 */     LaunchDesc launchDesc = null;
/*     */     
/*     */     try {
/* 101 */       launchDesc = XMLFormat.parse(paramString1.getBytes());
/* 102 */     } catch (Exception exception) {
/* 103 */       Trace.ignoredException(exception);
/*     */     } 
/*     */     
/* 106 */     if (launchDesc != null) {
/* 107 */       return launchDesc.getApplicationDescriptor().getArguments();
/*     */     }
/* 109 */     return new String[0];
/*     */   }
/*     */   private class TransferListener implements DeploySIListener { SingleInstanceListener _sil;
/*     */     private final SingleInstanceServiceImpl this$0;
/*     */     
/*     */     public TransferListener(SingleInstanceServiceImpl this$0, SingleInstanceListener param1SingleInstanceListener) {
/* 115 */       this.this$0 = this$0;
/* 116 */       this._sil = param1SingleInstanceListener;
/*     */     }
/*     */     
/*     */     public void newActivation(String[] param1ArrayOfString) {
/* 120 */       this._sil.newActivation(param1ArrayOfString);
/*     */     }
/*     */     
/*     */     public Object getSingleInstanceListener() {
/* 124 */       return this._sil;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\SingleInstanceServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */