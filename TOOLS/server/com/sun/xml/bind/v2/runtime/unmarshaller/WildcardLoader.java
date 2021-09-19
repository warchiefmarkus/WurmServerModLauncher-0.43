/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*    */ import javax.xml.bind.annotation.DomHandler;
/*    */ import javax.xml.transform.Result;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WildcardLoader
/*    */   extends ProxyLoader
/*    */ {
/*    */   private final DomLoader dom;
/*    */   private final WildcardMode mode;
/*    */   
/*    */   public WildcardLoader(DomHandler<?, Result> dom, WildcardMode mode) {
/* 66 */     this.dom = new DomLoader<Result>(dom);
/* 67 */     this.mode = mode;
/*    */   }
/*    */   
/*    */   protected Loader selectLoader(UnmarshallingContext.State state, TagName tag) throws SAXException {
/* 71 */     UnmarshallingContext context = state.getContext();
/*    */     
/* 73 */     if (this.mode.allowTypedObject) {
/* 74 */       Loader l = context.selectRootLoader(state, tag);
/* 75 */       if (l != null)
/* 76 */         return l; 
/*    */     } 
/* 78 */     if (this.mode.allowDom) {
/* 79 */       return this.dom;
/*    */     }
/*    */     
/* 82 */     return Discarder.INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\WildcardLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */