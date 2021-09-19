/*    */ package com.google.common.jimfs;
/*    */ 
/*    */ import java.util.Arrays;
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
/*    */ final class InternalCharMatcher
/*    */ {
/*    */   private final char[] chars;
/*    */   
/*    */   public static InternalCharMatcher anyOf(String chars) {
/* 29 */     return new InternalCharMatcher(chars);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private InternalCharMatcher(String chars) {
/* 35 */     this.chars = chars.toCharArray();
/* 36 */     Arrays.sort(this.chars);
/*    */   }
/*    */   
/*    */   public boolean matches(char c) {
/* 40 */     return (Arrays.binarySearch(this.chars, c) >= 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\common\jimfs\InternalCharMatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */