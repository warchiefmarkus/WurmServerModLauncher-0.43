/*     */ package javax.xml.stream.util;
/*     */ 
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
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
/*     */ public class StreamReaderDelegate
/*     */   implements XMLStreamReader
/*     */ {
/*     */   private XMLStreamReader reader;
/*     */   
/*     */   public StreamReaderDelegate() {}
/*     */   
/*     */   public StreamReaderDelegate(XMLStreamReader reader) {
/*  37 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(XMLStreamReader reader) {
/*  45 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamReader getParent() {
/*  53 */     return this.reader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int next() throws XMLStreamException {
/*  59 */     return this.reader.next();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextTag() throws XMLStreamException {
/*  65 */     return this.reader.nextTag();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElementText() throws XMLStreamException {
/*  71 */     return this.reader.getElementText();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
/*  77 */     this.reader.require(type, namespaceURI, localName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() throws XMLStreamException {
/*  83 */     return this.reader.hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/*  89 */     this.reader.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/*  94 */     return this.reader.getNamespaceURI(prefix);
/*     */   }
/*     */   
/*     */   public NamespaceContext getNamespaceContext() {
/*  98 */     return this.reader.getNamespaceContext();
/*     */   }
/*     */   
/*     */   public boolean isStartElement() {
/* 102 */     return this.reader.isStartElement();
/*     */   }
/*     */   
/*     */   public boolean isEndElement() {
/* 106 */     return this.reader.isEndElement();
/*     */   }
/*     */   
/*     */   public boolean isCharacters() {
/* 110 */     return this.reader.isCharacters();
/*     */   }
/*     */   
/*     */   public boolean isWhiteSpace() {
/* 114 */     return this.reader.isWhiteSpace();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAttributeValue(String namespaceUri, String localName) {
/* 120 */     return this.reader.getAttributeValue(namespaceUri, localName);
/*     */   }
/*     */   
/*     */   public int getAttributeCount() {
/* 124 */     return this.reader.getAttributeCount();
/*     */   }
/*     */   
/*     */   public QName getAttributeName(int index) {
/* 128 */     return this.reader.getAttributeName(index);
/*     */   }
/*     */   
/*     */   public String getAttributePrefix(int index) {
/* 132 */     return this.reader.getAttributePrefix(index);
/*     */   }
/*     */   
/*     */   public String getAttributeNamespace(int index) {
/* 136 */     return this.reader.getAttributeNamespace(index);
/*     */   }
/*     */   
/*     */   public String getAttributeLocalName(int index) {
/* 140 */     return this.reader.getAttributeLocalName(index);
/*     */   }
/*     */   
/*     */   public String getAttributeType(int index) {
/* 144 */     return this.reader.getAttributeType(index);
/*     */   }
/*     */   
/*     */   public String getAttributeValue(int index) {
/* 148 */     return this.reader.getAttributeValue(index);
/*     */   }
/*     */   
/*     */   public boolean isAttributeSpecified(int index) {
/* 152 */     return this.reader.isAttributeSpecified(index);
/*     */   }
/*     */   
/*     */   public int getNamespaceCount() {
/* 156 */     return this.reader.getNamespaceCount();
/*     */   }
/*     */   
/*     */   public String getNamespacePrefix(int index) {
/* 160 */     return this.reader.getNamespacePrefix(index);
/*     */   }
/*     */   
/*     */   public String getNamespaceURI(int index) {
/* 164 */     return this.reader.getNamespaceURI(index);
/*     */   }
/*     */   
/*     */   public int getEventType() {
/* 168 */     return this.reader.getEventType();
/*     */   }
/*     */   
/*     */   public String getText() {
/* 172 */     return this.reader.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
/* 180 */     return this.reader.getTextCharacters(sourceStart, target, targetStart, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] getTextCharacters() {
/* 188 */     return this.reader.getTextCharacters();
/*     */   }
/*     */   
/*     */   public int getTextStart() {
/* 192 */     return this.reader.getTextStart();
/*     */   }
/*     */   
/*     */   public int getTextLength() {
/* 196 */     return this.reader.getTextLength();
/*     */   }
/*     */   
/*     */   public String getEncoding() {
/* 200 */     return this.reader.getEncoding();
/*     */   }
/*     */   
/*     */   public boolean hasText() {
/* 204 */     return this.reader.hasText();
/*     */   }
/*     */   
/*     */   public Location getLocation() {
/* 208 */     return this.reader.getLocation();
/*     */   }
/*     */   
/*     */   public QName getName() {
/* 212 */     return this.reader.getName();
/*     */   }
/*     */   
/*     */   public String getLocalName() {
/* 216 */     return this.reader.getLocalName();
/*     */   }
/*     */   
/*     */   public boolean hasName() {
/* 220 */     return this.reader.hasName();
/*     */   }
/*     */   
/*     */   public String getNamespaceURI() {
/* 224 */     return this.reader.getNamespaceURI();
/*     */   }
/*     */   
/*     */   public String getPrefix() {
/* 228 */     return this.reader.getPrefix();
/*     */   }
/*     */   
/*     */   public String getVersion() {
/* 232 */     return this.reader.getVersion();
/*     */   }
/*     */   
/*     */   public boolean isStandalone() {
/* 236 */     return this.reader.isStandalone();
/*     */   }
/*     */   
/*     */   public boolean standaloneSet() {
/* 240 */     return this.reader.standaloneSet();
/*     */   }
/*     */   
/*     */   public String getCharacterEncodingScheme() {
/* 244 */     return this.reader.getCharacterEncodingScheme();
/*     */   }
/*     */   
/*     */   public String getPITarget() {
/* 248 */     return this.reader.getPITarget();
/*     */   }
/*     */   
/*     */   public String getPIData() {
/* 252 */     return this.reader.getPIData();
/*     */   }
/*     */   
/*     */   public Object getProperty(String name) {
/* 256 */     return this.reader.getProperty(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\strea\\util\StreamReaderDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */