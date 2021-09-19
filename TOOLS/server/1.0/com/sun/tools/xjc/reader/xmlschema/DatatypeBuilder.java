/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.msv.datatype.xsd.DatatypeFactory;
/*     */ import com.sun.msv.datatype.xsd.ErrorType;
/*     */ import com.sun.msv.datatype.xsd.TypeIncubator;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.datatype.xsd.XSDatatypeImpl;
/*     */ import com.sun.tools.xjc.reader.Const;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.relaxng.datatype.DatatypeException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatatypeBuilder
/*     */   implements XSSimpleTypeFunction
/*     */ {
/*     */   private final BGMBuilder builder;
/*     */   private final Map cache;
/*     */   
/*     */   DatatypeBuilder(BGMBuilder builder, XSSchemaSet schemas) {
/*  59 */     this.cache = new HashMap(); this.builder = builder; try {
/*     */       for (int i = 0; i < Const.builtinTypeNames.length; i++) {
/*     */         XSSimpleType type = schemas.getSimpleType("http://www.w3.org/2001/XMLSchema", Const.builtinTypeNames[i]); _assert((type != null)); this.cache.put(type, DatatypeFactory.getTypeByName(Const.builtinTypeNames[i]));
/*     */       } 
/*     */     } catch (DatatypeException e) {
/*     */       e.printStackTrace(); _assert(false);
/*  65 */     }  } public XSDatatype build(XSSimpleType type) { return (XSDatatype)type.apply(this); }
/*     */ 
/*     */   
/*     */   public Object restrictionSimpleType(XSRestrictionSimpleType type) {
/*  69 */     XSDatatype dt = (XSDatatype)this.cache.get(type);
/*  70 */     if (dt != null) return dt;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  75 */       TypeIncubator ti = new TypeIncubator(build(type.getSimpleBaseType()));
/*     */       
/*  77 */       Iterator itr = type.iterateDeclaredFacets();
/*  78 */       while (itr.hasNext()) {
/*  79 */         XSFacet facet = itr.next();
/*  80 */         ti.addFacet(facet.getName(), facet.getValue(), facet.isFixed(), facet.getContext());
/*     */       } 
/*  82 */       XSDatatypeImpl xSDatatypeImpl = ti.derive(type.getTargetNamespace(), type.getName());
/*     */ 
/*     */       
/*  85 */       this.cache.put(type, xSDatatypeImpl);
/*  86 */       return xSDatatypeImpl;
/*  87 */     } catch (DatatypeException e) {
/*  88 */       this.builder.errorReporter.error(type.getLocator(), "DatatypeBuilder.DatatypeError", e.getMessage());
/*     */       
/*  90 */       return ErrorType.theInstance;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object listSimpleType(XSListSimpleType type) {
/*  95 */     XSDatatype dt = (XSDatatype)this.cache.get(type);
/*  96 */     if (dt != null) return dt;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 101 */       dt = DatatypeFactory.deriveByList(type.getTargetNamespace(), type.getName(), build(type.getItemType()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       this.cache.put(type, dt);
/* 108 */       return dt;
/* 109 */     } catch (DatatypeException e) {
/* 110 */       this.builder.errorReporter.error(type.getLocator(), "DatatypeBuilder.DatatypeError", e.getMessage());
/*     */       
/* 112 */       return ErrorType.theInstance;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object unionSimpleType(XSUnionSimpleType type) {
/* 117 */     XSDatatype dt = (XSDatatype)this.cache.get(type);
/* 118 */     if (dt != null) return dt;
/*     */ 
/*     */     
/*     */     try {
/* 122 */       XSDatatype[] members = new XSDatatype[type.getMemberSize()];
/* 123 */       for (int i = 0; i < members.length; i++) {
/* 124 */         members[i] = build(type.getMember(i));
/*     */       }
/* 126 */       dt = DatatypeFactory.deriveByUnion(type.getTargetNamespace(), type.getName(), members);
/*     */ 
/*     */ 
/*     */       
/* 130 */       this.cache.put(type, dt);
/* 131 */       return dt;
/* 132 */     } catch (DatatypeException e) {
/* 133 */       this.builder.errorReporter.error(type.getLocator(), "DatatypeBuilder.DatatypeError", e.getMessage());
/*     */       
/* 135 */       return ErrorType.theInstance;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void _assert(boolean b) {
/* 142 */     if (!b)
/* 143 */       throw new JAXBAssertionError(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\DatatypeBuilder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */