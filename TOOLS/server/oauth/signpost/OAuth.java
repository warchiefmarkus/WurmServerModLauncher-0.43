/*     */ package oauth.signpost;
/*     */ 
/*     */ import com.google.gdata.util.common.base.PercentEscaper;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import oauth.signpost.http.HttpParameters;
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
/*     */ public class OAuth
/*     */ {
/*     */   public static final String VERSION_1_0 = "1.0";
/*     */   public static final String ENCODING = "UTF-8";
/*     */   public static final String FORM_ENCODED = "application/x-www-form-urlencoded";
/*     */   public static final String HTTP_AUTHORIZATION_HEADER = "Authorization";
/*     */   public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
/*     */   public static final String OAUTH_TOKEN = "oauth_token";
/*     */   public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";
/*     */   public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
/*     */   public static final String OAUTH_SIGNATURE = "oauth_signature";
/*     */   public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
/*     */   public static final String OAUTH_NONCE = "oauth_nonce";
/*     */   public static final String OAUTH_VERSION = "oauth_version";
/*     */   public static final String OAUTH_CALLBACK = "oauth_callback";
/*     */   public static final String OAUTH_CALLBACK_CONFIRMED = "oauth_callback_confirmed";
/*     */   public static final String OAUTH_VERIFIER = "oauth_verifier";
/*     */   public static final String OUT_OF_BAND = "oob";
/*  76 */   private static final PercentEscaper percentEncoder = new PercentEscaper("-._~", false);
/*     */ 
/*     */   
/*     */   public static String percentEncode(String s) {
/*  80 */     if (s == null) {
/*  81 */       return "";
/*     */     }
/*  83 */     return percentEncoder.escape(s);
/*     */   }
/*     */   
/*     */   public static String percentDecode(String s) {
/*     */     try {
/*  88 */       if (s == null) {
/*  89 */         return "";
/*     */       }
/*  91 */       return URLDecoder.decode(s, "UTF-8");
/*     */     }
/*  93 */     catch (UnsupportedEncodingException wow) {
/*  94 */       throw new RuntimeException(wow.getMessage(), wow);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Map.Entry<String, String>> void formEncode(Collection<T> parameters, OutputStream into) throws IOException {
/* 105 */     if (parameters != null) {
/* 106 */       boolean first = true;
/* 107 */       for (Map.Entry<String, String> entry : parameters) {
/* 108 */         if (first) {
/* 109 */           first = false;
/*     */         } else {
/* 111 */           into.write(38);
/*     */         } 
/* 113 */         into.write(percentEncode(safeToString(entry.getKey())).getBytes());
/* 114 */         into.write(61);
/* 115 */         into.write(percentEncode(safeToString(entry.getValue())).getBytes());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Map.Entry<String, String>> String formEncode(Collection<T> parameters) throws IOException {
/* 127 */     ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 128 */     formEncode(parameters, b);
/* 129 */     return new String(b.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpParameters decodeForm(String form) {
/* 134 */     HttpParameters params = new HttpParameters();
/* 135 */     if (isEmpty(form)) {
/* 136 */       return params;
/*     */     }
/* 138 */     for (String nvp : form.split("\\&")) {
/* 139 */       String name, value; int equals = nvp.indexOf('=');
/*     */ 
/*     */       
/* 142 */       if (equals < 0) {
/* 143 */         name = percentDecode(nvp);
/* 144 */         value = null;
/*     */       } else {
/* 146 */         name = percentDecode(nvp.substring(0, equals));
/* 147 */         value = percentDecode(nvp.substring(equals + 1));
/*     */       } 
/*     */       
/* 150 */       params.put(name, value);
/*     */     } 
/* 152 */     return params;
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpParameters decodeForm(InputStream content) throws IOException {
/* 157 */     BufferedReader reader = new BufferedReader(new InputStreamReader(content));
/*     */     
/* 159 */     StringBuilder sb = new StringBuilder();
/* 160 */     String line = reader.readLine();
/* 161 */     while (line != null) {
/* 162 */       sb.append(line);
/* 163 */       line = reader.readLine();
/*     */     } 
/*     */     
/* 166 */     return decodeForm(sb.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Map.Entry<String, String>> Map<String, String> toMap(Collection<T> from) {
/* 175 */     HashMap<String, String> map = new HashMap<String, String>();
/* 176 */     if (from != null) {
/* 177 */       for (Map.Entry<String, String> entry : from) {
/* 178 */         String key = entry.getKey();
/* 179 */         if (!map.containsKey(key)) {
/* 180 */           map.put(key, entry.getValue());
/*     */         }
/*     */       } 
/*     */     }
/* 184 */     return map;
/*     */   }
/*     */   
/*     */   public static final String safeToString(Object from) {
/* 188 */     return (from == null) ? null : from.toString();
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(String str) {
/* 192 */     return (str == null || str.length() == 0);
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
/*     */   public static String addQueryParameters(String url, String... kvPairs) {
/* 218 */     String queryDelim = url.contains("?") ? "&" : "?";
/* 219 */     StringBuilder sb = new StringBuilder(url + queryDelim);
/* 220 */     for (int i = 0; i < kvPairs.length; i += 2) {
/* 221 */       if (i > 0) {
/* 222 */         sb.append("&");
/*     */       }
/* 224 */       sb.append(percentEncode(kvPairs[i]) + "=" + percentEncode(kvPairs[i + 1]));
/*     */     } 
/*     */     
/* 227 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String addQueryParameters(String url, Map<String, String> params) {
/* 231 */     String[] kvPairs = new String[params.size() * 2];
/* 232 */     int idx = 0;
/* 233 */     for (String key : params.keySet()) {
/* 234 */       kvPairs[idx] = key;
/* 235 */       kvPairs[idx + 1] = params.get(key);
/* 236 */       idx += 2;
/*     */     } 
/* 238 */     return addQueryParameters(url, kvPairs);
/*     */   }
/*     */   
/*     */   public static String addQueryString(String url, String queryString) {
/* 242 */     String queryDelim = url.contains("?") ? "&" : "?";
/* 243 */     StringBuilder sb = new StringBuilder(url + queryDelim);
/* 244 */     sb.append(queryString);
/* 245 */     return sb.toString();
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
/*     */   public static String prepareOAuthHeader(String... kvPairs) {
/* 268 */     StringBuilder sb = new StringBuilder("OAuth ");
/* 269 */     for (int i = 0; i < kvPairs.length; i += 2) {
/* 270 */       if (i > 0) {
/* 271 */         sb.append(", ");
/*     */       }
/* 273 */       boolean isOAuthElem = (kvPairs[i].startsWith("oauth_") || kvPairs[i].startsWith("x_oauth_"));
/*     */       
/* 275 */       String value = isOAuthElem ? percentEncode(kvPairs[i + 1]) : kvPairs[i + 1];
/* 276 */       sb.append(percentEncode(kvPairs[i]) + "=\"" + value + "\"");
/*     */     } 
/* 278 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static HttpParameters oauthHeaderToParamsMap(String oauthHeader) {
/* 282 */     HttpParameters params = new HttpParameters();
/* 283 */     if (oauthHeader == null || !oauthHeader.startsWith("OAuth ")) {
/* 284 */       return params;
/*     */     }
/* 286 */     oauthHeader = oauthHeader.substring("OAuth ".length());
/* 287 */     String[] elements = oauthHeader.split(",");
/* 288 */     for (String keyValuePair : elements) {
/* 289 */       String[] keyValue = keyValuePair.split("=");
/* 290 */       params.put(keyValue[0].trim(), keyValue[1].replace("\"", "").trim());
/*     */     } 
/* 292 */     return params;
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
/*     */   public static String toHeaderElement(String name, String value) {
/* 307 */     return percentEncode(name) + "=\"" + percentEncode(value) + "\"";
/*     */   }
/*     */   
/*     */   public static void debugOut(String key, String value) {
/* 311 */     if (System.getProperty("debug") != null)
/* 312 */       System.out.println("[SIGNPOST] " + key + ": " + value); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\OAuth.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */