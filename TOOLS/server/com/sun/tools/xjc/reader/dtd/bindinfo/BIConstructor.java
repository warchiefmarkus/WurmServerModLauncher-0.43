/*    */ package com.sun.tools.xjc.reader.dtd.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.model.CClassInfo;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.StringTokenizer;
/*    */ import org.w3c.dom.Element;
/*    */ import org.xml.sax.Locator;
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
/*    */ public class BIConstructor
/*    */ {
/*    */   private final Element dom;
/*    */   private final String[] properties;
/*    */   
/*    */   BIConstructor(Element _node) {
/* 63 */     this.dom = _node;
/*    */     
/* 65 */     StringTokenizer tokens = new StringTokenizer(DOMUtil.getAttribute(_node, "properties"));
/*    */ 
/*    */     
/* 68 */     List<String> vec = new ArrayList<String>();
/* 69 */     while (tokens.hasMoreTokens())
/* 70 */       vec.add(tokens.nextToken()); 
/* 71 */     this.properties = vec.<String>toArray(new String[0]);
/*    */     
/* 73 */     if (this.properties.length == 0) {
/* 74 */       throw new AssertionError("this error should be catched by the validator");
/*    */     }
/*    */   }
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
/*    */   public void createDeclaration(CClassInfo cls) {
/* 91 */     cls.addConstructor(this.properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public Locator getSourceLocation() {
/* 96 */     return DOMLocator.getLocationInfo(this.dom);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\bindinfo\BIConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */