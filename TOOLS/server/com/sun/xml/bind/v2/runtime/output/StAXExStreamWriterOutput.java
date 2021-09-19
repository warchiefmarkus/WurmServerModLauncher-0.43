/*    */ package com.sun.xml.bind.v2.runtime.output;
/*    */ 
/*    */ import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamWriter;
/*    */ import org.jvnet.staxex.XMLStreamWriterEx;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class StAXExStreamWriterOutput
/*    */   extends XMLStreamWriterOutput
/*    */ {
/*    */   private final XMLStreamWriterEx out;
/*    */   
/*    */   public StAXExStreamWriterOutput(XMLStreamWriterEx out) {
/* 54 */     super((XMLStreamWriter)out);
/* 55 */     this.out = out;
/*    */   }
/*    */   
/*    */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws XMLStreamException {
/* 59 */     if (needsSeparatingWhitespace) {
/* 60 */       this.out.writeCharacters(" ");
/*    */     }
/*    */     
/* 63 */     if (!(value instanceof Base64Data)) {
/* 64 */       this.out.writeCharacters(value.toString());
/*    */     } else {
/* 66 */       Base64Data v = (Base64Data)value;
/* 67 */       this.out.writeBinary(v.getDataHandler());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\StAXExStreamWriterOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */