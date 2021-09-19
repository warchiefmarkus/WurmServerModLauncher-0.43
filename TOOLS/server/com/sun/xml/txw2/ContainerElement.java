/*     */ package com.sun.xml.txw2;
/*     */ 
/*     */ import com.sun.xml.txw2.annotation.XmlAttribute;
/*     */ import com.sun.xml.txw2.annotation.XmlCDATA;
/*     */ import com.sun.xml.txw2.annotation.XmlElement;
/*     */ import com.sun.xml.txw2.annotation.XmlNamespace;
/*     */ import com.sun.xml.txw2.annotation.XmlValue;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ContainerElement
/*     */   implements InvocationHandler, TypedXmlWriter
/*     */ {
/*     */   final Document document;
/*     */   StartTag startTag;
/*  50 */   final EndTag endTag = new EndTag();
/*     */ 
/*     */ 
/*     */   
/*     */   private final String nsUri;
/*     */ 
/*     */ 
/*     */   
/*     */   private Content tail;
/*     */ 
/*     */ 
/*     */   
/*     */   private ContainerElement prevOpen;
/*     */ 
/*     */ 
/*     */   
/*     */   private ContainerElement nextOpen;
/*     */ 
/*     */   
/*     */   private final ContainerElement parent;
/*     */ 
/*     */   
/*     */   private ContainerElement lastOpenChild;
/*     */ 
/*     */   
/*     */   private boolean blocked;
/*     */ 
/*     */ 
/*     */   
/*     */   public ContainerElement(Document document, ContainerElement parent, String nsUri, String localName) {
/*  80 */     this.parent = parent;
/*  81 */     this.document = document;
/*  82 */     this.nsUri = nsUri;
/*  83 */     this.startTag = new StartTag(this, nsUri, localName);
/*  84 */     this.tail = this.startTag;
/*     */     
/*  86 */     if (isRoot())
/*  87 */       document.setFirstContent(this.startTag); 
/*     */   }
/*     */   
/*     */   private boolean isRoot() {
/*  91 */     return (this.parent == null);
/*     */   }
/*     */   
/*     */   private boolean isCommitted() {
/*  95 */     return (this.tail == null);
/*     */   }
/*     */   
/*     */   public Document getDocument() {
/*  99 */     return this.document;
/*     */   }
/*     */   
/*     */   boolean isBlocked() {
/* 103 */     return (this.blocked && !isCommitted());
/*     */   }
/*     */   
/*     */   public void block() {
/* 107 */     this.blocked = true;
/*     */   }
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 111 */     if (method.getDeclaringClass() == TypedXmlWriter.class || method.getDeclaringClass() == Object.class) {
/*     */       
/*     */       try {
/* 114 */         return method.invoke(this, args);
/* 115 */       } catch (InvocationTargetException e) {
/* 116 */         throw e.getTargetException();
/*     */       } 
/*     */     }
/*     */     
/* 120 */     XmlAttribute xa = method.<XmlAttribute>getAnnotation(XmlAttribute.class);
/* 121 */     XmlValue xv = method.<XmlValue>getAnnotation(XmlValue.class);
/* 122 */     XmlElement xe = method.<XmlElement>getAnnotation(XmlElement.class);
/*     */ 
/*     */     
/* 125 */     if (xa != null) {
/* 126 */       if (xv != null || xe != null) {
/* 127 */         throw new IllegalAnnotationException(method.toString());
/*     */       }
/* 129 */       addAttribute(xa, method, args);
/* 130 */       return proxy;
/*     */     } 
/* 132 */     if (xv != null) {
/* 133 */       if (xe != null) {
/* 134 */         throw new IllegalAnnotationException(method.toString());
/*     */       }
/* 136 */       _pcdata(args);
/* 137 */       return proxy;
/*     */     } 
/*     */     
/* 140 */     return addElement(xe, method, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addAttribute(XmlAttribute xa, Method method, Object[] args) {
/* 147 */     assert xa != null;
/*     */     
/* 149 */     checkStartTag();
/*     */     
/* 151 */     String localName = xa.value();
/* 152 */     if (xa.value().length() == 0) {
/* 153 */       localName = method.getName();
/*     */     }
/* 155 */     _attribute(xa.ns(), localName, args);
/*     */   }
/*     */   
/*     */   private void checkStartTag() {
/* 159 */     if (this.startTag == null) {
/* 160 */       throw new IllegalStateException("start tag has already been written");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object addElement(XmlElement e, Method method, Object[] args) {
/* 167 */     Class<?> rt = method.getReturnType();
/*     */ 
/*     */     
/* 170 */     String nsUri = "##default";
/* 171 */     String localName = method.getName();
/*     */     
/* 173 */     if (e != null) {
/*     */       
/* 175 */       if (e.value().length() != 0)
/* 176 */         localName = e.value(); 
/* 177 */       nsUri = e.ns();
/*     */     } 
/*     */     
/* 180 */     if (nsUri.equals("##default")) {
/*     */       
/* 182 */       Class<?> c = method.getDeclaringClass();
/* 183 */       XmlElement ce = c.<XmlElement>getAnnotation(XmlElement.class);
/* 184 */       if (ce != null) {
/* 185 */         nsUri = ce.ns();
/*     */       }
/*     */       
/* 188 */       if (nsUri.equals("##default"))
/*     */       {
/* 190 */         nsUri = getNamespace(c.getPackage());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 195 */     if (rt == void.class) {
/*     */ 
/*     */       
/* 198 */       boolean isCDATA = (method.getAnnotation(XmlCDATA.class) != null);
/*     */       
/* 200 */       StartTag st = new StartTag(this.document, nsUri, localName);
/* 201 */       addChild(st);
/* 202 */       for (Object arg : args) {
/*     */         Text text;
/* 204 */         if (isCDATA) { text = new Cdata(this.document, st, arg); }
/* 205 */         else { text = new Pcdata(this.document, st, arg); }
/* 206 */          addChild(text);
/*     */       } 
/* 208 */       addChild(new EndTag());
/* 209 */       return null;
/*     */     } 
/* 211 */     if (TypedXmlWriter.class.isAssignableFrom(rt))
/*     */     {
/* 213 */       return _element(nsUri, localName, rt);
/*     */     }
/*     */     
/* 216 */     throw new IllegalSignatureException("Illegal return type: " + rt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getNamespace(Package pkg) {
/*     */     String nsUri;
/* 223 */     if (pkg == null) return "";
/*     */ 
/*     */     
/* 226 */     XmlNamespace ns = pkg.<XmlNamespace>getAnnotation(XmlNamespace.class);
/* 227 */     if (ns != null) {
/* 228 */       nsUri = ns.value();
/*     */     } else {
/* 230 */       nsUri = "";
/* 231 */     }  return nsUri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addChild(Content child) {
/* 238 */     this.tail.setNext(this.document, child);
/* 239 */     this.tail = child;
/*     */   }
/*     */   
/*     */   public void commit() {
/* 243 */     commit(true);
/*     */   }
/*     */   
/*     */   public void commit(boolean includingAllPredecessors) {
/* 247 */     _commit(includingAllPredecessors);
/* 248 */     this.document.flush();
/*     */   }
/*     */   
/*     */   private void _commit(boolean includingAllPredecessors) {
/* 252 */     if (isCommitted())
/*     */       return; 
/* 254 */     addChild(this.endTag);
/* 255 */     if (isRoot())
/* 256 */       addChild(new EndDocument()); 
/* 257 */     this.tail = null;
/*     */ 
/*     */     
/* 260 */     if (includingAllPredecessors) {
/* 261 */       for (ContainerElement e = this; e != null; e = e.parent) {
/* 262 */         while (e.prevOpen != null) {
/* 263 */           e.prevOpen._commit(false);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 270 */     while (this.lastOpenChild != null) {
/* 271 */       this.lastOpenChild._commit(false);
/*     */     }
/*     */     
/* 274 */     if (this.parent != null) {
/* 275 */       if (this.parent.lastOpenChild == this) {
/* 276 */         assert this.nextOpen == null : "this must be the last one";
/* 277 */         this.parent.lastOpenChild = this.prevOpen;
/*     */       } else {
/* 279 */         assert this.nextOpen.prevOpen == this;
/* 280 */         this.nextOpen.prevOpen = this.prevOpen;
/*     */       } 
/* 282 */       if (this.prevOpen != null) {
/* 283 */         assert this.prevOpen.nextOpen == this;
/* 284 */         this.prevOpen.nextOpen = this.nextOpen;
/*     */       } 
/*     */     } 
/*     */     
/* 288 */     this.nextOpen = null;
/* 289 */     this.prevOpen = null;
/*     */   }
/*     */   
/*     */   public void _attribute(String localName, Object value) {
/* 293 */     _attribute("", localName, value);
/*     */   }
/*     */   
/*     */   public void _attribute(String nsUri, String localName, Object value) {
/* 297 */     checkStartTag();
/* 298 */     this.startTag.addAttribute(nsUri, localName, value);
/*     */   }
/*     */   
/*     */   public void _attribute(QName attributeName, Object value) {
/* 302 */     _attribute(attributeName.getNamespaceURI(), attributeName.getLocalPart(), value);
/*     */   }
/*     */   
/*     */   public void _namespace(String uri) {
/* 306 */     _namespace(uri, false);
/*     */   }
/*     */   
/*     */   public void _namespace(String uri, String prefix) {
/* 310 */     if (prefix == null)
/* 311 */       throw new IllegalArgumentException(); 
/* 312 */     checkStartTag();
/* 313 */     this.startTag.addNamespaceDecl(uri, prefix, false);
/*     */   }
/*     */   
/*     */   public void _namespace(String uri, boolean requirePrefix) {
/* 317 */     checkStartTag();
/* 318 */     this.startTag.addNamespaceDecl(uri, null, requirePrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void _pcdata(Object value) {
/* 324 */     addChild(new Pcdata(this.document, this.startTag, value));
/*     */   }
/*     */   
/*     */   public void _cdata(Object value) {
/* 328 */     addChild(new Cdata(this.document, this.startTag, value));
/*     */   }
/*     */   
/*     */   public void _comment(Object value) throws UnsupportedOperationException {
/* 332 */     addChild(new Comment(this.document, this.startTag, value));
/*     */   }
/*     */   
/*     */   public <T extends TypedXmlWriter> T _element(String localName, Class<T> contentModel) {
/* 336 */     return _element(this.nsUri, localName, contentModel);
/*     */   }
/*     */   
/*     */   public <T extends TypedXmlWriter> T _element(QName tagName, Class<T> contentModel) {
/* 340 */     return _element(tagName.getNamespaceURI(), tagName.getLocalPart(), contentModel);
/*     */   }
/*     */   
/*     */   public <T extends TypedXmlWriter> T _element(Class<T> contentModel) {
/* 344 */     return _element(TXW.getTagName(contentModel), contentModel);
/*     */   }
/*     */   
/*     */   public <T extends TypedXmlWriter> T _cast(Class<T> facadeType) {
/* 348 */     return facadeType.cast(Proxy.newProxyInstance(facadeType.getClassLoader(), new Class[] { facadeType }, this));
/*     */   }
/*     */   
/*     */   public <T extends TypedXmlWriter> T _element(String nsUri, String localName, Class<T> contentModel) {
/* 352 */     ContainerElement child = new ContainerElement(this.document, this, nsUri, localName);
/* 353 */     addChild(child.startTag);
/* 354 */     this.tail = child.endTag;
/*     */ 
/*     */     
/* 357 */     if (this.lastOpenChild != null) {
/* 358 */       assert this.lastOpenChild.parent == this;
/*     */       
/* 360 */       assert child.prevOpen == null;
/* 361 */       assert child.nextOpen == null;
/* 362 */       child.prevOpen = this.lastOpenChild;
/* 363 */       assert this.lastOpenChild.nextOpen == null;
/* 364 */       this.lastOpenChild.nextOpen = child;
/*     */     } 
/*     */     
/* 367 */     this.lastOpenChild = child;
/*     */     
/* 369 */     return child._cast(contentModel);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\ContainerElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */