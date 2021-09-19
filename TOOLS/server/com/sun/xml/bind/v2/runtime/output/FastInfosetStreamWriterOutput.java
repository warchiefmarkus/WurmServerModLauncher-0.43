/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
/*     */ import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.jvnet.fastinfoset.VocabularyApplicationData;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public final class FastInfosetStreamWriterOutput
/*     */   extends XMLStreamWriterOutput
/*     */ {
/*     */   private final StAXDocumentSerializer fiout;
/*     */   private final Encoded[] localNames;
/*     */   private final TablesPerJAXBContext tables;
/*     */   
/*     */   static final class TablesPerJAXBContext
/*     */   {
/*     */     final int[] elementIndexes;
/*     */     final int[] attributeIndexes;
/*     */     final int[] localNameIndexes;
/*     */     int indexOffset;
/*     */     int maxIndex;
/*     */     boolean requiresClear;
/*     */     
/*     */     TablesPerJAXBContext(JAXBContextImpl context, int initialIndexOffset) {
/* 117 */       this.elementIndexes = new int[context.getNumberOfElementNames()];
/* 118 */       this.attributeIndexes = new int[context.getNumberOfAttributeNames()];
/* 119 */       this.localNameIndexes = new int[context.getNumberOfLocalNames()];
/*     */       
/* 121 */       this.indexOffset = 1;
/* 122 */       this.maxIndex = initialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void requireClearTables() {
/* 129 */       this.requiresClear = true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clearOrResetTables(int intialIndexOffset) {
/* 139 */       if (this.requiresClear) {
/* 140 */         this.requiresClear = false;
/*     */ 
/*     */         
/* 143 */         this.indexOffset += this.maxIndex;
/*     */         
/* 145 */         this.maxIndex = intialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length;
/*     */ 
/*     */         
/* 148 */         if (this.indexOffset + this.maxIndex < 0) {
/* 149 */           clearAll();
/*     */         }
/*     */       } else {
/*     */         
/* 153 */         this.maxIndex = intialIndexOffset + this.elementIndexes.length + this.attributeIndexes.length;
/*     */ 
/*     */         
/* 156 */         if (this.indexOffset + this.maxIndex < 0) {
/* 157 */           resetAll();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void clearAll() {
/* 163 */       clear(this.elementIndexes);
/* 164 */       clear(this.attributeIndexes);
/* 165 */       clear(this.localNameIndexes);
/* 166 */       this.indexOffset = 1;
/*     */     }
/*     */     
/*     */     private void clear(int[] array) {
/* 170 */       for (int i = 0; i < array.length; i++) {
/* 171 */         array[i] = 0;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void incrementMaxIndexValue() {
/* 182 */       this.maxIndex++;
/*     */ 
/*     */       
/* 185 */       if (this.indexOffset + this.maxIndex < 0) {
/* 186 */         resetAll();
/*     */       }
/*     */     }
/*     */     
/*     */     private void resetAll() {
/* 191 */       clear(this.elementIndexes);
/* 192 */       clear(this.attributeIndexes);
/* 193 */       clear(this.localNameIndexes);
/* 194 */       this.indexOffset = 1;
/*     */     }
/*     */     
/*     */     private void reset(int[] array) {
/* 198 */       for (int i = 0; i < array.length; i++) {
/* 199 */         if (array[i] > this.indexOffset) {
/* 200 */           array[i] = array[i] - this.indexOffset + 1;
/*     */         } else {
/* 202 */           array[i] = 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AppData
/*     */     implements VocabularyApplicationData
/*     */   {
/* 216 */     final Map<JAXBContext, FastInfosetStreamWriterOutput.TablesPerJAXBContext> contexts = new WeakHashMap<JAXBContext, FastInfosetStreamWriterOutput.TablesPerJAXBContext>();
/*     */     
/* 218 */     final Collection<FastInfosetStreamWriterOutput.TablesPerJAXBContext> collectionOfContexts = this.contexts.values();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clear() {
/* 224 */       for (FastInfosetStreamWriterOutput.TablesPerJAXBContext c : this.collectionOfContexts) {
/* 225 */         c.requireClearTables();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public FastInfosetStreamWriterOutput(StAXDocumentSerializer out, JAXBContextImpl context) {
/* 231 */     super((XMLStreamWriter)out);
/*     */     
/* 233 */     this.fiout = out;
/* 234 */     this.localNames = context.getUTF8NameTable();
/*     */     
/* 236 */     VocabularyApplicationData vocabAppData = this.fiout.getVocabularyApplicationData();
/* 237 */     AppData appData = null;
/* 238 */     if (vocabAppData == null || !(vocabAppData instanceof AppData)) {
/* 239 */       appData = new AppData();
/* 240 */       this.fiout.setVocabularyApplicationData(appData);
/*     */     } else {
/* 242 */       appData = (AppData)vocabAppData;
/*     */     } 
/*     */     
/* 245 */     TablesPerJAXBContext tablesPerContext = appData.contexts.get(context);
/* 246 */     if (tablesPerContext != null) {
/* 247 */       this.tables = tablesPerContext;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 252 */       this.tables.clearOrResetTables(out.getLocalNameIndex());
/*     */     } else {
/* 254 */       this.tables = new TablesPerJAXBContext(context, out.getLocalNameIndex());
/* 255 */       appData.contexts.put(context, this.tables);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/* 262 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */     
/* 264 */     if (fragment)
/* 265 */       this.fiout.initiateLowLevelWriting(); 
/*     */   }
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/* 269 */     super.endDocument(fragment);
/*     */   }
/*     */   
/*     */   public void beginStartTag(Name name) throws IOException {
/* 273 */     this.fiout.writeLowLevelTerminationAndMark();
/*     */     
/* 275 */     if (this.nsContext.getCurrent().count() == 0) {
/* 276 */       int qNameIndex = this.tables.elementIndexes[name.qNameIndex] - this.tables.indexOffset;
/* 277 */       if (qNameIndex >= 0) {
/* 278 */         this.fiout.writeLowLevelStartElementIndexed(0, qNameIndex);
/*     */       } else {
/* 280 */         this.tables.elementIndexes[name.qNameIndex] = this.fiout.getNextElementIndex() + this.tables.indexOffset;
/*     */         
/* 282 */         int prefix = this.nsUriIndex2prefixIndex[name.nsUriIndex];
/* 283 */         writeLiteral(60, name, this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix));
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 289 */       beginStartTagWithNamespaces(name);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beginStartTagWithNamespaces(Name name) throws IOException {
/* 294 */     NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/*     */     
/* 296 */     this.fiout.writeLowLevelStartNamespaces();
/* 297 */     for (int i = nse.count() - 1; i >= 0; i--) {
/* 298 */       String uri = nse.getNsUri(i);
/* 299 */       if (uri.length() != 0 || nse.getBase() != 1)
/*     */       {
/* 301 */         this.fiout.writeLowLevelNamespace(nse.getPrefix(i), uri); } 
/*     */     } 
/* 303 */     this.fiout.writeLowLevelEndNamespaces();
/*     */     
/* 305 */     int qNameIndex = this.tables.elementIndexes[name.qNameIndex] - this.tables.indexOffset;
/* 306 */     if (qNameIndex >= 0) {
/* 307 */       this.fiout.writeLowLevelStartElementIndexed(0, qNameIndex);
/*     */     } else {
/* 309 */       this.tables.elementIndexes[name.qNameIndex] = this.fiout.getNextElementIndex() + this.tables.indexOffset;
/*     */       
/* 311 */       int prefix = this.nsUriIndex2prefixIndex[name.nsUriIndex];
/* 312 */       writeLiteral(60, name, this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attribute(Name name, String value) throws IOException {
/* 320 */     this.fiout.writeLowLevelStartAttributes();
/*     */     
/* 322 */     int qNameIndex = this.tables.attributeIndexes[name.qNameIndex] - this.tables.indexOffset;
/* 323 */     if (qNameIndex >= 0) {
/* 324 */       this.fiout.writeLowLevelAttributeIndexed(qNameIndex);
/*     */     } else {
/* 326 */       this.tables.attributeIndexes[name.qNameIndex] = this.fiout.getNextAttributeIndex() + this.tables.indexOffset;
/*     */       
/* 328 */       int namespaceURIId = name.nsUriIndex;
/* 329 */       if (namespaceURIId == -1) {
/* 330 */         writeLiteral(120, name, "", "");
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 335 */         int prefix = this.nsUriIndex2prefixIndex[namespaceURIId];
/* 336 */         writeLiteral(120, name, this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     this.fiout.writeLowLevelAttributeValue(value);
/*     */   }
/*     */   
/*     */   private void writeLiteral(int type, Name name, String prefix, String namespaceURI) throws IOException {
/* 347 */     int localNameIndex = this.tables.localNameIndexes[name.localNameIndex] - this.tables.indexOffset;
/*     */     
/* 349 */     if (localNameIndex < 0) {
/* 350 */       this.tables.localNameIndexes[name.localNameIndex] = this.fiout.getNextLocalNameIndex() + this.tables.indexOffset;
/*     */       
/* 352 */       this.fiout.writeLowLevelStartNameLiteral(type, prefix, (this.localNames[name.localNameIndex]).buf, namespaceURI);
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 358 */       this.fiout.writeLowLevelStartNameLiteral(type, prefix, localNameIndex, namespaceURI);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endStartTag() throws IOException {
/* 367 */     this.fiout.writeLowLevelEndStartElement();
/*     */   }
/*     */   
/*     */   public void endTag(Name name) throws IOException {
/* 371 */     this.fiout.writeLowLevelEndElement();
/*     */   }
/*     */   
/*     */   public void endTag(int prefix, String localName) throws IOException {
/* 375 */     this.fiout.writeLowLevelEndElement();
/*     */   }
/*     */ 
/*     */   
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException {
/* 380 */     if (needsSeparatingWhitespace) {
/* 381 */       this.fiout.writeLowLevelText(" ");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 386 */     if (!(value instanceof Base64Data)) {
/* 387 */       int len = value.length();
/* 388 */       if (len < this.buf.length) {
/* 389 */         value.writeTo(this.buf, 0);
/* 390 */         this.fiout.writeLowLevelText(this.buf, len);
/*     */       } else {
/* 392 */         this.fiout.writeLowLevelText(value.toString());
/*     */       } 
/*     */     } else {
/* 395 */       Base64Data dataValue = (Base64Data)value;
/*     */       
/* 397 */       this.fiout.writeLowLevelOctets(dataValue.get(), dataValue.getDataLen());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException {
/* 403 */     if (needsSeparatingWhitespace) {
/* 404 */       this.fiout.writeLowLevelText(" ");
/*     */     }
/* 406 */     this.fiout.writeLowLevelText(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) throws IOException {
/* 411 */     this.fiout.writeLowLevelTerminationAndMark();
/*     */     
/* 413 */     int type = 0;
/* 414 */     if (this.nsContext.getCurrent().count() > 0) {
/* 415 */       NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/*     */       
/* 417 */       this.fiout.writeLowLevelStartNamespaces();
/* 418 */       for (int i = nse.count() - 1; i >= 0; i--) {
/* 419 */         String uri = nse.getNsUri(i);
/* 420 */         if (uri.length() != 0 || nse.getBase() != 1)
/*     */         {
/* 422 */           this.fiout.writeLowLevelNamespace(nse.getPrefix(i), uri); } 
/*     */       } 
/* 424 */       this.fiout.writeLowLevelEndNamespaces();
/*     */       
/* 426 */       type = 0;
/*     */     } 
/*     */     
/* 429 */     boolean isIndexed = this.fiout.writeLowLevelStartElement(type, this.nsContext.getPrefix(prefix), localName, this.nsContext.getNamespaceURI(prefix));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 435 */     if (!isIndexed)
/* 436 */       this.tables.incrementMaxIndexValue(); 
/*     */   }
/*     */   public void attribute(int prefix, String localName, String value) throws IOException {
/*     */     boolean isIndexed;
/* 440 */     this.fiout.writeLowLevelStartAttributes();
/*     */ 
/*     */     
/* 443 */     if (prefix == -1) {
/* 444 */       isIndexed = this.fiout.writeLowLevelAttribute("", "", localName);
/*     */     } else {
/* 446 */       isIndexed = this.fiout.writeLowLevelAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 451 */     if (!isIndexed) {
/* 452 */       this.tables.incrementMaxIndexValue();
/*     */     }
/* 454 */     this.fiout.writeLowLevelAttributeValue(value);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\FastInfosetStreamWriterOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */