/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.TokenIterator;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicTokenIterator
/*     */   implements TokenIterator
/*     */ {
/*     */   public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
/*     */   protected final HeaderIterator headerIt;
/*     */   protected String currentHeader;
/*     */   protected String currentToken;
/*     */   protected int searchPos;
/*     */   
/*     */   public BasicTokenIterator(HeaderIterator headerIterator) {
/*  83 */     if (headerIterator == null) {
/*  84 */       throw new IllegalArgumentException("Header iterator must not be null.");
/*     */     }
/*     */ 
/*     */     
/*  88 */     this.headerIt = headerIterator;
/*  89 */     this.searchPos = findNext(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  95 */     return (this.currentToken != null);
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
/*     */   public String nextToken() throws NoSuchElementException, ParseException {
/* 110 */     if (this.currentToken == null) {
/* 111 */       throw new NoSuchElementException("Iteration already finished.");
/*     */     }
/*     */     
/* 114 */     String result = this.currentToken;
/*     */     
/* 116 */     this.searchPos = findNext(this.searchPos);
/*     */     
/* 118 */     return result;
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
/*     */   public final Object next() throws NoSuchElementException, ParseException {
/* 133 */     return nextToken();
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
/*     */   public final void remove() throws UnsupportedOperationException {
/* 145 */     throw new UnsupportedOperationException("Removing tokens is not supported.");
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
/*     */   protected int findNext(int from) throws ParseException {
/* 169 */     if (from < 0) {
/*     */       
/* 171 */       if (!this.headerIt.hasNext()) {
/* 172 */         return -1;
/*     */       }
/* 174 */       this.currentHeader = this.headerIt.nextHeader().getValue();
/* 175 */       from = 0;
/*     */     } else {
/*     */       
/* 178 */       from = findTokenSeparator(from);
/*     */     } 
/*     */     
/* 181 */     int start = findTokenStart(from);
/* 182 */     if (start < 0) {
/* 183 */       this.currentToken = null;
/* 184 */       return -1;
/*     */     } 
/*     */     
/* 187 */     int end = findTokenEnd(start);
/* 188 */     this.currentToken = createToken(this.currentHeader, start, end);
/* 189 */     return end;
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
/*     */   protected String createToken(String value, int start, int end) {
/* 214 */     return value.substring(start, end);
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
/*     */   protected int findTokenStart(int from) {
/* 229 */     if (from < 0) {
/* 230 */       throw new IllegalArgumentException("Search position must not be negative: " + from);
/*     */     }
/*     */ 
/*     */     
/* 234 */     boolean found = false;
/* 235 */     while (!found && this.currentHeader != null) {
/*     */       
/* 237 */       int to = this.currentHeader.length();
/* 238 */       while (!found && from < to) {
/*     */         
/* 240 */         char ch = this.currentHeader.charAt(from);
/* 241 */         if (isTokenSeparator(ch) || isWhitespace(ch)) {
/*     */           
/* 243 */           from++; continue;
/* 244 */         }  if (isTokenChar(this.currentHeader.charAt(from))) {
/*     */           
/* 246 */           found = true; continue;
/*     */         } 
/* 248 */         throw new ParseException("Invalid character before token (pos " + from + "): " + this.currentHeader);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 253 */       if (!found) {
/* 254 */         if (this.headerIt.hasNext()) {
/* 255 */           this.currentHeader = this.headerIt.nextHeader().getValue();
/* 256 */           from = 0; continue;
/*     */         } 
/* 258 */         this.currentHeader = null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 263 */     return found ? from : -1;
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
/*     */   protected int findTokenSeparator(int from) {
/* 285 */     if (from < 0) {
/* 286 */       throw new IllegalArgumentException("Search position must not be negative: " + from);
/*     */     }
/*     */ 
/*     */     
/* 290 */     boolean found = false;
/* 291 */     int to = this.currentHeader.length();
/* 292 */     while (!found && from < to) {
/* 293 */       char ch = this.currentHeader.charAt(from);
/* 294 */       if (isTokenSeparator(ch)) {
/* 295 */         found = true; continue;
/* 296 */       }  if (isWhitespace(ch)) {
/* 297 */         from++; continue;
/* 298 */       }  if (isTokenChar(ch)) {
/* 299 */         throw new ParseException("Tokens without separator (pos " + from + "): " + this.currentHeader);
/*     */       }
/*     */ 
/*     */       
/* 303 */       throw new ParseException("Invalid character after token (pos " + from + "): " + this.currentHeader);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 309 */     return from;
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
/*     */   protected int findTokenEnd(int from) {
/* 325 */     if (from < 0) {
/* 326 */       throw new IllegalArgumentException("Token start position must not be negative: " + from);
/*     */     }
/*     */ 
/*     */     
/* 330 */     int to = this.currentHeader.length();
/* 331 */     int end = from + 1;
/* 332 */     while (end < to && isTokenChar(this.currentHeader.charAt(end))) {
/* 333 */       end++;
/*     */     }
/*     */     
/* 336 */     return end;
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
/*     */   protected boolean isTokenSeparator(char ch) {
/* 352 */     return (ch == ',');
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
/*     */   protected boolean isWhitespace(char ch) {
/* 371 */     return (ch == '\t' || Character.isSpaceChar(ch));
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
/*     */   protected boolean isTokenChar(char ch) {
/* 390 */     if (Character.isLetterOrDigit(ch)) {
/* 391 */       return true;
/*     */     }
/*     */     
/* 394 */     if (Character.isISOControl(ch)) {
/* 395 */       return false;
/*     */     }
/*     */     
/* 398 */     if (isHttpSeparator(ch)) {
/* 399 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 407 */     return true;
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
/*     */   protected boolean isHttpSeparator(char ch) {
/* 422 */     return (" ,;=()<>@:\\\"/[]?{}\t".indexOf(ch) >= 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicTokenIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */