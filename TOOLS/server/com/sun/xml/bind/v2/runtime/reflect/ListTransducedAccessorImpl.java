/*     */ package com.sun.xml.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import javax.xml.bind.JAXBException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ListTransducedAccessorImpl<BeanT, ListT, ItemT, PackT>
/*     */   extends DefaultTransducedAccessor<BeanT>
/*     */ {
/*     */   private final Transducer<ItemT> xducer;
/*     */   private final Lister<BeanT, ListT, ItemT, PackT> lister;
/*     */   private final Accessor<BeanT, ListT> acc;
/*     */   
/*     */   public ListTransducedAccessorImpl(Transducer<ItemT> xducer, Accessor<BeanT, ListT> acc, Lister<BeanT, ListT, ItemT, PackT> lister) {
/*  68 */     this.xducer = xducer;
/*  69 */     this.lister = lister;
/*  70 */     this.acc = acc;
/*     */   }
/*     */   
/*     */   public boolean useNamespace() {
/*  74 */     return this.xducer.useNamespace();
/*     */   }
/*     */   
/*     */   public void declareNamespace(BeanT bean, XMLSerializer w) throws AccessorException, SAXException {
/*  78 */     ListT list = this.acc.get(bean);
/*     */     
/*  80 */     if (list != null) {
/*  81 */       ListIterator<ItemT> itr = this.lister.iterator(list, w);
/*     */       
/*  83 */       while (itr.hasNext()) {
/*     */         try {
/*  85 */           ItemT item = itr.next();
/*  86 */           if (item != null) {
/*  87 */             this.xducer.declareNamespace(item, w);
/*     */           }
/*  89 */         } catch (JAXBException e) {
/*  90 */           w.reportError(null, (Throwable)e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String print(BeanT o) throws AccessorException, SAXException {
/* 100 */     ListT list = this.acc.get(o);
/*     */     
/* 102 */     if (list == null) {
/* 103 */       return null;
/*     */     }
/* 105 */     StringBuilder buf = new StringBuilder();
/* 106 */     XMLSerializer w = XMLSerializer.getInstance();
/* 107 */     ListIterator<ItemT> itr = this.lister.iterator(list, w);
/*     */     
/* 109 */     while (itr.hasNext()) {
/*     */       try {
/* 111 */         ItemT item = itr.next();
/* 112 */         if (item != null) {
/* 113 */           if (buf.length() > 0) buf.append(' '); 
/* 114 */           buf.append(this.xducer.print(item));
/*     */         } 
/* 116 */       } catch (JAXBException e) {
/* 117 */         w.reportError(null, (Throwable)e);
/*     */       } 
/*     */     } 
/* 120 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void processValue(BeanT bean, CharSequence s) throws AccessorException, SAXException {
/* 124 */     PackT pack = this.lister.startPacking(bean, this.acc);
/*     */     
/* 126 */     int idx = 0;
/* 127 */     int len = s.length();
/*     */     
/*     */     while (true) {
/* 130 */       int p = idx;
/* 131 */       while (p < len && !WhiteSpaceProcessor.isWhiteSpace(s.charAt(p))) {
/* 132 */         p++;
/*     */       }
/* 134 */       CharSequence token = s.subSequence(idx, p);
/* 135 */       if (!token.equals("")) {
/* 136 */         this.lister.addToPack(pack, (ItemT)this.xducer.parse(token));
/*     */       }
/* 138 */       if (p == len)
/*     */         break; 
/* 140 */       while (p < len && WhiteSpaceProcessor.isWhiteSpace(s.charAt(p)))
/* 141 */         p++; 
/* 142 */       if (p == len)
/*     */         break; 
/* 144 */       idx = p;
/*     */     } 
/*     */     
/* 147 */     this.lister.endPacking(pack, bean, this.acc);
/*     */   }
/*     */   
/*     */   public void parse(BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
/* 151 */     processValue(bean, lexical);
/*     */   }
/*     */   
/*     */   public boolean hasValue(BeanT bean) throws AccessorException {
/* 155 */     return (this.acc.get(bean) != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\ListTransducedAccessorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */