/*    */ package org.seamless.statemachine;
/*    */ 
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StateMachineBuilder
/*    */ {
/*    */   public static <T extends StateMachine> T build(Class<T> stateMachine, Class initialState) {
/* 26 */     return build(stateMachine, initialState, null, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends StateMachine> T build(Class<T> stateMachine, Class<?> initialState, Class[] constructorArgumentTypes, Object[] constructorArguments) {
/* 33 */     return (T)Proxy.newProxyInstance(stateMachine.getClassLoader(), new Class[] { stateMachine }, new StateMachineInvocationHandler(Arrays.asList(((States)stateMachine.<States>getAnnotation(States.class)).value()), initialState, constructorArgumentTypes, constructorArguments));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\statemachine\StateMachineBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */