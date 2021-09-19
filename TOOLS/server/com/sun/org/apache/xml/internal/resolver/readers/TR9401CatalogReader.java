/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TR9401CatalogReader
/*     */   extends TextCatalogReader
/*     */ {
/*     */   public void readCatalog(Catalog catalog, InputStream is) throws MalformedURLException, IOException {
/*  71 */     this.catfile = is;
/*     */     
/*  73 */     if (this.catfile == null) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     Vector<String> unknownEntry = null;
/*     */     
/*     */     try {
/*     */       while (true) {
/*  81 */         String token = nextToken();
/*     */         
/*  83 */         if (token == null) {
/*  84 */           if (unknownEntry != null) {
/*  85 */             catalog.unknownEntry(unknownEntry);
/*  86 */             unknownEntry = null;
/*     */           } 
/*  88 */           this.catfile.close();
/*  89 */           this.catfile = null;
/*     */           
/*     */           return;
/*     */         } 
/*  93 */         String entryToken = null;
/*  94 */         if (this.caseSensitive) {
/*  95 */           entryToken = token;
/*     */         } else {
/*  97 */           entryToken = token.toUpperCase();
/*     */         } 
/*     */         
/* 100 */         if (entryToken.equals("DELEGATE")) {
/* 101 */           entryToken = "DELEGATE_PUBLIC";
/*     */         }
/*     */         
/*     */         try {
/* 105 */           int type = CatalogEntry.getEntryType(entryToken);
/* 106 */           int numArgs = CatalogEntry.getEntryArgCount(type);
/* 107 */           Vector<String> args = new Vector();
/*     */           
/* 109 */           if (unknownEntry != null) {
/* 110 */             catalog.unknownEntry(unknownEntry);
/* 111 */             unknownEntry = null;
/*     */           } 
/*     */           
/* 114 */           for (int count = 0; count < numArgs; count++) {
/* 115 */             args.addElement(nextToken());
/*     */           }
/*     */           
/* 118 */           catalog.addEntry(new CatalogEntry(entryToken, args));
/* 119 */         } catch (CatalogException cex) {
/* 120 */           if (cex.getExceptionType() == 3) {
/* 121 */             if (unknownEntry == null) {
/* 122 */               unknownEntry = new Vector();
/*     */             }
/* 124 */             unknownEntry.addElement(token); continue;
/* 125 */           }  if (cex.getExceptionType() == 2) {
/* 126 */             (catalog.getCatalogManager()).debug.message(1, "Invalid catalog entry", token);
/* 127 */             unknownEntry = null; continue;
/* 128 */           }  if (cex.getExceptionType() == 8) {
/* 129 */             (catalog.getCatalogManager()).debug.message(1, cex.getMessage());
/*     */           }
/*     */         } 
/*     */       } 
/* 133 */     } catch (CatalogException cex2) {
/* 134 */       if (cex2.getExceptionType() == 8)
/* 135 */         (catalog.getCatalogManager()).debug.message(1, cex2.getMessage()); 
/*     */       return;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\TR9401CatalogReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */