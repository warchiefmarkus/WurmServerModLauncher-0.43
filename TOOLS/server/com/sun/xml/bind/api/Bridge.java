/*     */ package com.sun.xml.bind.api;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.xml.bind.v2.runtime.BridgeContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*     */ import javax.xml.bind.attachment.AttachmentUnmarshaller;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ public abstract class Bridge<T>
/*     */ {
/*     */   protected final JAXBContextImpl context;
/*     */   
/*     */   protected Bridge(JAXBContextImpl context) {
/*  80 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public JAXBRIContext getContext() {
/*  91 */     return (JAXBRIContext)this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void marshal(T object, XMLStreamWriter output) throws JAXBException {
/* 102 */     marshal(object, output, (AttachmentMarshaller)null);
/*     */   }
/*     */   public final void marshal(T object, XMLStreamWriter output, AttachmentMarshaller am) throws JAXBException {
/* 105 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 106 */     m.setAttachmentMarshaller(am);
/* 107 */     marshal(m, object, output);
/* 108 */     m.setAttachmentMarshaller(null);
/* 109 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */   
/*     */   public final void marshal(@NotNull BridgeContext context, T object, XMLStreamWriter output) throws JAXBException {
/* 113 */     marshal((Marshaller)((BridgeContextImpl)context).marshaller, object, output);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, XMLStreamWriter paramXMLStreamWriter) throws JAXBException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void marshal(T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
/* 133 */     marshal(object, output, nsContext, (AttachmentMarshaller)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void marshal(T object, OutputStream output, NamespaceContext nsContext, AttachmentMarshaller am) throws JAXBException {
/* 139 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 140 */     m.setAttachmentMarshaller(am);
/* 141 */     marshal(m, object, output, nsContext);
/* 142 */     m.setAttachmentMarshaller(null);
/* 143 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */   
/*     */   public final void marshal(@NotNull BridgeContext context, T object, OutputStream output, NamespaceContext nsContext) throws JAXBException {
/* 147 */     marshal((Marshaller)((BridgeContextImpl)context).marshaller, object, output, nsContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, OutputStream paramOutputStream, NamespaceContext paramNamespaceContext) throws JAXBException;
/*     */   
/*     */   public final void marshal(T object, Node output) throws JAXBException {
/* 154 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 155 */     marshal(m, object, output);
/* 156 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */   
/*     */   public final void marshal(@NotNull BridgeContext context, T object, Node output) throws JAXBException {
/* 160 */     marshal((Marshaller)((BridgeContextImpl)context).marshaller, object, output);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, Node paramNode) throws JAXBException;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void marshal(T object, ContentHandler contentHandler) throws JAXBException {
/* 170 */     marshal(object, contentHandler, (AttachmentMarshaller)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void marshal(T object, ContentHandler contentHandler, AttachmentMarshaller am) throws JAXBException {
/* 176 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 177 */     m.setAttachmentMarshaller(am);
/* 178 */     marshal(m, object, contentHandler);
/* 179 */     m.setAttachmentMarshaller(null);
/* 180 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */   public final void marshal(@NotNull BridgeContext context, T object, ContentHandler contentHandler) throws JAXBException {
/* 183 */     marshal((Marshaller)((BridgeContextImpl)context).marshaller, object, contentHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, ContentHandler paramContentHandler) throws JAXBException;
/*     */ 
/*     */   
/*     */   public final void marshal(T object, Result result) throws JAXBException {
/* 191 */     Marshaller m = (Marshaller)this.context.marshallerPool.take();
/* 192 */     marshal(m, object, result);
/* 193 */     this.context.marshallerPool.recycle(m);
/*     */   }
/*     */   public final void marshal(@NotNull BridgeContext context, T object, Result result) throws JAXBException {
/* 196 */     marshal((Marshaller)((BridgeContextImpl)context).marshaller, object, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void marshal(@NotNull Marshaller paramMarshaller, T paramT, Result paramResult) throws JAXBException;
/*     */   
/*     */   private T exit(T r, Unmarshaller u) {
/* 203 */     u.setAttachmentUnmarshaller(null);
/* 204 */     this.context.unmarshallerPool.recycle(u);
/* 205 */     return r;
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
/*     */   
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull XMLStreamReader in) throws JAXBException {
/* 225 */     return unmarshal(in, (AttachmentUnmarshaller)null);
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull XMLStreamReader in, @Nullable AttachmentUnmarshaller au) throws JAXBException {
/* 231 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 232 */     u.setAttachmentUnmarshaller(au);
/* 233 */     return exit(unmarshal(u, in), u);
/*     */   } @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull XMLStreamReader in) throws JAXBException {
/* 236 */     return unmarshal((Unmarshaller)((BridgeContextImpl)context).unmarshaller, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull XMLStreamReader paramXMLStreamReader) throws JAXBException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Source in) throws JAXBException {
/* 257 */     return unmarshal(in, (AttachmentUnmarshaller)null);
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Source in, @Nullable AttachmentUnmarshaller au) throws JAXBException {
/* 263 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 264 */     u.setAttachmentUnmarshaller(au);
/* 265 */     return exit(unmarshal(u, in), u);
/*     */   } @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull Source in) throws JAXBException {
/* 268 */     return unmarshal((Unmarshaller)((BridgeContextImpl)context).unmarshaller, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull Source paramSource) throws JAXBException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull InputStream in) throws JAXBException {
/* 289 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 290 */     return exit(unmarshal(u, in), u);
/*     */   } @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull InputStream in) throws JAXBException {
/* 293 */     return unmarshal((Unmarshaller)((BridgeContextImpl)context).unmarshaller, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull InputStream paramInputStream) throws JAXBException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Node n) throws JAXBException {
/* 312 */     return unmarshal(n, (AttachmentUnmarshaller)null);
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public final T unmarshal(@NotNull Node n, @Nullable AttachmentUnmarshaller au) throws JAXBException {
/* 318 */     Unmarshaller u = (Unmarshaller)this.context.unmarshallerPool.take();
/* 319 */     u.setAttachmentUnmarshaller(au);
/* 320 */     return exit(unmarshal(u, n), u);
/*     */   } @NotNull
/*     */   public final T unmarshal(@NotNull BridgeContext context, @NotNull Node n) throws JAXBException {
/* 323 */     return unmarshal((Unmarshaller)((BridgeContextImpl)context).unmarshaller, n);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public abstract T unmarshal(@NotNull Unmarshaller paramUnmarshaller, @NotNull Node paramNode) throws JAXBException;
/*     */   
/*     */   public abstract TypeReference getTypeReference();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\Bridge.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */