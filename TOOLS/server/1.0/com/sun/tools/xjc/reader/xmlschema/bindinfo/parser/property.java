/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.CollectionTypeState;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class property extends NGCCHandler {
/*     */   private String name;
/*     */   private String javadoc;
/*     */   private FieldRendererFactory ct;
/*     */   private String isSetStr;
/*     */   private String baseType;
/*     */   private String isConstStr;
/*     */   private BIConversion conv;
/*     */   private String failFast;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   private Boolean isConst;
/*     */   private Boolean isSet;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  36 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public property(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  40 */     super(source, parent, cookie);
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
/* 646 */     this.isConst = null;
/* 647 */     this.isSet = null;
/*     */     this.$runtime = runtime;
/*     */     this.$_ngcc_current_state = 35; } public BIProperty makeResult() throws SAXException {
/* 650 */     JType baseTypeRef = null;
/* 651 */     if (this.baseType != null) {
/* 652 */       baseTypeRef = this.$runtime.getType(this.baseType);
/*     */     }
/* 654 */     return new BIProperty(this.loc, this.name, this.javadoc, baseTypeRef, this.conv, this.ct, this.isConst, this.isSet);
/*     */   }
/*     */   
/*     */   public property(NGCCRuntimeEx runtime) {
/*     */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*     */     if (this.$runtime.parseBoolean(this.failFast))
/*     */       this.$runtime.reportUnimplementedFeature("generateFailFastSetterMethod"); 
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*     */     this.isSet = this.$runtime.parseBoolean(this.isSetStr) ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */   
/*     */   private void action2() throws SAXException {
/*     */     this.isConst = this.$runtime.parseBoolean(this.isConstStr) ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */   
/*     */   private void action3() throws SAXException {
/*     */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 27:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "baseType")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 23;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 31:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 27;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 10:
/*     */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "baseType") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javadoc")) {
/*     */           spawnChildFromEnterElement((NGCCEventReceiver)new InterleaveFilter_8_3(this, this, 370), $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */       case 15:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "generateIsSetMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 11;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 35:
/*     */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "property") {
/*     */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*     */           action3();
/*     */           this.$_ngcc_current_state = 31;
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 11:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "generateFailFastSetterMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 10;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 23:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "collectionType")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 19;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 19:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "fixedAttributeAsConstantProperty")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 15;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterElement($__qname);
/*     */   }
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 27:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "baseType")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 23;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 31:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 27;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "property") {
/*     */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*     */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 10:
/*     */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "property") {
/*     */           spawnChildFromLeaveElement((NGCCEventReceiver)new InterleaveFilter_8_3(this, this, 370), $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 15:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "generateIsSetMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 11;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 11:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "generateFailFastSetterMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 10;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 23:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "collectionType")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 19;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 19:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "fixedAttributeAsConstantProperty")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 15;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 27:
/*     */         if ($__uri == "" && $__local == "baseType") {
/*     */           this.$_ngcc_current_state = 29;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 23;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 31:
/*     */         if ($__uri == "" && $__local == "name") {
/*     */           this.$_ngcc_current_state = 33;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 27;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 15:
/*     */         if ($__uri == "" && $__local == "generateIsSetMethod") {
/*     */           this.$_ngcc_current_state = 17;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 11;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 11:
/*     */         if ($__uri == "" && $__local == "generateFailFastSetterMethod") {
/*     */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 10;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 23:
/*     */         if ($__uri == "" && $__local == "collectionType") {
/*     */           this.$_ngcc_current_state = 25;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 19;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 19:
/*     */         if ($__uri == "" && $__local == "fixedAttributeAsConstantProperty") {
/*     */           this.$_ngcc_current_state = 21;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 15;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 24:
/*     */         if ($__uri == "" && $__local == "collectionType") {
/*     */           this.$_ngcc_current_state = 19;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 28:
/*     */         if ($__uri == "" && $__local == "baseType") {
/*     */           this.$_ngcc_current_state = 23;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 16:
/*     */         if ($__uri == "" && $__local == "generateIsSetMethod") {
/*     */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 15:
/*     */         this.$_ngcc_current_state = 11;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 32:
/*     */         if ($__uri == "" && $__local == "name") {
/*     */           this.$_ngcc_current_state = 27;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 20:
/*     */         if ($__uri == "" && $__local == "fixedAttributeAsConstantProperty") {
/*     */           this.$_ngcc_current_state = 15;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 27:
/*     */         this.$_ngcc_current_state = 23;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 12:
/*     */         if ($__uri == "" && $__local == "generateFailFastSetterMethod") {
/*     */           this.$_ngcc_current_state = 10;
/*     */           action0();
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 31:
/*     */         this.$_ngcc_current_state = 27;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 11:
/*     */         this.$_ngcc_current_state = 10;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 19:
/*     */         this.$_ngcc_current_state = 15;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 23:
/*     */         this.$_ngcc_current_state = 19;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     CollectionTypeState collectionTypeState;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 13:
/*     */         this.failFast = $value;
/*     */         this.$_ngcc_current_state = 12;
/*     */         break;
/*     */       case 33:
/*     */         this.name = $value;
/*     */         this.$_ngcc_current_state = 32;
/*     */         break;
/*     */       case 0:
/*     */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */       case 15:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "generateIsSetMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 11;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 25:
/*     */         collectionTypeState = new CollectionTypeState(this, this._source, this.$runtime, 387);
/*     */         spawnChildFromText((NGCCEventReceiver)collectionTypeState, $value);
/*     */         break;
/*     */       case 27:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "baseType")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 23;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 21:
/*     */         this.isConstStr = $value;
/*     */         this.$_ngcc_current_state = 20;
/*     */         action2();
/*     */         break;
/*     */       case 31:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 27;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 17:
/*     */         this.isSetStr = $value;
/*     */         this.$_ngcc_current_state = 16;
/*     */         action1();
/*     */         break;
/*     */       case 29:
/*     */         this.baseType = $value;
/*     */         this.$_ngcc_current_state = 28;
/*     */         break;
/*     */       case 11:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "generateFailFastSetterMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 10;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 19:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "fixedAttributeAsConstantProperty")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 15;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 23:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "collectionType")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 19;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*     */     switch ($__cookie__) {
/*     */       case 368:
/*     */         this.javadoc = (String)$__result__;
/*     */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */       case 370:
/*     */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */       case 363:
/*     */         this.conv = (BIConversion)$__result__;
/*     */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */       case 387:
/*     */         this.ct = (FieldRendererFactory)$__result__;
/*     */         this.$_ngcc_current_state = 24;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean accepted() {
/*     */     return (this.$_ngcc_current_state == 3 || this.$_ngcc_current_state == 7 || this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 8 || this.$_ngcc_current_state == 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\property.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */