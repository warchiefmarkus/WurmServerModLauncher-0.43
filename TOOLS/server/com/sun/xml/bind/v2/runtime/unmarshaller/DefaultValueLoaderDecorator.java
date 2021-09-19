/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
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
/*    */ public final class DefaultValueLoaderDecorator
/*    */   extends Loader
/*    */ {
/*    */   private final Loader l;
/*    */   private final String defaultValue;
/*    */   
/*    */   public DefaultValueLoaderDecorator(Loader l, String defaultValue) {
/* 51 */     this.l = l;
/* 52 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 58 */     if (state.elementDefaultValue == null) {
/* 59 */       state.elementDefaultValue = this.defaultValue;
/*    */     }
/* 61 */     state.loader = this.l;
/* 62 */     this.l.startElement(state, ea);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\DefaultValueLoaderDecorator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */