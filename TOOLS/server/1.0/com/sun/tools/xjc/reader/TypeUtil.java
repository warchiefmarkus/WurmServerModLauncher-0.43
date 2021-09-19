/*     */ package 1.0.com.sun.tools.xjc.reader;
/*     */ 
/*     */ import com.sun.codemodel.JClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.reader.Messages;
/*     */ import com.sun.xml.bind.JAXBAssertionError;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class TypeUtil
/*     */ {
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, Set types) {
/*  40 */     return getCommonBaseType(codeModel, (JType[])types.toArray((Object[])new JType[types.size()]));
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
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, JType[] t) {
/*  55 */     Set uniqueTypes = new TreeSet(typeComparator);
/*  56 */     for (int i = 0; i < t.length; i++) {
/*  57 */       uniqueTypes.add(t[i]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (uniqueTypes.size() == 1)
/*  63 */       return uniqueTypes.iterator().next(); 
/*  64 */     if (uniqueTypes.size() == 0)
/*     */     {
/*  66 */       throw new JAXBAssertionError();
/*     */     }
/*     */     
/*  69 */     Set s = null;
/*  70 */     for (Iterator itr = uniqueTypes.iterator(); itr.hasNext(); ) {
/*  71 */       JType type = itr.next();
/*  72 */       if (type == codeModel.NULL) {
/*     */         continue;
/*     */       }
/*     */       
/*  76 */       JClass cls = box(codeModel, type);
/*     */       
/*  78 */       if (s == null) {
/*  79 */         s = getAssignableTypes(cls); continue;
/*     */       } 
/*  81 */       s.retainAll(getAssignableTypes(cls));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     JClass[] raw = (JClass[])s.toArray((Object[])new JClass[s.size()]);
/*     */     
/*  90 */     s.clear();
/*  91 */     for (int j = 0; j < raw.length; j++) {
/*     */       int k;
/*  93 */       for (k = 0; k < raw.length; k++) {
/*  94 */         if (j != k)
/*     */         {
/*     */           
/*  97 */           if (raw[j].isAssignableFrom(raw[k]))
/*     */             break; 
/*     */         }
/*     */       } 
/* 101 */       if (k == raw.length)
/*     */       {
/* 103 */         s.add(raw[j]);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     Iterator iterator = s.iterator();
/* 114 */     while (iterator.hasNext()) {
/* 115 */       JClass c = iterator.next();
/* 116 */       if (c instanceof com.sun.codemodel.JDefinedClass) {
/* 117 */         return (JType)c;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 123 */     return (JType)s.iterator().next();
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
/*     */   public static Set getAssignableTypes(JClass t) {
/* 135 */     Set s = new TreeSet(typeComparator);
/*     */ 
/*     */     
/* 138 */     s.add(t.owner().ref(Object.class));
/*     */     
/* 140 */     _getAssignableTypes(t, s);
/* 141 */     return s;
/*     */   }
/*     */   
/*     */   private static void _getAssignableTypes(JClass t, Set s) {
/* 145 */     if (!s.add(t)) {
/*     */       return;
/*     */     }
/*     */     
/* 149 */     JClass _super = t._extends();
/* 150 */     if (_super != null) {
/* 151 */       _getAssignableTypes(_super, s);
/*     */     }
/*     */     
/* 154 */     Iterator itr = t._implements();
/* 155 */     while (itr.hasNext()) {
/* 156 */       _getAssignableTypes(itr.next(), s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JClass box(JCodeModel codeModel, JType t) {
/* 165 */     if (t instanceof JClass) {
/* 166 */       return (JClass)t;
/*     */     }
/* 168 */     return ((JPrimitiveType)t).getWrapperClass();
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
/*     */   public static JType getType(JCodeModel codeModel, String typeName, ErrorHandler errorHandler, Locator errorSource) throws SAXException {
/*     */     try {
/* 183 */       return (JType)JType.parse(codeModel, typeName);
/* 184 */     } catch (IllegalArgumentException e) {
/*     */       
/*     */       try {
/* 187 */         return (JType)codeModel.ref(typeName);
/* 188 */       } catch (ClassNotFoundException ee) {
/*     */         
/* 190 */         errorHandler.error(new SAXParseException(Messages.format("TypeUtil.ClassNotFound", typeName), errorSource));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 195 */         return (JType)codeModel.ref(Object.class);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   private static final Comparator typeComparator = (Comparator)new Object();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\TypeUtil.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */