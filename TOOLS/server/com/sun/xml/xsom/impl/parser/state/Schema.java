/*      */ package com.sun.xml.xsom.impl.parser.state;
/*      */ 
/*      */ import com.sun.xml.xsom.XSAnnotation;
/*      */ import com.sun.xml.xsom.XSAttGroupDecl;
/*      */ import com.sun.xml.xsom.XSAttributeDecl;
/*      */ import com.sun.xml.xsom.XSComplexType;
/*      */ import com.sun.xml.xsom.XSDeclaration;
/*      */ import com.sun.xml.xsom.XSElementDecl;
/*      */ import com.sun.xml.xsom.XSModelGroupDecl;
/*      */ import com.sun.xml.xsom.XSNotation;
/*      */ import com.sun.xml.xsom.XSSimpleType;
/*      */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.xsom.impl.AttGroupDeclImpl;
/*      */ import com.sun.xml.xsom.impl.AttributeDeclImpl;
/*      */ import com.sun.xml.xsom.impl.ComplexTypeImpl;
/*      */ import com.sun.xml.xsom.impl.ElementDecl;
/*      */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*      */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*      */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*      */ import com.sun.xml.xsom.impl.parser.Messages;
/*      */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.xsom.parser.AnnotationContext;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ 
/*      */ 
/*      */ public class Schema
/*      */   extends NGCCHandler
/*      */ {
/*      */   private Integer finalDefault;
/*      */   private boolean efd;
/*      */   private boolean afd;
/*      */   private Integer blockDefault;
/*      */   private ForeignAttributesImpl fa;
/*      */   private boolean includeMode;
/*      */   private AnnotationImpl anno;
/*      */   private ComplexTypeImpl ct;
/*      */   private ElementDecl e;
/*      */   private String defaultValue;
/*      */   private XSNotation notation;
/*      */   private AttGroupDeclImpl ag;
/*      */   
/*      */   public final NGCCRuntime getRuntime() {
/*   46 */     return (NGCCRuntime)this.$runtime;
/*      */   }
/*      */   private String fixedValue; private ModelGroupDeclImpl group; private AttributeDeclImpl ad; private SimpleTypeImpl st; private String expectedNamespace; protected final NGCCRuntimeEx $runtime; private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private String tns; private Locator locator;
/*      */   public Schema(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, boolean _includeMode, String _expectedNamespace) {
/*   50 */     super(source, parent, cookie);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1287 */     this.tns = null;
/*      */     this.$runtime = runtime;
/*      */     this.includeMode = _includeMode;
/*      */     this.expectedNamespace = _expectedNamespace;
/*      */     this.$_ngcc_current_state = 57;
/*      */   }
/*      */   
/*      */   public Schema(NGCCRuntimeEx runtime, boolean _includeMode, String _expectedNamespace) {
/*      */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _includeMode, _expectedNamespace);
/*      */   }
/*      */   
/*      */   private void action0() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getAttGroupDecl(this.ag.getName()));
/*      */     this.$runtime.currentSchema.addAttGroupDecl((XSAttGroupDecl)this.ag, false);
/*      */   }
/*      */   
/*      */   private void action1() throws SAXException {
/*      */     this.$runtime.currentSchema.addNotation(this.notation);
/*      */   }
/*      */   
/*      */   private void action2() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getModelGroupDecl(this.group.getName()));
/*      */     this.$runtime.currentSchema.addModelGroupDecl((XSModelGroupDecl)this.group, false);
/*      */   }
/*      */   
/*      */   private void action3() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getAttributeDecl(this.ad.getName()));
/*      */     this.$runtime.currentSchema.addAttributeDecl((XSAttributeDecl)this.ad);
/*      */   }
/*      */   
/*      */   private void action4() throws SAXException {
/*      */     this.locator = this.$runtime.copyLocator();
/*      */     this.defaultValue = null;
/*      */     this.fixedValue = null;
/*      */   }
/*      */   
/*      */   private void action5() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getType(this.ct.getName()));
/*      */     this.$runtime.currentSchema.addComplexType((XSComplexType)this.ct, false);
/*      */   }
/*      */   
/*      */   private void action6() throws SAXException {
/*      */     this.$runtime.checkDoubleDefError((XSDeclaration)this.$runtime.currentSchema.getType(this.st.getName()));
/*      */     this.$runtime.currentSchema.addSimpleType((XSSimpleType)this.st, false);
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
/*      */     this.$runtime.currentSchema.addForeignAttributes(this.fa);
/*      */   }
/*      */   
/*      */   private void action11() throws SAXException {
/*      */     this.$runtime.finalDefault = this.finalDefault.intValue();
/*      */   }
/*      */   
/*      */   private void action12() throws SAXException {
/*      */     this.$runtime.blockDefault = this.blockDefault.intValue();
/*      */   }
/*      */   
/*      */   private void action13() throws SAXException {
/*      */     this.$runtime.elementFormDefault = this.efd;
/*      */   }
/*      */   
/*      */   private void action14() throws SAXException {
/*      */     this.$runtime.attributeFormDefault = this.afd;
/*      */   }
/*      */   
/*      */   private void action15() throws SAXException {
/*      */     Attributes test = this.$runtime.getCurrentAttributes();
/*      */     String tns = test.getValue("targetNamespace");
/*      */     if (!this.includeMode) {
/*      */       if (tns == null)
/*      */         tns = ""; 
/*      */       this.$runtime.currentSchema = this.$runtime.parser.schemaSet.createSchema(tns, this.$runtime.copyLocator());
/*      */       if (this.expectedNamespace != null && !this.expectedNamespace.equals(tns))
/*      */         this.$runtime.reportError(Messages.format("UnexpectedTargetnamespace.Import", new Object[] { tns, this.expectedNamespace, tns }), this.$runtime.getLocator()); 
/*      */     } else {
/*      */       if (tns != null && this.expectedNamespace != null && !this.expectedNamespace.equals(tns))
/*      */         this.$runtime.reportError(Messages.format("UnexpectedTargetnamespace.Include", new Object[] { tns, this.expectedNamespace, tns })); 
/*      */       if (tns == null)
/*      */         this.$runtime.chameleonMode = true; 
/*      */     } 
/*      */     if (this.$runtime.hasAlreadyBeenRead()) {
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
/*      */       case 16:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 12;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 11:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*      */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 24, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 36:
/*      */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*      */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 130, null);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 27:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*      */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 45:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 41;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 41:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 37;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 37:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 36;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 49:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 45;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 53:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 49;
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
/*      */       case 2:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*      */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 118, this.anno, AnnotationContext.SCHEMA);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/*      */           NGCCHandler h = new includeDecl(this, this._source, this.$runtime, 119);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/*      */           NGCCHandler h = new importDecl(this, this._source, this.$runtime, 120);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/*      */           NGCCHandler h = new redefine(this, this._source, this.$runtime, 121);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action8();
/*      */           this.$_ngcc_current_state = 27;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*      */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 123);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*      */           NGCCHandler h = new complexType(this, this._source, this.$runtime, 124);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action4();
/*      */           this.$_ngcc_current_state = 16;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/*      */           NGCCHandler h = new group(this, this._source, this.$runtime, 126);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/*      */           NGCCHandler h = new notation(this, this._source, this.$runtime, 127);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/*      */           NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 128);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 1;
/*      */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */       case 57:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("schema")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action15();
/*      */           this.$_ngcc_current_state = 53;
/*      */         } else {
/*      */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 1:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*      */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 107, this.anno, AnnotationContext.SCHEMA);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/*      */           NGCCHandler h = new includeDecl(this, this._source, this.$runtime, 108);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/*      */           NGCCHandler h = new importDecl(this, this._source, this.$runtime, 109);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("redefine")) {
/*      */           NGCCHandler h = new redefine(this, this._source, this.$runtime, 110);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action8();
/*      */           this.$_ngcc_current_state = 27;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*      */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 112);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*      */           NGCCHandler h = new complexType(this, this._source, this.$runtime, 113);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/*      */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*      */           action4();
/*      */           this.$_ngcc_current_state = 16;
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/*      */           NGCCHandler h = new group(this, this._source, this.$runtime, 115);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/*      */           NGCCHandler h = new notation(this, this._source, this.$runtime, 116);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/*      */           NGCCHandler h = new attributeGroupDecl(this, this._source, this.$runtime, 117);
/*      */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
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
/*      */       case 16:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 12;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 11:
/*      */         if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*      */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 24, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 36:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("schema")) {
/*      */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 130, null);
/*      */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
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
/*      */       case 27:
/*      */         if ((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/*      */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */       case 45:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 41;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 41:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 37;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 37:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 36;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 49:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 45;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 53:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 49;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
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
/*      */       case 12:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           this.$_ngcc_current_state = 11;
/*      */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 1:
/*      */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("schema")) {
/*      */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*      */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           unexpectedLeaveElement($__qname);
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
/*      */       case 37:
/*      */         if ($__uri.equals("") && $__local.equals("finalDefault")) {
/*      */           this.$_ngcc_current_state = 39;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 36;
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
/*      */       case 11:
/*      */         if (($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("form"))) {
/*      */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 24, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 53:
/*      */         if ($__uri.equals("") && $__local.equals("targetNamespace")) {
/*      */           this.$_ngcc_current_state = 55;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 49;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 49:
/*      */         if ($__uri.equals("") && $__local.equals("attributeFormDefault")) {
/*      */           this.$_ngcc_current_state = 51;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 45;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 12:
/*      */         if ($__uri.equals("") && $__local.equals("fixed")) {
/*      */           this.$_ngcc_current_state = 14;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 11;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 27:
/*      */         if (($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("fixed")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("abstract")) || ($__uri.equals("") && $__local.equals("name"))) {
/*      */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 45:
/*      */         if ($__uri.equals("") && $__local.equals("elementFormDefault")) {
/*      */           this.$_ngcc_current_state = 47;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 41;
/*      */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */       case 41:
/*      */         if ($__uri.equals("") && $__local.equals("blockDefault")) {
/*      */           this.$_ngcc_current_state = 43;
/*      */         } else {
/*      */           this.$_ngcc_current_state = 37;
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
/*      */       case 16:
/*      */         this.$_ngcc_current_state = 12;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 17:
/*      */         if ($__uri.equals("") && $__local.equals("default")) {
/*      */           this.$_ngcc_current_state = 12;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 45:
/*      */         this.$_ngcc_current_state = 41;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 13:
/*      */         if ($__uri.equals("") && $__local.equals("fixed")) {
/*      */           this.$_ngcc_current_state = 11;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 41:
/*      */         this.$_ngcc_current_state = 37;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 46:
/*      */         if ($__uri.equals("") && $__local.equals("elementFormDefault")) {
/*      */           this.$_ngcc_current_state = 41;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 37:
/*      */         this.$_ngcc_current_state = 36;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 54:
/*      */         if ($__uri.equals("") && $__local.equals("targetNamespace")) {
/*      */           this.$_ngcc_current_state = 49;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 49:
/*      */         this.$_ngcc_current_state = 45;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 53:
/*      */         this.$_ngcc_current_state = 49;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 42:
/*      */         if ($__uri.equals("") && $__local.equals("blockDefault")) {
/*      */           this.$_ngcc_current_state = 37;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 38:
/*      */         if ($__uri.equals("") && $__local.equals("finalDefault")) {
/*      */           this.$_ngcc_current_state = 36;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 12:
/*      */         this.$_ngcc_current_state = 11;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */       case 50:
/*      */         if ($__uri.equals("") && $__local.equals("attributeFormDefault")) {
/*      */           this.$_ngcc_current_state = 45;
/*      */         } else {
/*      */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */       case 0:
/*      */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */     } 
/*      */     unexpectedLeaveAttribute($__qname);
/*      */   }
/*      */   
/*      */   public void text(String $value) throws SAXException {
/*      */     int $ai;
/*      */     NGCCHandler h;
/*      */     switch (this.$_ngcc_current_state) {
/*      */       case 16:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 12;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 47:
/*      */         if ($value.equals("unqualified")) {
/*      */           NGCCHandler nGCCHandler = new qualification(this, this._source, this.$runtime, 142);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if ($value.equals("qualified")) {
/*      */           NGCCHandler nGCCHandler = new qualification(this, this._source, this.$runtime, 142);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */         } 
/*      */         break;
/*      */       case 11:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new attributeDeclBody(this, this._source, this.$runtime, 24, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new attributeDeclBody(this, this._source, this.$runtime, 24, this.locator, false, this.defaultValue, this.fixedValue);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */         } 
/*      */         break;
/*      */       case 14:
/*      */         this.fixedValue = $value;
/*      */         this.$_ngcc_current_state = 13;
/*      */         break;
/*      */       case 27:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */           break;
/*      */         } 
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*      */           NGCCHandler nGCCHandler = new elementDeclBody(this, this._source, this.$runtime, 42, this.locator, true);
/*      */           spawnChildFromText(nGCCHandler, $value);
/*      */         } 
/*      */         break;
/*      */       case 45:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "elementFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 41;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 41:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "blockDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 37;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 37:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "finalDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 36;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 43:
/*      */         h = new ersSet(this, this._source, this.$runtime, 137);
/*      */         spawnChildFromText(h, $value);
/*      */         break;
/*      */       case 49:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "attributeFormDefault")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 45;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 53:
/*      */         if (($ai = this.$runtime.getAttributeIndex("", "targetNamespace")) >= 0) {
/*      */           this.$runtime.consumeAttribute($ai);
/*      */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*      */         this.$_ngcc_current_state = 49;
/*      */         this.$runtime.sendText(this._cookie, $value);
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
/*      */       case 51:
/*      */         if ($value.equals("unqualified")) {
/*      */           h = new qualification(this, this._source, this.$runtime, 147);
/*      */           spawnChildFromText(h, $value);
/*      */           break;
/*      */         } 
/*      */         if ($value.equals("qualified")) {
/*      */           h = new qualification(this, this._source, this.$runtime, 147);
/*      */           spawnChildFromText(h, $value);
/*      */         } 
/*      */         break;
/*      */       case 18:
/*      */         this.defaultValue = $value;
/*      */         this.$_ngcc_current_state = 17;
/*      */         break;
/*      */       case 2:
/*      */         this.$_ngcc_current_state = 1;
/*      */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */       case 0:
/*      */         revertToParentFromText(this, this._cookie, $value);
/*      */         break;
/*      */       case 39:
/*      */         h = new erSet(this, this._source, this.$runtime, 132);
/*      */         spawnChildFromText(h, $value);
/*      */         break;
/*      */       case 55:
/*      */         this.$_ngcc_current_state = 54;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*      */     switch ($__cookie__) {
/*      */       case 24:
/*      */         this.ad = (AttributeDeclImpl)$__result__;
/*      */         action3();
/*      */         this.$_ngcc_current_state = 10;
/*      */         break;
/*      */       case 130:
/*      */         this.fa = (ForeignAttributesImpl)$__result__;
/*      */         action10();
/*      */         this.$_ngcc_current_state = 2;
/*      */         break;
/*      */       case 137:
/*      */         this.blockDefault = (Integer)$__result__;
/*      */         action12();
/*      */         this.$_ngcc_current_state = 42;
/*      */         break;
/*      */       case 132:
/*      */         this.finalDefault = (Integer)$__result__;
/*      */         action11();
/*      */         this.$_ngcc_current_state = 38;
/*      */         break;
/*      */       case 107:
/*      */         this.anno = (AnnotationImpl)$__result__;
/*      */         action9();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 108:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 109:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 110:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 112:
/*      */         this.st = (SimpleTypeImpl)$__result__;
/*      */         action6();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 113:
/*      */         this.ct = (ComplexTypeImpl)$__result__;
/*      */         action5();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 115:
/*      */         this.group = (ModelGroupDeclImpl)$__result__;
/*      */         action2();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 116:
/*      */         this.notation = (XSNotation)$__result__;
/*      */         action1();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 117:
/*      */         this.ag = (AttGroupDeclImpl)$__result__;
/*      */         action0();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 142:
/*      */         this.efd = ((Boolean)$__result__).booleanValue();
/*      */         action13();
/*      */         this.$_ngcc_current_state = 46;
/*      */         break;
/*      */       case 147:
/*      */         this.afd = ((Boolean)$__result__).booleanValue();
/*      */         action14();
/*      */         this.$_ngcc_current_state = 50;
/*      */         break;
/*      */       case 42:
/*      */         this.e = (ElementDecl)$__result__;
/*      */         action7();
/*      */         this.$_ngcc_current_state = 26;
/*      */         break;
/*      */       case 118:
/*      */         this.anno = (AnnotationImpl)$__result__;
/*      */         action9();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 119:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 120:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 121:
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 123:
/*      */         this.st = (SimpleTypeImpl)$__result__;
/*      */         action6();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 124:
/*      */         this.ct = (ComplexTypeImpl)$__result__;
/*      */         action5();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 126:
/*      */         this.group = (ModelGroupDeclImpl)$__result__;
/*      */         action2();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 127:
/*      */         this.notation = (XSNotation)$__result__;
/*      */         action1();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */       case 128:
/*      */         this.ag = (AttGroupDeclImpl)$__result__;
/*      */         action0();
/*      */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean accepted() {
/*      */     return (this.$_ngcc_current_state == 0);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\Schema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */