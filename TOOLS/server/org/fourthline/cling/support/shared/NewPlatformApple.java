/*    */ package org.fourthline.cling.support.shared;
/*    */ 
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Proxy;
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
/*    */ public class NewPlatformApple
/*    */ {
/*    */   public static void setup(ShutdownHandler shutdownHandler, String appName) throws Exception {
/* 29 */     System.setProperty("apple.laf.useScreenMenuBar", "true");
/* 30 */     System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
/* 31 */     System.setProperty("apple.awt.showGrowBox", "true");
/*    */ 
/*    */     
/* 34 */     Class<?> appClass = Class.forName("com.apple.eawt.Application");
/* 35 */     Object application = appClass.newInstance();
/* 36 */     Class<?> listenerClass = Class.forName("com.apple.eawt.ApplicationListener");
/* 37 */     Method addAppListmethod = appClass.getDeclaredMethod("addApplicationListener", new Class[] { listenerClass });
/*    */ 
/*    */     
/* 40 */     Class<?> adapterClass = Class.forName("com.apple.eawt.ApplicationAdapter");
/* 41 */     Object listener = AppListenerProxy.newInstance(adapterClass.newInstance(), shutdownHandler);
/* 42 */     addAppListmethod.invoke(application, new Object[] { listener });
/*    */   }
/*    */   
/*    */   static class AppListenerProxy
/*    */     implements InvocationHandler {
/*    */     private ShutdownHandler shutdownHandler;
/*    */     private Object object;
/*    */     
/*    */     public static Object newInstance(Object obj, ShutdownHandler shutdownHandler) {
/* 51 */       return Proxy.newProxyInstance(obj
/* 52 */           .getClass().getClassLoader(), obj
/* 53 */           .getClass().getInterfaces(), new AppListenerProxy(obj, shutdownHandler));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     private AppListenerProxy(Object obj, ShutdownHandler shutdownHandler) {
/* 59 */       this.object = obj;
/* 60 */       this.shutdownHandler = shutdownHandler;
/*    */     }
/*    */     
/*    */     public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
/* 64 */       Object result = null;
/*    */       try {
/* 66 */         if ("handleQuit".equals(m.getName())) {
/* 67 */           if (this.shutdownHandler != null) {
/* 68 */             this.shutdownHandler.shutdown();
/*    */           }
/*    */         } else {
/* 71 */           result = m.invoke(this.object, args);
/*    */         } 
/* 73 */       } catch (Exception exception) {}
/*    */ 
/*    */       
/* 76 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\NewPlatformApple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */