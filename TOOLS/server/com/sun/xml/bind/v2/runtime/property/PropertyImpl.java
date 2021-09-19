/*    */ package com.sun.xml.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimePropertyInfo;
/*    */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*    */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*    */ import com.sun.xml.bind.v2.runtime.reflect.Accessor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class PropertyImpl<BeanT>
/*    */   implements Property<BeanT>
/*    */ {
/*    */   protected final String fieldName;
/*    */   
/*    */   public PropertyImpl(JAXBContextImpl context, RuntimePropertyInfo prop) {
/* 61 */     this.fieldName = prop.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {}
/*    */ 
/*    */   
/*    */   public void serializeURIs(BeanT o, XMLSerializer w) throws SAXException, AccessorException {}
/*    */   
/*    */   public boolean hasSerializeURIAction() {
/* 71 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   public void wrapUp() {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\PropertyImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */