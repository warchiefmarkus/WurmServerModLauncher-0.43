/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupImpl;
/*     */ import com.sun.xml.xsom.impl.ParticleImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.particle;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class modelGroupBody
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String compositorName;
/*     */   private Locator locator;
/*     */   private ParticleImpl childParticle;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private ModelGroupImpl result; private final Vector particles;
/*     */   public modelGroupBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator, String _compositorName) {
/*  35 */     super(source, parent, cookie);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     this.particles = new Vector();
/*     */     this.$runtime = runtime;
/*     */     this.locator = _locator;
/*     */     this.compositorName = _compositorName;
/*     */     this.$_ngcc_current_state = 4;
/*     */   }
/*     */   
/*     */   public modelGroupBody(NGCCRuntimeEx runtime, Locator _locator, String _compositorName) {
/*     */     this(null, (NGCCEventSource)runtime, runtime, -1, _locator, _compositorName);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*     */     XSModelGroup.Compositor compositor = null;
/*     */     if (this.compositorName.equals("all"))
/*     */       compositor = XSModelGroup.ALL; 
/*     */     if (this.compositorName.equals("sequence"))
/*     */       compositor = XSModelGroup.SEQUENCE; 
/*     */     if (this.compositorName.equals("choice"))
/*     */       compositor = XSModelGroup.CHOICE; 
/*     */     if (compositor == null)
/*     */       throw new InternalError("unable to process " + this.compositorName); 
/*     */     this.result = new ModelGroupImpl(this.$runtime.currentSchema, this.annotation, this.locator, compositor, (XSParticle[])this.particles.toArray((Object[])new XSParticle[0]));
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*     */     this.particles.add(this.childParticle);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 4:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*     */           annotation annotation = new annotation(this, this._source, this.$runtime, 396, null, AnnotationContext.MODELGROUP);
/*     */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 2;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/*     */           particle particle = new particle(this, this._source, this.$runtime, 392);
/*     */           spawnChildFromEnterElement((NGCCEventReceiver)particle, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           action0();
/*     */           this.$_ngcc_current_state = 0;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */       case 2:
/*     */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/*     */           particle particle = new particle(this, this._source, this.$runtime, 393);
/*     */           spawnChildFromEnterElement((NGCCEventReceiver)particle, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 1;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterElement($__qname);
/*     */   }
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 4:
/*     */         this.$_ngcc_current_state = 2;
/*     */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 1:
/*     */         action0();
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
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
/*     */       case 1:
/*     */         action0();
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
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
/*     */       case 1:
/*     */         action0();
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 4:
/*     */         this.$_ngcc_current_state = 2;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 1:
/*     */         action0();
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 0:
/*     */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */       case 2:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*     */     switch ($__cookie__) {
/*     */       case 396:
/*     */         this.annotation = (AnnotationImpl)$__result__;
/*     */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */       case 392:
/*     */         this.childParticle = (ParticleImpl)$__result__;
/*     */         action1();
/*     */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */       case 393:
/*     */         this.childParticle = (ParticleImpl)$__result__;
/*     */         action1();
/*     */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean accepted() {
/*     */     return (this.$_ngcc_current_state == 2 || this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\modelGroupBody.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */