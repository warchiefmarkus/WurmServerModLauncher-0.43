/*    */ package com.sun.tools.xjc.reader.dtd;
/*    */ 
/*    */ import java.util.List;
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
/*    */ abstract class Term
/*    */ {
/* 58 */   static final Term EMPTY = new Term()
/*    */     {
/*    */       void normalize(List<Block> r, boolean optional) {}
/*    */ 
/*    */       
/*    */       void addAllElements(Block b) {}
/*    */       
/*    */       boolean isOptional() {
/* 66 */         return false;
/*    */       }
/*    */       
/*    */       boolean isRepeated() {
/* 70 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   abstract void normalize(List<Block> paramList, boolean paramBoolean);
/*    */   
/*    */   abstract void addAllElements(Block paramBlock);
/*    */   
/*    */   abstract boolean isOptional();
/*    */   
/*    */   abstract boolean isRepeated();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\Term.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */