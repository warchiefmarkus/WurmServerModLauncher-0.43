/*     */ package org.seamless.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DOMElement<CHILD extends DOMElement, PARENT extends DOMElement>
/*     */ {
/*     */   public final Builder<PARENT> PARENT_BUILDER;
/*     */   public final ArrayBuilder<CHILD> CHILD_BUILDER;
/*     */   private final XPath xpath;
/*     */   private Element element;
/*     */   
/*     */   public DOMElement(XPath xpath, Element element) {
/*  50 */     this.xpath = xpath;
/*  51 */     this.element = element;
/*  52 */     this.PARENT_BUILDER = createParentBuilder(this);
/*  53 */     this.CHILD_BUILDER = createChildBuilder(this);
/*     */   }
/*     */   
/*     */   public Element getW3CElement() {
/*  57 */     return this.element;
/*     */   }
/*     */   
/*     */   public String getElementName() {
/*  61 */     return getW3CElement().getNodeName();
/*     */   }
/*     */   
/*     */   public String getContent() {
/*  65 */     return getW3CElement().getTextContent();
/*     */   }
/*     */   
/*     */   public DOMElement<CHILD, PARENT> setContent(String content) {
/*  69 */     getW3CElement().setTextContent(content);
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public String getAttribute(String attribute) {
/*  74 */     String v = getW3CElement().getAttribute(attribute);
/*  75 */     return (v.length() > 0) ? v : null;
/*     */   }
/*     */   
/*     */   public DOMElement setAttribute(String attribute, String value) {
/*  79 */     getW3CElement().setAttribute(attribute, value);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public PARENT getParent() {
/*  84 */     return this.PARENT_BUILDER.build((Element)getW3CElement().getParentNode());
/*     */   }
/*     */   
/*     */   public CHILD[] getChildren() {
/*  88 */     NodeList nodes = getW3CElement().getChildNodes();
/*  89 */     List<CHILD> children = new ArrayList<CHILD>();
/*  90 */     for (int i = 0; i < nodes.getLength(); i++) {
/*  91 */       Node node = nodes.item(i);
/*  92 */       if (node.getNodeType() == 1) {
/*  93 */         children.add(this.CHILD_BUILDER.build((Element)node));
/*     */       }
/*     */     } 
/*  96 */     return (CHILD[])children.<DOMElement>toArray((DOMElement[])this.CHILD_BUILDER.newChildrenArray(children.size()));
/*     */   }
/*     */   
/*     */   public CHILD[] getChildren(String name) {
/* 100 */     Collection<CHILD> list = getXPathChildElements(this.CHILD_BUILDER, prefix(name));
/* 101 */     return (CHILD[])list.<DOMElement>toArray((DOMElement[])this.CHILD_BUILDER.newChildrenArray(list.size()));
/*     */   }
/*     */   
/*     */   public CHILD getRequiredChild(String name) throws ParserException {
/* 105 */     CHILD[] children = getChildren(name);
/* 106 */     if (children.length != 1) {
/* 107 */       throw new ParserException("Required single child element of '" + getElementName() + "' not found: " + name);
/*     */     }
/* 109 */     return children[0];
/*     */   }
/*     */   
/*     */   public CHILD[] findChildren(String name) {
/* 113 */     Collection<CHILD> list = getXPathChildElements(this.CHILD_BUILDER, "descendant::" + prefix(name));
/* 114 */     return (CHILD[])list.<DOMElement>toArray((DOMElement[])this.CHILD_BUILDER.newChildrenArray(list.size()));
/*     */   }
/*     */   
/*     */   public CHILD findChildWithIdentifier(String id) {
/* 118 */     Collection<CHILD> list = getXPathChildElements(this.CHILD_BUILDER, "descendant::" + prefix("*") + "[@id=\"" + id + "\"]");
/* 119 */     if (list.size() == 1) return (CHILD)list.iterator().next(); 
/* 120 */     return null;
/*     */   }
/*     */   
/*     */   public CHILD getFirstChild(String name) {
/* 124 */     return getXPathChildElement(this.CHILD_BUILDER, prefix(name) + "[1]");
/*     */   }
/*     */   
/*     */   public CHILD createChild(String name) {
/* 128 */     return createChild(name, null);
/*     */   }
/*     */   
/*     */   public CHILD createChild(String name, String namespaceURI) {
/* 132 */     CHILD child = this.CHILD_BUILDER.build((namespaceURI == null) ? getW3CElement().getOwnerDocument().createElement(name) : getW3CElement().getOwnerDocument().createElementNS(namespaceURI, name));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     getW3CElement().appendChild(child.getW3CElement());
/* 138 */     return child;
/*     */   }
/*     */   
/*     */   public CHILD appendChild(CHILD el, boolean copy) {
/* 142 */     el = adoptOrImport(getW3CElement().getOwnerDocument(), el, copy);
/* 143 */     getW3CElement().appendChild(el.getW3CElement());
/* 144 */     return el;
/*     */   }
/*     */   
/*     */   public CHILD replaceChild(CHILD original, CHILD replacement, boolean copy) {
/* 148 */     replacement = adoptOrImport(getW3CElement().getOwnerDocument(), replacement, copy);
/* 149 */     getW3CElement().replaceChild(replacement.getW3CElement(), original.getW3CElement());
/* 150 */     return replacement;
/*     */   }
/*     */   
/*     */   public void replaceEqualChild(DOMElement source, String identifier) {
/* 154 */     DOMElement original = (DOMElement)findChildWithIdentifier(identifier);
/* 155 */     DOMElement replacement = (DOMElement)source.findChildWithIdentifier(identifier);
/* 156 */     original.getParent().replaceChild(original, replacement, true);
/*     */   }
/*     */   
/*     */   public void removeChild(CHILD el) {
/* 160 */     getW3CElement().removeChild(el.getW3CElement());
/*     */   }
/*     */   
/*     */   public void removeChildren() {
/* 164 */     NodeList children = getW3CElement().getChildNodes();
/* 165 */     for (int i = 0; i < children.getLength(); i++) {
/* 166 */       Node child = children.item(i);
/* 167 */       getW3CElement().removeChild(child);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected CHILD adoptOrImport(Document document, CHILD child, boolean copy) {
/* 172 */     if (document != null) {
/* 173 */       if (copy) {
/* 174 */         child = this.CHILD_BUILDER.build((Element)document.importNode(child.getW3CElement(), true));
/*     */       }
/*     */       else {
/*     */         
/* 178 */         child = this.CHILD_BUILDER.build((Element)document.adoptNode(child.getW3CElement()));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 183 */     return child;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Builder<PARENT> createParentBuilder(DOMElement paramDOMElement);
/*     */   
/*     */   protected abstract ArrayBuilder<CHILD> createChildBuilder(DOMElement paramDOMElement);
/*     */   
/*     */   public String toSimpleXMLString() {
/* 192 */     StringBuilder sb = new StringBuilder();
/* 193 */     sb.append("<").append(getElementName());
/* 194 */     NamedNodeMap map = getW3CElement().getAttributes();
/* 195 */     for (int i = 0; i < map.getLength(); i++) {
/* 196 */       Node attr = map.item(i);
/* 197 */       sb.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getTextContent()).append("\"");
/*     */     } 
/*     */     
/* 200 */     if (getContent().length() > 0) {
/* 201 */       sb.append(">").append(getContent()).append("</").append(getElementName()).append(">");
/*     */     } else {
/* 203 */       sb.append("/>");
/*     */     } 
/* 205 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 210 */     return "(" + getClass().getSimpleName() + ") " + ((getW3CElement() == null) ? "UNBOUND" : getElementName());
/*     */   }
/*     */ 
/*     */   
/*     */   public XPath getXpath() {
/* 215 */     return this.xpath;
/*     */   }
/*     */   
/*     */   protected String prefix(String localName) {
/* 219 */     return localName;
/*     */   }
/*     */   
/*     */   public Collection<PARENT> getXPathParentElements(Builder<CHILD> builder, String expr) {
/* 223 */     return getXPathElements(builder, expr);
/*     */   }
/*     */   
/*     */   public Collection<CHILD> getXPathChildElements(Builder<CHILD> builder, String expr) {
/* 227 */     return getXPathElements(builder, expr);
/*     */   }
/*     */   
/*     */   public PARENT getXPathParentElement(Builder<PARENT> builder, String expr) {
/* 231 */     Node node = (Node)getXPathResult(getW3CElement(), expr, XPathConstants.NODE);
/* 232 */     return (node != null && node.getNodeType() == 1) ? builder.build((Element)node) : null;
/*     */   }
/*     */   
/*     */   public CHILD getXPathChildElement(Builder<CHILD> builder, String expr) {
/* 236 */     Node node = (Node)getXPathResult(getW3CElement(), expr, XPathConstants.NODE);
/* 237 */     return (node != null && node.getNodeType() == 1) ? builder.build((Element)node) : null;
/*     */   }
/*     */   
/*     */   public Collection getXPathElements(Builder<DOMElement> builder, String expr) {
/* 241 */     Collection<DOMElement> col = new ArrayList();
/* 242 */     NodeList result = (NodeList)getXPathResult(getW3CElement(), expr, XPathConstants.NODESET);
/* 243 */     for (int i = 0; i < result.getLength(); i++) {
/* 244 */       DOMElement e = builder.build((Element)result.item(i));
/* 245 */       col.add(e);
/*     */     } 
/* 247 */     return col;
/*     */   }
/*     */   
/*     */   public String getXPathString(XPath xpath, String expr) {
/* 251 */     return getXPathResult(getW3CElement(), expr, null).toString();
/*     */   }
/*     */   
/*     */   public Object getXPathResult(String expr, QName result) {
/* 255 */     return getXPathResult(getW3CElement(), expr, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getXPathResult(Node context, String expr, QName result) {
/*     */     try {
/* 261 */       if (result == null) {
/* 262 */         return this.xpath.evaluate(expr, context);
/*     */       }
/* 264 */       return this.xpath.evaluate(expr, context, result);
/* 265 */     } catch (Exception ex) {
/* 266 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract class Builder<T extends DOMElement>
/*     */   {
/*     */     public DOMElement element;
/*     */     
/*     */     protected Builder(DOMElement element) {
/* 275 */       this.element = element;
/*     */     }
/*     */     
/*     */     public abstract T build(Element param1Element);
/*     */     
/*     */     public T firstChildOrNull(String elementName) {
/* 281 */       DOMElement el = (DOMElement)this.element.getFirstChild(elementName);
/* 282 */       return (el != null) ? build(el.getW3CElement()) : null;
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract class ArrayBuilder<T extends DOMElement>
/*     */     extends Builder<T>
/*     */   {
/*     */     protected ArrayBuilder(DOMElement element) {
/* 290 */       super(element);
/*     */     }
/*     */     
/*     */     public abstract T[] newChildrenArray(int param1Int);
/*     */     
/*     */     public T[] getChildElements() {
/* 296 */       return buildArray((DOMElement[])this.element.getChildren());
/*     */     }
/*     */     
/*     */     public T[] getChildElements(String elementName) {
/* 300 */       return buildArray((DOMElement[])this.element.getChildren(elementName));
/*     */     }
/*     */     
/*     */     protected T[] buildArray(DOMElement[] list) {
/* 304 */       T[] children = newChildrenArray(list.length);
/* 305 */       for (int i = 0; i < children.length; i++) {
/* 306 */         children[i] = build(list[i].getW3CElement());
/*     */       }
/* 308 */       return children;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xml\DOMElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */