/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import com.sun.xml.xsom.parser.AnnotationParser;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ class annotation
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationContext context;
/*     */   private AnnotationImpl existing;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private AnnotationParser parser;
/*     */   private Locator locator;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  29 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public annotation(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _existing, AnnotationContext _context) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.existing = _existing;
/*  36 */     this.context = _context;
/*  37 */     this.$_ngcc_current_state = 2;
/*     */   }
/*     */   
/*     */   public annotation(NGCCRuntimeEx runtime, AnnotationImpl _existing, AnnotationContext _context) {
/*  41 */     this(null, (NGCCEventSource)runtime, runtime, -1, _existing, _context);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  46 */     this.locator = this.$runtime.copyLocator();
/*  47 */     this.parser = this.$runtime.createAnnotationParser();
/*  48 */     this.$runtime.redirectSubtree(this.parser.getContentHandler(this.context, this.$runtime.getAnnotationContextElementName(), this.$runtime.getErrorHandler(), this.$runtime.parser.getEntityResolver()), this.$uri, this.$localName, this.$qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  59 */     this.$uri = $__uri;
/*  60 */     this.$localName = $__local;
/*  61 */     this.$qname = $__qname;
/*  62 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  65 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  70 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  71 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  72 */           action0();
/*  73 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/*  76 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  82 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  90 */     this.$uri = $__uri;
/*  91 */     this.$localName = $__local;
/*  92 */     this.$qname = $__qname;
/*  93 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  96 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 101 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 102 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 103 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 106 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 120 */     this.$uri = $__uri;
/* 121 */     this.$localName = $__local;
/* 122 */     this.$qname = $__qname;
/* 123 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 126 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 131 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 139 */     this.$uri = $__uri;
/* 140 */     this.$localName = $__local;
/* 141 */     this.$qname = $__qname;
/* 142 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 145 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 150 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 158 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 161 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 173 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationImpl makeResult() {
/* 181 */     Object e = null;
/* 182 */     if (this.existing != null) e = this.existing.getAnnotation();
/*     */     
/* 184 */     return new AnnotationImpl(this.parser.getResult(e), this.locator);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\annotation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */