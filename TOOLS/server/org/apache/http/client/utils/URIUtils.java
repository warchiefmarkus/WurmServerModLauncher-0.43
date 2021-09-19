/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Stack;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class URIUtils
/*     */ {
/*     */   @Deprecated
/*     */   public static URI createURI(String scheme, String host, int port, String path, String query, String fragment) throws URISyntaxException {
/*  82 */     StringBuilder buffer = new StringBuilder();
/*  83 */     if (host != null) {
/*  84 */       if (scheme != null) {
/*  85 */         buffer.append(scheme);
/*  86 */         buffer.append("://");
/*     */       } 
/*  88 */       buffer.append(host);
/*  89 */       if (port > 0) {
/*  90 */         buffer.append(':');
/*  91 */         buffer.append(port);
/*     */       } 
/*     */     } 
/*  94 */     if (path == null || !path.startsWith("/")) {
/*  95 */       buffer.append('/');
/*     */     }
/*  97 */     if (path != null) {
/*  98 */       buffer.append(path);
/*     */     }
/* 100 */     if (query != null) {
/* 101 */       buffer.append('?');
/* 102 */       buffer.append(query);
/*     */     } 
/* 104 */     if (fragment != null) {
/* 105 */       buffer.append('#');
/* 106 */       buffer.append(fragment);
/*     */     } 
/* 108 */     return new URI(buffer.toString());
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
/*     */   public static URI rewriteURI(URI uri, HttpHost target, boolean dropFragment) throws URISyntaxException {
/* 131 */     if (uri == null) {
/* 132 */       throw new IllegalArgumentException("URI may not be null");
/*     */     }
/* 134 */     URIBuilder uribuilder = new URIBuilder(uri);
/* 135 */     if (target != null) {
/* 136 */       uribuilder.setScheme(target.getSchemeName());
/* 137 */       uribuilder.setHost(target.getHostName());
/* 138 */       uribuilder.setPort(target.getPort());
/*     */     } else {
/* 140 */       uribuilder.setScheme(null);
/* 141 */       uribuilder.setHost(null);
/* 142 */       uribuilder.setPort(-1);
/*     */     } 
/* 144 */     if (dropFragment) {
/* 145 */       uribuilder.setFragment(null);
/*     */     }
/* 147 */     if (uribuilder.getPath() == null || uribuilder.getPath().length() == 0) {
/* 148 */       uribuilder.setPath("/");
/*     */     }
/* 150 */     return uribuilder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URI rewriteURI(URI uri, HttpHost target) throws URISyntaxException {
/* 161 */     return rewriteURI(uri, target, false);
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
/*     */   public static URI rewriteURI(URI uri) throws URISyntaxException {
/* 176 */     if (uri == null) {
/* 177 */       throw new IllegalArgumentException("URI may not be null");
/*     */     }
/* 179 */     if (uri.getFragment() != null || uri.getUserInfo() != null || uri.getPath() == null || uri.getPath().length() == 0) {
/*     */       
/* 181 */       URIBuilder uribuilder = new URIBuilder(uri);
/* 182 */       uribuilder.setFragment(null).setUserInfo(null);
/* 183 */       if (uribuilder.getPath() == null || uribuilder.getPath().length() == 0) {
/* 184 */         uribuilder.setPath("/");
/*     */       }
/* 186 */       return uribuilder.build();
/*     */     } 
/* 188 */     return uri;
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
/*     */   public static URI resolve(URI baseURI, String reference) {
/* 201 */     return resolve(baseURI, URI.create(reference));
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
/*     */   public static URI resolve(URI baseURI, URI reference) {
/* 213 */     if (baseURI == null) {
/* 214 */       throw new IllegalArgumentException("Base URI may nor be null");
/*     */     }
/* 216 */     if (reference == null) {
/* 217 */       throw new IllegalArgumentException("Reference URI may nor be null");
/*     */     }
/* 219 */     String s = reference.toString();
/* 220 */     if (s.startsWith("?")) {
/* 221 */       return resolveReferenceStartingWithQueryString(baseURI, reference);
/*     */     }
/* 223 */     boolean emptyReference = (s.length() == 0);
/* 224 */     if (emptyReference) {
/* 225 */       reference = URI.create("#");
/*     */     }
/* 227 */     URI resolved = baseURI.resolve(reference);
/* 228 */     if (emptyReference) {
/* 229 */       String resolvedString = resolved.toString();
/* 230 */       resolved = URI.create(resolvedString.substring(0, resolvedString.indexOf('#')));
/*     */     } 
/*     */     
/* 233 */     return normalizeSyntax(resolved);
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
/*     */   private static URI resolveReferenceStartingWithQueryString(URI baseURI, URI reference) {
/* 245 */     String baseUri = baseURI.toString();
/* 246 */     baseUri = (baseUri.indexOf('?') > -1) ? baseUri.substring(0, baseUri.indexOf('?')) : baseUri;
/*     */     
/* 248 */     return URI.create(baseUri + reference.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URI normalizeSyntax(URI uri) {
/* 259 */     if (uri.isOpaque()) {
/* 260 */       return uri;
/*     */     }
/* 262 */     String path = (uri.getPath() == null) ? "" : uri.getPath();
/* 263 */     String[] inputSegments = path.split("/");
/* 264 */     Stack<String> outputSegments = new Stack<String>();
/* 265 */     for (String inputSegment : inputSegments) {
/* 266 */       if (inputSegment.length() != 0 && !".".equals(inputSegment))
/*     */       {
/*     */         
/* 269 */         if ("..".equals(inputSegment)) {
/* 270 */           if (!outputSegments.isEmpty()) {
/* 271 */             outputSegments.pop();
/*     */           }
/*     */         } else {
/* 274 */           outputSegments.push(inputSegment);
/*     */         }  } 
/*     */     } 
/* 277 */     StringBuilder outputBuffer = new StringBuilder();
/* 278 */     for (String outputSegment : outputSegments) {
/* 279 */       outputBuffer.append('/').append(outputSegment);
/*     */     }
/* 281 */     if (path.lastIndexOf('/') == path.length() - 1)
/*     */     {
/* 283 */       outputBuffer.append('/');
/*     */     }
/*     */     try {
/* 286 */       String scheme = uri.getScheme().toLowerCase();
/* 287 */       String auth = uri.getAuthority().toLowerCase();
/* 288 */       URI ref = new URI(scheme, auth, outputBuffer.toString(), null, null);
/*     */       
/* 290 */       if (uri.getQuery() == null && uri.getFragment() == null) {
/* 291 */         return ref;
/*     */       }
/* 293 */       StringBuilder normalized = new StringBuilder(ref.toASCIIString());
/*     */       
/* 295 */       if (uri.getQuery() != null)
/*     */       {
/* 297 */         normalized.append('?').append(uri.getRawQuery());
/*     */       }
/* 299 */       if (uri.getFragment() != null)
/*     */       {
/* 301 */         normalized.append('#').append(uri.getRawFragment());
/*     */       }
/* 303 */       return URI.create(normalized.toString());
/* 304 */     } catch (URISyntaxException e) {
/* 305 */       throw new IllegalArgumentException(e);
/*     */     } 
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
/*     */   public static HttpHost extractHost(URI uri) {
/* 319 */     if (uri == null) {
/* 320 */       return null;
/*     */     }
/* 322 */     HttpHost target = null;
/* 323 */     if (uri.isAbsolute()) {
/* 324 */       int port = uri.getPort();
/* 325 */       String host = uri.getHost();
/* 326 */       if (host == null) {
/*     */         
/* 328 */         host = uri.getAuthority();
/* 329 */         if (host != null) {
/*     */           
/* 331 */           int at = host.indexOf('@');
/* 332 */           if (at >= 0) {
/* 333 */             if (host.length() > at + 1) {
/* 334 */               host = host.substring(at + 1);
/*     */             } else {
/* 336 */               host = null;
/*     */             } 
/*     */           }
/*     */           
/* 340 */           if (host != null) {
/* 341 */             int colon = host.indexOf(':');
/* 342 */             if (colon >= 0) {
/* 343 */               int pos = colon + 1;
/* 344 */               int len = 0;
/* 345 */               for (int i = pos; i < host.length() && 
/* 346 */                 Character.isDigit(host.charAt(i)); i++) {
/* 347 */                 len++;
/*     */               }
/*     */ 
/*     */ 
/*     */               
/* 352 */               if (len > 0) {
/*     */                 try {
/* 354 */                   port = Integer.parseInt(host.substring(pos, pos + len));
/* 355 */                 } catch (NumberFormatException ex) {}
/*     */               }
/*     */               
/* 358 */               host = host.substring(0, colon);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 363 */       String scheme = uri.getScheme();
/* 364 */       if (host != null) {
/* 365 */         target = new HttpHost(host, port, scheme);
/*     */       }
/*     */     } 
/* 368 */     return target;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\clien\\utils\URIUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */