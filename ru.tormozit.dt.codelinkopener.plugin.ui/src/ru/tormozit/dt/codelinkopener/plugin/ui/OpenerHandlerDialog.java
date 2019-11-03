package ru.tormozit.dt.codelinkopener.plugin.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.jface.text.TextViewer;

import com.google.common.collect.Lists;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.ui.util.OpenHelper;
import com._1c.g5.v8.dt.bm.index.emf.IBmEmfIndexManager;
import com._1c.g5.v8.dt.bm.index.emf.IBmEmfIndexProvider;
import com._1c.g5.v8.dt.bsl.ui.menu.BslHandlerUtil;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.form.ui.editor.FormEditor;

public class OpenerHandlerDialog extends TitleAreaDialog {
	static private Map<String, String> typeMap1C = new HashMap<String, String>();
	private IBmEmfIndexManager bmEmfIndexManager;
	private IResourceLookup resourceLookup;
	private IV8ProjectManager projectManager;
	
	public OpenerHandlerDialog(Shell parentShell, IBmEmfIndexManager bmEmfIndexManager, IResourceLookup resourceLookup, IV8ProjectManager projectManager) {
		super(parentShell);
		this.bmEmfIndexManager = bmEmfIndexManager;
		this.resourceLookup = resourceLookup;
		this.projectManager = projectManager;
		typeMap1C.put("HTTPСервис", "HTTPService");
		typeMap1C.put("WebСервис", "WebService");
		typeMap1C.put("ВнешнийИсточникДанных", "ExternalDataSource");
		typeMap1C.put("Документ", "Document");
		typeMap1C.put("Задача", "Task");
		typeMap1C.put("Команда", "Command");
		typeMap1C.put("Конфигурация", "Configuration"); //
		typeMap1C.put("МодульУправляемогоПриложения", "ManagedApplicationModule");
		typeMap1C.put("МодульОбычногоПриложения", "OrdinaryApplicationModule");
		typeMap1C.put("МодульВнешнегоСоединения", "ExternalConnectionModule");
		typeMap1C.put("МодульОбъекта", "ObjectModule");
		typeMap1C.put("МодульНабораЗаписей", "RecordsetModule");
		typeMap1C.put("МодульМенеджера", "ManagerModule");
		typeMap1C.put("МодульКоманды", "CommandModule");
		typeMap1C.put("МодульСеанса", "SessionModule");
		typeMap1C.put("Модуль", "Module");
		typeMap1C.put("ОбщаяКоманда", "CommonCommand");
		typeMap1C.put("ОбщийМодуль", "CommonModule");
		typeMap1C.put("ОбщаяФорма", "CommonForm");
		typeMap1C.put("Обработка", "DataProcessor");
		typeMap1C.put("Отчет", "Report");
		typeMap1C.put("Перечисление", "Enum");
		typeMap1C.put("ПланВидовРасчета", "ChartOfCalculationTypes");
		typeMap1C.put("ПланВидовХарактеристик", "ChartOfCharacteristicTypes");
		typeMap1C.put("ПланОбмена", "ExchangePlan");
		typeMap1C.put("РегистрБухгалтерии", "AccountingRegister");
		typeMap1C.put("РегистрНакопления", "AccumulationRegister");
		typeMap1C.put("РегистрРасчета", "CalculationRegister"); 
		typeMap1C.put("РегистрСведений", "InformationRegister");
		typeMap1C.put("Форма", "Form");
		typeMap1C.put("Справочник", "Catalog");
	}
	
	private static EClass getMdLiteral(String mdClassName, Boolean isExternalDataSource, Boolean isForm, Boolean isCommand) {
		EClass mdLiteral = MdClassPackage.Literals.CONFIGURATION;
		if (mdClassName.equals("Подсистема")) 
			mdLiteral = MdClassPackage.Literals.SUBSYSTEM;
		else if (mdClassName.equals("Конфигурация")) 
			mdLiteral = MdClassPackage.Literals.CONFIGURATION;
		else if (mdClassName.equals("HTTPСервис"))
			mdLiteral = MdClassPackage.Literals.HTTP_SERVICE;
		else if (mdClassName.equals("WebСервис"))
			mdLiteral = MdClassPackage.Literals.WEB_SERVICE;
		else if (mdClassName.equals("ВнешнийИсточникДанных"))
			mdLiteral = MdClassPackage.Literals.EXTERNAL_DATA_SOURCE;
		else if (mdClassName.equals("Документ"))
			if (isForm)
				mdLiteral = MdClassPackage.Literals.DOCUMENT_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.DOCUMENT_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.DOCUMENT;
		else if (mdClassName.equals("Задача"))
			if (isForm)
				mdLiteral = MdClassPackage.Literals.TASK_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.TASK_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.TASK;
		else if (mdClassName.equals("ОбщаяКоманда")) 
			mdLiteral = MdClassPackage.Literals.COMMON_COMMAND;
		else if (mdClassName.equals("ОбщийМодуль")) 
			mdLiteral = MdClassPackage.Literals.COMMON_MODULE;
		else if (mdClassName.equals("ОбщаяФорма")) 
			mdLiteral = MdClassPackage.Literals.COMMON_FORM;
		else if (mdClassName.equals("Обработка")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.DATA_PROCESSOR_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.DATA_PROCESSOR_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.DATA_PROCESSOR;
		else if (mdClassName.equals("Отчет")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.REPORT_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.REPORT_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.REPORT;
		else if (mdClassName.equals("Перечисление")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.ENUM_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.ENUM_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.ENUM;
		else if (mdClassName.equals("ПланВидовХарактеристик")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES;
		else if (mdClassName.equals("ПланВидовРасчета")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES;
		else if (mdClassName.equals("ПланОбмена"))
			if (isForm)
				mdLiteral = MdClassPackage.Literals.EXCHANGE_PLAN_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.EXCHANGE_PLAN_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.EXCHANGE_PLAN;
		else if (mdClassName.equals("РегистрБухгалтерии")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.ACCOUNTING_REGISTER_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.ACCOUNTING_REGISTER_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.ACCOUNTING_REGISTER;
		else if (mdClassName.equals("РегистрНакопления")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.ACCUMULATION_REGISTER_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.ACCUMULATION_REGISTER_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.ACCUMULATION_REGISTER;
		else if (mdClassName.equals("РегистрРасчета")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.CALCULATION_REGISTER_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.CALCULATION_REGISTER_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.CALCULATION_REGISTER;
		else if (mdClassName.equals("РегистрСведений"))
			if (isForm)
				mdLiteral = MdClassPackage.Literals.INFORMATION_REGISTER_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.INFORMATION_REGISTER_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.INFORMATION_REGISTER;
		else if (mdClassName.equals("Справочник")) 
			if (isForm)
				mdLiteral = MdClassPackage.Literals.CATALOG_FORM;
			else if (isCommand)
				mdLiteral = MdClassPackage.Literals.CATALOG_COMMAND;
			else
				mdLiteral = MdClassPackage.Literals.CATALOG;
		return mdLiteral;
	}

	@Override
	protected void okPressed() {
		super.okPressed(); 
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle(Messages.DataProcessingHandlerDialog_dialog_title);
		setMessage(Messages.DataProcessingHandlerDialog_dialog_message);
		getShell().setText(Messages.DataProcessingHandlerDialog_dialog_text);
		Composite control = (Composite) super.createDialogArea(parent);

		Clipboard clipboard = new Clipboard(null);
		TextTransfer textTransfer = TextTransfer.getInstance();
		String textData = (String) clipboard.getContents(textTransfer);
		clipboard.dispose();
		if (textData == null || textData.isEmpty())
			return control;
		
		control.setLayout(new GridLayout());
		Group group = new Group(control, SWT.SHADOW_ETCHED_IN);
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TextViewer viewer = new TextViewer(group, SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		StyledText styledText = viewer.getTextWidget();
		styledText.setText(textData);

		String letter = "a-zа-яёА-ЯЁ0-9_";
		Pattern regexp = Pattern.compile("(\\{([" + letter + "]+ )?((?:[" + letter
				+ "]+\\.)*(?:Форма|Модуль|МодульУправляемогоПриложения|МодульОбычногоПриложения|МодульСеанса|МодульВнешнегоСоединения|МодульКоманды|МодульМенеджера|МодульОбъекта|МодульНабораЗаписей))\\((\\d+)(?:,(\\d+))?\\)\\})");
		Matcher m = regexp.matcher(textData);
		while (m.find()) {
			List<String> list = Lists.newArrayList();
			list.add(m.group(2));
			list.add(m.group(3));
			list.add(m.group(4));
			list.add(m.group(5));
			StyleRange styleRange = new StyleRange();
			styleRange.start = m.start();
			styleRange.length = m.end() - m.start();
			styleRange.underline = true;
			styleRange.data = list;
			styleRange.underlineStyle = SWT.UNDERLINE_LINK;
			styledText.setStyleRange(styleRange);
		}

		styledText.addListener(SWT.MouseDown, event -> {
			// In this snippet links are activated on mouse down when the control key is
			// held down
			if ((event.stateMask & SWT.MOD1) != 0) {
				int offset = styledText.getOffsetAtLocation(new Point(event.x, event.y));
				if (offset != -1) {
					StyleRange style1 = null;
					try {
						style1 = styledText.getStyleRangeAtOffset(offset);
					} catch (IllegalArgumentException e) {
						// no character under event.x, event.y
					}
					if (style1 != null && style1.underline && style1.underlineStyle == SWT.UNDERLINE_LINK) {
						List<String> list = (List<String>) style1.data;
						openLink(list);
					}
				}
			}
		});
		return control;
	}
	public void openLink(List<String> list) {
		String extensionName = list.get(0);
		String rusModuleName = list.get(1);
		String[] fragments = rusModuleName.split("\\.");
		String engModuleName = "";
		String mdClassName = "";
		Boolean isForm = false;
		Boolean isCommand = false;
		Boolean isExternalDataSource = false;
		if (fragments.length == 1) {
			mdClassName = "Конфигурация";
			engModuleName = typeMap1C.get(mdClassName);
		} else {
			for (int counter = 0; counter < fragments.length / 2; counter++) {
				String rusName = fragments[counter * 2];
				if (typeMap1C.get(rusName) == null) {
					// MessageBox.Show(this, "Unknown 1C object type " + rusName,
					// Application.ProductName, MessageBoxButtons.OK, MessageBoxIcon.Error);
					return;
				}
				if (rusName.equals("Форма"))
					isForm = true;
				else if (rusName.equals("Команда"))
					isCommand = true;
				else if (rusName.equals("ВнешнийИсточникДанных"))
					isExternalDataSource = true;
				else {
					if (rusName.equals("ОбщаяФорма"))
						isForm = true;						
					mdClassName = rusName;
				}
				String engName = typeMap1C.get(rusName);
				if (!engModuleName.isEmpty())
					engModuleName += ".";
				engModuleName += engName;
				engModuleName += ".";
				engModuleName += fragments[counter * 2 + 1];
			}
		}
		EClass mdLiteral = getMdLiteral(mdClassName, isExternalDataSource, isForm, isCommand);
		MdObject mdObject = (MdObject) getConfigurationObject(mdLiteral, engModuleName, bmEmfIndexManager, projectManager);
		if (mdObject == null)
			return;
		String modulePropertyName = typeMap1C.get(fragments[fragments.length - 1]);
		modulePropertyName = Character.toString(modulePropertyName.charAt(0)).toLowerCase() + modulePropertyName.substring(1); // Convert first letter to lower case
		EStructuralFeature moduleProperty = mdObject.eClass().getEStructuralFeature(modulePropertyName);
		close();
		int lineNumber = Integer.parseInt(list.get(2)) - 1;
		OpenHelper openHelper = new OpenHelper();
		IEditorPart editorPart = openHelper.openEditor(mdObject, moduleProperty);
		if (isForm) {
			FormEditor formEditor = (FormEditor) editorPart;
			formEditor.setActivePage("editors.form.pages.module");
		}			
		XtextEditor target = BslHandlerUtil.extractXtextEditor(editorPart);
		IXtextDocument document = target.getDocument();
		int offset;
		try {
			offset = document.getLineOffset(lineNumber);
		} catch (BadLocationException e) {
			return;
		}
		target.selectAndReveal(offset, 0);
	}
	
	private static MdObject getConfigurationObject(EClass mdLiteral, String engModuleName, IBmEmfIndexManager bmEmfIndexManager, IV8ProjectManager projectManager) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage activePage = window.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();
		IProject project = null;
		if (activeEditor != null) {
			project = activeEditor.getEditorInput().getAdapter(IProject.class);
			if (project == null) {
				IResource resource = activeEditor.getEditorInput().getAdapter(IResource.class);
				if (resource != null) {
					project = resource.getProject();
				}
			}
		}
		else {
			project = activePage.getInput().getAdapter(IProject.class);
			if (project == null) {
				IResource resource = activePage.getInput().getAdapter(IResource.class);
				if (resource != null) {
					project = resource.getProject();
				}
			}
		}
		IV8Project v8Project; 
		if (project == null) {
			v8Project = projectManager.getProjects().iterator().next();
		}
		else {
			v8Project = projectManager.getProject(project); 
		}
		if (v8Project == null)
			return null;
		IBmEmfIndexProvider bmEmfIndexProvider = bmEmfIndexManager.getEmfIndexProvider(v8Project.getProject());
		QualifiedName qnObjectName = getConfigurationObjectQualifiedName(engModuleName);
		MdObject object = null;
		Iterable<IEObjectDescription> objectIndex = bmEmfIndexProvider.getEObjectIndexByType(mdLiteral, qnObjectName, true);
		Iterator<IEObjectDescription> objectItr = objectIndex.iterator();
		if (objectItr.hasNext())
			object = (MdObject) objectItr.next().getEObjectOrProxy();
		else // for debug
		{
//			Iterable<IEObjectDescription> objectIndex1 = bmEmfIndexProvider.getEObjectIndexByType(mdLiteral);
//			Iterator<IEObjectDescription> objectItr1 = objectIndex.iterator();
//			if (objectItr1.hasNext())
//				object = (MdObject) objectItr1.next().getEObjectOrProxy();
		}
		return object;
	}

	private static QualifiedName getConfigurationObjectQualifiedName(String engModuleName) {
		String[] objectArray = engModuleName.split("[.]");
		QualifiedName qnObjectName = null;
		for (String objectValue : objectArray) {
			if (qnObjectName == null)
				qnObjectName = QualifiedName.create(objectValue);
			else 
				qnObjectName = qnObjectName.append(QualifiedName.create(objectValue));
		}
		return qnObjectName;
	}
}
