/*    */ package com.sun.tools.xjc;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class ConsoleErrorReporter
/*    */   extends ErrorReceiver
/*    */ {
/*    */   private PrintStream output;
/*    */   private boolean hadError = false;
/*    */   
/*    */   public ConsoleErrorReporter(PrintStream out) {
/* 59 */     this.output = out;
/*    */   }
/*    */   public ConsoleErrorReporter(OutputStream out) {
/* 62 */     this(new PrintStream(out));
/*    */   } public ConsoleErrorReporter() {
/* 64 */     this(System.out);
/*    */   }
/*    */   public void warning(SAXParseException e) {
/* 67 */     print("Driver.WarningMessage", e);
/*    */   }
/*    */   
/*    */   public void error(SAXParseException e) {
/* 71 */     this.hadError = true;
/* 72 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException e) {
/* 76 */     this.hadError = true;
/* 77 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */   
/*    */   public void info(SAXParseException e) {
/* 81 */     print("Driver.InfoMessage", e);
/*    */   }
/*    */   
/*    */   public boolean hadError() {
/* 85 */     return this.hadError;
/*    */   }
/*    */   
/*    */   private void print(String resource, SAXParseException e) {
/* 89 */     this.output.println(Messages.format(resource, new Object[] { e.getMessage() }));
/* 90 */     this.output.println(getLocationString(e));
/* 91 */     this.output.println();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ConsoleErrorReporter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */