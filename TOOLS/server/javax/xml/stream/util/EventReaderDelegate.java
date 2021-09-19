/*     */ package javax.xml.stream.util;
/*     */ 
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventReaderDelegate
/*     */   implements XMLEventReader
/*     */ {
/*     */   private XMLEventReader reader;
/*     */   
/*     */   public EventReaderDelegate() {}
/*     */   
/*     */   public EventReaderDelegate(XMLEventReader reader) {
/*  38 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(XMLEventReader reader) {
/*  46 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLEventReader getParent() {
/*  54 */     return this.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLEvent nextEvent() throws XMLStreamException {
/*  60 */     return this.reader.nextEvent();
/*     */   }
/*     */   
/*     */   public Object next() {
/*  64 */     return this.reader.next();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  69 */     return this.reader.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLEvent peek() throws XMLStreamException {
/*  75 */     return this.reader.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/*  81 */     this.reader.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElementText() throws XMLStreamException {
/*  87 */     return this.reader.getElementText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLEvent nextTag() throws XMLStreamException {
/*  93 */     return this.reader.nextTag();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  99 */     return this.reader.getProperty(name);
/*     */   }
/*     */   
/*     */   public void remove() {
/* 103 */     this.reader.remove();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\strea\\util\EventReaderDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */