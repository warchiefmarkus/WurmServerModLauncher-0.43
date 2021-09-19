/*    */ package org.fourthline.cling.support.shared;
/*    */ 
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Proxy;
/*    */ import javax.swing.JFrame;
/*    */ import org.seamless.swing.Controller;
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
/*    */ public class PlatformApple
/*    */ {
/*    */   public static void setup(Controller<JFrame> appController, String appName) throws Exception {
/* 32 */     System.setProperty("apple.laf.useScreenMenuBar", "true");
/* 33 */     System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
/* 34 */     System.setProperty("apple.awt.showGrowBox", "true");
/*    */ 
/*    */     
/* 37 */     Class<?> appClass = Class.forName("com.apple.eawt.Application");
/* 38 */     Object application = appClass.newInstance();
/* 39 */     Class<?> listenerClass = Class.forName("com.apple.eawt.ApplicationListener");
/* 40 */     Method addAppListmethod = appClass.getDeclaredMethod("addApplicationListener", new Class[] { listenerClass });
/*    */ 
/*    */     
/* 43 */     Class<?> adapterClass = Class.forName("com.apple.eawt.ApplicationAdapter");
/* 44 */     Object listener = AppListenerProxy.newInstance(adapterClass.newInstance(), appController);
/* 45 */     addAppListmethod.invoke(application, new Object[] { listener });
/*    */   }
/*    */   
/*    */   static class AppListenerProxy
/*    */     implements InvocationHandler {
/*    */     private Controller<JFrame> appController;
/*    */     private Object object;
/*    */     
/*    */     public static Object newInstance(Object obj, Controller<JFrame> appController) {
/* 54 */       return Proxy.newProxyInstance(obj
/* 55 */           .getClass().getClassLoader(), obj
/* 56 */           .getClass().getInterfaces(), new AppListenerProxy(obj, appController));
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     private AppListenerProxy(Object obj, Controller<JFrame> appController) {
/* 62 */       this.object = obj;
/* 63 */       this.appController = appController;
/*    */     }
/*    */     
/*    */     public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
/* 67 */       Object result = null;
/*    */       try {
/* 69 */         if ("handleQuit".equals(m.getName())) {
/* 70 */           if (this.appController != null) {
/* 71 */             this.appController.dispose();
/* 72 */             ((JFrame)this.appController.getView()).dispose();
/*    */           } 
/*    */         } else {
/* 75 */           result = m.invoke(this.object, args);
/*    */         } 
/* 77 */       } catch (Exception exception) {}
/*    */ 
/*    */       
/* 80 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\PlatformApple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */