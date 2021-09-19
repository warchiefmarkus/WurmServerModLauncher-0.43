/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.XSVariety;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class simpleType
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   private String finalValue;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected String $uri; protected String $localName; protected String $qname; private SimpleTypeImpl result; private Locator locator; private Set finalSet;
/*     */   public simpleType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  35 */     super(source, parent, cookie);
/*  36 */     this.$runtime = runtime;
/*  37 */     this.$_ngcc_current_state = 19;
/*     */   }
/*     */   
/*     */   public simpleType(NGCCRuntimeEx runtime) {
/*  41 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  45 */     this.finalSet = makeFinalSet(this.finalValue);
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*  49 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  54 */     this.$uri = $__uri;
/*  55 */     this.$localName = $__local;
/*  56 */     this.$qname = $__qname;
/*  57 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 8:
/*  60 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  61 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 632, null, AnnotationContext.SIMPLETYPE_DECL);
/*  62 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  65 */           this.$_ngcc_current_state = 7;
/*  66 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/*  72 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*  73 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  74 */           action1();
/*  75 */           this.$_ngcc_current_state = 15;
/*     */         } else {
/*     */           
/*  78 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/*  84 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  85 */           this.$runtime.consumeAttribute($ai);
/*  86 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  89 */           this.$_ngcc_current_state = 10;
/*  90 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  96 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 101 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 102 */           this.$runtime.consumeAttribute($ai);
/* 103 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 106 */           this.$_ngcc_current_state = 11;
/* 107 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 113 */         action0();
/* 114 */         this.$_ngcc_current_state = 2;
/* 115 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 120 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 121 */           NGCCHandler h = new SimpleType_Restriction(this, this._source, this.$runtime, 628, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 122 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 125 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/* 126 */           NGCCHandler h = new SimpleType_List(this, this._source, this.$runtime, 629, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 127 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 130 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("union")) {
/* 131 */           NGCCHandler h = new SimpleType_Union(this, this._source, this.$runtime, 623, this.annotation, this.locator, this.fa, this.name, this.finalSet);
/* 132 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 135 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 10:
/* 143 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("union")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list"))) {
/* 144 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 634, this.fa);
/* 145 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 148 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 154 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 162 */     this.$uri = $__uri;
/* 163 */     this.$localName = $__local;
/* 164 */     this.$qname = $__qname;
/* 165 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 8:
/* 168 */         this.$_ngcc_current_state = 7;
/* 169 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 174 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 175 */           this.$runtime.consumeAttribute($ai);
/* 176 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 179 */           this.$_ngcc_current_state = 10;
/* 180 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 186 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 191 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 192 */           this.$runtime.consumeAttribute($ai);
/* 193 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 196 */           this.$_ngcc_current_state = 11;
/* 197 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 203 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 204 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 205 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 208 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 214 */         action0();
/* 215 */         this.$_ngcc_current_state = 2;
/* 216 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 221 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 229 */     this.$uri = $__uri;
/* 230 */     this.$localName = $__local;
/* 231 */     this.$qname = $__qname;
/* 232 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 8:
/* 235 */         this.$_ngcc_current_state = 7;
/* 236 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 241 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 242 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 245 */           this.$_ngcc_current_state = 10;
/* 246 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 252 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 257 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 258 */           this.$_ngcc_current_state = 17;
/*     */         } else {
/*     */           
/* 261 */           this.$_ngcc_current_state = 11;
/* 262 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 268 */         action0();
/* 269 */         this.$_ngcc_current_state = 2;
/* 270 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 275 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 283 */     this.$uri = $__uri;
/* 284 */     this.$localName = $__local;
/* 285 */     this.$qname = $__qname;
/* 286 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 8:
/* 289 */         this.$_ngcc_current_state = 7;
/* 290 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 295 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 296 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 299 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 305 */         this.$_ngcc_current_state = 10;
/* 306 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 311 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 316 */         this.$_ngcc_current_state = 11;
/* 317 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 322 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 323 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 326 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 332 */         action0();
/* 333 */         this.$_ngcc_current_state = 2;
/* 334 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 339 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 347 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 8:
/* 350 */         this.$_ngcc_current_state = 7;
/* 351 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 11:
/* 356 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 357 */           this.$runtime.consumeAttribute($ai);
/* 358 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 361 */         this.$_ngcc_current_state = 10;
/* 362 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 368 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 373 */         this.name = $value;
/* 374 */         this.$_ngcc_current_state = 12;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 15:
/* 379 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 380 */           this.$runtime.consumeAttribute($ai);
/* 381 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 384 */         this.$_ngcc_current_state = 11;
/* 385 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/* 391 */         action0();
/* 392 */         this.$_ngcc_current_state = 2;
/* 393 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 17:
/* 398 */         this.finalValue = $value;
/* 399 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 406 */     switch ($__cookie__) {
/*     */       
/*     */       case 634:
/* 409 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 410 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 632:
/* 415 */         this.annotation = (AnnotationImpl)$__result__;
/* 416 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 628:
/* 421 */         this.result = (SimpleTypeImpl)$__result__;
/* 422 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 629:
/* 427 */         this.result = (SimpleTypeImpl)$__result__;
/* 428 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 623:
/* 433 */         this.result = (SimpleTypeImpl)$__result__;
/* 434 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 441 */     return (this.$_ngcc_current_state == 0);
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
/*     */   private Set makeFinalSet(String finalValue) {
/* 453 */     if (finalValue == null) {
/* 454 */       return Collections.EMPTY_SET;
/*     */     }
/* 456 */     Set<XSVariety> s = new HashSet();
/* 457 */     StringTokenizer tokens = new StringTokenizer(finalValue);
/* 458 */     while (tokens.hasMoreTokens()) {
/* 459 */       String token = tokens.nextToken();
/* 460 */       if (token.equals("#all")) {
/* 461 */         s.add(XSVariety.ATOMIC);
/* 462 */         s.add(XSVariety.UNION);
/* 463 */         s.add(XSVariety.LIST);
/*     */       } 
/* 465 */       if (token.equals("list")) {
/* 466 */         s.add(XSVariety.LIST);
/*     */       }
/* 468 */       if (token.equals("union")) {
/* 469 */         s.add(XSVariety.UNION);
/*     */       }
/* 471 */       if (token.equals("restriction")) {
/* 472 */         s.add(XSVariety.ATOMIC);
/*     */       }
/*     */     } 
/* 475 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\simpleType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */