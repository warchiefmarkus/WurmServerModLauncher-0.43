/*    */ package com.sun.codemodel.fmt;
/*    */ 
/*    */ import com.sun.codemodel.JResourceFile;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.Properties;
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
/*    */ public class JPropertyFile
/*    */   extends JResourceFile
/*    */ {
/*    */   private final Properties data;
/*    */   
/*    */   public JPropertyFile(String name) {
/* 34 */     super(name);
/*    */ 
/*    */     
/* 37 */     this.data = new Properties();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(String key, String value) {
/* 45 */     this.data.put(key, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void build(OutputStream out) throws IOException {
/* 53 */     this.data.store(out, (String)null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\fmt\JPropertyFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */