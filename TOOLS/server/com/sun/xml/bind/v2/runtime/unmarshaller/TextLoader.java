/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import com.sun.xml.bind.v2.runtime.Transducer;
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
/*    */ public class TextLoader
/*    */   extends Loader
/*    */ {
/*    */   private final Transducer xducer;
/*    */   
/*    */   public TextLoader(Transducer xducer) {
/* 59 */     super(true);
/* 60 */     this.xducer = xducer;
/*    */   }
/*    */   
/*    */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/*    */     try {
/* 65 */       state.target = this.xducer.parse(text);
/* 66 */     } catch (AccessorException e) {
/* 67 */       handleGenericException((Exception)e, true);
/* 68 */     } catch (RuntimeException e) {
/* 69 */       handleParseConversionException(state, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\TextLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */