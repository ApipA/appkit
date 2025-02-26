/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Aug 22, 2014
 * @deprecated - use de.xwic.appkit.core.util.Functions instead
 */
@Deprecated()
public final class Evaluators {

	/**
	 *
	 */
	private Evaluators() {
	}

	@SuppressWarnings ("rawtypes")
	private static Function IDENTITY_EVALUATOR = new Function() {

		@Override
		public Object evaluate(final Object obj) {
			return obj;
		}
	};

	/**
	 * @return
	 */
	@SuppressWarnings ("unchecked")
	public static <E> Function<E, E> identity() {
		return IDENTITY_EVALUATOR;
	}

}
