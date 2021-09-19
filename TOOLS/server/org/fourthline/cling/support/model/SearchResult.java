/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SearchResult
/*    */ {
/*    */   protected String result;
/*    */   protected UnsignedIntegerFourBytes count;
/*    */   protected UnsignedIntegerFourBytes totalMatches;
/*    */   protected UnsignedIntegerFourBytes containerUpdateID;
/*    */   
/*    */   public SearchResult(String result, UnsignedIntegerFourBytes count, UnsignedIntegerFourBytes totalMatches, UnsignedIntegerFourBytes containerUpdateID) {
/* 36 */     this.result = result;
/* 37 */     this.count = count;
/* 38 */     this.totalMatches = totalMatches;
/* 39 */     this.containerUpdateID = containerUpdateID;
/*    */   }
/*    */   
/*    */   public SearchResult(String result, long count, long totalMatches) {
/* 43 */     this(result, count, totalMatches, 0L);
/*    */   }
/*    */   
/*    */   public SearchResult(String result, long count, long totalMatches, long updateID) {
/* 47 */     this(result, new UnsignedIntegerFourBytes(count), new UnsignedIntegerFourBytes(totalMatches), new UnsignedIntegerFourBytes(updateID));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResult() {
/* 56 */     return this.result;
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getCount() {
/* 60 */     return this.count;
/*    */   }
/*    */   
/*    */   public long getCountLong() {
/* 64 */     return this.count.getValue().longValue();
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getTotalMatches() {
/* 68 */     return this.totalMatches;
/*    */   }
/*    */   
/*    */   public long getTotalMatchesLong() {
/* 72 */     return this.totalMatches.getValue().longValue();
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getContainerUpdateID() {
/* 76 */     return this.containerUpdateID;
/*    */   }
/*    */   
/*    */   public long getContainerUpdateIDLong() {
/* 80 */     return this.containerUpdateID.getValue().longValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\SearchResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */