/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthSchemeRegistry;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthenticationHandler;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public abstract class AbstractAuthenticationHandler
/*     */   implements AuthenticationHandler
/*     */ {
/*  67 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  69 */   private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[] { "negotiate", "NTLM", "Digest", "Basic" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, Header> parseChallenges(Header[] headers) throws MalformedChallengeException {
/*  84 */     Map<String, Header> map = new HashMap<String, Header>(headers.length);
/*  85 */     for (Header header : headers) {
/*     */       CharArrayBuffer buffer;
/*     */       int pos;
/*  88 */       if (header instanceof FormattedHeader) {
/*  89 */         buffer = ((FormattedHeader)header).getBuffer();
/*  90 */         pos = ((FormattedHeader)header).getValuePos();
/*     */       } else {
/*  92 */         String str = header.getValue();
/*  93 */         if (str == null) {
/*  94 */           throw new MalformedChallengeException("Header value is null");
/*     */         }
/*  96 */         buffer = new CharArrayBuffer(str.length());
/*  97 */         buffer.append(str);
/*  98 */         pos = 0;
/*     */       } 
/* 100 */       while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
/* 101 */         pos++;
/*     */       }
/* 103 */       int beginIndex = pos;
/* 104 */       while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
/* 105 */         pos++;
/*     */       }
/* 107 */       int endIndex = pos;
/* 108 */       String s = buffer.substring(beginIndex, endIndex);
/* 109 */       map.put(s.toLowerCase(Locale.US), header);
/*     */     } 
/* 111 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<String> getAuthPreferences() {
/* 120 */     return DEFAULT_SCHEME_PRIORITY;
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
/*     */   protected List<String> getAuthPreferences(HttpResponse response, HttpContext context) {
/* 135 */     return getAuthPreferences();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthScheme selectScheme(Map<String, Header> challenges, HttpResponse response, HttpContext context) throws AuthenticationException {
/* 143 */     AuthSchemeRegistry registry = (AuthSchemeRegistry)context.getAttribute("http.authscheme-registry");
/*     */     
/* 145 */     if (registry == null) {
/* 146 */       throw new IllegalStateException("AuthScheme registry not set in HTTP context");
/*     */     }
/*     */     
/* 149 */     Collection<String> authPrefs = getAuthPreferences(response, context);
/* 150 */     if (authPrefs == null) {
/* 151 */       authPrefs = DEFAULT_SCHEME_PRIORITY;
/*     */     }
/*     */     
/* 154 */     if (this.log.isDebugEnabled()) {
/* 155 */       this.log.debug("Authentication schemes in the order of preference: " + authPrefs);
/*     */     }
/*     */ 
/*     */     
/* 159 */     AuthScheme authScheme = null;
/* 160 */     for (String id : authPrefs) {
/* 161 */       Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));
/*     */       
/* 163 */       if (challenge != null) {
/* 164 */         if (this.log.isDebugEnabled()) {
/* 165 */           this.log.debug(id + " authentication scheme selected");
/*     */         }
/*     */         try {
/* 168 */           authScheme = registry.getAuthScheme(id, response.getParams());
/*     */           break;
/* 170 */         } catch (IllegalStateException e) {
/* 171 */           if (this.log.isWarnEnabled()) {
/* 172 */             this.log.warn("Authentication scheme " + id + " not supported");
/*     */           }
/*     */           continue;
/*     */         } 
/*     */       } 
/* 177 */       if (this.log.isDebugEnabled()) {
/* 178 */         this.log.debug("Challenge for " + id + " authentication scheme not available");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 183 */     if (authScheme == null)
/*     */     {
/* 185 */       throw new AuthenticationException("Unable to respond to any of these challenges: " + challenges);
/*     */     }
/*     */ 
/*     */     
/* 189 */     return authScheme;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\client\AbstractAuthenticationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */