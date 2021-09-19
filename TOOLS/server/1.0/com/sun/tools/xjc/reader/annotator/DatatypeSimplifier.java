/*     */ package 1.0.com.sun.tools.xjc.reader.annotator;
/*     */ 
/*     */ import com.sun.msv.datatype.xsd.EnumerationFacet;
/*     */ import com.sun.msv.datatype.xsd.ListType;
/*     */ import com.sun.msv.datatype.xsd.StringType;
/*     */ import com.sun.msv.datatype.xsd.UnionType;
/*     */ import com.sun.msv.datatype.xsd.XSDatatype;
/*     */ import com.sun.msv.grammar.DataExp;
/*     */ import com.sun.msv.grammar.Expression;
/*     */ import com.sun.msv.grammar.ExpressionPool;
/*     */ import com.sun.tools.xjc.grammar.util.BreadthFirstExpressionCloner;
/*     */ import com.sun.tools.xjc.reader.annotator.Messages;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatatypeSimplifier
/*     */   extends BreadthFirstExpressionCloner
/*     */ {
/*     */   private final Map dataExps;
/*     */   
/*     */   public DatatypeSimplifier(ExpressionPool pool) {
/*  57 */     super(pool);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     this.dataExps = new HashMap();
/*     */   }
/*     */   public Expression onAnyString() {
/*  66 */     return this.pool.createData((XSDatatype)StringType.theInstance);
/*     */   }
/*     */   
/*     */   public Expression onData(DataExp exp) {
/*  70 */     if (!(exp.dt instanceof XSDatatype))
/*     */     {
/*  72 */       return (Expression)exp;
/*     */     }
/*  74 */     Expression r = (Expression)this.dataExps.get(exp);
/*  75 */     if (r == null) {
/*  76 */       r = processDatatype((XSDatatype)exp.dt, false);
/*  77 */       this.dataExps.put(exp, r);
/*     */     } 
/*  79 */     return r;
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
/*     */   private Expression processDatatype(XSDatatype dt, boolean inList) {
/*  97 */     EnumerationFacet ef = (EnumerationFacet)dt.getFacetObject("enumeration");
/*     */     
/*  99 */     if (ef != null) {
/* 100 */       return processEnumeration(dt, ef);
/*     */     }
/*     */     
/* 103 */     switch (dt.getVariety()) {
/*     */       
/*     */       case 1:
/* 106 */         return this.pool.createData(dt);
/*     */       
/*     */       case 3:
/* 109 */         return processUnion(dt, inList);
/*     */       
/*     */       case 2:
/* 112 */         return processList(dt, inList);
/*     */     } 
/*     */     
/* 115 */     throw new Error();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression processEnumeration(XSDatatype type, EnumerationFacet enums) {
/* 124 */     Expression exp = Expression.nullSet;
/*     */     
/* 126 */     Iterator itr = enums.values.iterator();
/* 127 */     while (itr.hasNext()) {
/* 128 */       Object v = itr.next();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       exp = this.pool.createChoice(exp, this.pool.createValue((Datatype)type, null, v));
/*     */     } 
/*     */ 
/*     */     
/* 139 */     return exp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Expression processUnion(XSDatatype dt, boolean inList) {
/* 147 */     if (dt.getFacetObject("enumeration") != null) {
/* 148 */       throw new Error(Messages.format("DatatypeSimplifier.EnumFacetUnsupported"));
/*     */     }
/* 150 */     if (dt.getFacetObject("pattern") != null) {
/* 151 */       throw new Error(Messages.format("DatatypeSimplifier.PatternFacetUnsupported"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     while (!(dt instanceof UnionType)) {
/* 158 */       dt = dt.getBaseType();
/* 159 */       if (dt == null)
/*     */       {
/*     */         
/* 162 */         throw new Error();
/*     */       }
/*     */     } 
/* 165 */     UnionType ut = (UnionType)dt;
/* 166 */     Expression exp = Expression.nullSet;
/*     */ 
/*     */     
/* 169 */     for (int i = 0; i < ut.memberTypes.length; i++)
/*     */     {
/* 171 */       exp = this.pool.createChoice(exp, processDatatype((XSDatatype)ut.memberTypes[i], inList));
/*     */     }
/* 173 */     return exp;
/*     */   }
/*     */   
/*     */   private Expression processList(XSDatatype dt, boolean inList) {
/* 177 */     if (dt.getFacetObject("enumeration") != null) {
/* 178 */       throw new Error(Messages.format("DatatypeSimplifier.EnumFacetUnsupported"));
/*     */     }
/* 180 */     if (dt.getFacetObject("pattern") != null) {
/* 181 */       throw new Error(Messages.format("DatatypeSimplifier.PatternFacetUnsupported"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 186 */     XSDatatype d = dt;
/* 187 */     while (!(d instanceof ListType)) {
/* 188 */       d = d.getBaseType();
/* 189 */       if (d == null)
/*     */       {
/*     */         
/* 192 */         throw new Error(); } 
/*     */     } 
/* 194 */     ListType lt = (ListType)d;
/*     */ 
/*     */     
/* 197 */     Expression item = processDatatype((XSDatatype)lt.itemType, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     Expression exp = this.pool.createZeroOrMore(item);
/*     */     
/* 205 */     if (inList) return exp; 
/* 206 */     return this.pool.createList(exp);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\annotator\DatatypeSimplifier.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */