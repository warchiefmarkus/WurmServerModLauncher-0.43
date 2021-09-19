/*     */ package com.sun.xml.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.istack.ByteArrayDataSource;
/*     */ import com.sun.xml.bind.DatatypeConverterImpl;
/*     */ import com.sun.xml.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.TODO;
/*     */ import com.sun.xml.bind.v2.model.runtime.RuntimeBuiltinLeafInfo;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.bind.v2.runtime.output.Pcdata;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.bind.v2.util.ByteArrayOutputStreamEx;
/*     */ import com.sun.xml.bind.v2.util.DataSourceSource;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.MediaTracker;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.activation.MimeType;
/*     */ import javax.activation.MimeTypeParseException;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeConstants;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.Duration;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.stream.StreamResult;
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
/*     */ public abstract class RuntimeBuiltinLeafInfoImpl<T>
/*     */   extends BuiltinLeafInfoImpl<Type, Class>
/*     */   implements RuntimeBuiltinLeafInfo, Transducer<T>
/*     */ {
/*     */   private RuntimeBuiltinLeafInfoImpl(Class type, QName... typeNames) {
/* 115 */     super(type, typeNames);
/* 116 */     LEAVES.put(type, this);
/*     */   }
/*     */   
/*     */   public final Class getClazz() {
/* 120 */     return (Class)getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Transducer getTransducer() {
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public boolean useNamespace() {
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   public final boolean isDefault() {
/* 133 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void declareNamespace(T o, XMLSerializer w) throws AccessorException {}
/*     */   
/*     */   public QName getTypeName(T instance) {
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class StringImpl<T>
/*     */     extends RuntimeBuiltinLeafInfoImpl<T>
/*     */   {
/*     */     protected StringImpl(Class type, QName... typeNames) {
/* 148 */       super(type, typeNames);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 154 */       w.text(print(o), fieldName);
/*     */     }
/*     */     
/*     */     public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 158 */       w.leafElement(tagName, print(o), fieldName);
/*     */     }
/*     */     
/*     */     public abstract String print(T param1T) throws AccessorException;
/*     */   }
/*     */   
/*     */   private static abstract class PcdataImpl<T>
/*     */     extends RuntimeBuiltinLeafInfoImpl<T> {
/*     */     protected PcdataImpl(Class type, QName... typeNames) {
/* 167 */       super(type, typeNames);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 173 */       w.text(print(o), fieldName);
/*     */     }
/*     */     
/*     */     public final void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 177 */       w.leafElement(tagName, print(o), fieldName);
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract Pcdata print(T param1T) throws AccessorException;
/*     */   }
/*     */ 
/*     */   
/* 185 */   public static final Map<Type, RuntimeBuiltinLeafInfoImpl<?>> LEAVES = new HashMap<Type, RuntimeBuiltinLeafInfoImpl<?>>();
/*     */   
/* 187 */   public static final RuntimeBuiltinLeafInfoImpl<String> STRING = new StringImpl<String>(String.class, new QName[] { createXS("string"), createXS("normalizedString"), createXS("anyURI"), createXS("token"), createXS("language"), createXS("Name"), createXS("NCName"), createXS("NMTOKEN"), createXS("ENTITY") })
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String parse(CharSequence text)
/*     */       {
/* 199 */         return text.toString();
/*     */       }
/*     */       public String print(String s) {
/* 202 */         return s;
/*     */       }
/*     */       
/*     */       public final void writeText(XMLSerializer w, String o, String fieldName) throws IOException, SAXException, XMLStreamException {
/* 206 */         w.text(o, fieldName);
/*     */       }
/*     */       
/*     */       public final void writeLeafElement(XMLSerializer w, Name tagName, String o, String fieldName) throws IOException, SAXException, XMLStreamException {
/* 210 */         w.leafElement(tagName, o, fieldName);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final List<RuntimeBuiltinLeafInfoImpl<?>> builtinBeanInfos;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 224 */     RuntimeBuiltinLeafInfoImpl[] secondary = { new StringImpl<Character>(Character.class, new QName[] { createXS("unsignedShort") })
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public Character parse(CharSequence text)
/*     */           {
/* 245 */             return Character.valueOf((char)DatatypeConverterImpl._parseInt(text));
/*     */           }
/*     */           public String print(Character v) {
/* 248 */             return Integer.toString(v.charValue());
/*     */           }
/*     */         }, new StringImpl<Calendar>(Calendar.class, new QName[] { DatatypeConstants.DATETIME })
/*     */         {
/*     */           public Calendar parse(CharSequence text) {
/* 253 */             return DatatypeConverterImpl._parseDateTime(text.toString());
/*     */           }
/*     */           public String print(Calendar v) {
/* 256 */             return DatatypeConverterImpl._printDateTime(v);
/*     */           }
/*     */         }, new StringImpl<GregorianCalendar>(GregorianCalendar.class, new QName[] { DatatypeConstants.DATETIME })
/*     */         {
/*     */           public GregorianCalendar parse(CharSequence text) {
/* 261 */             return DatatypeConverterImpl._parseDateTime(text.toString());
/*     */           }
/*     */           public String print(GregorianCalendar v) {
/* 264 */             return DatatypeConverterImpl._printDateTime(v);
/*     */           }
/*     */         }, new StringImpl<Date>(Date.class, new QName[] { DatatypeConstants.DATETIME })
/*     */         {
/*     */           public Date parse(CharSequence text) {
/* 269 */             return DatatypeConverterImpl._parseDateTime(text.toString()).getTime();
/*     */           }
/*     */           public String print(Date v) {
/* 272 */             GregorianCalendar cal = new GregorianCalendar(0, 0, 0);
/* 273 */             cal.setTime(v);
/* 274 */             return DatatypeConverterImpl._printDateTime(cal);
/*     */           }
/*     */         }, new StringImpl<File>(File.class, new QName[] { createXS("string") })
/*     */         {
/*     */           public File parse(CharSequence text) {
/* 279 */             return new File(WhiteSpaceProcessor.trim(text).toString());
/*     */           }
/*     */           public String print(File v) {
/* 282 */             return v.getPath();
/*     */           }
/*     */         }, new StringImpl<URL>(URL.class, new QName[] { createXS("anyURI") })
/*     */         {
/*     */           public URL parse(CharSequence text) throws SAXException {
/* 287 */             TODO.checkSpec("JSR222 Issue #42");
/*     */             try {
/* 289 */               return new URL(WhiteSpaceProcessor.trim(text).toString());
/* 290 */             } catch (MalformedURLException e) {
/* 291 */               UnmarshallingContext.getInstance().handleError(e);
/* 292 */               return null;
/*     */             } 
/*     */           }
/*     */           public String print(URL v) {
/* 296 */             return v.toExternalForm();
/*     */           }
/*     */         }, new StringImpl<URI>(URI.class, new QName[] { createXS("string") })
/*     */         {
/*     */           public URI parse(CharSequence text) throws SAXException {
/*     */             try {
/* 302 */               return new URI(text.toString());
/* 303 */             } catch (URISyntaxException e) {
/* 304 */               UnmarshallingContext.getInstance().handleError(e);
/* 305 */               return null;
/*     */             } 
/*     */           }
/*     */           
/*     */           public String print(URI v) {
/* 310 */             return v.toString();
/*     */           }
/*     */         }, new StringImpl<Class>(Class.class, new QName[] { createXS("string") })
/*     */         {
/*     */           public Class parse(CharSequence text) throws SAXException {
/* 315 */             TODO.checkSpec("JSR222 Issue #42");
/*     */             try {
/* 317 */               String name = WhiteSpaceProcessor.trim(text).toString();
/* 318 */               ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 319 */               if (cl != null) {
/* 320 */                 return cl.loadClass(name);
/*     */               }
/* 322 */               return Class.forName(name);
/* 323 */             } catch (ClassNotFoundException e) {
/* 324 */               UnmarshallingContext.getInstance().handleError(e);
/* 325 */               return null;
/*     */             } 
/*     */           }
/*     */           public String print(Class v) {
/* 329 */             return v.getName();
/*     */           }
/*     */         }, new PcdataImpl<Image>(Image.class, new QName[] { createXS("base64Binary") })
/*     */         {
/*     */           public Image parse(CharSequence text) throws SAXException
/*     */           {
/*     */             try {
/*     */               InputStream is;
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 341 */               if (text instanceof Base64Data) {
/* 342 */                 is = ((Base64Data)text).getInputStream();
/*     */               } else {
/* 344 */                 is = new ByteArrayInputStream(RuntimeBuiltinLeafInfoImpl.decodeBase64(text));
/*     */               } 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               try {
/* 351 */                 return ImageIO.read(is);
/*     */               } finally {
/* 353 */                 is.close();
/*     */               } 
/* 355 */             } catch (IOException e) {
/* 356 */               UnmarshallingContext.getInstance().handleError(e);
/* 357 */               return null;
/*     */             } 
/*     */           }
/*     */           
/*     */           private BufferedImage convertToBufferedImage(Image image) throws IOException {
/* 362 */             if (image instanceof BufferedImage) {
/* 363 */               return (BufferedImage)image;
/*     */             }
/*     */             
/* 366 */             MediaTracker tracker = new MediaTracker(new Component() {  });
/* 367 */             tracker.addImage(image, 0);
/*     */             try {
/* 369 */               tracker.waitForAll();
/* 370 */             } catch (InterruptedException e) {
/* 371 */               throw new IOException(e.getMessage());
/*     */             } 
/* 373 */             BufferedImage bufImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 378 */             Graphics g = bufImage.createGraphics();
/* 379 */             g.drawImage(image, 0, 0, null);
/* 380 */             return bufImage;
/*     */           }
/*     */ 
/*     */           
/*     */           public Base64Data print(Image v) {
/* 385 */             ByteArrayOutputStreamEx imageData = new ByteArrayOutputStreamEx();
/* 386 */             XMLSerializer xs = XMLSerializer.getInstance();
/*     */             
/* 388 */             String mimeType = xs.getXMIMEContentType();
/* 389 */             if (mimeType == null || mimeType.startsWith("image/*"))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 395 */               mimeType = "image/png";
/*     */             }
/*     */             try {
/* 398 */               Iterator<ImageWriter> itr = ImageIO.getImageWritersByMIMEType(mimeType);
/* 399 */               if (itr.hasNext()) {
/* 400 */                 ImageWriter w = itr.next();
/* 401 */                 ImageOutputStream os = ImageIO.createImageOutputStream(imageData);
/* 402 */                 w.setOutput(os);
/* 403 */                 w.write(convertToBufferedImage(v));
/* 404 */                 os.close();
/* 405 */                 w.dispose();
/*     */               } else {
/*     */                 
/* 408 */                 xs.handleEvent((ValidationEvent)new ValidationEventImpl(1, Messages.NO_IMAGE_WRITER.format(new Object[] { mimeType }, ), xs.getCurrentLocation(null)));
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 413 */                 throw new RuntimeException("no encoder for MIME type " + mimeType);
/*     */               } 
/* 415 */             } catch (IOException e) {
/* 416 */               xs.handleError(e);
/*     */               
/* 418 */               throw new RuntimeException(e);
/*     */             } 
/* 420 */             Base64Data bd = new Base64Data();
/* 421 */             imageData.set(bd, mimeType);
/* 422 */             return bd;
/*     */           }
/*     */         }, new PcdataImpl<DataHandler>(DataHandler.class, new QName[] { createXS("base64Binary") })
/*     */         {
/*     */           public DataHandler parse(CharSequence text) {
/* 427 */             if (text instanceof Base64Data) {
/* 428 */               return ((Base64Data)text).getDataHandler();
/*     */             }
/* 430 */             return new DataHandler((DataSource)new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
/*     */           }
/*     */ 
/*     */           
/*     */           public Base64Data print(DataHandler v) {
/* 435 */             Base64Data bd = new Base64Data();
/* 436 */             bd.set(v);
/* 437 */             return bd;
/*     */           }
/*     */         }, 
/*     */         new PcdataImpl<Source>(Source.class, new QName[] { createXS("base64Binary") }) {
/*     */           public Source parse(CharSequence text) throws SAXException {
/*     */             try {
/* 443 */               if (text instanceof Base64Data) {
/* 444 */                 return (Source)new DataSourceSource(((Base64Data)text).getDataHandler());
/*     */               }
/* 446 */               return (Source)new DataSourceSource((DataSource)new ByteArrayDataSource(RuntimeBuiltinLeafInfoImpl.decodeBase64(text), UnmarshallingContext.getInstance().getXMIMEContentType()));
/*     */             }
/* 448 */             catch (MimeTypeParseException e) {
/* 449 */               UnmarshallingContext.getInstance().handleError((Exception)e);
/* 450 */               return null;
/*     */             } 
/*     */           }
/*     */           
/*     */           public Base64Data print(Source v) {
/* 455 */             XMLSerializer xs = XMLSerializer.getInstance();
/* 456 */             Base64Data bd = new Base64Data();
/*     */             
/* 458 */             String contentType = xs.getXMIMEContentType();
/* 459 */             MimeType mt = null;
/* 460 */             if (contentType != null) {
/*     */               try {
/* 462 */                 mt = new MimeType(contentType);
/* 463 */               } catch (MimeTypeParseException e) {
/* 464 */                 xs.handleError((Exception)e);
/*     */               } 
/*     */             }
/*     */             
/* 468 */             if (v instanceof DataSourceSource) {
/*     */ 
/*     */               
/* 471 */               DataSource ds = ((DataSourceSource)v).getDataSource();
/*     */               
/* 473 */               String dsct = ds.getContentType();
/* 474 */               if (dsct != null && (contentType == null || contentType.equals(dsct))) {
/* 475 */                 bd.set(new DataHandler(ds));
/* 476 */                 return bd;
/*     */               } 
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 483 */             String charset = null;
/* 484 */             if (mt != null)
/* 485 */               charset = mt.getParameter("charset"); 
/* 486 */             if (charset == null) {
/* 487 */               charset = "UTF-8";
/*     */             }
/*     */             try {
/* 490 */               ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx();
/* 491 */               xs.getIdentityTransformer().transform(v, new StreamResult(new OutputStreamWriter((OutputStream)baos, charset)));
/*     */               
/* 493 */               baos.set(bd, "application/xml; charset=" + charset);
/* 494 */               return bd;
/* 495 */             } catch (TransformerException e) {
/*     */               
/* 497 */               xs.handleError(e);
/* 498 */             } catch (UnsupportedEncodingException e) {
/* 499 */               xs.handleError(e);
/*     */             } 
/*     */ 
/*     */             
/* 503 */             bd.set(new byte[0], "application/xml");
/* 504 */             return bd;
/*     */           }
/*     */         }, new StringImpl<XMLGregorianCalendar>(XMLGregorianCalendar.class, new QName[] { createXS("anySimpleType"), DatatypeConstants.DATE, DatatypeConstants.DATETIME, DatatypeConstants.TIME, DatatypeConstants.GMONTH, DatatypeConstants.GDAY, DatatypeConstants.GYEAR, DatatypeConstants.GYEARMONTH, DatatypeConstants.GMONTHDAY })
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public String print(XMLGregorianCalendar cal)
/*     */           {
/* 519 */             XMLSerializer xs = XMLSerializer.getInstance();
/*     */             
/* 521 */             QName type = xs.getSchemaType();
/* 522 */             if (type != null) {
/*     */               try {
/* 524 */                 RuntimeBuiltinLeafInfoImpl.checkXmlGregorianCalendarFieldRef(type, cal);
/* 525 */                 String format = (String)RuntimeBuiltinLeafInfoImpl.xmlGregorianCalendarFormatString.get(type);
/* 526 */                 if (format != null) {
/* 527 */                   return format(format, cal);
/*     */                 
/*     */                 }
/*     */               
/*     */               }
/* 532 */               catch (MarshalException e) {
/*     */                 
/* 534 */                 System.out.println(e.toString());
/* 535 */                 return "";
/*     */               } 
/*     */             }
/* 538 */             return cal.toXMLFormat();
/*     */           }
/*     */           
/*     */           public XMLGregorianCalendar parse(CharSequence lexical) throws SAXException {
/*     */             try {
/* 543 */               return RuntimeBuiltinLeafInfoImpl.datatypeFactory.newXMLGregorianCalendar(lexical.toString());
/* 544 */             } catch (Exception e) {
/* 545 */               UnmarshallingContext.getInstance().handleError(e);
/* 546 */               return null;
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           private String format(String format, XMLGregorianCalendar value) {
/* 552 */             StringBuilder buf = new StringBuilder();
/* 553 */             int fidx = 0, flen = format.length();
/*     */             
/* 555 */             while (fidx < flen) {
/* 556 */               int offset; char fch = format.charAt(fidx++);
/* 557 */               if (fch != '%') {
/* 558 */                 buf.append(fch);
/*     */                 
/*     */                 continue;
/*     */               } 
/* 562 */               switch (format.charAt(fidx++)) {
/*     */                 case 'Y':
/* 564 */                   printNumber(buf, value.getEonAndYear(), 4);
/*     */                   continue;
/*     */                 case 'M':
/* 567 */                   printNumber(buf, value.getMonth(), 2);
/*     */                   continue;
/*     */                 case 'D':
/* 570 */                   printNumber(buf, value.getDay(), 2);
/*     */                   continue;
/*     */                 case 'h':
/* 573 */                   printNumber(buf, value.getHour(), 2);
/*     */                   continue;
/*     */                 case 'm':
/* 576 */                   printNumber(buf, value.getMinute(), 2);
/*     */                   continue;
/*     */                 case 's':
/* 579 */                   printNumber(buf, value.getSecond(), 2);
/* 580 */                   if (value.getFractionalSecond() != null) {
/* 581 */                     String frac = value.getFractionalSecond().toString();
/*     */                     
/* 583 */                     buf.append(frac.substring(1, frac.length()));
/*     */                   } 
/*     */                   continue;
/*     */                 case 'z':
/* 587 */                   offset = value.getTimezone();
/* 588 */                   if (offset == 0) {
/* 589 */                     buf.append('Z'); continue;
/* 590 */                   }  if (offset != Integer.MIN_VALUE) {
/* 591 */                     if (offset < 0) {
/* 592 */                       buf.append('-');
/* 593 */                       offset *= -1;
/*     */                     } else {
/* 595 */                       buf.append('+');
/*     */                     } 
/* 597 */                     printNumber(buf, offset / 60, 2);
/* 598 */                     buf.append(':');
/* 599 */                     printNumber(buf, offset % 60, 2);
/*     */                   } 
/*     */                   continue;
/*     */               } 
/* 603 */               throw new InternalError();
/*     */             } 
/*     */ 
/*     */             
/* 607 */             return buf.toString();
/*     */           }
/*     */           private void printNumber(StringBuilder out, BigInteger number, int nDigits) {
/* 610 */             String s = number.toString();
/* 611 */             for (int i = s.length(); i < nDigits; i++)
/* 612 */               out.append('0'); 
/* 613 */             out.append(s);
/*     */           }
/*     */           private void printNumber(StringBuilder out, int number, int nDigits) {
/* 616 */             String s = String.valueOf(number);
/* 617 */             for (int i = s.length(); i < nDigits; i++)
/* 618 */               out.append('0'); 
/* 619 */             out.append(s);
/*     */           }
/*     */           
/*     */           public QName getTypeName(XMLGregorianCalendar cal) {
/* 623 */             return cal.getXMLSchemaType();
/*     */           }
/*     */         } };
/*     */ 
/*     */     
/* 628 */     RuntimeBuiltinLeafInfoImpl[] primary = { STRING, new StringImpl<Boolean>(Boolean.class, new QName[] { createXS("boolean") })
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public Boolean parse(CharSequence text)
/*     */           {
/* 637 */             return Boolean.valueOf(DatatypeConverterImpl._parseBoolean(text));
/*     */           }
/*     */           
/*     */           public String print(Boolean v) {
/* 641 */             return v.toString();
/*     */           }
/*     */         }, new PcdataImpl<byte[]>(byte[].class, new QName[] { createXS("base64Binary"), createXS("hexBinary") })
/*     */         {
/*     */ 
/*     */           
/*     */           public byte[] parse(CharSequence text)
/*     */           {
/* 649 */             return RuntimeBuiltinLeafInfoImpl.decodeBase64(text);
/*     */           }
/*     */           
/*     */           public Base64Data print(byte[] v) {
/* 653 */             XMLSerializer w = XMLSerializer.getInstance();
/* 654 */             Base64Data bd = new Base64Data();
/* 655 */             String mimeType = w.getXMIMEContentType();
/* 656 */             bd.set(v, mimeType);
/* 657 */             return bd;
/*     */           }
/*     */         }, new StringImpl<Byte>(Byte.class, new QName[] { createXS("byte") })
/*     */         {
/*     */           
/*     */           public Byte parse(CharSequence text)
/*     */           {
/* 664 */             return Byte.valueOf(DatatypeConverterImpl._parseByte(text));
/*     */           }
/*     */           
/*     */           public String print(Byte v) {
/* 668 */             return DatatypeConverterImpl._printByte(v.byteValue());
/*     */           }
/*     */         }, new StringImpl<Short>(Short.class, new QName[] { createXS("short"), createXS("unsignedByte") })
/*     */         {
/*     */ 
/*     */           
/*     */           public Short parse(CharSequence text)
/*     */           {
/* 676 */             return Short.valueOf(DatatypeConverterImpl._parseShort(text));
/*     */           }
/*     */           
/*     */           public String print(Short v) {
/* 680 */             return DatatypeConverterImpl._printShort(v.shortValue());
/*     */           }
/*     */         }, new StringImpl<Integer>(Integer.class, new QName[] { createXS("int"), createXS("unsignedShort") })
/*     */         {
/*     */ 
/*     */           
/*     */           public Integer parse(CharSequence text)
/*     */           {
/* 688 */             return Integer.valueOf(DatatypeConverterImpl._parseInt(text));
/*     */           }
/*     */           
/*     */           public String print(Integer v) {
/* 692 */             return DatatypeConverterImpl._printInt(v.intValue());
/*     */           }
/*     */         }, new StringImpl<Long>(Long.class, new QName[] { createXS("long"), createXS("unsignedInt") })
/*     */         {
/*     */ 
/*     */           
/*     */           public Long parse(CharSequence text)
/*     */           {
/* 700 */             return Long.valueOf(DatatypeConverterImpl._parseLong(text));
/*     */           }
/*     */           
/*     */           public String print(Long v) {
/* 704 */             return DatatypeConverterImpl._printLong(v.longValue());
/*     */           }
/*     */         }, new StringImpl<Float>(Float.class, new QName[] { createXS("float") })
/*     */         {
/*     */           
/*     */           public Float parse(CharSequence text)
/*     */           {
/* 711 */             return Float.valueOf(DatatypeConverterImpl._parseFloat(text.toString()));
/*     */           }
/*     */           
/*     */           public String print(Float v) {
/* 715 */             return DatatypeConverterImpl._printFloat(v.floatValue());
/*     */           }
/*     */         }, new StringImpl<Double>(Double.class, new QName[] { createXS("double") })
/*     */         {
/*     */           
/*     */           public Double parse(CharSequence text)
/*     */           {
/* 722 */             return Double.valueOf(DatatypeConverterImpl._parseDouble(text));
/*     */           }
/*     */           
/*     */           public String print(Double v) {
/* 726 */             return DatatypeConverterImpl._printDouble(v.doubleValue());
/*     */           }
/*     */         }, new StringImpl<BigInteger>(BigInteger.class, new QName[] { createXS("integer"), createXS("positiveInteger"), createXS("negativeInteger"), createXS("nonPositiveInteger"), createXS("nonNegativeInteger"), createXS("unsignedLong") })
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public BigInteger parse(CharSequence text)
/*     */           {
/* 738 */             return DatatypeConverterImpl._parseInteger(text);
/*     */           }
/*     */           
/*     */           public String print(BigInteger v) {
/* 742 */             return DatatypeConverterImpl._printInteger(v);
/*     */           }
/*     */         }, 
/*     */         new StringImpl<BigDecimal>(BigDecimal.class, new QName[] { createXS("decimal") })
/*     */         {
/*     */           public BigDecimal parse(CharSequence text)
/*     */           {
/* 749 */             return DatatypeConverterImpl._parseDecimal(text.toString());
/*     */           }
/*     */           
/*     */           public String print(BigDecimal v) {
/* 753 */             return DatatypeConverterImpl._printDecimal(v);
/*     */           }
/*     */         }, new StringImpl<QName>(QName.class, new QName[] { createXS("QName") })
/*     */         {
/*     */           
/*     */           public QName parse(CharSequence text) throws SAXException
/*     */           {
/*     */             try {
/* 761 */               return DatatypeConverterImpl._parseQName(text.toString(), (NamespaceContext)UnmarshallingContext.getInstance());
/* 762 */             } catch (IllegalArgumentException e) {
/* 763 */               UnmarshallingContext.getInstance().handleError(e);
/* 764 */               return null;
/*     */             } 
/*     */           }
/*     */           
/*     */           public String print(QName v) {
/* 769 */             return DatatypeConverterImpl._printQName(v, (NamespaceContext)XMLSerializer.getInstance().getNamespaceContext());
/*     */           }
/*     */           
/*     */           public boolean useNamespace() {
/* 773 */             return true;
/*     */           }
/*     */           
/*     */           public void declareNamespace(QName v, XMLSerializer w) {
/* 777 */             w.getNamespaceContext().declareNamespace(v.getNamespaceURI(), v.getPrefix(), false);
/*     */           }
/*     */         }, new StringImpl<Duration>(Duration.class, new QName[] { createXS("duration") })
/*     */         {
/*     */           public String print(Duration duration) {
/* 782 */             return duration.toString();
/*     */           }
/*     */           
/*     */           public Duration parse(CharSequence lexical) {
/* 786 */             TODO.checkSpec("JSR222 Issue #42");
/* 787 */             return RuntimeBuiltinLeafInfoImpl.datatypeFactory.newDuration(lexical.toString());
/*     */           }
/*     */         }, new StringImpl<Void>(Void.class, new QName[0])
/*     */         {
/*     */ 
/*     */           
/*     */           public String print(Void value)
/*     */           {
/* 795 */             return "";
/*     */           }
/*     */           
/*     */           public Void parse(CharSequence lexical) {
/* 799 */             return null;
/*     */           }
/*     */         } };
/*     */ 
/*     */     
/* 804 */     List<RuntimeBuiltinLeafInfoImpl<?>> l = new ArrayList<RuntimeBuiltinLeafInfoImpl<?>>(secondary.length + primary.length + 1);
/* 805 */     for (RuntimeBuiltinLeafInfoImpl<?> item : secondary) {
/* 806 */       l.add(item);
/*     */     }
/*     */     
/*     */     try {
/* 810 */       l.add(new UUIDImpl());
/* 811 */     } catch (LinkageError e) {}
/*     */ 
/*     */ 
/*     */     
/* 815 */     for (RuntimeBuiltinLeafInfoImpl<?> item : primary) {
/* 816 */       l.add(item);
/*     */     }
/* 818 */     builtinBeanInfos = Collections.unmodifiableList(l);
/*     */   }
/*     */   
/*     */   private static byte[] decodeBase64(CharSequence text) {
/* 822 */     if (text instanceof Base64Data) {
/* 823 */       Base64Data base64Data = (Base64Data)text;
/* 824 */       return base64Data.getExact();
/*     */     } 
/* 826 */     return DatatypeConverterImpl._parseBase64Binary(text.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static QName createXS(String typeName) {
/* 832 */     return new QName("http://www.w3.org/2001/XMLSchema", typeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 839 */   private static final DatatypeFactory datatypeFactory = init();
/*     */   
/*     */   private static DatatypeFactory init() {
/*     */     try {
/* 843 */       return DatatypeFactory.newInstance();
/* 844 */     } catch (DatatypeConfigurationException e) {
/* 845 */       throw new Error(Messages.FAILED_TO_INITIALE_DATATYPE_FACTORY.format(new Object[0]), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkXmlGregorianCalendarFieldRef(QName type, XMLGregorianCalendar cal) throws MarshalException {
/* 851 */     StringBuffer buf = new StringBuffer();
/* 852 */     int bitField = ((Integer)xmlGregorianCalendarFieldRef.get(type)).intValue();
/* 853 */     int l = 1;
/* 854 */     int pos = 0;
/* 855 */     while (bitField != 0) {
/* 856 */       int bit = bitField & 0x1;
/* 857 */       bitField >>>= 4;
/* 858 */       pos++;
/*     */       
/* 860 */       if (bit == 1) {
/* 861 */         switch (pos) {
/*     */           case 1:
/* 863 */             if (cal.getSecond() == Integer.MIN_VALUE) {
/* 864 */               buf.append("  " + Messages.XMLGREGORIANCALENDAR_SEC);
/*     */             }
/*     */           
/*     */           case 2:
/* 868 */             if (cal.getMinute() == Integer.MIN_VALUE) {
/* 869 */               buf.append("  " + Messages.XMLGREGORIANCALENDAR_MIN);
/*     */             }
/*     */           
/*     */           case 3:
/* 873 */             if (cal.getHour() == Integer.MIN_VALUE) {
/* 874 */               buf.append("  " + Messages.XMLGREGORIANCALENDAR_HR);
/*     */             }
/*     */           
/*     */           case 4:
/* 878 */             if (cal.getDay() == Integer.MIN_VALUE) {
/* 879 */               buf.append("  " + Messages.XMLGREGORIANCALENDAR_DAY);
/*     */             }
/*     */           
/*     */           case 5:
/* 883 */             if (cal.getMonth() == Integer.MIN_VALUE) {
/* 884 */               buf.append("  " + Messages.XMLGREGORIANCALENDAR_MONTH);
/*     */             }
/*     */           
/*     */           case 6:
/* 888 */             if (cal.getYear() == Integer.MIN_VALUE) {
/* 889 */               buf.append("  " + Messages.XMLGREGORIANCALENDAR_YEAR);
/*     */             }
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */     } 
/* 897 */     if (buf.length() > 0) {
/* 898 */       throw new MarshalException(Messages.XMLGREGORIANCALENDAR_INVALID.format(new Object[] { type.getLocalPart() }) + buf.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 907 */   private static final Map<QName, String> xmlGregorianCalendarFormatString = new HashMap<QName, String>();
/*     */   
/*     */   static {
/* 910 */     Map<QName, String> m = xmlGregorianCalendarFormatString;
/*     */     
/* 912 */     m.put(DatatypeConstants.DATETIME, "%Y-%M-%DT%h:%m:%s%z");
/* 913 */     m.put(DatatypeConstants.DATE, "%Y-%M-%D%z");
/* 914 */     m.put(DatatypeConstants.TIME, "%h:%m:%s%z");
/* 915 */     m.put(DatatypeConstants.GMONTH, "--%M--%z");
/* 916 */     m.put(DatatypeConstants.GDAY, "---%D%z");
/* 917 */     m.put(DatatypeConstants.GYEAR, "%Y%z");
/* 918 */     m.put(DatatypeConstants.GYEARMONTH, "%Y-%M%z");
/* 919 */     m.put(DatatypeConstants.GMONTHDAY, "--%M-%D%z");
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
/* 932 */   private static final Map<QName, Integer> xmlGregorianCalendarFieldRef = new HashMap<QName, Integer>();
/*     */   
/*     */   static {
/* 935 */     Map<QName, Integer> f = xmlGregorianCalendarFieldRef;
/* 936 */     f.put(DatatypeConstants.DATETIME, Integer.valueOf(17895697));
/* 937 */     f.put(DatatypeConstants.DATE, Integer.valueOf(17895424));
/* 938 */     f.put(DatatypeConstants.TIME, Integer.valueOf(16777489));
/* 939 */     f.put(DatatypeConstants.GDAY, Integer.valueOf(16781312));
/* 940 */     f.put(DatatypeConstants.GMONTH, Integer.valueOf(16842752));
/* 941 */     f.put(DatatypeConstants.GYEAR, Integer.valueOf(17825792));
/* 942 */     f.put(DatatypeConstants.GYEARMONTH, Integer.valueOf(17891328));
/* 943 */     f.put(DatatypeConstants.GMONTHDAY, Integer.valueOf(16846848));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class UUIDImpl
/*     */     extends StringImpl<UUID>
/*     */   {
/*     */     public UUIDImpl() {
/* 953 */       super(UUID.class, new QName[] { RuntimeBuiltinLeafInfoImpl.access$500("string") });
/*     */     }
/*     */     
/*     */     public UUID parse(CharSequence text) throws SAXException {
/* 957 */       TODO.checkSpec("JSR222 Issue #42");
/*     */       try {
/* 959 */         return UUID.fromString(WhiteSpaceProcessor.trim(text).toString());
/* 960 */       } catch (IllegalArgumentException e) {
/* 961 */         UnmarshallingContext.getInstance().handleError(e);
/* 962 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public String print(UUID v) {
/* 967 */       return v.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\RuntimeBuiltinLeafInfoImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */