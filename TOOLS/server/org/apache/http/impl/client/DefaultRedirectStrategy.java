/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.CircularRedirectException;
/*     */ import org.apache.http.client.RedirectStrategy;
/*     */ import org.apache.http.client.methods.HttpDelete;
/*     */ import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpOptions;
/*     */ import org.apache.http.client.methods.HttpPatch;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpTrace;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultRedirectStrategy
/*     */   implements RedirectStrategy
/*     */ {
/*  79 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
/*     */ 
/*     */   
/*  86 */   private static final String[] REDIRECT_METHODS = new String[] { "GET", "HEAD" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/*  99 */     if (request == null) {
/* 100 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 102 */     if (response == null) {
/* 103 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/*     */     
/* 106 */     int statusCode = response.getStatusLine().getStatusCode();
/* 107 */     String method = request.getRequestLine().getMethod();
/* 108 */     Header locationHeader = response.getFirstHeader("location");
/* 109 */     switch (statusCode) {
/*     */       case 302:
/* 111 */         return (isRedirectable(method) && locationHeader != null);
/*     */       case 301:
/*     */       case 307:
/* 114 */         return isRedirectable(method);
/*     */       case 303:
/* 116 */         return true;
/*     */     } 
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/* 126 */     if (request == null) {
/* 127 */       throw new IllegalArgumentException("HTTP request may not be null");
/*     */     }
/* 129 */     if (response == null) {
/* 130 */       throw new IllegalArgumentException("HTTP response may not be null");
/*     */     }
/* 132 */     if (context == null) {
/* 133 */       throw new IllegalArgumentException("HTTP context may not be null");
/*     */     }
/*     */     
/* 136 */     Header locationHeader = response.getFirstHeader("location");
/* 137 */     if (locationHeader == null)
/*     */     {
/* 139 */       throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
/*     */     }
/*     */ 
/*     */     
/* 143 */     String location = locationHeader.getValue();
/* 144 */     if (this.log.isDebugEnabled()) {
/* 145 */       this.log.debug("Redirect requested to location '" + location + "'");
/*     */     }
/*     */     
/* 148 */     URI uri = createLocationURI(location);
/*     */     
/* 150 */     HttpParams params = request.getParams();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 155 */       uri = URIUtils.rewriteURI(uri);
/* 156 */       if (!uri.isAbsolute()) {
/* 157 */         if (params.isParameterTrue("http.protocol.reject-relative-redirect")) {
/* 158 */           throw new ProtocolException("Relative redirect location '" + uri + "' not allowed");
/*     */         }
/*     */ 
/*     */         
/* 162 */         HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/* 163 */         if (target == null) {
/* 164 */           throw new IllegalStateException("Target host not available in the HTTP context");
/*     */         }
/*     */         
/* 167 */         URI requestURI = new URI(request.getRequestLine().getUri());
/* 168 */         URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, true);
/* 169 */         uri = URIUtils.resolve(absoluteRequestURI, uri);
/*     */       } 
/* 171 */     } catch (URISyntaxException ex) {
/* 172 */       throw new ProtocolException(ex.getMessage(), ex);
/*     */     } 
/*     */     
/* 175 */     RedirectLocations redirectLocations = (RedirectLocations)context.getAttribute("http.protocol.redirect-locations");
/*     */     
/* 177 */     if (redirectLocations == null) {
/* 178 */       redirectLocations = new RedirectLocations();
/* 179 */       context.setAttribute("http.protocol.redirect-locations", redirectLocations);
/*     */     } 
/* 181 */     if (params.isParameterFalse("http.protocol.allow-circular-redirects") && 
/* 182 */       redirectLocations.contains(uri)) {
/* 183 */       throw new CircularRedirectException("Circular redirect to '" + uri + "'");
/*     */     }
/*     */     
/* 186 */     redirectLocations.add(uri);
/* 187 */     return uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected URI createLocationURI(String location) throws ProtocolException {
/*     */     try {
/* 195 */       return (new URI(location)).normalize();
/* 196 */     } catch (URISyntaxException ex) {
/* 197 */       throw new ProtocolException("Invalid redirect URI: " + location, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isRedirectable(String method) {
/* 205 */     for (String m : REDIRECT_METHODS) {
/* 206 */       if (m.equalsIgnoreCase(method)) {
/* 207 */         return true;
/*     */       }
/*     */     } 
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
/* 217 */     URI uri = getLocationURI(request, response, context);
/* 218 */     String method = request.getRequestLine().getMethod();
/* 219 */     if (method.equalsIgnoreCase("HEAD"))
/* 220 */       return (HttpUriRequest)new HttpHead(uri); 
/* 221 */     if (method.equalsIgnoreCase("GET")) {
/* 222 */       return (HttpUriRequest)new HttpGet(uri);
/*     */     }
/* 224 */     int status = response.getStatusLine().getStatusCode();
/* 225 */     if (status == 307) {
/* 226 */       if (method.equalsIgnoreCase("POST"))
/* 227 */         return copyEntity((HttpEntityEnclosingRequestBase)new HttpPost(uri), request); 
/* 228 */       if (method.equalsIgnoreCase("PUT"))
/* 229 */         return copyEntity((HttpEntityEnclosingRequestBase)new HttpPut(uri), request); 
/* 230 */       if (method.equalsIgnoreCase("DELETE"))
/* 231 */         return (HttpUriRequest)new HttpDelete(uri); 
/* 232 */       if (method.equalsIgnoreCase("TRACE"))
/* 233 */         return (HttpUriRequest)new HttpTrace(uri); 
/* 234 */       if (method.equalsIgnoreCase("OPTIONS"))
/* 235 */         return (HttpUriRequest)new HttpOptions(uri); 
/* 236 */       if (method.equalsIgnoreCase("PATCH")) {
/* 237 */         return copyEntity((HttpEntityEnclosingRequestBase)new HttpPatch(uri), request);
/*     */       }
/*     */     } 
/* 240 */     return (HttpUriRequest)new HttpGet(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpUriRequest copyEntity(HttpEntityEnclosingRequestBase redirect, HttpRequest original) {
/* 246 */     if (original instanceof HttpEntityEnclosingRequest) {
/* 247 */       redirect.setEntity(((HttpEntityEnclosingRequest)original).getEntity());
/*     */     }
/* 249 */     return (HttpUriRequest)redirect;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\DefaultRedirectStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */