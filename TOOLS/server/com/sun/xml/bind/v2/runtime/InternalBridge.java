/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.bind.api.Bridge;
/*    */ import com.sun.xml.bind.api.JAXBRIContext;
/*    */ import java.io.IOException;
/*    */ import javax.xml.stream.XMLStreamException;
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
/*    */ abstract class InternalBridge<T>
/*    */   extends Bridge<T>
/*    */ {
/*    */   protected InternalBridge(JAXBContextImpl context) {
/* 53 */     super(context);
/*    */   }
/*    */   
/*    */   public JAXBContextImpl getContext() {
/* 57 */     return this.context;
/*    */   }
/*    */   
/*    */   abstract void marshal(T paramT, XMLSerializer paramXMLSerializer) throws IOException, SAXException, XMLStreamException;
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\InternalBridge.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */