/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class includeDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String schemaLocation;
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
/*     */   public includeDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 7;
/*     */   }
/*     */   
/*     */   public includeDecl(NGCCRuntimeEx runtime) {
/*  38 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  42 */     this.$runtime.includeSchema(this.schemaLocation);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  47 */     this.$uri = $__uri;
/*  48 */     this.$localName = $__local;
/*  49 */     this.$qname = $__qname;
/*  50 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  53 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  58 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/*  59 */           this.$runtime.consumeAttribute($ai);
/*  60 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  63 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/*  69 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/*  70 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  71 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/*  74 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  80 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  81 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 521, null, AnnotationContext.SCHEMA);
/*  82 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  85 */           this.$_ngcc_current_state = 1;
/*  86 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  92 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 100 */     this.$uri = $__uri;
/* 101 */     this.$localName = $__local;
/* 102 */     this.$qname = $__qname;
/* 103 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 106 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 111 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/* 112 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 113 */           this.$_ngcc_current_state = 0;
/* 114 */           action0();
/*     */         } else {
/*     */           
/* 117 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 123 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 124 */           this.$runtime.consumeAttribute($ai);
/* 125 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 128 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 134 */         this.$_ngcc_current_state = 1;
/* 135 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 140 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 148 */     this.$uri = $__uri;
/* 149 */     this.$localName = $__local;
/* 150 */     this.$qname = $__qname;
/* 151 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 154 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 159 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 160 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 163 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 169 */         this.$_ngcc_current_state = 1;
/* 170 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 175 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 183 */     this.$uri = $__uri;
/* 184 */     this.$localName = $__local;
/* 185 */     this.$qname = $__qname;
/* 186 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 189 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 194 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 195 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 198 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 204 */         this.$_ngcc_current_state = 1;
/* 205 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 210 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 218 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 221 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 226 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 227 */           this.$runtime.consumeAttribute($ai);
/* 228 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 234 */         this.schemaLocation = $value;
/* 235 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 240 */         this.$_ngcc_current_state = 1;
/* 241 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 248 */     switch ($__cookie__) {
/*     */       
/*     */       case 521:
/* 251 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 258 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\includeDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */