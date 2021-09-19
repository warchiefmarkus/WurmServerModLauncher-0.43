/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HeaderElementIterator;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public class BasicHeaderElementIterator
/*     */   implements HeaderElementIterator
/*     */ {
/*     */   private final HeaderIterator headerIt;
/*     */   private final HeaderValueParser parser;
/*  51 */   private HeaderElement currentElement = null;
/*  52 */   private CharArrayBuffer buffer = null;
/*  53 */   private ParserCursor cursor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser parser) {
/*  61 */     if (headerIterator == null) {
/*  62 */       throw new IllegalArgumentException("Header iterator may not be null");
/*     */     }
/*  64 */     if (parser == null) {
/*  65 */       throw new IllegalArgumentException("Parser may not be null");
/*     */     }
/*  67 */     this.headerIt = headerIterator;
/*  68 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHeaderElementIterator(HeaderIterator headerIterator) {
/*  73 */     this(headerIterator, BasicHeaderValueParser.DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   private void bufferHeaderValue() {
/*  78 */     this.cursor = null;
/*  79 */     this.buffer = null;
/*  80 */     while (this.headerIt.hasNext()) {
/*  81 */       Header h = this.headerIt.nextHeader();
/*  82 */       if (h instanceof FormattedHeader) {
/*  83 */         this.buffer = ((FormattedHeader)h).getBuffer();
/*  84 */         this.cursor = new ParserCursor(0, this.buffer.length());
/*  85 */         this.cursor.updatePos(((FormattedHeader)h).getValuePos());
/*     */         break;
/*     */       } 
/*  88 */       String value = h.getValue();
/*  89 */       if (value != null) {
/*  90 */         this.buffer = new CharArrayBuffer(value.length());
/*  91 */         this.buffer.append(value);
/*  92 */         this.cursor = new ParserCursor(0, this.buffer.length());
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseNextElement() {
/* 101 */     while (this.headerIt.hasNext() || this.cursor != null) {
/* 102 */       if (this.cursor == null || this.cursor.atEnd())
/*     */       {
/* 104 */         bufferHeaderValue();
/*     */       }
/*     */       
/* 107 */       if (this.cursor != null) {
/*     */         
/* 109 */         while (!this.cursor.atEnd()) {
/* 110 */           HeaderElement e = this.parser.parseHeaderElement(this.buffer, this.cursor);
/* 111 */           if (e.getName().length() != 0 || e.getValue() != null) {
/*     */             
/* 113 */             this.currentElement = e;
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 118 */         if (this.cursor.atEnd()) {
/*     */           
/* 120 */           this.cursor = null;
/* 121 */           this.buffer = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/* 128 */     if (this.currentElement == null) {
/* 129 */       parseNextElement();
/*     */     }
/* 131 */     return (this.currentElement != null);
/*     */   }
/*     */   
/*     */   public HeaderElement nextElement() throws NoSuchElementException {
/* 135 */     if (this.currentElement == null) {
/* 136 */       parseNextElement();
/*     */     }
/*     */     
/* 139 */     if (this.currentElement == null) {
/* 140 */       throw new NoSuchElementException("No more header elements available");
/*     */     }
/*     */     
/* 143 */     HeaderElement element = this.currentElement;
/* 144 */     this.currentElement = null;
/* 145 */     return element;
/*     */   }
/*     */   
/*     */   public final Object next() throws NoSuchElementException {
/* 149 */     return nextElement();
/*     */   }
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 153 */     throw new UnsupportedOperationException("Remove not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHeaderElementIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */