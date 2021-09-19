/*     */ package com.sun.xml.txw2.output;
/*     */ 
/*     */ import java.util.Stack;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndentingXMLFilter
/*     */   extends XMLFilterImpl
/*     */   implements LexicalHandler
/*     */ {
/*     */   private LexicalHandler lexical;
/*     */   
/*     */   public IndentingXMLFilter() {}
/*     */   
/*     */   public IndentingXMLFilter(ContentHandler handler) {
/*  23 */     setContentHandler(handler);
/*     */   }
/*     */   
/*     */   public IndentingXMLFilter(ContentHandler handler, LexicalHandler lexical) {
/*  27 */     setContentHandler(handler);
/*  28 */     setLexicalHandler(lexical);
/*     */   }
/*     */   
/*     */   public LexicalHandler getLexicalHandler() {
/*  32 */     return this.lexical;
/*     */   }
/*     */   
/*     */   public void setLexicalHandler(LexicalHandler lexical) {
/*  36 */     this.lexical = lexical;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndentStep() {
/*  56 */     return this.indentStep.length();
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
/*     */   public void setIndentStep(int indentStep) {
/*  72 */     StringBuilder s = new StringBuilder();
/*  73 */     while (indentStep > 0) { s.append(' '); indentStep--; }
/*  74 */      setIndentStep(s.toString());
/*     */   }
/*     */   
/*     */   public void setIndentStep(String s) {
/*  78 */     this.indentStep = s;
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
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 109 */     this.stateStack.push(SEEN_ELEMENT);
/* 110 */     this.state = SEEN_NOTHING;
/* 111 */     if (this.depth > 0) {
/* 112 */       writeNewLine();
/*     */     }
/* 114 */     doIndent();
/* 115 */     super.startElement(uri, localName, qName, atts);
/* 116 */     this.depth++;
/*     */   }
/*     */   
/*     */   private void writeNewLine() throws SAXException {
/* 120 */     super.characters(NEWLINE, 0, NEWLINE.length);
/*     */   }
/*     */   
/* 123 */   private static final char[] NEWLINE = new char[] { '\n' };
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
/*     */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 147 */     this.depth--;
/* 148 */     if (this.state == SEEN_ELEMENT) {
/* 149 */       writeNewLine();
/* 150 */       doIndent();
/*     */     } 
/* 152 */     super.endElement(uri, localName, qName);
/* 153 */     this.state = this.stateStack.pop();
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
/*     */   public void characters(char[] ch, int start, int length) throws SAXException {
/* 203 */     this.state = SEEN_DATA;
/* 204 */     super.characters(ch, start, length);
/*     */   }
/*     */   
/*     */   public void comment(char[] ch, int start, int length) throws SAXException {
/* 208 */     if (this.depth > 0) {
/* 209 */       writeNewLine();
/*     */     }
/* 211 */     doIndent();
/* 212 */     if (this.lexical != null)
/* 213 */       this.lexical.comment(ch, start, length); 
/*     */   }
/*     */   
/*     */   public void startDTD(String name, String publicId, String systemId) throws SAXException {
/* 217 */     if (this.lexical != null)
/* 218 */       this.lexical.startDTD(name, publicId, systemId); 
/*     */   }
/*     */   
/*     */   public void endDTD() throws SAXException {
/* 222 */     if (this.lexical != null)
/* 223 */       this.lexical.endDTD(); 
/*     */   }
/*     */   
/*     */   public void startEntity(String name) throws SAXException {
/* 227 */     if (this.lexical != null)
/* 228 */       this.lexical.startEntity(name); 
/*     */   }
/*     */   
/*     */   public void endEntity(String name) throws SAXException {
/* 232 */     if (this.lexical != null)
/* 233 */       this.lexical.endEntity(name); 
/*     */   }
/*     */   
/*     */   public void startCDATA() throws SAXException {
/* 237 */     if (this.lexical != null)
/* 238 */       this.lexical.startCDATA(); 
/*     */   }
/*     */   
/*     */   public void endCDATA() throws SAXException {
/* 242 */     if (this.lexical != null) {
/* 243 */       this.lexical.endCDATA();
/*     */     }
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
/*     */   
/*     */   private void doIndent() throws SAXException {
/* 261 */     if (this.depth > 0) {
/* 262 */       char[] ch = this.indentStep.toCharArray();
/* 263 */       for (int i = 0; i < this.depth; i++) {
/* 264 */         characters(ch, 0, ch.length);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 273 */   private static final Object SEEN_NOTHING = new Object();
/* 274 */   private static final Object SEEN_ELEMENT = new Object();
/* 275 */   private static final Object SEEN_DATA = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 282 */   private Object state = SEEN_NOTHING;
/* 283 */   private Stack<Object> stateStack = new Stack();
/*     */   
/* 285 */   private String indentStep = "";
/* 286 */   private int depth = 0;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\IndentingXMLFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */