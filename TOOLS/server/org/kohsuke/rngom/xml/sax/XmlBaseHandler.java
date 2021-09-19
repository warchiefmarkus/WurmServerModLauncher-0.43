/*    */ package org.kohsuke.rngom.xml.sax;
/*    */ 
/*    */ import org.kohsuke.rngom.util.Uri;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ public class XmlBaseHandler {
/*  7 */   private int depth = 0;
/*    */   private Locator loc;
/*  9 */   private Entry stack = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLocator(Locator loc) {
/* 19 */     this.loc = loc;
/*    */   } private static class Entry {
/*    */     private Entry parent; private String attValue; private String systemId; private int depth; private Entry() {} }
/*    */   public void startElement() {
/* 23 */     this.depth++;
/*    */   }
/*    */   
/*    */   public void endElement() {
/* 27 */     if (this.stack != null && this.stack.depth == this.depth)
/* 28 */       this.stack = this.stack.parent; 
/* 29 */     this.depth--;
/*    */   }
/*    */   
/*    */   public void xmlBaseAttribute(String value) {
/* 33 */     Entry entry = new Entry();
/* 34 */     entry.parent = this.stack;
/* 35 */     this.stack = entry;
/* 36 */     entry.attValue = Uri.escapeDisallowedChars(value);
/* 37 */     entry.systemId = getSystemId();
/* 38 */     entry.depth = this.depth;
/*    */   }
/*    */   
/*    */   private String getSystemId() {
/* 42 */     return (this.loc == null) ? null : this.loc.getSystemId();
/*    */   }
/*    */   
/*    */   public String getBaseUri() {
/* 46 */     return getBaseUri1(getSystemId(), this.stack);
/*    */   }
/*    */   
/*    */   private static String getBaseUri1(String baseUri, Entry stack) {
/* 50 */     if (stack == null || (baseUri != null && !baseUri.equals(stack.systemId)))
/*    */     {
/* 52 */       return baseUri; } 
/* 53 */     baseUri = stack.attValue;
/* 54 */     if (Uri.isAbsolute(baseUri))
/* 55 */       return baseUri; 
/* 56 */     return Uri.resolve(getBaseUri1(stack.systemId, stack.parent), baseUri);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\xml\sax\XmlBaseHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */