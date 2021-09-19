/*     */ package org.seamless.statemachine;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StateMachineInvocationHandler
/*     */   implements InvocationHandler
/*     */ {
/*  30 */   private static Logger log = Logger.getLogger(StateMachineInvocationHandler.class.getName());
/*     */   
/*     */   public static final String METHOD_ON_ENTRY = "onEntry";
/*     */   
/*     */   public static final String METHOD_ON_EXIT = "onExit";
/*     */   final Class initialStateClass;
/*  36 */   final Map<Class, Object> stateObjects = (Map)new ConcurrentHashMap<Class<?>, Object>();
/*     */ 
/*     */   
/*     */   Object currentState;
/*     */ 
/*     */ 
/*     */   
/*     */   StateMachineInvocationHandler(List<Class<?>> stateClasses, Class<?> initialStateClass, Class[] constructorArgumentTypes, Object[] constructorArguments) {
/*  44 */     log.fine("Creating state machine with initial state: " + initialStateClass);
/*     */     
/*  46 */     this.initialStateClass = initialStateClass;
/*     */     
/*  48 */     for (Class<?> stateClass : stateClasses) {
/*     */       
/*     */       try {
/*  51 */         Object state = (constructorArgumentTypes != null) ? stateClass.getConstructor(constructorArgumentTypes).newInstance(constructorArguments) : stateClass.newInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  58 */         log.fine("Adding state instance: " + state.getClass().getName());
/*  59 */         this.stateObjects.put(stateClass, state);
/*     */       }
/*  61 */       catch (NoSuchMethodException ex) {
/*  62 */         throw new RuntimeException("State " + stateClass.getName() + " has the wrong constructor: " + ex, ex);
/*     */       
/*     */       }
/*  65 */       catch (Exception ex) {
/*  66 */         throw new RuntimeException("State " + stateClass.getName() + " can't be instantiated: " + ex, ex);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  72 */     if (!this.stateObjects.containsKey(initialStateClass)) {
/*  73 */       throw new RuntimeException("Initial state not in list of states: " + initialStateClass);
/*     */     }
/*     */     
/*  76 */     this.currentState = this.stateObjects.get(initialStateClass);
/*  77 */     synchronized (this) {
/*  78 */       invokeEntryMethod(this.currentState);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  83 */     synchronized (this) {
/*     */       
/*  85 */       if ("getCurrentState".equals(method.getName()) && (method.getParameterTypes()).length == 0)
/*     */       {
/*  87 */         return this.currentState;
/*     */       }
/*     */       
/*  90 */       if ("forceState".equals(method.getName()) && (method.getParameterTypes()).length == 1 && args.length == 1 && args[0] != null && args[0] instanceof Class) {
/*     */ 
/*     */         
/*  93 */         Object forcedState = this.stateObjects.get(args[0]);
/*  94 */         if (forcedState == null) {
/*  95 */           throw new TransitionException("Can't force to invalid state: " + args[0]);
/*     */         }
/*  97 */         log.finer("Forcing state machine into state: " + forcedState.getClass().getName());
/*  98 */         invokeExitMethod(this.currentState);
/*  99 */         this.currentState = forcedState;
/* 100 */         invokeEntryMethod(forcedState);
/* 101 */         return null;
/*     */       } 
/*     */       
/* 104 */       Method signalMethod = getMethodOfCurrentState(method);
/* 105 */       log.fine("Invoking signal method of current state: " + signalMethod.toString());
/* 106 */       Object methodReturn = signalMethod.invoke(this.currentState, args);
/*     */       
/* 108 */       if (methodReturn != null && methodReturn instanceof Class) {
/* 109 */         Class nextStateClass = (Class)methodReturn;
/* 110 */         if (this.stateObjects.containsKey(nextStateClass)) {
/* 111 */           log.fine("Executing transition to next state: " + nextStateClass.getName());
/* 112 */           invokeExitMethod(this.currentState);
/* 113 */           this.currentState = this.stateObjects.get(nextStateClass);
/* 114 */           invokeEntryMethod(this.currentState);
/*     */         } 
/*     */       } 
/* 117 */       return methodReturn;
/*     */     } 
/*     */   }
/*     */   
/*     */   private Method getMethodOfCurrentState(Method method) {
/*     */     try {
/* 123 */       return this.currentState.getClass().getMethod(method.getName(), method.getParameterTypes());
/*     */ 
/*     */     
/*     */     }
/* 127 */     catch (NoSuchMethodException ex) {
/* 128 */       throw new TransitionException("State '" + this.currentState.getClass().getName() + "' doesn't support signal '" + method.getName() + "'");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void invokeEntryMethod(Object state) {
/* 135 */     log.fine("Trying to invoke entry method of state: " + state.getClass().getName());
/*     */     try {
/* 137 */       Method onEntryMethod = state.getClass().getMethod("onEntry", new Class[0]);
/* 138 */       onEntryMethod.invoke(state, new Object[0]);
/* 139 */     } catch (NoSuchMethodException ex) {
/* 140 */       log.finer("No entry method found on state: " + state.getClass().getName());
/*     */     }
/* 142 */     catch (Exception ex) {
/* 143 */       throw new TransitionException("State '" + state.getClass().getName() + "' entry method threw exception: " + ex, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void invokeExitMethod(Object state) {
/* 150 */     log.finer("Trying to invoking exit method of state: " + state.getClass().getName());
/*     */     try {
/* 152 */       Method onExitMethod = state.getClass().getMethod("onExit", new Class[0]);
/* 153 */       onExitMethod.invoke(state, new Object[0]);
/* 154 */     } catch (NoSuchMethodException ex) {
/* 155 */       log.finer("No exit method found on state: " + state.getClass().getName());
/*     */     }
/* 157 */     catch (Exception ex) {
/* 158 */       throw new TransitionException("State '" + state.getClass().getName() + "' exit method threw exception: " + ex, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\statemachine\StateMachineInvocationHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */