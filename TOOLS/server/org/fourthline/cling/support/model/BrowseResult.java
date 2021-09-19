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
/*    */ public class BrowseResult
/*    */ {
/*    */   protected String result;
/*    */   protected UnsignedIntegerFourBytes count;
/*    */   protected UnsignedIntegerFourBytes totalMatches;
/*    */   protected UnsignedIntegerFourBytes containerUpdateID;
/*    */   
/*    */   public BrowseResult(String result, UnsignedIntegerFourBytes count, UnsignedIntegerFourBytes totalMatches, UnsignedIntegerFourBytes containerUpdateID) {
/* 34 */     this.result = result;
/* 35 */     this.count = count;
/* 36 */     this.totalMatches = totalMatches;
/* 37 */     this.containerUpdateID = containerUpdateID;
/*    */   }
/*    */   
/*    */   public BrowseResult(String result, long count, long totalMatches) {
/* 41 */     this(result, count, totalMatches, 0L);
/*    */   }
/*    */   
/*    */   public BrowseResult(String result, long count, long totalMatches, long updatedId) {
/* 45 */     this(result, new UnsignedIntegerFourBytes(count), new UnsignedIntegerFourBytes(totalMatches), new UnsignedIntegerFourBytes(updatedId));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResult() {
/* 54 */     return this.result;
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getCount() {
/* 58 */     return this.count;
/*    */   }
/*    */   
/*    */   public long getCountLong() {
/* 62 */     return this.count.getValue().longValue();
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getTotalMatches() {
/* 66 */     return this.totalMatches;
/*    */   }
/*    */   
/*    */   public long getTotalMatchesLong() {
/* 70 */     return this.totalMatches.getValue().longValue();
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getContainerUpdateID() {
/* 74 */     return this.containerUpdateID;
/*    */   }
/*    */   
/*    */   public long getContainerUpdateIDLong() {
/* 78 */     return this.containerUpdateID.getValue().longValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\BrowseResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */