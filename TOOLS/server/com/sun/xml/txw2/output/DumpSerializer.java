/*    */ package com.sun.xml.txw2.output;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DumpSerializer
/*    */   implements XmlSerializer
/*    */ {
/*    */   private final PrintStream out;
/*    */   
/*    */   public DumpSerializer(PrintStream out) {
/* 36 */     this.out = out;
/*    */   }
/*    */   
/*    */   public void beginStartTag(String uri, String localName, String prefix) {
/* 40 */     this.out.println('<' + prefix + ':' + localName);
/*    */   }
/*    */   
/*    */   public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/* 44 */     this.out.println('@' + prefix + ':' + localName + '=' + value);
/*    */   }
/*    */   
/*    */   public void writeXmlns(String prefix, String uri) {
/* 48 */     this.out.println("xmlns:" + prefix + '=' + uri);
/*    */   }
/*    */   
/*    */   public void endStartTag(String uri, String localName, String prefix) {
/* 52 */     this.out.println('>');
/*    */   }
/*    */   
/*    */   public void endTag() {
/* 56 */     this.out.println("</  >");
/*    */   }
/*    */   
/*    */   public void text(StringBuilder text) {
/* 60 */     this.out.println(text);
/*    */   }
/*    */   
/*    */   public void cdata(StringBuilder text) {
/* 64 */     this.out.println("<![CDATA[");
/* 65 */     this.out.println(text);
/* 66 */     this.out.println("]]>");
/*    */   }
/*    */   
/*    */   public void comment(StringBuilder comment) {
/* 70 */     this.out.println("<!--");
/* 71 */     this.out.println(comment);
/* 72 */     this.out.println("-->");
/*    */   }
/*    */   
/*    */   public void startDocument() {
/* 76 */     this.out.println("<?xml?>");
/*    */   }
/*    */   
/*    */   public void endDocument() {
/* 80 */     this.out.println("done");
/*    */   }
/*    */   
/*    */   public void flush() {
/* 84 */     this.out.println("flush");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\output\DumpSerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */