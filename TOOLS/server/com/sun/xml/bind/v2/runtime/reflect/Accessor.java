/*     */ package com.sun.xml.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.xml.bind.Util;
/*     */ import com.sun.xml.bind.api.AccessorException;
/*     */ import com.sun.xml.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.bind.v2.runtime.reflect.opt.OptimizedAccessorFactory;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Accessor<BeanT, ValueT>
/*     */   implements Receiver
/*     */ {
/*     */   public final Class<ValueT> valueType;
/*     */   
/*     */   public Class<ValueT> getValueType() {
/*  89 */     return this.valueType;
/*     */   }
/*     */   
/*     */   protected Accessor(Class<ValueT> valueType) {
/*  93 */     this.valueType = valueType;
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
/*     */   public Accessor<BeanT, ValueT> optimize(@Nullable JAXBContextImpl context) {
/* 106 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getUnadapted(BeanT bean) throws AccessorException {
/* 147 */     return get(bean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAdapted() {
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnadapted(BeanT bean, Object value) throws AccessorException {
/* 166 */     set(bean, (ValueT)value);
/*     */   }
/*     */   
/*     */   public void receive(UnmarshallingContext.State state, Object o) throws SAXException {
/*     */     try {
/* 171 */       set((BeanT)state.target, (ValueT)o);
/* 172 */     } catch (AccessorException e) {
/* 173 */       Loader.handleGenericException((Exception)e, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Accessor<BeanT, T> adapt(Class<T> targetType, Class<? extends XmlAdapter<T, ValueT>> adapter) {
/* 182 */     return new AdaptedAccessor<BeanT, ValueT, T>(targetType, this, adapter);
/*     */   }
/*     */   
/*     */   public final <T> Accessor<BeanT, T> adapt(Adapter<Type, Class<?>> adapter) {
/* 186 */     return new AdaptedAccessor<BeanT, ValueT, T>(Navigator.REFLECTION.erasure((Type)adapter.defaultType), this, (Class<? extends XmlAdapter<T, ValueT>>)adapter.adapterType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean accessWarned = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FieldReflection<BeanT, ValueT>
/*     */     extends Accessor<BeanT, ValueT>
/*     */   {
/*     */     public final Field f;
/*     */ 
/*     */ 
/*     */     
/* 205 */     private static final Logger logger = Util.getClassLogger();
/*     */     
/*     */     public FieldReflection(Field f) {
/* 208 */       super((Class)f.getType());
/* 209 */       this.f = f;
/*     */       
/* 211 */       int mod = f.getModifiers();
/* 212 */       if (!Modifier.isPublic(mod) || Modifier.isFinal(mod) || !Modifier.isPublic(f.getDeclaringClass().getModifiers())) {
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 217 */           f.setAccessible(true);
/* 218 */         } catch (SecurityException e) {
/* 219 */           if (!Accessor.accessWarned)
/*     */           {
/* 221 */             logger.log(Level.WARNING, Messages.UNABLE_TO_ACCESS_NON_PUBLIC_FIELD.format(new Object[] { f.getDeclaringClass().getName(), f.getName() }, ), e);
/*     */           }
/*     */ 
/*     */           
/* 225 */           Accessor.accessWarned = true;
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public ValueT get(BeanT bean) {
/*     */       try {
/* 232 */         return (ValueT)this.f.get(bean);
/* 233 */       } catch (IllegalAccessException e) {
/* 234 */         throw new IllegalAccessError(e.getMessage());
/*     */       } 
/*     */     }
/*     */     
/*     */     public void set(BeanT bean, ValueT value) {
/*     */       try {
/* 240 */         if (value == null)
/* 241 */           value = (ValueT)Accessor.uninitializedValues.get(this.valueType); 
/* 242 */         this.f.set(bean, value);
/* 243 */       } catch (IllegalAccessException e) {
/* 244 */         throw new IllegalAccessError(e.getMessage());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
/* 250 */       if (context != null && context.fastBoot)
/*     */       {
/* 252 */         return this; } 
/* 253 */       Accessor<BeanT, ValueT> acc = OptimizedAccessorFactory.get(this.f);
/* 254 */       if (acc != null) {
/* 255 */         return acc;
/*     */       }
/* 257 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ReadOnlyFieldReflection<BeanT, ValueT>
/*     */     extends FieldReflection<BeanT, ValueT>
/*     */   {
/*     */     public ReadOnlyFieldReflection(Field f) {
/* 266 */       super(f);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void set(BeanT bean, ValueT value) {}
/*     */ 
/*     */     
/*     */     public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
/* 275 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class GetterSetterReflection<BeanT, ValueT>
/*     */     extends Accessor<BeanT, ValueT>
/*     */   {
/*     */     public final Method getter;
/*     */     
/*     */     public final Method setter;
/*     */     
/* 287 */     private static final Logger logger = Util.getClassLogger();
/*     */     
/*     */     public GetterSetterReflection(Method getter, Method setter) {
/* 290 */       super((getter != null) ? (Class)getter.getReturnType() : (Class)setter.getParameterTypes()[0]);
/*     */ 
/*     */ 
/*     */       
/* 294 */       this.getter = getter;
/* 295 */       this.setter = setter;
/*     */       
/* 297 */       if (getter != null)
/* 298 */         makeAccessible(getter); 
/* 299 */       if (setter != null)
/* 300 */         makeAccessible(setter); 
/*     */     }
/*     */     
/*     */     private void makeAccessible(Method m) {
/* 304 */       if (!Modifier.isPublic(m.getModifiers()) || !Modifier.isPublic(m.getDeclaringClass().getModifiers())) {
/*     */         try {
/* 306 */           m.setAccessible(true);
/* 307 */         } catch (SecurityException e) {
/* 308 */           if (!Accessor.accessWarned)
/*     */           {
/* 310 */             logger.log(Level.WARNING, Messages.UNABLE_TO_ACCESS_NON_PUBLIC_FIELD.format(new Object[] { m.getDeclaringClass().getName(), m.getName() }, ), e);
/*     */           }
/*     */ 
/*     */           
/* 314 */           Accessor.accessWarned = true;
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public ValueT get(BeanT bean) throws AccessorException {
/*     */       try {
/* 321 */         return (ValueT)this.getter.invoke(bean, new Object[0]);
/* 322 */       } catch (IllegalAccessException e) {
/* 323 */         throw new IllegalAccessError(e.getMessage());
/* 324 */       } catch (InvocationTargetException e) {
/* 325 */         throw handleInvocationTargetException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void set(BeanT bean, ValueT value) throws AccessorException {
/*     */       try {
/* 331 */         if (value == null)
/* 332 */           value = (ValueT)Accessor.uninitializedValues.get(this.valueType); 
/* 333 */         this.setter.invoke(bean, new Object[] { value });
/* 334 */       } catch (IllegalAccessException e) {
/* 335 */         throw new IllegalAccessError(e.getMessage());
/* 336 */       } catch (InvocationTargetException e) {
/* 337 */         throw handleInvocationTargetException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private AccessorException handleInvocationTargetException(InvocationTargetException e) {
/* 343 */       Throwable t = e.getTargetException();
/* 344 */       if (t instanceof RuntimeException)
/* 345 */         throw (RuntimeException)t; 
/* 346 */       if (t instanceof Error) {
/* 347 */         throw (Error)t;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 354 */       return new AccessorException(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public Accessor<BeanT, ValueT> optimize(JAXBContextImpl context) {
/* 359 */       if (this.getter == null || this.setter == null)
/*     */       {
/* 361 */         return this; } 
/* 362 */       if (context != null && context.fastBoot)
/*     */       {
/* 364 */         return this;
/*     */       }
/* 366 */       Accessor<BeanT, ValueT> acc = OptimizedAccessorFactory.get(this.getter, this.setter);
/* 367 */       if (acc != null) {
/* 368 */         return acc;
/*     */       }
/* 370 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class GetterOnlyReflection<BeanT, ValueT>
/*     */     extends GetterSetterReflection<BeanT, ValueT>
/*     */   {
/*     */     public GetterOnlyReflection(Method getter) {
/* 382 */       super(getter, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(BeanT bean, ValueT value) throws AccessorException {
/* 387 */       throw new AccessorException(Messages.NO_SETTER.format(new Object[] { this.getter.toString() }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SetterOnlyReflection<BeanT, ValueT>
/*     */     extends GetterSetterReflection<BeanT, ValueT>
/*     */   {
/*     */     public SetterOnlyReflection(Method setter) {
/* 399 */       super(null, setter);
/*     */     }
/*     */ 
/*     */     
/*     */     public ValueT get(BeanT bean) throws AccessorException {
/* 404 */       throw new AccessorException(Messages.NO_GETTER.format(new Object[] { this.setter.toString() }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A, B> Accessor<A, B> getErrorInstance() {
/* 413 */     return ERROR;
/*     */   }
/*     */   
/* 416 */   private static final Accessor ERROR = new Accessor<Object, Object>(Object.class) {
/*     */       public Object get(Object o) {
/* 418 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void set(Object o, Object o1) {}
/*     */     };
/*     */ 
/*     */ 
/*     */   
/* 428 */   public static final Accessor<JAXBElement, Object> JAXB_ELEMENT_VALUE = new Accessor<JAXBElement, Object>(Object.class) {
/*     */       public Object get(JAXBElement jaxbElement) {
/* 430 */         return jaxbElement.getValue();
/*     */       }
/*     */       
/*     */       public void set(JAXBElement jaxbElement, Object o) {
/* 434 */         jaxbElement.setValue(o);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 441 */   private static final Map<Class, Object> uninitializedValues = (Map)new HashMap<Class<?>, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 454 */     uninitializedValues.put(byte.class, Byte.valueOf((byte)0));
/* 455 */     uninitializedValues.put(boolean.class, Boolean.valueOf(false));
/* 456 */     uninitializedValues.put(char.class, Character.valueOf(false));
/* 457 */     uninitializedValues.put(float.class, Float.valueOf(0.0F));
/* 458 */     uninitializedValues.put(double.class, Double.valueOf(0.0D));
/* 459 */     uninitializedValues.put(int.class, Integer.valueOf(0));
/* 460 */     uninitializedValues.put(long.class, Long.valueOf(0L));
/* 461 */     uninitializedValues.put(short.class, Short.valueOf((short)0));
/*     */   }
/*     */   
/*     */   public abstract ValueT get(BeanT paramBeanT) throws AccessorException;
/*     */   
/*     */   public abstract void set(BeanT paramBeanT, ValueT paramValueT) throws AccessorException;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\reflect\Accessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */