/*     */ package org.fourthline.cling.model.action;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.fourthline.cling.model.meta.Action;
/*     */ import org.fourthline.cling.model.meta.ActionArgument;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.profile.ClientInfo;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActionInvocation<S extends Service>
/*     */ {
/*     */   protected final Action<S> action;
/*     */   protected final ClientInfo clientInfo;
/*  40 */   protected Map<String, ActionArgumentValue<S>> input = new LinkedHashMap<>();
/*  41 */   protected Map<String, ActionArgumentValue<S>> output = new LinkedHashMap<>();
/*     */   
/*  43 */   protected ActionException failure = null;
/*     */   
/*     */   public ActionInvocation(Action<S> action) {
/*  46 */     this(action, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionInvocation(Action<S> action, ClientInfo clientInfo) {
/*  51 */     this(action, null, null, clientInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionInvocation(Action<S> action, ActionArgumentValue<S>[] input) {
/*  56 */     this(action, input, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionInvocation(Action<S> action, ActionArgumentValue<S>[] input, ClientInfo clientInfo) {
/*  62 */     this(action, input, null, clientInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionInvocation(Action<S> action, ActionArgumentValue<S>[] input, ActionArgumentValue<S>[] output) {
/*  68 */     this(action, input, output, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionInvocation(Action<S> action, ActionArgumentValue<S>[] input, ActionArgumentValue<S>[] output, ClientInfo clientInfo) {
/*  75 */     if (action == null) {
/*  76 */       throw new IllegalArgumentException("Action can not be null");
/*     */     }
/*  78 */     this.action = action;
/*     */     
/*  80 */     setInput(input);
/*  81 */     setOutput(output);
/*     */     
/*  83 */     this.clientInfo = clientInfo;
/*     */   }
/*     */   
/*     */   public ActionInvocation(ActionException failure) {
/*  87 */     this.action = null;
/*  88 */     this.input = null;
/*  89 */     this.output = null;
/*  90 */     this.failure = failure;
/*  91 */     this.clientInfo = null;
/*     */   }
/*     */   
/*     */   public Action<S> getAction() {
/*  95 */     return this.action;
/*     */   }
/*     */   
/*     */   public ActionArgumentValue<S>[] getInput() {
/*  99 */     return (ActionArgumentValue<S>[])this.input.values().toArray((Object[])new ActionArgumentValue[this.input.size()]);
/*     */   }
/*     */   
/*     */   public ActionArgumentValue<S> getInput(String argumentName) {
/* 103 */     return getInput(getInputArgument(argumentName));
/*     */   }
/*     */   
/*     */   public ActionArgumentValue<S> getInput(ActionArgument<S> argument) {
/* 107 */     return this.input.get(argument.getName());
/*     */   }
/*     */   
/*     */   public Map<String, ActionArgumentValue<S>> getInputMap() {
/* 111 */     return Collections.unmodifiableMap(this.input);
/*     */   }
/*     */   
/*     */   public ActionArgumentValue<S>[] getOutput() {
/* 115 */     return (ActionArgumentValue<S>[])this.output.values().toArray((Object[])new ActionArgumentValue[this.output.size()]);
/*     */   }
/*     */   
/*     */   public ActionArgumentValue<S> getOutput(String argumentName) {
/* 119 */     return getOutput(getOutputArgument(argumentName));
/*     */   }
/*     */   
/*     */   public Map<String, ActionArgumentValue<S>> getOutputMap() {
/* 123 */     return Collections.unmodifiableMap(this.output);
/*     */   }
/*     */   
/*     */   public ActionArgumentValue<S> getOutput(ActionArgument<S> argument) {
/* 127 */     return this.output.get(argument.getName());
/*     */   }
/*     */   
/*     */   public void setInput(String argumentName, Object value) throws InvalidValueException {
/* 131 */     setInput(new ActionArgumentValue<>(getInputArgument(argumentName), value));
/*     */   }
/*     */   
/*     */   public void setInput(ActionArgumentValue<S> value) {
/* 135 */     this.input.put(value.getArgument().getName(), value);
/*     */   }
/*     */   
/*     */   public void setInput(ActionArgumentValue<S>[] input) {
/* 139 */     if (input == null)
/* 140 */       return;  for (ActionArgumentValue<S> argumentValue : input) {
/* 141 */       this.input.put(argumentValue.getArgument().getName(), argumentValue);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOutput(String argumentName, Object value) throws InvalidValueException {
/* 146 */     setOutput(new ActionArgumentValue<>(getOutputArgument(argumentName), value));
/*     */   }
/*     */   
/*     */   public void setOutput(ActionArgumentValue<S> value) {
/* 150 */     this.output.put(value.getArgument().getName(), value);
/*     */   }
/*     */   
/*     */   public void setOutput(ActionArgumentValue<S>[] output) {
/* 154 */     if (output == null)
/* 155 */       return;  for (ActionArgumentValue<S> argumentValue : output) {
/* 156 */       this.output.put(argumentValue.getArgument().getName(), argumentValue);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ActionArgument<S> getInputArgument(String name) {
/* 161 */     ActionArgument<S> argument = getAction().getInputArgument(name);
/* 162 */     if (argument == null) throw new IllegalArgumentException("Argument not found: " + name); 
/* 163 */     return argument;
/*     */   }
/*     */   
/*     */   protected ActionArgument<S> getOutputArgument(String name) {
/* 167 */     ActionArgument<S> argument = getAction().getOutputArgument(name);
/* 168 */     if (argument == null) throw new IllegalArgumentException("Argument not found: " + name); 
/* 169 */     return argument;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActionException getFailure() {
/* 176 */     return this.failure;
/*     */   }
/*     */   
/*     */   public void setFailure(ActionException failure) {
/* 180 */     this.failure = failure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientInfo getClientInfo() {
/* 187 */     return this.clientInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 192 */     return "(" + getClass().getSimpleName() + ") " + getAction();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\action\ActionInvocation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */