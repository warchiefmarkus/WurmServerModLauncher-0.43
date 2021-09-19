/*     */ package org.fourthline.cling.model;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.state.StateVariableAccessor;
/*     */ import org.fourthline.cling.model.state.StateVariableValue;
/*     */ import org.seamless.util.Exceptions;
/*     */ import org.seamless.util.Reflections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultServiceManager<T>
/*     */   implements ServiceManager<T>
/*     */ {
/*  53 */   private static Logger log = Logger.getLogger(DefaultServiceManager.class.getName());
/*     */   
/*     */   protected final LocalService<T> service;
/*     */   protected final Class<T> serviceClass;
/*  57 */   protected final ReentrantLock lock = new ReentrantLock(true);
/*     */   
/*     */   protected T serviceImpl;
/*     */   
/*     */   protected PropertyChangeSupport propertyChangeSupport;
/*     */   
/*     */   protected DefaultServiceManager(LocalService<T> service) {
/*  64 */     this(service, null);
/*     */   }
/*     */   
/*     */   public DefaultServiceManager(LocalService<T> service, Class<T> serviceClass) {
/*  68 */     this.service = service;
/*  69 */     this.serviceClass = serviceClass;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void lock() {
/*     */     try {
/*  76 */       if (this.lock.tryLock(getLockTimeoutMillis(), TimeUnit.MILLISECONDS)) {
/*  77 */         if (log.isLoggable(Level.FINEST))
/*  78 */           log.finest("Acquired lock"); 
/*     */       } else {
/*  80 */         throw new RuntimeException("Failed to acquire lock in milliseconds: " + getLockTimeoutMillis());
/*     */       } 
/*  82 */     } catch (InterruptedException e) {
/*  83 */       throw new RuntimeException("Failed to acquire lock:" + e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void unlock() {
/*  88 */     if (log.isLoggable(Level.FINEST))
/*  89 */       log.finest("Releasing lock"); 
/*  90 */     this.lock.unlock();
/*     */   }
/*     */   
/*     */   protected int getLockTimeoutMillis() {
/*  94 */     return 500;
/*     */   }
/*     */   
/*     */   public LocalService<T> getService() {
/*  98 */     return this.service;
/*     */   }
/*     */   
/*     */   public T getImplementation() {
/* 102 */     lock();
/*     */     try {
/* 104 */       if (this.serviceImpl == null) {
/* 105 */         init();
/*     */       }
/* 107 */       return this.serviceImpl;
/*     */     } finally {
/* 109 */       unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public PropertyChangeSupport getPropertyChangeSupport() {
/* 114 */     lock();
/*     */     try {
/* 116 */       if (this.propertyChangeSupport == null) {
/* 117 */         init();
/*     */       }
/* 119 */       return this.propertyChangeSupport;
/*     */     } finally {
/* 121 */       unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void execute(Command<T> cmd) throws Exception {
/* 126 */     lock();
/*     */     try {
/* 128 */       cmd.execute(this);
/*     */     } finally {
/* 130 */       unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<StateVariableValue> getCurrentState() throws Exception {
/* 136 */     lock();
/*     */     try {
/* 138 */       Collection<StateVariableValue> values = readInitialEventedStateVariableValues();
/* 139 */       if (values != null) {
/* 140 */         log.fine("Obtained initial state variable values for event, skipping individual state variable accessors");
/* 141 */         return values;
/*     */       } 
/* 143 */       values = new ArrayList<>();
/* 144 */       for (StateVariable stateVariable : getService().getStateVariables()) {
/* 145 */         if (stateVariable.getEventDetails().isSendEvents()) {
/* 146 */           StateVariableAccessor accessor = getService().getAccessor(stateVariable);
/* 147 */           if (accessor == null)
/* 148 */             throw new IllegalStateException("No accessor for evented state variable"); 
/* 149 */           values.add(accessor.read(stateVariable, getImplementation()));
/*     */         } 
/*     */       } 
/* 152 */       return values;
/*     */     } finally {
/* 154 */       unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Collection<StateVariableValue> getCurrentState(String[] variableNames) throws Exception {
/* 159 */     lock();
/*     */     try {
/* 161 */       Collection<StateVariableValue> values = new ArrayList<>();
/* 162 */       for (String variableName : variableNames) {
/* 163 */         variableName = variableName.trim();
/*     */         
/* 165 */         StateVariable stateVariable = getService().getStateVariable(variableName);
/* 166 */         if (stateVariable == null || !stateVariable.getEventDetails().isSendEvents()) {
/* 167 */           log.fine("Ignoring unknown or non-evented state variable: " + variableName);
/*     */         }
/*     */         else {
/*     */           
/* 171 */           StateVariableAccessor accessor = getService().getAccessor(stateVariable);
/* 172 */           if (accessor == null)
/* 173 */           { log.warning("Ignoring evented state variable without accessor: " + variableName); }
/*     */           else
/*     */           
/* 176 */           { values.add(accessor.read(stateVariable, getImplementation())); } 
/*     */         } 
/* 178 */       }  return values;
/*     */     } finally {
/* 180 */       unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void init() {
/* 185 */     log.fine("No service implementation instance available, initializing...");
/*     */     
/*     */     try {
/* 188 */       this.serviceImpl = createServiceInstance();
/*     */ 
/*     */       
/* 191 */       this.propertyChangeSupport = createPropertyChangeSupport(this.serviceImpl);
/* 192 */       this.propertyChangeSupport.addPropertyChangeListener(createPropertyChangeListener(this.serviceImpl));
/*     */     }
/* 194 */     catch (Exception ex) {
/* 195 */       throw new RuntimeException("Could not initialize implementation: " + ex, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected T createServiceInstance() throws Exception {
/* 200 */     if (this.serviceClass == null) {
/* 201 */       throw new IllegalStateException("Subclass has to provide service class or override createServiceInstance()");
/*     */     }
/*     */     
/*     */     try {
/* 205 */       return this.serviceClass.getConstructor(new Class[] { LocalService.class }).newInstance(new Object[] { getService() });
/* 206 */     } catch (NoSuchMethodException ex) {
/* 207 */       log.fine("Creating new service implementation instance with no-arg constructor: " + this.serviceClass.getName());
/* 208 */       return this.serviceClass.newInstance();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected PropertyChangeSupport createPropertyChangeSupport(T serviceImpl) throws Exception {
/*     */     Method m;
/* 214 */     if ((m = Reflections.getGetterMethod(serviceImpl.getClass(), "propertyChangeSupport")) != null && PropertyChangeSupport.class
/* 215 */       .isAssignableFrom(m.getReturnType())) {
/* 216 */       log.fine("Service implementation instance offers PropertyChangeSupport, using that: " + serviceImpl.getClass().getName());
/* 217 */       return (PropertyChangeSupport)m.invoke(serviceImpl, new Object[0]);
/*     */     } 
/* 219 */     log.fine("Creating new PropertyChangeSupport for service implementation: " + serviceImpl.getClass().getName());
/* 220 */     return new PropertyChangeSupport(serviceImpl);
/*     */   }
/*     */   
/*     */   protected PropertyChangeListener createPropertyChangeListener(T serviceImpl) throws Exception {
/* 224 */     return new DefaultPropertyChangeListener();
/*     */   }
/*     */   
/*     */   protected Collection<StateVariableValue> readInitialEventedStateVariableValues() throws Exception {
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     return "(" + getClass().getSimpleName() + ") Implementation: " + this.serviceImpl;
/*     */   }
/*     */   
/*     */   protected class DefaultPropertyChangeListener
/*     */     implements PropertyChangeListener {
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 239 */       DefaultServiceManager.log.finer("Property change event on local service: " + e.getPropertyName());
/*     */ 
/*     */       
/* 242 */       if (e.getPropertyName().equals("_EventedStateVariables"))
/*     */         return; 
/* 244 */       String[] variableNames = ModelUtil.fromCommaSeparatedList(e.getPropertyName());
/* 245 */       DefaultServiceManager.log.fine("Changed variable names: " + Arrays.toString((Object[])variableNames));
/*     */       
/*     */       try {
/* 248 */         Collection<StateVariableValue> currentValues = DefaultServiceManager.this.getCurrentState(variableNames);
/*     */         
/* 250 */         if (!currentValues.isEmpty()) {
/* 251 */           DefaultServiceManager.this.getPropertyChangeSupport().firePropertyChange("_EventedStateVariables", (Object)null, currentValues);
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 258 */       catch (Exception ex) {
/*     */         
/* 260 */         DefaultServiceManager.log.log(Level.SEVERE, "Error reading state of service after state variable update event: " + 
/*     */             
/* 262 */             Exceptions.unwrap(ex), ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\DefaultServiceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */