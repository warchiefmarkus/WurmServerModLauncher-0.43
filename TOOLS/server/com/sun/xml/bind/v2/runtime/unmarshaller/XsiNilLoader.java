/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XsiNilLoader
/*     */   extends ProxyLoader
/*     */ {
/*     */   private final Loader defaultLoader;
/*     */   
/*     */   public XsiNilLoader(Loader defaultLoader) {
/*  57 */     this.defaultLoader = defaultLoader;
/*  58 */     assert defaultLoader != null;
/*     */   }
/*     */   
/*     */   protected Loader selectLoader(UnmarshallingContext.State state, TagName ea) throws SAXException {
/*  62 */     int idx = ea.atts.getIndex("http://www.w3.org/2001/XMLSchema-instance", "nil");
/*     */     
/*  64 */     if (idx != -1) {
/*  65 */       String value = ea.atts.getValue(idx);
/*  66 */       if (DatatypeConverterImpl._parseBoolean(value)) {
/*  67 */         onNil(state);
/*  68 */         return Discarder.INSTANCE;
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     return this.defaultLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onNil(UnmarshallingContext.State state) throws SAXException {}
/*     */ 
/*     */   
/*     */   public static final class Single
/*     */     extends XsiNilLoader
/*     */   {
/*     */     private final Accessor acc;
/*     */ 
/*     */     
/*     */     public Single(Loader l, Accessor acc) {
/*  86 */       super(l);
/*  87 */       this.acc = acc;
/*     */     }
/*     */     
/*     */     protected void onNil(UnmarshallingContext.State state) throws SAXException {
/*     */       try {
/*  92 */         this.acc.set(state.prev.target, null);
/*  93 */       } catch (AccessorException e) {
/*  94 */         handleGenericException((Exception)e, true);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Array extends XsiNilLoader {
/*     */     public Array(Loader core) {
/* 101 */       super(core);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onNil(UnmarshallingContext.State state) {
/* 106 */       state.target = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\XsiNilLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */