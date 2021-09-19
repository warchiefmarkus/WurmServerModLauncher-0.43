/*     */ package winterwell.jtwitter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.ParseException;
/*     */ import winterwell.json.JSONException;
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
/*     */ public class TwitterException
/*     */   extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static class AccessLevel
/*     */     extends E401
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public AccessLevel(String msg) {
/*  34 */       super(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BadParameter
/*     */     extends E403
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     public BadParameter(String msg) {
/*  49 */       super(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E401
/*     */     extends E40X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public E401(String string) {
/*  62 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E403
/*     */     extends E40X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E403(String string) {
/*  80 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E404
/*     */     extends E40X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public E404(String string) {
/*  93 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E406
/*     */     extends E40X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E406(String string) {
/* 111 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E40X
/*     */     extends TwitterException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     public E40X(String string) {
/* 125 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E413
/*     */     extends E40X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E413(String string) {
/* 142 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E416
/*     */     extends E40X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     public E416(String string) {
/* 156 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class E50X
/*     */     extends TwitterException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E50X(String string) {
/* 173 */       super(msg(string));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     static String msg(String msg) {
/* 179 */       if (msg == null) return null;
/*     */ 
/*     */       
/* 182 */       msg = InternalUtils.TAG_REGEX.matcher(msg).replaceAll("");
/* 183 */       msg = msg.replaceAll("\\s+", " ");
/* 184 */       if (msg.length() > 280) msg = String.valueOf(msg.substring(0, 280)) + "..."; 
/* 185 */       return msg;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class FollowerLimit
/*     */     extends E403
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public FollowerLimit(String msg) {
/* 196 */       super(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class IO
/*     */     extends TwitterException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public IO(IOException e) {
/* 209 */       super(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public IOException getCause() {
/* 214 */       return (IOException)super.getCause();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Parsing
/*     */     extends TwitterException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static String clip(String json, int len) {
/* 230 */       return (json == null) ? null : ((json.length() <= len) ? json : (
/* 231 */         String.valueOf(json.substring(0, len)) + "..."));
/*     */     }
/*     */ 
/*     */     
/*     */     public Parsing(String json, JSONException e) {
/* 236 */       super(String.valueOf((json == null) ? String.valueOf(e) : clip(json, 280)) + causeLine(e), (Exception)e);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static String causeLine(JSONException e) {
/* 245 */       if (e == null) return ""; 
/* 246 */       StackTraceElement[] st = e.getStackTrace(); byte b; int i; StackTraceElement[] arrayOfStackTraceElement1;
/* 247 */       for (i = (arrayOfStackTraceElement1 = st).length, b = 0; b < i; ) { StackTraceElement ste = arrayOfStackTraceElement1[b];
/* 248 */         if (ste.getClassName().contains("JSON")) { b++; continue; }
/* 249 */          return " caused by " + ste; }
/*     */       
/* 251 */       return "";
/*     */     }
/*     */     
/*     */     public Parsing(String date, ParseException e) {
/* 255 */       super(date, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class RateLimit
/*     */     extends TwitterException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public RateLimit(String string) {
/* 266 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Repetition
/*     */     extends E403
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public Repetition(String tweet) {
/* 279 */       super("Already tweeted! " + tweet);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SuspendedUser
/*     */     extends E403
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */     
/*     */     SuspendedUser(String msg) {
/* 294 */       super(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Timeout
/*     */     extends E50X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public Timeout(String string) {
/* 305 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TooManyLogins
/*     */     extends E40X
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TooManyLogins(String string) {
/* 325 */       super(string);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class TooRecent
/*     */     extends E403
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     TooRecent(String msg) {
/* 336 */       super(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class TwitLongerException
/*     */     extends TwitterException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public TwitLongerException(String string, String details) {
/* 348 */       super(string, details);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Unexplained
/*     */     extends TwitterException
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public Unexplained(String msg) {
/* 359 */       super(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class UpdateToOAuth
/*     */     extends E401
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */     
/*     */     public UpdateToOAuth() {
/* 371 */       super("You need to switch to OAuth. Twitter no longer support basic authentication.");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 377 */   private String additionalInfo = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TwitterException(Exception e) {
/* 383 */     super(e);
/*     */     
/* 385 */     assert !(e instanceof TwitterException) : e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TwitterException(String string) {
/* 392 */     super(string);
/*     */   }
/*     */   
/*     */   TwitterException(String msg, Exception e) {
/* 396 */     super(msg, e);
/*     */     
/* 398 */     assert !(e instanceof TwitterException) : e;
/*     */   }
/*     */   
/*     */   public TwitterException(String string, String additionalInfo) {
/* 402 */     this(string);
/* 403 */     setAdditionalInfo(additionalInfo);
/*     */   }
/*     */   
/*     */   public String getAdditionalInfo() {
/* 407 */     return this.additionalInfo;
/*     */   }
/*     */   
/*     */   public void setAdditionalInfo(String additionalInfo) {
/* 411 */     this.additionalInfo = additionalInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\jtwitter\TwitterException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */