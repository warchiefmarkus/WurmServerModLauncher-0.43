/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.stream.XMLStreamException;
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
/*     */ public class UTF8XmlOutput
/*     */   extends XmlOutputAbstractImpl
/*     */ {
/*     */   protected final OutputStream out;
/*  61 */   private Encoded[] prefixes = new Encoded[8];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int prefixCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Encoded[] localNames;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private final Encoded textBuffer = new Encoded();
/*     */ 
/*     */ 
/*     */   
/*  84 */   protected final byte[] octetBuffer = new byte[1024];
/*     */ 
/*     */   
/*     */   protected int octetBufferIndex;
/*     */ 
/*     */   
/*     */   protected boolean closeStartTagPending = false;
/*     */ 
/*     */   
/*     */   private String header;
/*     */ 
/*     */   
/*     */   private final byte[] XMLNS_EQUALS;
/*     */ 
/*     */   
/*     */   private final byte[] XMLNS_COLON;
/*     */ 
/*     */   
/*     */   private final byte[] EQUALS;
/*     */ 
/*     */   
/*     */   private final byte[] CLOSE_TAG;
/*     */ 
/*     */   
/*     */   private final byte[] EMPTY_TAG;
/*     */   
/*     */   private final byte[] XML_DECL;
/*     */ 
/*     */   
/*     */   public void setHeader(String header) {
/* 114 */     this.header = header;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/* 119 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/*     */     
/* 121 */     this.octetBufferIndex = 0;
/* 122 */     if (!fragment) {
/* 123 */       write(this.XML_DECL);
/*     */     }
/* 125 */     if (this.header != null) {
/* 126 */       this.textBuffer.set(this.header);
/* 127 */       this.textBuffer.write(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/* 132 */     flushBuffer();
/* 133 */     super.endDocument(fragment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void closeStartTag() throws IOException {
/* 140 */     if (this.closeStartTagPending) {
/* 141 */       write(62);
/* 142 */       this.closeStartTagPending = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) throws IOException {
/* 147 */     closeStartTag();
/* 148 */     int base = pushNsDecls();
/* 149 */     write(60);
/* 150 */     writeName(prefix, localName);
/* 151 */     writeNsDecls(base);
/*     */   }
/*     */   
/*     */   public void beginStartTag(Name name) throws IOException {
/* 155 */     closeStartTag();
/* 156 */     int base = pushNsDecls();
/* 157 */     write(60);
/* 158 */     writeName(name);
/* 159 */     writeNsDecls(base);
/*     */   }
/*     */   
/*     */   private int pushNsDecls() {
/* 163 */     int total = this.nsContext.count();
/* 164 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/*     */     
/* 166 */     if (total > this.prefixes.length) {
/*     */       
/* 168 */       int m = Math.max(total, this.prefixes.length * 2);
/* 169 */       Encoded[] buf = new Encoded[m];
/* 170 */       System.arraycopy(this.prefixes, 0, buf, 0, this.prefixes.length);
/* 171 */       for (int j = this.prefixes.length; j < buf.length; j++)
/* 172 */         buf[j] = new Encoded(); 
/* 173 */       this.prefixes = buf;
/*     */     } 
/*     */     
/* 176 */     int base = Math.min(this.prefixCount, ns.getBase());
/* 177 */     int size = this.nsContext.count();
/* 178 */     for (int i = base; i < size; i++) {
/* 179 */       String p = this.nsContext.getPrefix(i);
/*     */       
/* 181 */       Encoded e = this.prefixes[i];
/*     */       
/* 183 */       if (p.length() == 0) {
/* 184 */         e.buf = EMPTY_BYTE_ARRAY;
/* 185 */         e.len = 0;
/*     */       } else {
/* 187 */         e.set(p);
/* 188 */         e.append(':');
/*     */       } 
/*     */     } 
/* 191 */     this.prefixCount = size;
/* 192 */     return base;
/*     */   }
/*     */   
/*     */   protected void writeNsDecls(int base) throws IOException {
/* 196 */     NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
/* 197 */     int size = this.nsContext.count();
/*     */     
/* 199 */     for (int i = ns.getBase(); i < size; i++) {
/* 200 */       writeNsDecl(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void writeNsDecl(int prefixIndex) throws IOException {
/* 207 */     String p = this.nsContext.getPrefix(prefixIndex);
/*     */     
/* 209 */     if (p.length() == 0) {
/* 210 */       if (this.nsContext.getCurrent().isRootElement() && this.nsContext.getNamespaceURI(prefixIndex).length() == 0) {
/*     */         return;
/*     */       }
/* 213 */       write(this.XMLNS_EQUALS);
/*     */     } else {
/* 215 */       Encoded e = this.prefixes[prefixIndex];
/* 216 */       write(this.XMLNS_COLON);
/* 217 */       write(e.buf, 0, e.len - 1);
/* 218 */       write(this.EQUALS);
/*     */     } 
/* 220 */     doText(this.nsContext.getNamespaceURI(prefixIndex), true);
/* 221 */     write(34);
/*     */   }
/*     */   
/*     */   private void writePrefix(int prefix) throws IOException {
/* 225 */     this.prefixes[prefix].write(this);
/*     */   }
/*     */   
/*     */   private void writeName(Name name) throws IOException {
/* 229 */     writePrefix(this.nsUriIndex2prefixIndex[name.nsUriIndex]);
/* 230 */     this.localNames[name.localNameIndex].write(this);
/*     */   }
/*     */   
/*     */   private void writeName(int prefix, String localName) throws IOException {
/* 234 */     writePrefix(prefix);
/* 235 */     this.textBuffer.set(localName);
/* 236 */     this.textBuffer.write(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void attribute(Name name, String value) throws IOException {
/* 241 */     write(32);
/* 242 */     if (name.nsUriIndex == -1) {
/* 243 */       this.localNames[name.localNameIndex].write(this);
/*     */     } else {
/* 245 */       writeName(name);
/* 246 */     }  write(this.EQUALS);
/* 247 */     doText(value, true);
/* 248 */     write(34);
/*     */   }
/*     */   
/*     */   public void attribute(int prefix, String localName, String value) throws IOException {
/* 252 */     write(32);
/* 253 */     if (prefix == -1) {
/* 254 */       this.textBuffer.set(localName);
/* 255 */       this.textBuffer.write(this);
/*     */     } else {
/* 257 */       writeName(prefix, localName);
/* 258 */     }  write(this.EQUALS);
/* 259 */     doText(value, true);
/* 260 */     write(34);
/*     */   }
/*     */   
/*     */   public void endStartTag() throws IOException {
/* 264 */     this.closeStartTagPending = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTag(Name name) throws IOException {
/* 269 */     if (this.closeStartTagPending) {
/* 270 */       write(this.EMPTY_TAG);
/* 271 */       this.closeStartTagPending = false;
/*     */     } else {
/* 273 */       write(this.CLOSE_TAG);
/* 274 */       writeName(name);
/* 275 */       write(62);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void endTag(int prefix, String localName) throws IOException {
/* 280 */     if (this.closeStartTagPending) {
/* 281 */       write(this.EMPTY_TAG);
/* 282 */       this.closeStartTagPending = false;
/*     */     } else {
/* 284 */       write(this.CLOSE_TAG);
/* 285 */       writeName(prefix, localName);
/* 286 */       write(62);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void text(String value, boolean needSP) throws IOException {
/* 291 */     closeStartTag();
/* 292 */     if (needSP)
/* 293 */       write(32); 
/* 294 */     doText(value, false);
/*     */   }
/*     */   
/*     */   public void text(Pcdata value, boolean needSP) throws IOException {
/* 298 */     closeStartTag();
/* 299 */     if (needSP)
/* 300 */       write(32); 
/* 301 */     value.writeTo(this);
/*     */   }
/*     */   
/*     */   private void doText(String value, boolean isAttribute) throws IOException {
/* 305 */     this.textBuffer.setEscape(value, isAttribute);
/* 306 */     this.textBuffer.write(this);
/*     */   }
/*     */   
/*     */   public final void text(int value) throws IOException {
/* 310 */     closeStartTag();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 317 */     boolean minus = (value < 0);
/* 318 */     this.textBuffer.ensureSize(11);
/* 319 */     byte[] buf = this.textBuffer.buf;
/* 320 */     int idx = 11;
/*     */     
/*     */     do {
/* 323 */       int r = value % 10;
/* 324 */       if (r < 0) r = -r; 
/* 325 */       buf[--idx] = (byte)(0x30 | r);
/* 326 */       value /= 10;
/* 327 */     } while (value != 0);
/*     */     
/* 329 */     if (minus) buf[--idx] = 45;
/*     */     
/* 331 */     write(buf, idx, 11 - idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(byte[] data, int dataLen) throws IOException {
/* 342 */     closeStartTag();
/*     */     
/* 344 */     int start = 0;
/*     */     
/* 346 */     while (dataLen > 0) {
/*     */       
/* 348 */       int batchSize = Math.min((this.octetBuffer.length - this.octetBufferIndex) / 4 * 3, dataLen);
/*     */ 
/*     */       
/* 351 */       this.octetBufferIndex = DatatypeConverterImpl._printBase64Binary(data, start, batchSize, this.octetBuffer, this.octetBufferIndex);
/*     */       
/* 353 */       if (batchSize < dataLen) {
/* 354 */         flushBuffer();
/*     */       }
/* 356 */       start += batchSize;
/* 357 */       dataLen -= batchSize;
/*     */     } 
/*     */   }
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
/*     */   public final void write(int i) throws IOException {
/* 376 */     if (this.octetBufferIndex < this.octetBuffer.length) {
/* 377 */       this.octetBuffer[this.octetBufferIndex++] = (byte)i;
/*     */     } else {
/* 379 */       this.out.write(this.octetBuffer);
/* 380 */       this.octetBufferIndex = 1;
/* 381 */       this.octetBuffer[0] = (byte)i;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void write(byte[] b) throws IOException {
/* 386 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   protected final void write(byte[] b, int start, int length) throws IOException {
/* 390 */     if (this.octetBufferIndex + length < this.octetBuffer.length) {
/* 391 */       System.arraycopy(b, start, this.octetBuffer, this.octetBufferIndex, length);
/* 392 */       this.octetBufferIndex += length;
/*     */     } else {
/* 394 */       this.out.write(this.octetBuffer, 0, this.octetBufferIndex);
/* 395 */       this.out.write(b, start, length);
/* 396 */       this.octetBufferIndex = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final void flushBuffer() throws IOException {
/* 401 */     this.out.write(this.octetBuffer, 0, this.octetBufferIndex);
/* 402 */     this.octetBufferIndex = 0;
/*     */   }
/*     */   
/*     */   static byte[] toBytes(String s) {
/* 406 */     byte[] buf = new byte[s.length()];
/* 407 */     for (int i = s.length() - 1; i >= 0; i--)
/* 408 */       buf[i] = (byte)s.charAt(i); 
/* 409 */     return buf;
/*     */   }
/*     */ 
/*     */   
/*     */   public UTF8XmlOutput(OutputStream out, Encoded[] localNames) {
/* 414 */     this.XMLNS_EQUALS = (byte[])_XMLNS_EQUALS.clone();
/* 415 */     this.XMLNS_COLON = (byte[])_XMLNS_COLON.clone();
/* 416 */     this.EQUALS = (byte[])_EQUALS.clone();
/* 417 */     this.CLOSE_TAG = (byte[])_CLOSE_TAG.clone();
/* 418 */     this.EMPTY_TAG = (byte[])_EMPTY_TAG.clone();
/* 419 */     this.XML_DECL = (byte[])_XML_DECL.clone(); this.out = out;
/*     */     this.localNames = localNames;
/*     */     for (int i = 0; i < this.prefixes.length; i++)
/* 422 */       this.prefixes[i] = new Encoded();  } private static final byte[] _XMLNS_EQUALS = toBytes(" xmlns=\"");
/* 423 */   private static final byte[] _XMLNS_COLON = toBytes(" xmlns:");
/* 424 */   private static final byte[] _EQUALS = toBytes("=\"");
/* 425 */   private static final byte[] _CLOSE_TAG = toBytes("</");
/* 426 */   private static final byte[] _EMPTY_TAG = toBytes("/>");
/* 427 */   private static final byte[] _XML_DECL = toBytes("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
/*     */ 
/*     */   
/* 430 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\UTF8XmlOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */