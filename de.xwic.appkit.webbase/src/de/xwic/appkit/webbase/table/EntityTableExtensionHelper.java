/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.table;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.webbase.table.filter.IFilterControlCreator;

/**
 * Helper for EntityTable extensions.
 * @author lippisch
 */
public class EntityTableExtensionHelper {
	
	private final static Log log = LogFactory.getLog(EntityTableExtensionHelper.class);
	

	public final static String EP_ENTITY_TABLE_LABEL_PROVIDERS = "customLabelProviders";
	public final static String EP_ENTITY_TABLE_COLUMN_FILTER_CREATORS = "customColumnFilterCreators";
	public final static String EP_ATTR_ENTITY = "entity";
	public final static String EP_ATTR_FIELD = "field";
	
	/**
	 * Searches for ColumnLabelProviders registered for an entity type.  
	 * @param entityType
	 * @return
	 */
	public static IColumnLabelProvider getEntityColumnLabelProvider(String entityType) {
		
		IExtension lblProviderExt = null;
		
		List<IExtension> extensions = ExtensionRegistry.getInstance().getExtensions(EP_ENTITY_TABLE_LABEL_PROVIDERS);
		for (IExtension ex : extensions) {
			String entity = ex.getAttribute(EP_ATTR_ENTITY);
			if (entity != null && entity.equals(entityType)) {
				if (lblProviderExt == null || lblProviderExt.getPriority() < ex.getPriority()) {
					lblProviderExt = ex;
				}
			}
		}
		
		if (lblProviderExt != null) {
			try {
				return (IColumnLabelProvider) lblProviderExt.createExtensionObject();
			} catch (Exception e) {
				log.warn("Error instantiating extension " + lblProviderExt.getId() + " : " + e, e);
			}
		}
		
		return null;
	}
	
	/**
	 * Searches for IFilterControlCreators registered for an entity type.  
	 * @param entityType
	 * @return
	 */
	public static IFilterControlCreator getEntityColumnFilterControl(String entityType) {
		return getColumnFilterControl(entityType, EP_ATTR_ENTITY);
	}
	
	/**
	 * Searches for IFilterControlCreators registered for a field.
	 * @param entityType
	 * @return
	 */
	public static IFilterControlCreator getFieldColumnFilterControl(String fieldFullName) {
		return getColumnFilterControl(fieldFullName, EP_ATTR_FIELD);
	}
	
	/**
	 * @param attributeValue
	 * @param attributeName
	 * @return
	 */
	private static IFilterControlCreator getColumnFilterControl(String attributeValue, String attributeName) {
		IExtension columnFilterExt = null;
		
		List<IExtension> extensions = ExtensionRegistry.getInstance().getExtensions(EP_ENTITY_TABLE_COLUMN_FILTER_CREATORS);
		for (IExtension ex : extensions) {
			String entity = ex.getAttribute(attributeName);
			if (entity != null && entity.equals(attributeValue)) {
				if (columnFilterExt == null || columnFilterExt.getPriority() < ex.getPriority()) {
					columnFilterExt = ex;
				}
			}
		}
		
		if (columnFilterExt != null) {
			try {
				return (IFilterControlCreator) columnFilterExt.createExtensionObject();
			} catch (Exception e) {
				log.warn("Error instantiating extension " + columnFilterExt.getId() + " : " + e, e);
			}
		}
		
		return null;
	}
}