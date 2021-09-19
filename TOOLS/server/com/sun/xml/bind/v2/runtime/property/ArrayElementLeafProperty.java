/*    */ package com.sun.xml.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*    */ import com.sun.xml.bind.v2.model.runtime.RuntimeTypeRef;
/*    */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*    */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*    */ import com.sun.xml.bind.v2.runtime.Transducer;
/*    */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ArrayElementLeafProperty<BeanT, ListT, ItemT>
/*    */   extends ArrayElementProperty<BeanT, ListT, ItemT>
/*    */ {
/*    */   private final Transducer<ItemT> xducer;
/*    */   
/*    */   public ArrayElementLeafProperty(JAXBContextImpl p, RuntimeElementPropertyInfo prop) {
/* 66 */     super(p, prop);
/*    */ 
/*    */     
/* 69 */     assert prop.getTypes().size() == 1;
/*    */ 
/*    */     
/* 72 */     this.xducer = ((RuntimeTypeRef)prop.getTypes().get(0)).getTransducer();
/* 73 */     assert this.xducer != null;
/*    */   }
/*    */   
/*    */   public void serializeItem(JaxBeanInfo bi, ItemT item, XMLSerializer w) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 77 */     this.xducer.declareNamespace(item, w);
/* 78 */     w.endNamespaceDecls(item);
/* 79 */     w.endAttributes();
/*    */ 
/*    */     
/* 82 */     this.xducer.writeText(w, item, this.fieldName);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\property\ArrayElementLeafProperty.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */