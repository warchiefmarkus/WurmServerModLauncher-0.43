/*     */ package org.seamless.xhtml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.xpath.XPath;
/*     */ import org.seamless.xml.DOMElement;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XHTMLElement
/*     */   extends DOMElement<XHTMLElement, XHTMLElement>
/*     */ {
/*     */   public static final String XPATH_PREFIX = "h";
/*     */   
/*     */   public XHTMLElement(XPath xpath, Element element) {
/*  32 */     super(xpath, element);
/*     */   }
/*     */ 
/*     */   
/*     */   protected DOMElement<XHTMLElement, XHTMLElement>.Builder<XHTMLElement> createParentBuilder(DOMElement el) {
/*  37 */     return new DOMElement<XHTMLElement, XHTMLElement>.Builder<XHTMLElement>(el)
/*     */       {
/*     */         public XHTMLElement build(Element element) {
/*  40 */           return new XHTMLElement(XHTMLElement.this.getXpath(), element);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   protected DOMElement<XHTMLElement, XHTMLElement>.ArrayBuilder<XHTMLElement> createChildBuilder(DOMElement el) {
/*  47 */     return new DOMElement<XHTMLElement, XHTMLElement>.ArrayBuilder<XHTMLElement>(el)
/*     */       {
/*     */         public XHTMLElement[] newChildrenArray(int length) {
/*  50 */           return new XHTMLElement[length];
/*     */         }
/*     */ 
/*     */         
/*     */         public XHTMLElement build(Element element) {
/*  55 */           return new XHTMLElement(XHTMLElement.this.getXpath(), element);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   protected String prefix(String localName) {
/*  62 */     return "h:" + localName;
/*     */   }
/*     */   
/*     */   public XHTML.ELEMENT getConstant() {
/*  66 */     return XHTML.ELEMENT.valueOf(getElementName());
/*     */   }
/*     */   
/*     */   public XHTMLElement[] getChildren(XHTML.ELEMENT el) {
/*  70 */     return (XHTMLElement[])getChildren(el.name());
/*     */   }
/*     */   
/*     */   public XHTMLElement getFirstChild(XHTML.ELEMENT el) {
/*  74 */     return (XHTMLElement)getFirstChild(el.name());
/*     */   }
/*     */   
/*     */   public XHTMLElement[] findChildren(XHTML.ELEMENT el) {
/*  78 */     return (XHTMLElement[])findChildren(el.name());
/*     */   }
/*     */   
/*     */   public XHTMLElement createChild(XHTML.ELEMENT el) {
/*  82 */     return (XHTMLElement)createChild(el.name(), "http://www.w3.org/1999/xhtml");
/*     */   }
/*     */   
/*     */   public String getAttribute(XHTML.ATTR attribute) {
/*  86 */     return getAttribute(attribute.name());
/*     */   }
/*     */   
/*     */   public XHTMLElement setAttribute(XHTML.ATTR attribute, String value) {
/*  90 */     super.setAttribute(attribute.name(), value);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  95 */     return getAttribute(XHTML.ATTR.id);
/*     */   }
/*     */   
/*     */   public XHTMLElement setId(String id) {
/*  99 */     setAttribute(XHTML.ATTR.id, id);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public String getTitle() {
/* 104 */     return getAttribute(XHTML.ATTR.title);
/*     */   }
/*     */   
/*     */   public XHTMLElement setTitle(String title) {
/* 108 */     setAttribute(XHTML.ATTR.title, title);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public XHTMLElement setClasses(String classes) {
/* 113 */     setAttribute("class", classes);
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public XHTMLElement setClasses(String[] classes) {
/* 118 */     StringBuilder sb = new StringBuilder();
/* 119 */     for (int i = 0; i < classes.length; i++) {
/* 120 */       sb.append(classes[i]);
/* 121 */       if (i != classes.length - 1) sb.append(" "); 
/*     */     } 
/* 123 */     setAttribute("class", sb.toString());
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public String[] getClasses() {
/* 128 */     String v = getAttribute("class");
/* 129 */     if (v == null) return new String[0]; 
/* 130 */     return v.split(" ");
/*     */   }
/*     */   
/*     */   public Option[] getOptions() {
/* 134 */     return Option.fromString(getAttribute(XHTML.ATTR.style));
/*     */   }
/*     */   
/*     */   public Option getOption(String key) {
/* 138 */     for (Option option : getOptions()) {
/* 139 */       if (option.getKey().equals(key)) return option; 
/*     */     } 
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   public Anchor[] findAllAnchors() {
/* 145 */     return findAllAnchors((String)null, (String)null);
/*     */   }
/*     */   
/*     */   public Anchor[] findAllAnchors(String requiredScheme) {
/* 149 */     return findAllAnchors(requiredScheme, (String)null);
/*     */   }
/*     */   
/*     */   public Anchor[] findAllAnchors(String requiredScheme, String requiredClass) {
/* 153 */     XHTMLElement[] elements = findChildrenWithClass(XHTML.ELEMENT.a, requiredClass);
/* 154 */     List<Anchor> anchors = new ArrayList<Anchor>(elements.length);
/* 155 */     for (XHTMLElement element : elements) {
/* 156 */       String href = element.getAttribute(XHTML.ATTR.href);
/* 157 */       if (requiredScheme == null || (href != null && href.startsWith(requiredScheme))) {
/* 158 */         anchors.add(new Anchor(getXpath(), element.getW3CElement()));
/*     */       }
/*     */     } 
/* 161 */     return anchors.<Anchor>toArray(new Anchor[anchors.size()]);
/*     */   }
/*     */   
/*     */   public XHTMLElement[] findChildrenWithClass(XHTML.ELEMENT el, String clazz) {
/* 165 */     List<XHTMLElement> list = new ArrayList<XHTMLElement>();
/* 166 */     XHTMLElement[] children = findChildren(el);
/* 167 */     for (XHTMLElement child : children) {
/* 168 */       if (clazz == null) {
/* 169 */         list.add(child);
/*     */       } else {
/* 171 */         for (String c : child.getClasses()) {
/* 172 */           if (c.matches(clazz)) {
/* 173 */             list.add(child);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 179 */     return (XHTMLElement[])list.<DOMElement>toArray(this.CHILD_BUILDER.newChildrenArray(list.size()));
/*     */   }
/*     */ 
/*     */   
/*     */   public XHTMLElement setContent(String content) {
/* 184 */     super.setContent(content);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public XHTMLElement setAttribute(String attribute, String value) {
/* 190 */     super.setAttribute(attribute, value);
/* 191 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\xhtml\XHTMLElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */