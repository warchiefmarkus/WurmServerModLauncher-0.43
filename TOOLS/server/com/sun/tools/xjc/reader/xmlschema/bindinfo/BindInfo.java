/*     */ package com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.tools.xjc.SchemaCache;
/*     */ import com.sun.tools.xjc.model.CCustomizations;
/*     */ import com.sun.tools.xjc.model.CPluginCustomization;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.bind.annotation.XmlLocation;
/*     */ import com.sun.xml.bind.marshaller.MinimumEscapeHandler;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import java.io.FilterWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAnyElement;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlMixed;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Element;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlRootElement(namespace = "http://www.w3.org/2001/XMLSchema", name = "annotation")
/*     */ @XmlType(namespace = "http://www.w3.org/2001/XMLSchema", name = "foobar")
/*     */ public final class BindInfo
/*     */   implements Iterable<BIDeclaration>
/*     */ {
/*     */   private BGMBuilder builder;
/*     */   @XmlLocation
/*     */   private Locator location;
/*     */   @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema")
/*     */   private Documentation documentation;
/*     */   
/*     */   public boolean isPointless() {
/* 106 */     if (size() > 0) return false; 
/* 107 */     if (this.documentation != null && !this.documentation.contents.isEmpty()) {
/* 108 */       return false;
/*     */     }
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   private static final class Documentation {
/*     */     @XmlAnyElement
/*     */     @XmlMixed
/* 116 */     List<Object> contents = new ArrayList();
/*     */     
/*     */     void addAll(Documentation rhs) {
/* 119 */       if (rhs == null)
/*     */         return; 
/* 121 */       if (this.contents == null)
/* 122 */         this.contents = new ArrayList(); 
/* 123 */       if (!this.contents.isEmpty())
/* 124 */         this.contents.add("\n\n"); 
/* 125 */       this.contents.addAll(rhs.contents);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 130 */   private final List<BIDeclaration> decls = new ArrayList<BIDeclaration>();
/*     */   
/*     */   private XSComponent owner;
/*     */   
/*     */   private static final class AppInfo
/*     */   {
/*     */     @XmlAnyElement(lax = true, value = DomHandlerEx.class)
/* 137 */     List<Object> contents = new ArrayList();
/*     */     
/*     */     public void addTo(BindInfo bi) {
/* 140 */       if (this.contents == null)
/*     */         return; 
/* 142 */       for (Object o : this.contents) {
/* 143 */         if (o instanceof BIDeclaration) {
/* 144 */           bi.addDecl((BIDeclaration)o);
/*     */         }
/* 146 */         if (o instanceof DomHandlerEx.DomAndLocation) {
/* 147 */           DomHandlerEx.DomAndLocation e = (DomHandlerEx.DomAndLocation)o;
/* 148 */           String nsUri = e.element.getNamespaceURI();
/* 149 */           if (nsUri == null || nsUri.equals("") || nsUri.equals("http://www.w3.org/2001/XMLSchema")) {
/*     */             continue;
/*     */           }
/* 152 */           bi.addDecl(new BIXPluginCustomization(e.element, e.loc));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElement(namespace = "http://www.w3.org/2001/XMLSchema")
/*     */   void setAppinfo(AppInfo aib) {
/* 162 */     aib.addTo(this);
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
/*     */   public Locator getSourceLocation() {
/* 176 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOwner(BGMBuilder _builder, XSComponent _owner) {
/* 186 */     this.owner = _owner;
/* 187 */     this.builder = _builder;
/* 188 */     for (BIDeclaration d : this.decls)
/* 189 */       d.onSetOwner(); 
/*     */   } public XSComponent getOwner() {
/* 191 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BGMBuilder getBuilder() {
/* 197 */     return this.builder;
/*     */   }
/*     */   
/*     */   public void addDecl(BIDeclaration decl) {
/* 201 */     if (decl == null) throw new IllegalArgumentException(); 
/* 202 */     decl.setParent(this);
/* 203 */     this.decls.add(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BIDeclaration> T get(Class<T> kind) {
/* 212 */     for (BIDeclaration decl : this.decls) {
/* 213 */       if (kind.isInstance(decl))
/* 214 */         return kind.cast(decl); 
/*     */     } 
/* 216 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIDeclaration[] getDecls() {
/* 223 */     return this.decls.<BIDeclaration>toArray(new BIDeclaration[this.decls.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDocumentation() {
/* 233 */     if (this.documentation == null || this.documentation.contents == null) return null;
/*     */     
/* 235 */     StringBuilder buf = new StringBuilder();
/* 236 */     for (Object c : this.documentation.contents) {
/* 237 */       if (c instanceof String) {
/* 238 */         buf.append(c.toString());
/*     */       }
/* 240 */       if (c instanceof Element) {
/* 241 */         Transformer t = this.builder.getIdentityTransformer();
/* 242 */         StringWriter w = new StringWriter();
/*     */         try {
/* 244 */           Writer fw = new FilterWriter(w) {
/* 245 */               char[] buf = new char[1];
/*     */               
/*     */               public void write(int c) throws IOException {
/* 248 */                 this.buf[0] = (char)c;
/* 249 */                 write(this.buf, 0, 1);
/*     */               }
/*     */               
/*     */               public void write(char[] cbuf, int off, int len) throws IOException {
/* 253 */                 MinimumEscapeHandler.theInstance.escape(cbuf, off, len, false, this.out);
/*     */               }
/*     */               
/*     */               public void write(String str, int off, int len) throws IOException {
/* 257 */                 write(str.toCharArray(), off, len);
/*     */               }
/*     */             };
/* 260 */           t.transform(new DOMSource((Element)c), new StreamResult(fw));
/* 261 */         } catch (TransformerException e) {
/* 262 */           throw new Error(e);
/*     */         } 
/* 264 */         buf.append("\n<pre>\n");
/* 265 */         buf.append(w);
/* 266 */         buf.append("\n</pre>\n");
/*     */       } 
/*     */     } 
/* 269 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void absorb(BindInfo bi) {
/* 277 */     for (BIDeclaration d : bi)
/* 278 */       d.setParent(this); 
/* 279 */     this.decls.addAll(bi.decls);
/*     */     
/* 281 */     if (this.documentation == null) {
/* 282 */       this.documentation = bi.documentation;
/*     */     } else {
/* 284 */       this.documentation.addAll(bi.documentation);
/*     */     } 
/*     */   }
/*     */   public int size() {
/* 288 */     return this.decls.size();
/*     */   } public BIDeclaration get(int idx) {
/* 290 */     return this.decls.get(idx);
/*     */   }
/*     */   public Iterator<BIDeclaration> iterator() {
/* 293 */     return this.decls.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CCustomizations toCustomizationList() {
/* 304 */     CCustomizations r = null;
/* 305 */     for (BIDeclaration d : this) {
/* 306 */       if (d instanceof BIXPluginCustomization) {
/* 307 */         BIXPluginCustomization pc = (BIXPluginCustomization)d;
/* 308 */         pc.markAsAcknowledged();
/* 309 */         if (!((Model)Ring.get(Model.class)).options.pluginURIs.contains(pc.getName().getNamespaceURI()))
/*     */           continue; 
/* 311 */         if (r == null)
/* 312 */           r = new CCustomizations(); 
/* 313 */         r.add(new CPluginCustomization(pc.element, pc.getLocation()));
/*     */       } 
/*     */     } 
/*     */     
/* 317 */     if (r == null) r = CCustomizations.EMPTY; 
/* 318 */     return new CCustomizations((Collection)r);
/*     */   }
/*     */   
/* 321 */   public static final BindInfo empty = new BindInfo();
/*     */ 
/*     */   
/*     */   private static JAXBContextImpl customizationContext;
/*     */ 
/*     */ 
/*     */   
/*     */   public static JAXBContextImpl getJAXBContext() {
/* 329 */     synchronized (AnnotationParserFactoryImpl.class) {
/*     */       
/* 331 */       if (customizationContext == null) {
/* 332 */         customizationContext = new JAXBContextImpl(new Class[] { BindInfo.class, BIClass.class, BIConversion.User.class, BIConversion.UserAdapter.class, BIDom.class, BIFactoryMethod.class, BIInlineBinaryData.class, BIXDom.class, BIXSubstitutable.class, BIEnum.class, BIEnumMember.class, BIGlobalBinding.class, BIProperty.class, BISchemaBinding.class }, Collections.emptyList(), Collections.emptyMap(), null, false, null, false, false);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 350 */       return customizationContext;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 360 */   public static final SchemaCache bindingFileSchema = new SchemaCache(BindInfo.class.getResource("binding.xsd"));
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BindInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */