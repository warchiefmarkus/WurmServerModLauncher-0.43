/*     */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BIInterface
/*     */ {
/*     */   private final Element dom;
/*     */   private final String name;
/*     */   private final String[] members;
/*     */   private final String[] fields;
/*     */   
/*     */   BIInterface(Element e) {
/*  49 */     this.dom = e;
/*  50 */     this.name = DOMUtil.getAttribute(e, "name");
/*  51 */     this.members = parseTokens(DOMUtil.getAttribute(e, "members"));
/*     */     
/*  53 */     if (DOMUtil.getAttribute(e, "properties") != null) {
/*  54 */       this.fields = parseTokens(DOMUtil.getAttribute(e, "properties"));
/*  55 */       throw new AssertionError("//interface/@properties is not supported");
/*     */     } 
/*  57 */     this.fields = new String[0];
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
/*     */   public String name() {
/*  70 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] members() {
/*  79 */     return this.members;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] fields() {
/*  85 */     return this.fields;
/*     */   }
/*     */ 
/*     */   
/*     */   public Locator getSourceLocation() {
/*  90 */     return DOMLocator.getLocationInfo(this.dom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] parseTokens(String value) {
/*  97 */     StringTokenizer tokens = new StringTokenizer(value);
/*     */     
/*  99 */     String[] r = new String[tokens.countTokens()];
/* 100 */     int i = 0;
/* 101 */     while (tokens.hasMoreTokens()) {
/* 102 */       r[i++] = tokens.nextToken();
/*     */     }
/* 104 */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BIInterface.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */