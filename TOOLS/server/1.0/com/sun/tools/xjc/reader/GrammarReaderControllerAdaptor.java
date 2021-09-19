/*    */ package 1.0.com.sun.tools.xjc.reader;
/*    */ 
/*    */ import com.sun.msv.reader.GrammarReaderController;
/*    */ import com.sun.tools.xjc.ErrorReceiver;
/*    */ import com.sun.tools.xjc.reader.Messages;
/*    */ import com.sun.tools.xjc.util.ErrorReceiverFilter;
/*    */ import java.io.IOException;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public class GrammarReaderControllerAdaptor
/*    */   extends ErrorReceiverFilter
/*    */   implements GrammarReaderController
/*    */ {
/*    */   private final EntityResolver entityResolver;
/*    */   
/*    */   public GrammarReaderControllerAdaptor(ErrorReceiver core, EntityResolver _entityResolver) {
/* 44 */     super(core);
/* 45 */     this.entityResolver = _entityResolver;
/*    */   }
/*    */   
/*    */   public void warning(Locator[] locs, String msg) {
/* 49 */     boolean firstTime = true;
/* 50 */     if (locs != null) {
/* 51 */       for (int i = 0; i < locs.length; i++) {
/* 52 */         if (locs[i] != null) {
/* 53 */           if (firstTime) {
/* 54 */             warning(locs[i], msg);
/*    */           } else {
/* 56 */             warning(locs[i], Messages.format("GrammarReaderControllerAdaptor.RelevantLocation"));
/* 57 */           }  firstTime = false;
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/* 62 */     if (firstTime)
/* 63 */       warning((Locator)null, msg); 
/*    */   }
/*    */   
/*    */   public void error(Locator[] locs, String msg, Exception e) {
/* 67 */     boolean firstTime = true;
/* 68 */     if (locs != null) {
/* 69 */       for (int i = 0; i < locs.length; i++) {
/* 70 */         if (locs[i] != null) {
/* 71 */           if (firstTime) {
/* 72 */             error(locs[i], msg);
/*    */           } else {
/* 74 */             error(locs[i], Messages.format("GrammarReaderControllerAdaptor.RelevantLocation"));
/* 75 */           }  firstTime = false;
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/* 80 */     if (firstTime)
/* 81 */       error((Locator)null, msg); 
/*    */   }
/*    */   
/*    */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 85 */     if (this.entityResolver == null) return null; 
/* 86 */     return this.entityResolver.resolveEntity(publicId, systemId);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\GrammarReaderControllerAdaptor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */