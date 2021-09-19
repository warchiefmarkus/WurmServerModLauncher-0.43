/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import com.sun.xml.txw2.TxwException;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
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
/*     */ public class DomSerializer
/*     */   implements XmlSerializer
/*     */ {
/*     */   private final SaxSerializer serializer;
/*     */   
/*     */   public DomSerializer(Node node) {
/*  53 */     Dom2SaxAdapter adapter = new Dom2SaxAdapter(node);
/*  54 */     this.serializer = new SaxSerializer(adapter, adapter, false);
/*     */   }
/*     */   
/*     */   public DomSerializer(DOMResult domResult) {
/*  58 */     Node node = domResult.getNode();
/*     */     
/*  60 */     if (node == null) {
/*     */       try {
/*  62 */         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  63 */         dbf.setNamespaceAware(true);
/*  64 */         DocumentBuilder db = dbf.newDocumentBuilder();
/*  65 */         Document doc = db.newDocument();
/*  66 */         domResult.setNode(doc);
/*  67 */         this.serializer = new SaxSerializer(new Dom2SaxAdapter(doc), null, false);
/*  68 */       } catch (ParserConfigurationException pce) {
/*  69 */         throw new TxwException(pce);
/*     */       } 
/*     */     } else {
/*  72 */       this.serializer = new SaxSerializer(new Dom2SaxAdapter(node), null, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument() {
/*  78 */     this.serializer.startDocument();
/*     */   }
/*     */   
/*     */   public void beginStartTag(String uri, String localName, String prefix) {
/*  82 */     this.serializer.beginStartTag(uri, localName, prefix);
/*     */   }
/*     */   
/*     */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/*  86 */     this.serializer.writeAttribute(uri, localName, prefix, value);
/*     */   }
/*     */   
/*     */   public void writeXmlns(String prefix, String uri) {
/*  90 */     this.serializer.writeXmlns(prefix, uri);
/*     */   }
/*     */   
/*     */   public void endStartTag(String uri, String localName, String prefix) {
/*  94 */     this.serializer.endStartTag(uri, localName, prefix);
/*     */   }
/*     */   
/*     */   public void endTag() {
/*  98 */     this.serializer.endTag();
/*     */   }
/*     */   
/*     */   public void text(StringBuilder text) {
/* 102 */     this.serializer.text(text);
/*     */   }
/*     */   
/*     */   public void cdata(StringBuilder text) {
/* 106 */     this.serializer.cdata(text);
/*     */   }
/*     */   
/*     */   public void comment(StringBuilder comment) {
/* 110 */     this.serializer.comment(comment);
/*     */   }
/*     */   
/*     */   public void endDocument() {
/* 114 */     this.serializer.endDocument();
/*     */   }
/*     */   
/*     */   public void flush() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\DomSerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */