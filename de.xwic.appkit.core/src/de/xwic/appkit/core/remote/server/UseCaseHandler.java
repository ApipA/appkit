/**
 *
 */
package de.xwic.appkit.core.remote.server;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.UseCaseSerializer;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class UseCaseHandler implements IRequestHandler {

	public final static String PARAM_USE_CASE = "uc";
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#getAction()
	 */
	@Override
	public String getAction() {
		return RemoteDataAccessServlet.ACTION_EXECUTE_USE_CASE;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.io.PrintWriter)
	 */
	@Override
	public void handle(final HttpServletRequest req, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
		
		String strUseCase = req.getParameter(PARAM_USE_CASE);
		
		if (strUseCase == null || strUseCase.trim().isEmpty()) {
			throw new IllegalArgumentException("The string is empty!");
		}
		
		UseCase uc = UseCaseSerializer.deseralize(strUseCase);
		
		Object result = uc.execute();
		String serialized = XmlBeanSerializer.serializeToXML("result", result);
		
		pwOut.write(serialized);
	}

}
