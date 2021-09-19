/*     */ package 1.0.com.sun.tools.xjc.reader.decorator;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JClassContainer;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.msv.datatype.DatabindableDatatype;
/*     */ import com.sun.msv.datatype.xsd.BooleanType;
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.OtherExp;
/*     */ import com.sun.msv.reader.GrammarReader;
/*     */ import com.sun.msv.reader.State;
/*     */ import com.sun.msv.util.StartTagInfo;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import com.sun.tools.xjc.generator.field.ArrayFieldRenderer;
/*     */ import com.sun.tools.xjc.generator.field.TypedListFieldRenderer;
/*     */ import com.sun.tools.xjc.grammar.AnnotatedGrammar;
/*     */ import com.sun.tools.xjc.grammar.ClassItem;
/*     */ import com.sun.tools.xjc.grammar.ExternalItem;
/*     */ import com.sun.tools.xjc.grammar.FieldItem;
/*     */ import com.sun.tools.xjc.grammar.IgnoreItem;
/*     */ import com.sun.tools.xjc.grammar.InterfaceItem;
/*     */ import com.sun.tools.xjc.grammar.PrimitiveItem;
/*     */ import com.sun.tools.xjc.grammar.SuperClassItem;
/*     */ import com.sun.tools.xjc.grammar.ext.DOMItemFactory;
/*     */ import com.sun.tools.xjc.grammar.util.NameFinder;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.UserTransducer;
/*     */ import com.sun.tools.xjc.reader.NameConverter;
/*     */ import com.sun.tools.xjc.reader.PackageManager;
/*     */ import com.sun.tools.xjc.reader.TypeUtil;
/*     */ import com.sun.tools.xjc.reader.decorator.Decorator;
/*     */ import com.sun.tools.xjc.reader.decorator.DecoratorImpl;
/*     */ import com.sun.tools.xjc.reader.decorator.Messages;
/*     */ import com.sun.tools.xjc.util.CodeModelClassFactory;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RoleBasedDecorator
/*     */   extends DecoratorImpl
/*     */ {
/*     */   private final CodeModelClassFactory classFactory;
/*     */   private final Decorator defaultDecorator;
/*     */   private final PackageManager packageManager;
/*     */   
/*     */   public RoleBasedDecorator(GrammarReader _reader, ErrorReceiver _errorReceiver, AnnotatedGrammar _grammar, NameConverter _nc, PackageManager pkgMan, Decorator _defaultDecorator) {
/*  63 */     super(_reader, _grammar, _nc);
/*  64 */     this.defaultDecorator = _defaultDecorator;
/*  65 */     this.packageManager = pkgMan;
/*  66 */     this.classFactory = new CodeModelClassFactory(_errorReceiver);
/*     */   }
/*     */   
/*     */   public Expression decorate(State state, Expression exp) {
/*     */     IgnoreItem ignoreItem;
/*  71 */     StartTagInfo tag = state.getStartTag();
/*     */ 
/*     */     
/*  74 */     String role = getAttribute(tag, "role");
/*     */     
/*  76 */     if (role == null) {
/*     */ 
/*     */ 
/*     */       
/*  80 */       if (this.defaultDecorator != null)
/*  81 */         exp = this.defaultDecorator.decorate(state, exp); 
/*  82 */       return exp;
/*     */     } 
/*     */     
/*  85 */     role = role.intern();
/*     */ 
/*     */ 
/*     */     
/*  89 */     if (role == "none")
/*     */     {
/*  91 */       return exp;
/*     */     }
/*  93 */     if (role == "superClass") {
/*  94 */       SuperClassItem superClassItem = new SuperClassItem(null, state.getLocation());
/*     */     }
/*  96 */     else if (role == "class") {
/*  97 */       ClassItem classItem = this.grammar.createClassItem(this.classFactory.createInterface((JClassContainer)this.packageManager.getCurrentPackage(), decideName(state, exp, role, "", state.getLocation()), state.getLocation()), null, state.getLocation());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 106 */     else if (role == "field") {
/*     */       
/* 108 */       String collection = getAttribute(tag, "collection");
/* 109 */       String typeAtt = getAttribute(tag, "baseType");
/* 110 */       String delegation = getAttribute(tag, "delegate");
/*     */       
/* 112 */       JClass type = null;
/* 113 */       if (typeAtt != null) {
/*     */         try {
/* 115 */           type = this.codeModel.ref(typeAtt);
/* 116 */         } catch (ClassNotFoundException e) {
/* 117 */           reportError(Messages.format("ClassNotFound", typeAtt), state.getLocation());
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 122 */       FieldItem fi = new FieldItem(decideName(state, exp, role, "", state.getLocation()), null, (JType)type, this.reader.locator);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 128 */       FieldItem fieldItem1 = fi;
/*     */       
/* 130 */       if (delegation != null && delegation.equals("true")) {
/* 131 */         fi.setDelegation(true);
/*     */       }
/* 133 */       if (collection != null) {
/* 134 */         if (collection.equals("array"))
/* 135 */           fi.realization = ArrayFieldRenderer.theFactory; 
/* 136 */         if (collection.equals("list"))
/* 137 */           fi.realization = TypedListFieldRenderer.theFactory; 
/* 138 */         if (fi.realization == null) {
/* 139 */           reportError(Messages.format("InvalidCollectionType", collection), state.getLocation());
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 144 */     else if (role == "interface") {
/* 145 */       InterfaceItem interfaceItem = this.grammar.createInterfaceItem((JClass)this.classFactory.createInterface((JClassContainer)this.packageManager.getCurrentPackage(), decideName(state, exp, role, "", state.getLocation()), state.getLocation()), null, state.getLocation());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 154 */     else if (role == "primitive") {
/* 155 */       String name = getAttribute(tag, "name");
/* 156 */       String parseMethod = getAttribute(tag, "parseMethod");
/* 157 */       String printMethod = getAttribute(tag, "printMethod");
/* 158 */       boolean hasNsContext = BooleanType.load(getAttribute(tag, "hasNsContext", "false")).booleanValue();
/*     */       
/*     */       try {
/* 161 */         PrimitiveItem primitiveItem = this.grammar.createPrimitiveItem((Transducer)new UserTransducer(TypeUtil.getType(this.codeModel, name, (ErrorHandler)this.reader.controller, state.getLocation()), (parseMethod != null) ? parseMethod : "new", (printMethod != null) ? printMethod : "toString", hasNsContext), (DatabindableDatatype)StringType.theInstance, null, state.getLocation());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 169 */       catch (SAXException e) {
/*     */         
/* 171 */         OtherExp roleExp = new OtherExp();
/* 172 */       } catch (IllegalArgumentException e) {
/* 173 */         reportError(e.getMessage(), state.getLocation());
/* 174 */         OtherExp roleExp = new OtherExp();
/*     */       }
/*     */     
/* 177 */     } else if (role == "dom") {
/* 178 */       String type = getAttribute(tag, "type");
/* 179 */       if (type == null) type = "w3c"; 
/*     */       try {
/* 181 */         ExternalItem externalItem = DOMItemFactory.getInstance(type).create(NameFinder.findElement(exp), this.grammar, state.getLocation());
/*     */       
/*     */       }
/* 184 */       catch (com.sun.tools.xjc.grammar.ext.DOMItemFactory.UndefinedNameException e) {
/* 185 */         reportError(e.getMessage(), state.getLocation());
/* 186 */         return exp;
/*     */       }
/*     */     
/*     */     }
/* 190 */     else if (role == "ignore") {
/* 191 */       ignoreItem = new IgnoreItem(state.getLocation());
/*     */     } else {
/* 193 */       reportError(Messages.format("UndefinedRole", role), state.getLocation());
/* 194 */       return exp;
/*     */     } 
/*     */ 
/*     */     
/* 198 */     this.reader.setDeclaredLocationOf(ignoreItem);
/*     */ 
/*     */     
/* 201 */     ((OtherExp)ignoreItem).exp = exp;
/* 202 */     return (Expression)ignoreItem;
/*     */   }
/*     */ 
/*     */   
/*     */   private void reportError(String msg, Locator locator) {
/* 207 */     this.reader.controller.error(new Locator[] { locator }, msg, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\decorator\RoleBasedDecorator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */