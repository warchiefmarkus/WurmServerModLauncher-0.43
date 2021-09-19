/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.Validatable;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Action<S extends Service>
/*     */   implements Validatable
/*     */ {
/*  33 */   private static final Logger log = Logger.getLogger(Action.class.getName());
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ActionArgument[] arguments;
/*     */   
/*     */   private final ActionArgument[] inputArguments;
/*     */   private final ActionArgument[] outputArguments;
/*     */   private S service;
/*     */   
/*     */   public Action(String name, ActionArgument[] arguments) {
/*  44 */     this.name = name;
/*  45 */     if (arguments != null) {
/*     */       
/*  47 */       List<ActionArgument> inputList = new ArrayList<>();
/*  48 */       List<ActionArgument> outputList = new ArrayList<>();
/*     */       
/*  50 */       for (ActionArgument<S> argument : arguments) {
/*  51 */         argument.setAction(this);
/*  52 */         if (argument.getDirection().equals(ActionArgument.Direction.IN))
/*  53 */           inputList.add(argument); 
/*  54 */         if (argument.getDirection().equals(ActionArgument.Direction.OUT)) {
/*  55 */           outputList.add(argument);
/*     */         }
/*     */       } 
/*  58 */       this.arguments = arguments;
/*  59 */       this.inputArguments = inputList.<ActionArgument>toArray(new ActionArgument[inputList.size()]);
/*  60 */       this.outputArguments = outputList.<ActionArgument>toArray(new ActionArgument[outputList.size()]);
/*     */     } else {
/*  62 */       this.arguments = new ActionArgument[0];
/*  63 */       this.inputArguments = new ActionArgument[0];
/*  64 */       this.outputArguments = new ActionArgument[0];
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getName() {
/*  69 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean hasArguments() {
/*  73 */     return (getArguments() != null && (getArguments()).length > 0);
/*     */   }
/*     */   
/*     */   public ActionArgument[] getArguments() {
/*  77 */     return this.arguments;
/*     */   }
/*     */   
/*     */   public S getService() {
/*  81 */     return this.service;
/*     */   }
/*     */   
/*     */   void setService(S service) {
/*  85 */     if (this.service != null)
/*  86 */       throw new IllegalStateException("Final value has been set already, model is immutable"); 
/*  87 */     this.service = service;
/*     */   }
/*     */   
/*     */   public ActionArgument<S> getFirstInputArgument() {
/*  91 */     if (!hasInputArguments()) throw new IllegalStateException("No input arguments: " + this); 
/*  92 */     return getInputArguments()[0];
/*     */   }
/*     */   
/*     */   public ActionArgument<S> getFirstOutputArgument() {
/*  96 */     if (!hasOutputArguments()) throw new IllegalStateException("No output arguments: " + this); 
/*  97 */     return getOutputArguments()[0];
/*     */   }
/*     */   
/*     */   public ActionArgument<S>[] getInputArguments() {
/* 101 */     return (ActionArgument<S>[])this.inputArguments;
/*     */   }
/*     */   
/*     */   public ActionArgument<S> getInputArgument(String name) {
/* 105 */     for (ActionArgument<S> arg : getInputArguments()) {
/* 106 */       if (arg.isNameOrAlias(name)) return arg; 
/*     */     } 
/* 108 */     return null;
/*     */   }
/*     */   
/*     */   public ActionArgument<S>[] getOutputArguments() {
/* 112 */     return (ActionArgument<S>[])this.outputArguments;
/*     */   }
/*     */   
/*     */   public ActionArgument<S> getOutputArgument(String name) {
/* 116 */     for (ActionArgument<S> arg : getOutputArguments()) {
/* 117 */       if (arg.getName().equals(name)) return arg; 
/*     */     } 
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasInputArguments() {
/* 123 */     return (getInputArguments() != null && (getInputArguments()).length > 0);
/*     */   }
/*     */   
/*     */   public boolean hasOutputArguments() {
/* 127 */     return (getOutputArguments() != null && (getOutputArguments()).length > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     return "(" + getClass().getSimpleName() + ", Arguments: " + (
/* 134 */       (getArguments() != null) ? (String)Integer.valueOf((getArguments()).length) : "NO ARGS") + ") " + 
/* 135 */       getName();
/*     */   }
/*     */   
/*     */   public List<ValidationError> validate() {
/* 139 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/* 141 */     if (getName() == null || getName().length() == 0) {
/* 142 */       errors.add(new ValidationError(
/* 143 */             getClass(), "name", "Action without name of: " + 
/*     */             
/* 145 */             getService()));
/*     */     }
/* 147 */     else if (!ModelUtil.isValidUDAName(getName())) {
/* 148 */       log.warning("UPnP specification violation of: " + getService().getDevice());
/* 149 */       log.warning("Invalid action name: " + this);
/*     */     } 
/*     */     
/* 152 */     for (ActionArgument actionArgument : getArguments()) {
/*     */ 
/*     */       
/* 155 */       if (getService().getStateVariable(actionArgument.getRelatedStateVariableName()) == null) {
/* 156 */         errors.add(new ValidationError(
/* 157 */               getClass(), "arguments", "Action argument references an unknown state variable: " + actionArgument
/*     */               
/* 159 */               .getRelatedStateVariableName()));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 164 */     ActionArgument retValueArgument = null;
/* 165 */     int retValueArgumentIndex = 0;
/* 166 */     int i = 0;
/* 167 */     for (ActionArgument actionArgument : getArguments()) {
/*     */       
/* 169 */       if (actionArgument.isReturnValue()) {
/* 170 */         if (actionArgument.getDirection() == ActionArgument.Direction.IN) {
/* 171 */           log.warning("UPnP specification violation of :" + getService().getDevice());
/* 172 */           log.warning("Input argument can not have <retval/>");
/*     */         } else {
/* 174 */           if (retValueArgument != null) {
/* 175 */             log.warning("UPnP specification violation of: " + getService().getDevice());
/* 176 */             log.warning("Only one argument of action '" + getName() + "' can be <retval/>");
/*     */           } 
/* 178 */           retValueArgument = actionArgument;
/* 179 */           retValueArgumentIndex = i;
/*     */         } 
/*     */       }
/* 182 */       i++;
/*     */     } 
/* 184 */     if (retValueArgument != null) {
/* 185 */       for (int j = 0; j < retValueArgumentIndex; j++) {
/* 186 */         ActionArgument a = getArguments()[j];
/* 187 */         if (a.getDirection() == ActionArgument.Direction.OUT) {
/* 188 */           log.warning("UPnP specification violation of: " + getService().getDevice());
/* 189 */           log.warning("Argument '" + retValueArgument.getName() + "' of action '" + getName() + "' is <retval/> but not the first OUT argument");
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 194 */     for (ActionArgument argument : this.arguments) {
/* 195 */       errors.addAll(argument.validate());
/*     */     }
/*     */     
/* 198 */     return errors;
/*     */   }
/*     */   
/*     */   public Action<S> deepCopy() {
/* 202 */     ActionArgument[] arrayOfActionArgument = new ActionArgument[(getArguments()).length];
/* 203 */     for (int i = 0; i < (getArguments()).length; i++) {
/* 204 */       ActionArgument arg = getArguments()[i];
/* 205 */       arrayOfActionArgument[i] = arg.deepCopy();
/*     */     } 
/*     */     
/* 208 */     return new Action(
/* 209 */         getName(), arrayOfActionArgument);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\Action.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */