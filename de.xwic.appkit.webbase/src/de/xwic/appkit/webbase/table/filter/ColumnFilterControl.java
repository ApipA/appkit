/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table.filter;

import de.jwic.base.ControlContainer;
import de.jwic.base.Event;
import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;
import de.jwic.controls.Button;
import de.jwic.controls.LabelControl;
import de.jwic.ecolib.controls.StackedContainer;
import de.jwic.ecolib.tableviewer.TableColumn;
import de.jwic.ecolib.tableviewer.TableViewer;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.EntityTableExtensionHelper;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.table.columns.ColumnSelectorDialog;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * This control is placed inside of a balloon that is opened when a column is clicked.
 * @author lippisch
 */
@JavaScriptSupport
public class ColumnFilterControl extends ControlContainer implements IFilterControlListener {

	private int positionIdx = 0;
	private Column column = null;
	private final EntityTableModel model;
	private TableViewer tblViewer;
	
	private StackedContainer filterStack;
	private DefaultFilter defFilter = null;
	private PicklistFilter plFilter = null;
	private DateFilter dateFilter = null;
	private LabelControl lblNoFilter = null;
	private AbstractFilterControl currentFilter = null;
	private BooleanFilter bolFilter = null;
	private AbstractFilterControl customFilter = null;
	private IFilterControlCreator customFilterCreator = null;
	private ColumnAction btFilterClear;
	private ColumnAction btSortUp;
	private ColumnAction btSortDown;
	
	/**
	 * @param container
	 * @param name
	 */
	public ColumnFilterControl(IControlContainer container, String name, EntityTableModel model) {
		super(container, name);
		this.model = model;
		
		createButtons();
		createFilters();
		setVisible(false); // hidden by default.
		
	}

	/**
	 * 
	 */
	private void createFilters() {
		
		filterStack = new StackedContainer(this, "filters");
		lblNoFilter = new LabelControl(filterStack, "noFilter");
		lblNoFilter.setText("No filter options available");
		
		defFilter = new DefaultFilter(filterStack, "default");
		defFilter.addListener(this);
		plFilter = new PicklistFilter(filterStack, "plFilter");
		plFilter.addListener(this);
		dateFilter = new DateFilter(filterStack, "dateFilter");
		dateFilter.addListener(this);
		bolFilter = new BooleanFilter(filterStack, "bolFilter");
		bolFilter.addListener(this);
	}

	/**
	 * Returns the calculated height.
	 * @return
	 */
	public int getHeight() {
		
		int height = 40; // base height
		if (btSortDown.isVisible()) height += 26;
		if (btSortUp.isVisible()) height += 26;
		if (btFilterClear.isVisible()) height += 26;
		
		height += getFilterHeight();
		
		return height;
		
	}

	/**
	 * Returns the height of the filter.
	 * @return
	 */
	public int getFilterHeight() {
		if (currentFilter == null) {
			return 25;
		} else {
			return currentFilter.getPreferredHeight();
		}
	}
	
	/**
	 * 
	 */
	private void createButtons() {

		
		Button btColSetup = new Button(this, "btColSetup");
		btColSetup.setTitle("");
		btColSetup.setIconEnabled(ImageLibrary.ICON_TABLE);
		btColSetup.setTooltip("Column Configuration");
		btColSetup.setWidth(32);
		btColSetup.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				actionColumnConfiguration();
			}
		});
		
		Button btOk = new Button(this, "btOk");
		btOk.setTitle("Ok");
		btOk.setWidth(80);
		btOk.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				actionOk();
			}
		});
		
		Button btCancel = new Button(this, "btCancel");
		btCancel.setTitle("Close");
		btCancel.setWidth(80);
		btCancel.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				actionCancel();
			}
		});
		
		btSortUp = new ColumnAction(this, "btSortUp");
		btSortUp.setTitle("Sort A to Z");
		btSortUp.setImage(ImageLibrary.ICON_SORT_AZ);
		btSortUp.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				actionSortUp();
			}
		});

		btSortDown = new ColumnAction(this, "btSortDown");
		btSortDown.setTitle("Sort Z to A");
		btSortDown.setImage(ImageLibrary.ICON_SORT_ZA);
		btSortDown.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				actionSortDown();
			}
		});

		btFilterClear = new ColumnAction(this, "btFilterClear");
		btFilterClear.setImage(ImageLibrary.ICON_TBL_FILTER_CLEAR);
		btFilterClear.setTitle("Remove active filter");
		btFilterClear.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				actionClearFilter();
			}
		});

		
	}

	/**
	 * Open Column Configuration.
	 */
	protected void actionColumnConfiguration() {
		
		ColumnSelectorDialog dialog = new ColumnSelectorDialog(ExtendedApplication.getInstance(this).getSite(), model);
		dialog.show();
		hide();
	}

	/**
	 * 
	 */
	protected void actionClearFilter() {
		model.updateFilter(column, null);
		hide();
	}

	protected void hide() {
		setVisible(false);
	}
	
	/**
	 * 
	 */
	protected void actionSortDown() {
		model.sortColumn(column, Column.Sort.DOWN);
		hide();
	}

	/**
	 * 
	 */
	protected void actionSortUp() {
		model.sortColumn(column, Column.Sort.UP);
		hide();
	}

	/**
	 * 
	 */
	protected void actionCancel() {
		
		hide();
		
	}

	/**
	 * 
	 */
	protected void actionOk() {
		
		if (currentFilter != null) {
			model.updateFilter(column, currentFilter.getQueryElement());
		}
		hide();
		
	}

	/**
	 * Returns the title of the column.
	 * @return
	 */
	public String getColumnTitle() {
		return column != null ? model.getBundle().getString(column.getTitleId()) : "No Column";
	}
	/**
	 * @return the column
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(Column column) {
		this.column = column;
	}

	/**
	 * @param tableColumn
	 */
	public void open(TableColumn tableColumn) {
		
		btSortUp.setVisible(true);
		btSortDown.setVisible(true);

		positionIdx = tableColumn.getIndex();
		setColumn((Column) tableColumn.getUserObject());
		
		// choose the right filter
		currentFilter = null;
		//if (column.getPropertyIds() != null && column.getPropertyIds().length > 0) {
			Property finalProperty = column.getListColumn().getFinalProperty();
			
			if (finalProperty == null) {
				return;
			}
			
			IFilterControlCreator fcc = null;
			
			// first look for a custom filter control for this specific field
			String fieldName = finalProperty.getEntityDescriptor().getClassname() + "." + finalProperty.getName(); 
			fcc = EntityTableExtensionHelper.getFieldColumnFilterControl(fieldName);
			
			// if not found and the property is an entity, look for a custom filter control for the entity type
			if (fcc == null && finalProperty.isEntity()) {
				fcc = EntityTableExtensionHelper.getEntityColumnFilterControl(finalProperty.getEntityType());
			}

			if(fcc != null){
				// if we found a custom filter control, use it
				
				if (customFilterCreator == null || !fcc.getClass().getName().equals(customFilterCreator.getClass().getName())) {
					// if different than the last custom filter control used, create an instance and use it as the customFilter
					customFilterCreator = fcc;
					filterStack.removeControl("customFilter");

					customFilter = fcc.createFilterControl(filterStack, "customFilter");
					customFilter.addListener(this);
				}

				currentFilter = customFilter;
				
				btSortUp.setTitle("Sort First to Last");
				btSortDown.setTitle("Sort Last to First");
			} else {
				if (finalProperty.isEntity()) {
					if (finalProperty.getPicklistId() != null) {
						currentFilter = plFilter;
					} else {
						currentFilter = defFilter;
					}
					btSortUp.setTitle("Sort First to Last");
					btSortDown.setTitle("Sort Last to First");
				} else if ("java.util.Date".equals(finalProperty.getEntityType())) {
					currentFilter = dateFilter;
					btSortUp.setTitle("Sort Oldest to Newest");
					btSortDown.setTitle("Sort Newest to Oldest");
				} else if ("java.lang.Boolean".equals(finalProperty.getEntityType()) || "boolean".equals(finalProperty.getEntityType())) {
					currentFilter = bolFilter;
					btSortUp.setTitle("Sort False to True ");
					btSortDown.setTitle("Sort True to False");
				} else if (finalProperty.isCollection()) {
					btSortUp.setVisible(false);
					btSortDown.setVisible(false);
					if (finalProperty.getPicklistId() != null) { // it's a picklist
						currentFilter = plFilter;
					}
				} else {
					currentFilter = defFilter;
					btSortUp.setTitle("Sort A-Z");
					btSortDown.setTitle("Sort Z-A");
				}
			}
		//}
		
		if (currentFilter != null) {
			currentFilter.initialize(column, column.getFilter());
			filterStack.setCurrentControlName(currentFilter.getName());
		} else {
			filterStack.setCurrentControlName(lblNoFilter.getName());
		}
		
		btFilterClear.setVisible(column.getFilter() != null);
		setVisible(true);
		requireRedraw();
		
	}

	/**
	 * @return the positionIdx
	 */
	public int getPositionIdx() {
		return positionIdx;
	}

	/**
	 * @param tblViewer
	 */
	public void setTableViewer(TableViewer tblViewer) {
		this.tblViewer = tblViewer;
	}
	
	/**
	 * Returns the id of the tableViewer.
	 * @return
	 */
	public String getTableViewerId() {
		return tblViewer.getControlID();
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.filter.IFilterControlListener#applyFilter(de.jwic.base.Event)
	 */
	@Override
	public void applyFilter(Event event) {
		actionOk();
	}
	
}
