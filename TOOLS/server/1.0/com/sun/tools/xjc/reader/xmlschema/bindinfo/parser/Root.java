/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.AnnotationState;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.declaration;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Root
/*     */   extends NGCCHandler
/*     */ {
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  28 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public Root(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public Root(NGCCRuntimeEx runtime) {
/*  38 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  42 */     this.$uri = $__uri;
/*  43 */     this.$localName = $__local;
/*  44 */     this.$qname = $__qname;
/*  45 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  48 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/*  53 */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumMember") || ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "dom") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "class") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumClass") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "property") || ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "idSymbolSpace") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "globalBindings") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "schemaBindings") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType")) {
/*  54 */           declaration declaration = new declaration(this, this._source, this.$runtime, 426);
/*  55 */           spawnChildFromEnterElement((NGCCEventReceiver)declaration, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  58 */         else if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "annotation") {
/*  59 */           AnnotationState annotationState = new AnnotationState(this, this._source, this.$runtime, 424);
/*  60 */           spawnChildFromEnterElement((NGCCEventReceiver)annotationState, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  63 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  70 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  77 */     this.$uri = $__uri;
/*  78 */     this.$localName = $__local;
/*  79 */     this.$qname = $__qname;
/*  80 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  83 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  88 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  95 */     this.$uri = $__uri;
/*  96 */     this.$localName = $__local;
/*  97 */     this.$qname = $__qname;
/*  98 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 101 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 106 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 113 */     this.$uri = $__uri;
/* 114 */     this.$localName = $__local;
/* 115 */     this.$qname = $__qname;
/* 116 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 119 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 124 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 131 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 134 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 141 */     switch ($__cookie__) {
/*     */       
/*     */       case 426:
/* 144 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 424:
/* 149 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 156 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\Root.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */