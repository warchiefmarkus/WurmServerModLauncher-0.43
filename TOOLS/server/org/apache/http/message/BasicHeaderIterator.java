/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
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
/*     */ @NotThreadSafe
/*     */ public class BasicHeaderIterator
/*     */   implements HeaderIterator
/*     */ {
/*     */   protected final Header[] allHeaders;
/*     */   protected int currentIndex;
/*     */   protected String headerName;
/*     */   
/*     */   public BasicHeaderIterator(Header[] headers, String name) {
/*  76 */     if (headers == null) {
/*  77 */       throw new IllegalArgumentException("Header array must not be null.");
/*     */     }
/*     */ 
/*     */     
/*  81 */     this.allHeaders = headers;
/*  82 */     this.headerName = name;
/*  83 */     this.currentIndex = findNext(-1);
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
/*     */   protected int findNext(int from) {
/*  97 */     if (from < -1) {
/*  98 */       return -1;
/*     */     }
/* 100 */     int to = this.allHeaders.length - 1;
/* 101 */     boolean found = false;
/* 102 */     while (!found && from < to) {
/* 103 */       from++;
/* 104 */       found = filterHeader(from);
/*     */     } 
/* 106 */     return found ? from : -1;
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
/*     */   protected boolean filterHeader(int index) {
/* 119 */     return (this.headerName == null || this.headerName.equalsIgnoreCase(this.allHeaders[index].getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 126 */     return (this.currentIndex >= 0);
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
/*     */   public Header nextHeader() throws NoSuchElementException {
/* 140 */     int current = this.currentIndex;
/* 141 */     if (current < 0) {
/* 142 */       throw new NoSuchElementException("Iteration already finished.");
/*     */     }
/*     */     
/* 145 */     this.currentIndex = findNext(current);
/*     */     
/* 147 */     return this.allHeaders[current];
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
/*     */   public final Object next() throws NoSuchElementException {
/* 161 */     return nextHeader();
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
/*     */   public void remove() throws UnsupportedOperationException {
/* 173 */     throw new UnsupportedOperationException("Removing headers is not supported.");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHeaderIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */