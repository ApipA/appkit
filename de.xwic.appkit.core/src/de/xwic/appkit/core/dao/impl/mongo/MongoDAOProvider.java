/*
 * de.xwic.appkit.core.model.impl.AbstractHibernateDAOProvider
 * Created on 05.04.2005
 *
 */
package de.xwic.appkit.core.dao.impl.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import de.xwic.appkit.core.dao.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of a DAOProvider that is using hibernate.
 * 
 * @author Florian Lippisch
 */
public class MongoDAOProvider implements DAOProvider {

	/** reuse the current session */
	public final static int USE_CURRENT_SESSION = 0;
	/** create a new session for each request */
	public final static int USE_NEW_SESSION = 1;
	
	private int sessionMode = USE_CURRENT_SESSION;
	private Map<Class<? extends EntityQuery>, IEntityQueryResolver> queryResolvers = new HashMap<Class<? extends EntityQuery>, IEntityQueryResolver>();
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProvider#registerQuery(java.lang.Class, de.xwic.appkit.core.dao.IEntityQueryResolver)
	 */
	public void registerQuery(Class<? extends EntityQuery> queryClass, IEntityQueryResolver resolver) {
		queryResolvers.put(queryClass, resolver);
	}
	
	/**
	 * Returns the resolver specified.
	 * @param queryClass
	 * @return
	 */
	IEntityQueryResolver getResolver(Class<? extends EntityQuery> queryClass) throws DataAccessException {
		IEntityQueryResolver qr = queryResolvers.get(queryClass);
		if (qr == null) {
			throw new DataAccessException("No IEntityQueryResolver registerd for query '" + queryClass.getName() + "'");
		}
		return qr;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.DAOProvider#execute(de.xwic.appkit.core.dao.DAOCallback)
	 */
	public Object execute(DAOCallback operation) {
        MongoClient session =
                sessionMode == USE_CURRENT_SESSION ?
        		MongoUtil.currentSession() :
        		MongoUtil.openSession();
        		
        try {
        	Object o = operation.run(new MongoDAOProviderAPI(this, session));
        	return o;
        } finally {
        	// an exception occurred
        	// If we use our own session, we must close it by ourself.
        	if (sessionMode != USE_CURRENT_SESSION) {
        		session.close();
        	}
        }
	}

	/**
	 * @return Returns the sessionMode.
	 */
	public int getSessionMode() {
		return sessionMode;
	}

	/**
	 * @param sessionMode The sessionMode to set.
	 */
	public void setSessionMode(int sessionMode) {
		this.sessionMode = sessionMode;
	}


}
