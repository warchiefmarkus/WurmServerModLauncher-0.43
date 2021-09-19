/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.beans.Introspector;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JAXB
/*     */ {
/*     */   private static volatile WeakReference<Cache> cache;
/*     */   
/*     */   private static final class Cache
/*     */   {
/*     */     final Class type;
/*     */     final JAXBContext context;
/*     */     
/*     */     public Cache(Class type) throws JAXBException {
/*  86 */       this.type = type;
/*  87 */       this.context = JAXBContext.newInstance(new Class[] { type });
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
/*     */   private static <T> JAXBContext getContext(Class<T> type) throws JAXBException {
/* 106 */     WeakReference<Cache> c = cache;
/* 107 */     if (c != null) {
/* 108 */       Cache cache = c.get();
/* 109 */       if (cache != null && cache.type == type) {
/* 110 */         return cache.context;
/*     */       }
/*     */     } 
/*     */     
/* 114 */     Cache d = new Cache(type);
/* 115 */     cache = new WeakReference<Cache>(d);
/*     */     
/* 117 */     return d.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T unmarshal(File xml, Class<T> type) {
/*     */     try {
/* 128 */       JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(new StreamSource(xml), type);
/* 129 */       return item.getValue();
/* 130 */     } catch (JAXBException e) {
/* 131 */       throw new DataBindingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T unmarshal(URL xml, Class<T> type) {
/*     */     try {
/* 143 */       JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
/* 144 */       return item.getValue();
/* 145 */     } catch (JAXBException e) {
/* 146 */       throw new DataBindingException(e);
/* 147 */     } catch (IOException e) {
/* 148 */       throw new DataBindingException(e);
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
/*     */   public static <T> T unmarshal(URI xml, Class<T> type) {
/*     */     try {
/* 161 */       JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
/* 162 */       return item.getValue();
/* 163 */     } catch (JAXBException e) {
/* 164 */       throw new DataBindingException(e);
/* 165 */     } catch (IOException e) {
/* 166 */       throw new DataBindingException(e);
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
/*     */   public static <T> T unmarshal(String xml, Class<T> type) {
/*     */     try {
/* 180 */       JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
/* 181 */       return item.getValue();
/* 182 */     } catch (JAXBException e) {
/* 183 */       throw new DataBindingException(e);
/* 184 */     } catch (IOException e) {
/* 185 */       throw new DataBindingException(e);
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
/*     */   public static <T> T unmarshal(InputStream xml, Class<T> type) {
/*     */     try {
/* 198 */       JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
/* 199 */       return item.getValue();
/* 200 */     } catch (JAXBException e) {
/* 201 */       throw new DataBindingException(e);
/* 202 */     } catch (IOException e) {
/* 203 */       throw new DataBindingException(e);
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
/*     */   public static <T> T unmarshal(Reader xml, Class<T> type) {
/*     */     try {
/* 217 */       JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
/* 218 */       return item.getValue();
/* 219 */     } catch (JAXBException e) {
/* 220 */       throw new DataBindingException(e);
/* 221 */     } catch (IOException e) {
/* 222 */       throw new DataBindingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T unmarshal(Source xml, Class<T> type) {
/*     */     try {
/* 234 */       JAXBElement<T> item = getContext(type).createUnmarshaller().unmarshal(toSource(xml), type);
/* 235 */       return item.getValue();
/* 236 */     } catch (JAXBException e) {
/* 237 */       throw new DataBindingException(e);
/* 238 */     } catch (IOException e) {
/* 239 */       throw new DataBindingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Source toSource(Object xml) throws IOException {
/* 250 */     if (xml == null) {
/* 251 */       throw new IllegalArgumentException("no XML is given");
/*     */     }
/* 253 */     if (xml instanceof String) {
/*     */       try {
/* 255 */         xml = new URI((String)xml);
/* 256 */       } catch (URISyntaxException e) {
/* 257 */         xml = new File((String)xml);
/*     */       } 
/*     */     }
/* 260 */     if (xml instanceof File) {
/* 261 */       File file = (File)xml;
/* 262 */       return new StreamSource(file);
/*     */     } 
/* 264 */     if (xml instanceof URI) {
/* 265 */       URI uri = (URI)xml;
/* 266 */       xml = uri.toURL();
/*     */     } 
/* 268 */     if (xml instanceof URL) {
/* 269 */       URL url = (URL)xml;
/* 270 */       return new StreamSource(url.toExternalForm());
/*     */     } 
/* 272 */     if (xml instanceof InputStream) {
/* 273 */       InputStream in = (InputStream)xml;
/* 274 */       return new StreamSource(in);
/*     */     } 
/* 276 */     if (xml instanceof Reader) {
/* 277 */       Reader r = (Reader)xml;
/* 278 */       return new StreamSource(r);
/*     */     } 
/* 280 */     if (xml instanceof Source) {
/* 281 */       return (Source)xml;
/*     */     }
/* 283 */     throw new IllegalArgumentException("I don't understand how to handle " + xml.getClass());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void marshal(Object jaxbObject, File xml) {
/* 307 */     _marshal(jaxbObject, xml);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void marshal(Object jaxbObject, URL xml) {
/* 334 */     _marshal(jaxbObject, xml);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void marshal(Object jaxbObject, URI xml) {
/* 358 */     _marshal(jaxbObject, xml);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void marshal(Object jaxbObject, String xml) {
/* 383 */     _marshal(jaxbObject, xml);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void marshal(Object jaxbObject, OutputStream xml) {
/* 407 */     _marshal(jaxbObject, xml);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void marshal(Object jaxbObject, Writer xml) {
/* 431 */     _marshal(jaxbObject, xml);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void marshal(Object jaxbObject, Result xml) {
/* 454 */     _marshal(jaxbObject, xml);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void _marshal(Object jaxbObject, Object xml) {
/*     */     try {
/*     */       JAXBContext context;
/* 533 */       if (jaxbObject instanceof JAXBElement) {
/* 534 */         context = getContext(((JAXBElement)jaxbObject).getDeclaredType());
/*     */       } else {
/* 536 */         Class<?> clazz = jaxbObject.getClass();
/* 537 */         XmlRootElement r = clazz.<XmlRootElement>getAnnotation(XmlRootElement.class);
/* 538 */         context = getContext(clazz);
/* 539 */         if (r == null)
/*     */         {
/* 541 */           jaxbObject = new JAXBElement(new QName(inferName(clazz)), clazz, jaxbObject);
/*     */         }
/*     */       } 
/*     */       
/* 545 */       Marshaller m = context.createMarshaller();
/* 546 */       m.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
/* 547 */       m.marshal(jaxbObject, toResult(xml));
/* 548 */     } catch (JAXBException e) {
/* 549 */       throw new DataBindingException(e);
/* 550 */     } catch (IOException e) {
/* 551 */       throw new DataBindingException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String inferName(Class clazz) {
/* 556 */     return Introspector.decapitalize(clazz.getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Result toResult(Object xml) throws IOException {
/* 564 */     if (xml == null) {
/* 565 */       throw new IllegalArgumentException("no XML is given");
/*     */     }
/* 567 */     if (xml instanceof String) {
/*     */       try {
/* 569 */         xml = new URI((String)xml);
/* 570 */       } catch (URISyntaxException e) {
/* 571 */         xml = new File((String)xml);
/*     */       } 
/*     */     }
/* 574 */     if (xml instanceof File) {
/* 575 */       File file = (File)xml;
/* 576 */       return new StreamResult(file);
/*     */     } 
/* 578 */     if (xml instanceof URI) {
/* 579 */       URI uri = (URI)xml;
/* 580 */       xml = uri.toURL();
/*     */     } 
/* 582 */     if (xml instanceof URL) {
/* 583 */       URL url = (URL)xml;
/* 584 */       URLConnection con = url.openConnection();
/* 585 */       con.setDoOutput(true);
/* 586 */       con.setDoInput(false);
/* 587 */       con.connect();
/* 588 */       return new StreamResult(con.getOutputStream());
/*     */     } 
/* 590 */     if (xml instanceof OutputStream) {
/* 591 */       OutputStream os = (OutputStream)xml;
/* 592 */       return new StreamResult(os);
/*     */     } 
/* 594 */     if (xml instanceof Writer) {
/* 595 */       Writer w = (Writer)xml;
/* 596 */       return new StreamResult(w);
/*     */     } 
/* 598 */     if (xml instanceof Result) {
/* 599 */       return (Result)xml;
/*     */     }
/* 601 */     throw new IllegalArgumentException("I don't understand how to handle " + xml.getClass());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\JAXB.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */