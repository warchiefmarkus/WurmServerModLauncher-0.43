/*     */ package org.seamless.util;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URIUtil
/*     */ {
/*     */   public static URI createAbsoluteURI(URI base, String uri) throws IllegalArgumentException {
/*  36 */     return createAbsoluteURI(base, URI.create(uri));
/*     */   }
/*     */   
/*     */   public static URI createAbsoluteURI(URI base, URI relativeOrNot) throws IllegalArgumentException {
/*  40 */     if (base == null && !relativeOrNot.isAbsolute())
/*  41 */       throw new IllegalArgumentException("Base URI is null and given URI is not absolute"); 
/*  42 */     if (base == null && relativeOrNot.isAbsolute()) {
/*  43 */       return relativeOrNot;
/*     */     }
/*  45 */     assert base != null;
/*     */     
/*  47 */     if (base.getPath().length() == 0) {
/*     */       try {
/*  49 */         base = new URI(base.getScheme(), base.getAuthority(), "/", base.getQuery(), base.getFragment());
/*  50 */       } catch (Exception ex) {
/*  51 */         throw new IllegalArgumentException(ex);
/*     */       } 
/*     */     }
/*  54 */     return base.resolve(relativeOrNot);
/*     */   }
/*     */ 
/*     */   
/*     */   public static URL createAbsoluteURL(URL base, String uri) throws IllegalArgumentException {
/*  59 */     return createAbsoluteURL(base, URI.create(uri));
/*     */   }
/*     */ 
/*     */   
/*     */   public static URL createAbsoluteURL(URL base, URI relativeOrNot) throws IllegalArgumentException {
/*  64 */     if (base == null && !relativeOrNot.isAbsolute())
/*  65 */       throw new IllegalArgumentException("Base URL is null and given URI is not absolute"); 
/*  66 */     if (base == null && relativeOrNot.isAbsolute()) {
/*     */       try {
/*  68 */         return relativeOrNot.toURL();
/*  69 */       } catch (Exception ex) {
/*  70 */         throw new IllegalArgumentException("Base URL was null and given URI can't be converted to URL");
/*     */       } 
/*     */     }
/*     */     try {
/*  74 */       assert base != null;
/*  75 */       URI baseURI = base.toURI();
/*  76 */       URI absoluteURI = createAbsoluteURI(baseURI, relativeOrNot);
/*  77 */       return absoluteURI.toURL();
/*  78 */     } catch (Exception ex) {
/*  79 */       throw new IllegalArgumentException("Base URL is not an URI, or can't create absolute URI (null?), or absolute URI can not be converted to URL", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL createAbsoluteURL(URI base, URI relativeOrNot) throws IllegalArgumentException {
/*     */     try {
/*  88 */       return createAbsoluteURI(base, relativeOrNot).toURL();
/*  89 */     } catch (Exception ex) {
/*  90 */       throw new IllegalArgumentException("Absolute URI can not be converted to URL", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URL createAbsoluteURL(InetAddress address, int localStreamPort, URI relativeOrNot) throws IllegalArgumentException {
/*     */     try {
/*  96 */       if (address instanceof java.net.Inet6Address)
/*  97 */         return createAbsoluteURL(new URL("http://[" + address.getHostAddress() + "]:" + localStreamPort), relativeOrNot); 
/*  98 */       if (address instanceof java.net.Inet4Address) {
/*  99 */         return createAbsoluteURL(new URL("http://" + address.getHostAddress() + ":" + localStreamPort), relativeOrNot);
/*     */       }
/* 101 */       throw new IllegalArgumentException("InetAddress is neither IPv4 nor IPv6: " + address);
/*     */     }
/* 103 */     catch (Exception ex) {
/* 104 */       throw new IllegalArgumentException("Address, port, and URI can not be converted to URL", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URI createRelativePathURI(URI uri) {
/* 109 */     assertRelativeURI("Given", uri);
/*     */ 
/*     */     
/* 112 */     URI normalizedURI = uri.normalize();
/*     */ 
/*     */     
/* 115 */     String uriString = normalizedURI.toString();
/*     */     int idx;
/* 117 */     while ((idx = uriString.indexOf("../")) != -1) {
/* 118 */       uriString = uriString.substring(0, idx) + uriString.substring(idx + 3);
/*     */     }
/*     */     
/* 121 */     while (uriString.startsWith("/")) {
/* 122 */       uriString = uriString.substring(1);
/*     */     }
/* 124 */     return URI.create(uriString);
/*     */   }
/*     */   
/*     */   public static URI createRelativeURI(URI base, URI full) {
/* 128 */     return base.relativize(full);
/*     */   }
/*     */   
/*     */   public static URI createRelativeURI(URL base, URL full) throws IllegalArgumentException {
/*     */     try {
/* 133 */       return createRelativeURI(base.toURI(), full.toURI());
/* 134 */     } catch (Exception ex) {
/* 135 */       throw new IllegalArgumentException("Can't convert base or full URL to URI", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URI createRelativeURI(URI base, URL full) throws IllegalArgumentException {
/*     */     try {
/* 141 */       return createRelativeURI(base, full.toURI());
/* 142 */     } catch (Exception ex) {
/* 143 */       throw new IllegalArgumentException("Can't convert full URL to URI", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URI createRelativeURI(URL base, URI full) throws IllegalArgumentException {
/*     */     try {
/* 149 */       return createRelativeURI(base.toURI(), full);
/* 150 */     } catch (Exception ex) {
/* 151 */       throw new IllegalArgumentException("Can't convert base URL to URI", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isAbsoluteURI(String s) {
/* 156 */     URI uri = URI.create(s);
/* 157 */     return uri.isAbsolute();
/*     */   }
/*     */   
/*     */   public static void assertRelativeURI(String what, URI uri) {
/* 161 */     if (uri.isAbsolute()) {
/* 162 */       throw new IllegalArgumentException(what + " URI must be relative, without scheme and authority");
/*     */     }
/*     */   }
/*     */   
/*     */   public static URL toURL(URI uri) {
/* 167 */     if (uri == null) return null; 
/*     */     try {
/* 169 */       return uri.toURL();
/* 170 */     } catch (MalformedURLException ex) {
/* 171 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URI toURI(URL url) {
/* 176 */     if (url == null) return null; 
/*     */     try {
/* 178 */       return url.toURI();
/* 179 */     } catch (URISyntaxException ex) {
/* 180 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String percentEncode(String s) {
/* 185 */     if (s == null) return ""; 
/*     */     try {
/* 187 */       return URLEncoder.encode(s, "UTF-8");
/* 188 */     } catch (Exception ex) {
/* 189 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String percentDecode(String s) {
/* 194 */     if (s == null) return ""; 
/*     */     try {
/* 196 */       return URLDecoder.decode(s, "UTF-8");
/* 197 */     } catch (Exception ex) {
/* 198 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 207 */   public static final BitSet ALLOWED = new BitSet()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   public static final BitSet PATH_SEGMENT = new BitSet()
/*     */     {
/*     */     
/*     */     };
/*     */   
/* 242 */   public static final BitSet PATH_PARAM_NAME = new BitSet()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/* 248 */   public static final BitSet PATH_PARAM_VALUE = new BitSet()
/*     */     {
/*     */     
/*     */     };
/*     */   
/* 253 */   public static final BitSet QUERY = new BitSet()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   public static final BitSet FRAGMENT = new BitSet()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */   
/*     */   public static String encodePathSegment(String pathSegment) {
/* 269 */     return encode(PATH_SEGMENT, pathSegment, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String encodePathParamName(String pathParamName) {
/* 273 */     return encode(PATH_PARAM_NAME, pathParamName, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String encodePathParamValue(String pathParamValue) {
/* 277 */     return encode(PATH_PARAM_VALUE, pathParamValue, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String encodeQueryNameOrValue(String queryNameOrValue) {
/* 281 */     return encode(QUERY, queryNameOrValue, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String encodeFragment(String fragment) {
/* 285 */     return encode(FRAGMENT, fragment, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String encode(BitSet allowedCharacters, String s, String charset) {
/* 289 */     if (s == null)
/* 290 */       return null; 
/* 291 */     StringBuilder encoded = new StringBuilder(s.length() * 3);
/* 292 */     char[] characters = s.toCharArray();
/*     */     try {
/* 294 */       for (char c : characters) {
/* 295 */         if (allowedCharacters.get(c)) {
/* 296 */           encoded.append(c);
/*     */         } else {
/* 298 */           byte[] bytes = String.valueOf(c).getBytes(charset);
/* 299 */           for (byte b : bytes)
/* 300 */           { encoded.append(String.format("%%%1$02X", new Object[] { Integer.valueOf(b & 0xFF) })); } 
/*     */         } 
/*     */       } 
/* 303 */     } catch (Exception ex) {
/* 304 */       throw new RuntimeException(ex);
/*     */     } 
/* 306 */     return encoded.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\URIUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */