/*    */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*    */ 
/*    */ import com.sun.msv.grammar.Expression;
/*    */ import com.sun.msv.reader.GrammarReader;
/*    */ import com.sun.tools.xjc.ErrorReceiver;
/*    */ import com.sun.tools.xjc.reader.NameConverter;
/*    */ import com.sun.tools.xjc.reader.PackageTracker;
/*    */ import com.sun.tools.xjc.reader.annotator.AnnotatorController;
/*    */ import java.util.Vector;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotatorControllerImpl
/*    */   implements AnnotatorController
/*    */ {
/*    */   private final GrammarReader reader;
/*    */   private final PackageTracker tracker;
/*    */   private final ErrorReceiver errorReceiver;
/*    */   
/*    */   public AnnotatorControllerImpl(GrammarReader _reader, ErrorReceiver _errorReceiver, PackageTracker _tracker) {
/* 26 */     this.reader = _reader;
/* 27 */     this.tracker = _tracker;
/* 28 */     this.errorReceiver = _errorReceiver;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NameConverter getNameConverter() {
/* 36 */     return NameConverter.smart;
/*    */   }
/*    */   public PackageTracker getPackageTracker() {
/* 39 */     return this.tracker;
/*    */   }
/*    */ 
/*    */   
/*    */   public void reportError(Expression[] srcs, String msg) {
/* 44 */     Vector locs = new Vector();
/* 45 */     for (int i = 0; i < srcs.length; i++) {
/* 46 */       Locator loc = this.reader.getDeclaredLocationOf(srcs[i]);
/* 47 */       if (loc != null) {
/* 48 */         locs.add(loc);
/*    */       }
/*    */     } 
/* 51 */     this.reader.controller.error(locs.<Locator>toArray(new Locator[0]), msg, null);
/*    */   }
/*    */   
/*    */   public void reportError(Locator[] srcs, String msg) {
/* 55 */     this.reader.controller.error(srcs, msg, null);
/*    */   }
/*    */   
/*    */   public ErrorReceiver getErrorReceiver() {
/* 59 */     return this.errorReceiver;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\AnnotatorControllerImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */