/*    */ package com.sun.tools.xjc;
/*    */ 
/*    */ import com.sun.istack.tools.ProtectedTask;
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XJCTask
/*    */   extends ProtectedTask
/*    */ {
/* 64 */   private String source = "2.0";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSource(String version) {
/* 70 */     if (version.equals("1.0") || version.equals("2.0")) {
/* 71 */       this.source = version;
/*    */       return;
/*    */     } 
/* 74 */     throw new BuildException("Illegal version " + version);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ClassLoader createClassLoader() throws ClassNotFoundException, IOException {
/* 79 */     return ClassLoaderBuilder.createProtectiveClassLoader(XJCTask.class.getClassLoader(), this.source);
/*    */   }
/*    */   
/*    */   protected String getCoreClassName() {
/* 83 */     if (this.source.equals("2.0")) {
/* 84 */       return "com.sun.tools.xjc.XJC2Task";
/*    */     }
/* 86 */     return "com.sun.tools.xjc.XJCTask";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\XJCTask.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */