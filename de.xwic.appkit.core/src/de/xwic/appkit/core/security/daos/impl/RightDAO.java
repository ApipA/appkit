/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.RightDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.impl.Right;
import de.xwic.appkit.core.security.queries.RightQuery;

/**
 * @author Florian Lippisch
 */
public class RightDAO extends AbstractDAO<IRight, Right> implements IRightDAO {

	/**
	 *
	 */
	public RightDAO() {
		super(IRight.class, Right.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.daos.IRightDAO#getRightsByRole(de.xwic.appkit.core.security.IRole)
	 */
	@Override
	public EntityList getRightsByRole(IRole role) {

		return getEntities(null, new RightQuery(role));

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.daos.IRightDAO#createRight(de.xwic.appkit.core.security.IRole, de.xwic.appkit.core.security.IScope, de.xwic.appkit.core.security.IAction)
	 */
	@Override
	public IRight createRight(IRole role, IScope scope, IAction action) {
		IRight right = (IRight)createEntity();
		right.setRole(role);
		right.setScope(scope);
		right.setAction(action);
		update(right);
		return right;
	}

}
