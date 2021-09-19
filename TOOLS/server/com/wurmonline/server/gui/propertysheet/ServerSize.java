/*    */ package com.wurmonline.server.gui.propertysheet;
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
/*    */ enum ServerSize
/*    */ {
/* 29 */   SIZE_16(16),
/* 30 */   SIZE_32(32),
/* 31 */   SIZE_64(64),
/* 32 */   SIZE_128(128),
/* 33 */   SIZE_256(256),
/* 34 */   SIZE_512(512),
/* 35 */   SIZE_1024(1024),
/* 36 */   SIZE_2048(2048),
/* 37 */   SIZE_4096(4096),
/* 38 */   SIZE_8192(8192),
/* 39 */   SIZE_16384(16384);
/*    */ 
/*    */ 
/*    */   
/*    */   private final int size;
/*    */ 
/*    */ 
/*    */   
/*    */   ServerSize(int size) {
/* 48 */     this.size = size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getSize() {
/* 58 */     return this.size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return String.valueOf(this.size);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\propertysheet\ServerSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */