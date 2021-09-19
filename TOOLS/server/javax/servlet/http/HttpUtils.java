/*     */ package javax.servlet.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.servlet.ServletInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpUtils
/*     */ {
/*     */   private static final String LSTRING_FILE = "javax.servlet.http.LocalStrings";
/*  78 */   private static ResourceBundle lStrings = ResourceBundle.getBundle("javax.servlet.http.LocalStrings");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Hashtable<String, String[]> parseQueryString(String s) {
/* 118 */     String[] valArray = null;
/*     */     
/* 120 */     if (s == null) {
/* 121 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 124 */     Hashtable<String, String[]> ht = (Hashtable)new Hashtable<String, String>();
/* 125 */     StringBuilder sb = new StringBuilder();
/* 126 */     StringTokenizer st = new StringTokenizer(s, "&");
/* 127 */     while (st.hasMoreTokens()) {
/* 128 */       String pair = st.nextToken();
/* 129 */       int pos = pair.indexOf('=');
/* 130 */       if (pos == -1)
/*     */       {
/*     */         
/* 133 */         throw new IllegalArgumentException();
/*     */       }
/* 135 */       String key = parseName(pair.substring(0, pos), sb);
/* 136 */       String val = parseName(pair.substring(pos + 1, pair.length()), sb);
/* 137 */       if (ht.containsKey(key)) {
/* 138 */         String[] oldVals = ht.get(key);
/* 139 */         valArray = new String[oldVals.length + 1];
/* 140 */         for (int i = 0; i < oldVals.length; i++) {
/* 141 */           valArray[i] = oldVals[i];
/*     */         }
/* 143 */         valArray[oldVals.length] = val;
/*     */       } else {
/* 145 */         valArray = new String[1];
/* 146 */         valArray[0] = val;
/*     */       } 
/* 148 */       ht.put(key, valArray);
/*     */     } 
/*     */     
/* 151 */     return ht;
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
/*     */   public static Hashtable<String, String[]> parsePostData(int len, ServletInputStream in) {
/* 195 */     if (len <= 0)
/*     */     {
/* 197 */       return (Hashtable)new Hashtable<String, String>();
/*     */     }
/*     */     
/* 200 */     if (in == null) {
/* 201 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     byte[] postedBytes = new byte[len];
/*     */     try {
/* 209 */       int offset = 0;
/*     */       
/*     */       do {
/* 212 */         int inputLen = in.read(postedBytes, offset, len - offset);
/* 213 */         if (inputLen <= 0) {
/* 214 */           String msg = lStrings.getString("err.io.short_read");
/* 215 */           throw new IllegalArgumentException(msg);
/*     */         } 
/* 217 */         offset += inputLen;
/* 218 */       } while (len - offset > 0);
/*     */     }
/* 220 */     catch (IOException e) {
/* 221 */       throw new IllegalArgumentException(e.getMessage());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 230 */       String postedBody = new String(postedBytes, 0, len, "8859_1");
/* 231 */       return parseQueryString(postedBody);
/* 232 */     } catch (UnsupportedEncodingException e) {
/*     */ 
/*     */       
/* 235 */       throw new IllegalArgumentException(e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String parseName(String s, StringBuilder sb) {
/* 244 */     sb.setLength(0);
/* 245 */     for (int i = 0; i < s.length(); i++) {
/* 246 */       char c = s.charAt(i);
/* 247 */       switch (c) {
/*     */         case '+':
/* 249 */           sb.append(' ');
/*     */           break;
/*     */         case '%':
/*     */           try {
/* 253 */             sb.append((char)Integer.parseInt(s.substring(i + 1, i + 3), 16));
/*     */             
/* 255 */             i += 2;
/* 256 */           } catch (NumberFormatException e) {
/*     */ 
/*     */             
/* 259 */             throw new IllegalArgumentException();
/* 260 */           } catch (StringIndexOutOfBoundsException e) {
/* 261 */             String rest = s.substring(i);
/* 262 */             sb.append(rest);
/* 263 */             if (rest.length() == 2) {
/* 264 */               i++;
/*     */             }
/*     */           } 
/*     */           break;
/*     */         default:
/* 269 */           sb.append(c);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 274 */     return sb.toString();
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
/*     */   public static StringBuffer getRequestURL(HttpServletRequest req) {
/* 300 */     StringBuffer url = new StringBuffer();
/* 301 */     String scheme = req.getScheme();
/* 302 */     int port = req.getServerPort();
/* 303 */     String urlPath = req.getRequestURI();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 308 */     url.append(scheme);
/* 309 */     url.append("://");
/* 310 */     url.append(req.getServerName());
/* 311 */     if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
/*     */       
/* 313 */       url.append(':');
/* 314 */       url.append(req.getServerPort());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 320 */     url.append(urlPath);
/*     */     
/* 322 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\servlet\http\HttpUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */