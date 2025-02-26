/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import java.util.Date;

import de.jwic.base.IControlContainer;
import de.jwic.controls.DatePicker;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitDateInputControl extends AbstractToolkitHTMLElementControl<DatePicker> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String, java.lang.Object)
	 */
	@Override
	public DatePicker create(IControlContainer container, String name, Object optionalParam) {
		return new DatePicker(container, name);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(DatePicker control) {
		return control.getDate();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(DatePicker control, Object obj) {
		control.setDate((Date) obj);
	}
}
