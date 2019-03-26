package com.general.mbts4ma.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.general.mbts4ma.view.dialog.ExtractCESsDialog;
import com.general.mbts4ma.view.dialog.ExtractEventFlowDialog;
import com.general.mbts4ma.view.dialog.ProjectPropertiesDialog;
import com.general.mbts4ma.view.dialog.WebProjectPropertiesDialog;
import com.general.mbts4ma.view.framework.bo.GraphConverter;
import com.general.mbts4ma.view.framework.bo.GraphProjectBO;
import com.general.mbts4ma.view.framework.bo.GraphSolver;
import com.general.mbts4ma.view.framework.graph.CustomGraphActions;
import com.general.mbts4ma.view.framework.util.ASTSpoonScanner;
import com.general.mbts4ma.view.framework.util.HardwareUtil;
import com.general.mbts4ma.view.framework.util.PageObject;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.github.eta.esg.Vertex;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import spoon.Launcher;
import spoon.reflect.CtModelImpl;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;
import spoon.reflect.visitor.filter.TypeFilter;

public class MainView extends JFrame {

	public static final String MARKED_EDGE = "MARKED_EDGE";
	public static final String GENERATED_EDGE = "GENERATED_EDGE";
	public static final String NORMAL_VERTEX = "NORMAL_VERTEX";
	public static final String EVENT_VERTEX = "EVENT_VERTEX";
	public static final String PARAMETER_VERTEX = "PARAMETER_VERTEX";
	public static final String GENERATED_EVENT_VERTEX = "GENERATED_EVENT_VERTEX";
	public static final String START_VERTEX = "START_VERTEX";
	public static final String END_VERTEX = "END_VERTEX";

	public static final String ID_START_VERTEX = "1000";
	public static final String ID_END_VERTEX = "2000";

	private static final long serialVersionUID = 8273385277816531639L;

	/* BEGIN WEB VARIABLES */
	//public static ArrayList<String> metodos;
	//public static ArrayList<ArrayList<String>> metodosWeb;
	public static ArrayList<String> pageObjectsPath;
	public static ArrayList<PageObject> pageObjects;
	/* END WEB VARIABLES */
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	private mxGraph graph = null;
	private mxGraphComponent graphComponent = null;

	public static final String MY_CUSTOM_VERTEX_STYLE = "MY_CUSTOM_VERTEX_STYLE";
	public static final String MY_CUSTOM_EDGE_STYLE = "MY_CUSTOM_EDGE_STYLE";

	private GraphProjectVO graphProject = null;

	private JButton btnNew;
	private JButton btnOpen;
	private JButton btnSave;
	private JButton btnPreferences;
	private JButton btnClose;
	private JButton btnExecuteGraph;
	private JButton btnExit;

	private JMenu mnFile;
	private JMenu mnProject;
	private JMenu mnSettings;

	private JMenuItem mnItemNew;
	private JMenuItem mnItemNewWebApplicationProject;
	private JMenuItem mnItemOpen;
	private JMenuItem mnItemSave;
	private JMenuItem mnItemClose;
	private JMenuItem mnItemExit;

	private JMenuItem mnItemProperties;
	private JMenuItem mnItemExtractEventFlow;
	private JMenu mnItemExportGraph;
	private JMenu mnItemImportGraph;

	private JMenuItem mnItemToPngImage;
	private JMenuItem mnItemToXml;
	private JMenuItem mnItemFromXml;

	private JMenuItem mnItemExtractCESs;
	private JMenuItem mnItemPreferences;
	private JButton btnProperties;
	private JButton btnExtractEventFlow;
	private JButton btnGenerateReusedEsg;
	private JMenuItem mnItemGenerateReusedESG;

	public MainView() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 600, 450);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.setJMenuBar(menuBar);

		this.mnFile = new JMenu("File");
		this.mnFile.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnFile);

		this.mnItemNew = new JMenuItem("New");
		this.mnItemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newProject();
			}
		});
		this.mnItemNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemNew);
		
		this.mnItemNewWebApplicationProject = new JMenuItem("New Web App Project");
		this.mnItemNewWebApplicationProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newWebAppProject();
			}
		});
		this.mnItemNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemNewWebApplicationProject);

		this.mnItemOpen = new JMenuItem("Open");
		this.mnItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.openProject();
			}
		});
		this.mnItemOpen.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemOpen);

		this.mnItemSave = new JMenuItem("Save");
		this.mnItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.saveProject();
			}
		});
		this.mnItemSave.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemSave);

		this.mnItemClose = new JMenuItem("Close");
		this.mnItemClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.closeProject();
			}
		});
		this.mnItemClose.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemClose);

		JSeparator separator = new JSeparator();
		this.mnFile.add(separator);

		this.mnItemExit = new JMenuItem("Exit");
		this.mnItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exitApplication();
			}
		});
		this.mnItemExit.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemExit);

		this.mnProject = new JMenu("Project");
		this.mnProject.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnProject);

		this.mnItemProperties = new JMenuItem("Properties");
		this.mnItemProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayProjectProperties();
			}
		});
		this.mnItemProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemProperties);

		this.mnItemExtractEventFlow = new JMenuItem("Show edges (event pairs)");
		this.mnItemExtractEventFlow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.showEventPairs();
			}
		});
		this.mnItemExtractEventFlow.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemExtractEventFlow);

		this.mnItemExtractCESs = new JMenuItem("Extract CESs");
		this.mnItemExtractCESs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.extractCESs();
			}
		});
		this.mnProject.add(this.mnItemExtractCESs);
		this.mnItemExtractCESs.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.mnItemGenerateReusedESG = new JMenuItem("Generate reused ESG");
		this.mnItemGenerateReusedESG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.generateReusedESG();
			}
		});
		this.mnItemGenerateReusedESG.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemGenerateReusedESG);

		JSeparator separator_1 = new JSeparator();
		this.mnProject.add(separator_1);

		this.mnItemExportGraph = new JMenu("Export graph");
		this.mnItemExportGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemExportGraph);

		this.mnItemToPngImage = new JMenuItem("to PNG (Image)");
		this.mnItemToPngImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exportToPng();
			}
		});
		this.mnItemToPngImage.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemExportGraph.add(this.mnItemToPngImage);

		JSeparator separator_3 = new JSeparator();
		this.mnItemExportGraph.add(separator_3);

		this.mnItemToXml = new JMenuItem("to XML");
		this.mnItemToXml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exportToXml();
			}
		});
		this.mnItemToXml.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemExportGraph.add(this.mnItemToXml);

		this.mnItemImportGraph = new JMenu("Import graph");
		this.mnItemImportGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemImportGraph);

		this.mnItemFromXml = new JMenuItem("from XML");
		this.mnItemFromXml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.importFromXml();
			}
		});
		this.mnItemFromXml.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemImportGraph.add(this.mnItemFromXml);

		this.mnSettings = new JMenu("Settings");
		this.mnSettings.setEnabled(false);
		this.mnSettings.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnSettings);

		this.mnItemPreferences = new JMenuItem("Preferences");
		this.mnItemPreferences.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnSettings.add(this.mnItemPreferences);

		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		
		/*this.tabbedPane = new JTabbedPane();
		this.tabbedPane.addTab("Model", this.contentPane);*/		

		this.setContentPane(this.contentPane);

		this.init();

		this.updateControllers();
	}

	private void updateControllers() {
		boolean isProjectOpened = this.graphProject != null;

		this.btnNew.setEnabled(!isProjectOpened);
		this.btnOpen.setEnabled(!isProjectOpened);
		this.btnSave.setEnabled(isProjectOpened);
		this.btnClose.setEnabled(isProjectOpened);

		this.btnProperties.setEnabled(isProjectOpened);
		this.btnExtractEventFlow.setEnabled(isProjectOpened);
		this.btnExecuteGraph.setEnabled(isProjectOpened);
		this.btnGenerateReusedEsg.setEnabled(isProjectOpened);

		this.btnPreferences.setEnabled(false);
		this.btnExit.setEnabled(true);

		this.mnItemNew.setEnabled(!isProjectOpened);
		this.mnItemNewWebApplicationProject.setEnabled(!isProjectOpened);
		this.mnItemOpen.setEnabled(!isProjectOpened);
		this.mnItemSave.setEnabled(isProjectOpened);
		this.mnItemClose.setEnabled(isProjectOpened);

		this.mnProject.setEnabled(isProjectOpened);

		this.mnItemPreferences.setEnabled(true);
		this.mnItemExit.setEnabled(true);
	}

	private void initGraph() {
		if (this.graphComponent != null) {
			this.contentPane.remove(this.graphComponent);
		}

		this.graph = new mxGraph();

		this.graph.setCellsEditable(true);

		this.graphComponent = new mxGraphComponent(this.graph);

		this.graphComponent.getViewport().setOpaque(true);
		this.graphComponent.getViewport().setBackground(Color.WHITE);
		// this.graphComponent.setGridVisible(true);
		// this.graphComponent.setGridColor(Color.GRAY);

		this.graphComponent.setToolTips(true);

		this.setCustomStylesheet();

		this.configureKeyboardEvents();

		/* this.graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
		
			@Override
			public void invoke(Object sender, mxEventObject event) {
				try {
					System.out.println(((mxCell) ((mxGraphSelectionModel) sender).getCell()).getValue());
				} catch (Exception e) {
				}
			}
		}); */

		this.graph.addListener(mxEvent.CELLS_REMOVED, new mxIEventListener() {

			@Override
			public void invoke(Object o, mxEventObject eo) {
				Object[] cells = (Object[]) eo.getProperty("cells");

				for (Object oCell : cells) {
					MainView.this.graphProject.removeEventInstanceByVertices(((mxCell) oCell).getId());
					MainView.this.graphProject.removeMethodTemplatePropertiesByVertice(((mxCell) oCell).getId());
					MainView.this.graphProject.removeEdgeTemplateByVertice(((mxCell) oCell).getId());
				}
			}
		});
		
		this.graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);

				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
					String nodeValue = JOptionPane.showInputDialog(null, "Enter the value of the node", "Attention", JOptionPane.INFORMATION_MESSAGE);
										
					if (nodeValue != null && !"".equalsIgnoreCase(nodeValue)) {
						MainView.this.graph.getModel().beginUpdate();

						MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), UUID.randomUUID().toString(), nodeValue, e.getX() - 50, e.getY() - 25, 100, 50, NORMAL_VERTEX);

						MainView.this.graph.getModel().endUpdate();
					}
										
				} else {
					if (SwingUtilities.isMiddleMouseButton(e)) {
						MainView.this.graphComponent.zoomActual();
					} else if (SwingUtilities.isRightMouseButton(e)) {
						final JPopupMenu popup = new JPopupMenu();
						popup.add(MainView.this.bind("Delete", CustomGraphActions.getDeleteAction()));
						popup.add(MainView.this.bind("Rename", CustomGraphActions.getEditAction()));
						popup.add(MainView.this.bind("Display ID", CustomGraphActions.getDisplayIdAction()));
						popup.add(MainView.this.bind("Parameters", CustomGraphActions.getParametersAction(MainView.this.graphProject)));

						popup.addSeparator();

						popup.add(MainView.this.bind("Select All Edges", CustomGraphActions.getSelectAllEdgesAction()));
						popup.add(MainView.this.bind("Select All Vertices", CustomGraphActions.getSelectAllVerticesAction()));

						popup.addSeparator();
						
						if (graphProject.getItsAndroidProject()) {

							final JMenu methodTemplatesMenu = new JMenu("Method Templates");
	
							methodTemplatesMenu.add(MainView.this.bind("Clear Method Template", CustomGraphActions.getClearMethodTemplateAction(MainView.this.graphProject)));
	
							methodTemplatesMenu.addSeparator();
	
							Map<String, String> methodTemplates = GraphProjectBO.getMethodTemplates(graphProject.getFramework());
	
							Iterator<String> iMethodTemplates = methodTemplates.keySet().iterator();
	
							while (iMethodTemplates.hasNext()) {
								String key = iMethodTemplates.next();
	
								methodTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, key)));
							}
	
							popup.add(methodTemplatesMenu);
	
							final JMenu edgeTemplatesMenu = new JMenu("Edge Templates");
	
							edgeTemplatesMenu.add(MainView.this.bind("Clear Edge Template", CustomGraphActions.getClearEdgeTemplateAction(MainView.this.graphProject)));
	
							edgeTemplatesMenu.addSeparator();
	
							Map<String, String> edgeTemplates = GraphProjectBO.getEdgeTemplates(graphProject.getFramework());
	
							Iterator<String> iEdgeTemplates = edgeTemplates.keySet().iterator();
	
							while (iEdgeTemplates.hasNext()) {
								String key = iEdgeTemplates.next();
	
								edgeTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineEdgeTemplateAction(MainView.this.graphProject, key)));
							}
	
							popup.add(edgeTemplatesMenu);
							
						} else { //Senão é web project
							ArrayList<String> pageObjectsPath = new ArrayList(Arrays.asList(graphProject.getWebProjectPageObject().split(",")));
							
							JMenu pageObjectsTemplatesMenu;
							
							Map<String, String> metodosWeb;
							
							//for (Iterator<String> i = pageObjectsPath.iterator(); i.hasNext(); ){
							for (Iterator<PageObject> i = graphProject.getPageObjects().iterator(); i.hasNext(); ) {
								
								PageObject pageObjectNext = i.next();
								
								String fileContentPageObject = pageObjectNext.getContent();
								String fileNamePageObject = pageObjectNext.getClassName();
								
								pageObjectsTemplatesMenu = new JMenu(fileNamePageObject);
								
								metodosWeb = new LinkedHashMap<String, String>();
								
								pageObjectNext.getParsedClass().getAllMethods();
								
								Set<CtMethod> ctMethods = pageObjectNext.getParsedClass().getMethods();
								
						        //Run through all methods
						        for (CtMethod method : ctMethods) {
			
									String key = method.getSimpleName();
									
									pageObjectsTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, key)));
			
								}
								
								popup.add(pageObjectsTemplatesMenu);
								
							}
						}

						popup.show(MainView.this.graphComponent, e.getX(), e.getY());
					}
				}
			}

		});

		this.graphComponent.getGraphControl().addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					MainView.this.graphComponent.zoomIn();
				} else {
					MainView.this.graphComponent.zoomOut();
				}
			}
		});

		this.getContentPane().add(this.graphComponent);
		this.refreshContentPane();
	}

	@SuppressWarnings("serial")
	private Action bind(String name, final Action action) {

		return new AbstractAction(name, null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(MainView.this.graphComponent, e.getID(), e.getActionCommand()));
			}
		};

	}

	private void refreshContentPane() {
		this.contentPane.revalidate();
		this.contentPane.repaint();
	}

	private void createBasicGraph() {
		MainView.this.graph.getModel().beginUpdate();

		MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), ID_START_VERTEX, "[", 50, 400, 50, 50, START_VERTEX);
		MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), ID_END_VERTEX, "]", 1400, 500, 50, 50, END_VERTEX);

		MainView.this.graph.getModel().endUpdate();
	}

	private void newProject() {
		this.graphProject = null;

		ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(this.graphProject);

		dialog.setVisible(true);

		this.graphProject = dialog.getGraphProject();

		if (this.graphProject != null) {
			this.initGraph();

			this.createBasicGraph();
		}

		this.updateControllers();
	}
	
	/*
	 * NAO FUNCIONOU **/
	private String getIdFromPreviousVertice(ArrayList<String> sequence, String last) {
		
		try {
			GraphSolver.solve(this.graph);
			
			List<List<Vertex>> cess = GraphSolver.getCess();
			
			mxCell vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(ID_START_VERTEX);
			
			for (List<Vertex> vertexList : cess) {
							
				if (vertexList.get(sequence.size()).getName() == sequence.get(sequence.size()-1)) {
					
					boolean control = true;
					int count = sequence.size();
					
					while (control == true && count > 0) {
						
						if (sequence.get(count-1) != vertexList.get(count).getName()) {
							control = false;
						}
						
						count--;
						
					}
					
					//Nesse caso, nao precisa criar o vertice
					if (control == true) 
						return null;
					
				} else if (vertexList.get(sequence.size()-1).getName() == sequence.get(sequence.size()-2)) {
					
					boolean control = true;
					int count = sequence.size()-1;
					
					while (control == true && count > 0) {
						
						if (sequence.get(count-1) != vertexList.get(count).getName()) {
							control = false;
						}
						
						count--;
						
					}
					
					//Nesse caso, retorna o ultimo vertice
					if (control == true) 
						return vertexList.get(sequence.size()-1).getId();
					
				}
				
			}
				
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		return last;
				
	}

	//Funcao retorna uma das listas, dentro do parametro searchingIntoSequences, que possui a lista enviada por parametro (searchingBySequence) como sublist.
	private LinkedHashMap<String, String> getPreviousVerticesList(ArrayList<LinkedHashMap<String, String>> searchingIntoSequences, ArrayList<String> searchingBySequence) {
		
		if (searchingBySequence.isEmpty()) 
			return null;
		
		for (LinkedHashMap<String, String> linkedHashMap : searchingIntoSequences) {
			
			boolean control = true;
			int count = searchingBySequence.size()-1;
			
			while (control == true && count > 0 && linkedHashMap.size() > count) {
				
				Set<Map.Entry<String, String>> mapSetSearchingIntoList = linkedHashMap.entrySet();
		        Map.Entry<String, String> elementAtSearchingIntoList = (Map.Entry<String, String>) mapSetSearchingIntoList.toArray()[count];
		        
		        String elementAtSearchingForList = searchingBySequence.get(count);
		        
		        if (!elementAtSearchingForList.equals(elementAtSearchingIntoList.getKey())) {
		        	control = false;
		        }

				count--;
				
			}
			
			//Nesse caso, retorna a lista
			if (control == true) 
				return linkedHashMap;
			
		}
				
		return null;
	}
	
	private void newWebAppProject() {
		this.graphProject = null;

		WebProjectPropertiesDialog dialog = new WebProjectPropertiesDialog(this.graphProject);

		dialog.setVisible(true);

		this.graphProject = dialog.getGraphProject();

		if (this.graphProject != null) {
			graphProject.setIsWebProject(true);
			
			this.initGraph();
			
			this.createBasicGraph();
		}
		
		for (PageObject po : graphProject.getPageObjects()) {
			System.out.println(po.getParsedClass());
		}
		
		/* PUXA TODOS OS ARQUIVOS DE UMA PASTA E CRIA UM MODELO SPOON (UTIL PARA OS PAGE OBJECTS)*/
		Launcher launcher = new Launcher();
		launcher.getEnvironment().setNoClasspath(true);
		launcher.addInputResource("/Users/guimat/po/");
		//launcher.getModelBuilder().setBinaryOutputDirectory(new File("./src/main/java/com/basic/dao/"));
		launcher.buildModel();
		
		final CtModelImpl model = (CtModelImpl) launcher.getModel(); 
		List<CtType<?>> classesList = launcher.getFactory().Class().getAll();
				
		for (CtType<?> type : classesList) {
			
			Set<CtMethod<?>> ctMethods = type.getMethods();
			
			double verticalDistance = 50;
			
			//Essa lista serve para identificar caminhos pré-existentes
			ArrayList<LinkedHashMap<String, String>> listaStatements = new ArrayList<LinkedHashMap<String, String>>();
			
			for (CtMethod<?> method : ctMethods) { 
									        	
	            //Get the annotations to look for test methods        	
	        	for (CtAnnotation<? extends Annotation> ann : method.getAnnotations()) {
	        		
	                if (ann.toString().contains("org.junit.Test")) {
	                	CtBlock block = method.getBody();
	                	
	                	//Grava a sequência de statements desse método com o respectivo id do vértice
	                	LinkedHashMap<String, String> methodSequence = new LinkedHashMap<String, String>();
	                		              	                	
	                    List<CtStatement> statements = block.getStatements();
	                    
	                    ArrayList<String> statementsSequence = new ArrayList<String>();
	                    
	                    double horizontalDistance = 200;	                   
	                    
	                    mxCell lastVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(ID_START_VERTEX); 	                    	                    
	                    	                    
	                    for (CtStatement statement : statements) {
	                    		                    	
	                    	ArrayList<CtElement> elementsFromStatement = (new ASTSpoonScanner()).visitStatementAST(statement);	     	                    	
	                    	
	                    	System.out.println(elementsFromStatement);
	                    	
	                    	//Check if the statement is an Assert
	                    	if (elementsFromStatement.size() > 0 && elementsFromStatement.get(elementsFromStatement.size()-1) instanceof CtInvocation) {	                    		
	                    		CtInvocation isAssert = (CtInvocation) elementsFromStatement.get(elementsFromStatement.size()-1);
	                    	
	                    		//If an Assert, remove all related invocations
	                    		if (isAssert.toString().contains("org.junit.Assert")) {
		                    		elementsFromStatement.clear();
		                    		elementsFromStatement.add(isAssert);
		                    	}
	                    	}
	                    	
	                    	for (CtElement ctElement : elementsFromStatement) {
	                    		
	                    		if (ctElement instanceof CtInvocation) {
	                    		
		                    		CtInvocation ctInvocation = (CtInvocation) ctElement;
		                    		
		                    		//Add to sequence control array
		                    		//statementsSequence.add(ctInvocation.getTarget().getType() + "::" + ctInvocation.getExecutable());
		                    		
		                    		double vertexSize = ctInvocation.getExecutable().getSimpleName().toString().length() * 3 + 120;
		                    		
		                    		statementsSequence.add(ctInvocation.getTarget().getType() + "::" + ctInvocation.getExecutable());
		                    		
		                    		LinkedHashMap<String, String> listLastCommonVertex = getPreviousVerticesList(listaStatements, statementsSequence);

		                    		mxCell newVertex = null;
		                    		
		                    		String newVertexId = UUID.randomUUID().toString();
		                    		
		                    		boolean createNewVertex = true;
		                    		
		                    		if (listLastCommonVertex != null && listLastCommonVertex.size() > 0) {
		                    			Set<Map.Entry<String, String>> mapSetSearchingReturnedList = listLastCommonVertex.entrySet();
		                		        Map.Entry<String, String> lastElementAtSearchingIntoList = (Map.Entry<String, String>) mapSetSearchingReturnedList.toArray()[statementsSequence.size()-1];
		                    			
		                		        String lastElementAtSearchingForList = statementsSequence.get(statementsSequence.size()-1);
		                		        
		                		        if (lastElementAtSearchingForList.equals(lastElementAtSearchingIntoList.getKey())) {
		                		        	
		                		        	newVertexId = lastElementAtSearchingIntoList.getValue();
		                		        	createNewVertex = false;
		                		        	
		                		        } else if (listLastCommonVertex.size() > 1 && statementsSequence.size() > 1) {
		                		        	
		                		        	Map.Entry<String, String> beforeLastElementAtSearchingIntoList = (Map.Entry<String, String>) mapSetSearchingReturnedList.toArray()[statementsSequence.size()-2];
		                		        	String beforeLastElementAtSearchingForList = statementsSequence.get(statementsSequence.size()-2);
		                		        	
		                		        	if (beforeLastElementAtSearchingForList.equals(beforeLastElementAtSearchingIntoList.getKey())) {
		                		        		newVertexId = lastElementAtSearchingIntoList.getValue();
		                		        	}
		                		        }
		                    		}
		                    		
		                    		if (createNewVertex) {		                    		
			                    		//Se é assert, o label e a cor do vértice é diferente
			                    		if (ctInvocation.toString().contains("org.junit.Assert")) {
			                    			newVertex = (mxCell) graph.insertVertex(MainView.this.graph.getDefaultParent(), newVertexId, "ASSERT\n" + ctInvocation.getExecutable().getSimpleName(), horizontalDistance, verticalDistance, vertexSize, 50, MainView.NORMAL_VERTEX);
			                    		} else {
			                    			newVertex = (mxCell) graph.insertVertex(MainView.this.graph.getDefaultParent(), newVertexId, ctInvocation.getTarget().getType().getSimpleName() + "::\n" + ctInvocation.getExecutable().getSimpleName(), horizontalDistance, verticalDistance, vertexSize, 50, MainView.GENERATED_EVENT_VERTEX);
			                    		}
			                    		
			                    		graph.insertEdge(graph.getDefaultParent(), UUID.randomUUID().toString(), "", lastVertex, newVertex, MainView.GENERATED_EDGE);										
										
			                    		//Update methodTemplateByVertice
			                    		this.graphProject.updateMethodTemplateByVertice(newVertexId, ctInvocation.getTarget().getType() + "::" + ctInvocation.getExecutable());
		                    		} else {
		                    			newVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(newVertexId);
		                    		}
		                    				                    		
		                    		methodSequence.put(ctInvocation.getTarget().getType() + "::" + ctInvocation.getExecutable(), newVertexId);
									
									lastVertex = newVertex;
	                    		
			                    	horizontalDistance += vertexSize + 50;
			                    	
	                    		}
		                    	
							}
	                    		                    	
		    				System.out.println("---------\n---------\n---------");
	                    	
	                    }
	                    
	                    verticalDistance += 100;
	                    
	                    //link the last vertex created to the last vertex of the graph
	                    mxCell lastGraphVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(ID_END_VERTEX);
	                    graph.insertEdge(graph.getDefaultParent(), UUID.randomUUID().toString(), "", lastVertex, lastGraphVertex, MainView.GENERATED_EDGE);	                    
	                   
	                    listaStatements.add(methodSequence);
	                    
	                    int longerArrayStatement = listaStatements.get(0).size();	                    
	                    
	                    for (LinkedHashMap statement : listaStatements) {
							if (statement.size() > longerArrayStatement)
								longerArrayStatement = statement.size();
						}
	                    
	                    mxGeometry geo = (mxGeometry) graph.getCellGeometry(lastGraphVertex).clone();
	                    geo.setY(50 * listaStatements.size());
	                    geo.setX(200 + 200 * longerArrayStatement);
	                    MainView.this.graph.getModel().setGeometry(lastGraphVertex, geo);
	                    
	                    mxCell firstVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(ID_START_VERTEX);
	                    
	                    mxGeometry geo2 = (mxGeometry) graph.getCellGeometry(firstVertex).clone();
	                    geo2.setY(50 * listaStatements.size());	                    
	                    graph.getModel().setGeometry(firstVertex, geo2);
	                    
	                }
	        	}
			}
			
	    }

		MainView.this.graph.getModel().endUpdate();
		
		
		this.updateControllers();
	}

	private void openProject() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("Model-Based Test Suite For Mobile Applications (*.mbtsma, *.graph, *.esg)", "mbtsma", "graph", "esg"));
		fileChooser.setDialogTitle("Specify a file to open");

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			this.load(fileChooser.getSelectedFile().getAbsolutePath());
		}

		this.updateControllers();
	}

	private void load(String path) {
		this.graphProject = GraphProjectBO.open(path);

		if (this.graphProject != null) {
			this.initGraph();

			try {
				GraphProjectBO.loadGraphFromXML(this.graph, this.graphProject.getGraphXML());

				JOptionPane.showMessageDialog(null, "Project successfully opened.", "Attention", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.updateControllers();
	}

	private void saveProject() {
		String fileSavingPath = null;

		if (this.graphProject.hasFileSavingPath()) {
			fileSavingPath = this.graphProject.getFileSavingPath();
		} else {
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

			fileChooser.setSelectedFile(new File(this.graphProject.getName()));
			fileChooser.setFileFilter(new FileNameExtensionFilter("Model-Based Test Suite For Mobile Applications (*.mbtsma, *.graph, *.esg)", "mbtsma", "graph", "esg"));
			fileChooser.setDialogTitle("Specify a file to save");

			int result = fileChooser.showSaveDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				fileSavingPath = fileChooser.getSelectedFile().getAbsolutePath();
			}
		}

		if (fileSavingPath != null) {
			GraphProjectBO.updateGraph(this.graphProject, this.graph);

			this.graphProject.setLastDate(new Date());
			this.graphProject.setUser(HardwareUtil.getComputerName());

			if (GraphProjectBO.save(fileSavingPath, this.graphProject)) {
				JOptionPane.showMessageDialog(null, "Project successfully saved.", "Attention", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		this.updateControllers();
	}

	private void closeProject() {
		if (this.graphComponent != null) {
			this.contentPane.remove(this.graphComponent);
		}

		this.refreshContentPane();

		this.graphProject = null;

		this.updateControllers();
	}

	private void displayProjectProperties() {
		if (this.graphProject != null) {
			ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(this.graphProject);

			dialog.setVisible(true);
		}
	}

	private void showEventPairs() {
		if (this.graphProject != null) {
			ArrayList<String> errorMsgs = GraphConverter.verifyESG(this.graph);
			if(errorMsgs.isEmpty()) {
				try {
					GraphConverter.convertToESG(this.graph);

					String eventPairs = GraphConverter.getEventFlow();

					ExtractEventFlowDialog dialog = new ExtractEventFlowDialog(this.graphProject, eventPairs);

					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			else {
				JOptionPane.showMessageDialog(null, setUpMessages(errorMsgs), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	private String setUpMessages(ArrayList<String> msgs) {
		StringBuilder setupMsg = new StringBuilder("");
		for(String msg : msgs)
			setupMsg.append(msg + "\n");
		
		return setupMsg.toString();
	}

	private void extractCESs() {
		if (this.graphProject != null) {
			ArrayList<String> errorMsgs = GraphConverter.verifyESG(this.graph);
			if(errorMsgs.isEmpty()) {
				try {
					GraphSolver.solve(this.graph);
	
					List<List<Vertex>> cess = GraphSolver.getCess();
	
					String cessAsString = GraphSolver.getCESsAsString();
	
					ExtractCESsDialog dialog = new ExtractCESsDialog(this.graph, this.graphProject, cess, cessAsString);
	
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, setUpMessages(errorMsgs), "Error", JOptionPane.ERROR_MESSAGE);
			}				
		}
	}

	private void generateReusedESG() {
		GraphProjectBO.generateReusedESG(this.graph, this.graphProject);
	}

	private void exportToPng() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
		fileChooser.setDialogTitle("Specify a file to export PNG");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.exportToPNG(this.graph, this.graphComponent, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void exportToXml() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		fileChooser.setDialogTitle("Specify a file to export XML");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.exportToXML(this.graph, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void importFromXml() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		fileChooser.setDialogTitle("Specify a file to import XML");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.importFromXML(this.graph, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void displayPreferences() {

	}

	private void exitApplication() {
		int result = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "default");

		if (result == JOptionPane.YES_OPTION) {
			this.dispose();
		}
	}

	private void init() {
		JToolBar toolBarHeader = new JToolBar();
		toolBarHeader.setFloatable(false);
		toolBarHeader.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.contentPane.add(toolBarHeader, BorderLayout.NORTH);

		this.btnNew = new JButton("");
		this.btnNew.setToolTipText("New");
		this.btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newProject();
			}
		});
		this.btnNew.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/new.png")));
		this.btnNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnNew);

		this.btnOpen = new JButton("");
		this.btnOpen.setToolTipText("Open");
		this.btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.openProject();
			}
		});
		this.btnOpen.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/open.png")));
		this.btnOpen.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnOpen);

		this.btnSave = new JButton("");
		this.btnSave.setToolTipText("Save");
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.saveProject();
			}
		});
		this.btnSave.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/save.png")));
		this.btnSave.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnSave);

		this.btnPreferences = new JButton("");
		this.btnPreferences.setToolTipText("Preferences");
		this.btnPreferences.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/preferences.png")));
		this.btnPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayPreferences();
			}
		});

		this.btnClose = new JButton("");
		this.btnClose.setToolTipText("Close");
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.closeProject();
			}
		});
		this.btnClose.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/close.png")));
		this.btnClose.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnClose);

		toolBarHeader.addSeparator();

		this.btnExecuteGraph = new JButton("");
		this.btnExecuteGraph.setToolTipText("Extract CESs");
		this.btnExecuteGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.executeGraph();
			}
		});

		this.btnProperties = new JButton("");
		this.btnProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayProjectProperties();
			}
		});
		this.btnProperties.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/properties.png")));
		this.btnProperties.setToolTipText("Properties");
		this.btnProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnProperties.setEnabled(false);
		toolBarHeader.add(this.btnProperties);

		this.btnExtractEventFlow = new JButton("");
		this.btnExtractEventFlow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.showEventPairs();
			}
		});
		this.btnExtractEventFlow.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/eventflow.png")));
		this.btnExtractEventFlow.setToolTipText("Show edges (event pairs)");
		this.btnExtractEventFlow.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnExtractEventFlow.setEnabled(false);
		toolBarHeader.add(this.btnExtractEventFlow);
		this.btnExecuteGraph.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/executegraph.png")));
		this.btnExecuteGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnExecuteGraph);

		this.btnGenerateReusedEsg = new JButton("");
		this.btnGenerateReusedEsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.generateReusedESG();
			}
		});
		this.btnGenerateReusedEsg.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/generate.png")));
		this.btnGenerateReusedEsg.setToolTipText("Generate reused ESG");
		this.btnGenerateReusedEsg.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnGenerateReusedEsg.setEnabled(false);
		toolBarHeader.add(this.btnGenerateReusedEsg);

		toolBarHeader.addSeparator();

		this.btnPreferences.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnPreferences);

		toolBarHeader.addSeparator();

		this.btnExit = new JButton("");
		this.btnExit.setToolTipText("Exit");
		this.btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exitApplication();
			}
		});
		this.btnExit.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/exit.png")));
		this.btnExit.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnExit);

		JToolBar toolBarFooter = new JToolBar();
		toolBarFooter.setFloatable(false);
		this.contentPane.add(toolBarFooter, BorderLayout.SOUTH);

		JLabel lblModelbasedTesting = new JLabel("Model-Based Test Suite For Mobile Applications (MBTS4MA)");
		lblModelbasedTesting.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarFooter.add(lblModelbasedTesting);
	}

	private void executeGraph() {
		this.extractCESs();
	}

	private void configureKeyboardEvents() {
		new mxKeyboardHandler(this.graphComponent) {

			@Override
			protected InputMap getInputMap(int condition) {
				InputMap map = null;

				if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
					map = (InputMap) UIManager.get("ScrollPane.ancestorInputMap");
				} else if (condition == JComponent.WHEN_FOCUSED) {
					map = new InputMap();

					map.put(KeyStroke.getKeyStroke("DELETE"), "delete");
					map.put(KeyStroke.getKeyStroke("F1"), "displayid");
					map.put(KeyStroke.getKeyStroke("F2"), "edit");
					map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), "selectall");
				}

				return map;
			}

			@Override
			protected ActionMap createActionMap() {
				ActionMap map = (ActionMap) UIManager.get("ScrollPane.actionMap");

				map.put("delete", CustomGraphActions.getDeleteAction());
				map.put("displayid", CustomGraphActions.getDisplayIdAction());
				map.put("edit", CustomGraphActions.getEditAction());
				map.put("selectall", CustomGraphActions.getSelectAllAction());

				return map;
			}

		};
	}

	private void setCustomStylesheet() {
		mxStylesheet stylesheet = new mxStylesheet();

		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put(mxConstants.STYLE_ROUNDED, true);
		edge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// edge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		edge.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		edge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		edge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		edge.put(mxConstants.STYLE_FONTSIZE, "10");
		edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		Map<String, Object> markedEdge = new HashMap<>(edge);
		markedEdge.put(mxConstants.STYLE_ROUNDED, true);
		markedEdge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// markedEdge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		markedEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		markedEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		markedEdge.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");
		markedEdge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		markedEdge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		markedEdge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		markedEdge.put(mxConstants.STYLE_FONTSIZE, "10");
		markedEdge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		markedEdge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		Map<String, Object> generatedEdge = new HashMap<>(edge);
		generatedEdge.put(mxConstants.STYLE_ROUNDED, true);
		generatedEdge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// generatedEdge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		generatedEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		generatedEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		generatedEdge.put(mxConstants.STYLE_STROKECOLOR, "#339900");
		generatedEdge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		generatedEdge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		generatedEdge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		generatedEdge.put(mxConstants.STYLE_FONTSIZE, "10");
		generatedEdge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		generatedEdge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		Map<String, Object> startVertex = new HashMap<String, Object>();
		startVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		startVertex.put(mxConstants.STYLE_FILLCOLOR, "#88FFAA");
		startVertex.put(mxConstants.STYLE_STROKECOLOR, "#009933");
		startVertex.put(mxConstants.STYLE_STROKEWIDTH, "3");
		startVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		startVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		startVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		startVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);

		Map<String, Object> endVertex = new HashMap<String, Object>();
		endVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		endVertex.put(mxConstants.STYLE_FILLCOLOR, "#FF9090");
		endVertex.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");
		endVertex.put(mxConstants.STYLE_STROKEWIDTH, "3");
		endVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		endVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		endVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		endVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);

		Map<String, Object> normalVertex = new HashMap<String, Object>();
		normalVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		normalVertex.put(mxConstants.STYLE_FILLCOLOR, "#9DE7FF");
		normalVertex.put(mxConstants.STYLE_STROKECOLOR, "#006688");
		normalVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		normalVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		normalVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		normalVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		normalVertex.put(mxConstants.STYLE_ROUNDED, true);
		normalVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		Map<String, Object> eventVertex = new HashMap<String, Object>();
		eventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		eventVertex.put(mxConstants.STYLE_FILLCOLOR, "#FFFCDD");
		eventVertex.put(mxConstants.STYLE_STROKECOLOR, "#9A9E40");
		eventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		eventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		eventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		eventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		eventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		eventVertex.put(mxConstants.STYLE_ROUNDED, true);
		eventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		
		Map<String, Object> parameterVertex = new HashMap<String, Object>();
		parameterVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		parameterVertex.put(mxConstants.STYLE_FILLCOLOR, "#E6E3C4");
		parameterVertex.put(mxConstants.STYLE_STROKECOLOR, "#666344");
		parameterVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		parameterVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		parameterVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		parameterVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		parameterVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		parameterVertex.put(mxConstants.STYLE_ROUNDED, true);
		parameterVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		Map<String, Object> generatedEventVertex = new HashMap<String, Object>();
		generatedEventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		generatedEventVertex.put(mxConstants.STYLE_FILLCOLOR, "#CFBBEE");
		generatedEventVertex.put(mxConstants.STYLE_STROKECOLOR, "#996699");
		generatedEventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		generatedEventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		generatedEventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		generatedEventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		generatedEventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		generatedEventVertex.put(mxConstants.STYLE_ROUNDED, true);
		generatedEventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		stylesheet.setDefaultEdgeStyle(edge);

		stylesheet.putCellStyle(MARKED_EDGE, markedEdge);

		stylesheet.putCellStyle(GENERATED_EDGE, generatedEdge);

		stylesheet.putCellStyle(START_VERTEX, startVertex);

		stylesheet.putCellStyle(END_VERTEX, endVertex);

		stylesheet.putCellStyle(NORMAL_VERTEX, normalVertex);

		stylesheet.putCellStyle(EVENT_VERTEX, eventVertex);
		
		stylesheet.putCellStyle(PARAMETER_VERTEX, parameterVertex);

		stylesheet.putCellStyle(GENERATED_EVENT_VERTEX, generatedEventVertex);

		this.graph.setStylesheet(stylesheet);

		this.graph.setAllowDanglingEdges(false);
		this.graph.setMultigraph(false);
		this.graph.setAllowLoops(true);
	}

}
