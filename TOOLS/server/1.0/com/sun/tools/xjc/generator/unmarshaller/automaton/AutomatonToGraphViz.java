/*     */ package 1.0.com.sun.tools.xjc.generator.unmarshaller.automaton;
/*     */ 
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Alphabet;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Automaton;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.State;
/*     */ import com.sun.tools.xjc.generator.unmarshaller.automaton.Transition;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutomatonToGraphViz
/*     */ {
/*  28 */   private static final PrintStream debug = null;
/*     */ 
/*     */   
/*     */   private static String getStateName(Automaton a, State s) {
/*  32 */     return "\"s" + a.getStateNumber(s) + (s.isListState ? "*" : "") + '"';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getColor(Alphabet a) {
/*  38 */     if (a instanceof Alphabet.EnterElement) return "0"; 
/*  39 */     if (a instanceof Alphabet.EnterAttribute) return "0.125"; 
/*  40 */     if (a instanceof Alphabet.LeaveAttribute) return "0.25"; 
/*  41 */     if (a instanceof Alphabet.LeaveElement) return "0.375"; 
/*  42 */     if (a instanceof Alphabet.Child) return "0.5"; 
/*  43 */     if (a instanceof Alphabet.SuperClass) return "0.625"; 
/*  44 */     if (a instanceof Alphabet.External) return "0.625"; 
/*  45 */     if (a instanceof Alphabet.Dispatch) return "0.625"; 
/*  46 */     if (a instanceof Alphabet.EverythingElse) return "0.625"; 
/*  47 */     if (a instanceof Alphabet.Text) return "0.75"; 
/*  48 */     if (a instanceof Alphabet.Interleave) return "0.875"; 
/*  49 */     throw new InternalError(a.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void convert(Automaton a, File target) throws IOException, InterruptedException {
/*  58 */     System.err.println("generating a graph to " + target.getPath());
/*     */ 
/*     */     
/*  61 */     Process proc = Runtime.getRuntime().exec(new String[] { "dot", "-Tgif", "-o", target.getPath() });
/*     */     
/*  63 */     PrintWriter out = new PrintWriter(new BufferedOutputStream(proc.getOutputStream()));
/*     */ 
/*     */     
/*  66 */     out.println("digraph G {");
/*  67 */     out.println("node [shape=\"circle\"];");
/*     */     
/*  69 */     Iterator itr = a.states();
/*  70 */     while (itr.hasNext()) {
/*  71 */       State s = itr.next();
/*  72 */       if (s.isFinalState()) {
/*  73 */         out.println(getStateName(a, s) + " [shape=\"doublecircle\"];");
/*     */       }
/*  75 */       if (s.getDelegatedState() != null) {
/*  76 */         out.println(MessageFormat.format("{0} -> {1} [style=dotted];", new Object[] { getStateName(a, s), getStateName(a, s.getDelegatedState()) }));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       Iterator jtr = s.transitions();
/*  83 */       while (jtr.hasNext()) {
/*  84 */         Transition t = jtr.next();
/*     */         
/*  86 */         String str = MessageFormat.format("{0} -> {1} [ label=\"{2}\",color=\"{3} 1 .5\",fontcolor=\"{3} 1 .3\" ];", new Object[] { getStateName(a, s), getStateName(a, t.to), getAlphabetName(a, t.alphabet), getColor(t.alphabet) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  93 */         out.println(str);
/*  94 */         if (debug != null) debug.println(str);
/*     */       
/*     */       } 
/*     */     } 
/*  98 */     out.println("}");
/*  99 */     out.flush();
/* 100 */     out.close();
/*     */     
/* 102 */     BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
/*     */     while (true) {
/* 104 */       String s = in.readLine();
/* 105 */       if (s == null)
/* 106 */         break;  System.out.println(s);
/*     */     } 
/* 108 */     in.close();
/*     */     
/* 110 */     proc.waitFor();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getAlphabetName(Automaton a, Alphabet alphabet) {
/* 116 */     String s = alphabet.toString();
/* 117 */     if (alphabet instanceof Alphabet.Interleave) {
/* 118 */       s = s + " ->";
/* 119 */       Alphabet.Interleave ia = (Alphabet.Interleave)alphabet;
/* 120 */       for (int i = 0; i < ia.branches.length; i++)
/* 121 */         s = s + " " + a.getStateNumber((ia.branches[i]).initialState); 
/*     */     } 
/* 123 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\generato\\unmarshaller\automaton\AutomatonToGraphViz.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */