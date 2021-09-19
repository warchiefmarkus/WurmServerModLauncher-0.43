/*     */ package com.wurmonline.server.meshgen;
/*     */ 
/*     */ import com.wurmonline.mesh.MeshIO;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.SwingWorker;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.swing.text.DefaultCaret;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MeshGenGui
/*     */   extends JFrame
/*     */   implements ActionListener
/*     */ {
/*     */   private static final long serialVersionUID = -1462641916710560981L;
/*  68 */   private static final Logger logger = Logger.getLogger(MeshGenGui.class.getName());
/*     */   
/*     */   JLabel imageLabel;
/*     */   
/*     */   JButton generateGroundButton;
/*     */   
/*     */   JButton normaliseButton;
/*     */   
/*     */   JButton flowButton;
/*     */   
/*     */   JButton texturizeButton;
/*     */   
/*     */   JButton saveButton;
/*     */   
/*     */   JButton saveImageButton;
/*     */   
/*     */   JButton loadButton;
/*     */   
/*     */   JButton addIslandsButton;
/*     */   JToggleButton layerToggle;
/*  88 */   long seed = 0L;
/*     */   
/*     */   MeshGen meshGen;
/*     */   
/*     */   JPanel panel;
/*     */   MeshIO topLayerMeshIO;
/*     */   MeshIO rockLayerMeshIO;
/*     */   boolean loaded = false;
/*  96 */   final String baseDir = "worldmachine" + File.separator + "NewEle2015";
/*  97 */   final String baseFile = File.separator + "output.r32";
/*     */   
/*     */   private JProgressBar progressBar;
/*     */   
/*     */   private JTextArea taskOutput;
/*     */   private Task task;
/*     */   private JFrame frame;
/*     */   
/*     */   public MeshGenGui() {
/* 106 */     super("Wurm MeshGen GUI");
/*     */     
/* 108 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 110 */       logger.fine("Starting Wurm MeshGen GUI");
/*     */     }
/* 112 */     this.panel = new JPanel();
/* 113 */     this.panel.setLayout(new BorderLayout());
/*     */     
/* 115 */     this.imageLabel = new JLabel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     this.progressBar = new JProgressBar(0, 100);
/* 126 */     this.progressBar.setValue(0);
/* 127 */     this.progressBar.setStringPainted(true);
/*     */     
/* 129 */     this.flowButton = new JButton("Load base map");
/* 130 */     this.flowButton.addActionListener(this);
/* 131 */     this.flowButton.setToolTipText("Load the output.r32 base map file");
/*     */     
/* 133 */     this.texturizeButton = new JButton("Texturize");
/* 134 */     this.texturizeButton.addActionListener(this);
/*     */     
/* 136 */     this.saveButton = new JButton("Save");
/* 137 */     this.saveButton.addActionListener(this);
/* 138 */     this.saveButton.setToolTipText("Save the top_layer.map and rock_layer.map");
/*     */     
/* 140 */     this.loadButton = new JButton("Load");
/* 141 */     this.loadButton.addActionListener(this);
/* 142 */     this.loadButton.setToolTipText("Load the top_layer.map and rock_layer.map");
/*     */     
/* 144 */     this.saveImageButton = new JButton("Save Image");
/* 145 */     this.saveImageButton.addActionListener(this);
/* 146 */     this.saveImageButton.setToolTipText("Save the coloured image of top_layer.map to map.png");
/*     */     
/* 148 */     this.layerToggle = new JToggleButton("Layer", false);
/* 149 */     this.layerToggle.addActionListener(this);
/* 150 */     this.layerToggle.setToolTipText("Selected shows the rock layer, unselected shows the surface layer");
/*     */     
/* 152 */     this.addIslandsButton = new JButton("Add Islands");
/* 153 */     this.addIslandsButton.addActionListener(this);
/* 154 */     this.addIslandsButton.setToolTipText("Add some islands to the top_layer.map and rock_layer.map");
/*     */     
/* 156 */     JPanel buttonPanel = new JPanel();
/* 157 */     buttonPanel.add(this.progressBar);
/* 158 */     buttonPanel.add(this.layerToggle);
/*     */ 
/*     */     
/* 161 */     buttonPanel.add(this.flowButton);
/* 162 */     buttonPanel.add(this.texturizeButton);
/* 163 */     buttonPanel.add(this.addIslandsButton);
/* 164 */     buttonPanel.add(this.saveButton);
/* 165 */     buttonPanel.add(this.loadButton);
/* 166 */     buttonPanel.add(this.saveImageButton);
/*     */     
/* 168 */     this.panel.add(new JScrollPane(this.imageLabel), "Center");
/* 169 */     this.panel.add(buttonPanel, "South");
/*     */     
/* 171 */     setContentPane(this.panel);
/* 172 */     setSize(1200, 800);
/* 173 */     setDefaultCloseOperation(3);
/* 174 */     enableButtons(false);
/*     */ 
/*     */     
/* 177 */     this.frame = new JFrame("Please wait...");
/* 178 */     this.frame.setDefaultCloseOperation(3);
/* 179 */     this.frame.setLayout(new BorderLayout());
/*     */     
/* 181 */     this.taskOutput = new JTextArea(20, 30);
/* 182 */     this.taskOutput.setMargin(new Insets(5, 5, 5, 5));
/* 183 */     this.taskOutput.setEditable(false);
/*     */     
/* 185 */     DefaultCaret caret = (DefaultCaret)this.taskOutput.getCaret();
/* 186 */     caret.setUpdatePolicy(2);
/*     */     
/* 188 */     JScrollPane scroll = new JScrollPane(this.taskOutput, 22, 31);
/*     */ 
/*     */     
/* 191 */     JPanel mpanel = new JPanel();
/* 192 */     mpanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
/* 193 */     mpanel.add(scroll, "Center");
/* 194 */     this.frame.setContentPane(mpanel);
/* 195 */     this.frame.pack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*     */     try {
/* 203 */       Thread.sleep(500L);
/*     */     }
/* 205 */     catch (InterruptedException e2) {
/*     */       
/* 207 */       e2.printStackTrace();
/*     */     } 
/*     */     
/*     */     try {
/* 211 */       if (e.getSource() == this.flowButton) {
/*     */         
/* 213 */         if (!this.loaded) {
/*     */           
/* 215 */           this.loaded = true;
/*     */           
/*     */           try {
/* 218 */             JFileChooser chooser = new JFileChooser();
/* 219 */             chooser.setFileFilter(new FileFilter()
/*     */                 {
/*     */                   
/*     */                   public boolean accept(File f)
/*     */                   {
/* 224 */                     return f.isFile();
/*     */                   }
/*     */ 
/*     */ 
/*     */                   
/*     */                   public String getDescription() {
/* 230 */                     return "Files";
/*     */                   }
/*     */                 });
/*     */             
/* 234 */             chooser.setCurrentDirectory(new File(this.baseDir));
/* 235 */             int returnVal = chooser.showOpenDialog(this.panel);
/*     */             
/* 237 */             final File worldMachineOutput = (returnVal == 0) ? chooser.getSelectedFile() : new File(this.baseDir + this.baseFile);
/*     */ 
/*     */             
/* 240 */             long baseFileSize = worldMachineOutput.length();
/*     */             
/* 242 */             final double mapDimension = Math.sqrt(baseFileSize) / 2.0D;
/* 243 */             logger.info("Math.sqrt(fis.getChannel().size())/2 " + mapDimension);
/*     */             
/* 245 */             if (logger.isLoggable(Level.FINE))
/*     */             {
/* 247 */               logger.fine("Opening " + worldMachineOutput.getName() + ", length: " + baseFileSize + " Bytes");
/*     */             }
/*     */ 
/*     */             
/* 251 */             this.task = new Task("Loading file.")
/*     */               {
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void doWork() throws Exception
/*     */                 {
/*     */                   try {
/* 259 */                     MeshGenGui.this.imageLabel.setIcon(null);
/* 260 */                     FileInputStream fis = new FileInputStream(worldMachineOutput);
/* 261 */                     DataInputStream dis = new DataInputStream(fis);
/*     */                     
/* 263 */                     int size2 = (int)(Math.log(mapDimension) / Math.log(2.0D));
/*     */                     
/* 265 */                     int mapPixels = (int)(mapDimension * mapDimension);
/*     */ 
/*     */                     
/* 268 */                     byte[] d = new byte[mapPixels * 4];
/* 269 */                     dis.readFully(d);
/* 270 */                     dis.close();
/* 271 */                     ByteBuffer bb = ByteBuffer.wrap(d);
/* 272 */                     bb.order(ByteOrder.LITTLE_ENDIAN);
/* 273 */                     FloatBuffer fb = bb.asFloatBuffer();
/*     */                     
/* 275 */                     float[] data = new float[mapPixels];
/* 276 */                     fb.get(data);
/* 277 */                     fb.position(0);
/* 278 */                     fb.limit(data.length);
/*     */                     
/* 280 */                     fb = null;
/* 281 */                     bb = null;
/* 282 */                     d = null;
/* 283 */                     dis = null;
/* 284 */                     System.gc();
/*     */                     
/* 286 */                     MeshGenGui.this.meshGen = new MeshGen(size2, this);
/*     */                     
/* 288 */                     MeshGenGui.this.meshGen.setData(data, this);
/* 289 */                     MeshGenGui.this.imageLabel.setIcon(new ImageIcon(MeshGenGui.this.meshGen.getImage(this)));
/*     */                   }
/* 291 */                   catch (FileNotFoundException fnfe) {
/*     */                     
/* 293 */                     MeshGenGui.logger.log(Level.WARNING, "Problem loading Base Map", fnfe);
/*     */                   }
/* 295 */                   catch (IOException ioe) {
/*     */                     
/* 297 */                     MeshGenGui.logger.log(Level.WARNING, "Problem loading Base Map", ioe);
/*     */                   } 
/*     */                 }
/*     */               };
/*     */             
/* 302 */             logger.log(Level.INFO, "Created task. Now setting prio and starting.");
/* 303 */             this.task.execute();
/*     */           }
/* 305 */           catch (Exception ex) {
/*     */             
/* 307 */             logger.log(Level.WARNING, "Problem loading Base Map", ex);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 312 */           this.task = new Task("Showing Image.")
/*     */             {
/*     */ 
/*     */               
/*     */               public void doWork() throws Exception
/*     */               {
/* 318 */                 MeshGenGui.this.imageLabel.setIcon(new ImageIcon(MeshGenGui.this.meshGen.getImage(this)));
/*     */               }
/*     */             };
/*     */           
/* 322 */           this.task.execute();
/*     */         }
/*     */       
/* 325 */       } else if (e.getSource() == this.texturizeButton) {
/*     */         
/* 327 */         if (this.meshGen != null)
/*     */         {
/* 329 */           this.task = new Task("Adding textures.")
/*     */             {
/*     */               
/*     */               public void doWork() throws Exception
/*     */               {
/* 334 */                 MeshGenGui.this.imageLabel.setIcon(null);
/* 335 */                 Random random = new Random();
/* 336 */                 MeshGenGui.this.meshGen.generateTextures(random, this);
/* 337 */                 MeshGenGui.this.imageLabel.setIcon(new ImageIcon(MeshGenGui.this.meshGen.getImage(this)));
/*     */               }
/*     */             };
/*     */           
/* 341 */           this.task.execute();
/*     */         }
/*     */       
/* 344 */       } else if (e.getSource() == this.addIslandsButton) {
/*     */         
/* 346 */         if (this.meshGen != null)
/*     */         {
/* 348 */           if (this.topLayerMeshIO != null) {
/*     */             
/* 350 */             this.task = new Task("Adding islands.")
/*     */               {
/*     */                 
/*     */                 public void doWork() throws Exception
/*     */                 {
/* 355 */                   MeshGenGui.this.imageLabel.setIcon(null);
/* 356 */                   IslandAdder islandAdder = new IslandAdder(MeshGenGui.this.topLayerMeshIO, MeshGenGui.this.rockLayerMeshIO);
/* 357 */                   islandAdder.addIslands(MeshGenGui.this.meshGen.getWidth());
/* 358 */                   MeshGenGui.this.meshGen = new MeshGen(islandAdder.getTopLayer().getSizeLevel(), this);
/* 359 */                   MeshGenGui.this.meshGen.setData(islandAdder.getTopLayer(), islandAdder.getRockLayer(), this);
/* 360 */                   BufferedImage image = MeshGenGui.this.meshGen.getImage(this);
/* 361 */                   MeshGenGui.this.imageLabel.setIcon(new ImageIcon(image));
/* 362 */                   MeshGenGui.this.topLayerMeshIO = islandAdder.getTopLayer();
/* 363 */                   MeshGenGui.this.rockLayerMeshIO = islandAdder.getRockLayer();
/*     */                 }
/*     */               };
/*     */             
/* 367 */             this.task.execute();
/*     */           } else {
/*     */             
/* 370 */             logger.info("Failed to add Islands. Save map first.");
/*     */           } 
/*     */         }
/* 373 */       } else if (e.getSource() == this.loadButton) {
/*     */         
/*     */         try
/*     */         {
/* 377 */           String mapDirectory = selectMapDir();
/* 378 */           logger.info("Opening Mesh " + mapDirectory + File.separatorChar + "top_layer.map");
/* 379 */           final MeshIO meshIO = MeshIO.open(mapDirectory + File.separatorChar + "top_layer.map");
/* 380 */           logger.info("Opening Mesh " + mapDirectory + File.separatorChar + "rock_layer.map");
/* 381 */           final MeshIO meshIO2 = MeshIO.open(mapDirectory + File.separatorChar + "rock_layer.map");
/* 382 */           if (meshIO.getSize() != meshIO2.getSize())
/*     */           {
/* 384 */             logger.warning("top layer and rock layer are not the same size");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 389 */           this.task = new Task("Loading maps.")
/*     */             {
/*     */               
/*     */               public void doWork() throws Exception
/*     */               {
/* 394 */                 MeshGenGui.this.meshGen = new MeshGen(meshIO.getSizeLevel(), this);
/* 395 */                 MeshGenGui.this.meshGen.setData(meshIO, meshIO2, this);
/* 396 */                 BufferedImage image = MeshGenGui.this.meshGen.getImage(this);
/* 397 */                 MeshGenGui.this.imageLabel.setIcon(new ImageIcon(image));
/* 398 */                 MeshGenGui.this.topLayerMeshIO = meshIO;
/* 399 */                 MeshGenGui.this.rockLayerMeshIO = meshIO2;
/*     */               }
/*     */             };
/*     */           
/* 403 */           this.task.execute();
/*     */         }
/* 405 */         catch (IOException ioe)
/*     */         {
/* 407 */           logger.log(Level.WARNING, "Problem loading Map", ioe);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 419 */       else if (e.getSource() == this.generateGroundButton) {
/*     */         
/* 421 */         if (this.meshGen != null)
/*     */         {
/* 423 */           this.task = new Task("Generating ground.")
/*     */             {
/*     */               
/*     */               public void doWork() throws Exception
/*     */               {
/* 428 */                 MeshGenGui.this.imageLabel.setIcon(null);
/* 429 */                 MeshGenGui.this.meshGen.generateGround(new Random(), this);
/* 430 */                 BufferedImage image = MeshGenGui.this.meshGen.getImage(this);
/* 431 */                 MeshGenGui.this.imageLabel.setIcon(new ImageIcon(image));
/*     */               }
/*     */             };
/*     */           
/* 435 */           this.task.execute();
/*     */         }
/*     */       
/* 438 */       } else if (e.getSource() == this.saveButton) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 443 */         this.task = new Task("Saving maps.")
/*     */           {
/*     */ 
/*     */             
/*     */             public void doWork() throws Exception
/*     */             {
/*     */               try {
/* 450 */                 MeshGenGui.this.imageLabel.setIcon(null);
/* 451 */                 MeshIO meshIO = MeshIO.createMap("top_layer_out.map", MeshGenGui.this.meshGen.getLevel(), MeshGenGui.this.meshGen.getData(this));
/* 452 */                 meshIO.close();
/* 453 */                 MeshGenGui.this.topLayerMeshIO = meshIO;
/* 454 */                 MeshGenGui.this.task.setNote(49, "Created top_layer_out.map");
/* 455 */                 MeshGenGui.logger.info("Created top_layer_out.map");
/* 456 */                 meshIO = MeshIO.createMap("rock_layer_out.map", MeshGenGui.this.meshGen.getLevel(), MeshGenGui.this.meshGen.getRockData(this));
/* 457 */                 meshIO.close();
/* 458 */                 MeshGenGui.this.rockLayerMeshIO = meshIO;
/* 459 */                 MeshGenGui.this.task.setNote(99, "Created rock_layer_out.map");
/* 460 */                 MeshGenGui.logger.info("Created rock_layer_out.map");
/*     */               }
/* 462 */               catch (IOException e1) {
/*     */                 
/* 464 */                 MeshGenGui.logger.log(Level.WARNING, "problem saving Map", e1);
/*     */               } 
/*     */             }
/*     */           };
/*     */         
/* 469 */         this.task.execute();
/*     */       }
/* 471 */       else if (e.getSource() == this.saveImageButton) {
/*     */         
/* 473 */         if (this.meshGen != null)
/*     */         {
/* 475 */           this.task = new Task("Saving png.")
/*     */             {
/*     */ 
/*     */               
/*     */               public void doWork() throws Exception
/*     */               {
/*     */                 try {
/* 482 */                   MeshGenGui.this.imageLabel.setIcon(null);
/* 483 */                   BufferedImage image = MeshGenGui.this.meshGen.getImage(this);
/* 484 */                   MeshGenGui.this.imageLabel.setIcon(new ImageIcon(image));
/* 485 */                   ImageIO.write(image, "png", new File("map.png"));
/* 486 */                   MeshGenGui.logger.info("Created map.png");
/*     */                 }
/* 488 */                 catch (IOException e1) {
/*     */                   
/* 490 */                   MeshGenGui.logger.log(Level.WARNING, "problem saving image", e1);
/*     */                 } 
/*     */               }
/*     */             };
/*     */           
/* 495 */           this.task.execute();
/*     */         }
/*     */       
/* 498 */       } else if (e.getSource() == this.layerToggle) {
/*     */         
/* 500 */         if (this.meshGen != null)
/*     */         {
/* 502 */           this.task = new Task("Toggling layer.")
/*     */             {
/*     */               
/*     */               public void doWork() throws Exception
/*     */               {
/* 507 */                 MeshGenGui.this.imageLabel.setIcon(null);
/* 508 */                 MeshGenGui.this.meshGen.setImageLayer(MeshGenGui.this.layerToggle.isSelected() ? 1 : 0);
/* 509 */                 BufferedImage image = MeshGenGui.this.meshGen.getImage(this);
/* 510 */                 MeshGenGui.this.imageLabel.setIcon(new ImageIcon(image));
/*     */               }
/*     */             };
/*     */           
/* 514 */           this.task.execute();
/*     */         }
/*     */       
/*     */       } 
/* 518 */     } catch (RuntimeException re) {
/*     */       
/* 520 */       logger.log(Level.SEVERE, "Error while handling ActionClass ", re);
/* 521 */       throw re;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract class Task
/*     */     extends SwingWorker<Void, Void>
/*     */   {
/*     */     int pmax;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Task(final String title) {
/* 545 */       this.pmax = 100; (new Thread("Wurm MeshGenGui") { public void run() { MeshGenGui.this.taskOutput.setText(title); MeshGenGui.this.frame.setTitle(title + " Please wait..."); MeshGenGui.Task.this.setProgress(0);
/*     */             MeshGenGui.this.progressBar.setValue(0);
/*     */             MeshGenGui.this.frame.setVisible(true);
/*     */             MeshGenGui.this.enableButtons(true); } }).start();
/* 549 */     } public void setMax(int max) { this.pmax = max; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setNote(String text) {
/* 554 */       System.out.println(text);
/* 555 */       if (MeshGenGui.this.taskOutput.getText().length() > 0)
/* 556 */         MeshGenGui.this.taskOutput.append("\n"); 
/* 557 */       MeshGenGui.this.taskOutput.append(text);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setNote(int progress, String text) {
/* 564 */       setNote(progress);
/* 565 */       setNote(text);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setNote(int progress) {
/* 570 */       int p = Math.max(Math.min(progress * 100 / this.pmax, 100), 0);
/*     */       
/* 572 */       MeshGenGui.this.progressBar.setValue(p);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Void doInBackground() {
/*     */       try {
/* 583 */         doWork();
/*     */       }
/* 585 */       catch (OutOfMemoryError e) {
/*     */         
/* 587 */         MeshGenGui.logger.log(Level.SEVERE, "Out of memory (Java heap space)", e);
/* 588 */         SwingUtilities.invokeLater(new Runnable()
/*     */             {
/*     */               
/*     */               public void run()
/*     */               {
/* 593 */                 String msg = StringUtil.format("Unexpected problem: %s", new Object[] { this.val$e
/* 594 */                       .toString() });
/*     */                 
/* 596 */                 JOptionPane.showMessageDialog(MeshGenGui.this.panel, msg, "Unrecoverable error", 0);
/*     */               }
/*     */             });
/*     */ 
/*     */       
/*     */       }
/* 602 */       catch (Exception e) {
/*     */         
/* 604 */         MeshGenGui.logger.log(Level.SEVERE, "Error while performing Task.doWork ", e);
/* 605 */         SwingUtilities.invokeLater(new Runnable()
/*     */             {
/*     */               
/*     */               public void run()
/*     */               {
/* 610 */                 String msg = StringUtil.format("Unexpected problem: %s", new Object[] { this.val$e
/* 611 */                       .toString() });
/*     */                 
/* 613 */                 JOptionPane.showMessageDialog(MeshGenGui.this.panel, msg, "Unrecoverable error", 0);
/*     */               }
/*     */             });
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 620 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void done() {
/* 631 */       setProgress(100);
/* 632 */       MeshGenGui.this.progressBar.setValue(100);
/* 633 */       setNote("Finished!");
/* 634 */       MeshGenGui.this.frame.setVisible(false);
/* 635 */       MeshGenGui.this.enableButtons(false);
/*     */     }
/*     */     
/*     */     public abstract void doWork() throws Exception; }
/*     */   
/*     */   private void enableButtons(boolean running) {
/* 641 */     if (running) {
/*     */       
/* 643 */       this.progressBar.setVisible(true);
/* 644 */       this.layerToggle.setEnabled(false);
/* 645 */       this.flowButton.setEnabled(false);
/* 646 */       this.texturizeButton.setEnabled(false);
/* 647 */       this.addIslandsButton.setEnabled(false);
/* 648 */       this.saveButton.setEnabled(false);
/* 649 */       this.loadButton.setEnabled(false);
/* 650 */       this.saveImageButton.setEnabled(false);
/*     */     }
/*     */     else {
/*     */       
/* 654 */       boolean sf = (this.meshGen != null);
/* 655 */       this.progressBar.setVisible(false);
/* 656 */       this.layerToggle.setEnabled(sf);
/* 657 */       this.flowButton.setEnabled(true);
/* 658 */       this.texturizeButton.setEnabled(sf);
/* 659 */       this.addIslandsButton.setEnabled(sf);
/* 660 */       this.saveButton.setEnabled(sf);
/* 661 */       this.loadButton.setEnabled(true);
/* 662 */       this.saveImageButton.setEnabled(sf);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String selectMapDir() {
/*     */     while (true) {
/* 670 */       JFileChooser chooser = new JFileChooser();
/* 671 */       chooser.setFileFilter(new FileFilter()
/*     */           {
/*     */             
/*     */             public boolean accept(File f)
/*     */             {
/* 676 */               return f.isDirectory();
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public String getDescription() {
/* 682 */               return "Directories";
/*     */             }
/*     */           });
/* 685 */       chooser.setCurrentDirectory(new File("."));
/* 686 */       chooser.setAcceptAllFileFilterUsed(false);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 691 */       chooser.setFileFilter(new FileFilter()
/*     */           {
/*     */             
/*     */             public boolean accept(File f)
/*     */             {
/* 696 */               return f.isFile();
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public String getDescription() {
/* 702 */               return "Files";
/*     */             }
/*     */           });
/* 705 */       chooser.setCurrentDirectory(new File("."));
/* 706 */       chooser.setFileSelectionMode(1);
/* 707 */       chooser.setDialogTitle("Select the directory containing the map files");
/* 708 */       chooser.setApproveButtonText("Use this dir");
/* 709 */       chooser.setApproveButtonToolTipText("<html>The selected directory will be used by the Mesh Generator GUI<br> to load the top_layer.map and rock_layer.map files</html");
/*     */ 
/*     */       
/* 712 */       int returnVal = chooser.showOpenDialog(this.panel);
/* 713 */       if (returnVal == 0) {
/*     */         
/* 715 */         File file = chooser.getSelectedFile();
/* 716 */         if (file.isFile()) {
/*     */           
/* 718 */           if (logger.isLoggable(Level.FINE))
/*     */           {
/* 720 */             logger.fine("Using the directory containing the chosen file: " + file);
/*     */           }
/* 722 */           file = file.getParentFile();
/*     */         } 
/* 724 */         if (!file.exists()) {
/* 725 */           file.mkdir();
/*     */         }
/* 727 */         if ((file.listFiles()).length != 0) {
/*     */           
/* 729 */           int i = JOptionPane.showConfirmDialog(this.panel, "<html>Use \"" + file.toString() + "\"?", "Confirm directory", 0);
/*     */           
/* 731 */           if (i == 0) {
/* 732 */             return file.toString();
/*     */           }
/*     */           continue;
/*     */         } 
/* 736 */         int option = JOptionPane.showConfirmDialog(this.panel, "<html>Use \"" + file.toString() + "\"?<br><br><b>Warning: The directory is empty.</b><br>This should contain the maps", "Confirm directory", 0);
/*     */ 
/*     */         
/* 739 */         if (option == 0)
/* 740 */           return file.toString(); 
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 745 */     return null;
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
/*     */   public static void main(String[] args) {
/* 785 */     (new MeshGenGui()).setVisible(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\meshgen\MeshGenGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */