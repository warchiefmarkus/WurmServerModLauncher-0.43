/*    */ package javax.xml.bind.annotation;
/*    */ 
/*    */ import javax.xml.bind.ValidationEventHandler;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.dom.DOMResult;
/*    */ import javax.xml.transform.dom.DOMSource;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Element;
/*    */ import org.w3c.dom.Node;
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
/*    */ public class W3CDomHandler
/*    */   implements DomHandler<Element, DOMResult>
/*    */ {
/*    */   private DocumentBuilder builder;
/*    */   
/*    */   public W3CDomHandler() {
/* 36 */     this.builder = null;
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
/*    */   public W3CDomHandler(DocumentBuilder builder) {
/* 48 */     if (builder == null)
/* 49 */       throw new IllegalArgumentException(); 
/* 50 */     this.builder = builder;
/*    */   }
/*    */   
/*    */   public DocumentBuilder getBuilder() {
/* 54 */     return this.builder;
/*    */   }
/*    */   
/*    */   public void setBuilder(DocumentBuilder builder) {
/* 58 */     this.builder = builder;
/*    */   }
/*    */   
/*    */   public DOMResult createUnmarshaller(ValidationEventHandler errorHandler) {
/* 62 */     if (this.builder == null) {
/* 63 */       return new DOMResult();
/*    */     }
/* 65 */     return new DOMResult(this.builder.newDocument());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Element getElement(DOMResult r) {
/* 71 */     Node n = r.getNode();
/* 72 */     if (n instanceof Document) {
/* 73 */       return ((Document)n).getDocumentElement();
/*    */     }
/* 75 */     if (n instanceof Element)
/* 76 */       return (Element)n; 
/* 77 */     if (n instanceof org.w3c.dom.DocumentFragment) {
/* 78 */       return (Element)n.getChildNodes().item(0);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 83 */     throw new IllegalStateException(n.toString());
/*    */   }
/*    */   
/*    */   public Source marshal(Element element, ValidationEventHandler errorHandler) {
/* 87 */     return new DOMSource(element);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\W3CDomHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */