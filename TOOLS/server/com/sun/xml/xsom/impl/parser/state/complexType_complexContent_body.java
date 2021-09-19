/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.xsom.impl.ContentTypeImpl;
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
/*     */ class complexType_complexContent_body
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AttributesHolder owner;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private ContentTypeImpl particle;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  28 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public complexType_complexContent_body(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AttributesHolder _owner) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.owner = _owner;
/*  35 */     this.$_ngcc_current_state = 2;
/*     */   }
/*     */   
/*     */   public complexType_complexContent_body(NGCCRuntimeEx runtime, AttributesHolder _owner) {
/*  39 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _owner);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  43 */     if (this.particle == null) {
/*  44 */       this.particle = (ContentTypeImpl)this.$runtime.parser.schemaSet.empty;
/*     */     }
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  49 */     this.$uri = $__uri;
/*  50 */     this.$localName = $__local;
/*  51 */     this.$qname = $__qname;
/*  52 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  55 */         revertToParentFromEnterElement(this.particle, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/*  60 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  61 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 515, this.owner);
/*  62 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  65 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 515, this.owner);
/*  66 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  72 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/*  73 */           NGCCHandler h = new particle(this, this._source, this.$runtime, 517);
/*  74 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  77 */           this.$_ngcc_current_state = 1;
/*  78 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  84 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/*  92 */     this.$uri = $__uri;
/*  93 */     this.$localName = $__local;
/*  94 */     this.$qname = $__qname;
/*  95 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  98 */         revertToParentFromLeaveElement(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 103 */         h = new attributeUses(this, this._source, this.$runtime, 515, this.owner);
/* 104 */         spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 109 */         this.$_ngcc_current_state = 1;
/* 110 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 115 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/* 123 */     this.$uri = $__uri;
/* 124 */     this.$localName = $__local;
/* 125 */     this.$qname = $__qname;
/* 126 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 129 */         revertToParentFromEnterAttribute(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 134 */         h = new attributeUses(this, this._source, this.$runtime, 515, this.owner);
/* 135 */         spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 140 */         this.$_ngcc_current_state = 1;
/* 141 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 146 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/* 154 */     this.$uri = $__uri;
/* 155 */     this.$localName = $__local;
/* 156 */     this.$qname = $__qname;
/* 157 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 160 */         revertToParentFromLeaveAttribute(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 165 */         h = new attributeUses(this, this._source, this.$runtime, 515, this.owner);
/* 166 */         spawnChildFromLeaveAttribute(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 171 */         this.$_ngcc_current_state = 1;
/* 172 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 177 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     NGCCHandler h;
/* 185 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 188 */         revertToParentFromText(this.particle, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 193 */         h = new attributeUses(this, this._source, this.$runtime, 515, this.owner);
/* 194 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 199 */         this.$_ngcc_current_state = 1;
/* 200 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 207 */     switch ($__cookie__) {
/*     */       
/*     */       case 515:
/* 210 */         action0();
/* 211 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 517:
/* 216 */         this.particle = (ContentTypeImpl)$__result__;
/* 217 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 224 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\complexType_complexContent_body.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */