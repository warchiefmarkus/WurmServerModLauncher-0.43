/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.xml.bind.api.TypeReference;
/*     */ import com.sun.xml.bind.marshaller.SAX2DOMEx;
/*     */ import com.sun.xml.bind.v2.runtime.output.SAXOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.XMLStreamWriterOutput;
/*     */ import com.sun.xml.bind.v2.runtime.output.XmlOutput;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ final class BridgeImpl<T>
/*     */   extends InternalBridge<T>
/*     */ {
/*     */   private final Name tagName;
/*     */   private final JaxBeanInfo<T> bi;
/*     */   private final TypeReference typeRef;
/*     */   
/*     */   public BridgeImpl(JAXBContextImpl context, Name tagName, JaxBeanInfo<T> bi, TypeReference typeRef) {
/*  82 */     super(context);
/*  83 */     this.tagName = tagName;
/*  84 */     this.bi = bi;
/*  85 */     this.typeRef = typeRef;
/*     */   }
/*     */   
/*     */   public void marshal(Marshaller _m, T t, XMLStreamWriter output) throws JAXBException {
/*  89 */     MarshallerImpl m = (MarshallerImpl)_m;
/*  90 */     m.write(this.tagName, this.bi, t, XMLStreamWriterOutput.create(output, this.context), new StAXPostInitAction(output, m.serializer));
/*     */   }
/*     */   
/*     */   public void marshal(Marshaller _m, T t, OutputStream output, NamespaceContext nsContext) throws JAXBException {
/*  94 */     MarshallerImpl m = (MarshallerImpl)_m;
/*     */     
/*  96 */     Runnable pia = null;
/*  97 */     if (nsContext != null) {
/*  98 */       pia = new StAXPostInitAction(nsContext, m.serializer);
/*     */     }
/* 100 */     m.write(this.tagName, this.bi, t, m.createWriter(output), pia);
/*     */   }
/*     */   
/*     */   public void marshal(Marshaller _m, T t, Node output) throws JAXBException {
/* 104 */     MarshallerImpl m = (MarshallerImpl)_m;
/* 105 */     m.write(this.tagName, this.bi, t, (XmlOutput)new SAXOutput((ContentHandler)new SAX2DOMEx(output)), new DomPostInitAction(output, m.serializer));
/*     */   }
/*     */   
/*     */   public void marshal(Marshaller _m, T t, ContentHandler contentHandler) throws JAXBException {
/* 109 */     MarshallerImpl m = (MarshallerImpl)_m;
/* 110 */     m.write(this.tagName, this.bi, t, (XmlOutput)new SAXOutput(contentHandler), null);
/*     */   }
/*     */   
/*     */   public void marshal(Marshaller _m, T t, Result result) throws JAXBException {
/* 114 */     MarshallerImpl m = (MarshallerImpl)_m;
/* 115 */     m.write(this.tagName, this.bi, t, m.createXmlOutput(result), m.createPostInitAction(result));
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, XMLStreamReader in) throws JAXBException {
/* 119 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 120 */     return (T)((JAXBElement)u.unmarshal0(in, this.bi)).getValue();
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, Source in) throws JAXBException {
/* 124 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 125 */     return (T)((JAXBElement)u.unmarshal0(in, this.bi)).getValue();
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, InputStream in) throws JAXBException {
/* 129 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 130 */     return (T)((JAXBElement)u.unmarshal0(in, this.bi)).getValue();
/*     */   }
/*     */   @NotNull
/*     */   public T unmarshal(Unmarshaller _u, Node n) throws JAXBException {
/* 134 */     UnmarshallerImpl u = (UnmarshallerImpl)_u;
/* 135 */     return (T)((JAXBElement)u.unmarshal0(n, this.bi)).getValue();
/*     */   }
/*     */   
/*     */   public TypeReference getTypeReference() {
/* 139 */     return this.typeRef;
/*     */   }
/*     */   
/*     */   public void marshal(T value, XMLSerializer out) throws IOException, SAXException, XMLStreamException {
/* 143 */     out.startElement(this.tagName, null);
/* 144 */     if (value == null) {
/* 145 */       out.writeXsiNilTrue();
/*     */     } else {
/* 147 */       out.childAsXsiType(value, null, this.bi);
/*     */     } 
/* 149 */     out.endElement();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\BridgeImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */