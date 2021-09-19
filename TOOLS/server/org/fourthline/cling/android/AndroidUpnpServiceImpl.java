/*     */ package org.fourthline.cling.android;
/*     */ 
/*     */ import android.app.Service;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.os.IBinder;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.UpnpServiceConfiguration;
/*     */ import org.fourthline.cling.UpnpServiceImpl;
/*     */ import org.fourthline.cling.controlpoint.ControlPoint;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.registry.Registry;
/*     */ import org.fourthline.cling.registry.RegistryListener;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AndroidUpnpServiceImpl
/*     */   extends Service
/*     */ {
/*     */   protected UpnpService upnpService;
/*  45 */   protected Binder binder = new Binder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCreate() {
/*  52 */     super.onCreate();
/*     */     
/*  54 */     this.upnpService = (UpnpService)new UpnpServiceImpl(createConfiguration(), new RegistryListener[0])
/*     */       {
/*     */         protected Router createRouter(ProtocolFactory protocolFactory, Registry registry)
/*     */         {
/*  58 */           return (Router)AndroidUpnpServiceImpl.this.createRouter(
/*  59 */               getConfiguration(), protocolFactory, (Context)AndroidUpnpServiceImpl.this);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public synchronized void shutdown() {
/*  69 */           ((AndroidRouter)getRouter()).unregisterBroadcastReceiver();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  74 */           shutdown(true);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   protected UpnpServiceConfiguration createConfiguration() {
/*  80 */     return (UpnpServiceConfiguration)new AndroidUpnpServiceConfiguration();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AndroidRouter createRouter(UpnpServiceConfiguration configuration, ProtocolFactory protocolFactory, Context context) {
/*  86 */     return new AndroidRouter(configuration, protocolFactory, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public IBinder onBind(Intent intent) {
/*  91 */     return (IBinder)this.binder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDestroy() {
/*  99 */     this.upnpService.shutdown();
/* 100 */     super.onDestroy();
/*     */   }
/*     */   
/*     */   protected class Binder
/*     */     extends android.os.Binder implements AndroidUpnpService {
/*     */     public UpnpService get() {
/* 106 */       return AndroidUpnpServiceImpl.this.upnpService;
/*     */     }
/*     */     
/*     */     public UpnpServiceConfiguration getConfiguration() {
/* 110 */       return AndroidUpnpServiceImpl.this.upnpService.getConfiguration();
/*     */     }
/*     */     
/*     */     public Registry getRegistry() {
/* 114 */       return AndroidUpnpServiceImpl.this.upnpService.getRegistry();
/*     */     }
/*     */     
/*     */     public ControlPoint getControlPoint() {
/* 118 */       return AndroidUpnpServiceImpl.this.upnpService.getControlPoint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\android\AndroidUpnpServiceImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */