/*    */ package 1.0.com.sun.tools.xjc.reader.internalizer;
/*    */ 
/*    */ import com.sun.xml.util.XmlChars;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.XMLReader;
/*    */ import org.xml.sax.helpers.XMLFilterImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class WhitespaceStripper
/*    */   extends XMLFilterImpl
/*    */ {
/* 30 */   private int state = 0;
/*    */   
/* 32 */   private char[] buf = new char[1024];
/* 33 */   private int bufLen = 0;
/*    */   
/*    */   private static final int AFTER_START_ELEMENT = 1;
/*    */   private static final int AFTER_END_ELEMENT = 2;
/*    */   
/*    */   public WhitespaceStripper(XMLReader reader) {
/* 39 */     setParent(reader);
/*    */   } public void characters(char[] ch, int start, int length) throws SAXException {
/*    */     int len;
/*    */     int i;
/* 43 */     switch (this.state) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       case 1:
/* 49 */         if (this.bufLen + length > this.buf.length) {
/*    */           
/* 51 */           char[] newBuf = new char[Math.max(this.bufLen + length, this.buf.length * 2)];
/* 52 */           System.arraycopy(this.buf, 0, newBuf, 0, this.bufLen);
/* 53 */           this.buf = newBuf;
/*    */         } 
/* 55 */         System.arraycopy(ch, start, this.buf, this.bufLen, length);
/* 56 */         this.bufLen += length;
/*    */         break;
/*    */       
/*    */       case 2:
/* 60 */         len = start + length;
/* 61 */         for (i = start; i < len; i++) {
/* 62 */           if (!XmlChars.isSpace(ch[i])) {
/* 63 */             super.characters(ch, start, length);
/*    */             return;
/*    */           } 
/*    */         } 
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
/* 72 */     processPendingText();
/* 73 */     super.startElement(uri, localName, qName, atts);
/* 74 */     this.state = 1;
/* 75 */     this.bufLen = 0;
/*    */   }
/*    */   
/*    */   public void endElement(String uri, String localName, String qName) throws SAXException {
/* 79 */     processPendingText();
/* 80 */     super.endElement(uri, localName, qName);
/* 81 */     this.state = 2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void processPendingText() throws SAXException {
/* 89 */     if (this.state == 1)
/* 90 */       for (int i = this.bufLen - 1; i >= 0; i--) {
/* 91 */         if (!XmlChars.isSpace(this.buf[i])) {
/* 92 */           super.characters(this.buf, 0, this.bufLen);
/*    */           return;
/*    */         } 
/*    */       }  
/*    */   }
/*    */   
/*    */   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\internalizer\WhitespaceStripper.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */