/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import com.sun.xml.xsom.parser.AnnotationParser;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  30 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public annotation(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _existing, AnnotationContext _context) {
/*  34 */     super(source, parent, cookie);
/*  35 */     this.$runtime = runtime;
/*  36 */     this.existing = _existing;
/*  37 */     this.context = _context;
/*  38 */     this.$_ngcc_current_state = 2;
/*     */   }
/*     */   
/*     */   public annotation(NGCCRuntimeEx runtime, AnnotationImpl _existing, AnnotationContext _context) {
/*  42 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _existing, _context);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  47 */     this.locator = this.$runtime.copyLocator();
/*  48 */     this.parser = this.$runtime.createAnnotationParser();
/*  49 */     this.$runtime.redirectSubtree(this.parser.getContentHandler(this.context, this.$runtime.getAnnotationContextElementName(), this.$runtime.getErrorHandler(), this.$runtime.parser.getEntityResolver()), this.$uri, this.$localName, this.$qname);
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
/*  60 */     this.$uri = $__uri;
/*  61 */     this.$localName = $__local;
/*  62 */     this.$qname = $__qname;
/*  63 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  66 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  71 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  72 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  73 */           action0();
/*  74 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/*  77 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  83 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  91 */     this.$uri = $__uri;
/*  92 */     this.$localName = $__local;
/*  93 */     this.$qname = $__qname;
/*  94 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  97 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 102 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 103 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 104 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 107 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 113 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 121 */     this.$uri = $__uri;
/* 122 */     this.$localName = $__local;
/* 123 */     this.$qname = $__qname;
/* 124 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 127 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 132 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 140 */     this.$uri = $__uri;
/* 141 */     this.$localName = $__local;
/* 142 */     this.$qname = $__qname;
/* 143 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 146 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 151 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 159 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 162 */         revertToParentFromText(makeResult(), this._cookie, $value);
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
/* 174 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationImpl makeResult() {
/* 182 */     Object e = null;
/* 183 */     if (this.existing != null) e = this.existing.getAnnotation();
/*     */     
/* 185 */     return new AnnotationImpl(this.parser.getResult(e), this.locator);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\annotation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */