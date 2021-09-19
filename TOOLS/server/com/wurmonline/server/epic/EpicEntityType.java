/*    */ package com.wurmonline.server.epic;
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
/*    */ enum EpicEntityType
/*    */ {
/* 29 */   TYPE_DEITY(0),
/*    */   
/* 31 */   TYPE_SOURCE(1),
/*    */   
/* 33 */   TYPE_COLLECT(2),
/*    */   
/* 35 */   TYPE_WURM(4),
/*    */   
/* 37 */   TYPE_MONSTER_SENTINEL(5),
/*    */   
/* 39 */   TYPE_ALLY(6),
/*    */   
/* 41 */   TYPE_DEMIGOD(7);
/*    */   
/*    */   private final int code;
/*    */ 
/*    */   
/*    */   EpicEntityType(int aCode) {
/* 47 */     this.code = aCode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getCode() {
/* 57 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\EpicEntityType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */