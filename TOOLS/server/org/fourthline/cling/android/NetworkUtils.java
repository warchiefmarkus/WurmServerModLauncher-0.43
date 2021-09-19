/*    */ package org.fourthline.cling.android;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.net.ConnectivityManager;
/*    */ import android.net.NetworkInfo;
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.model.ModelUtil;
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
/*    */ public class NetworkUtils
/*    */ {
/* 32 */   private static final Logger log = Logger.getLogger(NetworkUtils.class.getName());
/*    */ 
/*    */   
/*    */   public static NetworkInfo getConnectedNetworkInfo(Context context) {
/* 36 */     ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
/*    */     
/* 38 */     NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
/* 39 */     if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
/* 40 */       return networkInfo;
/*    */     }
/*    */     
/* 43 */     networkInfo = connectivityManager.getNetworkInfo(1);
/* 44 */     if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) return networkInfo;
/*    */     
/* 46 */     networkInfo = connectivityManager.getNetworkInfo(0);
/* 47 */     if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) return networkInfo;
/*    */     
/* 49 */     networkInfo = connectivityManager.getNetworkInfo(6);
/* 50 */     if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) return networkInfo;
/*    */     
/* 52 */     networkInfo = connectivityManager.getNetworkInfo(9);
/* 53 */     if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) return networkInfo;
/*    */     
/* 55 */     log.info("Could not find any connected network...");
/*    */     
/* 57 */     return null;
/*    */   }
/*    */   
/*    */   public static boolean isEthernet(NetworkInfo networkInfo) {
/* 61 */     return isNetworkType(networkInfo, 9);
/*    */   }
/*    */   
/*    */   public static boolean isWifi(NetworkInfo networkInfo) {
/* 65 */     return (isNetworkType(networkInfo, 1) || ModelUtil.ANDROID_EMULATOR);
/*    */   }
/*    */   
/*    */   public static boolean isMobile(NetworkInfo networkInfo) {
/* 69 */     return (isNetworkType(networkInfo, 0) || isNetworkType(networkInfo, 6));
/*    */   }
/*    */   
/*    */   public static boolean isNetworkType(NetworkInfo networkInfo, int type) {
/* 73 */     return (networkInfo != null && networkInfo.getType() == type);
/*    */   }
/*    */   
/*    */   public static boolean isSSDPAwareNetwork(NetworkInfo networkInfo) {
/* 77 */     return (isWifi(networkInfo) || isEthernet(networkInfo));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\android\NetworkUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */