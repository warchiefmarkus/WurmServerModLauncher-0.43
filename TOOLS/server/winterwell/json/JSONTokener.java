/*     */ package winterwell.json;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONTokener
/*     */ {
/*     */   private int myIndex;
/*     */   private String mySource;
/*     */   
/*     */   public JSONTokener(String s) {
/*  55 */     this.myIndex = 0;
/*  56 */     this.mySource = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void back() {
/*  66 */     if (this.myIndex > 0) {
/*  67 */       this.myIndex--;
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
/*     */   public static int dehexchar(char c) {
/*  80 */     if (c >= '0' && c <= '9') {
/*  81 */       return c - 48;
/*     */     }
/*  83 */     if (c >= 'A' && c <= 'F') {
/*  84 */       return c - 55;
/*     */     }
/*  86 */     if (c >= 'a' && c <= 'f') {
/*  87 */       return c - 87;
/*     */     }
/*  89 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean more() {
/*  99 */     return (this.myIndex < this.mySource.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char next() {
/* 109 */     if (more()) {
/* 110 */       char c = this.mySource.charAt(this.myIndex);
/* 111 */       this.myIndex++;
/* 112 */       return c;
/*     */     } 
/* 114 */     return Character.MIN_VALUE;
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
/*     */   public char next(char c) throws JSONException {
/* 126 */     char n = next();
/* 127 */     if (n != c) {
/* 128 */       throw syntaxError("Expected '" + c + "' and instead saw '" + 
/* 129 */           n + "'");
/*     */     }
/* 131 */     return n;
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
/*     */   public String next(int n) throws JSONException {
/* 145 */     int i = this.myIndex;
/* 146 */     int j = i + n;
/* 147 */     if (j >= this.mySource.length()) {
/* 148 */       throw syntaxError("Substring bounds error");
/*     */     }
/* 150 */     this.myIndex += n;
/* 151 */     return this.mySource.substring(i, j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char nextClean() throws JSONException {
/*     */     char c;
/*     */     label38: 
/* 163 */     do { c = next();
/* 164 */       if (c == '/') {
/* 165 */         switch (next()) {
/*     */           case '/':
/*     */             while (true) {
/* 168 */               c = next();
/* 169 */               if (c != '\n') { if (c != '\r') { if (c == '\000')
/*     */                     continue label38;  continue; }  continue label38; }  continue label38;
/*     */             } 
/*     */           case '*':
/* 173 */             while (true) { c = next();
/* 174 */               if (c == '\000') {
/* 175 */                 throw syntaxError("Unclosed comment");
/*     */               }
/* 177 */               if (c == '*') {
/* 178 */                 if (next() == '/') {
/*     */                   continue label38;
/*     */                 }
/* 181 */                 back();
/*     */               }  }
/*     */           
/*     */         } 
/*     */         
/* 186 */         back();
/* 187 */         return '/';
/*     */       } 
/* 189 */       if (c != '#')
/*     */         continue;  while (true)
/* 191 */       { c = next();
/* 192 */         if (c != '\n') { if (c != '\r') { if (c == '\000')
/* 193 */               continue label38;  continue; }  continue label38; }  continue label38; }  } while (c != '\000' && c <= ' ');
/* 194 */     return c;
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
/*     */   public String nextString(char quote) throws JSONException {
/* 213 */     StringBuffer sb = new StringBuffer();
/*     */     while (true) {
/* 215 */       char c = next();
/* 216 */       switch (c) {
/*     */         case '\000':
/*     */         case '\n':
/*     */         case '\r':
/* 220 */           throw syntaxError("Unterminated string");
/*     */         case '\\':
/* 222 */           c = next();
/* 223 */           switch (c) {
/*     */             case 'b':
/* 225 */               sb.append('\b');
/*     */               continue;
/*     */             case 't':
/* 228 */               sb.append('\t');
/*     */               continue;
/*     */             case 'n':
/* 231 */               sb.append('\n');
/*     */               continue;
/*     */             case 'f':
/* 234 */               sb.append('\f');
/*     */               continue;
/*     */             case 'r':
/* 237 */               sb.append('\r');
/*     */               continue;
/*     */             case 'u':
/* 240 */               sb.append((char)Integer.parseInt(next(4), 16));
/*     */               continue;
/*     */             case 'x':
/* 243 */               sb.append((char)Integer.parseInt(next(2), 16));
/*     */               continue;
/*     */           } 
/* 246 */           sb.append(c);
/*     */           continue;
/*     */       } 
/*     */       
/* 250 */       if (c == quote) {
/* 251 */         return sb.toString();
/*     */       }
/* 253 */       sb.append(c);
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
/*     */   public String nextTo(char d) {
/* 266 */     StringBuffer sb = new StringBuffer();
/*     */     while (true) {
/* 268 */       char c = next();
/* 269 */       if (c == d || c == '\000' || c == '\n' || c == '\r') {
/* 270 */         if (c != '\000') {
/* 271 */           back();
/*     */         }
/* 273 */         return sb.toString().trim();
/*     */       } 
/* 275 */       sb.append(c);
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
/*     */   public String nextTo(String delimiters) {
/* 288 */     StringBuffer sb = new StringBuffer();
/*     */     while (true) {
/* 290 */       char c = next();
/* 291 */       if (delimiters.indexOf(c) >= 0 || c == '\000' || 
/* 292 */         c == '\n' || c == '\r') {
/* 293 */         if (c != '\000') {
/* 294 */           back();
/*     */         }
/* 296 */         return sb.toString().trim();
/*     */       } 
/* 298 */       sb.append(c);
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
/*     */   public Object nextValue() throws JSONException {
/* 311 */     char c = nextClean();
/*     */ 
/*     */     
/* 314 */     switch (c) {
/*     */       case '"':
/*     */       case '\'':
/* 317 */         return nextString(c);
/*     */       case '{':
/* 319 */         back();
/* 320 */         return new JSONObject(this);
/*     */       case '[':
/* 322 */         back();
/* 323 */         return new JSONArray(this);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 335 */     StringBuffer sb = new StringBuffer();
/* 336 */     char b = c;
/* 337 */     while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
/* 338 */       sb.append(c);
/* 339 */       c = next();
/*     */     } 
/* 341 */     back();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 347 */     String s = sb.toString().trim();
/* 348 */     if (s.equals("")) {
/* 349 */       throw syntaxError("Missing value");
/*     */     }
/* 351 */     if (s.equalsIgnoreCase("true")) {
/* 352 */       return Boolean.TRUE;
/*     */     }
/* 354 */     if (s.equalsIgnoreCase("false")) {
/* 355 */       return Boolean.FALSE;
/*     */     }
/* 357 */     if (s.equalsIgnoreCase("null")) {
/* 358 */       return JSONObject.NULL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 369 */     if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
/* 370 */       if (b == '0') {
/* 371 */         if (s.length() > 2 && (
/* 372 */           s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
/*     */           try {
/* 374 */             return new Integer(Integer.parseInt(s.substring(2), 
/* 375 */                   16));
/* 376 */           } catch (Exception exception) {}
/*     */         } else {
/*     */ 
/*     */           
/*     */           try {
/* 381 */             return new Integer(Integer.parseInt(s, 8));
/* 382 */           } catch (Exception exception) {}
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 388 */         return new Integer(s);
/* 389 */       } catch (Exception e) {
/*     */         try {
/* 391 */           return new Long(s);
/* 392 */         } catch (Exception f) {
/*     */           try {
/* 394 */             return new Double(s);
/* 395 */           } catch (Exception g) {
/* 396 */             return s;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 401 */     return s;
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
/*     */   public char skipTo(char to) {
/* 414 */     int index = this.myIndex;
/*     */     while (true) {
/* 416 */       char c = next();
/* 417 */       if (c == '\000') {
/* 418 */         this.myIndex = index;
/* 419 */         return c;
/*     */       } 
/* 421 */       if (c == to) {
/* 422 */         back();
/* 423 */         return c;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean skipPast(String to) {
/* 433 */     this.myIndex = this.mySource.indexOf(to, this.myIndex);
/* 434 */     if (this.myIndex < 0) {
/* 435 */       this.myIndex = this.mySource.length();
/* 436 */       return false;
/*     */     } 
/* 438 */     this.myIndex += to.length();
/* 439 */     return true;
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
/*     */   public JSONException syntaxError(String message) {
/* 451 */     return new JSONException(String.valueOf(message) + toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 462 */     return " at character " + this.myIndex + " of " + this.mySource;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\winterwell\json\JSONTokener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */