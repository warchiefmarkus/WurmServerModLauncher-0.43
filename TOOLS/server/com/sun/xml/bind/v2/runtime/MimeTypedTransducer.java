/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.bind.api.AccessorException;
/*    */ import java.io.IOException;
/*    */ import javax.activation.MimeType;
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
/*    */ public final class MimeTypedTransducer<V>
/*    */   extends FilterTransducer<V>
/*    */ {
/*    */   private final MimeType expectedMimeType;
/*    */   
/*    */   public MimeTypedTransducer(Transducer<V> core, MimeType expectedMimeType) {
/* 63 */     super(core);
/* 64 */     this.expectedMimeType = expectedMimeType;
/*    */   }
/*    */ 
/*    */   
/*    */   public CharSequence print(V o) throws AccessorException {
/* 69 */     XMLSerializer w = XMLSerializer.getInstance();
/* 70 */     MimeType old = w.setExpectedMimeType(this.expectedMimeType);
/*    */     try {
/* 72 */       return this.core.print(o);
/*    */     } finally {
/* 74 */       w.setExpectedMimeType(old);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 80 */     MimeType old = w.setExpectedMimeType(this.expectedMimeType);
/*    */     try {
/* 82 */       this.core.writeText(w, o, fieldName);
/*    */     } finally {
/* 84 */       w.setExpectedMimeType(old);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 90 */     MimeType old = w.setExpectedMimeType(this.expectedMimeType);
/*    */     try {
/* 92 */       this.core.writeLeafElement(w, tagName, o, fieldName);
/*    */     } finally {
/* 94 */       w.setExpectedMimeType(old);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\MimeTypedTransducer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */