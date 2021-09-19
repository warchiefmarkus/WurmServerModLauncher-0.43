/*     */ package com.sun.xml.bind.v2.model.nav;
/*     */ 
/*     */ import com.sun.xml.bind.v2.runtime.Location;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Navigator<T, C, F, M>
/*     */ {
/* 256 */   public static final ReflectionNavigator REFLECTION = new ReflectionNavigator();
/*     */   
/*     */   C getSuperClass(C paramC);
/*     */   
/*     */   T getBaseClass(T paramT, C paramC);
/*     */   
/*     */   String getClassName(C paramC);
/*     */   
/*     */   String getTypeName(T paramT);
/*     */   
/*     */   String getClassShortName(C paramC);
/*     */   
/*     */   Collection<? extends F> getDeclaredFields(C paramC);
/*     */   
/*     */   F getDeclaredField(C paramC, String paramString);
/*     */   
/*     */   Collection<? extends M> getDeclaredMethods(C paramC);
/*     */   
/*     */   C getDeclaringClassForField(F paramF);
/*     */   
/*     */   C getDeclaringClassForMethod(M paramM);
/*     */   
/*     */   T getFieldType(F paramF);
/*     */   
/*     */   String getFieldName(F paramF);
/*     */   
/*     */   String getMethodName(M paramM);
/*     */   
/*     */   T getReturnType(M paramM);
/*     */   
/*     */   T[] getMethodParameters(M paramM);
/*     */   
/*     */   boolean isStaticMethod(M paramM);
/*     */   
/*     */   boolean isSubClassOf(T paramT1, T paramT2);
/*     */   
/*     */   T ref(Class paramClass);
/*     */   
/*     */   T use(C paramC);
/*     */   
/*     */   C asDecl(T paramT);
/*     */   
/*     */   C asDecl(Class paramClass);
/*     */   
/*     */   boolean isArray(T paramT);
/*     */   
/*     */   boolean isArrayButNotByteArray(T paramT);
/*     */   
/*     */   T getComponentType(T paramT);
/*     */   
/*     */   T getTypeArgument(T paramT, int paramInt);
/*     */   
/*     */   boolean isParameterizedType(T paramT);
/*     */   
/*     */   boolean isPrimitive(T paramT);
/*     */   
/*     */   T getPrimitive(Class paramClass);
/*     */   
/*     */   Location getClassLocation(C paramC);
/*     */   
/*     */   Location getFieldLocation(F paramF);
/*     */   
/*     */   Location getMethodLocation(M paramM);
/*     */   
/*     */   boolean hasDefaultConstructor(C paramC);
/*     */   
/*     */   boolean isStaticField(F paramF);
/*     */   
/*     */   boolean isPublicMethod(M paramM);
/*     */   
/*     */   boolean isPublicField(F paramF);
/*     */   
/*     */   boolean isEnum(C paramC);
/*     */   
/*     */   <P> T erasure(T paramT);
/*     */   
/*     */   boolean isAbstract(C paramC);
/*     */   
/*     */   boolean isFinal(C paramC);
/*     */   
/*     */   F[] getEnumConstants(C paramC);
/*     */   
/*     */   T getVoidType();
/*     */   
/*     */   String getPackageName(C paramC);
/*     */   
/*     */   C findClass(String paramString, C paramC);
/*     */   
/*     */   boolean isBridgeMethod(M paramM);
/*     */   
/*     */   boolean isOverriding(M paramM, C paramC);
/*     */   
/*     */   boolean isInterface(C paramC);
/*     */   
/*     */   boolean isTransient(F paramF);
/*     */   
/*     */   boolean isInnerClass(C paramC);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\nav\Navigator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */