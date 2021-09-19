/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.impl.AttGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.ComplexTypeImpl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.attributeGroupDecl;
/*     */ import com.sun.xml.xsom.impl.parser.state.complexType;
/*     */ import com.sun.xml.xsom.impl.parser.state.group;
/*     */ import com.sun.xml.xsom.impl.parser.state.simpleType;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class redefine extends NGCCHandler {
/*     */   private String schemaLocation;
/*     */   private ModelGroupDeclImpl newGrp;
/*     */   private AttGroupDeclImpl newAg;
/*     */   private SimpleTypeImpl newSt;
/*     */   private ComplexTypeImpl newCt;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected final NGCCRuntimeEx $runtime; private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname;
/*     */   public redefine(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  35 */     super(source, parent, cookie);
/*  36 */     this.$runtime = runtime;
/*  37 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */   
/*     */   public redefine(NGCCRuntimeEx runtime) {
/*  41 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  45 */     XSAttGroupDecl oldAg = this.$runtime.currentSchema.getAttGroupDecl(this.newAg.getName());
/*  46 */     this.newAg.redefine((AttGroupDeclImpl)oldAg);
/*  47 */     this.$runtime.currentSchema.addAttGroupDecl((XSAttGroupDecl)this.newAg);
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*  51 */     XSModelGroupDecl oldGrp = this.$runtime.currentSchema.getModelGroupDecl(this.newGrp.getName());
/*  52 */     this.newGrp.redefine((ModelGroupDeclImpl)oldGrp);
/*  53 */     this.$runtime.currentSchema.addModelGroupDecl((XSModelGroupDecl)this.newGrp);
/*     */   }
/*     */   
/*     */   private void action2() throws SAXException {
/*  57 */     XSComplexType oldCt = this.$runtime.currentSchema.getComplexType(this.newCt.getName());
/*  58 */     this.newCt.redefine((ComplexTypeImpl)oldCt);
/*  59 */     this.$runtime.currentSchema.addComplexType((XSComplexType)this.newCt);
/*     */   }
/*     */   
/*     */   private void action3() throws SAXException {
/*  63 */     XSSimpleType oldSt = this.$runtime.currentSchema.getSimpleType(this.newSt.getName());
/*     */     
/*  65 */     this.newSt.redefine((SimpleTypeImpl)oldSt);
/*  66 */     this.$runtime.currentSchema.addSimpleType((XSSimpleType)this.newSt);
/*     */   }
/*     */   
/*     */   private void action4() throws SAXException {
/*  70 */     this.$runtime.includeSchema(this.schemaLocation);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  75 */     this.$uri = $__uri;
/*  76 */     this.$localName = $__local;
/*  77 */     this.$qname = $__qname;
/*  78 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  81 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  86 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  87 */           annotation annotation = new annotation(this, this._source, this.$runtime, 481, null, AnnotationContext.SCHEMA);
/*  88 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  91 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*  92 */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 482);
/*  93 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  96 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  97 */           complexType complexType = new complexType(this, this._source, this.$runtime, 483);
/*  98 */           spawnChildFromEnterElement((NGCCEventReceiver)complexType, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 101 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 102 */           group group = new group(this, this._source, this.$runtime, 484);
/* 103 */           spawnChildFromEnterElement((NGCCEventReceiver)group, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 106 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 107 */           attributeGroupDecl attributeGroupDecl = new attributeGroupDecl(this, this._source, this.$runtime, 485);
/* 108 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeGroupDecl, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 111 */           this.$_ngcc_current_state = 1;
/* 112 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 122 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 123 */           annotation annotation = new annotation(this, this._source, this.$runtime, 476, null, AnnotationContext.SCHEMA);
/* 124 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 127 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 128 */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 477);
/* 129 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 132 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/* 133 */           complexType complexType = new complexType(this, this._source, this.$runtime, 478);
/* 134 */           spawnChildFromEnterElement((NGCCEventReceiver)complexType, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 137 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 138 */           group group = new group(this, this._source, this.$runtime, 479);
/* 139 */           spawnChildFromEnterElement((NGCCEventReceiver)group, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 142 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 143 */           attributeGroupDecl attributeGroupDecl = new attributeGroupDecl(this, this._source, this.$runtime, 480);
/* 144 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeGroupDecl, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 147 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 15:
/* 157 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/* 158 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 159 */           this.$_ngcc_current_state = 14;
/*     */         } else {
/*     */           
/* 162 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 168 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 169 */           this.$runtime.consumeAttribute($ai);
/* 170 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 173 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 179 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 187 */     this.$uri = $__uri;
/* 188 */     this.$localName = $__local;
/* 189 */     this.$qname = $__qname;
/* 190 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 193 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 198 */         this.$_ngcc_current_state = 1;
/* 199 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 204 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/* 205 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 206 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 209 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 215 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 216 */           this.$runtime.consumeAttribute($ai);
/* 217 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 220 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 226 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 234 */     this.$uri = $__uri;
/* 235 */     this.$localName = $__local;
/* 236 */     this.$qname = $__qname;
/* 237 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 240 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 245 */         this.$_ngcc_current_state = 1;
/* 246 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 251 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 252 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 255 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 261 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 269 */     this.$uri = $__uri;
/* 270 */     this.$localName = $__local;
/* 271 */     this.$qname = $__qname;
/* 272 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 275 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 280 */         this.$_ngcc_current_state = 1;
/* 281 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 286 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 287 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 290 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 296 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 304 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 307 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 312 */         this.$_ngcc_current_state = 1;
/* 313 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 318 */         this.schemaLocation = $value;
/* 319 */         this.$_ngcc_current_state = 12;
/* 320 */         action4();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 14:
/* 325 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 326 */           this.$runtime.consumeAttribute($ai);
/* 327 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 335 */     switch ($__cookie__) {
/*     */       
/*     */       case 476:
/* 338 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 477:
/* 343 */         this.newSt = (SimpleTypeImpl)$__result__;
/* 344 */         action3();
/* 345 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 478:
/* 350 */         this.newCt = (ComplexTypeImpl)$__result__;
/* 351 */         action2();
/* 352 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 479:
/* 357 */         this.newGrp = (ModelGroupDeclImpl)$__result__;
/* 358 */         action1();
/* 359 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 480:
/* 364 */         this.newAg = (AttGroupDeclImpl)$__result__;
/* 365 */         action0();
/* 366 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 481:
/* 371 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 482:
/* 376 */         this.newSt = (SimpleTypeImpl)$__result__;
/* 377 */         action3();
/* 378 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 483:
/* 383 */         this.newCt = (ComplexTypeImpl)$__result__;
/* 384 */         action2();
/* 385 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 484:
/* 390 */         this.newGrp = (ModelGroupDeclImpl)$__result__;
/* 391 */         action1();
/* 392 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 485:
/* 397 */         this.newAg = (AttGroupDeclImpl)$__result__;
/* 398 */         action0();
/* 399 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 406 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\redefine.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */