/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.UnionSimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.qname;
/*     */ import com.sun.xml.xsom.impl.parser.state.simpleType;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class SimpleType_Union extends NGCCHandler {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String __text;
/*     */   private UName memberTypeName;
/*     */   private String name;
/*     */   private SimpleTypeImpl anonymousMemberType;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  33 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private UnionSimpleTypeImpl result; private final Vector members; private Locator uloc;
/*     */   public SimpleType_Union(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, String _name) {
/*  37 */     super(source, parent, cookie);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 401 */     this.members = new Vector();
/*     */     this.$runtime = runtime;
/*     */     this.annotation = _annotation;
/*     */     this.locator = _locator;
/*     */     this.name = _name;
/*     */     this.$_ngcc_current_state = 11;
/*     */   }
/*     */   
/*     */   public SimpleType_Union(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, String _name) {
/*     */     this(null, (NGCCEventSource)runtime, runtime, -1, _annotation, _locator, _name);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*     */     this.result = new UnionSimpleTypeImpl(this.$runtime.currentSchema, this.annotation, this.locator, this.name, (this.name == null), (Ref.SimpleType[])this.members.toArray((Object[])new Ref.SimpleType[this.members.size()]));
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*     */     this.members.add(this.anonymousMemberType);
/*     */   }
/*     */   
/*     */   private void action2() throws SAXException {
/*     */     this.members.add(new DelayedRef.SimpleType((PatcherManager)this.$runtime, this.uloc, this.$runtime.currentSchema, this.memberTypeName));
/*     */   }
/*     */   
/*     */   private void action3() throws SAXException {
/*     */     this.$runtime.processList(this.__text);
/*     */   }
/*     */   
/*     */   private void action4() throws SAXException {
/*     */     this.uloc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 4:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*     */           annotation annotation = new annotation(this, this._source, this.$runtime, 658, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/*     */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 2;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 2:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*     */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 655);
/*     */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 1;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*     */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 654);
/*     */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 11:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("union")) {
/*     */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*     */           action4();
/*     */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 6:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "memberTypes")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 4;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
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
/*     */       case 4:
/*     */         this.$_ngcc_current_state = 2;
/*     */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 1:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("union")) {
/*     */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*     */           this.$_ngcc_current_state = 0;
/*     */           action0();
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 6:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "memberTypes")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 4;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
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
/*     */       case 4:
/*     */         this.$_ngcc_current_state = 2;
/*     */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 6:
/*     */         if ($__uri.equals("") && $__local.equals("memberTypes")) {
/*     */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 4;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
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
/*     */       case 4:
/*     */         this.$_ngcc_current_state = 2;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 6:
/*     */         this.$_ngcc_current_state = 4;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 7:
/*     */         if ($__uri.equals("") && $__local.equals("memberTypes")) {
/*     */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     qname qname2;
/*     */     qname qname1;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 4:
/*     */         this.$_ngcc_current_state = 2;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 9:
/*     */         this.__text = $value;
/*     */         this.$_ngcc_current_state = 8;
/*     */         action3();
/*     */         break;
/*     */       case 8:
/*     */         qname2 = new qname(this, this._source, this.$runtime, 661);
/*     */         spawnChildFromText((NGCCEventReceiver)qname2, $value);
/*     */         break;
/*     */       case 6:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "memberTypes")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 4;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 0:
/*     */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */       case 7:
/*     */         qname1 = new qname(this, this._source, this.$runtime, 662);
/*     */         spawnChildFromText((NGCCEventReceiver)qname1, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*     */     switch ($__cookie__) {
/*     */       case 658:
/*     */         this.annotation = (AnnotationImpl)$__result__;
/*     */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */       case 655:
/*     */         this.anonymousMemberType = (SimpleTypeImpl)$__result__;
/*     */         action1();
/*     */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */       case 661:
/*     */         this.memberTypeName = (UName)$__result__;
/*     */         action2();
/*     */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */       case 654:
/*     */         this.anonymousMemberType = (SimpleTypeImpl)$__result__;
/*     */         action1();
/*     */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */       case 662:
/*     */         this.memberTypeName = (UName)$__result__;
/*     */         action2();
/*     */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean accepted() {
/*     */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\SimpleType_Union.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */