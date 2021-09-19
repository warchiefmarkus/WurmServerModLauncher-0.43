/*     */ package 1.0.com.sun.xml.xsom.impl.util;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaWriter
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*     */   private final Writer out;
/*     */   private int indent;
/*     */   private boolean hadError;
/*     */   
/*     */   private void println(String s) {
/*     */     try {
/*     */       for (int i = 0; i < this.indent; ) {
/*     */         this.out.write("  ");
/*     */         i++;
/*     */       } 
/*     */       this.out.write(s);
/*     */       this.out.write(10);
/*     */     } catch (IOException e) {
/*     */       this.hadError = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public SchemaWriter(Writer _out) {
/*  82 */     this.hadError = false;
/*     */     this.out = _out;
/*     */   }
/*     */   public boolean checkError() {
/*     */     try {
/*  87 */       this.out.flush();
/*  88 */     } catch (IOException e) {
/*  89 */       this.hadError = true;
/*     */     } 
/*  91 */     return this.hadError;
/*     */   }
/*     */ 
/*     */   
/*     */   private String toString(UName name) {
/*  96 */     return '{' + name.getNamespaceURI() + '}' + name.getName();
/*     */   } private void println() {
/*     */     println("");
/*     */   } public void visit(XSSchemaSet s) {
/* 100 */     Iterator itr = s.iterateSchema();
/* 101 */     while (itr.hasNext()) {
/* 102 */       schema(itr.next());
/* 103 */       println();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void schema(XSSchema s) {
/* 110 */     if (s.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/*     */       return;
/*     */     }
/* 113 */     println(MessageFormat.format("<schema targetNamespace=\"{0}\">", new Object[] { s.getTargetNamespace() }));
/*     */ 
/*     */ 
/*     */     
/* 117 */     this.indent++;
/*     */ 
/*     */ 
/*     */     
/* 121 */     Iterator itr = s.iterateAttGroupDecls();
/* 122 */     while (itr.hasNext()) {
/* 123 */       attGroupDecl(itr.next());
/*     */     }
/* 125 */     itr = s.iterateAttributeDecls();
/* 126 */     while (itr.hasNext()) {
/* 127 */       attributeDecl((XSAttributeDecl)itr.next());
/*     */     }
/* 129 */     itr = s.iterateComplexTypes();
/* 130 */     while (itr.hasNext()) {
/* 131 */       complexType((XSComplexType)itr.next());
/*     */     }
/* 133 */     itr = s.iterateElementDecls();
/* 134 */     while (itr.hasNext()) {
/* 135 */       elementDecl((XSElementDecl)itr.next());
/*     */     }
/* 137 */     itr = s.iterateModelGroupDecls();
/* 138 */     while (itr.hasNext()) {
/* 139 */       modelGroupDecl((XSModelGroupDecl)itr.next());
/*     */     }
/* 141 */     itr = s.iterateSimpleTypes();
/* 142 */     while (itr.hasNext()) {
/* 143 */       simpleType((XSSimpleType)itr.next());
/*     */     }
/* 145 */     this.indent--;
/* 146 */     println("</schema>");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {
/* 152 */     println(MessageFormat.format("<attGroup name=\"{0}\">", new Object[] { decl.getName() }));
/*     */     
/* 154 */     this.indent++;
/*     */ 
/*     */ 
/*     */     
/* 158 */     Iterator itr = decl.iterateAttGroups();
/* 159 */     while (itr.hasNext()) {
/* 160 */       dumpRef(itr.next());
/*     */     }
/* 162 */     itr = decl.iterateDeclaredAttributeUses();
/* 163 */     while (itr.hasNext()) {
/* 164 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/* 166 */     this.indent--;
/* 167 */     println("</attGroup>");
/*     */   }
/*     */   
/*     */   public void dumpRef(XSAttGroupDecl decl) {
/* 171 */     println(MessageFormat.format("<attGroup ref=\"'{'{0}'}'{1}\"/>", new Object[] { decl.getTargetNamespace(), decl.getName() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 176 */     XSAttributeDecl decl = use.getDecl();
/*     */     
/* 178 */     String additionalAtts = "";
/*     */     
/* 180 */     if (use.isRequired())
/* 181 */       additionalAtts = additionalAtts + " use=\"required\""; 
/* 182 */     if (use.getFixedValue() != null && use.getDecl().getFixedValue() == null)
/* 183 */       additionalAtts = additionalAtts + " fixed=\"" + use.getFixedValue() + "\""; 
/* 184 */     if (use.getDefaultValue() != null && use.getDecl().getDefaultValue() == null) {
/* 185 */       additionalAtts = additionalAtts + " default=\"" + use.getDefaultValue() + "\"";
/*     */     }
/* 187 */     if (decl.isLocal()) {
/*     */       
/* 189 */       dump(decl, additionalAtts);
/*     */     } else {
/*     */       
/* 192 */       println(MessageFormat.format("<attribute ref=\"'{'{0}'}'{1}{2}\"/>", new Object[] { decl.getTargetNamespace(), decl.getName(), additionalAtts }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl decl) {
/* 199 */     dump(decl, "");
/*     */   }
/*     */   
/*     */   private void dump(XSAttributeDecl decl, String additionalAtts) {
/* 203 */     XSSimpleType type = decl.getType();
/*     */     
/* 205 */     println(MessageFormat.format("<attribute name=\"{0}\"{1}{2}{3}{4}{5}>", new Object[] { decl.getName(), additionalAtts, type.isLocal() ? "" : MessageFormat.format(" type=\"'{'{0}'}'{1}\"", new Object[] { type.getTargetNamespace(), type.getName() }), (decl.getFixedValue() == null) ? "" : (" fixed=\"" + decl.getFixedValue() + "\""), (decl.getDefaultValue() == null) ? "" : (" default=\"" + decl.getDefaultValue() + "\""), type.isLocal() ? "" : " /" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 222 */     if (type.isLocal()) {
/* 223 */       this.indent++;
/* 224 */       simpleType(type);
/* 225 */       this.indent--;
/* 226 */       println("</attribute>");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType type) {
/* 231 */     println(MessageFormat.format("<simpleType{0}>", new Object[] { type.isLocal() ? "" : (" name=\"" + type.getName() + "\"") }));
/*     */ 
/*     */ 
/*     */     
/* 235 */     this.indent++;
/*     */     
/* 237 */     type.visit(this);
/*     */     
/* 239 */     this.indent--;
/* 240 */     println("</simpleType>");
/*     */   }
/*     */   
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 244 */     XSSimpleType itemType = type.getItemType();
/*     */     
/* 246 */     if (itemType.isLocal()) {
/* 247 */       println("<list>");
/* 248 */       this.indent++;
/* 249 */       simpleType(itemType);
/* 250 */       this.indent--;
/* 251 */       println("</list>");
/*     */     } else {
/*     */       
/* 254 */       println(MessageFormat.format("<list itemType=\"'{'{0}'}'{1}\" />", new Object[] { itemType.getTargetNamespace(), itemType.getName() }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 263 */     int len = type.getMemberSize();
/* 264 */     StringBuffer ref = new StringBuffer();
/*     */     int i;
/* 266 */     for (i = 0; i < len; i++) {
/* 267 */       XSSimpleType member = type.getMember(i);
/* 268 */       if (member.isGlobal()) {
/* 269 */         ref.append(MessageFormat.format(" '{'{0}'}'{1}", new Object[] { member.getTargetNamespace(), member.getName() }));
/*     */       }
/*     */     } 
/*     */     
/* 273 */     if (ref.length() == 0) {
/* 274 */       println("<union>");
/*     */     } else {
/* 276 */       println("<union memberTypes=\"" + ref + "\">");
/* 277 */     }  this.indent++;
/*     */     
/* 279 */     for (i = 0; i < len; i++) {
/* 280 */       XSSimpleType member = type.getMember(i);
/* 281 */       if (member.isLocal())
/* 282 */         simpleType(member); 
/*     */     } 
/* 284 */     this.indent--;
/* 285 */     println("</union>");
/*     */   }
/*     */ 
/*     */   
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type) {
/* 290 */     if (type.getBaseType() == null) {
/*     */       
/* 292 */       if (!type.getName().equals("anySimpleType"))
/* 293 */         throw new InternalError(); 
/* 294 */       if (!"http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace())) {
/* 295 */         throw new InternalError();
/*     */       }
/*     */       return;
/*     */     } 
/* 299 */     XSSimpleType baseType = type.getSimpleBaseType();
/*     */     
/* 301 */     println(MessageFormat.format("<restriction{0}>", new Object[] { baseType.isLocal() ? "" : (" base=\"{" + baseType.getTargetNamespace() + "}" + baseType.getName() + "\"") }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     this.indent++;
/*     */     
/* 309 */     if (baseType.isLocal()) {
/* 310 */       simpleType(baseType);
/*     */     }
/* 312 */     Iterator itr = type.iterateDeclaredFacets();
/* 313 */     while (itr.hasNext()) {
/* 314 */       facet(itr.next());
/*     */     }
/* 316 */     this.indent--;
/* 317 */     println("</restriction>");
/*     */   }
/*     */   
/*     */   public void facet(XSFacet facet) {
/* 321 */     println(MessageFormat.format("<{0} value=\"{1}\"/>", new Object[] { facet.getName(), facet.getValue() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notation(XSNotation notation) {
/* 328 */     println(MessageFormat.format("<notation name='\"0}\" public =\"{1}\" system=\"{2}\" />", new Object[] { notation.getName(), notation.getPublicId(), notation.getSystemId() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void complexType(XSComplexType type) {
/* 338 */     println(MessageFormat.format("<complexType{0}>", new Object[] { type.isLocal() ? "" : (" name=\"" + type.getName() + "\"") }));
/*     */ 
/*     */ 
/*     */     
/* 342 */     this.indent++;
/*     */ 
/*     */ 
/*     */     
/* 346 */     if (type.getContentType().asSimpleType() != null) {
/*     */       
/* 348 */       println("<simpleContent>");
/* 349 */       this.indent++;
/*     */       
/* 351 */       XSType baseType = type.getBaseType();
/*     */       
/* 353 */       if (type.getDerivationMethod() == 2) {
/*     */         
/* 355 */         println(MessageFormat.format("<restriction base=\"<{0}>{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */         
/* 359 */         this.indent++;
/*     */         
/* 361 */         dumpComplexTypeAttribute(type);
/*     */         
/* 363 */         this.indent--;
/* 364 */         println("</restriction>");
/*     */       } else {
/*     */         
/* 367 */         println(MessageFormat.format("<extension base=\"<{0}>{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */         
/* 371 */         this.indent++;
/*     */         
/* 373 */         dumpComplexTypeAttribute(type);
/*     */         
/* 375 */         this.indent--;
/* 376 */         println("</extension>");
/*     */       } 
/*     */       
/* 379 */       this.indent--;
/* 380 */       println("</simpleContent>");
/*     */     } else {
/*     */       
/* 383 */       println("<complexContent>");
/* 384 */       this.indent++;
/*     */       
/* 386 */       XSComplexType baseType = type.getBaseType().asComplexType();
/*     */       
/* 388 */       if (type.getDerivationMethod() == 2) {
/*     */         
/* 390 */         println(MessageFormat.format("<restriction base=\"'{'{0}'}'{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */         
/* 394 */         this.indent++;
/*     */         
/* 396 */         type.getContentType().visit(this);
/* 397 */         dumpComplexTypeAttribute(type);
/*     */         
/* 399 */         this.indent--;
/* 400 */         println("</restriction>");
/*     */       } else {
/*     */         
/* 403 */         println(MessageFormat.format("<extension base=\"'{'{0}'}'{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */         
/* 407 */         this.indent++;
/*     */         
/* 409 */         type.getExplicitContent().visit(this);
/* 410 */         dumpComplexTypeAttribute(type);
/*     */         
/* 412 */         this.indent--;
/* 413 */         println("</extension>");
/*     */       } 
/*     */       
/* 416 */       this.indent--;
/* 417 */       println("</complexContent>");
/*     */     } 
/*     */     
/* 420 */     this.indent--;
/* 421 */     println("</complexType>");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dumpComplexTypeAttribute(XSComplexType type) {
/* 427 */     Iterator itr = type.iterateAttGroups();
/* 428 */     while (itr.hasNext()) {
/* 429 */       dumpRef(itr.next());
/*     */     }
/* 431 */     itr = type.iterateDeclaredAttributeUses();
/* 432 */     while (itr.hasNext())
/* 433 */       attributeUse((XSAttributeUse)itr.next()); 
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 437 */     elementDecl(decl, "");
/*     */   }
/*     */   private void elementDecl(XSElementDecl decl, String extraAtts) {
/* 440 */     XSType type = decl.getType();
/*     */ 
/*     */ 
/*     */     
/* 444 */     println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>", new Object[] { decl.getName(), type.isLocal() ? "" : (" type=\"{" + type.getTargetNamespace() + "}" + type.getName() + "\""), extraAtts, type.isLocal() ? "" : "/" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 454 */     if (type.isLocal()) {
/* 455 */       this.indent++;
/*     */       
/* 457 */       if (type.isLocal()) type.visit(this);
/*     */       
/* 459 */       this.indent--;
/* 460 */       println("</element>");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 465 */     println(MessageFormat.format("<group name=\"{0}\">", new Object[] { decl.getName() }));
/*     */ 
/*     */ 
/*     */     
/* 469 */     this.indent++;
/*     */     
/* 471 */     modelGroup(decl.getModelGroup());
/*     */     
/* 473 */     this.indent--;
/* 474 */     println("</group>");
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 478 */     modelGroup(group, "");
/*     */   }
/*     */   private void modelGroup(XSModelGroup group, String extraAtts) {
/* 481 */     println(MessageFormat.format("<{0}{1}>", new Object[] { group.getCompositor(), extraAtts }));
/*     */     
/* 483 */     this.indent++;
/*     */     
/* 485 */     int len = group.getSize();
/* 486 */     for (int i = 0; i < len; i++) {
/* 487 */       particle(group.getChild(i));
/*     */     }
/* 489 */     this.indent--;
/* 490 */     println(MessageFormat.format("</{0}>", new Object[] { group.getCompositor() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void particle(XSParticle part) {
/* 497 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 499 */     int i = part.getMaxOccurs();
/* 500 */     if (i == -1) {
/* 501 */       buf.append(" maxOccurs=\"unbounded\"");
/* 502 */     } else if (i != 1) {
/* 503 */       buf.append(" maxOccurs=\"" + i + "\"");
/*     */     } 
/* 505 */     i = part.getMinOccurs();
/* 506 */     if (i != 1) {
/* 507 */       buf.append(" minOccurs=\"" + i + "\"");
/*     */     }
/* 509 */     String extraAtts = buf.toString();
/*     */     
/* 511 */     part.getTerm().visit((XSTermVisitor)new Object(this, extraAtts));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 544 */     wildcard(wc, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private void wildcard(XSWildcard wc, String extraAtts) {
/* 549 */     println(MessageFormat.format("<any/>", new Object[] { extraAtts }));
/*     */   }
/*     */   
/*     */   public void annotation(XSAnnotation ann) {}
/*     */   
/*     */   public void empty(XSContentType t) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\imp\\util\SchemaWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */