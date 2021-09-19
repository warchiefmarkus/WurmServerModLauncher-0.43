/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.impl.AttGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.ComplexTypeImpl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.parser.Messages;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class redefine
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String schemaLocation;
/*     */   private ModelGroupDeclImpl newGrp;
/*     */   private AttGroupDeclImpl newAg;
/*     */   private SimpleTypeImpl newSt;
/*     */   private ComplexTypeImpl newCt;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  32 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public redefine(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  36 */     super(source, parent, cookie);
/*  37 */     this.$runtime = runtime;
/*  38 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */   
/*     */   public redefine(NGCCRuntimeEx runtime) {
/*  42 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  46 */     XSAttGroupDecl oldAg = this.$runtime.currentSchema.getAttGroupDecl(this.newAg.getName());
/*  47 */     if (oldAg == null) {
/*  48 */       this.$runtime.reportError(Messages.format("UndefinedAttributeGroup", new Object[] { this.newAg.getName() }));
/*     */     } else {
/*  50 */       this.newAg.redefine((AttGroupDeclImpl)oldAg);
/*  51 */       this.$runtime.currentSchema.addAttGroupDecl((XSAttGroupDecl)this.newAg, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*  56 */     XSModelGroupDecl oldGrp = this.$runtime.currentSchema.getModelGroupDecl(this.newGrp.getName());
/*  57 */     if (oldGrp == null) {
/*  58 */       this.$runtime.reportError(Messages.format("UndefinedModelGroup", new Object[] { this.newGrp.getName() }));
/*     */     } else {
/*  60 */       this.newGrp.redefine((ModelGroupDeclImpl)oldGrp);
/*  61 */       this.$runtime.currentSchema.addModelGroupDecl((XSModelGroupDecl)this.newGrp, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action2() throws SAXException {
/*  66 */     XSComplexType oldCt = this.$runtime.currentSchema.getComplexType(this.newCt.getName());
/*  67 */     if (oldCt == null) {
/*  68 */       this.$runtime.reportError(Messages.format("UndefinedCompplexType", new Object[] { this.newCt.getName() }));
/*     */     } else {
/*  70 */       this.newCt.redefine((ComplexTypeImpl)oldCt);
/*  71 */       this.$runtime.currentSchema.addComplexType((XSComplexType)this.newCt, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action3() throws SAXException {
/*  76 */     XSSimpleType oldSt = this.$runtime.currentSchema.getSimpleType(this.newSt.getName());
/*  77 */     if (oldSt == null) {
/*  78 */       this.$runtime.reportError(Messages.format("UndefinedSimpleType", new Object[] { this.newSt.getName() }));
/*     */     } else {
/*  80 */       this.newSt.redefine((SimpleTypeImpl)oldSt);
/*  81 */       this.$runtime.currentSchema.addSimpleType((XSSimpleType)this.newSt, true);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void action4() throws SAXException {
/*  86 */     this.$runtime.includeSchema(this.schemaLocation);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  91 */     this.$uri = $__uri;
/*  92 */     this.$localName = $__local;
/*  93 */     this.$qname = $__qname;
/*  94 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  97 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  98 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 369, null, AnnotationContext.SCHEMA);
/*  99 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 102 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 103 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 370);
/* 104 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 107 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/* 108 */           NGCCHandler h = new complexType(this, this._source, this.$runtime, 371);
/* 109 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 112 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 113 */           NGCCHandler h = new group(this, this._source, this.$runtime, 372);
/* 114 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 117 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 118 */           NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 373);
/* 119 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 122 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 132 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 133 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 374, null, AnnotationContext.SCHEMA);
/* 134 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 137 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 138 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 375);
/* 139 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 142 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/* 143 */           NGCCHandler h = new complexType(this, this._source, this.$runtime, 376);
/* 144 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 147 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 148 */           NGCCHandler h = new group(this, this._source, this.$runtime, 377);
/* 149 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 152 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 153 */           NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 378);
/* 154 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 157 */           this.$_ngcc_current_state = 1;
/* 158 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 15:
/* 168 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/* 169 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 170 */           this.$_ngcc_current_state = 14;
/*     */         } else {
/*     */           
/* 173 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 179 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 180 */           this.$runtime.consumeAttribute($ai);
/* 181 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 184 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 190 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 195 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 203 */     this.$uri = $__uri;
/* 204 */     this.$localName = $__local;
/* 205 */     this.$qname = $__qname;
/* 206 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 209 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/* 210 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 211 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 214 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 220 */         this.$_ngcc_current_state = 1;
/* 221 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 226 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 227 */           this.$runtime.consumeAttribute($ai);
/* 228 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 231 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 237 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 242 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 250 */     this.$uri = $__uri;
/* 251 */     this.$localName = $__local;
/* 252 */     this.$qname = $__qname;
/* 253 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 256 */         this.$_ngcc_current_state = 1;
/* 257 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 262 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 263 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 266 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 272 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 277 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 285 */     this.$uri = $__uri;
/* 286 */     this.$localName = $__local;
/* 287 */     this.$qname = $__qname;
/* 288 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 291 */         this.$_ngcc_current_state = 1;
/* 292 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 297 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 298 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 301 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 307 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 312 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 320 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 323 */         this.$_ngcc_current_state = 1;
/* 324 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 14:
/* 329 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 330 */           this.$runtime.consumeAttribute($ai);
/* 331 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 337 */         this.schemaLocation = $value;
/* 338 */         this.$_ngcc_current_state = 12;
/* 339 */         action4();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 344 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 351 */     switch ($__cookie__) {
/*     */       
/*     */       case 374:
/* 354 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 375:
/* 359 */         this.newSt = (SimpleTypeImpl)$__result__;
/* 360 */         action3();
/* 361 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 376:
/* 366 */         this.newCt = (ComplexTypeImpl)$__result__;
/* 367 */         action2();
/* 368 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 377:
/* 373 */         this.newGrp = (ModelGroupDeclImpl)$__result__;
/* 374 */         action1();
/* 375 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 378:
/* 380 */         this.newAg = (AttGroupDeclImpl)$__result__;
/* 381 */         action0();
/* 382 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 369:
/* 387 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 370:
/* 392 */         this.newSt = (SimpleTypeImpl)$__result__;
/* 393 */         action3();
/* 394 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 371:
/* 399 */         this.newCt = (ComplexTypeImpl)$__result__;
/* 400 */         action2();
/* 401 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 372:
/* 406 */         this.newGrp = (ModelGroupDeclImpl)$__result__;
/* 407 */         action1();
/* 408 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 373:
/* 413 */         this.newAg = (AttGroupDeclImpl)$__result__;
/* 414 */         action0();
/* 415 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 422 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\redefine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */