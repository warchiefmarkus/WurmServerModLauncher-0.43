/*    */ package com.sun.xml.bind.v2.schemagen;
/*    */ 
/*    */ import com.sun.xml.bind.Util;
/*    */ import java.io.IOException;
/*    */ import java.util.logging.Logger;
/*    */ import javax.xml.bind.SchemaOutputResolver;
/*    */ import javax.xml.transform.Result;
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
/*    */ final class FoolProofResolver
/*    */   extends SchemaOutputResolver
/*    */ {
/* 56 */   private static final Logger logger = Util.getClassLogger();
/*    */   private final SchemaOutputResolver resolver;
/*    */   
/*    */   public FoolProofResolver(SchemaOutputResolver resolver) {
/* 60 */     assert resolver != null;
/* 61 */     this.resolver = resolver;
/*    */   }
/*    */   
/*    */   public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
/* 65 */     logger.entering(getClass().getName(), "createOutput", new Object[] { namespaceUri, suggestedFileName });
/* 66 */     Result r = this.resolver.createOutput(namespaceUri, suggestedFileName);
/* 67 */     if (r != null) {
/* 68 */       String sysId = r.getSystemId();
/* 69 */       logger.finer("system ID = " + sysId);
/* 70 */       if (sysId == null)
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 79 */         throw new AssertionError("system ID cannot be null"); } 
/*    */     } 
/* 81 */     logger.exiting(getClass().getName(), "createOutput", r);
/* 82 */     return r;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\FoolProofResolver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */