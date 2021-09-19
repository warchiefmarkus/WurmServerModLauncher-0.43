/*     */ package com.sun.tools.jxc;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessorFactory;
/*     */ import com.sun.tools.jxc.apt.Options;
/*     */ import com.sun.tools.jxc.apt.SchemaGenerator;
/*     */ import com.sun.tools.xjc.BadCommandLineException;
/*     */ import com.sun.tools.xjc.api.util.APTClassLoader;
/*     */ import com.sun.tools.xjc.api.util.ToolsJarNotFoundException;
/*     */ import com.sun.xml.bind.util.Which;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SchemaGenerator
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  66 */     System.exit(run(args));
/*     */   }
/*     */   
/*     */   public static int run(String[] args) throws Exception {
/*     */     try {
/*  71 */       ClassLoader cl = SchemaGenerator.class.getClassLoader();
/*  72 */       if (cl == null) cl = ClassLoader.getSystemClassLoader(); 
/*  73 */       APTClassLoader aPTClassLoader = new APTClassLoader(cl, packagePrefixes);
/*  74 */       return run(args, (ClassLoader)aPTClassLoader);
/*  75 */     } catch (ToolsJarNotFoundException e) {
/*  76 */       System.err.println(e.getMessage());
/*  77 */       return -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private static final String[] packagePrefixes = new String[] { "com.sun.tools.jxc.", "com.sun.tools.xjc.", "com.sun.istack.tools.", "com.sun.tools.apt.", "com.sun.tools.javac.", "com.sun.tools.javadoc.", "com.sun.mirror." };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int run(String[] args, ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
/* 107 */     Options options = new Options();
/* 108 */     if (args.length == 0) {
/* 109 */       usage();
/* 110 */       return -1;
/*     */     } 
/* 112 */     for (String arg : args) {
/* 113 */       if (arg.equals("-help")) {
/* 114 */         usage();
/* 115 */         return -1;
/*     */       } 
/*     */       
/* 118 */       if (arg.equals("-version")) {
/* 119 */         System.out.println(Messages.VERSION.format(new Object[0]));
/* 120 */         return -1;
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 125 */       options.parseArguments(args);
/* 126 */     } catch (BadCommandLineException e) {
/*     */ 
/*     */       
/* 129 */       System.out.println(e.getMessage());
/* 130 */       System.out.println();
/* 131 */       usage();
/* 132 */       return -1;
/*     */     } 
/*     */     
/* 135 */     Class<?> schemagenRunner = classLoader.loadClass(Runner.class.getName());
/* 136 */     Method mainMethod = schemagenRunner.getDeclaredMethod("main", new Class[] { String[].class, File.class });
/*     */     
/* 138 */     List<String> aptargs = new ArrayList<String>();
/*     */     
/* 140 */     if (hasClass(options.arguments)) {
/* 141 */       aptargs.add("-XclassesAsDecls");
/*     */     }
/*     */     
/* 144 */     File jaxbApi = findJaxbApiJar();
/* 145 */     if (jaxbApi != null) {
/* 146 */       if (options.classpath != null) {
/* 147 */         options.classpath += File.pathSeparatorChar + jaxbApi;
/*     */       } else {
/* 149 */         options.classpath = jaxbApi.getPath();
/*     */       } 
/*     */     }
/*     */     
/* 153 */     aptargs.add("-cp");
/* 154 */     aptargs.add(options.classpath);
/*     */     
/* 156 */     if (options.targetDir != null) {
/* 157 */       aptargs.add("-d");
/* 158 */       aptargs.add(options.targetDir.getPath());
/*     */     } 
/*     */     
/* 161 */     aptargs.addAll(options.arguments);
/*     */     
/* 163 */     String[] argsarray = aptargs.<String>toArray(new String[aptargs.size()]);
/* 164 */     return ((Integer)mainMethod.invoke(null, new Object[] { argsarray, options.episodeFile })).intValue();
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
/*     */   private static File findJaxbApiJar() {
/* 179 */     String url = Which.which(JAXBContext.class);
/* 180 */     if (url == null) return null;
/*     */     
/* 182 */     if (!url.startsWith("jar:") || url.lastIndexOf('!') == -1)
/*     */     {
/* 184 */       return null;
/*     */     }
/* 186 */     String jarFileUrl = url.substring(4, url.lastIndexOf('!'));
/* 187 */     if (!jarFileUrl.startsWith("file:")) {
/* 188 */       return null;
/*     */     }
/*     */     try {
/* 191 */       File f = new File((new URL(jarFileUrl)).getFile());
/* 192 */       if (f.exists() && f.getName().endsWith(".jar")) {
/* 193 */         return f;
/*     */       }
/* 195 */       return null;
/* 196 */     } catch (MalformedURLException e) {
/* 197 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasClass(List<String> args) {
/* 206 */     for (String arg : args) {
/* 207 */       if (!arg.endsWith(".java"))
/* 208 */         return true; 
/*     */     } 
/* 210 */     return false;
/*     */   }
/*     */   
/*     */   private static void usage() {
/* 214 */     System.out.println(Messages.USAGE.format(new Object[0]));
/*     */   }
/*     */   
/*     */   public static final class Runner {
/*     */     public static int main(String[] args, File episode) throws Exception {
/* 219 */       ClassLoader cl = Runner.class.getClassLoader();
/* 220 */       Class<?> apt = cl.loadClass("com.sun.tools.apt.Main");
/* 221 */       Method processMethod = apt.getMethod("process", new Class[] { AnnotationProcessorFactory.class, String[].class });
/*     */       
/* 223 */       SchemaGenerator r = new SchemaGenerator();
/* 224 */       if (episode != null)
/* 225 */         r.setEpisodeFile(episode); 
/* 226 */       return ((Integer)processMethod.invoke(null, new Object[] { r, args })).intValue();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\SchemaGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */