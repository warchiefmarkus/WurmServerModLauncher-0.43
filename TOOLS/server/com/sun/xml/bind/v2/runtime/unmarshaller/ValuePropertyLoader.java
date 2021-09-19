/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import com.sun.xml.bind.v2.runtime.reflect.TransducedAccessor;
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
/*    */ public class ValuePropertyLoader
/*    */   extends Loader
/*    */ {
/*    */   private final TransducedAccessor xacc;
/*    */   
/*    */   public ValuePropertyLoader(TransducedAccessor xacc) {
/* 55 */     super(true);
/* 56 */     this.xacc = xacc;
/*    */   }
/*    */   
/*    */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
/*    */     try {
/* 61 */       this.xacc.parse(state.target, text);
/* 62 */     } catch (AccessorException e) {
/* 63 */       handleGenericException((Exception)e, true);
/* 64 */     } catch (RuntimeException e) {
/* 65 */       handleParseConversionException(state, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\ValuePropertyLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */