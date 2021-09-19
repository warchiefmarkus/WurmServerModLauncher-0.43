/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BasicListHeaderIterator
/*     */   implements HeaderIterator
/*     */ {
/*     */   protected final List<Header> allHeaders;
/*     */   protected int currentIndex;
/*     */   protected int lastIndex;
/*     */   protected String headerName;
/*     */   
/*     */   public BasicListHeaderIterator(List<Header> headers, String name) {
/*  83 */     if (headers == null) {
/*  84 */       throw new IllegalArgumentException("Header list must not be null.");
/*     */     }
/*     */ 
/*     */     
/*  88 */     this.allHeaders = headers;
/*  89 */     this.headerName = name;
/*  90 */     this.currentIndex = findNext(-1);
/*  91 */     this.lastIndex = -1;
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
/* 105 */     if (from < -1) {
/* 106 */       return -1;
/*     */     }
/* 108 */     int to = this.allHeaders.size() - 1;
/* 109 */     boolean found = false;
/* 110 */     while (!found && from < to) {
/* 111 */       from++;
/* 112 */       found = filterHeader(from);
/*     */     } 
/* 114 */     return found ? from : -1;
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
/* 127 */     if (this.headerName == null) {
/* 128 */       return true;
/*     */     }
/*     */     
/* 131 */     String name = ((Header)this.allHeaders.get(index)).getName();
/*     */     
/* 133 */     return this.headerName.equalsIgnoreCase(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 139 */     return (this.currentIndex >= 0);
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
/* 153 */     int current = this.currentIndex;
/* 154 */     if (current < 0) {
/* 155 */       throw new NoSuchElementException("Iteration already finished.");
/*     */     }
/*     */     
/* 158 */     this.lastIndex = current;
/* 159 */     this.currentIndex = findNext(current);
/*     */     
/* 161 */     return this.allHeaders.get(current);
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
/* 175 */     return nextHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 185 */     if (this.lastIndex < 0) {
/* 186 */       throw new IllegalStateException("No header to remove.");
/*     */     }
/* 188 */     this.allHeaders.remove(this.lastIndex);
/* 189 */     this.lastIndex = -1;
/* 190 */     this.currentIndex--;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicListHeaderIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */