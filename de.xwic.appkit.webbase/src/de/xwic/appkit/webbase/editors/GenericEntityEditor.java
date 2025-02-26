/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editor.GenericEntityEditor
 * Created on May 14, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Tab;
import de.xwic.appkit.core.config.editor.ETab;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.webbase.editors.builders.Builder;
import de.xwic.appkit.webbase.editors.builders.BuilderRegistry;
import de.xwic.appkit.webbase.editors.builders.EContainerBuilder;

/**
 * This class defines the generic entity editor.
 * 
 * @author Aron Cotrau
 */
public class GenericEntityEditor extends ControlContainer {

	public static final String ID_EDITOR = GenericEntityEditor.class.getName();

	private GenericEditorInput input = null;
	private EditorConfiguration config = null;
	private EditorContext context = null;

	/**
	 * @param container
	 * @param name
	 */
	public GenericEntityEditor(IControlContainer container, String name) {
		super(container, name);
	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.wap.core.Part#createControls(de.jwic.base.IControlContainer)
	 */
	public void createControls(IControlContainer container) {
//		try {
//			input = (GenericEditorInput) getEditorInput();
//			config = input.getConfig();
//			List tabs = config.getTabs();
//			setTitle(input.getName());
//			context = new EditorContext(this);
//
//			TabStripControl mainTabs = new TabStripControl(container);
//			mainTabs.setLocation(TabStripControl.LOCATION_BOTTOM);
//			
//			for (Iterator it = tabs.iterator(); it.hasNext();) {
//				ETab eTab = (ETab) it.next();
//				Tab tab = mainTabs.addTab(eTab.getTitle());
//				
//				createPage(tab, eTab);
//			}
//		} catch (Exception e) {
//			JWicErrorDialog.openError("An error occured while initializing controls from the editor", e, getSite());
//		}
	}

	/**
	 * creates a page.
	 * 
	 * @param tab
	 * @param eTab
	 */
	private void createPage(Tab tab, ETab eTab) {
		Builder builder = BuilderRegistry.getBuilderByClass(EContainerBuilder.class);
		builder.buildComponents(eTab, tab, context);
	}

	/**
	 * @return the context
	 */
	public EditorContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(EditorContext context) {
		this.context = context;
	}
}
