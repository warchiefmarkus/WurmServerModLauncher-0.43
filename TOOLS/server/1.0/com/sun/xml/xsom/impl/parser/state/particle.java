/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ElementDecl;
/*     */ import com.sun.xml.xsom.impl.ParticleImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.elementDeclBody;
/*     */ import com.sun.xml.xsom.impl.parser.state.modelGroupBody;
/*     */ import com.sun.xml.xsom.impl.parser.state.occurs;
/*     */ import com.sun.xml.xsom.impl.parser.state.qname;
/*     */ import com.sun.xml.xsom.impl.parser.state.wildcardBody;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class particle extends NGCCHandler {
/*     */   private AnnotationImpl annotation;
/*     */   private ElementDecl anonymousElementDecl;
/*     */   private WildcardImpl wcBody;
/*     */   private ModelGroupImpl term;
/*     */   private UName elementTypeName;
/*     */   private occurs occurs;
/*     */   private UName groupName;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  33 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private Locator wloc; private Locator loc; private ParticleImpl result; private String compositorName;
/*     */   public particle(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  37 */     super(source, parent, cookie);
/*  38 */     this.$runtime = runtime;
/*  39 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public particle(NGCCRuntimeEx runtime) {
/*  43 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  48 */     this.result = new ParticleImpl(this.$runtime.currentSchema, null, (Ref.Term)this.wcBody, this.wloc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  53 */     this.wloc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  58 */     this.result = new ParticleImpl(this.$runtime.currentSchema, null, (Ref.Term)this.anonymousElementDecl, this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  66 */     this.result = new ParticleImpl(this.$runtime.currentSchema, this.annotation, (Ref.Term)new DelayedRef.Element((PatcherManager)this.$runtime, this.loc, this.$runtime.currentSchema, this.elementTypeName), this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action4() throws SAXException {
/*  73 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action5() throws SAXException {
/*  78 */     this.result = new ParticleImpl(this.$runtime.currentSchema, this.annotation, (Ref.Term)new DelayedRef.ModelGroup((PatcherManager)this.$runtime, this.loc, this.$runtime.currentSchema, this.groupName), this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action6() throws SAXException {
/*  85 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action7() throws SAXException {
/*  90 */     this.result = new ParticleImpl(this.$runtime.currentSchema, null, (Ref.Term)this.term, this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action8() throws SAXException {
/*  96 */     this.compositorName = this.$localName;
/*  97 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/* 103 */     this.$uri = $__uri;
/* 104 */     this.$localName = $__local;
/* 105 */     this.$qname = $__qname;
/* 106 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 109 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 110 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 416, this.wloc);
/* 111 */           spawnChildFromEnterElement((NGCCEventReceiver)wildcardBody, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 114 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 120 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 121 */           annotation annotation = new annotation(this, this._source, this.$runtime, 425, null, AnnotationContext.PARTICLE);
/* 122 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 125 */           this.$_ngcc_current_state = 10;
/* 126 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 26:
/* 132 */         if ((($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 133 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 442);
/* 134 */           spawnChildFromEnterElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 137 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 143 */         action3();
/* 144 */         this.$_ngcc_current_state = 7;
/* 145 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 150 */         if ((($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 151 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 417);
/* 152 */           spawnChildFromEnterElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 155 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 20:
/* 161 */         action5();
/* 162 */         this.$_ngcc_current_state = 19;
/* 163 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 168 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 169 */           this.$runtime.consumeAttribute($ai);
/* 170 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 173 */         else if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 174 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 175 */           spawnChildFromEnterElement((NGCCEventReceiver)elementDeclBody, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 178 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 30:
/* 185 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))))) {
/* 186 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 447);
/* 187 */           spawnChildFromEnterElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 190 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 196 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 197 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 198 */           action8();
/* 199 */           this.$_ngcc_current_state = 30;
/*     */         
/*     */         }
/* 202 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 203 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 204 */           action6();
/* 205 */           this.$_ngcc_current_state = 26;
/*     */         
/*     */         }
/* 208 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/* 209 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 210 */           action4();
/* 211 */           this.$_ngcc_current_state = 16;
/*     */         
/*     */         }
/* 214 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) {
/* 215 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 216 */           action1();
/* 217 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 220 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 229 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 234 */         if ((($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")))) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique"))))) {
/* 235 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 236 */           spawnChildFromEnterElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 239 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 245 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 246 */           this.$runtime.consumeAttribute($ai);
/* 247 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 250 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 256 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 257 */           modelGroupBody modelGroupBody = new modelGroupBody(this, this._source, this.$runtime, 446, this.loc, this.compositorName);
/* 258 */           spawnChildFromEnterElement((NGCCEventReceiver)modelGroupBody, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 261 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 267 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 268 */           annotation annotation = new annotation(this, this._source, this.$runtime, 437, null, AnnotationContext.PARTICLE);
/* 269 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 272 */           this.$_ngcc_current_state = 20;
/* 273 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 279 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 287 */     this.$uri = $__uri;
/* 288 */     this.$localName = $__local;
/* 289 */     this.$qname = $__qname;
/* 290 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 293 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/* 294 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 295 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 298 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 304 */         if ((($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/* 305 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 416, this.wloc);
/* 306 */           spawnChildFromLeaveElement((NGCCEventReceiver)wildcardBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 309 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 315 */         this.$_ngcc_current_state = 10;
/* 316 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 28:
/* 321 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 322 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 323 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 326 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 26:
/* 332 */         if ((($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/* 333 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 442);
/* 334 */           spawnChildFromLeaveElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 337 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 343 */         action3();
/* 344 */         this.$_ngcc_current_state = 7;
/* 345 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 350 */         if ((($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/* 351 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 417);
/* 352 */           spawnChildFromLeaveElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 355 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 361 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) {
/* 362 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 363 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 366 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 372 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 373 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 374 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 377 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 20:
/* 383 */         action5();
/* 384 */         this.$_ngcc_current_state = 19;
/* 385 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 390 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 391 */           this.$runtime.consumeAttribute($ai);
/* 392 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 395 */         else if ((($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/* 396 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 397 */           spawnChildFromLeaveElement((NGCCEventReceiver)elementDeclBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 400 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 30:
/* 407 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")))) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))))) {
/* 408 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 447);
/* 409 */           spawnChildFromLeaveElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 412 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 418 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 423 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 424 */           this.$runtime.consumeAttribute($ai);
/* 425 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 428 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 434 */         if ((($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/* 435 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 436 */           spawnChildFromLeaveElement((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 439 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 445 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 446 */           modelGroupBody modelGroupBody = new modelGroupBody(this, this._source, this.$runtime, 446, this.loc, this.compositorName);
/* 447 */           spawnChildFromLeaveElement((NGCCEventReceiver)modelGroupBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 450 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 456 */         this.$_ngcc_current_state = 20;
/* 457 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 462 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 470 */     this.$uri = $__uri;
/* 471 */     this.$localName = $__local;
/* 472 */     this.$qname = $__qname;
/* 473 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 20:
/* 476 */         action5();
/* 477 */         this.$_ngcc_current_state = 19;
/* 478 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 483 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 484 */           this.$_ngcc_current_state = 14;
/*     */         
/*     */         }
/* 487 */         else if (($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("abstract")) || ($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("fixed"))) {
/* 488 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 489 */           spawnChildFromEnterAttribute((NGCCEventReceiver)elementDeclBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 492 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 30:
/* 499 */         if (($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("maxOccurs"))) {
/* 500 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 447);
/* 501 */           spawnChildFromEnterAttribute((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 504 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 510 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 515 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 516 */           this.$_ngcc_current_state = 24;
/*     */         } else {
/*     */           
/* 519 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 525 */         if (($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("ref")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("abstract")) || ($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("fixed")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("maxOccurs"))) {
/* 526 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 527 */           spawnChildFromEnterAttribute((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 530 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 536 */         if (($__uri.equals("") && $__local.equals("processContents")) || ($__uri.equals("") && $__local.equals("namespace"))) {
/* 537 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 416, this.wloc);
/* 538 */           spawnChildFromEnterAttribute((NGCCEventReceiver)wildcardBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 541 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 547 */         action3();
/* 548 */         this.$_ngcc_current_state = 7;
/* 549 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 26:
/* 554 */         if (($__uri.equals("") && $__local.equals("ref")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("maxOccurs"))) {
/* 555 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 442);
/* 556 */           spawnChildFromEnterAttribute((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 559 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 565 */         this.$_ngcc_current_state = 10;
/* 566 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 571 */         if (($__uri.equals("") && $__local.equals("processContents")) || ($__uri.equals("") && $__local.equals("namespace")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("maxOccurs"))) {
/* 572 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 417);
/* 573 */           spawnChildFromEnterAttribute((NGCCEventReceiver)occurs1, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 576 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 582 */         this.$_ngcc_current_state = 20;
/* 583 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 588 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 596 */     this.$uri = $__uri;
/* 597 */     this.$localName = $__local;
/* 598 */     this.$qname = $__qname;
/* 599 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 23:
/* 602 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 603 */           this.$_ngcc_current_state = 21;
/*     */         } else {
/*     */           
/* 606 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 20:
/* 612 */         action5();
/* 613 */         this.$_ngcc_current_state = 19;
/* 614 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 619 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 620 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 623 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 629 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 634 */         action3();
/* 635 */         this.$_ngcc_current_state = 7;
/* 636 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 641 */         this.$_ngcc_current_state = 10;
/* 642 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 647 */         this.$_ngcc_current_state = 20;
/* 648 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 653 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     qname qname2;
/*     */     qname qname1;
/* 661 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 24:
/* 664 */         qname2 = new qname(this, this._source, this.$runtime, 440);
/* 665 */         spawnChildFromText((NGCCEventReceiver)qname2, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 14:
/* 670 */         qname1 = new qname(this, this._source, this.$runtime, 428);
/* 671 */         spawnChildFromText((NGCCEventReceiver)qname1, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 676 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 677 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 416, this.wloc);
/* 678 */           spawnChildFromText((NGCCEventReceiver)wildcardBody, $value);
/*     */           break;
/*     */         } 
/* 681 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 682 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 416, this.wloc);
/* 683 */           spawnChildFromText((NGCCEventReceiver)wildcardBody, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 11:
/* 690 */         this.$_ngcc_current_state = 10;
/* 691 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 26:
/* 696 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 697 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 442);
/* 698 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 701 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 702 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 442);
/* 703 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 706 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 707 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 442);
/* 708 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 10:
/* 716 */         action3();
/* 717 */         this.$_ngcc_current_state = 7;
/* 718 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 723 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 724 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 417);
/* 725 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 728 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 729 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 417);
/* 730 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 733 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 734 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 417);
/* 735 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 738 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 739 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 417);
/* 740 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 20:
/* 749 */         action5();
/* 750 */         this.$_ngcc_current_state = 19;
/* 751 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 756 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 757 */           this.$runtime.consumeAttribute($ai);
/* 758 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 761 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 762 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 763 */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*     */           break;
/*     */         } 
/* 766 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 767 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 768 */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*     */           break;
/*     */         } 
/* 771 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 772 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 773 */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*     */           break;
/*     */         } 
/* 776 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 777 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 778 */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*     */           break;
/*     */         } 
/* 781 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 782 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 783 */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*     */           break;
/*     */         } 
/* 786 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 787 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 788 */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*     */           break;
/*     */         } 
/* 791 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 792 */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 422, this.loc, false);
/* 793 */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 30:
/* 806 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 807 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 447);
/* 808 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 811 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 812 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 447);
/* 813 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 820 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 16:
/* 825 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 826 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 827 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 830 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 831 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 832 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 835 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 836 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 837 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 840 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 841 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 842 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 845 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 846 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 847 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 850 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 851 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 852 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 855 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 856 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 857 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 860 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 861 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 862 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 865 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 866 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 867 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */           break;
/*     */         } 
/* 870 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 871 */           occurs occurs1 = new occurs(this, this._source, this.$runtime, 431);
/* 872 */           spawnChildFromText((NGCCEventReceiver)occurs1, $value);
/*     */         } 
/*     */         break;
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
/*     */       
/*     */       case 25:
/* 887 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 888 */           this.$runtime.consumeAttribute($ai);
/* 889 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 21:
/* 895 */         this.$_ngcc_current_state = 20;
/* 896 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 903 */     switch ($__cookie__) {
/*     */       
/*     */       case 440:
/* 906 */         this.groupName = (UName)$__result__;
/* 907 */         this.$_ngcc_current_state = 23;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 416:
/* 912 */         this.wcBody = (WildcardImpl)$__result__;
/* 913 */         action0();
/* 914 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 425:
/* 919 */         this.annotation = (AnnotationImpl)$__result__;
/* 920 */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 447:
/* 925 */         this.occurs = (occurs)$__result__;
/* 926 */         this.$_ngcc_current_state = 29;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 431:
/* 931 */         this.occurs = (occurs)$__result__;
/* 932 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 446:
/* 937 */         this.term = (ModelGroupImpl)$__result__;
/* 938 */         action7();
/* 939 */         this.$_ngcc_current_state = 28;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 428:
/* 944 */         this.elementTypeName = (UName)$__result__;
/* 945 */         this.$_ngcc_current_state = 13;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 442:
/* 950 */         this.occurs = (occurs)$__result__;
/* 951 */         this.$_ngcc_current_state = 25;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 417:
/* 956 */         this.occurs = (occurs)$__result__;
/* 957 */         this.$_ngcc_current_state = 3;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 422:
/* 962 */         this.anonymousElementDecl = (ElementDecl)$__result__;
/* 963 */         action2();
/* 964 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 437:
/* 969 */         this.annotation = (AnnotationImpl)$__result__;
/* 970 */         this.$_ngcc_current_state = 20;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 977 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\particle.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */