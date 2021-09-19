/*     */ package org.seamless.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Pager
/*     */   implements Serializable
/*     */ {
/*  24 */   private Long numOfRecords = Long.valueOf(0L);
/*  25 */   private Integer page = Integer.valueOf(1);
/*  26 */   private Long pageSize = Long.valueOf(15L);
/*     */ 
/*     */   
/*     */   public Pager() {}
/*     */   
/*     */   public Pager(Long numOfRecords) {
/*  32 */     this.numOfRecords = numOfRecords;
/*     */   }
/*     */   
/*     */   public Pager(Long numOfRecords, Integer page) {
/*  36 */     this.numOfRecords = numOfRecords;
/*  37 */     this.page = page;
/*     */   }
/*     */   
/*     */   public Pager(Long numOfRecords, Integer page, Long pageSize) {
/*  41 */     this.numOfRecords = numOfRecords;
/*  42 */     this.page = page;
/*  43 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */   public Long getNumOfRecords() {
/*  47 */     return this.numOfRecords;
/*     */   }
/*     */   
/*     */   public void setNumOfRecords(Long numOfRecords) {
/*  51 */     this.numOfRecords = numOfRecords;
/*     */   }
/*     */   
/*     */   public Integer getPage() {
/*  55 */     return this.page;
/*     */   }
/*     */   
/*     */   public void setPage(Integer page) {
/*  59 */     if (page != null) this.page = page; 
/*     */   }
/*     */   
/*     */   public Long getPageSize() {
/*  63 */     return this.pageSize;
/*     */   }
/*     */   
/*     */   public void setPageSize(Long pageSize) {
/*  67 */     if (pageSize != null) this.pageSize = pageSize; 
/*     */   }
/*     */   
/*     */   public int getNextPage() {
/*  71 */     return this.page.intValue() + 1;
/*     */   }
/*     */   
/*     */   public int getPreviousPage() {
/*  75 */     return this.page.intValue() - 1;
/*     */   }
/*     */   
/*     */   public int getFirstPage() {
/*  79 */     return 1;
/*     */   }
/*     */   
/*     */   public long getIndexRangeBegin() {
/*  83 */     long retval = (getPage().intValue() - 1) * getPageSize().longValue();
/*  84 */     return Math.max(Math.min(getNumOfRecords().longValue() - 1L, (retval >= 0L) ? retval : 0L), 0L);
/*     */   }
/*     */   
/*     */   public long getIndexRangeEnd() {
/*  88 */     long firstIndex = getIndexRangeBegin();
/*  89 */     long pageIndex = getPageSize().longValue() - 1L;
/*  90 */     long lastIndex = getNumOfRecords().longValue() - 1L;
/*  91 */     return Math.min(firstIndex + pageIndex, lastIndex);
/*     */   }
/*     */   
/*     */   public long getLastPage() {
/*  95 */     long lastPage = this.numOfRecords.longValue() / this.pageSize.longValue();
/*  96 */     if (this.numOfRecords.longValue() % this.pageSize.longValue() == 0L) lastPage--; 
/*  97 */     return lastPage + 1L;
/*     */   }
/*     */   
/*     */   public boolean isPreviousPageAvailable() {
/* 101 */     return (getIndexRangeBegin() + 1L > getPageSize().longValue());
/*     */   }
/*     */   
/*     */   public boolean isNextPageAvailable() {
/* 105 */     return (this.numOfRecords.longValue() - 1L > getIndexRangeEnd());
/*     */   }
/*     */   
/*     */   public boolean isSeveralPages() {
/* 109 */     return (getNumOfRecords().longValue() != 0L && getNumOfRecords().longValue() > getPageSize().longValue());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 113 */     return "Pager - Records: " + getNumOfRecords() + " Page size: " + getPageSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\Pager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */