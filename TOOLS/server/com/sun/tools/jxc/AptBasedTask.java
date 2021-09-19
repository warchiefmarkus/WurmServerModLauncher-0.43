/*     */ package com.sun.tools.jxc;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessorFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.Javac;
/*     */ import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AptBasedTask
/*     */   extends Javac
/*     */ {
/*     */   static Class class$com$sun$mirror$apt$AnnotationProcessorFactory;
/*     */   static Class array$Ljava$lang$String;
/*     */   
/*     */   public AptBasedTask() {
/*  57 */     setExecutable("apt");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class AptAdapter
/*     */     extends DefaultCompilerAdapter
/*     */   {
/*     */     private final AptBasedTask this$0;
/*     */ 
/*     */     
/*     */     protected AptAdapter(AptBasedTask this$0) {
/*  69 */       this.this$0 = this$0;
/*  70 */       setJavac(this$0);
/*     */     }
/*     */     
/*     */     protected Commandline setupModernJavacCommandlineSwitches(Commandline cmd) {
/*  74 */       super.setupModernJavacCommandlineSwitches(cmd);
/*  75 */       this.this$0.setupCommandlineSwitches(cmd);
/*  76 */       return cmd;
/*     */     } }
/*     */   
/*     */   private final class InternalAptAdapter extends AptAdapter {
/*     */     private final AptBasedTask this$0;
/*     */     
/*     */     private InternalAptAdapter(AptBasedTask this$0) {
/*  83 */       AptBasedTask.this = AptBasedTask.this;
/*     */     } public boolean execute() throws BuildException {
/*  85 */       Commandline cmd = setupModernJavacCommand();
/*     */       try {
/*     */         Method process;
/*  88 */         Class apt = Class.forName("com.sun.tools.apt.Main");
/*     */         
/*     */         try {
/*  91 */           process = apt.getMethod("process", new Class[] { (AptBasedTask.class$com$sun$mirror$apt$AnnotationProcessorFactory == null) ? (AptBasedTask.class$com$sun$mirror$apt$AnnotationProcessorFactory = AptBasedTask.class$("com.sun.mirror.apt.AnnotationProcessorFactory")) : AptBasedTask.class$com$sun$mirror$apt$AnnotationProcessorFactory, (AptBasedTask.array$Ljava$lang$String == null) ? (AptBasedTask.array$Ljava$lang$String = AptBasedTask.class$("[Ljava.lang.String;")) : AptBasedTask.array$Ljava$lang$String });
/*     */         }
/*  93 */         catch (NoSuchMethodException e) {
/*  94 */           throw new BuildException("JDK 1.5.0_01 or later is necessary", e, this.location);
/*     */         } 
/*     */         
/*  97 */         int result = ((Integer)process.invoke(null, new Object[] { this.this$0.createFactory(), cmd.getArguments() })).intValue();
/*     */ 
/*     */         
/* 100 */         return (result == 0);
/* 101 */       } catch (BuildException e) {
/* 102 */         throw e;
/* 103 */       } catch (Exception ex) {
/* 104 */         throw new BuildException("Error starting apt", ex, this.location);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class class$(String x0) {
/*     */     try {
/*     */       return Class.forName(x0);
/*     */     } catch (ClassNotFoundException x1) {
/*     */       throw new NoClassDefFoundError(x1.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void compile() {
/* 125 */     if (this.compileList.length == 0)
/*     */       return; 
/* 127 */     log(getCompilationMessage() + this.compileList.length + " source file" + ((this.compileList.length == 1) ? "" : "s"));
/*     */ 
/*     */     
/* 130 */     if (this.listFiles) {
/* 131 */       for (int i = 0; i < this.compileList.length; i++) {
/* 132 */         String filename = this.compileList[i].getAbsolutePath();
/* 133 */         log(filename);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     AptAdapter apt = new InternalAptAdapter();
/*     */ 
/*     */     
/* 144 */     if (!apt.execute()) {
/* 145 */       if (this.failOnError) {
/* 146 */         throw new BuildException(getFailedMessage(), getLocation());
/*     */       }
/* 148 */       log(getFailedMessage(), 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void setupCommandlineSwitches(Commandline paramCommandline);
/*     */   
/*     */   protected abstract AnnotationProcessorFactory createFactory();
/*     */   
/*     */   protected abstract String getCompilationMessage();
/*     */   
/*     */   protected abstract String getFailedMessage();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\AptBasedTask.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */