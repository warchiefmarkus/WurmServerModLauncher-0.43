/*    */ package com.sun.tools.xjc.api.util;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public final class ToolsJarNotFoundException
/*    */   extends Exception
/*    */ {
/*    */   public final File toolsJar;
/*    */   
/*    */   public ToolsJarNotFoundException(File toolsJar) {
/* 55 */     super(calcMessage(toolsJar));
/* 56 */     this.toolsJar = toolsJar;
/*    */   }
/*    */   
/*    */   private static String calcMessage(File toolsJar) {
/* 60 */     return Messages.TOOLS_JAR_NOT_FOUND.format(new Object[] { toolsJar.getPath() });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ap\\util\ToolsJarNotFoundException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */