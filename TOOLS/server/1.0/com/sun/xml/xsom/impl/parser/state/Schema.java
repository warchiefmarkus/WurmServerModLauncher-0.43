/*      */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*      */ import com.sun.xml.xsom.XSDeclaration;
/*      */ import com.sun.xml.xsom.XSNotation;
/*      */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.xsom.impl.AttGroupDeclImpl;
/*      */ import com.sun.xml.xsom.impl.ComplexTypeImpl;
/*      */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*      */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*      */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*      */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*      */ import com.sun.xml.xsom.impl.parser.state.attributeDeclBody;
/*      */ import com.sun.xml.xsom.impl.parser.state.attributeGroupDecl;
/*      */ import com.sun.xml.xsom.impl.parser.state.complexType;
/*      */ import com.sun.xml.xsom.impl.parser.state.elementDeclBody;
/*      */ import com.sun.xml.xsom.impl.parser.state.erSet;
/*      */ import com.sun.xml.xsom.impl.parser.state.ersSet;
/*      */ import com.sun.xml.xsom.impl.parser.state.group;
/*      */ import com.sun.xml.xsom.impl.parser.state.importDecl;
/*      */ import com.sun.xml.xsom.impl.parser.state.includeDecl;
/*      */ import com.sun.xml.xsom.impl.parser.state.notation;
/*      */ import com.sun.xml.xsom.impl.parser.state.qualification;
/*      */ import com.sun.xml.xsom.impl.parser.state.redefine;
/*      */ import com.sun.xml.xsom.impl.parser.state.simpleType;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public class Schema extends NGCCHandler {
/*      */   private Integer finalDefault;
/*      */   private boolean efd;
/*      */   private boolean afd;
/*      */   private Integer blockDefault;
/*      */   private boolean includeMode;
/*      */   private AnnotationImpl anno;
/*      */   private ComplexTypeImpl ct;
/*      */   private ElementDecl e;
/*      */   private String defaultValue;
/*      */   private XSNotation notation;
/*      */   private AttGroupDeclImpl ag;
/*      */   
/*      */   public final NGCCRuntime getRuntime() {
/*   44 */     return (NGCCRuntime)this.$runtime;
/*      */   }
/*      */   private String fixedValue; private ModelGroupDeclImpl group; private AttributeDeclImpl ad; private SimpleTypeImpl st; private String expectedNamespace; protected final NGCCRuntimeEx $runtime; private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private String tns; private Locator locator;
/*      */   public Schema(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, boolean _includeMode, String _expectedNamespace) {
/*   48 */     super(source, parent, cookie);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1251 */     this.tns = null;
/*      */     this.$runtime = runtime;
/*      */     this.includeMode = _includeMode;
/*      */     this.expectedNamespace = _expectedNamespace;
/*      */     this.$_ngcc_current_state = 56;
/*      */   }
/*      */   
/*      */   public Schema(NGCCRuntimeEx runtime, boolean _includeMode, String _expectedNamespace) {
/*      */     this(null, (NGCCEventSource)runtime, runtime, -1, _includeMode, _expectedNamespace);
/*      */   }
/*      */   
/*      */   private void action0() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getAttGroupDecl(this.ag.getName()));
/*      */     this.$runtime.currentSchema.addAttGroupDecl((XSAttGroupDecl)this.ag);
/*      */   }
/*      */   
/*      */   private void action1() throws SAXException {
/*      */     this.$runtime.currentSchema.addNotation(this.notation);
/*      */   }
/*      */   
/*      */   private void action2() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getModelGroupDecl(this.group.getName()));
/*      */     this.$runtime.currentSchema.addModelGroupDecl((XSModelGroupDecl)this.group);
/*      */   }
/*      */   
/*      */   private void action3() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getAttributeDecl(this.ad.getName()));
/*      */     this.$runtime.currentSchema.addAttributeDecl((XSAttributeDecl)this.ad);
/*      */   }
/*      */   
/*      */   private void action4() throws SAXException {
/*      */     this.locator = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   private void action5() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getType(this.ct.getName()));
/*      */     this.$runtime.currentSchema.addComplexType((XSComplexType)this.ct);
/*      */   }
/*      */   
/*      */   private void action6() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getType(this.st.getName()));
/*      */     this.$runtime.currentSchema.addSimpleType((XSSimpleType)this.st);
/*      */   }
/*      */   
/*      */   private void action7() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getElementDecl(this.e.getName()));
/*      */     this.$runtime.currentSchema.addElementDecl((XSElementDecl)this.e);
/*      */   }
/*      */   
/*      */   private void action8() throws SAXException {
/*      */     this.locator = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   private void action9() throws SAXException {
/*      */     this.$runtime.currentSchema.setAnnotation((XSAnnotation)this.anno);
/*      */   }
/*      */   
/*      */   private void action10() throws SAXException {
/*      */     this.$runtime.finalDefault = this.finalDefault.intValue();
/*      */   }
/*      */   
/*      */   private void action11() throws SAXException {
/*      */     this.$runtime.blockDefault = this.blockDefault.intValue();
/*      */   }
/*      */   
/*      */   private void action12() throws SAXException {
/*      */     this.$runtime.elementFormDefault = this.efd;
/*      */   }
/*      */   
/*      */   private void action13() throws SAXException {
/*      */     this.$runtime.attributeFormDefault = this.afd;
/*      */   }
/*      */   
/*      */   private void action14() throws SAXException {
/*      */     Attributes test = this.$runtime.getCurrentAttributes();
/*      */     String tns = test.getValue("targetNamespace");
/*      */     if (!this.includeMode) {
/*      */       if (tns == null)
/*      */         tns = ""; 
/*      */       this.$runtime.currentSchema = this.$runtime.parser.schemaSet.createSchema(tns, this.$runtime.copyLocator());
/*      */       if (this.expectedNamespace != null && !this.expectedNamespace.equals(tns))
/*      */         this.$runtime.reportError(Messages.format("UnexpectedTargetnamespace.Import", tns, this.expectedNamespace, tns), this.$runtime.getLocator()); 
/*      */     } else {
/*      */       if (tns != null && this.expectedNamespace != null && !this.expectedNamespace.equals(tns))
/*      */         this.$runtime.reportError(Messages.format("UnexpectedTargetnamespace.Include", tns, this.expectedNamespace, tns), this.$runtime.getLocator()); 
/*      */       if (tns == null)
/*      */         this.$runtime.chameleonMode = true; 
/*      */     } 
/*      */     if (this.$runtime.hasAlreadyBeenRead(this.$runtime.currentSchema.getTargetNamespace())) {
/*      */       this.$runtime.redirectSubtree(new DefaultHandler(), "", "", "");
/*      */       return;
/*      */     } 
/*      */     this.anno = (AnnotationImpl)this.$runtime.currentSchema.getAnnotation();
/*      */     this.$runtime.blockDefault = 0;
/*      */     this.$runtime.finalDefault = 0;
/*      */   }
/*      */   
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*      */     int $ai;
/*      */     this.$uri = $__uri;
/*      */     this.$localName = $__local;
/*      */     this.$qname = $__qname;
/*      */     switch (this.$_ngcc_current_state) {
/*      */       case 2:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*      */           annotation annotation = new annotation(this, this._source, this.$runtime, 103, this.anno, AnnotationContext.SCHEMA);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/*      */           includeDecl includeDecl = new includeDecl(this, this._source, this.$runtime, 104);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)includeDecl, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/*      */           importDecl importDecl = new importDecl(this, this._source, this.$runtime, 105);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)importDecl, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/*      */           redefine redefine = new redefine(this, this._source, this.$runtime, 106);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)redefine, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action8();
/*      */           this.$_ngcc_current_state = 27;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*      */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 108);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*      */           complexType complexType = new complexType(this, this._source, this.$runtime, 109);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)complexType, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action4();
/*      */           this.$_ngcc_current_state = 16;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/*      */           group group = new group(this, this._source, this.$runtime, 111);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)group, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/*      */           notation notation = new notation(this, this._source, this.$runtime, 112);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)notation, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/*      */           attributeGroupDecl attributeGroupDecl = new attributeGroupDecl(this, this._source, this.$runtime, 113);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)attributeGroupDecl, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 1;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */       case 11:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*      */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 9, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)attributeDeclBody, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 40:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 36;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 1:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*      */           annotation annotation = new annotation(this, this._source, this.$runtime, 92, this.anno, AnnotationContext.SCHEMA);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/*      */           includeDecl includeDecl = new includeDecl(this, this._source, this.$runtime, 93);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)includeDecl, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/*      */           importDecl importDecl = new importDecl(this, this._source, this.$runtime, 94);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)importDecl, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/*      */           redefine redefine = new redefine(this, this._source, this.$runtime, 95);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)redefine, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action8();
/*      */           this.$_ngcc_current_state = 27;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*      */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 97);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*      */           complexType complexType = new complexType(this, this._source, this.$runtime, 98);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)complexType, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action4();
/*      */           this.$_ngcc_current_state = 16;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/*      */           group group = new group(this, this._source, this.$runtime, 100);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)group, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/*      */           notation notation = new notation(this, this._source, this.$runtime, 101);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)notation, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/*      */           attributeGroupDecl attributeGroupDecl = new attributeGroupDecl(this, this._source, this.$runtime, 102);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)attributeGroupDecl, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 36:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 2;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 52:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 48;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 12:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 11;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 27:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromEnterElement((NGCCEventReceiver)elementDeclBody, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 48:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 44;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 44:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 40;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 16:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 12;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 56:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("schema")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action14();
/*      */           this.$_ngcc_current_state = 52;
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */     unexpectedEnterElement($__qname);
/*      */   }
/*      */   
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     int $ai;
/*      */     this.$uri = $__uri;
/*      */     this.$localName = $__local;
/*      */     this.$qname = $__qname;
/*      */     switch (this.$_ngcc_current_state) {
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 11:
/*      */         if ((($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*      */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 9, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromLeaveElement((NGCCEventReceiver)attributeDeclBody, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 40:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 36;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 1:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("schema")) {
/*      */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*      */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 36:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 2;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 52:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 48;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 12:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 11;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 27:
/*      */         if ((($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromLeaveElement((NGCCEventReceiver)elementDeclBody, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 48:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 44;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 10:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/*      */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*      */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 26:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/*      */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*      */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 44:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 40;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 16:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 12;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */     unexpectedLeaveElement($__qname);
/*      */   }
/*      */   
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     this.$uri = $__uri;
/*      */     this.$localName = $__local;
/*      */     this.$qname = $__qname;
/*      */     switch (this.$_ngcc_current_state) {
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 12:
/*      */         if ($__uri.equals("") && $__local.equals("fixed")) {
/*      */           this.$_ngcc_current_state = 14;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 11;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 52:
/*      */         if ($__uri.equals("") && $__local.equals("targetNamespace")) {
/*      */           this.$_ngcc_current_state = 54;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 48;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 27:
/*      */         if (($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("abstract")) || ($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("fixed"))) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromEnterAttribute((NGCCEventReceiver)elementDeclBody, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 11:
/*      */         if (($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("name"))) {
/*      */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 9, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromEnterAttribute((NGCCEventReceiver)attributeDeclBody, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 40:
/*      */         if ($__uri.equals("") && $__local.equals("blockDefault")) {
/*      */           this.$_ngcc_current_state = 42;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 36;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 48:
/*      */         if ($__uri.equals("") && $__local.equals("attributeFormDefault")) {
/*      */           this.$_ngcc_current_state = 50;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 44;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 44:
/*      */         if ($__uri.equals("") && $__local.equals("elementFormDefault")) {
/*      */           this.$_ngcc_current_state = 46;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 40;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 16:
/*      */         if ($__uri.equals("") && $__local.equals("default")) {
/*      */           this.$_ngcc_current_state = 18;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 12;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 36:
/*      */         if ($__uri.equals("") && $__local.equals("finalDefault")) {
/*      */           this.$_ngcc_current_state = 38;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 2;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */     unexpectedEnterAttribute($__qname);
/*      */   }
/*      */   
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     this.$uri = $__uri;
/*      */     this.$localName = $__local;
/*      */     this.$qname = $__qname;
/*      */     switch (this.$_ngcc_current_state) {
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 53:
/*      */         if ($__uri.equals("") && $__local.equals("targetNamespace")) {
/*      */           this.$_ngcc_current_state = 48;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 40:
/*      */         this.$_ngcc_current_state = 36;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 17:
/*      */         if ($__uri.equals("") && $__local.equals("default")) {
/*      */           this.$_ngcc_current_state = 12;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 49:
/*      */         if ($__uri.equals("") && $__local.equals("attributeFormDefault")) {
/*      */           this.$_ngcc_current_state = 44;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 37:
/*      */         if ($__uri.equals("") && $__local.equals("finalDefault")) {
/*      */           this.$_ngcc_current_state = 2;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 36:
/*      */         this.$_ngcc_current_state = 2;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 41:
/*      */         if ($__uri.equals("") && $__local.equals("blockDefault")) {
/*      */           this.$_ngcc_current_state = 36;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 52:
/*      */         this.$_ngcc_current_state = 48;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 12:
/*      */         this.$_ngcc_current_state = 11;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 45:
/*      */         if ($__uri.equals("") && $__local.equals("elementFormDefault")) {
/*      */           this.$_ngcc_current_state = 40;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 48:
/*      */         this.$_ngcc_current_state = 44;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 44:
/*      */         this.$_ngcc_current_state = 40;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 13:
/*      */         if ($__uri.equals("") && $__local.equals("fixed")) {
/*      */           this.$_ngcc_current_state = 11;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 16:
/*      */         this.$_ngcc_current_state = 12;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */     } 
/*      */     unexpectedLeaveAttribute($__qname);
/*      */   }
/*      */   
/*      */   public void text(String $value) throws SAXException {
/*      */     int $ai;
/*      */     erSet erSet;
/*      */     ersSet ersSet;
/*      */     switch (this.$_ngcc_current_state) {
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 0:
/*      */         revertToParentFromText(this, this._cookie, $value);
/*      */         break;
/*      */       case 38:
/*      */         erSet = new erSet(this, this._source, this.$runtime, 116);
/*      */         spawnChildFromText((NGCCEventReceiver)erSet, $value);
/*      */         break;
/*      */       case 11:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*      */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 9, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromText((NGCCEventReceiver)attributeDeclBody, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*      */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 9, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromText((NGCCEventReceiver)attributeDeclBody, $value);
/*      */         } 
/*      */         break;
/*      */       case 54:
/*      */         this.$_ngcc_current_state = 53;
/*      */         break;
/*      */       case 40:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 36;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 36:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 2;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 42:
/*      */         ersSet = new ersSet(this, this._source, this.$runtime, 121);
/*      */         spawnChildFromText((NGCCEventReceiver)ersSet, $value);
/*      */         break;
/*      */       case 52:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 48;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 50:
/*      */         if ($value.equals("unqualified")) {
/*      */           qualification qualification = new qualification(this, this._source, this.$runtime, 131);
/*      */           spawnChildFromText((NGCCEventReceiver)qualification, $value);
/*      */           break;
/*      */         } 
/*      */         if ($value.equals("qualified")) {
/*      */           qualification qualification = new qualification(this, this._source, this.$runtime, 131);
/*      */           spawnChildFromText((NGCCEventReceiver)qualification, $value);
/*      */         } 
/*      */         break;
/*      */       case 12:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 11;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 18:
/*      */         this.defaultValue = $value;
/*      */         this.$_ngcc_current_state = 17;
/*      */         break;
/*      */       case 46:
/*      */         if ($value.equals("unqualified")) {
/*      */           qualification qualification = new qualification(this, this._source, this.$runtime, 126);
/*      */           spawnChildFromText((NGCCEventReceiver)qualification, $value);
/*      */           break;
/*      */         } 
/*      */         if ($value.equals("qualified")) {
/*      */           qualification qualification = new qualification(this, this._source, this.$runtime, 126);
/*      */           spawnChildFromText((NGCCEventReceiver)qualification, $value);
/*      */         } 
/*      */         break;
/*      */       case 27:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*      */           elementDeclBody elementDeclBody = new elementDeclBody(this, this._source, this.$runtime, 27, this.locator, true);
/*      */           spawnChildFromText((NGCCEventReceiver)elementDeclBody, $value);
/*      */         } 
/*      */         break;
/*      */       case 48:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 44;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 14:
/*      */         this.fixedValue = $value;
/*      */         this.$_ngcc_current_state = 13;
/*      */         break;
/*      */       case 44:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 40;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 16:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 12;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*      */     switch ($__cookie__) {
/*      */       case 9:
/*      */         this.ad = (AttributeDeclImpl)$__result__;
/*      */         action3();
/*      */         this.$_ngcc_current_state = 10;
/*      */         break;
/*      */       case 131:
/*      */         this.afd = ((Boolean)$__result__).booleanValue();
/*      */         action13();
/*      */         this.$_ngcc_current_state = 49;
/*      */         break;
/*      */       case 27:
/*      */         this.e = (ElementDecl)$__result__;
/*      */         action7();
/*      */         this.$_ngcc_current_state = 26;
/*      */         break;
/*      */       case 116:
/*      */         this.finalDefault = (Integer)$__result__;
/*      */         action10();
/*      */         this.$_ngcc_current_state = 37;
/*      */         break;
/*      */       case 92:
/*      */         this.anno = (AnnotationImpl)$__result__;
/*      */         action9();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 93:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 94:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 95:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 97:
/*      */         this.st = (SimpleTypeImpl)$__result__;
/*      */         action6();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 98:
/*      */         this.ct = (ComplexTypeImpl)$__result__;
/*      */         action5();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 100:
/*      */         this.group = (ModelGroupDeclImpl)$__result__;
/*      */         action2();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 101:
/*      */         this.notation = (XSNotation)$__result__;
/*      */         action1();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 102:
/*      */         this.ag = (AttGroupDeclImpl)$__result__;
/*      */         action0();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 103:
/*      */         this.anno = (AnnotationImpl)$__result__;
/*      */         action9();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 104:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 105:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 106:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 108:
/*      */         this.st = (SimpleTypeImpl)$__result__;
/*      */         action6();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 109:
/*      */         this.ct = (ComplexTypeImpl)$__result__;
/*      */         action5();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 111:
/*      */         this.group = (ModelGroupDeclImpl)$__result__;
/*      */         action2();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 112:
/*      */         this.notation = (XSNotation)$__result__;
/*      */         action1();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 113:
/*      */         this.ag = (AttGroupDeclImpl)$__result__;
/*      */         action0();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 121:
/*      */         this.blockDefault = (Integer)$__result__;
/*      */         action11();
/*      */         this.$_ngcc_current_state = 41;
/*      */         break;
/*      */       case 126:
/*      */         this.efd = ((Boolean)$__result__).booleanValue();
/*      */         action12();
/*      */         this.$_ngcc_current_state = 45;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean accepted() {
/*      */     return (this.$_ngcc_current_state == 0);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\Schema.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */