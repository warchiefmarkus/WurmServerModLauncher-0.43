/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.xsom.impl.ContentTypeImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.attributeUses;
/*     */ import com.sun.xml.xsom.impl.parser.state.particle;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*  27 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public complexType_complexContent_body(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AttributesHolder _owner) {
/*  31 */     super(source, parent, cookie);
/*  32 */     this.$runtime = runtime;
/*  33 */     this.owner = _owner;
/*  34 */     this.$_ngcc_current_state = 2;
/*     */   }
/*     */   
/*     */   public complexType_complexContent_body(NGCCRuntimeEx runtime, AttributesHolder _owner) {
/*  38 */     this(null, (NGCCEventSource)runtime, runtime, -1, _owner);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  42 */     if (this.particle == null) {
/*  43 */       this.particle = (ContentTypeImpl)this.$runtime.parser.schemaSet.empty;
/*     */     }
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  48 */     this.$uri = $__uri;
/*  49 */     this.$localName = $__local;
/*  50 */     this.$qname = $__qname;
/*  51 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  54 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/*  55 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 245, this.owner);
/*  56 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  59 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 245, this.owner);
/*  60 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  66 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/*  67 */           particle particle = new particle(this, this._source, this.$runtime, 247);
/*  68 */           spawnChildFromEnterElement((NGCCEventReceiver)particle, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  71 */           this.$_ngcc_current_state = 1;
/*  72 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  78 */         revertToParentFromEnterElement(this.particle, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  83 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     attributeUses attributeUses;
/*  91 */     this.$uri = $__uri;
/*  92 */     this.$localName = $__local;
/*  93 */     this.$qname = $__qname;
/*  94 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  97 */         attributeUses = new attributeUses(this, this._source, this.$runtime, 245, this.owner);
/*  98 */         spawnChildFromLeaveElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 103 */         this.$_ngcc_current_state = 1;
/* 104 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 109 */         revertToParentFromLeaveElement(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 114 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     attributeUses attributeUses;
/* 122 */     this.$uri = $__uri;
/* 123 */     this.$localName = $__local;
/* 124 */     this.$qname = $__qname;
/* 125 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 128 */         attributeUses = new attributeUses(this, this._source, this.$runtime, 245, this.owner);
/* 129 */         spawnChildFromEnterAttribute((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 134 */         this.$_ngcc_current_state = 1;
/* 135 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 140 */         revertToParentFromEnterAttribute(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 145 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     attributeUses attributeUses;
/* 153 */     this.$uri = $__uri;
/* 154 */     this.$localName = $__local;
/* 155 */     this.$qname = $__qname;
/* 156 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 159 */         attributeUses = new attributeUses(this, this._source, this.$runtime, 245, this.owner);
/* 160 */         spawnChildFromLeaveAttribute((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 165 */         this.$_ngcc_current_state = 1;
/* 166 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 171 */         revertToParentFromLeaveAttribute(this.particle, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 176 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     attributeUses attributeUses;
/* 184 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 187 */         attributeUses = new attributeUses(this, this._source, this.$runtime, 245, this.owner);
/* 188 */         spawnChildFromText((NGCCEventReceiver)attributeUses, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 193 */         this.$_ngcc_current_state = 1;
/* 194 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 199 */         revertToParentFromText(this.particle, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 206 */     switch ($__cookie__) {
/*     */       
/*     */       case 245:
/* 209 */         action0();
/* 210 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 247:
/* 215 */         this.particle = (ContentTypeImpl)$__result__;
/* 216 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 223 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\complexType_complexContent_body.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */