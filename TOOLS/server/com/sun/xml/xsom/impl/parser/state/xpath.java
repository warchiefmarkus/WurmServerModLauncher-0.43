/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.XPathImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class xpath
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String xpath;
/*     */   private ForeignAttributesImpl fa;
/*     */   private AnnotationImpl ann;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  30 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public xpath(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  34 */     super(source, parent, cookie);
/*  35 */     this.$runtime = runtime;
/*  36 */     this.$_ngcc_current_state = 6;
/*     */   }
/*     */   
/*     */   public xpath(NGCCRuntimeEx runtime) {
/*  40 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  45 */     this.$uri = $__uri;
/*  46 */     this.$localName = $__local;
/*  47 */     this.$qname = $__qname;
/*  48 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/*  51 */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/*  52 */           this.$runtime.consumeAttribute($ai);
/*  53 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  56 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/*  62 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  63 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 158, null, AnnotationContext.XPATH);
/*  64 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  67 */           this.$_ngcc_current_state = 0;
/*  68 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  74 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  79 */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  80 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 163, null);
/*  81 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  84 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  90 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*  98 */     this.$uri = $__uri;
/*  99 */     this.$localName = $__local;
/* 100 */     this.$qname = $__qname;
/* 101 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 104 */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 105 */           this.$runtime.consumeAttribute($ai);
/* 106 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 109 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 115 */         this.$_ngcc_current_state = 0;
/* 116 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 121 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 126 */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 127 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 163, null);
/* 128 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 131 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 137 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 145 */     this.$uri = $__uri;
/* 146 */     this.$localName = $__local;
/* 147 */     this.$qname = $__qname;
/* 148 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 151 */         if ($__uri.equals("") && $__local.equals("xpath")) {
/* 152 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 155 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 161 */         this.$_ngcc_current_state = 0;
/* 162 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 167 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 172 */         if ($__uri.equals("") && $__local.equals("xpath")) {
/* 173 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 163, null);
/* 174 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 177 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 183 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 191 */     this.$uri = $__uri;
/* 192 */     this.$localName = $__local;
/* 193 */     this.$qname = $__qname;
/* 194 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 197 */         this.$_ngcc_current_state = 0;
/* 198 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 203 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 208 */         if ($__uri.equals("") && $__local.equals("xpath")) {
/* 209 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 212 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 218 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 226 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 229 */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 230 */           this.$runtime.consumeAttribute($ai);
/* 231 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 237 */         this.$_ngcc_current_state = 0;
/* 238 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 243 */         this.xpath = $value;
/* 244 */         this.$_ngcc_current_state = 3;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 249 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 254 */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/* 255 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 163, null);
/* 256 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 264 */     switch ($__cookie__) {
/*     */       
/*     */       case 158:
/* 267 */         this.ann = (AnnotationImpl)$__result__;
/* 268 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 163:
/* 273 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 274 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 281 */     return (this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private XPathImpl makeResult() {
/* 286 */     return new XPathImpl(this.$runtime.document, this.ann, this.$runtime.copyLocator(), this.fa, this.$runtime.createXmlString(this.xpath));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\xpath.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */