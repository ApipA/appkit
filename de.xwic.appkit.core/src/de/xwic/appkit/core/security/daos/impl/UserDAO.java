/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.security.daos.impl.UserDAO
 * Created on 02.08.2005
 *
 */
package de.xwic.appkit.core.security.daos.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.ScopeActionKey;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.security.impl.User;

/**
 * @author Florian Lippisch
 */
public class UserDAO extends AbstractDAO<IUser, User> implements IUserDAO {

	/**
	 *
	 */
	public UserDAO() {
		super(IUser.class, User.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.IUser#buildAllRights()
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<ScopeActionKey> buildAllRights(IUser user) {
		// read effective rights
		Set<ScopeActionKey> allRights = new HashSet<ScopeActionKey>();
		IRightDAO rightDAO = (IRightDAO)DAOSystem.getDAO(IRightDAO.class);
		for (Iterator<?> it = user.getRoles().iterator(); it.hasNext(); ) {
			IRole role = (IRole)it.next();
			EntityList list = rightDAO.getRightsByRole(role);
			for (Iterator<Object> rIt = list.iterator(); rIt.hasNext(); ) {
				IRight right = (IRight)rIt.next();
				allRights.add(new ScopeActionKey(right.getScope(), right.getAction()));
			}
		}
		return allRights;

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.security.daos.IUserDAO#getUserByLogonName(java.lang.String)
	 */
	@Override
	public IUser getUserByLogonName(String logonName) {
		
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("logonName", logonName);
		
		EntityList<IUser> list = getEntities(new Limit(0,1), pq);
		
		if (!list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}
	
}
