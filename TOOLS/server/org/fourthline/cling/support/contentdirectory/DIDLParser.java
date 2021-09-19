/*      */ package org.fourthline.cling.support.contentdirectory;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.StringReader;
/*      */ import java.io.StringWriter;
/*      */ import java.net.URI;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import org.fourthline.cling.model.XMLUtil;
/*      */ import org.fourthline.cling.model.types.Datatype;
/*      */ import org.fourthline.cling.model.types.InvalidValueException;
/*      */ import org.fourthline.cling.support.model.DIDLAttribute;
/*      */ import org.fourthline.cling.support.model.DIDLContent;
/*      */ import org.fourthline.cling.support.model.DIDLObject;
/*      */ import org.fourthline.cling.support.model.DescMeta;
/*      */ import org.fourthline.cling.support.model.Person;
/*      */ import org.fourthline.cling.support.model.PersonWithRole;
/*      */ import org.fourthline.cling.support.model.ProtocolInfo;
/*      */ import org.fourthline.cling.support.model.Res;
/*      */ import org.fourthline.cling.support.model.StorageMedium;
/*      */ import org.fourthline.cling.support.model.WriteStatus;
/*      */ import org.fourthline.cling.support.model.container.Container;
/*      */ import org.fourthline.cling.support.model.item.Item;
/*      */ import org.seamless.util.Exceptions;
/*      */ import org.seamless.util.io.IO;
/*      */ import org.seamless.xml.SAXParser;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.InputSource;
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
/*      */ public class DIDLParser
/*      */   extends SAXParser
/*      */ {
/*   80 */   private static final Logger log = Logger.getLogger(DIDLParser.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String UNKNOWN_TITLE = "Unknown Title";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DIDLContent parseResource(String resource) throws Exception {
/*   92 */     InputStream is = null;
/*      */     try {
/*   94 */       is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
/*   95 */       return parse(IO.readLines(is));
/*      */     } finally {
/*   97 */       if (is != null) is.close();
/*      */     
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
/*      */   public DIDLContent parse(String xml) throws Exception {
/*  110 */     if (xml == null || xml.length() == 0) {
/*  111 */       throw new RuntimeException("Null or empty XML");
/*      */     }
/*      */     
/*  114 */     DIDLContent content = new DIDLContent();
/*  115 */     createRootHandler(content, this);
/*      */     
/*  117 */     log.fine("Parsing DIDL XML content");
/*  118 */     parse(new InputSource(new StringReader(xml)));
/*  119 */     return content;
/*      */   }
/*      */   
/*      */   protected RootHandler createRootHandler(DIDLContent instance, SAXParser parser) {
/*  123 */     return new RootHandler(instance, parser);
/*      */   }
/*      */   
/*      */   protected ContainerHandler createContainerHandler(Container instance, SAXParser.Handler parent) {
/*  127 */     return new ContainerHandler(instance, parent);
/*      */   }
/*      */   
/*      */   protected ItemHandler createItemHandler(Item instance, SAXParser.Handler parent) {
/*  131 */     return new ItemHandler(instance, parent);
/*      */   }
/*      */   
/*      */   protected ResHandler createResHandler(Res instance, SAXParser.Handler parent) {
/*  135 */     return new ResHandler(instance, parent);
/*      */   }
/*      */   
/*      */   protected DescMetaHandler createDescMetaHandler(DescMeta instance, SAXParser.Handler parent) {
/*  139 */     return new DescMetaHandler(instance, parent);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Container createContainer(Attributes attributes) {
/*  144 */     Container container = new Container();
/*      */     
/*  146 */     container.setId(attributes.getValue("id"));
/*  147 */     container.setParentID(attributes.getValue("parentID"));
/*      */     
/*  149 */     if (attributes.getValue("childCount") != null) {
/*  150 */       container.setChildCount(Integer.valueOf(attributes.getValue("childCount")));
/*      */     }
/*      */     try {
/*  153 */       Boolean value = (Boolean)Datatype.Builtin.BOOLEAN.getDatatype().valueOf(attributes
/*  154 */           .getValue("restricted"));
/*      */       
/*  156 */       if (value != null) {
/*  157 */         container.setRestricted(value.booleanValue());
/*      */       }
/*  159 */       value = (Boolean)Datatype.Builtin.BOOLEAN.getDatatype().valueOf(attributes
/*  160 */           .getValue("searchable"));
/*      */       
/*  162 */       if (value != null)
/*  163 */         container.setSearchable(value.booleanValue()); 
/*  164 */     } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */     
/*  168 */     return container;
/*      */   }
/*      */   
/*      */   protected Item createItem(Attributes attributes) {
/*  172 */     Item item = new Item();
/*      */     
/*  174 */     item.setId(attributes.getValue("id"));
/*  175 */     item.setParentID(attributes.getValue("parentID"));
/*      */     
/*      */     try {
/*  178 */       Boolean value = (Boolean)Datatype.Builtin.BOOLEAN.getDatatype().valueOf(attributes
/*  179 */           .getValue("restricted"));
/*      */       
/*  181 */       if (value != null) {
/*  182 */         item.setRestricted(value.booleanValue());
/*      */       }
/*  184 */     } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */     
/*  188 */     if (attributes.getValue("refID") != null) {
/*  189 */       item.setRefID(attributes.getValue("refID"));
/*      */     }
/*  191 */     return item;
/*      */   }
/*      */   
/*      */   protected Res createResource(Attributes attributes) {
/*  195 */     Res res = new Res();
/*      */     
/*  197 */     if (attributes.getValue("importUri") != null) {
/*  198 */       res.setImportUri(URI.create(attributes.getValue("importUri")));
/*      */     }
/*      */     try {
/*  201 */       res.setProtocolInfo(new ProtocolInfo(attributes
/*  202 */             .getValue("protocolInfo")));
/*      */     }
/*  204 */     catch (InvalidValueException ex) {
/*  205 */       log.warning("In DIDL content, invalid resource protocol info: " + Exceptions.unwrap((Throwable)ex));
/*  206 */       return null;
/*      */     } 
/*      */     
/*  209 */     if (attributes.getValue("size") != null) {
/*  210 */       res.setSize(toLongOrNull(attributes.getValue("size")));
/*      */     }
/*  212 */     if (attributes.getValue("duration") != null) {
/*  213 */       res.setDuration(attributes.getValue("duration"));
/*      */     }
/*  215 */     if (attributes.getValue("bitrate") != null) {
/*  216 */       res.setBitrate(toLongOrNull(attributes.getValue("bitrate")));
/*      */     }
/*  218 */     if (attributes.getValue("sampleFrequency") != null) {
/*  219 */       res.setSampleFrequency(toLongOrNull(attributes.getValue("sampleFrequency")));
/*      */     }
/*  221 */     if (attributes.getValue("bitsPerSample") != null) {
/*  222 */       res.setBitsPerSample(toLongOrNull(attributes.getValue("bitsPerSample")));
/*      */     }
/*  224 */     if (attributes.getValue("nrAudioChannels") != null) {
/*  225 */       res.setNrAudioChannels(toLongOrNull(attributes.getValue("nrAudioChannels")));
/*      */     }
/*  227 */     if (attributes.getValue("colorDepth") != null) {
/*  228 */       res.setColorDepth(toLongOrNull(attributes.getValue("colorDepth")));
/*      */     }
/*  230 */     if (attributes.getValue("protection") != null) {
/*  231 */       res.setProtection(attributes.getValue("protection"));
/*      */     }
/*  233 */     if (attributes.getValue("resolution") != null) {
/*  234 */       res.setResolution(attributes.getValue("resolution"));
/*      */     }
/*  236 */     return res;
/*      */   }
/*      */   
/*      */   private Long toLongOrNull(String value) {
/*      */     try {
/*  241 */       return Long.valueOf(value);
/*  242 */     } catch (NumberFormatException x) {
/*  243 */       return null;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected DescMeta createDescMeta(Attributes attributes) {
/*  248 */     DescMeta desc = new DescMeta();
/*      */     
/*  250 */     desc.setId(attributes.getValue("id"));
/*      */     
/*  252 */     if (attributes.getValue("type") != null) {
/*  253 */       desc.setType(attributes.getValue("type"));
/*      */     }
/*  255 */     if (attributes.getValue("nameSpace") != null) {
/*  256 */       desc.setNameSpace(URI.create(attributes.getValue("nameSpace")));
/*      */     }
/*  258 */     return desc;
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
/*      */   public String generate(DIDLContent content) throws Exception {
/*  277 */     return generate(content, false);
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
/*      */   public String generate(DIDLContent content, boolean nestedItems) throws Exception {
/*  295 */     return documentToString(buildDOM(content, nestedItems), true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected String documentToString(Document document, boolean omitProlog) throws Exception {
/*  301 */     TransformerFactory transFactory = TransformerFactory.newInstance();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  306 */     Transformer transformer = transFactory.newTransformer();
/*      */     
/*  308 */     if (omitProlog)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  313 */       transformer.setOutputProperty("omit-xml-declaration", "yes");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  319 */     StringWriter out = new StringWriter();
/*  320 */     transformer.transform(new DOMSource(document), new StreamResult(out));
/*  321 */     return out.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected Document buildDOM(DIDLContent content, boolean nestedItems) throws Exception {
/*  326 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  327 */     factory.setNamespaceAware(true);
/*      */     
/*  329 */     Document d = factory.newDocumentBuilder().newDocument();
/*      */     
/*  331 */     generateRoot(content, d, nestedItems);
/*      */     
/*  333 */     return d;
/*      */   }
/*      */   
/*      */   protected void generateRoot(DIDLContent content, Document descriptor, boolean nestedItems) {
/*  337 */     Element rootElement = descriptor.createElementNS("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "DIDL-Lite");
/*  338 */     descriptor.appendChild(rootElement);
/*      */ 
/*      */     
/*  341 */     rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:upnp", "urn:schemas-upnp-org:metadata-1-0/upnp/");
/*  342 */     rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dc", "http://purl.org/dc/elements/1.1/");
/*  343 */     rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:sec", "http://www.sec.co.kr/");
/*      */     
/*  345 */     for (Container container : content.getContainers()) {
/*  346 */       if (container == null)
/*  347 */         continue;  generateContainer(container, descriptor, rootElement, nestedItems);
/*      */     } 
/*      */     
/*  350 */     for (Item item : content.getItems()) {
/*  351 */       if (item == null)
/*  352 */         continue;  generateItem(item, descriptor, rootElement);
/*      */     } 
/*      */     
/*  355 */     for (DescMeta descMeta : content.getDescMetadata()) {
/*  356 */       if (descMeta == null)
/*  357 */         continue;  generateDescMetadata(descMeta, descriptor, rootElement);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void generateContainer(Container container, Document descriptor, Element parent, boolean nestedItems) {
/*  363 */     if (container.getClazz() == null) {
/*  364 */       throw new RuntimeException("Missing 'upnp:class' element for container: " + container.getId());
/*      */     }
/*      */     
/*  367 */     Element containerElement = XMLUtil.appendNewElement(descriptor, parent, "container");
/*      */     
/*  369 */     if (container.getId() == null)
/*  370 */       throw new NullPointerException("Missing id on container: " + container); 
/*  371 */     containerElement.setAttribute("id", container.getId());
/*      */     
/*  373 */     if (container.getParentID() == null)
/*  374 */       throw new NullPointerException("Missing parent id on container: " + container); 
/*  375 */     containerElement.setAttribute("parentID", container.getParentID());
/*      */     
/*  377 */     if (container.getChildCount() != null) {
/*  378 */       containerElement.setAttribute("childCount", Integer.toString(container.getChildCount().intValue()));
/*      */     }
/*      */     
/*  381 */     containerElement.setAttribute("restricted", booleanToInt(container.isRestricted()));
/*  382 */     containerElement.setAttribute("searchable", booleanToInt(container.isSearchable()));
/*      */     
/*  384 */     String title = container.getTitle();
/*  385 */     if (title == null) {
/*  386 */       log.warning("Missing 'dc:title' element for container: " + container.getId());
/*  387 */       title = "Unknown Title";
/*      */     } 
/*      */     
/*  390 */     XMLUtil.appendNewElementIfNotNull(descriptor, containerElement, "dc:title", title, "http://purl.org/dc/elements/1.1/");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  398 */     XMLUtil.appendNewElementIfNotNull(descriptor, containerElement, "dc:creator", container
/*      */ 
/*      */ 
/*      */         
/*  402 */         .getCreator(), "http://purl.org/dc/elements/1.1/");
/*      */ 
/*      */ 
/*      */     
/*  406 */     XMLUtil.appendNewElementIfNotNull(descriptor, containerElement, "upnp:writeStatus", container
/*      */ 
/*      */ 
/*      */         
/*  410 */         .getWriteStatus(), "urn:schemas-upnp-org:metadata-1-0/upnp/");
/*      */ 
/*      */ 
/*      */     
/*  414 */     appendClass(descriptor, containerElement, container.getClazz(), "upnp:class", false);
/*      */     
/*  416 */     for (DIDLObject.Class searchClass : container.getSearchClasses()) {
/*  417 */       appendClass(descriptor, containerElement, searchClass, "upnp:searchClass", true);
/*      */     }
/*      */     
/*  420 */     for (DIDLObject.Class createClass : container.getCreateClasses()) {
/*  421 */       appendClass(descriptor, containerElement, createClass, "upnp:createClass", true);
/*      */     }
/*      */     
/*  424 */     appendProperties(descriptor, containerElement, (DIDLObject)container, "upnp", (Class)DIDLObject.Property.UPNP.NAMESPACE.class, "urn:schemas-upnp-org:metadata-1-0/upnp/");
/*  425 */     appendProperties(descriptor, containerElement, (DIDLObject)container, "dc", (Class)DIDLObject.Property.DC.NAMESPACE.class, "http://purl.org/dc/elements/1.1/");
/*      */     
/*  427 */     if (nestedItems) {
/*  428 */       for (Item item : container.getItems()) {
/*  429 */         if (item == null)
/*  430 */           continue;  generateItem(item, descriptor, containerElement);
/*      */       } 
/*      */     }
/*      */     
/*  434 */     for (Res resource : container.getResources()) {
/*  435 */       if (resource == null)
/*  436 */         continue;  generateResource(resource, descriptor, containerElement);
/*      */     } 
/*      */     
/*  439 */     for (DescMeta descMeta : container.getDescMetadata()) {
/*  440 */       if (descMeta == null)
/*  441 */         continue;  generateDescMetadata(descMeta, descriptor, containerElement);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void generateItem(Item item, Document descriptor, Element parent) {
/*  447 */     if (item.getClazz() == null) {
/*  448 */       throw new RuntimeException("Missing 'upnp:class' element for item: " + item.getId());
/*      */     }
/*      */     
/*  451 */     Element itemElement = XMLUtil.appendNewElement(descriptor, parent, "item");
/*      */     
/*  453 */     if (item.getId() == null)
/*  454 */       throw new NullPointerException("Missing id on item: " + item); 
/*  455 */     itemElement.setAttribute("id", item.getId());
/*      */     
/*  457 */     if (item.getParentID() == null)
/*  458 */       throw new NullPointerException("Missing parent id on item: " + item); 
/*  459 */     itemElement.setAttribute("parentID", item.getParentID());
/*      */     
/*  461 */     if (item.getRefID() != null)
/*  462 */       itemElement.setAttribute("refID", item.getRefID()); 
/*  463 */     itemElement.setAttribute("restricted", booleanToInt(item.isRestricted()));
/*      */     
/*  465 */     String title = item.getTitle();
/*  466 */     if (title == null) {
/*  467 */       log.warning("Missing 'dc:title' element for item: " + item.getId());
/*  468 */       title = "Unknown Title";
/*      */     } 
/*      */     
/*  471 */     XMLUtil.appendNewElementIfNotNull(descriptor, itemElement, "dc:title", title, "http://purl.org/dc/elements/1.1/");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  479 */     XMLUtil.appendNewElementIfNotNull(descriptor, itemElement, "dc:creator", item
/*      */ 
/*      */ 
/*      */         
/*  483 */         .getCreator(), "http://purl.org/dc/elements/1.1/");
/*      */ 
/*      */ 
/*      */     
/*  487 */     XMLUtil.appendNewElementIfNotNull(descriptor, itemElement, "upnp:writeStatus", item
/*      */ 
/*      */ 
/*      */         
/*  491 */         .getWriteStatus(), "urn:schemas-upnp-org:metadata-1-0/upnp/");
/*      */ 
/*      */ 
/*      */     
/*  495 */     appendClass(descriptor, itemElement, item.getClazz(), "upnp:class", false);
/*      */     
/*  497 */     appendProperties(descriptor, itemElement, (DIDLObject)item, "upnp", (Class)DIDLObject.Property.UPNP.NAMESPACE.class, "urn:schemas-upnp-org:metadata-1-0/upnp/");
/*  498 */     appendProperties(descriptor, itemElement, (DIDLObject)item, "dc", (Class)DIDLObject.Property.DC.NAMESPACE.class, "http://purl.org/dc/elements/1.1/");
/*  499 */     appendProperties(descriptor, itemElement, (DIDLObject)item, "sec", (Class)DIDLObject.Property.SEC.NAMESPACE.class, "http://www.sec.co.kr/");
/*      */     
/*  501 */     for (Res resource : item.getResources()) {
/*  502 */       if (resource == null)
/*  503 */         continue;  generateResource(resource, descriptor, itemElement);
/*      */     } 
/*      */     
/*  506 */     for (DescMeta descMeta : item.getDescMetadata()) {
/*  507 */       if (descMeta == null)
/*  508 */         continue;  generateDescMetadata(descMeta, descriptor, itemElement);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void generateResource(Res resource, Document descriptor, Element parent) {
/*  514 */     if (resource.getValue() == null) {
/*  515 */       throw new RuntimeException("Missing resource URI value" + resource);
/*      */     }
/*  517 */     if (resource.getProtocolInfo() == null) {
/*  518 */       throw new RuntimeException("Missing resource protocol info: " + resource);
/*      */     }
/*      */     
/*  521 */     Element resourceElement = XMLUtil.appendNewElement(descriptor, parent, "res", resource.getValue());
/*  522 */     resourceElement.setAttribute("protocolInfo", resource.getProtocolInfo().toString());
/*  523 */     if (resource.getImportUri() != null)
/*  524 */       resourceElement.setAttribute("importUri", resource.getImportUri().toString()); 
/*  525 */     if (resource.getSize() != null)
/*  526 */       resourceElement.setAttribute("size", resource.getSize().toString()); 
/*  527 */     if (resource.getDuration() != null)
/*  528 */       resourceElement.setAttribute("duration", resource.getDuration()); 
/*  529 */     if (resource.getBitrate() != null)
/*  530 */       resourceElement.setAttribute("bitrate", resource.getBitrate().toString()); 
/*  531 */     if (resource.getSampleFrequency() != null)
/*  532 */       resourceElement.setAttribute("sampleFrequency", resource.getSampleFrequency().toString()); 
/*  533 */     if (resource.getBitsPerSample() != null)
/*  534 */       resourceElement.setAttribute("bitsPerSample", resource.getBitsPerSample().toString()); 
/*  535 */     if (resource.getNrAudioChannels() != null)
/*  536 */       resourceElement.setAttribute("nrAudioChannels", resource.getNrAudioChannels().toString()); 
/*  537 */     if (resource.getColorDepth() != null)
/*  538 */       resourceElement.setAttribute("colorDepth", resource.getColorDepth().toString()); 
/*  539 */     if (resource.getProtection() != null)
/*  540 */       resourceElement.setAttribute("protection", resource.getProtection()); 
/*  541 */     if (resource.getResolution() != null) {
/*  542 */       resourceElement.setAttribute("resolution", resource.getResolution());
/*      */     }
/*      */   }
/*      */   
/*      */   protected void generateDescMetadata(DescMeta descMeta, Document descriptor, Element parent) {
/*  547 */     if (descMeta.getId() == null) {
/*  548 */       throw new RuntimeException("Missing id of description metadata: " + descMeta);
/*      */     }
/*  550 */     if (descMeta.getNameSpace() == null) {
/*  551 */       throw new RuntimeException("Missing namespace of description metadata: " + descMeta);
/*      */     }
/*      */     
/*  554 */     Element descElement = XMLUtil.appendNewElement(descriptor, parent, "desc");
/*  555 */     descElement.setAttribute("id", descMeta.getId());
/*  556 */     descElement.setAttribute("nameSpace", descMeta.getNameSpace().toString());
/*  557 */     if (descMeta.getType() != null)
/*  558 */       descElement.setAttribute("type", descMeta.getType()); 
/*  559 */     populateDescMetadata(descElement, descMeta);
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
/*      */   protected void populateDescMetadata(Element descElement, DescMeta descMeta) {
/*  574 */     if (descMeta.getMetadata() instanceof Document) {
/*  575 */       Document doc = (Document)descMeta.getMetadata();
/*      */       
/*  577 */       NodeList nl = doc.getDocumentElement().getChildNodes();
/*  578 */       for (int i = 0; i < nl.getLength(); i++) {
/*  579 */         Node n = nl.item(i);
/*  580 */         if (n.getNodeType() == 1) {
/*      */ 
/*      */           
/*  583 */           Node clone = descElement.getOwnerDocument().importNode(n, true);
/*  584 */           descElement.appendChild(clone);
/*      */         } 
/*      */       } 
/*      */     } else {
/*  588 */       log.warning("Unknown desc metadata content, please override populateDescMetadata(): " + descMeta.getMetadata());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void appendProperties(Document descriptor, Element parent, DIDLObject object, String prefix, Class<? extends DIDLObject.Property.NAMESPACE> namespace, String namespaceURI) {
/*  595 */     for (DIDLObject.Property<Object> property : object.getPropertiesByNamespace(namespace)) {
/*  596 */       Element el = descriptor.createElementNS(namespaceURI, prefix + ":" + property.getDescriptorName());
/*  597 */       parent.appendChild(el);
/*  598 */       property.setOnElement(el);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void appendClass(Document descriptor, Element parent, DIDLObject.Class clazz, String element, boolean appendDerivation) {
/*  603 */     Element classElement = XMLUtil.appendNewElementIfNotNull(descriptor, parent, element, clazz
/*      */ 
/*      */ 
/*      */         
/*  607 */         .getValue(), "urn:schemas-upnp-org:metadata-1-0/upnp/");
/*      */ 
/*      */     
/*  610 */     if (clazz.getFriendlyName() != null && clazz.getFriendlyName().length() > 0)
/*  611 */       classElement.setAttribute("name", clazz.getFriendlyName()); 
/*  612 */     if (appendDerivation)
/*  613 */       classElement.setAttribute("includeDerived", Boolean.toString(clazz.isIncludeDerived())); 
/*      */   }
/*      */   
/*      */   protected String booleanToInt(boolean b) {
/*  617 */     return b ? "1" : "0";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void debugXML(String s) {
/*  626 */     if (log.isLoggable(Level.FINE)) {
/*  627 */       log.fine("-------------------------------------------------------------------------------------");
/*  628 */       log.fine("\n" + s);
/*  629 */       log.fine("-------------------------------------------------------------------------------------");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract class DIDLObjectHandler<I extends DIDLObject>
/*      */     extends SAXParser.Handler<I>
/*      */   {
/*      */     protected DIDLObjectHandler(I instance, SAXParser.Handler parent) {
/*  640 */       super(instance, parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public void endElement(String uri, String localName, String qName) throws SAXException {
/*  645 */       super.endElement(uri, localName, qName);
/*      */       
/*  647 */       if ("http://purl.org/dc/elements/1.1/".equals(uri)) {
/*      */         
/*  649 */         if ("title".equals(localName)) {
/*  650 */           ((DIDLObject)getInstance()).setTitle(getCharacters());
/*  651 */         } else if ("creator".equals(localName)) {
/*  652 */           ((DIDLObject)getInstance()).setCreator(getCharacters());
/*  653 */         } else if ("description".equals(localName)) {
/*  654 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.DC.DESCRIPTION(getCharacters()));
/*  655 */         } else if ("publisher".equals(localName)) {
/*  656 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.DC.PUBLISHER(new Person(getCharacters())));
/*  657 */         } else if ("contributor".equals(localName)) {
/*  658 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.DC.CONTRIBUTOR(new Person(getCharacters())));
/*  659 */         } else if ("date".equals(localName)) {
/*  660 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.DC.DATE(getCharacters()));
/*  661 */         } else if ("language".equals(localName)) {
/*  662 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.DC.LANGUAGE(getCharacters()));
/*  663 */         } else if ("rights".equals(localName)) {
/*  664 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RIGHTS(getCharacters()));
/*  665 */         } else if ("relation".equals(localName)) {
/*  666 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.DC.RELATION(URI.create(getCharacters())));
/*      */         }
/*      */       
/*  669 */       } else if ("urn:schemas-upnp-org:metadata-1-0/upnp/".equals(uri)) {
/*      */         
/*  671 */         if ("writeStatus".equals(localName)) {
/*      */           try {
/*  673 */             ((DIDLObject)getInstance()).setWriteStatus(
/*  674 */                 WriteStatus.valueOf(getCharacters()));
/*      */           }
/*  676 */           catch (Exception ex) {
/*  677 */             DIDLParser.log.info("Ignoring invalid writeStatus value: " + getCharacters());
/*      */           } 
/*  679 */         } else if ("class".equals(localName)) {
/*  680 */           ((DIDLObject)getInstance()).setClazz(new DIDLObject.Class(
/*      */                 
/*  682 */                 getCharacters(), 
/*  683 */                 getAttributes().getValue("name")));
/*      */         
/*      */         }
/*  686 */         else if ("artist".equals(localName)) {
/*  687 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST(new PersonWithRole(
/*      */                   
/*  689 */                   getCharacters(), getAttributes().getValue("role"))));
/*      */         
/*      */         }
/*  692 */         else if ("actor".equals(localName)) {
/*  693 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ACTOR(new PersonWithRole(
/*      */                   
/*  695 */                   getCharacters(), getAttributes().getValue("role"))));
/*      */         
/*      */         }
/*  698 */         else if ("author".equals(localName)) {
/*  699 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.AUTHOR(new PersonWithRole(
/*      */                   
/*  701 */                   getCharacters(), getAttributes().getValue("role"))));
/*      */         
/*      */         }
/*  704 */         else if ("producer".equals(localName)) {
/*  705 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PRODUCER(new Person(
/*  706 */                   getCharacters())));
/*      */         }
/*  708 */         else if ("director".equals(localName)) {
/*  709 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.DIRECTOR(new Person(
/*  710 */                   getCharacters())));
/*      */         }
/*  712 */         else if ("longDescription".equals(localName)) {
/*  713 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LONG_DESCRIPTION(
/*  714 */                 getCharacters()));
/*      */         }
/*  716 */         else if ("storageUsed".equals(localName)) {
/*  717 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_USED(
/*  718 */                 Long.valueOf(getCharacters())));
/*      */         }
/*  720 */         else if ("storageTotal".equals(localName)) {
/*  721 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_TOTAL(
/*  722 */                 Long.valueOf(getCharacters())));
/*      */         }
/*  724 */         else if ("storageFree".equals(localName)) {
/*  725 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_FREE(
/*  726 */                 Long.valueOf(getCharacters())));
/*      */         }
/*  728 */         else if ("storageMaxPartition".equals(localName)) {
/*  729 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MAX_PARTITION(
/*  730 */                 Long.valueOf(getCharacters())));
/*      */         }
/*  732 */         else if ("storageMedium".equals(localName)) {
/*  733 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(
/*  734 */                 StorageMedium.valueOrVendorSpecificOf(getCharacters())));
/*      */         }
/*  736 */         else if ("genre".equals(localName)) {
/*  737 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.GENRE(
/*  738 */                 getCharacters()));
/*      */         }
/*  740 */         else if ("album".equals(localName)) {
/*  741 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ALBUM(
/*  742 */                 getCharacters()));
/*      */         }
/*  744 */         else if ("playlist".equals(localName)) {
/*  745 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.PLAYLIST(
/*  746 */                 getCharacters()));
/*      */         }
/*  748 */         else if ("region".equals(localName)) {
/*  749 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.REGION(
/*  750 */                 getCharacters()));
/*      */         }
/*  752 */         else if ("rating".equals(localName)) {
/*  753 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RATING(
/*  754 */                 getCharacters()));
/*      */         }
/*  756 */         else if ("toc".equals(localName)) {
/*  757 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.TOC(
/*  758 */                 getCharacters()));
/*      */         }
/*  760 */         else if ("albumArtURI".equals(localName)) {
/*  761 */           DIDLObject.Property.UPNP.ALBUM_ART_URI aLBUM_ART_URI = new DIDLObject.Property.UPNP.ALBUM_ART_URI(URI.create(getCharacters()));
/*      */           
/*  763 */           Attributes albumArtURIAttributes = getAttributes();
/*  764 */           for (int i = 0; i < albumArtURIAttributes.getLength(); i++) {
/*  765 */             if ("profileID".equals(albumArtURIAttributes.getLocalName(i))) {
/*  766 */               aLBUM_ART_URI.addAttribute((DIDLObject.Property)new DIDLObject.Property.DLNA.PROFILE_ID(new DIDLAttribute("urn:schemas-dlna-org:metadata-1-0/", "dlna", albumArtURIAttributes
/*      */ 
/*      */ 
/*      */ 
/*      */                       
/*  771 */                       .getValue(i))));
/*      */             }
/*      */           } 
/*      */ 
/*      */           
/*  776 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)aLBUM_ART_URI);
/*  777 */         } else if ("artistDiscographyURI".equals(localName)) {
/*  778 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ARTIST_DISCO_URI(
/*  779 */                 URI.create(getCharacters())));
/*      */         }
/*  781 */         else if ("lyricsURI".equals(localName)) {
/*  782 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.LYRICS_URI(
/*  783 */                 URI.create(getCharacters())));
/*      */         }
/*  785 */         else if ("icon".equals(localName)) {
/*  786 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ICON(
/*  787 */                 URI.create(getCharacters())));
/*      */         }
/*  789 */         else if ("radioCallSign".equals(localName)) {
/*  790 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RADIO_CALL_SIGN(
/*  791 */                 getCharacters()));
/*      */         }
/*  793 */         else if ("radioStationID".equals(localName)) {
/*  794 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RADIO_STATION_ID(
/*  795 */                 getCharacters()));
/*      */         }
/*  797 */         else if ("radioBand".equals(localName)) {
/*  798 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RADIO_BAND(
/*  799 */                 getCharacters()));
/*      */         }
/*  801 */         else if ("channelNr".equals(localName)) {
/*  802 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.CHANNEL_NR(
/*  803 */                 Integer.valueOf(getCharacters())));
/*      */         }
/*  805 */         else if ("channelName".equals(localName)) {
/*  806 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.CHANNEL_NAME(
/*  807 */                 getCharacters()));
/*      */         }
/*  809 */         else if ("scheduledStartTime".equals(localName)) {
/*  810 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.SCHEDULED_START_TIME(
/*  811 */                 getCharacters()));
/*      */         }
/*  813 */         else if ("scheduledEndTime".equals(localName)) {
/*  814 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.SCHEDULED_END_TIME(
/*  815 */                 getCharacters()));
/*      */         }
/*  817 */         else if ("DVDRegionCode".equals(localName)) {
/*  818 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.DVD_REGION_CODE(
/*  819 */                 Integer.valueOf(getCharacters())));
/*      */         }
/*  821 */         else if ("originalTrackNumber".equals(localName)) {
/*  822 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ORIGINAL_TRACK_NUMBER(
/*  823 */                 Integer.valueOf(getCharacters())));
/*      */         }
/*  825 */         else if ("userAnnotation".equals(localName)) {
/*  826 */           ((DIDLObject)getInstance()).addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.USER_ANNOTATION(
/*  827 */                 getCharacters()));
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public class RootHandler
/*      */     extends SAXParser.Handler<DIDLContent>
/*      */   {
/*      */     RootHandler(DIDLContent instance, SAXParser parser) {
/*  837 */       super(instance, parser);
/*      */     }
/*      */ 
/*      */     
/*      */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/*  842 */       super.startElement(uri, localName, qName, attributes);
/*      */       
/*  844 */       if (!"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri))
/*      */         return; 
/*  846 */       if (localName.equals("container")) {
/*      */         
/*  848 */         Container container = DIDLParser.this.createContainer(attributes);
/*  849 */         ((DIDLContent)getInstance()).addContainer(container);
/*  850 */         DIDLParser.this.createContainerHandler(container, this);
/*      */       }
/*  852 */       else if (localName.equals("item")) {
/*      */         
/*  854 */         Item item = DIDLParser.this.createItem(attributes);
/*  855 */         ((DIDLContent)getInstance()).addItem(item);
/*  856 */         DIDLParser.this.createItemHandler(item, this);
/*      */       }
/*  858 */       else if (localName.equals("desc")) {
/*      */         
/*  860 */         DescMeta desc = DIDLParser.this.createDescMeta(attributes);
/*  861 */         ((DIDLContent)getInstance()).addDescMetadata(desc);
/*  862 */         DIDLParser.this.createDescMetaHandler(desc, this);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isLastElement(String uri, String localName, String qName) {
/*  869 */       if ("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri) && "DIDL-Lite".equals(localName)) {
/*      */ 
/*      */ 
/*      */         
/*  873 */         ((DIDLContent)getInstance()).replaceGenericContainerAndItems();
/*      */         
/*  875 */         return true;
/*      */       } 
/*  877 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   public class ContainerHandler extends DIDLObjectHandler<Container> {
/*      */     public ContainerHandler(Container instance, SAXParser.Handler parent) {
/*  883 */       super(instance, parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/*  888 */       super.startElement(uri, localName, qName, attributes);
/*      */       
/*  890 */       if (!"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri))
/*      */         return; 
/*  892 */       if (localName.equals("item")) {
/*      */         
/*  894 */         Item item = DIDLParser.this.createItem(attributes);
/*  895 */         ((Container)getInstance()).addItem(item);
/*  896 */         DIDLParser.this.createItemHandler(item, this);
/*      */       }
/*  898 */       else if (localName.equals("desc")) {
/*      */         
/*  900 */         DescMeta desc = DIDLParser.this.createDescMeta(attributes);
/*  901 */         ((Container)getInstance()).addDescMetadata(desc);
/*  902 */         DIDLParser.this.createDescMetaHandler(desc, this);
/*      */       }
/*  904 */       else if (localName.equals("res")) {
/*      */         
/*  906 */         Res res = DIDLParser.this.createResource(attributes);
/*  907 */         if (res != null) {
/*  908 */           ((Container)getInstance()).addResource(res);
/*  909 */           DIDLParser.this.createResHandler(res, this);
/*      */         } 
/*      */       } 
/*      */     }
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
/*      */     public void endElement(String uri, String localName, String qName) throws SAXException {
/*  924 */       super.endElement(uri, localName, qName);
/*      */       
/*  926 */       if ("urn:schemas-upnp-org:metadata-1-0/upnp/".equals(uri))
/*      */       {
/*  928 */         if ("searchClass".equals(localName)) {
/*  929 */           ((Container)getInstance()).getSearchClasses().add(new DIDLObject.Class(
/*      */                 
/*  931 */                 getCharacters(), 
/*  932 */                 getAttributes().getValue("name"), "true"
/*  933 */                 .equals(getAttributes().getValue("includeDerived"))));
/*      */         
/*      */         }
/*  936 */         else if ("createClass".equals(localName)) {
/*  937 */           ((Container)getInstance()).getCreateClasses().add(new DIDLObject.Class(
/*      */                 
/*  939 */                 getCharacters(), 
/*  940 */                 getAttributes().getValue("name"), "true"
/*  941 */                 .equals(getAttributes().getValue("includeDerived"))));
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isLastElement(String uri, String localName, String qName) {
/*  950 */       if ("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri) && "container".equals(localName)) {
/*  951 */         if (((Container)getInstance()).getTitle() == null) {
/*  952 */           DIDLParser.log.warning("In DIDL content, missing 'dc:title' element for container: " + ((Container)getInstance()).getId());
/*      */         }
/*  954 */         if (((Container)getInstance()).getClazz() == null) {
/*  955 */           DIDLParser.log.warning("In DIDL content, missing 'upnp:class' element for container: " + ((Container)getInstance()).getId());
/*      */         }
/*  957 */         return true;
/*      */       } 
/*  959 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   public class ItemHandler extends DIDLObjectHandler<Item> {
/*      */     public ItemHandler(Item instance, SAXParser.Handler parent) {
/*  965 */       super(instance, parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/*  970 */       super.startElement(uri, localName, qName, attributes);
/*      */       
/*  972 */       if (!"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri))
/*      */         return; 
/*  974 */       if (localName.equals("res")) {
/*      */         
/*  976 */         Res res = DIDLParser.this.createResource(attributes);
/*  977 */         if (res != null) {
/*  978 */           ((Item)getInstance()).addResource(res);
/*  979 */           DIDLParser.this.createResHandler(res, this);
/*      */         }
/*      */       
/*  982 */       } else if (localName.equals("desc")) {
/*      */         
/*  984 */         DescMeta desc = DIDLParser.this.createDescMeta(attributes);
/*  985 */         ((Item)getInstance()).addDescMetadata(desc);
/*  986 */         DIDLParser.this.createDescMetaHandler(desc, this);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isLastElement(String uri, String localName, String qName) {
/*  993 */       if ("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri) && "item".equals(localName)) {
/*  994 */         if (((Item)getInstance()).getTitle() == null) {
/*  995 */           DIDLParser.log.warning("In DIDL content, missing 'dc:title' element for item: " + ((Item)getInstance()).getId());
/*      */         }
/*  997 */         if (((Item)getInstance()).getClazz() == null) {
/*  998 */           DIDLParser.log.warning("In DIDL content, missing 'upnp:class' element for item: " + ((Item)getInstance()).getId());
/*      */         }
/* 1000 */         return true;
/*      */       } 
/* 1002 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class ResHandler extends SAXParser.Handler<Res> {
/*      */     public ResHandler(Res instance, SAXParser.Handler parent) {
/* 1008 */       super(instance, parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public void endElement(String uri, String localName, String qName) throws SAXException {
/* 1013 */       super.endElement(uri, localName, qName);
/* 1014 */       ((Res)getInstance()).setValue(getCharacters());
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean isLastElement(String uri, String localName, String qName) {
/* 1019 */       return ("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri) && "res".equals(localName));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class DescMetaHandler
/*      */     extends SAXParser.Handler<DescMeta>
/*      */   {
/*      */     protected Element current;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public DescMetaHandler(DescMeta instance, SAXParser.Handler parent) {
/* 1035 */       super(instance, parent);
/* 1036 */       instance.setMetadata(instance.createMetadataDocument());
/* 1037 */       this.current = ((Document)getInstance().getMetadata()).getDocumentElement();
/*      */     }
/*      */ 
/*      */     
/*      */     public DescMeta<Document> getInstance() {
/* 1042 */       return (DescMeta<Document>)super.getInstance();
/*      */     }
/*      */ 
/*      */     
/*      */     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/* 1047 */       super.startElement(uri, localName, qName, attributes);
/*      */       
/* 1049 */       Element newEl = ((Document)getInstance().getMetadata()).createElementNS(uri, qName);
/* 1050 */       for (int i = 0; i < attributes.getLength(); i++) {
/* 1051 */         newEl.setAttributeNS(attributes
/* 1052 */             .getURI(i), attributes
/* 1053 */             .getQName(i), attributes
/* 1054 */             .getValue(i));
/*      */       }
/*      */       
/* 1057 */       this.current.appendChild(newEl);
/* 1058 */       this.current = newEl;
/*      */     }
/*      */ 
/*      */     
/*      */     public void endElement(String uri, String localName, String qName) throws SAXException {
/* 1063 */       super.endElement(uri, localName, qName);
/* 1064 */       if (isLastElement(uri, localName, qName)) {
/*      */         return;
/*      */       }
/* 1067 */       if (getCharacters().length() > 0 && !getCharacters().matches("[\\t\\n\\x0B\\f\\r\\s]+")) {
/* 1068 */         this.current.appendChild(((Document)getInstance().getMetadata()).createTextNode(getCharacters()));
/*      */       }
/* 1070 */       this.current = (Element)this.current.getParentNode();
/*      */ 
/*      */       
/* 1073 */       this.characters = new StringBuilder();
/* 1074 */       this.attributes = null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean isLastElement(String uri, String localName, String qName) {
/* 1079 */       return ("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/".equals(uri) && "desc".equals(localName));
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirectory\DIDLParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */