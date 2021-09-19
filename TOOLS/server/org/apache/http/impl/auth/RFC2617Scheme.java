/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.message.BasicHeaderValueParser;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public abstract class RFC2617Scheme
/*     */   extends AuthSchemeBase
/*     */ {
/*     */   private final Map<String, String> params;
/*     */   
/*     */   public RFC2617Scheme(ChallengeState challengeState) {
/*  65 */     super(challengeState);
/*  66 */     this.params = new HashMap<String, String>();
/*     */   }
/*     */   
/*     */   public RFC2617Scheme() {
/*  70 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseChallenge(CharArrayBuffer buffer, int pos, int len) throws MalformedChallengeException {
/*  76 */     BasicHeaderValueParser basicHeaderValueParser = BasicHeaderValueParser.DEFAULT;
/*  77 */     ParserCursor cursor = new ParserCursor(pos, buffer.length());
/*  78 */     HeaderElement[] elements = basicHeaderValueParser.parseElements(buffer, cursor);
/*  79 */     if (elements.length == 0) {
/*  80 */       throw new MalformedChallengeException("Authentication challenge is empty");
/*     */     }
/*  82 */     this.params.clear();
/*  83 */     for (HeaderElement element : elements) {
/*  84 */       this.params.put(element.getName(), element.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, String> getParameters() {
/*  94 */     return this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 105 */     if (name == null) {
/* 106 */       return null;
/*     */     }
/* 108 */     return this.params.get(name.toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 117 */     return getParameter("realm");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\auth\RFC2617Scheme.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */