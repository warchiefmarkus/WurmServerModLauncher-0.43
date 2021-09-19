/*     */ package com.sun.xml.txw2;
/*     */ 
/*     */ import com.sun.xml.txw2.annotation.XmlElement;
/*     */ import com.sun.xml.txw2.annotation.XmlNamespace;
/*     */ import com.sun.xml.txw2.output.XmlSerializer;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public abstract class TXW
/*     */ {
/*     */   static QName getTagName(Class<?> c) {
/*  39 */     String localName = "";
/*  40 */     String nsUri = "##default";
/*     */     
/*  42 */     XmlElement xe = c.<XmlElement>getAnnotation(XmlElement.class);
/*  43 */     if (xe != null) {
/*  44 */       localName = xe.value();
/*  45 */       nsUri = xe.ns();
/*     */     } 
/*     */     
/*  48 */     if (localName.length() == 0) {
/*  49 */       localName = c.getName();
/*  50 */       int idx = localName.lastIndexOf('.');
/*  51 */       if (idx >= 0) {
/*  52 */         localName = localName.substring(idx + 1);
/*     */       }
/*  54 */       localName = Character.toLowerCase(localName.charAt(0)) + localName.substring(1);
/*     */     } 
/*     */     
/*  57 */     if (nsUri.equals("##default")) {
/*  58 */       Package pkg = c.getPackage();
/*  59 */       if (pkg != null) {
/*  60 */         XmlNamespace xn = pkg.<XmlNamespace>getAnnotation(XmlNamespace.class);
/*  61 */         if (xn != null)
/*  62 */           nsUri = xn.value(); 
/*     */       } 
/*     */     } 
/*  65 */     if (nsUri.equals("##default")) {
/*  66 */       nsUri = "";
/*     */     }
/*  68 */     return new QName(nsUri, localName);
/*     */   }
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
/*     */   public static <T extends TypedXmlWriter> T create(Class<T> rootElement, XmlSerializer out) {
/*  82 */     Document doc = new Document(out);
/*  83 */     QName n = getTagName(rootElement);
/*  84 */     return (new ContainerElement(doc, null, n.getNamespaceURI(), n.getLocalPart()))._cast(rootElement);
/*     */   }
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
/*     */   public static <T extends TypedXmlWriter> T create(QName tagName, Class<T> rootElement, XmlSerializer out) {
/* 100 */     return (new ContainerElement(new Document(out), null, tagName.getNamespaceURI(), tagName.getLocalPart()))._cast(rootElement);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\TXW.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */