/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.ISecurityManager
 * Created on 09.08.2005
 *
 */
package de.xwic.appkit.core.dao;

import de.xwic.appkit.core.security.IUser;


/**
 * Manages Security for the DAO System.
 * @author Florian Lippisch
 */
public interface ISecurityManager {

	/** no access */
	public final static int NONE = 0;
	/** read access */
	public final static int READ = 1;
	/** read and write access */
	public final static int READ_WRITE = 2;
	
	/** action key name for NONE access */
	public final static String ACTION_NONE = "NONE";
	/** action key name for READ access */
	public final static String ACTION_READ = "READ";
	/** action key name for WRITE access */
	public final static String ACTION_WRITE = "WRITE";
	
	/**
	 * Returns the current User.
	 * @return
	 */
	public IUser getCurrentUser();
	
	/**
	 * Returns true if the current user has the right to do the
	 * specified action on the specified scope.
	 *  
	 * @param scope
	 * @param right
	 * @return
	 */
	public boolean hasRight(String scope, String action);

	/**
	 * Returns the access to the specified subscope.
	 * @param scope
	 * @param subscope
	 * @param action
	 * @return
	 */
	public int getAccess(String scope, String subscope);
	
	/**
	 * Logon with the specified username/password.
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean logon(String username, String password);
	
	/**
	 * Clear the users security settings.
	 */
	public void logout();

	/**
	 * Trys to detect the current user from a cookie. If no user was detected,
	 * null is returned.
	 * 
	 * Works only on a web server based environment.
	 * @return
	 */
	public IUser detectUser();
	
	/**
	 * Sends a cookie to the client to remember the logon info.
	 */
	public void rememberActiveUser();
	
	/**
	 * Drops the credential from the cache, which forces the server to reload the
	 * credentials for the specified user.
	 * @param user
	 */
	public void dropCredentialFromCache(IUser user);
	/**
	 * Drop the user from the cache.
	 * @param user
	 */
	public void dropUserFromCache(IUser user);

	
	/**
	 * Find a user by its logonname.
	 * @param logonName
	 * @return
	 */
	public IUser findUser(String logonName);
}