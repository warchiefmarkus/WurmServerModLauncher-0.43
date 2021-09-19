/*     */ package winterwell.jtwitter.android;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.app.Dialog;
/*     */ import android.content.Context;
/*     */ import android.graphics.Bitmap;
/*     */ import android.net.Uri;
/*     */ import android.os.Handler;
/*     */ import android.util.Log;
/*     */ import android.view.MotionEvent;
/*     */ import android.view.View;
/*     */ import android.webkit.WebView;
/*     */ import android.webkit.WebViewClient;
/*     */ import android.widget.Toast;
/*     */ import java.net.URI;
/*     */ import winterwell.jtwitter.OAuthSignpostClient;
/*     */ import winterwell.jtwitter.Twitter;
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
/*     */ public abstract class AndroidTwitterLogin
/*     */ {
/*     */   private String callbackUrl;
/*     */   private Activity context;
/*     */   OAuthSignpostClient client;
/*     */   private String authoriseMessage;
/*     */   private String consumerSecret;
/*     */   private String consumerKey;
/*     */   
/*     */   public void setAuthoriseMessage(String authoriseMessage) {
/*  50 */     this.authoriseMessage = authoriseMessage;
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
/*     */   public AndroidTwitterLogin(Activity myActivity, String oauthAppKey, String oauthAppSecret, String calbackUrl) {
/*  71 */     this.authoriseMessage = "Please authorize with Twitter";
/*     */     this.context = myActivity;
/*     */     this.consumerKey = oauthAppKey;
/*     */     this.consumerSecret = oauthAppSecret;
/*     */     this.callbackUrl = calbackUrl;
/*     */     this.client = new OAuthSignpostClient(this.consumerKey, this.consumerSecret, this.callbackUrl);
/*     */   } public final void run() {
/*  78 */     Log.i("jtwitter", "TwitterAuth run!");
/*  79 */     final WebView webview = new WebView((Context)this.context);
/*  80 */     webview.setBackgroundColor(-16777216);
/*  81 */     webview.setVisibility(0);
/*  82 */     final Dialog dialog = new Dialog((Context)this.context, 16973834);
/*  83 */     dialog.setContentView((View)webview);
/*  84 */     dialog.show();
/*     */     
/*  86 */     webview.getSettings().setJavaScriptEnabled(true);
/*  87 */     webview.setWebViewClient(new WebViewClient()
/*     */         {
/*     */           public void onPageStarted(WebView view, String url, Bitmap favicon)
/*     */           {
/*  91 */             Log.d("jtwitter", "url: " + url);
/*  92 */             if (!url.contains(AndroidTwitterLogin.this.callbackUrl))
/*  93 */               return;  Uri uri = Uri.parse(url);
/*  94 */             String verifier = uri.getQueryParameter("oauth_verifier");
/*  95 */             if (verifier == null) {
/*     */               
/*  97 */               Log.i("jtwitter", "Auth-fail: " + url);
/*  98 */               dialog.dismiss();
/*  99 */               AndroidTwitterLogin.this.onFail(new Exception(url));
/*     */               return;
/*     */             } 
/* 102 */             AndroidTwitterLogin.this.client.setAuthorizationCode(verifier);
/* 103 */             String[] tokens = AndroidTwitterLogin.this.client.getAccessToken();
/* 104 */             Twitter jtwitter = new Twitter(null, (Twitter.IHttpClient)AndroidTwitterLogin.this.client);
/* 105 */             Log.i("jtwitter", "Authorised :)");
/* 106 */             dialog.dismiss();
/* 107 */             AndroidTwitterLogin.this.onSuccess(jtwitter, tokens);
/*     */           }
/*     */           
/*     */           public void onPageFinished(WebView view, String url) {
/* 111 */             Log.i("jtwitter", "url finished: " + url);
/*     */           }
/*     */         });
/*     */     
/* 115 */     webview.requestFocus(130);
/* 116 */     webview.setOnTouchListener(new View.OnTouchListener()
/*     */         {
/*     */           public boolean onTouch(View v, MotionEvent e)
/*     */           {
/* 120 */             if ((e.getAction() == 0 || 
/* 121 */               e.getAction() == 1) && 
/* 122 */               !v.hasFocus()) {
/* 123 */               v.requestFocus();
/*     */             }
/*     */             
/* 126 */             return false;
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 131 */     Toast.makeText((Context)this.context, this.authoriseMessage, 0).show();
/* 132 */     Handler handler = new Handler();
/* 133 */     handler.postDelayed(new Runnable()
/*     */         {
/*     */           public void run() {
/*     */             try {
/* 137 */               URI authUrl = AndroidTwitterLogin.this.client.authorizeUrl();
/* 138 */               webview.loadUrl(authUrl.toString());
/* 139 */             } catch (Exception e) {
/* 140 */               AndroidTwitterLogin.this.onFail(e);
/*     */             } 
/*     */           }
/* 143 */         },  10L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onFail(Exception e) {
/* 149 */     Toast.makeText((Context)this.context, "Twitter authorisation failed?!", 1).show();
/* 150 */     Log.w("jtwitter", e.toString());
/*     */   }
/*     */   
/*     */   protected abstract void onSuccess(Twitter paramTwitter, String[] paramArrayOfString);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\android\AndroidTwitterLogin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */