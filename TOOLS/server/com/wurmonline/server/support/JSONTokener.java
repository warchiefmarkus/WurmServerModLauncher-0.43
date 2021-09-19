/*     */ package com.wurmonline.server.support;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   private long character;
/*     */   private boolean eof;
/*     */   private long index;
/*     */   private long line;
/*     */   private char previous;
/*     */   private Reader reader;
/*     */   private boolean usePrevious;
/*     */   
/*     */   public JSONTokener(Reader aReader) {
/*  82 */     this.reader = aReader.markSupported() ? aReader : new BufferedReader(aReader);
/*     */ 
/*     */     
/*  85 */     this.eof = false;
/*  86 */     this.usePrevious = false;
/*  87 */     this.previous = Character.MIN_VALUE;
/*  88 */     this.index = 0L;
/*  89 */     this.character = 1L;
/*  90 */     this.line = 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONTokener(InputStream inputStream) throws JSONException {
/*  98 */     this(new InputStreamReader(inputStream));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONTokener(String s) {
/* 108 */     this(new StringReader(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void back() throws JSONException {
/* 118 */     if (this.usePrevious || this.index <= 0L)
/*     */     {
/* 120 */       throw new JSONException("Stepping back two steps is not supported");
/*     */     }
/* 122 */     this.index--;
/* 123 */     this.character--;
/* 124 */     this.usePrevious = true;
/* 125 */     this.eof = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int dehexchar(char c) {
/* 136 */     if (c >= '0' && c <= '9')
/*     */     {
/* 138 */       return c - 48;
/*     */     }
/* 140 */     if (c >= 'A' && c <= 'F')
/*     */     {
/* 142 */       return c - 55;
/*     */     }
/* 144 */     if (c >= 'a' && c <= 'f')
/*     */     {
/* 146 */       return c - 87;
/*     */     }
/* 148 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean end() {
/* 153 */     return (this.eof && !this.usePrevious);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean more() throws JSONException {
/* 163 */     next();
/* 164 */     if (end())
/*     */     {
/* 166 */       return false;
/*     */     }
/* 168 */     back();
/* 169 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char next() throws JSONException {
/*     */     int c;
/* 180 */     if (this.usePrevious) {
/*     */       
/* 182 */       this.usePrevious = false;
/* 183 */       c = this.previous;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 189 */         c = this.reader.read();
/*     */       }
/* 191 */       catch (IOException exception) {
/*     */         
/* 193 */         throw new JSONException(exception);
/*     */       } 
/*     */       
/* 196 */       if (c <= 0) {
/*     */         
/* 198 */         this.eof = true;
/* 199 */         c = 0;
/*     */       } 
/*     */     } 
/* 202 */     this.index++;
/* 203 */     if (this.previous == '\r') {
/*     */       
/* 205 */       this.line++;
/* 206 */       this.character = (c == 10) ? 0L : 1L;
/*     */     }
/* 208 */     else if (c == 10) {
/*     */       
/* 210 */       this.line++;
/* 211 */       this.character = 0L;
/*     */     }
/*     */     else {
/*     */       
/* 215 */       this.character++;
/*     */     } 
/* 217 */     this.previous = (char)c;
/* 218 */     return this.previous;
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
/* 230 */     char n = next();
/* 231 */     if (n != c)
/*     */     {
/* 233 */       throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
/*     */     }
/*     */     
/* 236 */     return n;
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
/* 250 */     if (n == 0)
/*     */     {
/* 252 */       return "";
/*     */     }
/*     */     
/* 255 */     char[] chars = new char[n];
/* 256 */     int pos = 0;
/*     */     
/* 258 */     while (pos < n) {
/*     */       
/* 260 */       chars[pos] = next();
/* 261 */       if (end())
/*     */       {
/* 263 */         throw syntaxError("Substring bounds error");
/*     */       }
/* 265 */       pos++;
/*     */     } 
/* 267 */     return new String(chars);
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
/*     */     do {
/* 279 */       c = next();
/* 280 */     } while (c != '\000' && c <= ' ');
/*     */     
/* 282 */     return c;
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
/* 301 */     StringBuffer sb = new StringBuffer();
/*     */     
/*     */     while (true) {
/* 304 */       char c = next();
/* 305 */       switch (c) {
/*     */         
/*     */         case '\000':
/*     */         case '\n':
/*     */         case '\r':
/* 310 */           throw syntaxError("Unterminated string");
/*     */         case '\\':
/* 312 */           c = next();
/* 313 */           switch (c) {
/*     */             
/*     */             case 'b':
/* 316 */               sb.append('\b');
/*     */               continue;
/*     */             case 't':
/* 319 */               sb.append('\t');
/*     */               continue;
/*     */             case 'n':
/* 322 */               sb.append('\n');
/*     */               continue;
/*     */             case 'f':
/* 325 */               sb.append('\f');
/*     */               continue;
/*     */             case 'r':
/* 328 */               sb.append('\r');
/*     */               continue;
/*     */             case 'u':
/* 331 */               sb.append((char)Integer.parseInt(next(4), 16));
/*     */               continue;
/*     */             case '"':
/*     */             case '\'':
/*     */             case '/':
/*     */             case '\\':
/* 337 */               sb.append(c);
/*     */               continue;
/*     */           } 
/* 340 */           throw syntaxError("Illegal escape.");
/*     */       } 
/*     */ 
/*     */       
/* 344 */       if (c == quote)
/*     */       {
/* 346 */         return sb.toString();
/*     */       }
/* 348 */       sb.append(c);
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
/*     */   public String nextTo(char delimiter) throws JSONException {
/* 361 */     StringBuffer sb = new StringBuffer();
/*     */     
/*     */     while (true) {
/* 364 */       char c = next();
/* 365 */       if (c == delimiter || c == '\000' || c == '\n' || c == '\r') {
/*     */         
/* 367 */         if (c != '\000')
/*     */         {
/* 369 */           back();
/*     */         }
/* 371 */         return sb.toString().trim();
/*     */       } 
/* 373 */       sb.append(c);
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
/*     */   public String nextTo(String delimiters) throws JSONException {
/* 386 */     StringBuffer sb = new StringBuffer();
/*     */     
/*     */     while (true) {
/* 389 */       char c = next();
/* 390 */       if (delimiters.indexOf(c) >= 0 || c == '\000' || c == '\n' || c == '\r') {
/*     */ 
/*     */         
/* 393 */         if (c != '\000')
/*     */         {
/* 395 */           back();
/*     */         }
/* 397 */         return sb.toString().trim();
/*     */       } 
/* 399 */       sb.append(c);
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
/* 412 */     char c = nextClean();
/*     */ 
/*     */     
/* 415 */     switch (c) {
/*     */       
/*     */       case '"':
/*     */       case '\'':
/* 419 */         return nextString(c);
/*     */       case '{':
/* 421 */         back();
/* 422 */         return new JSONObject(this);
/*     */       case '[':
/* 424 */         back();
/* 425 */         return new JSONArray(this);
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
/*     */ 
/*     */     
/* 439 */     StringBuffer sb = new StringBuffer();
/* 440 */     while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
/*     */       
/* 442 */       sb.append(c);
/* 443 */       c = next();
/*     */     } 
/* 445 */     back();
/*     */     
/* 447 */     String string = sb.toString().trim();
/* 448 */     if ("".equals(string))
/*     */     {
/* 450 */       throw syntaxError("Missing value");
/*     */     }
/* 452 */     return JSONObject.stringToValue(string);
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
/*     */   public char skipTo(char to) throws JSONException {
/*     */     char c;
/*     */     try {
/* 467 */       long startIndex = this.index;
/* 468 */       long startCharacter = this.character;
/* 469 */       long startLine = this.line;
/* 470 */       this.reader.mark(1000000);
/*     */       
/*     */       do {
/* 473 */         c = next();
/* 474 */         if (c == '\000') {
/*     */           
/* 476 */           this.reader.reset();
/* 477 */           this.index = startIndex;
/* 478 */           this.character = startCharacter;
/* 479 */           this.line = startLine;
/* 480 */           return c;
/*     */         } 
/* 482 */       } while (c != to);
/*     */     }
/* 484 */     catch (IOException exc) {
/*     */       
/* 486 */       throw new JSONException(exc);
/*     */     } 
/*     */     
/* 489 */     back();
/* 490 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONException syntaxError(String message) {
/* 501 */     return new JSONException(message + toString());
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
/* 512 */     return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\JSONTokener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */