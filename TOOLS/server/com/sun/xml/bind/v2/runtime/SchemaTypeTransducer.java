/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import java.io.IOException;
/*    */ import javax.xml.namespace.QName;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SchemaTypeTransducer<V>
/*    */   extends FilterTransducer<V>
/*    */ {
/*    */   private final QName schemaType;
/*    */   
/*    */   public SchemaTypeTransducer(Transducer<V> core, QName schemaType) {
/* 64 */     super(core);
/* 65 */     this.schemaType = schemaType;
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence print(V o) throws AccessorException {
/* 70 */     XMLSerializer w = XMLSerializer.getInstance();
/* 71 */     QName old = w.setSchemaType(this.schemaType);
/*    */     try {
/* 73 */       return this.core.print(o);
/*    */     } finally {
/* 75 */       w.setSchemaType(old);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 81 */     QName old = w.setSchemaType(this.schemaType);
/*    */     try {
/* 83 */       this.core.writeText(w, o, fieldName);
/*    */     } finally {
/* 85 */       w.setSchemaType(old);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 91 */     QName old = w.setSchemaType(this.schemaType);
/*    */     try {
/* 93 */       this.core.writeLeafElement(w, tagName, o, fieldName);
/*    */     } finally {
/* 95 */       w.setSchemaType(old);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\SchemaTypeTransducer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */