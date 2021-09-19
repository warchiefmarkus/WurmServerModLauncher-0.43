/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.v2.ClassFactory;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Coordinator
/*     */   implements ErrorHandler, ValidationEventHandler
/*     */ {
/*  78 */   private final HashMap<Class<? extends XmlAdapter>, XmlAdapter> adapters = new HashMap<Class<? extends XmlAdapter>, XmlAdapter>();
/*     */   
/*     */   private Coordinator old;
/*     */   
/*     */   public final XmlAdapter putAdapter(Class<? extends XmlAdapter> c, XmlAdapter a) {
/*  83 */     if (a == null) {
/*  84 */       return this.adapters.remove(c);
/*     */     }
/*  86 */     return this.adapters.put(c, a);
/*     */   }
/*     */ 
/*     */   
/*     */   private Coordinator[] table;
/*     */   
/*     */   public Exception guyWhoSetTheTableToNull;
/*     */ 
/*     */   
/*     */   public final <T extends XmlAdapter> T getAdapter(Class<T> key) {
/*  96 */     XmlAdapter xmlAdapter = (XmlAdapter)key.cast(this.adapters.get(key));
/*  97 */     if (xmlAdapter == null) {
/*  98 */       xmlAdapter = (XmlAdapter)ClassFactory.create(key);
/*  99 */       putAdapter(key, xmlAdapter);
/*     */     } 
/* 101 */     return (T)xmlAdapter;
/*     */   }
/*     */   
/*     */   public <T extends XmlAdapter> boolean containsAdapter(Class<T> type) {
/* 105 */     return this.adapters.containsKey(type);
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
/*     */   protected final void setThreadAffinity() {
/* 131 */     this.table = activeTable.get();
/* 132 */     assert this.table != null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void resetThreadAffinity() {
/* 140 */     if (debugTableNPE)
/* 141 */       this.guyWhoSetTheTableToNull = new Exception(); 
/* 142 */     this.table = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void pushCoordinator() {
/* 149 */     this.old = this.table[0];
/* 150 */     this.table[0] = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void popCoordinator() {
/* 157 */     assert this.table[0] == this;
/* 158 */     this.table[0] = this.old;
/* 159 */     this.old = null;
/*     */   }
/*     */   
/*     */   public static Coordinator _getInstance() {
/* 163 */     return ((Coordinator[])activeTable.get())[0];
/*     */   }
/*     */ 
/*     */   
/* 167 */   private static final ThreadLocal<Coordinator[]> activeTable = new ThreadLocal<Coordinator[]>() {
/*     */       public Coordinator[] initialValue() {
/* 169 */         return new Coordinator[1];
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean debugTableNPE;
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract ValidationEventLocator getLocation();
/*     */ 
/*     */ 
/*     */   
/*     */   public final void error(SAXParseException exception) throws SAXException {
/* 185 */     propagateEvent(1, exception);
/*     */   }
/*     */   
/*     */   public final void warning(SAXParseException exception) throws SAXException {
/* 189 */     propagateEvent(0, exception);
/*     */   }
/*     */   
/*     */   public final void fatalError(SAXParseException exception) throws SAXException {
/* 193 */     propagateEvent(2, exception);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void propagateEvent(int severity, SAXParseException saxException) throws SAXException {
/* 199 */     ValidationEventImpl ve = new ValidationEventImpl(severity, saxException.getMessage(), getLocation());
/*     */ 
/*     */     
/* 202 */     Exception e = saxException.getException();
/* 203 */     if (e != null) {
/* 204 */       ve.setLinkedException(e);
/*     */     } else {
/* 206 */       ve.setLinkedException(saxException);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 211 */     boolean result = handleEvent((ValidationEvent)ve);
/* 212 */     if (!result)
/*     */     {
/*     */       
/* 215 */       throw saxException;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/* 223 */       debugTableNPE = Boolean.getBoolean(Coordinator.class.getName() + ".debugTableNPE");
/* 224 */     } catch (SecurityException t) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\Coordinator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */