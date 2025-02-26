/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.impl.Scope
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IActionSet;
import de.xwic.appkit.core.security.IScope;

/**
 * @author Florian Lippisch
 */
public class Scope extends Entity implements IScope {

	private String name = null;
	private String description = null;
	private Set<IEntity> actions = null;
	private Set<IEntity> actionSets = null;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getActions()
	 */
	public Set<IEntity> getActions() {
		if (actions == null) {
			actions = new HashSet<IEntity>();
		}
		return actions;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setActions(java.util.Set)
	 */
	public void setActions(Set<IEntity> actions) {
		this.actions = actions;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getActionSets()
	 */
	public Set<IEntity> getActionSets() {
		if (actionSets == null) {
			actionSets = new HashSet<IEntity>();
		}
		return actionSets;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#setActionSets(java.util.Set)
	 */
	public void setActionSets(Set<IEntity> actionSets) {
		this.actionSets = actionSets;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#getAllActions()
	 */
	public Set<IEntity> getAllActions() {
		HashSet<IEntity> allActions = new HashSet<IEntity>();
		allActions.addAll(getActions());
		for (Iterator<?> it = getActionSets().iterator(); it.hasNext(); ) {
			IActionSet as = (IActionSet)it.next();
			allActions.addAll(as.getActions());
		}
		return allActions;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IScope#isActionAllowed(de.xwic.appkit.core.security.IAction)
	 */
	public boolean isActionAllowed(IAction action) {
		return getAllActions().contains(action);
	}

}
