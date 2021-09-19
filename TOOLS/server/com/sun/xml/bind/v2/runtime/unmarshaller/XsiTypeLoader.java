/*     */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class XsiTypeLoader
/*     */   extends Loader
/*     */ {
/*     */   private final JaxBeanInfo defaultBeanInfo;
/*     */   
/*     */   public XsiTypeLoader(JaxBeanInfo defaultBeanInfo) {
/*  62 */     super(true);
/*  63 */     this.defaultBeanInfo = defaultBeanInfo;
/*     */   }
/*     */   
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/*  67 */     JaxBeanInfo beanInfo = parseXsiType(state, ea, this.defaultBeanInfo);
/*  68 */     if (beanInfo == null) {
/*  69 */       beanInfo = this.defaultBeanInfo;
/*     */     }
/*  71 */     Loader loader = beanInfo.getLoader(null, false);
/*  72 */     state.loader = loader;
/*  73 */     loader.startElement(state, ea);
/*     */   }
/*     */   
/*     */   static JaxBeanInfo parseXsiType(UnmarshallingContext.State state, TagName ea, @Nullable JaxBeanInfo defaultBeanInfo) throws SAXException {
/*  77 */     UnmarshallingContext context = state.getContext();
/*  78 */     JaxBeanInfo beanInfo = null;
/*     */ 
/*     */     
/*  81 */     Attributes atts = ea.atts;
/*  82 */     int idx = atts.getIndex("http://www.w3.org/2001/XMLSchema-instance", "type");
/*     */     
/*  84 */     if (idx >= 0) {
/*     */ 
/*     */       
/*  87 */       String value = atts.getValue(idx);
/*     */       
/*  89 */       QName type = DatatypeConverterImpl._parseQName(value, context);
/*  90 */       if (type == null) {
/*  91 */         reportError(Messages.NOT_A_QNAME.format(new Object[] { value }, ), true);
/*     */       } else {
/*  93 */         if (defaultBeanInfo != null && defaultBeanInfo.getTypeNames().contains(type))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 100 */           return defaultBeanInfo;
/*     */         }
/* 102 */         beanInfo = context.getJAXBContext().getGlobalType(type);
/* 103 */         if (beanInfo == null) {
/* 104 */           String nearest = context.getJAXBContext().getNearestTypeName(type);
/* 105 */           if (nearest != null) {
/* 106 */             reportError(Messages.UNRECOGNIZED_TYPE_NAME_MAYBE.format(new Object[] { type, nearest }, ), true);
/*     */           } else {
/* 108 */             reportError(Messages.UNRECOGNIZED_TYPE_NAME.format(new Object[] { type }, ), true);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     return beanInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\XsiTypeLoader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */