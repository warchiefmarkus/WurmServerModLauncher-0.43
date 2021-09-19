/*     */ package SteamJni;
/*     */ 
/*     */ import com.wurmonline.server.steam.SteamHandler;
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
/*     */ public class SteamServerApi
/*     */ {
/*     */   private final SteamHandler steamHandler;
/*     */   public final int eServerModeInvalid = 0;
/*     */   public final int eServerModeNoAuthentication = 1;
/*     */   public final int eServerModeAuthentication = 2;
/*     */   public final int eServerModeAuthenticationAndSecure = 3;
/*     */   public static final int beginAuthSessionResultOk = 0;
/*     */   public static final int beginAuthSessionResultDuplicateResult = 2;
/*     */   public static final boolean USE_GS_AUTH_API = true;
/*     */   
/*     */   static {
/*  26 */     System.loadLibrary("SteamServerJni");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SteamServerApi(SteamHandler inSteamHandler) {
/*  37 */     this.eServerModeInvalid = 0;
/*  38 */     this.eServerModeNoAuthentication = 1;
/*  39 */     this.eServerModeAuthentication = 2;
/*  40 */     this.eServerModeAuthenticationAndSecure = 3;
/*     */     this.steamHandler = inSteamHandler;
/*     */   }
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
/*     */   public void OnSteamServersConnected() {
/* 226 */     this.steamHandler.onSteamConnected();
/*     */   }
/*     */ 
/*     */   
/*     */   public void OnValidateAuthTicketResponse(String steamIdString, boolean wasSucces) {
/* 231 */     this.steamHandler.onValidateAuthTicketResponse(steamIdString, wasSucces);
/*     */   }
/*     */   
/*     */   public native void CreateCallback();
/*     */   
/*     */   public native void DeleteCallback();
/*     */   
/*     */   public native void SteamGameServer_RunCallbacks();
/*     */   
/*     */   public native boolean SteamGameServer_Init(long paramLong, short paramShort1, short paramShort2, short paramShort3, int paramInt, String paramString);
/*     */   
/*     */   public native void SetModDir(String paramString);
/*     */   
/*     */   public native void SetDedicatedServer(boolean paramBoolean);
/*     */   
/*     */   public native void SetProduct(String paramString);
/*     */   
/*     */   public native void SetGameDescription(String paramString);
/*     */   
/*     */   public native void SetGameTags(String paramString);
/*     */   
/*     */   public native void LogOnAnonymous();
/*     */   
/*     */   public native void EnableHeartbeats(boolean paramBoolean);
/*     */   
/*     */   public native void LogOff();
/*     */   
/*     */   public native void SteamGameServer_Shutdown();
/*     */   
/*     */   public native void setMaxPlayerCount(int paramInt);
/*     */   
/*     */   public native void SetPasswordProtected(boolean paramBoolean);
/*     */   
/*     */   public native void SetServerName(String paramString);
/*     */   
/*     */   public native void SetBotCount(int paramInt);
/*     */   
/*     */   public native void SetMapName(String paramString);
/*     */   
/*     */   public native void SetUserAchievement(int paramInt, String paramString);
/*     */   
/*     */   public native void GetUserAchievement(int paramInt, String paramString);
/*     */   
/*     */   public native void StoreUserStats(int paramInt);
/*     */   
/*     */   public native int BeginAuthSession(String paramString, byte[] paramArrayOfbyte, long paramLong);
/*     */   
/*     */   public native void EndAuthSession(String paramString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\SteamJni\SteamServerApi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */