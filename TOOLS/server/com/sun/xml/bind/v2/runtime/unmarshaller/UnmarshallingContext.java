/*      */ package com.sun.xml.bind.v2.runtime.unmarshaller;
/*      */ 
/*      */ import com.sun.istack.NotNull;
/*      */ import com.sun.istack.Nullable;
/*      */ import com.sun.istack.SAXParseException2;
/*      */ import com.sun.xml.bind.IDResolver;
/*      */ import com.sun.xml.bind.api.AccessorException;
/*      */ import com.sun.xml.bind.api.ClassResolver;
/*      */ import com.sun.xml.bind.unmarshaller.InfosetScanner;
/*      */ import com.sun.xml.bind.v2.ClassFactory;
/*      */ import com.sun.xml.bind.v2.runtime.AssociationMap;
/*      */ import com.sun.xml.bind.v2.runtime.Coordinator;
/*      */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*      */ import com.sun.xml.bind.v2.runtime.JaxBeanInfo;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.Callable;
/*      */ import javax.xml.bind.JAXBElement;
/*      */ import javax.xml.bind.UnmarshalException;
/*      */ import javax.xml.bind.ValidationEvent;
/*      */ import javax.xml.bind.ValidationEventHandler;
/*      */ import javax.xml.bind.ValidationEventLocator;
/*      */ import javax.xml.bind.helpers.ValidationEventImpl;
/*      */ import javax.xml.namespace.NamespaceContext;
/*      */ import javax.xml.namespace.QName;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.helpers.LocatorImpl;
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
/*      */ public final class UnmarshallingContext
/*      */   extends Coordinator
/*      */   implements NamespaceContext, ValidationEventHandler, ErrorHandler, XmlVisitor, XmlVisitor.TextPredictor
/*      */ {
/*      */   private final State root;
/*      */   private State current;
/*      */   private static final LocatorEx DUMMY_INSTANCE;
/*      */   
/*      */   static {
/*  104 */     LocatorImpl loc = new LocatorImpl();
/*  105 */     loc.setPublicId(null);
/*  106 */     loc.setSystemId(null);
/*  107 */     loc.setLineNumber(-1);
/*  108 */     loc.setColumnNumber(-1);
/*  109 */     DUMMY_INSTANCE = new LocatorExWrapper(loc);
/*      */   }
/*      */   @NotNull
/*  112 */   private LocatorEx locator = DUMMY_INSTANCE;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object result;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private JaxBeanInfo expectedType;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IDResolver idResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isUnmarshalInProgress = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean aborted = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final UnmarshallerImpl parent;
/*      */ 
/*      */ 
/*      */   
/*      */   private final AssociationMap assoc;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isInplaceMode;
/*      */ 
/*      */ 
/*      */   
/*      */   private InfosetScanner scanner;
/*      */ 
/*      */ 
/*      */   
/*      */   private Object currentElement;
/*      */ 
/*      */ 
/*      */   
/*      */   private NamespaceContext environmentNamespaceContext;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public ClassResolver classResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   private final Map<Class, Factory> factories;
/*      */ 
/*      */ 
/*      */   
/*      */   private Patcher[] patchers;
/*      */ 
/*      */ 
/*      */   
/*      */   private int patchersLen;
/*      */ 
/*      */ 
/*      */   
/*      */   private String[] nsBind;
/*      */ 
/*      */ 
/*      */   
/*      */   private int nsLen;
/*      */ 
/*      */ 
/*      */   
/*      */   private Scope[] scopes;
/*      */ 
/*      */ 
/*      */   
/*      */   private int scopeTop;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final class State
/*      */   {
/*      */     public Loader loader;
/*      */ 
/*      */ 
/*      */     
/*      */     public Receiver receiver;
/*      */ 
/*      */ 
/*      */     
/*      */     public Intercepter intercepter;
/*      */ 
/*      */ 
/*      */     
/*      */     public Object target;
/*      */ 
/*      */ 
/*      */     
/*      */     public Object backup;
/*      */ 
/*      */ 
/*      */     
/*      */     private int numNsDecl;
/*      */ 
/*      */ 
/*      */     
/*      */     public String elementDefaultValue;
/*      */ 
/*      */ 
/*      */     
/*      */     public final State prev;
/*      */ 
/*      */ 
/*      */     
/*      */     private State next;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public UnmarshallingContext getContext() {
/*  240 */       return UnmarshallingContext.this;
/*      */     }
/*      */     
/*      */     private State(State prev) {
/*  244 */       this.prev = prev;
/*  245 */       if (prev != null)
/*  246 */         prev.next = this; 
/*      */     }
/*      */     
/*      */     private void push() {
/*  250 */       if (this.next == null)
/*  251 */         UnmarshallingContext.this.allocateMoreStates(); 
/*  252 */       State n = this.next;
/*  253 */       n.numNsDecl = UnmarshallingContext.this.nsLen;
/*  254 */       UnmarshallingContext.this.current = n;
/*      */     }
/*      */     
/*      */     private void pop() {
/*  258 */       assert this.prev != null;
/*  259 */       this.loader = null;
/*  260 */       this.receiver = null;
/*  261 */       this.intercepter = null;
/*  262 */       this.elementDefaultValue = null;
/*  263 */       this.target = null;
/*  264 */       UnmarshallingContext.this.current = this.prev;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Factory
/*      */   {
/*      */     private final Object factorInstance;
/*      */     
/*      */     private final Method method;
/*      */     
/*      */     public Factory(Object factorInstance, Method method) {
/*  276 */       this.factorInstance = factorInstance;
/*  277 */       this.method = method;
/*      */     }
/*      */     
/*      */     public Object createInstance() throws SAXException {
/*      */       try {
/*  282 */         return this.method.invoke(this.factorInstance, new Object[0]);
/*  283 */       } catch (IllegalAccessException e) {
/*  284 */         UnmarshallingContext.getInstance().handleError(e, false);
/*  285 */       } catch (InvocationTargetException e) {
/*  286 */         UnmarshallingContext.getInstance().handleError(e, false);
/*      */       } 
/*  288 */       return null;
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
/*      */   public void reset(InfosetScanner scanner, boolean isInplaceMode, JaxBeanInfo expectedType, IDResolver idResolver) {
/*  308 */     this.scanner = scanner;
/*  309 */     this.isInplaceMode = isInplaceMode;
/*  310 */     this.expectedType = expectedType;
/*  311 */     this.idResolver = idResolver;
/*      */   }
/*      */   
/*      */   public JAXBContextImpl getJAXBContext() {
/*  315 */     return this.parent.context;
/*      */   }
/*      */   
/*      */   public State getCurrentState() {
/*  319 */     return this.current;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Loader selectRootLoader(State state, TagName tag) throws SAXException {
/*      */     try {
/*  331 */       Loader l = getJAXBContext().selectRootLoader(state, tag);
/*  332 */       if (l != null) return l;
/*      */       
/*  334 */       if (this.classResolver != null) {
/*  335 */         Class<?> clazz = this.classResolver.resolveElementName(tag.uri, tag.local);
/*  336 */         if (clazz != null) {
/*  337 */           JAXBContextImpl enhanced = getJAXBContext().createAugmented(clazz);
/*  338 */           JaxBeanInfo<?> bi = enhanced.getBeanInfo(clazz);
/*  339 */           return bi.getLoader(enhanced, true);
/*      */         } 
/*      */       } 
/*  342 */     } catch (RuntimeException e) {
/*  343 */       throw e;
/*  344 */     } catch (Exception e) {
/*  345 */       handleError(e);
/*      */     } 
/*      */     
/*  348 */     return null;
/*      */   } public void setFactories(Object factoryInstances) { this.factories.clear(); if (factoryInstances == null)
/*      */       return;  if (factoryInstances instanceof Object[]) { for (Object factory : (Object[])factoryInstances)
/*      */         addFactory(factory);  } else { addFactory(factoryInstances); }  } private void addFactory(Object factory) { for (Method m : factory.getClass().getMethods()) { if (m.getName().startsWith("create"))
/*      */         if ((m.getParameterTypes()).length <= 0) { Class<?> type = m.getReturnType(); this.factories.put(type, new Factory(factory, m)); }   }  }
/*      */   public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException { if (locator != null)
/*      */       this.locator = locator;  this.environmentNamespaceContext = nsContext; this.result = null; this.current = this.root; this.patchersLen = 0; this.aborted = false; this.isUnmarshalInProgress = true; this.nsLen = 0; setThreadAffinity(); if (this.expectedType != null) { this.root.loader = EXPECTED_TYPE_ROOT_LOADER; } else { this.root.loader = DEFAULT_ROOT_LOADER; }  this.idResolver.startDocument(this); }
/*      */   public void startElement(TagName tagName) throws SAXException { pushCoordinator(); try { _startElement(tagName); }
/*      */     finally { popCoordinator(); }
/*      */      }
/*      */   private void _startElement(TagName tagName) throws SAXException { if (this.assoc != null)
/*      */       this.currentElement = this.scanner.getCurrentElement();  Loader h = this.current.loader; this.current.push(); h.childElement(this.current, tagName); assert this.current.loader != null; this.current.loader.startElement(this.current, tagName); }
/*  360 */   private void allocateMoreStates() { assert this.current.next == null;
/*      */     
/*  362 */     State s = this.current;
/*  363 */     for (int i = 0; i < 8; i++)
/*  364 */       s = new State(s);  }
/*      */   public void text(CharSequence pcdata) throws SAXException { State cur = this.current; pushCoordinator(); try { if (cur.elementDefaultValue != null && pcdata.length() == 0) pcdata = cur.elementDefaultValue;  cur.loader.text(cur, pcdata); } finally { popCoordinator(); }  }
/*      */   public final void endElement(TagName tagName) throws SAXException { pushCoordinator(); try { State child = this.current; child.loader.leaveElement(child, tagName); Object target = child.target; Receiver recv = child.receiver; Intercepter intercepter = child.intercepter; child.pop(); if (intercepter != null) target = intercepter.intercept(this.current, target);  if (recv != null) recv.receive(this.current, target);  } finally { popCoordinator(); }  }
/*      */   public void endDocument() throws SAXException { runPatchers(); this.idResolver.endDocument(); this.isUnmarshalInProgress = false; this.currentElement = null; this.locator = DUMMY_INSTANCE; this.environmentNamespaceContext = null; assert this.root == this.current; resetThreadAffinity(); }
/*      */   @Deprecated public boolean expectText() { return this.current.loader.expectText; }
/*      */   @Deprecated public XmlVisitor.TextPredictor getPredictor() { return this; }
/*  370 */   public UnmarshallingContext getContext() { return this; } public Object getResult() throws UnmarshalException { if (this.isUnmarshalInProgress) throw new IllegalStateException();  if (!this.aborted) return this.result;  throw new UnmarshalException((String)null); } public Object createInstance(Class<?> clazz) throws SAXException { if (!this.factories.isEmpty()) { Factory factory = this.factories.get(clazz); if (factory != null) return factory.createInstance();  }  return ClassFactory.create(clazz); } public Object createInstance(JaxBeanInfo beanInfo) throws SAXException { if (!this.factories.isEmpty()) { Factory factory = this.factories.get(beanInfo.jaxbType); if (factory != null) return factory.createInstance();  }  try { return beanInfo.createInstance(this); } catch (IllegalAccessException e) { Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e, false); } catch (InvocationTargetException e) { Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e, false); } catch (InstantiationException e) { Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e, false); }  return null; } public UnmarshallingContext(UnmarshallerImpl _parent, AssociationMap assoc) { this.factories = (Map)new HashMap<Class<?>, Factory>();
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
/*  672 */     this.patchers = null;
/*  673 */     this.patchersLen = 0;
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
/*  757 */     this.nsBind = new String[16];
/*  758 */     this.nsLen = 0;
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
/*  898 */     this.scopes = new Scope[16];
/*      */ 
/*      */ 
/*      */     
/*  902 */     this.scopeTop = 0;
/*      */ 
/*      */     
/*  905 */     for (int i = 0; i < this.scopes.length; i++)
/*  906 */       this.scopes[i] = new Scope<Object, Object, Object, Object>(this);  this.parent = _parent;
/*      */     this.assoc = assoc;
/*      */     this.root = this.current = new State(null);
/*      */     allocateMoreStates(); }
/*      */   public void handleEvent(ValidationEvent event, boolean canRecover) throws SAXException { ValidationEventHandler eventHandler = this.parent.getEventHandler();
/*      */     boolean recover = eventHandler.handleEvent(event);
/*      */     if (!recover)
/*      */       this.aborted = true; 
/*      */     if (!canRecover || !recover)
/*      */       throw new SAXParseException2(event.getMessage(), this.locator, new UnmarshalException(event.getMessage(), event.getLinkedException()));  }
/*      */   public boolean handleEvent(ValidationEvent event) { try {
/*      */       boolean recover = this.parent.getEventHandler().handleEvent(event);
/*      */       if (!recover)
/*      */         this.aborted = true; 
/*      */       return recover;
/*      */     } catch (RuntimeException re) {
/*      */       return false;
/*      */     }  }
/*      */   public void handleError(Exception e) throws SAXException { handleError(e, true); }
/*  925 */   public void handleError(Exception e, boolean canRecover) throws SAXException { handleEvent((ValidationEvent)new ValidationEventImpl(1, e.getMessage(), this.locator.getLocation(), e), canRecover); } public void startScope(int frameSize) { this.scopeTop += frameSize;
/*      */ 
/*      */     
/*  928 */     if (this.scopeTop >= this.scopes.length) {
/*  929 */       Scope[] s = new Scope[Math.max(this.scopeTop + 1, this.scopes.length * 2)];
/*  930 */       System.arraycopy(this.scopes, 0, s, 0, this.scopes.length);
/*  931 */       for (int i = this.scopes.length; i < s.length; i++)
/*  932 */         s[i] = new Scope<Object, Object, Object, Object>(this); 
/*  933 */       this.scopes = s;
/*      */     }  } public void handleError(String msg) { handleEvent((ValidationEvent)new ValidationEventImpl(1, msg, this.locator.getLocation())); }
/*      */   protected ValidationEventLocator getLocation() { return this.locator.getLocation(); }
/*      */   public LocatorEx getLocator() { return this.locator; }
/*      */   public void errorUnresolvedIDREF(Object bean, String idref, LocatorEx loc) throws SAXException { handleEvent((ValidationEvent)new ValidationEventImpl(1, Messages.UNRESOLVED_IDREF.format(new Object[] { idref }, ), loc.getLocation()), true); }
/*      */   public void addPatcher(Patcher job) { if (this.patchers == null)
/*      */       this.patchers = new Patcher[32];  if (this.patchers.length == this.patchersLen) { Patcher[] buf = new Patcher[this.patchersLen * 2]; System.arraycopy(this.patchers, 0, buf, 0, this.patchersLen); this.patchers = buf; }
/*      */      this.patchers[this.patchersLen++] = job; }
/*      */   private void runPatchers() throws SAXException { if (this.patchers != null)
/*      */       for (int i = 0; i < this.patchersLen; i++) { this.patchers[i].run(); this.patchers[i] = null; }
/*      */         }
/*      */   public String addToIdTable(String id) throws SAXException { Object o = this.current.target; if (o == null)
/*      */       o = this.current.prev.target;  this.idResolver.bind(id, o); return id; }
/*      */   public Callable getObjectFromId(String id, Class targetType) throws SAXException { return this.idResolver.resolve(id, targetType); }
/*      */   public void startPrefixMapping(String prefix, String uri) { if (this.nsBind.length == this.nsLen) { String[] n = new String[this.nsLen * 2]; System.arraycopy(this.nsBind, 0, n, 0, this.nsLen); this.nsBind = n; }
/*      */      this.nsBind[this.nsLen++] = prefix; this.nsBind[this.nsLen++] = uri; }
/*  949 */   public void endScope(int frameSize) throws SAXException { try { for (; frameSize > 0; frameSize--, this.scopeTop--)
/*  950 */         this.scopes[this.scopeTop].finish();  }
/*  951 */     catch (AccessorException e)
/*  952 */     { handleError((Exception)e);
/*      */ 
/*      */ 
/*      */       
/*  956 */       for (; frameSize > 0; frameSize--)
/*  957 */         this.scopes[this.scopeTop--] = new Scope<Object, Object, Object, Object>(this);  }
/*      */      } public void endPrefixMapping(String prefix) { this.nsLen -= 2; }
/*      */   private String resolveNamespacePrefix(String prefix) { if (prefix.equals("xml"))
/*      */       return "http://www.w3.org/XML/1998/namespace";  for (int i = this.nsLen - 2; i >= 0; i -= 2) {
/*      */       if (prefix.equals(this.nsBind[i]))
/*      */         return this.nsBind[i + 1]; 
/*      */     } 
/*      */     if (this.environmentNamespaceContext != null)
/*      */       return this.environmentNamespaceContext.getNamespaceURI(prefix.intern()); 
/*      */     if (prefix.equals(""))
/*      */       return ""; 
/*      */     return null; }
/*      */   public String[] getNewlyDeclaredPrefixes() { return getPrefixList(this.current.prev.numNsDecl); }
/*      */   public String[] getAllDeclaredPrefixes() { return getPrefixList(0); }
/*  971 */   public Scope getScope(int offset) { return this.scopes[this.scopeTop - offset]; } private String[] getPrefixList(int startIndex) { int size = (this.current.numNsDecl - startIndex) / 2; String[] r = new String[size]; for (int i = 0; i < r.length; i++) r[i] = this.nsBind[startIndex + i * 2];  return r; }
/*      */   public Iterator<String> getPrefixes(String uri) { return Collections.<String>unmodifiableList(getAllPrefixesInList(uri)).iterator(); }
/*      */   private List<String> getAllPrefixesInList(String uri) { List<String> a = new ArrayList<String>(); if (uri == null) throw new IllegalArgumentException();  if (uri.equals("http://www.w3.org/XML/1998/namespace")) { a.add("xml"); return a; }  if (uri.equals("http://www.w3.org/2000/xmlns/")) { a.add("xmlns"); return a; }  for (int i = this.nsLen - 2; i >= 0; i -= 2) { if (uri.equals(this.nsBind[i + 1]) && getNamespaceURI(this.nsBind[i]).equals(this.nsBind[i + 1]))
/*      */         a.add(this.nsBind[i]);  }  return a; }
/*      */   public String getPrefix(String uri) { if (uri == null)
/*      */       throw new IllegalArgumentException();  if (uri.equals("http://www.w3.org/XML/1998/namespace"))
/*      */       return "xml";  if (uri.equals("http://www.w3.org/2000/xmlns/"))
/*      */       return "xmlns";  for (int i = this.nsLen - 2; i >= 0; i -= 2) { if (uri.equals(this.nsBind[i + 1]) && getNamespaceURI(this.nsBind[i]).equals(this.nsBind[i + 1]))
/*      */         return this.nsBind[i];  }  if (this.environmentNamespaceContext != null)
/*      */       return this.environmentNamespaceContext.getPrefix(uri);  return null; }
/*      */   public String getNamespaceURI(String prefix) { if (prefix == null)
/*      */       throw new IllegalArgumentException();  if (prefix.equals("xmlns"))
/*      */       return "http://www.w3.org/2000/xmlns/";  return resolveNamespacePrefix(prefix); }
/*  984 */   private static final Loader DEFAULT_ROOT_LOADER = new DefaultRootLoader();
/*  985 */   private static final Loader EXPECTED_TYPE_ROOT_LOADER = new ExpectedTypeRootLoader();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class DefaultRootLoader
/*      */     extends Loader
/*      */     implements Receiver
/*      */   {
/*      */     private DefaultRootLoader() {}
/*      */ 
/*      */     
/*      */     public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
/*  997 */       Loader loader = state.getContext().selectRootLoader(state, ea);
/*  998 */       if (loader != null) {
/*  999 */         state.loader = loader;
/* 1000 */         state.receiver = this;
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1006 */       JaxBeanInfo beanInfo = XsiTypeLoader.parseXsiType(state, ea, null);
/* 1007 */       if (beanInfo == null) {
/*      */         
/* 1009 */         reportUnexpectedChildElement(ea, false);
/*      */         
/*      */         return;
/*      */       } 
/* 1013 */       state.loader = beanInfo.getLoader(null, false);
/* 1014 */       state.prev.backup = new JAXBElement(ea.createQName(), Object.class, null);
/* 1015 */       state.receiver = this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<QName> getExpectedChildElements() {
/* 1020 */       return UnmarshallingContext.getInstance().getJAXBContext().getValidRootNames();
/*      */     }
/*      */     
/*      */     public void receive(UnmarshallingContext.State state, Object o) {
/* 1024 */       if (state.backup != null) {
/* 1025 */         ((JAXBElement)state.backup).setValue(o);
/* 1026 */         o = state.backup;
/*      */       } 
/* 1028 */       (state.getContext()).result = o;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ExpectedTypeRootLoader
/*      */     extends Loader
/*      */     implements Receiver
/*      */   {
/*      */     private ExpectedTypeRootLoader() {}
/*      */ 
/*      */     
/*      */     public void childElement(UnmarshallingContext.State state, TagName ea) {
/* 1042 */       UnmarshallingContext context = state.getContext();
/*      */ 
/*      */       
/* 1045 */       QName qn = new QName(ea.uri, ea.local);
/* 1046 */       state.prev.target = new JAXBElement(qn, context.expectedType.jaxbType, null, null);
/* 1047 */       state.receiver = this;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1052 */       state.loader = new XsiNilLoader(context.expectedType.getLoader(null, true));
/*      */     }
/*      */     
/*      */     public void receive(UnmarshallingContext.State state, Object o) {
/* 1056 */       JAXBElement e = (JAXBElement)state.target;
/* 1057 */       e.setValue(o);
/* 1058 */       state.getContext().recordOuterPeer(e);
/* 1059 */       (state.getContext()).result = e;
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
/*      */   public void recordInnerPeer(Object innerPeer) {
/* 1076 */     if (this.assoc != null) {
/* 1077 */       this.assoc.addInner(this.currentElement, innerPeer);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getInnerPeer() {
/* 1088 */     if (this.assoc != null && this.isInplaceMode) {
/* 1089 */       return this.assoc.getInnerPeer(this.currentElement);
/*      */     }
/* 1091 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void recordOuterPeer(Object outerPeer) {
/* 1102 */     if (this.assoc != null) {
/* 1103 */       this.assoc.addOuter(this.currentElement, outerPeer);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getOuterPeer() {
/* 1114 */     if (this.assoc != null && this.isInplaceMode) {
/* 1115 */       return this.assoc.getOuterPeer(this.currentElement);
/*      */     }
/* 1117 */     return null;
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
/*      */   public String getXMIMEContentType() {
/* 1139 */     Object t = this.current.target;
/* 1140 */     if (t == null) return null; 
/* 1141 */     return getJAXBContext().getXMIMEContentType(t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UnmarshallingContext getInstance() {
/* 1149 */     return (UnmarshallingContext)Coordinator._getInstance();
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\UnmarshallingContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */