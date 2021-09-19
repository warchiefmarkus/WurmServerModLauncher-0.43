/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.ListSimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SimpleType_List
/*     */   extends NGCCHandler
/*     */ {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private UName itemTypeName;
/*     */   private Set finalSet;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  33 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private ListSimpleTypeImpl result; private Ref.SimpleType itemType; private Locator lloc;
/*     */   public SimpleType_List(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  37 */     super(source, parent, cookie);
/*  38 */     this.$runtime = runtime;
/*  39 */     this.annotation = _annotation;
/*  40 */     this.locator = _locator;
/*  41 */     this.fa = _fa;
/*  42 */     this.name = _name;
/*  43 */     this.finalSet = _finalSet;
/*  44 */     this.$_ngcc_current_state = 10;
/*     */   }
/*     */   
/*     */   public SimpleType_List(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  48 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _annotation, _locator, _fa, _name, _finalSet);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  53 */     this.result = new ListSimpleTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, (this.name == null), this.finalSet, this.itemType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  61 */     this.itemType = (Ref.SimpleType)new DelayedRef.SimpleType((PatcherManager)this.$runtime, this.lloc, this.$runtime.currentSchema, this.itemTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  67 */     this.lloc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  72 */     this.$uri = $__uri;
/*  73 */     this.$localName = $__local;
/*  74 */     this.$qname = $__qname;
/*  75 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/*  78 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/*  79 */           this.$runtime.consumeAttribute($ai);
/*  80 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  83 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*  84 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 166);
/*  85 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  88 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 10:
/*  95 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/*  96 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  97 */           action2();
/*  98 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 101 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 107 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 108 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 172, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/* 109 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 112 */           this.$_ngcc_current_state = 2;
/* 113 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 119 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 124 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType"))) {
/* 125 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 174, this.fa);
/* 126 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 129 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 135 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 143 */     this.$uri = $__uri;
/* 144 */     this.$localName = $__local;
/* 145 */     this.$qname = $__qname;
/* 146 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 149 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 150 */           this.$runtime.consumeAttribute($ai);
/* 151 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 154 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 160 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/* 161 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 162 */           this.$_ngcc_current_state = 0;
/* 163 */           action0();
/*     */         } else {
/*     */           
/* 166 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 172 */         this.$_ngcc_current_state = 2;
/* 173 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 178 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 183 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/* 184 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 174, this.fa);
/* 185 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 188 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 194 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 202 */     this.$uri = $__uri;
/* 203 */     this.$localName = $__local;
/* 204 */     this.$qname = $__qname;
/* 205 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 208 */         if ($__uri.equals("") && $__local.equals("itemType")) {
/* 209 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 212 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 218 */         this.$_ngcc_current_state = 2;
/* 219 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 224 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 229 */         if ($__uri.equals("") && $__local.equals("itemType")) {
/* 230 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 174, this.fa);
/* 231 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 234 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 240 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 248 */     this.$uri = $__uri;
/* 249 */     this.$localName = $__local;
/* 250 */     this.$qname = $__qname;
/* 251 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 254 */         if ($__uri.equals("") && $__local.equals("itemType")) {
/* 255 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 258 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 264 */         this.$_ngcc_current_state = 2;
/* 265 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 270 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 275 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/* 283 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 286 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 287 */           this.$runtime.consumeAttribute($ai);
/* 288 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 294 */         h = new qname(this, this._source, this.$runtime, 168);
/* 295 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 7:
/* 300 */         this.$_ngcc_current_state = 2;
/* 301 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 306 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 311 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 312 */           h = new foreignAttributes(this, this._source, this.$runtime, 174, this.fa);
/* 313 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 321 */     switch ($__cookie__) {
/*     */       
/*     */       case 168:
/* 324 */         this.itemTypeName = (UName)$__result__;
/* 325 */         action1();
/* 326 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 166:
/* 331 */         this.itemType = (Ref.SimpleType)$__result__;
/* 332 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 172:
/* 337 */         this.annotation = (AnnotationImpl)$__result__;
/* 338 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 174:
/* 343 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 344 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 351 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\SimpleType_List.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */