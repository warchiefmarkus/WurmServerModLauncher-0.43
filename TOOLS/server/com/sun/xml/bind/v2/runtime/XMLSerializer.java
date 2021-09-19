/*      */ package com.sun.xml.bind.v2.runtime;
/*      */ 
/*      */ import com.sun.istack.SAXException2;
/*      */ import com.sun.xml.bind.CycleRecoverable;
/*      */ import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
/*      */ import com.sun.xml.bind.util.ValidationEventLocatorExImpl;
/*      */ import com.sun.xml.bind.v2.runtime.output.MTOMXmlOutput;
/*      */ import com.sun.xml.bind.v2.runtime.output.NamespaceContextImpl;
/*      */ import com.sun.xml.bind.v2.runtime.output.Pcdata;
/*      */ import com.sun.xml.bind.v2.runtime.output.XmlOutput;
/*      */ import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;
/*      */ import com.sun.xml.bind.v2.runtime.unmarshaller.IntData;
/*      */ import com.sun.xml.bind.v2.util.CollisionCheckStack;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.activation.MimeType;
/*      */ import javax.xml.bind.DatatypeConverter;
/*      */ import javax.xml.bind.JAXBException;
/*      */ import javax.xml.bind.Marshaller;
/*      */ import javax.xml.bind.ValidationEvent;
/*      */ import javax.xml.bind.ValidationEventHandler;
/*      */ import javax.xml.bind.ValidationEventLocator;
/*      */ import javax.xml.bind.annotation.DomHandler;
/*      */ import javax.xml.bind.attachment.AttachmentMarshaller;
/*      */ import javax.xml.bind.helpers.NotIdentifiableEventImpl;
/*      */ import javax.xml.bind.helpers.ValidationEventImpl;
/*      */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.stream.XMLStreamException;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.sax.SAXResult;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class XMLSerializer
/*      */   extends Coordinator
/*      */ {
/*      */   public final JAXBContextImpl grammar;
/*      */   private XmlOutput out;
/*      */   public final NameList nameList;
/*      */   public final int[] knownUri2prefixIndexMap;
/*      */   private final NamespaceContextImpl nsContext;
/*      */   private NamespaceContextImpl.Element nse;
/*      */   private boolean textHasAlreadyPrinted = false;
/*      */   private boolean seenRoot = false;
/*      */   private final MarshallerImpl marshaller;
/*  160 */   private final Set<Object> idReferencedObjects = new HashSet();
/*      */ 
/*      */   
/*  163 */   private final Set<Object> objectsWithId = new HashSet();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  169 */   private final CollisionCheckStack<Object> cycleDetectionStack = new CollisionCheckStack();
/*      */ 
/*      */ 
/*      */   
/*      */   private String schemaLocation;
/*      */ 
/*      */   
/*      */   private String noNsSchemaLocation;
/*      */ 
/*      */   
/*      */   private Transformer identityTransformer;
/*      */ 
/*      */   
/*      */   private ContentHandlerAdaptor contentHandlerAdapter;
/*      */ 
/*      */   
/*      */   private boolean fragment;
/*      */ 
/*      */   
/*      */   private Base64Data base64Data;
/*      */ 
/*      */   
/*  191 */   private final IntData intData = new IntData();
/*      */   
/*      */   public AttachmentMarshaller attachmentMarshaller;
/*      */   private MimeType expectedMimeType;
/*      */   
/*      */   XMLSerializer(MarshallerImpl _owner) {
/*  197 */     this.marshaller = _owner;
/*  198 */     this.grammar = this.marshaller.context;
/*  199 */     this.nsContext = new NamespaceContextImpl(this);
/*  200 */     this.nameList = this.marshaller.context.nameList;
/*  201 */     this.knownUri2prefixIndexMap = new int[this.nameList.namespaceURIs.length];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean inlineBinaryFlag;
/*      */   
/*      */   private QName schemaType;
/*      */ 
/*      */   
/*      */   public Base64Data getCachedBase64DataInstance() {
/*  212 */     return new Base64Data();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getIdFromObject(Object identifiableObject) throws SAXException, JAXBException {
/*  219 */     return this.grammar.getBeanInfo(identifiableObject, true).getId(identifiableObject, this);
/*      */   }
/*      */   
/*      */   private void handleMissingObjectError(String fieldName) throws SAXException, IOException, XMLStreamException {
/*  223 */     reportMissingObjectError(fieldName);
/*      */ 
/*      */     
/*  226 */     endNamespaceDecls((Object)null);
/*  227 */     endAttributes();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void reportError(ValidationEvent ve) throws SAXException {
/*      */     ValidationEventHandler handler;
/*      */     try {
/*  235 */       handler = this.marshaller.getEventHandler();
/*  236 */     } catch (JAXBException e) {
/*  237 */       throw new SAXException2(e);
/*      */     } 
/*      */     
/*  240 */     if (!handler.handleEvent(ve)) {
/*  241 */       if (ve.getLinkedException() instanceof Exception) {
/*  242 */         throw new SAXException2((Exception)ve.getLinkedException());
/*      */       }
/*  244 */       throw new SAXException2(ve.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void reportError(String fieldName, Throwable t) throws SAXException {
/*  255 */     ValidationEventImpl validationEventImpl = new ValidationEventImpl(1, t.getMessage(), getCurrentLocation(fieldName), t);
/*      */     
/*  257 */     reportError((ValidationEvent)validationEventImpl);
/*      */   }
/*      */   
/*      */   public void startElement(Name tagName, Object outerPeer) {
/*  261 */     startElement();
/*  262 */     this.nse.setTagName(tagName, outerPeer);
/*      */   }
/*      */   
/*      */   public void startElement(String nsUri, String localName, String preferredPrefix, Object outerPeer) {
/*  266 */     startElement();
/*  267 */     int idx = this.nsContext.declareNsUri(nsUri, preferredPrefix, false);
/*  268 */     this.nse.setTagName(idx, localName, outerPeer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void startElementForce(String nsUri, String localName, String forcedPrefix, Object outerPeer) {
/*  276 */     startElement();
/*  277 */     int idx = this.nsContext.force(nsUri, forcedPrefix);
/*  278 */     this.nse.setTagName(idx, localName, outerPeer);
/*      */   }
/*      */   
/*      */   public void endNamespaceDecls(Object innerPeer) throws IOException, XMLStreamException {
/*  282 */     this.nsContext.collectionMode = false;
/*  283 */     this.nse.startElement(this.out, innerPeer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endAttributes() throws SAXException, IOException, XMLStreamException {
/*  291 */     if (!this.seenRoot) {
/*  292 */       this.seenRoot = true;
/*  293 */       if (this.schemaLocation != null || this.noNsSchemaLocation != null) {
/*  294 */         int p = this.nsContext.getPrefixIndex("http://www.w3.org/2001/XMLSchema-instance");
/*  295 */         if (this.schemaLocation != null)
/*  296 */           this.out.attribute(p, "schemaLocation", this.schemaLocation); 
/*  297 */         if (this.noNsSchemaLocation != null) {
/*  298 */           this.out.attribute(p, "noNamespaceSchemaLocation", this.noNsSchemaLocation);
/*      */         }
/*      */       } 
/*      */     } 
/*  302 */     this.out.endStartTag();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endElement() throws SAXException, IOException, XMLStreamException {
/*  310 */     this.nse.endElement(this.out);
/*  311 */     this.nse = this.nse.pop();
/*  312 */     this.textHasAlreadyPrinted = false;
/*      */   }
/*      */   
/*      */   public void leafElement(Name tagName, String data, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  316 */     if (this.seenRoot) {
/*  317 */       this.textHasAlreadyPrinted = false;
/*  318 */       this.nse = this.nse.push();
/*  319 */       this.out.beginStartTag(tagName);
/*  320 */       this.out.endStartTag();
/*  321 */       this.out.text(data, false);
/*  322 */       this.out.endTag(tagName);
/*  323 */       this.nse = this.nse.pop();
/*      */     }
/*      */     else {
/*      */       
/*  327 */       startElement(tagName, (Object)null);
/*  328 */       endNamespaceDecls((Object)null);
/*  329 */       endAttributes();
/*  330 */       this.out.text(data, false);
/*  331 */       endElement();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void leafElement(Name tagName, Pcdata data, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  336 */     if (this.seenRoot) {
/*  337 */       this.textHasAlreadyPrinted = false;
/*  338 */       this.nse = this.nse.push();
/*  339 */       this.out.beginStartTag(tagName);
/*  340 */       this.out.endStartTag();
/*  341 */       this.out.text(data, false);
/*  342 */       this.out.endTag(tagName);
/*  343 */       this.nse = this.nse.pop();
/*      */     }
/*      */     else {
/*      */       
/*  347 */       startElement(tagName, (Object)null);
/*  348 */       endNamespaceDecls((Object)null);
/*  349 */       endAttributes();
/*  350 */       this.out.text(data, false);
/*  351 */       endElement();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void leafElement(Name tagName, int data, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  356 */     this.intData.reset(data);
/*  357 */     leafElement(tagName, (Pcdata)this.intData, fieldName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void text(String text, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  397 */     if (text == null) {
/*  398 */       reportMissingObjectError(fieldName);
/*      */       
/*      */       return;
/*      */     } 
/*  402 */     this.out.text(text, this.textHasAlreadyPrinted);
/*  403 */     this.textHasAlreadyPrinted = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void text(Pcdata text, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  412 */     if (text == null) {
/*  413 */       reportMissingObjectError(fieldName);
/*      */       
/*      */       return;
/*      */     } 
/*  417 */     this.out.text(text, this.textHasAlreadyPrinted);
/*  418 */     this.textHasAlreadyPrinted = true;
/*      */   }
/*      */   
/*      */   public void attribute(String uri, String local, String value) throws SAXException {
/*      */     int prefix;
/*  423 */     if (uri.length() == 0) {
/*      */       
/*  425 */       prefix = -1;
/*      */     } else {
/*  427 */       prefix = this.nsContext.getPrefixIndex(uri);
/*      */     } 
/*      */     
/*      */     try {
/*  431 */       this.out.attribute(prefix, local, value);
/*  432 */     } catch (IOException e) {
/*  433 */       throw new SAXException2(e);
/*  434 */     } catch (XMLStreamException e) {
/*  435 */       throw new SAXException2(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void attribute(Name name, CharSequence value) throws IOException, XMLStreamException {
/*  442 */     this.out.attribute(name, value.toString());
/*      */   }
/*      */   
/*      */   public NamespaceContext2 getNamespaceContext() {
/*  446 */     return (NamespaceContext2)this.nsContext;
/*      */   }
/*      */ 
/*      */   
/*      */   public String onID(Object owner, String value) {
/*  451 */     this.objectsWithId.add(owner);
/*  452 */     return value;
/*      */   }
/*      */   
/*      */   public String onIDREF(Object obj) throws SAXException {
/*      */     String id;
/*      */     try {
/*  458 */       id = getIdFromObject(obj);
/*  459 */     } catch (JAXBException e) {
/*  460 */       reportError((String)null, (Throwable)e);
/*  461 */       return null;
/*      */     } 
/*  463 */     this.idReferencedObjects.add(obj);
/*  464 */     if (id == null) {
/*  465 */       reportError((ValidationEvent)new NotIdentifiableEventImpl(1, Messages.NOT_IDENTIFIABLE.format(new Object[0]), (ValidationEventLocator)new ValidationEventLocatorImpl(obj)));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  470 */     return id;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void childAsRoot(Object obj) throws JAXBException, IOException, SAXException, XMLStreamException {
/*  478 */     JaxBeanInfo<Object> beanInfo = this.grammar.getBeanInfo(obj, true);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  483 */     this.cycleDetectionStack.pushNocheck(obj);
/*      */     
/*  485 */     boolean lookForLifecycleMethods = beanInfo.lookForLifecycleMethods();
/*  486 */     if (lookForLifecycleMethods) {
/*  487 */       fireBeforeMarshalEvents(beanInfo, obj);
/*      */     }
/*      */     
/*  490 */     beanInfo.serializeRoot(obj, this);
/*      */     
/*  492 */     if (lookForLifecycleMethods) {
/*  493 */       fireAfterMarshalEvents(beanInfo, obj);
/*      */     }
/*      */     
/*  496 */     this.cycleDetectionStack.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object pushObject(Object obj, String fieldName) throws SAXException {
/*  512 */     if (!this.cycleDetectionStack.push(obj)) {
/*  513 */       return obj;
/*      */     }
/*      */     
/*  516 */     if (obj instanceof CycleRecoverable) {
/*  517 */       obj = ((CycleRecoverable)obj).onCycleDetected(new CycleRecoverable.Context() {
/*      */             public Marshaller getMarshaller() {
/*  519 */               return (Marshaller)XMLSerializer.this.marshaller;
/*      */             }
/*      */           });
/*  522 */       if (obj != null) {
/*      */ 
/*      */ 
/*      */         
/*  526 */         this.cycleDetectionStack.pop();
/*  527 */         return pushObject(obj, fieldName);
/*      */       } 
/*  529 */       return null;
/*      */     } 
/*      */ 
/*      */     
/*  533 */     reportError((ValidationEvent)new ValidationEventImpl(1, Messages.CYCLE_IN_MARSHALLER.format(new Object[] { this.cycleDetectionStack.getCycleString() }, ), getCurrentLocation(fieldName), null));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  538 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void childAsSoleContent(Object child, String fieldName) throws SAXException, IOException, XMLStreamException {
/*  557 */     if (child == null) {
/*  558 */       handleMissingObjectError(fieldName);
/*      */     } else {
/*  560 */       JaxBeanInfo<Object> beanInfo; child = pushObject(child, fieldName);
/*  561 */       if (child == null) {
/*      */         
/*  563 */         endNamespaceDecls((Object)null);
/*  564 */         endAttributes();
/*  565 */         this.cycleDetectionStack.pop();
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  570 */         beanInfo = this.grammar.getBeanInfo(child, true);
/*  571 */       } catch (JAXBException e) {
/*  572 */         reportError(fieldName, (Throwable)e);
/*      */         
/*  574 */         endNamespaceDecls((Object)null);
/*  575 */         endAttributes();
/*  576 */         this.cycleDetectionStack.pop();
/*      */         
/*      */         return;
/*      */       } 
/*  580 */       boolean lookForLifecycleMethods = beanInfo.lookForLifecycleMethods();
/*  581 */       if (lookForLifecycleMethods) {
/*  582 */         fireBeforeMarshalEvents(beanInfo, child);
/*      */       }
/*      */       
/*  585 */       beanInfo.serializeURIs(child, this);
/*  586 */       endNamespaceDecls(child);
/*  587 */       beanInfo.serializeAttributes(child, this);
/*  588 */       endAttributes();
/*  589 */       beanInfo.serializeBody(child, this);
/*      */       
/*  591 */       if (lookForLifecycleMethods) {
/*  592 */         fireAfterMarshalEvents(beanInfo, child);
/*      */       }
/*      */       
/*  595 */       this.cycleDetectionStack.pop();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void childAsXsiType(Object child, String fieldName, JaxBeanInfo<Object> expected) throws SAXException, IOException, XMLStreamException {
/*  624 */     if (child == null) {
/*  625 */       handleMissingObjectError(fieldName);
/*      */     } else {
/*  627 */       child = pushObject(child, fieldName);
/*  628 */       if (child == null) {
/*  629 */         endNamespaceDecls((Object)null);
/*  630 */         endAttributes();
/*      */         
/*      */         return;
/*      */       } 
/*  634 */       boolean asExpected = (child.getClass() == expected.jaxbType);
/*  635 */       JaxBeanInfo<Object> actual = expected;
/*  636 */       QName actualTypeName = null;
/*      */       
/*  638 */       if (asExpected && actual.lookForLifecycleMethods()) {
/*  639 */         fireBeforeMarshalEvents(actual, child);
/*      */       }
/*      */       
/*  642 */       if (!asExpected) {
/*      */         try {
/*  644 */           actual = this.grammar.getBeanInfo(child, true);
/*  645 */           if (actual.lookForLifecycleMethods()) {
/*  646 */             fireBeforeMarshalEvents(actual, child);
/*      */           }
/*  648 */         } catch (JAXBException e) {
/*  649 */           reportError(fieldName, (Throwable)e);
/*  650 */           endNamespaceDecls((Object)null);
/*  651 */           endAttributes();
/*      */           return;
/*      */         } 
/*  654 */         if (actual == expected) {
/*  655 */           asExpected = true;
/*      */         } else {
/*  657 */           actualTypeName = actual.getTypeName(child);
/*  658 */           if (actualTypeName == null) {
/*  659 */             reportError((ValidationEvent)new ValidationEventImpl(1, Messages.SUBSTITUTED_BY_ANONYMOUS_TYPE.format(new Object[] { expected.jaxbType.getName(), child.getClass().getName(), actual.jaxbType.getName() }, ), getCurrentLocation(fieldName)));
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */ 
/*      */             
/*  668 */             getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
/*  669 */             getNamespaceContext().declareNamespace(actualTypeName.getNamespaceURI(), null, false);
/*      */           } 
/*      */         } 
/*      */       } 
/*  673 */       actual.serializeURIs(child, this);
/*  674 */       endNamespaceDecls(child);
/*  675 */       if (!asExpected) {
/*  676 */         attribute("http://www.w3.org/2001/XMLSchema-instance", "type", DatatypeConverter.printQName(actualTypeName, getNamespaceContext()));
/*      */       }
/*      */       
/*  679 */       actual.serializeAttributes(child, this);
/*  680 */       endAttributes();
/*  681 */       actual.serializeBody(child, this);
/*      */       
/*  683 */       if (actual.lookForLifecycleMethods()) {
/*  684 */         fireAfterMarshalEvents(actual, child);
/*      */       }
/*      */       
/*  687 */       this.cycleDetectionStack.pop();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fireAfterMarshalEvents(JaxBeanInfo beanInfo, Object currentTarget) {
/*  702 */     if (beanInfo.hasAfterMarshalMethod()) {
/*  703 */       Method m = (beanInfo.getLifecycleMethods()).afterMarshal;
/*  704 */       fireMarshalEvent(currentTarget, m);
/*      */     } 
/*      */ 
/*      */     
/*  708 */     Marshaller.Listener externalListener = this.marshaller.getListener();
/*  709 */     if (externalListener != null) {
/*  710 */       externalListener.afterMarshal(currentTarget);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fireBeforeMarshalEvents(JaxBeanInfo beanInfo, Object currentTarget) {
/*  726 */     if (beanInfo.hasBeforeMarshalMethod()) {
/*  727 */       Method m = (beanInfo.getLifecycleMethods()).beforeMarshal;
/*  728 */       fireMarshalEvent(currentTarget, m);
/*      */     } 
/*      */ 
/*      */     
/*  732 */     Marshaller.Listener externalListener = this.marshaller.getListener();
/*  733 */     if (externalListener != null) {
/*  734 */       externalListener.beforeMarshal(currentTarget);
/*      */     }
/*      */   }
/*      */   
/*      */   private void fireMarshalEvent(Object target, Method m) {
/*      */     try {
/*  740 */       m.invoke(target, new Object[] { this.marshaller });
/*  741 */     } catch (Exception e) {
/*      */       
/*  743 */       throw new IllegalStateException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void attWildcardAsURIs(Map<QName, String> attributes, String fieldName) {
/*  748 */     if (attributes == null)
/*  749 */       return;  for (Map.Entry<QName, String> e : attributes.entrySet()) {
/*  750 */       QName n = e.getKey();
/*  751 */       String nsUri = n.getNamespaceURI();
/*  752 */       if (nsUri.length() > 0) {
/*  753 */         String p = n.getPrefix();
/*  754 */         if (p.length() == 0) p = null; 
/*  755 */         this.nsContext.declareNsUri(nsUri, p, true);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void attWildcardAsAttributes(Map<QName, String> attributes, String fieldName) throws SAXException {
/*  761 */     if (attributes == null)
/*  762 */       return;  for (Map.Entry<QName, String> e : attributes.entrySet()) {
/*  763 */       QName n = e.getKey();
/*  764 */       attribute(n.getNamespaceURI(), n.getLocalPart(), e.getValue());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeXsiNilTrue() throws SAXException, IOException, XMLStreamException {
/*  779 */     getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
/*  780 */     endNamespaceDecls((Object)null);
/*  781 */     attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
/*  782 */     endAttributes();
/*      */   }
/*      */   
/*      */   public <E> void writeDom(E element, DomHandler<E, ?> domHandler, Object parentBean, String fieldName) throws SAXException {
/*  786 */     Source source = domHandler.marshal(element, this);
/*  787 */     if (this.contentHandlerAdapter == null)
/*  788 */       this.contentHandlerAdapter = new ContentHandlerAdaptor(this); 
/*      */     try {
/*  790 */       getIdentityTransformer().transform(source, new SAXResult(this.contentHandlerAdapter));
/*  791 */     } catch (TransformerException e) {
/*  792 */       reportError(fieldName, e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Transformer getIdentityTransformer() {
/*  797 */     if (this.identityTransformer == null)
/*  798 */       this.identityTransformer = JAXBContextImpl.createTransformer(); 
/*  799 */     return this.identityTransformer;
/*      */   }
/*      */   
/*      */   public void setPrefixMapper(NamespacePrefixMapper prefixMapper) {
/*  803 */     this.nsContext.setPrefixMapper(prefixMapper);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void startDocument(XmlOutput out, boolean fragment, String schemaLocation, String noNsSchemaLocation) throws IOException, SAXException, XMLStreamException {
/*      */     MTOMXmlOutput mTOMXmlOutput;
/*  815 */     setThreadAffinity();
/*  816 */     pushCoordinator();
/*  817 */     this.nsContext.reset();
/*  818 */     this.nse = this.nsContext.getCurrent();
/*  819 */     if (this.attachmentMarshaller != null && this.attachmentMarshaller.isXOPPackage())
/*  820 */       mTOMXmlOutput = new MTOMXmlOutput(out); 
/*  821 */     this.out = (XmlOutput)mTOMXmlOutput;
/*  822 */     this.objectsWithId.clear();
/*  823 */     this.idReferencedObjects.clear();
/*  824 */     this.textHasAlreadyPrinted = false;
/*  825 */     this.seenRoot = false;
/*  826 */     this.schemaLocation = schemaLocation;
/*  827 */     this.noNsSchemaLocation = noNsSchemaLocation;
/*  828 */     this.fragment = fragment;
/*  829 */     this.inlineBinaryFlag = false;
/*  830 */     this.expectedMimeType = null;
/*  831 */     this.cycleDetectionStack.reset();
/*      */     
/*  833 */     mTOMXmlOutput.startDocument(this, fragment, this.knownUri2prefixIndexMap, this.nsContext);
/*      */   }
/*      */   
/*      */   public void endDocument() throws IOException, SAXException, XMLStreamException {
/*  837 */     this.out.endDocument(this.fragment);
/*      */   }
/*      */   
/*      */   public void close() {
/*  841 */     popCoordinator();
/*  842 */     resetThreadAffinity();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInscopeBinding(String nsUri, String prefix) {
/*  855 */     this.nsContext.put(nsUri, prefix);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getXMIMEContentType() {
/*  869 */     String v = this.grammar.getXMIMEContentType(this.cycleDetectionStack.peek());
/*  870 */     if (v != null) return v;
/*      */ 
/*      */     
/*  873 */     if (this.expectedMimeType != null) {
/*  874 */       return this.expectedMimeType.toString();
/*      */     }
/*  876 */     return null;
/*      */   }
/*      */   
/*      */   private void startElement() {
/*  880 */     this.nse = this.nse.push();
/*      */     
/*  882 */     if (!this.seenRoot) {
/*      */ 
/*      */       
/*  885 */       String[] knownUris = this.nameList.namespaceURIs;
/*  886 */       for (int i = 0; i < knownUris.length; i++) {
/*  887 */         this.knownUri2prefixIndexMap[i] = this.nsContext.declareNsUri(knownUris[i], null, this.nameList.nsUriCannotBeDefaulted[i]);
/*      */       }
/*      */ 
/*      */       
/*  891 */       String[] uris = this.nsContext.getPrefixMapper().getPreDeclaredNamespaceUris();
/*  892 */       if (uris != null)
/*  893 */         for (String uri : uris) {
/*  894 */           if (uri != null) {
/*  895 */             this.nsContext.declareNsUri(uri, null, false);
/*      */           }
/*      */         }  
/*  898 */       String[] pairs = this.nsContext.getPrefixMapper().getPreDeclaredNamespaceUris2();
/*  899 */       if (pairs != null) {
/*  900 */         for (int j = 0; j < pairs.length; j += 2) {
/*  901 */           String prefix = pairs[j];
/*  902 */           String nsUri = pairs[j + 1];
/*  903 */           if (prefix != null && nsUri != null)
/*      */           {
/*      */ 
/*      */             
/*  907 */             this.nsContext.put(nsUri, prefix);
/*      */           }
/*      */         } 
/*      */       }
/*  911 */       if (this.schemaLocation != null || this.noNsSchemaLocation != null) {
/*  912 */         this.nsContext.declareNsUri("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
/*      */       }
/*      */     } 
/*      */     
/*  916 */     this.nsContext.collectionMode = true;
/*  917 */     this.textHasAlreadyPrinted = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MimeType setExpectedMimeType(MimeType expectedMimeType) {
/*  927 */     MimeType old = this.expectedMimeType;
/*  928 */     this.expectedMimeType = expectedMimeType;
/*  929 */     return old;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setInlineBinaryFlag(boolean value) {
/*  938 */     boolean old = this.inlineBinaryFlag;
/*  939 */     this.inlineBinaryFlag = value;
/*  940 */     return old;
/*      */   }
/*      */   
/*      */   public boolean getInlineBinaryFlag() {
/*  944 */     return this.inlineBinaryFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public QName setSchemaType(QName st) {
/*  959 */     QName old = this.schemaType;
/*  960 */     this.schemaType = st;
/*  961 */     return old;
/*      */   }
/*      */   
/*      */   public QName getSchemaType() {
/*  965 */     return this.schemaType;
/*      */   }
/*      */   
/*      */   public void setObjectIdentityCycleDetection(boolean val) {
/*  969 */     this.cycleDetectionStack.setUseIdentity(val);
/*      */   }
/*      */   public boolean getObjectIdentityCycleDetection() {
/*  972 */     return this.cycleDetectionStack.getUseIdentity();
/*      */   }
/*      */ 
/*      */   
/*      */   void reconcileID() throws SAXException {
/*  977 */     this.idReferencedObjects.removeAll(this.objectsWithId);
/*      */     
/*  979 */     for (Object idObj : this.idReferencedObjects) {
/*      */       try {
/*  981 */         String id = getIdFromObject(idObj);
/*  982 */         reportError((ValidationEvent)new NotIdentifiableEventImpl(1, Messages.DANGLING_IDREF.format(new Object[] { id }, ), (ValidationEventLocator)new ValidationEventLocatorImpl(idObj)));
/*      */ 
/*      */       
/*      */       }
/*  986 */       catch (JAXBException e) {}
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  992 */     this.idReferencedObjects.clear();
/*  993 */     this.objectsWithId.clear();
/*      */   }
/*      */   
/*      */   public boolean handleError(Exception e) {
/*  997 */     return handleError(e, this.cycleDetectionStack.peek(), (String)null);
/*      */   }
/*      */   
/*      */   public boolean handleError(Exception e, Object source, String fieldName) {
/* 1001 */     return handleEvent((ValidationEvent)new ValidationEventImpl(1, e.getMessage(), (ValidationEventLocator)new ValidationEventLocatorExImpl(source, fieldName), e));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean handleEvent(ValidationEvent event) {
/*      */     try {
/* 1011 */       return this.marshaller.getEventHandler().handleEvent(event);
/* 1012 */     } catch (JAXBException e) {
/*      */       
/* 1014 */       throw new Error(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void reportMissingObjectError(String fieldName) throws SAXException {
/* 1019 */     reportError((ValidationEvent)new ValidationEventImpl(1, Messages.MISSING_OBJECT.format(new Object[] { fieldName }, ), getCurrentLocation(fieldName), new NullPointerException()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void errorMissingId(Object obj) throws SAXException {
/* 1030 */     reportError((ValidationEvent)new ValidationEventImpl(1, Messages.MISSING_ID.format(new Object[] { obj }, ), (ValidationEventLocator)new ValidationEventLocatorImpl(obj)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ValidationEventLocator getCurrentLocation(String fieldName) {
/* 1037 */     return (ValidationEventLocator)new ValidationEventLocatorExImpl(this.cycleDetectionStack.peek(), fieldName);
/*      */   }
/*      */   
/*      */   protected ValidationEventLocator getLocation() {
/* 1041 */     return getCurrentLocation((String)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static XMLSerializer getInstance() {
/* 1049 */     return (XMLSerializer)Coordinator._getInstance();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\XMLSerializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */