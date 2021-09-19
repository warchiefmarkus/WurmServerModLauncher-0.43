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
/*    */ 
/*    */ public abstract class ProxyLoader
/*    */   extends Loader
/*    */ {
/*    */   public ProxyLoader() {
/* 49 */     super(false);
/*    */   }
/*    */   
/*    */   public final void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/* 53 */     Loader loader = selectLoader(state, ea);
/* 54 */     state.loader = loader;
/* 55 */     loader.startElement(state, ea);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Loader selectLoader(UnmarshallingContext.State paramState, TagName paramTagName) throws SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void leaveElement(UnmarshallingContext.State state, TagName ea) {
/* 69 */     throw new IllegalStateException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\ProxyLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */