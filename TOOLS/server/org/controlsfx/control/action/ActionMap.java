/*     */ package org.controlsfx.control.action;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import javafx.event.ActionEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActionMap
/*     */ {
/*  69 */   private static AnnotatedActionFactory actionFactory = new DefaultActionFactory();
/*     */   
/*  71 */   private static final Map<String, AnnotatedAction> actions = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AnnotatedActionFactory getActionFactory() {
/*  84 */     return actionFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setActionFactory(AnnotatedActionFactory factory) {
/*  92 */     Objects.requireNonNull(factory);
/*  93 */     actionFactory = factory;
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
/*     */   public static void register(Object target) {
/* 116 */     for (Method method : target.getClass().getDeclaredMethods()) {
/*     */       
/* 118 */       Annotation[] annotations = method.getAnnotationsByType((Class)ActionProxy.class);
/* 119 */       if (annotations.length != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 127 */         int paramCount = method.getParameterCount();
/* 128 */         Class[] paramTypes = method.getParameterTypes();
/*     */         
/* 130 */         if (paramCount > 2) {
/* 131 */           throw new IllegalArgumentException(String.format("Method %s has too many parameters", new Object[] { method.getName() }));
/*     */         }
/*     */         
/* 134 */         if (paramCount == 1 && !ActionEvent.class.isAssignableFrom(paramTypes[0])) {
/* 135 */           throw new IllegalArgumentException(String.format("Method %s -- single parameter must be of type ActionEvent", new Object[] { method.getName() }));
/*     */         }
/*     */         
/* 138 */         if (paramCount == 2 && (!ActionEvent.class.isAssignableFrom(paramTypes[0]) || 
/* 139 */           !Action.class.isAssignableFrom(paramTypes[1]))) {
/* 140 */           throw new IllegalArgumentException(String.format("Method %s -- parameters must be of types (ActionEvent, Action)", new Object[] { method.getName() }));
/*     */         }
/*     */         
/* 143 */         ActionProxy annotation = (ActionProxy)annotations[0];
/*     */         
/* 145 */         AnnotatedActionFactory factory = determineActionFactory(annotation);
/* 146 */         AnnotatedAction action = factory.createAction(annotation, method, target);
/*     */         
/* 148 */         String id = annotation.id().isEmpty() ? method.getName() : annotation.id();
/* 149 */         actions.put(id, action);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static AnnotatedActionFactory determineActionFactory(ActionProxy annotation) {
/* 158 */     AnnotatedActionFactory factory = actionFactory;
/*     */ 
/*     */ 
/*     */     
/* 162 */     String factoryClassName = annotation.factory();
/* 163 */     if (!factoryClassName.isEmpty()) {
/*     */       try {
/* 165 */         Class<?> factoryClass = Class.forName(factoryClassName);
/* 166 */         factory = (AnnotatedActionFactory)factoryClass.newInstance();
/*     */       }
/* 168 */       catch (ClassNotFoundException ex) {
/* 169 */         throw new IllegalArgumentException(String.format("Action proxy refers to non-existant factory class %s", new Object[] { factoryClassName }), ex);
/*     */       }
/* 171 */       catch (InstantiationException|IllegalAccessException ex) {
/* 172 */         throw new IllegalStateException(String.format("Unable to instantiate action factory class %s", new Object[] { factoryClassName }), ex);
/*     */       } 
/*     */     }
/*     */     
/* 176 */     return factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregister(Object target) {
/* 185 */     if (target != null) {
/* 186 */       Iterator<Map.Entry<String, AnnotatedAction>> entryIter = actions.entrySet().iterator();
/* 187 */       while (entryIter.hasNext()) {
/* 188 */         Map.Entry<String, AnnotatedAction> entry = entryIter.next();
/*     */         
/* 190 */         Object actionTarget = ((AnnotatedAction)entry.getValue()).getTarget();
/*     */         
/* 192 */         if (actionTarget == null || actionTarget == target) {
/* 193 */           entryIter.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Action action(String id) {
/* 205 */     return actions.get(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection<Action> actions(String... ids) {
/* 216 */     List<Action> result = new ArrayList<>();
/* 217 */     for (String id : ids) {
/* 218 */       if (id.startsWith("---")) result.add(ActionUtils.ACTION_SEPARATOR); 
/* 219 */       Action action = action(id);
/* 220 */       if (action != null) result.add(action); 
/*     */     } 
/* 222 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\ActionMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */