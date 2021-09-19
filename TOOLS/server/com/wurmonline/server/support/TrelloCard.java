/*    */ package com.wurmonline.server.support;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TrelloCard
/*    */ {
/*    */   private final String trelloBoardId;
/*    */   private final String trelloListId;
/*    */   private final String trelloCardTitle;
/*    */   private final String trelloCardDescription;
/*    */   private final String trelloLabel;
/*    */   
/*    */   public TrelloCard(String boardId, String listId, String title, String description, String label) {
/* 44 */     this.trelloBoardId = boardId;
/* 45 */     this.trelloListId = listId;
/* 46 */     this.trelloCardTitle = title;
/* 47 */     this.trelloCardDescription = description;
/* 48 */     this.trelloLabel = label;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getBoardId() {
/* 53 */     return this.trelloBoardId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getListId() {
/* 58 */     return this.trelloListId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTitle() {
/* 63 */     return this.trelloCardTitle;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 68 */     return this.trelloCardDescription;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getLabel() {
/* 73 */     return this.trelloLabel;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\TrelloCard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */