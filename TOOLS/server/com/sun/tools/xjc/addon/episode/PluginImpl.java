/*     */ package com.sun.tools.xjc.addon.episode;
/*     */ 
/*     */ import com.sun.tools.xjc.BadCommandLineException;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.Plugin;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.v2.schemagen.episode.Bindings;
/*     */ import com.sun.xml.txw2.TXW;
/*     */ import com.sun.xml.txw2.output.StreamSerializer;
/*     */ import com.sun.xml.txw2.output.XmlSerializer;
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class PluginImpl
/*     */   extends Plugin
/*     */ {
/*     */   private File episodeFile;
/*     */   
/*     */   public String getOptionName() {
/*  92 */     return "episode";
/*     */   }
/*     */   
/*     */   public String getUsage() {
/*  96 */     return "  -episode <FILE>    :  generate the episode file for separate compilation";
/*     */   }
/*     */   
/*     */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/* 100 */     if (args[i].equals("-episode")) {
/* 101 */       this.episodeFile = new File(opt.requireArgument("-episode", args, ++i));
/* 102 */       return 2;
/*     */     } 
/* 104 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean run(Outline model, Options opt, ErrorHandler errorHandler) throws SAXException {
/*     */     try {
/* 115 */       Map<XSSchema, List<ClassOutline>> perSchema = new HashMap<XSSchema, List<ClassOutline>>();
/* 116 */       boolean hasComponentInNoNamespace = false;
/*     */       
/* 118 */       for (ClassOutline co : model.getClasses()) {
/* 119 */         XSComponent sc = co.target.getSchemaComponent();
/* 120 */         if (sc == null || 
/* 121 */           !(sc instanceof XSDeclaration))
/*     */           continue; 
/* 123 */         XSDeclaration decl = (XSDeclaration)sc;
/* 124 */         if (decl.isLocal()) {
/*     */           continue;
/*     */         }
/* 127 */         List<ClassOutline> list = perSchema.get(decl.getOwnerSchema());
/* 128 */         if (list == null) {
/* 129 */           list = new ArrayList<ClassOutline>();
/* 130 */           perSchema.put(decl.getOwnerSchema(), list);
/*     */         } 
/*     */         
/* 133 */         list.add(co);
/*     */         
/* 135 */         if (decl.getTargetNamespace().equals("")) {
/* 136 */           hasComponentInNoNamespace = true;
/*     */         }
/*     */       } 
/* 139 */       OutputStream os = new FileOutputStream(this.episodeFile);
/* 140 */       Bindings bindings = (Bindings)TXW.create(Bindings.class, (XmlSerializer)new StreamSerializer(os, "UTF-8"));
/* 141 */       if (hasComponentInNoNamespace) {
/* 142 */         bindings._namespace("http://java.sun.com/xml/ns/jaxb", "jaxb");
/*     */       } else {
/* 144 */         bindings._namespace("http://java.sun.com/xml/ns/jaxb", "");
/* 145 */       }  bindings.version("2.1");
/* 146 */       bindings._comment("\n\n" + opt.getPrologComment() + "\n  ");
/*     */ 
/*     */       
/* 149 */       for (Map.Entry<XSSchema, List<ClassOutline>> e : perSchema.entrySet()) {
/* 150 */         Bindings group = bindings.bindings();
/* 151 */         String tns = ((XSSchema)e.getKey()).getTargetNamespace();
/* 152 */         if (!tns.equals("")) {
/* 153 */           group._namespace(tns, "tns");
/*     */         }
/* 155 */         group.scd("x-schema::" + (tns.equals("") ? "" : "tns"));
/* 156 */         group.schemaBindings().map(false);
/*     */         
/* 158 */         for (ClassOutline co : e.getValue()) {
/* 159 */           Bindings child = group.bindings();
/* 160 */           child.scd((String)co.target.getSchemaComponent().apply(SCD));
/* 161 */           child.klass().ref(co.implClass.fullName());
/*     */         } 
/* 163 */         group.commit(true);
/*     */       } 
/*     */       
/* 166 */       bindings.commit();
/*     */       
/* 168 */       return true;
/* 169 */     } catch (IOException e) {
/* 170 */       errorHandler.error(new SAXParseException("Failed to write to " + this.episodeFile, null, e));
/* 171 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   private static final XSFunction<String> SCD = new XSFunction<String>() {
/*     */       private String name(XSDeclaration decl) {
/* 181 */         if (decl.getTargetNamespace().equals("")) {
/* 182 */           return decl.getName();
/*     */         }
/* 184 */         return "tns:" + decl.getName();
/*     */       }
/*     */       
/*     */       public String complexType(XSComplexType type) {
/* 188 */         return "~" + name((XSDeclaration)type);
/*     */       }
/*     */       
/*     */       public String simpleType(XSSimpleType simpleType) {
/* 192 */         return "~" + name((XSDeclaration)simpleType);
/*     */       }
/*     */       
/*     */       public String elementDecl(XSElementDecl decl) {
/* 196 */         return name((XSDeclaration)decl);
/*     */       }
/*     */ 
/*     */       
/*     */       public String annotation(XSAnnotation ann) {
/* 201 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String attGroupDecl(XSAttGroupDecl decl) {
/* 205 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String attributeDecl(XSAttributeDecl decl) {
/* 209 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String attributeUse(XSAttributeUse use) {
/* 213 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String schema(XSSchema schema) {
/* 217 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String facet(XSFacet facet) {
/* 221 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String notation(XSNotation notation) {
/* 225 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String identityConstraint(XSIdentityConstraint decl) {
/* 229 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String xpath(XSXPath xpath) {
/* 233 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String particle(XSParticle particle) {
/* 237 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String empty(XSContentType empty) {
/* 241 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String wildcard(XSWildcard wc) {
/* 245 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String modelGroupDecl(XSModelGroupDecl decl) {
/* 249 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       
/*     */       public String modelGroup(XSModelGroup group) {
/* 253 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\addon\episode\PluginImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */