/*    */ package com.sun.xml.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.istack.NotNull;
/*    */ import com.sun.xml.bind.api.AccessorException;
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
/*    */ public class InlineBinaryTransducer<V>
/*    */   extends FilterTransducer<V>
/*    */ {
/*    */   public InlineBinaryTransducer(Transducer<V> core) {
/* 55 */     super(core);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public CharSequence print(@NotNull V o) throws AccessorException {
/* 60 */     XMLSerializer w = XMLSerializer.getInstance();
/* 61 */     boolean old = w.setInlineBinaryFlag(true);
/*    */     try {
/* 63 */       return this.core.print(o);
/*    */     } finally {
/* 65 */       w.setInlineBinaryFlag(old);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 71 */     boolean old = w.setInlineBinaryFlag(true);
/*    */     try {
/* 73 */       this.core.writeText(w, o, fieldName);
/*    */     } finally {
/* 75 */       w.setInlineBinaryFlag(old);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 81 */     boolean old = w.setInlineBinaryFlag(true);
/*    */     try {
/* 83 */       this.core.writeLeafElement(w, tagName, o, fieldName);
/*    */     } finally {
/* 85 */       w.setInlineBinaryFlag(old);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\InlineBinaryTransducer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */