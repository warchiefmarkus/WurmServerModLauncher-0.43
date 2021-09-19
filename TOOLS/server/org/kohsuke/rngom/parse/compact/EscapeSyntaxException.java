/*    */ package org.kohsuke.rngom.parse.compact;
/*    */ 
/*    */ class EscapeSyntaxException extends RuntimeException {
/*    */   private final String key;
/*    */   private final int lineNumber;
/*    */   private final int columnNumber;
/*    */   
/*    */   EscapeSyntaxException(String key, int lineNumber, int columnNumber) {
/*  9 */     this.key = key;
/* 10 */     this.lineNumber = lineNumber;
/* 11 */     this.columnNumber = columnNumber;
/*    */   }
/*    */   
/*    */   String getKey() {
/* 15 */     return this.key;
/*    */   }
/*    */   
/*    */   int getLineNumber() {
/* 19 */     return this.lineNumber;
/*    */   }
/*    */   
/*    */   int getColumnNumber() {
/* 23 */     return this.columnNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\parse\compact\EscapeSyntaxException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */