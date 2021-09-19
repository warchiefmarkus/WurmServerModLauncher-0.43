/*    */ package com.sun.javaws.exceptions;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TooManyArgumentsException
/*    */   extends JNLPException
/*    */ {
/*    */   private String[] _arguments;
/*    */   
/*    */   public TooManyArgumentsException(String[] paramArrayOfString) {
/* 14 */     super(ResourceManager.getString("launch.error.category.arguments"));
/* 15 */     this._arguments = paramArrayOfString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRealMessage() {
/* 20 */     StringBuffer stringBuffer = new StringBuffer("{");
/* 21 */     for (byte b = 0; b < this._arguments.length - 1; b++) {
/* 22 */       stringBuffer.append(this._arguments[b]);
/* 23 */       stringBuffer.append(", ");
/*    */     } 
/* 25 */     stringBuffer.append(this._arguments[this._arguments.length - 1]);
/* 26 */     stringBuffer.append(" }");
/*    */     
/* 28 */     return ResourceManager.getString("launch.error.toomanyargs", stringBuffer.toString());
/*    */   }
/*    */   
/*    */   public String getField() {
/* 32 */     return getMessage();
/*    */   }
/*    */   public String toString() {
/* 35 */     return "TooManyArgumentsException[ " + getRealMessage() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\exceptions\TooManyArgumentsException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */