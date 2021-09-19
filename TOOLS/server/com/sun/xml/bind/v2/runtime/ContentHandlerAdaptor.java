/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.FinalArrayList;
/*     */ import com.sun.istack.SAXException2;
/*     */ import java.io.IOException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ final class ContentHandlerAdaptor
/*     */   extends DefaultHandler
/*     */ {
/*  59 */   private final FinalArrayList<String> prefixMap = new FinalArrayList();
/*     */ 
/*     */   
/*     */   private final XMLSerializer serializer;
/*     */   
/*  64 */   private final StringBuffer text = new StringBuffer();
/*     */ 
/*     */   
/*     */   ContentHandlerAdaptor(XMLSerializer _serializer) {
/*  68 */     this.serializer = _serializer;
/*     */   }
/*     */   
/*     */   public void startDocument() {
/*  72 */     this.prefixMap.clear();
/*     */   }
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) {
/*  76 */     this.prefixMap.add(prefix);
/*  77 */     this.prefixMap.add(uri);
/*     */   }
/*     */   
/*     */   private boolean containsPrefixMapping(String prefix, String uri) {
/*  81 */     for (int i = 0; i < this.prefixMap.size(); i += 2) {
/*  82 */       if (((String)this.prefixMap.get(i)).equals(prefix) && ((String)this.prefixMap.get(i + 1)).equals(uri))
/*     */       {
/*  84 */         return true; } 
/*     */     } 
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
/*     */     try {
/*  92 */       flushText();
/*     */       
/*  94 */       int len = atts.getLength();
/*     */       
/*  96 */       String p = getPrefix(qName);
/*     */ 
/*     */       
/*  99 */       if (containsPrefixMapping(p, namespaceURI)) {
/* 100 */         this.serializer.startElementForce(namespaceURI, localName, p, null);
/*     */       } else {
/* 102 */         this.serializer.startElement(namespaceURI, localName, p, null);
/*     */       } 
/*     */       int i;
/* 105 */       for (i = 0; i < this.prefixMap.size(); i += 2)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 110 */         this.serializer.getNamespaceContext().force((String)this.prefixMap.get(i + 1), (String)this.prefixMap.get(i));
/*     */       }
/*     */ 
/*     */       
/* 114 */       for (i = 0; i < len; i++) {
/* 115 */         String qname = atts.getQName(i);
/* 116 */         if (!qname.startsWith("xmlns")) {
/*     */           
/* 118 */           String prefix = getPrefix(qname);
/*     */           
/* 120 */           this.serializer.getNamespaceContext().declareNamespace(atts.getURI(i), prefix, true);
/*     */         } 
/*     */       } 
/*     */       
/* 124 */       this.serializer.endNamespaceDecls(null);
/*     */       
/* 126 */       for (i = 0; i < len; i++) {
/*     */         
/* 128 */         if (!atts.getQName(i).startsWith("xmlns"))
/*     */         {
/* 130 */           this.serializer.attribute(atts.getURI(i), atts.getLocalName(i), atts.getValue(i)); } 
/*     */       } 
/* 132 */       this.prefixMap.clear();
/* 133 */       this.serializer.endAttributes();
/* 134 */     } catch (IOException e) {
/* 135 */       throw new SAXException2(e);
/* 136 */     } catch (XMLStreamException e) {
/* 137 */       throw new SAXException2(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getPrefix(String qname) {
/* 142 */     int idx = qname.indexOf(':');
/* 143 */     String prefix = (idx == -1) ? qname : qname.substring(0, idx);
/* 144 */     return prefix;
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
/*     */     try {
/* 149 */       flushText();
/* 150 */       this.serializer.endElement();
/* 151 */     } catch (IOException e) {
/* 152 */       throw new SAXException2(e);
/* 153 */     } catch (XMLStreamException e) {
/* 154 */       throw new SAXException2(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void flushText() throws SAXException, IOException, XMLStreamException {
/* 159 */     if (this.text.length() != 0) {
/* 160 */       this.serializer.text(this.text.toString(), (String)null);
/* 161 */       this.text.setLength(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length) {
/* 166 */     this.text.append(ch, start, length);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\ContentHandlerAdaptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */