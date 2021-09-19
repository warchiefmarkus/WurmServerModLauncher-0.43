/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.stream.XMLStreamException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IndentingUTF8XmlOutput
/*     */   extends UTF8XmlOutput
/*     */ {
/*     */   private final Encoded indent8;
/*     */   private final int unitLen;
/*  72 */   private int depth = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean seenText = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndentingUTF8XmlOutput(OutputStream out, String indentStr, Encoded[] localNames) {
/*  83 */     super(out, localNames);
/*     */     
/*  85 */     if (indentStr != null) {
/*  86 */       Encoded e = new Encoded(indentStr);
/*  87 */       this.indent8 = new Encoded();
/*  88 */       this.indent8.ensureSize(e.len * 8);
/*  89 */       this.unitLen = e.len;
/*  90 */       for (int i = 0; i < 8; i++)
/*  91 */         System.arraycopy(e.buf, 0, this.indent8.buf, this.unitLen * i, this.unitLen); 
/*     */     } else {
/*  93 */       this.indent8 = null;
/*  94 */       this.unitLen = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) throws IOException {
/* 100 */     indentStartTag();
/* 101 */     super.beginStartTag(prefix, localName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginStartTag(Name name) throws IOException {
/* 106 */     indentStartTag();
/* 107 */     super.beginStartTag(name);
/*     */   }
/*     */   
/*     */   private void indentStartTag() throws IOException {
/* 111 */     closeStartTag();
/* 112 */     if (!this.seenText)
/* 113 */       printIndent(); 
/* 114 */     this.depth++;
/* 115 */     this.seenText = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTag(Name name) throws IOException {
/* 120 */     indentEndTag();
/* 121 */     super.endTag(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTag(int prefix, String localName) throws IOException {
/* 126 */     indentEndTag();
/* 127 */     super.endTag(prefix, localName);
/*     */   }
/*     */   
/*     */   private void indentEndTag() throws IOException {
/* 131 */     this.depth--;
/* 132 */     if (!this.closeStartTagPending && !this.seenText)
/* 133 */       printIndent(); 
/* 134 */     this.seenText = false;
/*     */   }
/*     */   
/*     */   private void printIndent() throws IOException {
/* 138 */     write(10);
/* 139 */     int i = this.depth % 8;
/*     */     
/* 141 */     write(this.indent8.buf, 0, i * this.unitLen);
/*     */     
/* 143 */     i >>= 3;
/*     */     
/* 145 */     for (; i > 0; i--) {
/* 146 */       this.indent8.write(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void text(String value, boolean needSP) throws IOException {
/* 151 */     this.seenText = true;
/* 152 */     super.text(value, needSP);
/*     */   }
/*     */ 
/*     */   
/*     */   public void text(Pcdata value, boolean needSP) throws IOException {
/* 157 */     this.seenText = true;
/* 158 */     super.text(value, needSP);
/*     */   }
/*     */ 
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/* 163 */     write(10);
/* 164 */     super.endDocument(fragment);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\IndentingUTF8XmlOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */