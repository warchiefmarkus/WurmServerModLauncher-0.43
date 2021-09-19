/*    */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*    */ 
/*    */ import com.sun.tools.xjc.model.Model;
/*    */ import com.sun.tools.xjc.reader.Ring;
/*    */ import javax.xml.namespace.QName;
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
/*    */ public final class BIXPluginCustomization
/*    */   extends AbstractDeclarationImpl
/*    */ {
/*    */   public final Element element;
/*    */   private QName name;
/*    */   
/*    */   public BIXPluginCustomization(Element e, Locator _loc) {
/* 63 */     super(_loc);
/* 64 */     this.element = e;
/*    */   }
/*    */   
/*    */   public void onSetOwner() {
/* 68 */     super.onSetOwner();
/* 69 */     if (!((Model)Ring.get(Model.class)).options.pluginURIs.contains(this.element.getNamespaceURI()))
/* 70 */       markAsAcknowledged(); 
/*    */   }
/*    */   
/*    */   public final QName getName() {
/* 74 */     if (this.name == null)
/* 75 */       this.name = new QName(this.element.getNamespaceURI(), this.element.getLocalName()); 
/* 76 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIXPluginCustomization.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */