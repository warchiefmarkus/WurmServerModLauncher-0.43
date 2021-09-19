/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.xml.bind.marshaller.SAX2DOMEx;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.annotation.DomHandler;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
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
/*     */ final class DomHandlerEx
/*     */   implements DomHandler<DomHandlerEx.DomAndLocation, DomHandlerEx.ResultImpl>
/*     */ {
/*     */   public static final class DomAndLocation
/*     */   {
/*     */     public final Element element;
/*     */     public final Locator loc;
/*     */     
/*     */     public DomAndLocation(Element element, Locator loc) {
/*  66 */       this.element = element;
/*  67 */       this.loc = loc;
/*     */     }
/*     */   }
/*     */   
/*     */   public ResultImpl createUnmarshaller(ValidationEventHandler errorHandler) {
/*  72 */     return new ResultImpl();
/*     */   }
/*     */   
/*     */   public DomAndLocation getElement(ResultImpl r) {
/*  76 */     return new DomAndLocation(((Document)r.s2d.getDOM()).getDocumentElement(), r.location);
/*     */   }
/*     */   
/*     */   public Source marshal(DomAndLocation domAndLocation, ValidationEventHandler errorHandler) {
/*  80 */     return new DOMSource(domAndLocation.element);
/*     */   }
/*     */   
/*     */   public static final class ResultImpl
/*     */     extends SAXResult {
/*     */     final SAX2DOMEx s2d;
/*  86 */     Locator location = null;
/*     */     
/*     */     ResultImpl() {
/*     */       try {
/*  90 */         this.s2d = new SAX2DOMEx();
/*  91 */       } catch (ParserConfigurationException e) {
/*  92 */         throw new AssertionError(e);
/*     */       } 
/*     */       
/*  95 */       XMLFilterImpl f = new XMLFilterImpl() {
/*     */           public void setDocumentLocator(Locator locator) {
/*  97 */             super.setDocumentLocator(locator);
/*  98 */             DomHandlerEx.ResultImpl.this.location = new LocatorImpl(locator);
/*     */           }
/*     */         };
/* 101 */       f.setContentHandler((ContentHandler)this.s2d);
/*     */       
/* 103 */       setHandler(f);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\DomHandlerEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */