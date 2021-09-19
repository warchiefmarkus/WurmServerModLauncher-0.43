/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.MarshallerImpl;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
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
/*     */ public class XMLStreamWriterOutput
/*     */   extends XmlOutputAbstractImpl
/*     */ {
/*     */   private final XMLStreamWriter out;
/*     */   
/*     */   public static XmlOutput create(XMLStreamWriter out, JAXBContextImpl context) {
/*  70 */     Class<?> writerClass = out.getClass();
/*  71 */     if (writerClass == FI_STAX_WRITER_CLASS) {
/*     */       try {
/*  73 */         return FI_OUTPUT_CTOR.newInstance(new Object[] { out, context });
/*  74 */       } catch (Exception e) {}
/*     */     }
/*     */     
/*  77 */     if (STAXEX_WRITER_CLASS != null && STAXEX_WRITER_CLASS.isAssignableFrom(writerClass)) {
/*     */       try {
/*  79 */         return STAXEX_OUTPUT_CTOR.newInstance(new Object[] { out });
/*  80 */       } catch (Exception e) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     return new XMLStreamWriterOutput(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   protected final char[] buf = new char[256];
/*     */   
/*     */   protected XMLStreamWriterOutput(XMLStreamWriter out) {
/*  94 */     this.out = out;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
/*  99 */     super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
/* 100 */     if (!fragment)
/* 101 */       this.out.writeStartDocument(); 
/*     */   }
/*     */   
/*     */   public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
/* 105 */     if (!fragment) {
/* 106 */       this.out.writeEndDocument();
/* 107 */       this.out.flush();
/*     */     } 
/* 109 */     super.endDocument(fragment);
/*     */   }
/*     */   
/*     */   public void beginStartTag(int prefix, String localName) throws IOException, XMLStreamException {
/* 113 */     this.out.writeStartElement(this.nsContext.getPrefix(prefix), localName, this.nsContext.getNamespaceURI(prefix));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
/* 119 */     if (nse.count() > 0)
/* 120 */       for (int i = nse.count() - 1; i >= 0; i--) {
/* 121 */         String uri = nse.getNsUri(i);
/* 122 */         if (uri.length() != 0 || nse.getBase() != 1)
/*     */         {
/* 124 */           this.out.writeNamespace(nse.getPrefix(i), uri);
/*     */         }
/*     */       }  
/*     */   }
/*     */   
/*     */   public void attribute(int prefix, String localName, String value) throws IOException, XMLStreamException {
/* 130 */     if (prefix == -1) {
/* 131 */       this.out.writeAttribute(localName, value);
/*     */     } else {
/* 133 */       this.out.writeAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endStartTag() throws IOException, SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTag(int prefix, String localName) throws IOException, SAXException, XMLStreamException {
/* 144 */     this.out.writeEndElement();
/*     */   }
/*     */   
/*     */   public void text(String value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 148 */     if (needsSeparatingWhitespace)
/* 149 */       this.out.writeCharacters(" "); 
/* 150 */     this.out.writeCharacters(value);
/*     */   }
/*     */   
/*     */   public void text(Pcdata value, boolean needsSeparatingWhitespace) throws IOException, SAXException, XMLStreamException {
/* 154 */     if (needsSeparatingWhitespace) {
/* 155 */       this.out.writeCharacters(" ");
/*     */     }
/* 157 */     int len = value.length();
/* 158 */     if (len < this.buf.length) {
/* 159 */       value.writeTo(this.buf, 0);
/* 160 */       this.out.writeCharacters(this.buf, 0, len);
/*     */     } else {
/* 162 */       this.out.writeCharacters(value.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   private static final Class FI_STAX_WRITER_CLASS = initFIStAXWriterClass();
/* 171 */   private static final Constructor<? extends XmlOutput> FI_OUTPUT_CTOR = initFastInfosetOutputClass();
/*     */   
/*     */   private static Class initFIStAXWriterClass() {
/*     */     try {
/* 175 */       Class<?> llfisw = MarshallerImpl.class.getClassLoader().loadClass("org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter");
/*     */       
/* 177 */       Class<?> sds = MarshallerImpl.class.getClassLoader().loadClass("com.sun.xml.fastinfoset.stax.StAXDocumentSerializer");
/*     */ 
/*     */       
/* 180 */       if (llfisw.isAssignableFrom(sds)) {
/* 181 */         return sds;
/*     */       }
/* 183 */       return null;
/* 184 */     } catch (Throwable e) {
/* 185 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Constructor<? extends XmlOutput> initFastInfosetOutputClass() {
/*     */     try {
/* 191 */       if (FI_STAX_WRITER_CLASS == null) {
/* 192 */         return null;
/*     */       }
/* 194 */       Class<?> c = UnmarshallerImpl.class.getClassLoader().loadClass("com.sun.xml.bind.v2.runtime.output.FastInfosetStreamWriterOutput");
/* 195 */       return (Constructor)c.getConstructor(new Class[] { FI_STAX_WRITER_CLASS, JAXBContextImpl.class });
/* 196 */     } catch (Throwable e) {
/* 197 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   private static final Class STAXEX_WRITER_CLASS = initStAXExWriterClass();
/* 205 */   private static final Constructor<? extends XmlOutput> STAXEX_OUTPUT_CTOR = initStAXExOutputClass();
/*     */   
/*     */   private static Class initStAXExWriterClass() {
/*     */     try {
/* 209 */       return MarshallerImpl.class.getClassLoader().loadClass("org.jvnet.staxex.XMLStreamWriterEx");
/* 210 */     } catch (Throwable e) {
/* 211 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Constructor<? extends XmlOutput> initStAXExOutputClass() {
/*     */     try {
/* 217 */       Class<?> c = UnmarshallerImpl.class.getClassLoader().loadClass("com.sun.xml.bind.v2.runtime.output.StAXExStreamWriterOutput");
/* 218 */       return (Constructor)c.getConstructor(new Class[] { STAXEX_WRITER_CLASS });
/* 219 */     } catch (Throwable e) {
/* 220 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\XMLStreamWriterOutput.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */