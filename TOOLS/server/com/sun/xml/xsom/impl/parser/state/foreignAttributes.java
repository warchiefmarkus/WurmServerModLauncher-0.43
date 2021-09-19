/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*     */ class foreignAttributes
/*     */   extends NGCCHandler
/*     */ {
/*     */   private ForeignAttributesImpl current;
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
/*     */   public foreignAttributes(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, ForeignAttributesImpl _current) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.current = _current;
/*  35 */     this.$_ngcc_current_state = 0;
/*     */   }
/*     */   
/*     */   public foreignAttributes(NGCCRuntimeEx runtime, ForeignAttributesImpl _current) {
/*  39 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _current);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  44 */     this.$uri = $__uri;
/*  45 */     this.$localName = $__local;
/*  46 */     this.$qname = $__qname;
/*  47 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  50 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  55 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  63 */     this.$uri = $__uri;
/*  64 */     this.$localName = $__local;
/*  65 */     this.$qname = $__qname;
/*  66 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  69 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  74 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  82 */     this.$uri = $__uri;
/*  83 */     this.$localName = $__local;
/*  84 */     this.$qname = $__qname;
/*  85 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  88 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 101 */     this.$uri = $__uri;
/* 102 */     this.$localName = $__local;
/* 103 */     this.$qname = $__qname;
/* 104 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 107 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 120 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 123 */         revertToParentFromText(makeResult(), this._cookie, $value);
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
/* 135 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   ForeignAttributesImpl makeResult() {
/* 140 */     return this.$runtime.parseForeignAttributes(this.current);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\foreignAttributes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */