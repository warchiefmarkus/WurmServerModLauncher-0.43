/*     */ package com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.ErrorReceiver;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class TypeUtil
/*     */ {
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, Collection<? extends JType> types) {
/*  72 */     return getCommonBaseType(codeModel, types.<JType>toArray(new JType[types.size()]));
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
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, JType... t) {
/*  87 */     Set<JType> uniqueTypes = new TreeSet<JType>(typeComparator);
/*  88 */     for (JType type : t) {
/*  89 */       uniqueTypes.add(type);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  94 */     if (uniqueTypes.size() == 1) {
/*  95 */       return uniqueTypes.iterator().next();
/*     */     }
/*     */     
/*  98 */     assert !uniqueTypes.isEmpty();
/*     */ 
/*     */     
/* 101 */     uniqueTypes.remove(codeModel.NULL);
/*     */ 
/*     */     
/* 104 */     Set<JClass> s = null;
/*     */     
/* 106 */     for (JType type : uniqueTypes) {
/* 107 */       JClass cls = type.boxify();
/*     */       
/* 109 */       if (s == null) {
/* 110 */         s = getAssignableTypes(cls); continue;
/*     */       } 
/* 112 */       s.retainAll(getAssignableTypes(cls));
/*     */     } 
/*     */ 
/*     */     
/* 116 */     s.add(codeModel.ref(Object.class));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     JClass[] raw = s.<JClass>toArray(new JClass[s.size()]);
/* 123 */     s.clear();
/*     */     
/* 125 */     for (int i = 0; i < raw.length; i++) {
/*     */       int k;
/* 127 */       for (k = 0; k < raw.length; k++) {
/* 128 */         if (i != k)
/*     */         {
/*     */           
/* 131 */           if (raw[i].isAssignableFrom(raw[k]))
/*     */             break; 
/*     */         }
/*     */       } 
/* 135 */       if (k == raw.length)
/*     */       {
/* 137 */         s.add(raw[i]);
/*     */       }
/*     */     } 
/* 140 */     assert !s.isEmpty();
/*     */ 
/*     */     
/* 143 */     JClass result = pickOne(s);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (result.isParameterized()) {
/* 152 */       return (JType)result;
/*     */     }
/*     */     
/* 155 */     List<List<JClass>> parameters = new ArrayList<List<JClass>>(uniqueTypes.size());
/* 156 */     int paramLen = -1;
/*     */     
/* 158 */     for (JType type : uniqueTypes) {
/* 159 */       JClass cls = type.boxify();
/* 160 */       JClass bp = cls.getBaseClass(result);
/*     */ 
/*     */ 
/*     */       
/* 164 */       if (bp.equals(result)) {
/* 165 */         return (JType)result;
/*     */       }
/* 167 */       assert bp.isParameterized();
/* 168 */       List<JClass> tp = bp.getTypeParameters();
/* 169 */       parameters.add(tp);
/*     */       
/* 171 */       assert paramLen == -1 || paramLen == tp.size();
/*     */ 
/*     */       
/* 174 */       paramLen = tp.size();
/*     */     } 
/*     */     
/* 177 */     List<JClass> paramResult = new ArrayList<JClass>();
/* 178 */     List<JClass> argList = new ArrayList<JClass>(parameters.size());
/*     */     
/* 180 */     for (int j = 0; j < paramLen; j++) {
/* 181 */       argList.clear();
/* 182 */       for (List<JClass> list : parameters) {
/* 183 */         argList.add(list.get(j));
/*     */       }
/*     */       
/* 186 */       JClass bound = (JClass)getCommonBaseType(codeModel, (Collection)argList);
/* 187 */       boolean allSame = true;
/* 188 */       for (JClass a : argList)
/* 189 */         allSame &= a.equals(bound); 
/* 190 */       if (!allSame) {
/* 191 */         bound = bound.wildcard();
/*     */       }
/* 193 */       paramResult.add(bound);
/*     */     } 
/*     */     
/* 196 */     return (JType)result.narrow(paramResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JClass pickOne(Set<JClass> s) {
/* 205 */     for (JClass c : s) {
/* 206 */       if (c instanceof com.sun.codemodel.JDefinedClass) {
/* 207 */         return c;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 212 */     return s.iterator().next();
/*     */   }
/*     */   
/*     */   private static Set<JClass> getAssignableTypes(JClass t) {
/* 216 */     Set<JClass> r = new TreeSet(typeComparator);
/* 217 */     getAssignableTypes(t, r);
/* 218 */     return r;
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
/*     */   private static void getAssignableTypes(JClass t, Set<JClass> s) {
/* 230 */     if (!s.add(t)) {
/*     */       return;
/*     */     }
/*     */     
/* 234 */     s.add(t.erasure());
/*     */ 
/*     */ 
/*     */     
/* 238 */     JClass _super = t._extends();
/* 239 */     if (_super != null) {
/* 240 */       getAssignableTypes(_super, s);
/*     */     }
/*     */     
/* 243 */     Iterator<JClass> itr = t._implements();
/* 244 */     while (itr.hasNext()) {
/* 245 */       getAssignableTypes(itr.next(), s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JType getType(JCodeModel codeModel, String typeName, ErrorReceiver errorHandler, Locator errorSource) {
/*     */     try {
/* 256 */       return codeModel.parseType(typeName);
/* 257 */     } catch (ClassNotFoundException ee) {
/*     */ 
/*     */       
/* 260 */       errorHandler.warning(new SAXParseException(Messages.ERR_CLASS_NOT_FOUND.format(new Object[] { typeName }, ), errorSource));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 265 */       return (JType)codeModel.directClass(typeName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 272 */   private static final Comparator<JType> typeComparator = new Comparator<JType>() {
/*     */       public int compare(JType t1, JType t2) {
/* 274 */         return t1.fullName().compareTo(t2.fullName());
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\TypeUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */