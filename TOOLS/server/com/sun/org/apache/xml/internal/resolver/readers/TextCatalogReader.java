/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextCatalogReader
/*     */   implements CatalogReader
/*     */ {
/*  49 */   protected InputStream catfile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   protected int[] stack = new int[3];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected Stack tokenStack = new Stack();
/*     */ 
/*     */   
/*  64 */   protected int top = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean caseSensitive = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean isCaseSensitive) {
/*  75 */     this.caseSensitive = isCaseSensitive;
/*     */   }
/*     */   
/*     */   public boolean getCaseSensitive() {
/*  79 */     return this.caseSensitive;
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
/*     */   public void readCatalog(Catalog catalog, String fileUrl) throws MalformedURLException, IOException {
/*  94 */     URL catURL = null;
/*     */     
/*     */     try {
/*  97 */       catURL = new URL(fileUrl);
/*  98 */     } catch (MalformedURLException e) {
/*  99 */       catURL = new URL("file:///" + fileUrl);
/*     */     } 
/*     */     
/* 102 */     URLConnection urlCon = catURL.openConnection();
/*     */     try {
/* 104 */       readCatalog(catalog, urlCon.getInputStream());
/* 105 */     } catch (FileNotFoundException e) {
/* 106 */       (catalog.getCatalogManager()).debug.message(1, "Failed to load catalog, file not found", catURL.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readCatalog(Catalog catalog, InputStream is) throws MalformedURLException, IOException {
/* 114 */     this.catfile = is;
/*     */     
/* 116 */     if (this.catfile == null) {
/*     */       return;
/*     */     }
/*     */     
/* 120 */     Vector<String> unknownEntry = null;
/*     */     
/*     */     try {
/*     */       while (true) {
/* 124 */         String token = nextToken();
/*     */         
/* 126 */         if (token == null) {
/* 127 */           if (unknownEntry != null) {
/* 128 */             catalog.unknownEntry(unknownEntry);
/* 129 */             unknownEntry = null;
/*     */           } 
/* 131 */           this.catfile.close();
/* 132 */           this.catfile = null;
/*     */           
/*     */           return;
/*     */         } 
/* 136 */         String entryToken = null;
/* 137 */         if (this.caseSensitive) {
/* 138 */           entryToken = token;
/*     */         } else {
/* 140 */           entryToken = token.toUpperCase();
/*     */         } 
/*     */         
/*     */         try {
/* 144 */           int type = CatalogEntry.getEntryType(entryToken);
/* 145 */           int numArgs = CatalogEntry.getEntryArgCount(type);
/* 146 */           Vector<String> args = new Vector();
/*     */           
/* 148 */           if (unknownEntry != null) {
/* 149 */             catalog.unknownEntry(unknownEntry);
/* 150 */             unknownEntry = null;
/*     */           } 
/*     */           
/* 153 */           for (int count = 0; count < numArgs; count++) {
/* 154 */             args.addElement(nextToken());
/*     */           }
/*     */           
/* 157 */           catalog.addEntry(new CatalogEntry(entryToken, args));
/* 158 */         } catch (CatalogException cex) {
/* 159 */           if (cex.getExceptionType() == 3) {
/* 160 */             if (unknownEntry == null) {
/* 161 */               unknownEntry = new Vector();
/*     */             }
/* 163 */             unknownEntry.addElement(token); continue;
/* 164 */           }  if (cex.getExceptionType() == 2) {
/* 165 */             (catalog.getCatalogManager()).debug.message(1, "Invalid catalog entry", token);
/* 166 */             unknownEntry = null; continue;
/* 167 */           }  if (cex.getExceptionType() == 8) {
/* 168 */             (catalog.getCatalogManager()).debug.message(1, cex.getMessage());
/*     */           }
/*     */         } 
/*     */       } 
/* 172 */     } catch (CatalogException cex2) {
/* 173 */       if (cex2.getExceptionType() == 8) {
/* 174 */         (catalog.getCatalogManager()).debug.message(1, cex2.getMessage());
/*     */       }
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() {
/* 185 */     if (this.catfile != null) {
/*     */       try {
/* 187 */         this.catfile.close();
/* 188 */       } catch (IOException e) {}
/*     */     }
/*     */ 
/*     */     
/* 192 */     this.catfile = null;
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
/*     */   protected String nextToken() throws IOException, CatalogException {
/*     */     int nextch;
/* 207 */     String token = "";
/*     */ 
/*     */     
/* 210 */     if (!this.tokenStack.empty()) {
/* 211 */       return this.tokenStack.pop();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 217 */       ch = this.catfile.read();
/* 218 */       while (ch <= 32) {
/* 219 */         ch = this.catfile.read();
/* 220 */         if (ch < 0) {
/* 221 */           return null;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 226 */       nextch = this.catfile.read();
/* 227 */       if (nextch < 0) {
/* 228 */         return null;
/*     */       }
/*     */       
/* 231 */       if (ch == 45 && nextch == 45) {
/*     */         
/* 233 */         ch = 32;
/* 234 */         nextch = nextChar();
/* 235 */         while ((ch != 45 || nextch != 45) && nextch > 0) {
/* 236 */           ch = nextch;
/* 237 */           nextch = nextChar();
/*     */         } 
/*     */         
/* 240 */         if (nextch < 0) {
/* 241 */           throw new CatalogException(8, "Unterminated comment in catalog file; EOF treated as end-of-comment.");
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 248 */     this.stack[++this.top] = nextch;
/* 249 */     this.stack[++this.top] = ch;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     int ch = nextChar();
/* 255 */     if (ch == 34 || ch == 39) {
/* 256 */       int quote = ch;
/* 257 */       while ((ch = nextChar()) != quote) {
/* 258 */         char[] chararr = new char[1];
/* 259 */         chararr[0] = (char)ch;
/* 260 */         String s = new String(chararr);
/* 261 */         token = token.concat(s);
/*     */       } 
/* 263 */       return token;
/*     */     } 
/*     */ 
/*     */     
/* 267 */     while (ch > 32) {
/* 268 */       nextch = nextChar();
/* 269 */       if (ch == 45 && nextch == 45) {
/* 270 */         this.stack[++this.top] = ch;
/* 271 */         this.stack[++this.top] = nextch;
/* 272 */         return token;
/*     */       } 
/* 274 */       char[] chararr = new char[1];
/* 275 */       chararr[0] = (char)ch;
/* 276 */       String s = new String(chararr);
/* 277 */       token = token.concat(s);
/* 278 */       ch = nextch;
/*     */     } 
/*     */     
/* 281 */     return token;
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
/*     */   protected int nextChar() throws IOException {
/* 294 */     if (this.top < 0) {
/* 295 */       return this.catfile.read();
/*     */     }
/* 297 */     return this.stack[this.top--];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\TextCatalogReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */