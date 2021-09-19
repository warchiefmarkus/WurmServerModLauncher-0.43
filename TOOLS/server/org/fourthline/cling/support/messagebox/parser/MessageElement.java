/*    */ package org.fourthline.cling.support.messagebox.parser;
/*    */ 
/*    */ import javax.xml.xpath.XPath;
/*    */ import org.seamless.xml.DOMElement;
/*    */ import org.w3c.dom.Element;
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
/*    */ public class MessageElement
/*    */   extends DOMElement<MessageElement, MessageElement>
/*    */ {
/*    */   public static final String XPATH_PREFIX = "m";
/*    */   
/*    */   public MessageElement(XPath xpath, Element element) {
/* 31 */     super(xpath, element);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String prefix(String localName) {
/* 36 */     return "m:" + localName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected DOMElement<MessageElement, MessageElement>.Builder<MessageElement> createParentBuilder(DOMElement el) {
/* 41 */     return new DOMElement<MessageElement, MessageElement>.Builder<MessageElement>(el)
/*    */       {
/*    */         public MessageElement build(Element element) {
/* 44 */           return new MessageElement(MessageElement.this.getXpath(), element);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   protected DOMElement<MessageElement, MessageElement>.ArrayBuilder<MessageElement> createChildBuilder(DOMElement el) {
/* 51 */     return new DOMElement<MessageElement, MessageElement>.ArrayBuilder<MessageElement>(el)
/*    */       {
/*    */         public MessageElement[] newChildrenArray(int length) {
/* 54 */           return new MessageElement[length];
/*    */         }
/*    */ 
/*    */         
/*    */         public MessageElement build(Element element) {
/* 59 */           return new MessageElement(MessageElement.this.getXpath(), element);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\parser\MessageElement.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */