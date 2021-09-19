/*    */ package com.sun.tools.xjc.reader.dtd;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
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
/*    */ final class Block
/*    */ {
/*    */   final boolean isOptional;
/*    */   final boolean isRepeated;
/* 55 */   final Set<Element> elements = new LinkedHashSet<Element>();
/*    */   
/*    */   Block(boolean optional, boolean repeated) {
/* 58 */     this.isOptional = optional;
/* 59 */     this.isRepeated = repeated;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\Block.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */