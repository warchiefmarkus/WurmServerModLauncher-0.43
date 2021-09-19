/*     */ package com.sun.tools.xjc.reader.internalizer;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.SAXParseException2;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.util.DOMUtils;
/*     */ import com.sun.tools.xjc.util.ForkContentHandler;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import com.sun.xml.xsom.SCD;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.UnmarshallerHandler;
/*     */ import javax.xml.validation.ValidatorHandler;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SCDBasedBindingSet
/*     */ {
/*     */   private Target topLevel;
/*     */   private final DOMForest forest;
/*     */   private ErrorReceiver errorReceiver;
/*     */   private UnmarshallerHandler unmarshaller;
/*     */   private ForkContentHandler loader;
/*     */   
/*     */   final class Target
/*     */   {
/*     */     private Target firstChild;
/*     */     private final Target nextSibling;
/*     */     @NotNull
/*     */     private final SCD scd;
/*     */     @NotNull
/*     */     private final Element src;
/* 111 */     private final List<Element> bindings = new ArrayList<Element>();
/*     */     
/*     */     private Target(Target parent, Element src, SCD scd) {
/* 114 */       if (parent == null) {
/* 115 */         this.nextSibling = SCDBasedBindingSet.this.topLevel;
/* 116 */         SCDBasedBindingSet.this.topLevel = this;
/*     */       } else {
/* 118 */         this.nextSibling = parent.firstChild;
/* 119 */         parent.firstChild = this;
/*     */       } 
/* 121 */       this.src = src;
/* 122 */       this.scd = scd;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void addBinidng(Element binding) {
/* 130 */       this.bindings.add(binding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void applyAll(Collection<? extends XSComponent> contextNode) {
/* 137 */       for (Target self = this; self != null; self = self.nextSibling) {
/* 138 */         self.apply(contextNode);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void apply(Collection<? extends XSComponent> contextNode) {
/* 146 */       Collection<XSComponent> childNodes = this.scd.select(contextNode);
/* 147 */       if (childNodes.isEmpty()) {
/*     */         
/* 149 */         if (this.src.getAttributeNode("if-exists") != null) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 154 */         SCDBasedBindingSet.this.reportError(this.src, Messages.format("ERR_SCD_EVALUATED_EMPTY", new Object[] { this.scd }));
/*     */         
/*     */         return;
/*     */       } 
/* 158 */       if (this.firstChild != null) {
/* 159 */         this.firstChild.applyAll(childNodes);
/*     */       }
/* 161 */       if (!this.bindings.isEmpty()) {
/*     */         
/* 163 */         Iterator<XSComponent> itr = childNodes.iterator();
/* 164 */         XSComponent target = itr.next();
/* 165 */         if (itr.hasNext()) {
/* 166 */           SCDBasedBindingSet.this.reportError(this.src, Messages.format("ERR_SCD_MATCHED_MULTIPLE_NODES", new Object[] { this.scd, Integer.valueOf(childNodes.size()) }));
/* 167 */           SCDBasedBindingSet.this.errorReceiver.error(target.getLocator(), Messages.format("ERR_SCD_MATCHED_MULTIPLE_NODES_FIRST", new Object[0]));
/* 168 */           SCDBasedBindingSet.this.errorReceiver.error(((XSComponent)itr.next()).getLocator(), Messages.format("ERR_SCD_MATCHED_MULTIPLE_NODES_SECOND", new Object[0]));
/*     */         } 
/*     */ 
/*     */         
/* 172 */         for (Element binding : this.bindings) {
/* 173 */           for (Element item : DOMUtils.getChildElements(binding)) {
/* 174 */             String localName = item.getLocalName();
/*     */             
/* 176 */             if (!"bindings".equals(localName)) {
/*     */               
/*     */               try {
/*     */                 
/* 180 */                 (new DOMForestScanner(SCDBasedBindingSet.this.forest)).scan(item, (ContentHandler)SCDBasedBindingSet.this.loader);
/* 181 */                 BIDeclaration decl = (BIDeclaration)SCDBasedBindingSet.this.unmarshaller.getResult();
/*     */ 
/*     */                 
/* 184 */                 XSAnnotation ann = target.getAnnotation(true);
/* 185 */                 BindInfo bi = (BindInfo)ann.getAnnotation();
/* 186 */                 if (bi == null) {
/* 187 */                   bi = new BindInfo();
/* 188 */                   ann.setAnnotation(bi);
/*     */                 } 
/* 190 */                 bi.addDecl(decl);
/* 191 */               } catch (SAXException e) {
/*     */               
/* 193 */               } catch (JAXBException e) {
/*     */                 
/* 195 */                 throw new AssertionError(e);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SCDBasedBindingSet(DOMForest forest) {
/* 218 */     this.forest = forest;
/*     */   }
/*     */   
/*     */   Target createNewTarget(Target parent, Element src, SCD scd) {
/* 222 */     return new Target(parent, src, scd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(XSSchemaSet schema, ErrorReceiver errorReceiver) {
/* 229 */     if (this.topLevel != null) {
/* 230 */       this.errorReceiver = errorReceiver;
/* 231 */       UnmarshallerImpl u = BindInfo.getJAXBContext().createUnmarshaller();
/* 232 */       this.unmarshaller = u.getUnmarshallerHandler();
/* 233 */       ValidatorHandler v = BindInfo.bindingFileSchema.newValidator();
/* 234 */       v.setErrorHandler((ErrorHandler)errorReceiver);
/* 235 */       this.loader = new ForkContentHandler(v, (ContentHandler)this.unmarshaller);
/*     */       
/* 237 */       this.topLevel.applyAll(schema.getSchemas());
/*     */       
/* 239 */       this.loader = null;
/* 240 */       this.unmarshaller = null;
/* 241 */       this.errorReceiver = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg) {
/* 246 */     reportError(errorSource, formattedMsg, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void reportError(Element errorSource, String formattedMsg, Exception nestedException) {
/* 252 */     SAXParseException2 sAXParseException2 = new SAXParseException2(formattedMsg, this.forest.locatorTable.getStartLocation(errorSource), nestedException);
/*     */ 
/*     */     
/* 255 */     this.errorReceiver.error((SAXParseException)sAXParseException2);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\internalizer\SCDBasedBindingSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */