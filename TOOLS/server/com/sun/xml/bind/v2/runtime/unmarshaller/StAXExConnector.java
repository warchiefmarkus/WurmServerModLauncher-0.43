/*    */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ import org.jvnet.staxex.Base64Data;
/*    */ import org.jvnet.staxex.XMLStreamReaderEx;
/*    */ import org.xml.sax.SAXException;
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
/*    */ final class StAXExConnector
/*    */   extends StAXStreamConnector
/*    */ {
/*    */   private final XMLStreamReaderEx in;
/*    */   
/*    */   public StAXExConnector(XMLStreamReaderEx in, XmlVisitor visitor) {
/* 59 */     super((XMLStreamReader)in, visitor);
/* 60 */     this.in = in;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void handleCharacters() throws XMLStreamException, SAXException {
/* 65 */     if (this.predictor.expectText()) {
/* 66 */       CharSequence pcdata = this.in.getPCDATA();
/* 67 */       if (pcdata instanceof Base64Data) {
/* 68 */         Base64Data bd = (Base64Data)pcdata;
/* 69 */         Base64Data binary = new Base64Data();
/* 70 */         if (!bd.hasData()) {
/* 71 */           binary.set(bd.getDataHandler());
/*    */         } else {
/* 73 */           binary.set(bd.get(), bd.getDataLen(), bd.getMimeType());
/*    */         } 
/*    */         
/* 76 */         this.visitor.text((CharSequence)binary);
/* 77 */         this.textReported = true;
/*    */       } else {
/* 79 */         this.buffer.append(pcdata);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\StAXExConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */