/*    */ package 1.0.com.sun.tools.xjc;
/*    */ 
/*    */ import com.sun.tools.xjc.ErrorReceiver;
/*    */ import com.sun.tools.xjc.Messages;
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
/*    */ public class ConsoleErrorReporter
/*    */   extends ErrorReceiver
/*    */ {
/*    */   private PrintStream output;
/*    */   private boolean supressWarnings;
/*    */   private boolean supressInfo;
/*    */   
/*    */   public ConsoleErrorReporter(PrintStream out, boolean supressInfo, boolean supressWarnings) {
/* 36 */     this.output = out;
/* 37 */     this.supressInfo = supressInfo;
/* 38 */     this.supressWarnings = supressWarnings;
/*    */   }
/*    */   public ConsoleErrorReporter(OutputStream out, boolean supressInfo, boolean supressWarnings) {
/* 41 */     this(new PrintStream(out), supressInfo, supressWarnings);
/*    */   } public ConsoleErrorReporter() {
/* 43 */     this(System.out, true, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void supressInfoOutput() {
/* 49 */     this.supressInfo = true;
/*    */   }
/*    */   
/*    */   public void supressWarnings() {
/* 53 */     this.supressWarnings = true;
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException e) {
/* 57 */     if (this.supressWarnings)
/* 58 */       return;  print("Driver.WarningMessage", e);
/*    */   }
/*    */   
/*    */   public void error(SAXParseException e) {
/* 62 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException e) {
/* 66 */     print("Driver.ErrorMessage", e);
/*    */   }
/*    */   
/*    */   public void info(SAXParseException e) {
/* 70 */     if (this.supressInfo)
/* 71 */       return;  print("Driver.InfoMessage", e);
/*    */   }
/*    */   
/*    */   private void print(String resource, SAXParseException e) {
/* 75 */     this.output.println(Messages.format(resource, e.getMessage()));
/* 76 */     this.output.println(getLocationString(e));
/* 77 */     this.output.println();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\ConsoleErrorReporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */