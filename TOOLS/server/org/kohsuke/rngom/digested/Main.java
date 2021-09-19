/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ import org.kohsuke.rngom.ast.builder.BuildException;
/*    */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*    */ import org.kohsuke.rngom.ast.util.CheckingSchemaBuilder;
/*    */ import org.kohsuke.rngom.parse.compact.CompactParseable;
/*    */ import org.kohsuke.rngom.parse.xml.SAXParseable;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/*    */     CompactParseable compactParseable;
/* 22 */     ErrorHandler eh = new DefaultHandler() {
/*    */         public void error(SAXParseException e) throws SAXException {
/* 24 */           throw e;
/*    */         }
/*    */       };
/*    */ 
/*    */     
/* 29 */     if (args[0].endsWith(".rng")) {
/* 30 */       SAXParseable sAXParseable = new SAXParseable(new InputSource(args[0]), eh);
/*    */     } else {
/* 32 */       compactParseable = new CompactParseable(new InputSource(args[0]), eh);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 38 */     CheckingSchemaBuilder checkingSchemaBuilder = new CheckingSchemaBuilder(new DSchemaBuilderImpl(), eh);
/*    */     
/*    */     try {
/* 41 */       compactParseable.parse((SchemaBuilder)checkingSchemaBuilder);
/* 42 */     } catch (BuildException e) {
/*    */ 
/*    */ 
/*    */       
/* 46 */       if (e.getCause() instanceof SAXException) {
/* 47 */         SAXException se = (SAXException)e.getCause();
/* 48 */         if (se.getException() != null)
/* 49 */           se.getException().printStackTrace(); 
/*    */       } 
/* 51 */       throw e;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\Main.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */