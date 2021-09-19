/*    */ package org.fourthline.cling.controlpoint.event;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.MXHeader;
/*    */ import org.fourthline.cling.model.message.header.STAllHeader;
/*    */ import org.fourthline.cling.model.message.header.UpnpHeader;
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
/*    */ public class Search
/*    */ {
/* 27 */   protected UpnpHeader searchType = (UpnpHeader)new STAllHeader();
/* 28 */   protected int mxSeconds = MXHeader.DEFAULT_VALUE.intValue();
/*    */ 
/*    */   
/*    */   public Search() {}
/*    */   
/*    */   public Search(UpnpHeader searchType) {
/* 34 */     this.searchType = searchType;
/*    */   }
/*    */   
/*    */   public Search(UpnpHeader searchType, int mxSeconds) {
/* 38 */     this.searchType = searchType;
/* 39 */     this.mxSeconds = mxSeconds;
/*    */   }
/*    */   
/*    */   public Search(int mxSeconds) {
/* 43 */     this.mxSeconds = mxSeconds;
/*    */   }
/*    */   
/*    */   public UpnpHeader getSearchType() {
/* 47 */     return this.searchType;
/*    */   }
/*    */   
/*    */   public int getMxSeconds() {
/* 51 */     return this.mxSeconds;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\controlpoint\event\Search.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */