/*    */ package com.sun.tools.xjc.addon.code_injector;
/*    */ 
/*    */ import com.sun.tools.xjc.Options;
/*    */ import com.sun.tools.xjc.Plugin;
/*    */ import com.sun.tools.xjc.model.CPluginCustomization;
/*    */ import com.sun.tools.xjc.outline.ClassOutline;
/*    */ import com.sun.tools.xjc.outline.Outline;
/*    */ import com.sun.tools.xjc.util.DOMUtils;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.xml.sax.ErrorHandler;
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
/*    */ public class PluginImpl
/*    */   extends Plugin
/*    */ {
/*    */   public String getOptionName() {
/* 60 */     return "Xinject-code";
/*    */   }
/*    */   
/*    */   public List<String> getCustomizationURIs() {
/* 64 */     return Collections.singletonList("http://jaxb.dev.java.net/plugin/code-injector");
/*    */   }
/*    */   
/*    */   public boolean isCustomizationTagName(String nsUri, String localName) {
/* 68 */     return (nsUri.equals("http://jaxb.dev.java.net/plugin/code-injector") && localName.equals("code"));
/*    */   }
/*    */   
/*    */   public String getUsage() {
/* 72 */     return "  -Xinject-code      :  inject specified Java code fragments into the generated code";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler) {
/* 77 */     for (ClassOutline co : model.getClasses()) {
/* 78 */       CPluginCustomization c = co.target.getCustomizations().find("http://jaxb.dev.java.net/plugin/code-injector", "code");
/* 79 */       if (c == null) {
/*    */         continue;
/*    */       }
/* 82 */       c.markAsAcknowledged();
/*    */ 
/*    */       
/* 85 */       String codeFragment = DOMUtils.getElementText(c.element);
/*    */ 
/*    */       
/* 88 */       co.implClass.direct(codeFragment);
/*    */     } 
/*    */     
/* 91 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\addon\code_injector\PluginImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */