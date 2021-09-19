/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import com.sun.xml.txw2.TxwException;
/*     */ import java.util.Stack;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.AttributesImpl;
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
/*     */ public class SaxSerializer
/*     */   implements XmlSerializer
/*     */ {
/*     */   private final ContentHandler writer;
/*     */   private final LexicalHandler lexical;
/*     */   
/*     */   public SaxSerializer(ContentHandler handler) {
/*  43 */     this(handler, null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SaxSerializer(ContentHandler handler, LexicalHandler lex) {
/*  54 */     this(handler, lex, true);
/*     */   }
/*     */   
/*     */   public SaxSerializer(ContentHandler handler, LexicalHandler lex, boolean indenting) {
/*  58 */     if (!indenting) {
/*  59 */       this.writer = handler;
/*  60 */       this.lexical = lex;
/*     */     } else {
/*  62 */       IndentingXMLFilter indenter = new IndentingXMLFilter(handler, lex);
/*  63 */       this.writer = indenter;
/*  64 */       this.lexical = indenter;
/*     */     } 
/*     */   }
/*     */   
/*     */   public SaxSerializer(SAXResult result) {
/*  69 */     this(result.getHandler(), result.getLexicalHandler());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument() {
/*     */     try {
/*  77 */       this.writer.startDocument();
/*  78 */     } catch (SAXException e) {
/*  79 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  85 */   private final Stack<String> prefixBindings = new Stack<String>();
/*     */ 
/*     */   
/*     */   public void writeXmlns(String prefix, String uri) {
/*  89 */     if (prefix == null) {
/*  90 */       prefix = "";
/*     */     }
/*     */     
/*  93 */     if (prefix.equals("xml")) {
/*     */       return;
/*     */     }
/*     */     
/*  97 */     this.prefixBindings.add(uri);
/*  98 */     this.prefixBindings.add(prefix);
/*     */   }
/*     */ 
/*     */   
/* 102 */   private final Stack<String> elementBindings = new Stack<String>();
/*     */ 
/*     */   
/*     */   public void beginStartTag(String uri, String localName, String prefix) {
/* 106 */     this.elementBindings.add(getQName(prefix, localName));
/* 107 */     this.elementBindings.add(localName);
/* 108 */     this.elementBindings.add(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   private final AttributesImpl attrs = new AttributesImpl();
/*     */   
/*     */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/* 117 */     this.attrs.addAttribute(uri, localName, getQName(prefix, localName), "CDATA", value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endStartTag(String uri, String localName, String prefix) {
/*     */     try {
/* 126 */       while (this.prefixBindings.size() != 0) {
/* 127 */         this.writer.startPrefixMapping(this.prefixBindings.pop(), this.prefixBindings.pop());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 132 */       this.writer.startElement(uri, localName, getQName(prefix, localName), this.attrs);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 137 */       this.attrs.clear();
/* 138 */     } catch (SAXException e) {
/* 139 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endTag() {
/*     */     try {
/* 145 */       this.writer.endElement(this.elementBindings.pop(), this.elementBindings.pop(), this.elementBindings.pop());
/*     */ 
/*     */     
/*     */     }
/* 149 */     catch (SAXException e) {
/* 150 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void text(StringBuilder text) {
/*     */     try {
/* 156 */       this.writer.characters(text.toString().toCharArray(), 0, text.length());
/* 157 */     } catch (SAXException e) {
/* 158 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cdata(StringBuilder text) {
/* 163 */     if (this.lexical == null) {
/* 164 */       throw new UnsupportedOperationException("LexicalHandler is needed to write PCDATA");
/*     */     }
/*     */     try {
/* 167 */       this.lexical.startCDATA();
/* 168 */       text(text);
/* 169 */       this.lexical.endCDATA();
/* 170 */     } catch (SAXException e) {
/* 171 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void comment(StringBuilder comment) {
/*     */     try {
/* 177 */       if (this.lexical == null) {
/* 178 */         throw new UnsupportedOperationException("LexicalHandler is needed to write comments");
/*     */       }
/* 180 */       this.lexical.comment(comment.toString().toCharArray(), 0, comment.length());
/* 181 */     } catch (SAXException e) {
/* 182 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endDocument() {
/*     */     try {
/* 188 */       this.writer.endDocument();
/* 189 */     } catch (SAXException e) {
/* 190 */       throw new TxwException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {}
/*     */ 
/*     */   
/*     */   private static String getQName(String prefix, String localName) {
/*     */     String qName;
/* 201 */     if (prefix == null || prefix.length() == 0) {
/* 202 */       qName = localName;
/*     */     } else {
/* 204 */       qName = prefix + ':' + localName;
/*     */     } 
/* 206 */     return qName;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\SaxSerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */