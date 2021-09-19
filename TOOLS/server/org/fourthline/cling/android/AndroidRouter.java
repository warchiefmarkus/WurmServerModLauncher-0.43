/*     */ package org.fourthline.cling.android;
/*     */ 
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.net.NetworkInfo;
/*     */ import android.net.wifi.WifiManager;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpServiceConfiguration;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ import org.fourthline.cling.transport.RouterImpl;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.seamless.util.Exceptions;
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
/*     */ public class AndroidRouter
/*     */   extends RouterImpl
/*     */ {
/*  45 */   private static final Logger log = Logger.getLogger(Router.class.getName());
/*     */   
/*     */   private final Context context;
/*     */   
/*     */   private final WifiManager wifiManager;
/*     */   
/*     */   protected WifiManager.MulticastLock multicastLock;
/*     */   
/*     */   protected WifiManager.WifiLock wifiLock;
/*     */   protected NetworkInfo networkInfo;
/*     */   protected BroadcastReceiver broadcastReceiver;
/*     */   
/*     */   public AndroidRouter(UpnpServiceConfiguration configuration, ProtocolFactory protocolFactory, Context context) throws InitializationException {
/*  58 */     super(configuration, protocolFactory);
/*     */     
/*  60 */     this.context = context;
/*  61 */     this.wifiManager = (WifiManager)context.getSystemService("wifi");
/*  62 */     this.networkInfo = NetworkUtils.getConnectedNetworkInfo(context);
/*     */ 
/*     */     
/*  65 */     if (!ModelUtil.ANDROID_EMULATOR) {
/*  66 */       this.broadcastReceiver = createConnectivityBroadcastReceiver();
/*  67 */       context.registerReceiver(this.broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BroadcastReceiver createConnectivityBroadcastReceiver() {
/*  72 */     return new ConnectivityBroadcastReceiver();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLockTimeoutMillis() {
/*  77 */     return 15000;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws RouterException {
/*  82 */     super.shutdown();
/*  83 */     unregisterBroadcastReceiver();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enable() throws RouterException {
/*  88 */     lock(this.writeLock);
/*     */     try {
/*     */       boolean enabled;
/*  91 */       if (enabled = super.enable())
/*     */       {
/*     */         
/*  94 */         if (isWifi()) {
/*  95 */           setWiFiMulticastLock(true);
/*  96 */           setWifiLock(true);
/*     */         } 
/*     */       }
/*  99 */       return enabled;
/*     */     } finally {
/* 101 */       unlock(this.writeLock);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disable() throws RouterException {
/* 107 */     lock(this.writeLock);
/*     */ 
/*     */     
/*     */     try {
/* 111 */       if (isWifi()) {
/* 112 */         setWiFiMulticastLock(false);
/* 113 */         setWifiLock(false);
/*     */       } 
/* 115 */       return super.disable();
/*     */     } finally {
/* 117 */       unlock(this.writeLock);
/*     */     } 
/*     */   }
/*     */   
/*     */   public NetworkInfo getNetworkInfo() {
/* 122 */     return this.networkInfo;
/*     */   }
/*     */   
/*     */   public boolean isMobile() {
/* 126 */     return NetworkUtils.isMobile(this.networkInfo);
/*     */   }
/*     */   
/*     */   public boolean isWifi() {
/* 130 */     return NetworkUtils.isWifi(this.networkInfo);
/*     */   }
/*     */   
/*     */   public boolean isEthernet() {
/* 134 */     return NetworkUtils.isEthernet(this.networkInfo);
/*     */   }
/*     */   
/*     */   public boolean enableWiFi() {
/* 138 */     log.info("Enabling WiFi...");
/*     */     try {
/* 140 */       return this.wifiManager.setWifiEnabled(true);
/* 141 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       log.log(Level.WARNING, "SetWifiEnabled failed", t);
/* 150 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unregisterBroadcastReceiver() {
/* 155 */     if (this.broadcastReceiver != null) {
/* 156 */       this.context.unregisterReceiver(this.broadcastReceiver);
/* 157 */       this.broadcastReceiver = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setWiFiMulticastLock(boolean enable) {
/* 162 */     if (this.multicastLock == null) {
/* 163 */       this.multicastLock = this.wifiManager.createMulticastLock(getClass().getSimpleName());
/*     */     }
/*     */     
/* 166 */     if (enable) {
/* 167 */       if (this.multicastLock.isHeld()) {
/* 168 */         log.warning("WiFi multicast lock already acquired");
/*     */       } else {
/* 170 */         log.info("WiFi multicast lock acquired");
/* 171 */         this.multicastLock.acquire();
/*     */       }
/*     */     
/* 174 */     } else if (this.multicastLock.isHeld()) {
/* 175 */       log.info("WiFi multicast lock released");
/* 176 */       this.multicastLock.release();
/*     */     } else {
/* 178 */       log.warning("WiFi multicast lock already released");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setWifiLock(boolean enable) {
/* 184 */     if (this.wifiLock == null) {
/* 185 */       this.wifiLock = this.wifiManager.createWifiLock(3, getClass().getSimpleName());
/*     */     }
/*     */     
/* 188 */     if (enable) {
/* 189 */       if (this.wifiLock.isHeld()) {
/* 190 */         log.warning("WiFi lock already acquired");
/*     */       } else {
/* 192 */         log.info("WiFi lock acquired");
/* 193 */         this.wifiLock.acquire();
/*     */       }
/*     */     
/* 196 */     } else if (this.wifiLock.isHeld()) {
/* 197 */       log.info("WiFi lock released");
/* 198 */       this.wifiLock.release();
/*     */     } else {
/* 200 */       log.warning("WiFi lock already released");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onNetworkTypeChange(NetworkInfo oldNetwork, NetworkInfo newNetwork) throws RouterException {
/* 211 */     log.info(String.format("Network type changed %s => %s", new Object[] { (oldNetwork == null) ? "" : oldNetwork
/* 212 */             .getTypeName(), (newNetwork == null) ? "NONE" : newNetwork
/* 213 */             .getTypeName() }));
/*     */     
/* 215 */     if (disable()) {
/* 216 */       log.info(String.format("Disabled router on network type change (old network: %s)", new Object[] { (oldNetwork == null) ? "NONE" : oldNetwork
/*     */               
/* 218 */               .getTypeName() }));
/*     */     }
/*     */ 
/*     */     
/* 222 */     this.networkInfo = newNetwork;
/* 223 */     if (enable())
/*     */     {
/*     */       
/* 226 */       log.info(String.format("Enabled router on network type change (new network: %s)", new Object[] { (newNetwork == null) ? "NONE" : newNetwork
/*     */               
/* 228 */               .getTypeName() }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleRouterExceptionOnNetworkTypeChange(RouterException ex) {
/* 239 */     Throwable cause = Exceptions.unwrap((Throwable)ex);
/* 240 */     if (cause instanceof InterruptedException) {
/* 241 */       log.log(Level.INFO, "Router was interrupted: " + ex, cause);
/*     */     } else {
/* 243 */       log.log(Level.WARNING, "Router error on network change: " + ex, (Throwable)ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   class ConnectivityBroadcastReceiver
/*     */     extends BroadcastReceiver
/*     */   {
/*     */     public void onReceive(Context context, Intent intent) {
/* 252 */       if (!intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
/*     */         return;
/*     */       }
/* 255 */       displayIntentInfo(intent);
/*     */       
/* 257 */       NetworkInfo newNetworkInfo = NetworkUtils.getConnectedNetworkInfo(context);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 265 */       if (AndroidRouter.this.networkInfo != null && newNetworkInfo == null) {
/* 266 */         for (int i = 1; i <= 3; i++) {
/*     */           try {
/* 268 */             Thread.sleep(1000L);
/* 269 */           } catch (InterruptedException e) {
/*     */             return;
/*     */           } 
/* 272 */           AndroidRouter.log.warning(String.format("%s => NONE network transition, waiting for new network... retry #%d", new Object[] { this.this$0.networkInfo
/*     */                   
/* 274 */                   .getTypeName(), Integer.valueOf(i) }));
/*     */           
/* 276 */           newNetworkInfo = NetworkUtils.getConnectedNetworkInfo(context);
/* 277 */           if (newNetworkInfo != null) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       }
/* 282 */       if (isSameNetworkType(AndroidRouter.this.networkInfo, newNetworkInfo)) {
/* 283 */         AndroidRouter.log.info("No actual network change... ignoring event!");
/*     */       } else {
/*     */         try {
/* 286 */           AndroidRouter.this.onNetworkTypeChange(AndroidRouter.this.networkInfo, newNetworkInfo);
/* 287 */         } catch (RouterException ex) {
/* 288 */           AndroidRouter.this.handleRouterExceptionOnNetworkTypeChange(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected boolean isSameNetworkType(NetworkInfo network1, NetworkInfo network2) {
/* 294 */       if (network1 == null && network2 == null)
/* 295 */         return true; 
/* 296 */       if (network1 == null || network2 == null)
/* 297 */         return false; 
/* 298 */       return (network1.getType() == network2.getType());
/*     */     }
/*     */     
/*     */     protected void displayIntentInfo(Intent intent) {
/* 302 */       boolean noConnectivity = intent.getBooleanExtra("noConnectivity", false);
/* 303 */       String reason = intent.getStringExtra("reason");
/* 304 */       boolean isFailover = intent.getBooleanExtra("isFailover", false);
/*     */       
/* 306 */       NetworkInfo currentNetworkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
/* 307 */       NetworkInfo otherNetworkInfo = (NetworkInfo)intent.getParcelableExtra("otherNetwork");
/*     */       
/* 309 */       AndroidRouter.log.info("Connectivity change detected...");
/* 310 */       AndroidRouter.log.info("EXTRA_NO_CONNECTIVITY: " + noConnectivity);
/* 311 */       AndroidRouter.log.info("EXTRA_REASON: " + reason);
/* 312 */       AndroidRouter.log.info("EXTRA_IS_FAILOVER: " + isFailover);
/* 313 */       AndroidRouter.log.info("EXTRA_NETWORK_INFO: " + ((currentNetworkInfo == null) ? "none" : (String)currentNetworkInfo));
/* 314 */       AndroidRouter.log.info("EXTRA_OTHER_NETWORK_INFO: " + ((otherNetworkInfo == null) ? "none" : (String)otherNetworkInfo));
/* 315 */       AndroidRouter.log.info("EXTRA_EXTRA_INFO: " + intent.getStringExtra("extraInfo"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\android\AndroidRouter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */