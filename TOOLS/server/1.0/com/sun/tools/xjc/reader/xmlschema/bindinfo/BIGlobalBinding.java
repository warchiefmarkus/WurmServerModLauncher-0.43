/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.tools.xjc.generator.field.DefaultFieldRendererFactory;
/*     */ import com.sun.tools.xjc.generator.field.FieldRendererFactory;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.AbstractDeclarationImpl;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXSerializable;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXSuperClass;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.Messages;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
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
/*     */ public final class BIGlobalBinding
/*     */   extends AbstractDeclarationImpl
/*     */ {
/*     */   private final NameConverter nameConverter;
/*     */   private final boolean enableJavaNamingConvention;
/*     */   private final boolean modelGroupBinding;
/*     */   private final BIProperty property;
/*     */   private final boolean generateEnumMemberName;
/*     */   private final boolean choiceContentPropertyWithModelGroupBinding;
/*     */   private final Set enumBaseTypes;
/*     */   private final BIXSerializable serializable;
/*     */   private final BIXSuperClass superClass;
/*     */   public final boolean smartWildcardDefaultBinding;
/*     */   private final boolean enableTypeSubstitutionSupport;
/*     */   private final Map globalConversions;
/*     */   
/*     */   private static Set createSet() {
/*  67 */     Set s = new HashSet();
/*  68 */     s.add(new QName("http://www.w3.org/2001/XMLSchema", "NCName"));
/*  69 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIGlobalBinding(JCodeModel codeModel) {
/*  76 */     this(codeModel, new HashMap(), NameConverter.standard, false, false, true, false, false, false, createSet(), null, null, null, false, false, null);
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
/*     */   public BIGlobalBinding(JCodeModel codeModel, Map _globalConvs, NameConverter nconv, boolean _modelGroupBinding, boolean _choiceContentPropertyWithModelGroupBinding, boolean _enableJavaNamingConvention, boolean _fixedAttrToConstantProperty, boolean _needIsSetMethod, boolean _generateEnumMemberName, Set _enumBaseTypes, FieldRendererFactory collectionFieldRenderer, BIXSerializable _serializable, BIXSuperClass _superClass, boolean _enableTypeSubstitutionSupport, boolean _smartWildcardDefaultBinding, Locator _loc) {
/* 101 */     super(_loc);
/*     */     
/* 103 */     this.globalConversions = _globalConvs;
/* 104 */     this.nameConverter = nconv;
/* 105 */     this.modelGroupBinding = _modelGroupBinding;
/* 106 */     this.choiceContentPropertyWithModelGroupBinding = _choiceContentPropertyWithModelGroupBinding;
/* 107 */     this.enableJavaNamingConvention = _enableJavaNamingConvention;
/* 108 */     this.generateEnumMemberName = _generateEnumMemberName;
/* 109 */     this.enumBaseTypes = _enumBaseTypes;
/* 110 */     this.serializable = _serializable;
/* 111 */     this.superClass = _superClass;
/* 112 */     this.enableTypeSubstitutionSupport = _enableTypeSubstitutionSupport;
/* 113 */     this.smartWildcardDefaultBinding = _smartWildcardDefaultBinding;
/*     */     
/* 115 */     this.property = new BIProperty(_loc, null, null, null, null, (collectionFieldRenderer == null) ? (FieldRendererFactory)new DefaultFieldRendererFactory(codeModel) : (FieldRendererFactory)new DefaultFieldRendererFactory(collectionFieldRenderer), _fixedAttrToConstantProperty ? Boolean.TRUE : Boolean.FALSE, _needIsSetMethod ? Boolean.TRUE : Boolean.FALSE);
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
/*     */   public NameConverter getNameConverter() {
/* 138 */     return this.nameConverter;
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
/*     */   boolean isJavaNamingConventionEnabled() {
/* 152 */     return this.enableJavaNamingConvention;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isModelGroupBinding() {
/* 157 */     return this.modelGroupBinding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChoiceContentPropertyModelGroupBinding() {
/* 164 */     return this.choiceContentPropertyWithModelGroupBinding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTypeSubstitutionSupportEnabled() {
/* 175 */     return this.enableTypeSubstitutionSupport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIProperty getDefaultProperty() {
/* 182 */     return this.property;
/*     */   }
/*     */   
/*     */   public void setParent(BindInfo parent) {
/* 186 */     super.setParent(parent);
/* 187 */     this.property.setParent(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispatchGlobalConversions(XSSchemaSet schema) {
/* 196 */     for (Iterator itr = this.globalConversions.entrySet().iterator(); itr.hasNext(); ) {
/* 197 */       Map.Entry e = itr.next();
/*     */       
/* 199 */       QName name = (QName)e.getKey();
/* 200 */       BIConversion conv = (BIConversion)e.getValue();
/*     */       
/* 202 */       XSSimpleType st = schema.getSimpleType(name.getNamespaceURI(), name.getLocalPart());
/* 203 */       if (st == null) {
/* 204 */         (getBuilder()).errorReceiver.error(getLocation(), Messages.format("UndefinedSimpleType", name));
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 211 */       getBuilder().getOrCreateBindInfo((XSComponent)st).addDecl((BIDeclaration)conv);
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
/*     */   public boolean canBeMappedToTypeSafeEnum(QName typeName) {
/* 225 */     return this.enumBaseTypes.contains(typeName);
/*     */   }
/*     */   
/*     */   public boolean canBeMappedToTypeSafeEnum(String nsUri, String localName) {
/* 229 */     return canBeMappedToTypeSafeEnum(new QName(nsUri, localName));
/*     */   }
/*     */   
/*     */   public boolean canBeMappedToTypeSafeEnum(XSDeclaration decl) {
/* 233 */     return canBeMappedToTypeSafeEnum(decl.getTargetNamespace(), decl.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean needsToGenerateEnumMemberName() {
/* 241 */     return this.generateEnumMemberName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIXSerializable getSerializableExtension() {
/* 249 */     return this.serializable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BIXSuperClass getSuperClassExtension() {
/* 257 */     return this.superClass;
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
/*     */   public QName getName() {
/* 278 */     return NAME;
/* 279 */   } public static final QName NAME = new QName("http://java.sun.com/xml/ns/jaxb", "globalBinding");
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIGlobalBinding.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */