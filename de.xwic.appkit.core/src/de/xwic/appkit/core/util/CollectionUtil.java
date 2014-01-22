/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * @author Alexandru Bledea
 * @since Jul 30, 2013
 */
public class CollectionUtil {

	/**
	 * Used to transform an array of <b>objects</b> into a <b>collection</b><br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <C extends Collection<V>, V, O> C createCollection(O[] objects, ILazyEval<O, V> evaluator, C collection) {
		if (objects == null) {
			return collection;
		}
		return createCollection(Arrays.asList(objects), evaluator, collection);
	}

	/**
	 * Used to transform a collection of <b>objects</b> into another <b>collection</b><br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <C extends Collection<V>, V, O> C createCollection(final Collection<? extends O> objects, ILazyEval<O, V> evaluator, final C collection) {
		if (objects != null) {

			for (final O obj : objects) {
				if (obj == null) {
					continue;
				}

				final V value = evaluator.evaluate(obj);
				if (value != null) {
					collection.add(value);
				}
			}

		}
		return collection;
	}

	/**
	 * Used to filter a collection of <b>objects</b><br>
	 * Only the values that return true are kept<br>
	 * <b>It is your responsibility to make sure that the element is not null, consider {@link NotNullFilter}</b><br>
	 * @param objects the collection from which we create the collection
	 * @param filter the filter
	 * @return the filtered collection passed as the parameter. <b>the original collection is also altered.</b>
	 */
	public static <C extends Collection<O>, O> C filter(C objects, IFilter<O> filter) {
		if (objects != null) {
			Iterator<O> iterator = objects.iterator();
			while (iterator.hasNext()) {
				if (!filter.keep(iterator.next())) {
					iterator.remove();
				}
			}
		}
		return objects;
	}

	/**
	 * used to break a large collection into smaller collection
	 * @param collection
	 * @param maxElements
	 * @param clazz
	 * @return
	 */
	public static <O, C extends Object & Collection> List<Collection<O>> breakCollection(Collection<O> collection, int maxElements, Class<C> clazz) {
		final List<Collection<O>> result = new ArrayList<Collection<O>>();
		if (collection == null){
			return result;
		}
		C step = instantiate(clazz);
		final Iterator<O> iterator = collection.iterator();
		while (iterator.hasNext()) {
			if (step.size() == maxElements) {
				result.add(step);
				step = instantiate(clazz);
			}
			step.add(iterator.next());
		}
		if (!step.isEmpty()) {
			result.add(step);
		}
		return result;
	}

	/**
	 * @param collection
	 * @param evaluator
	 * @param emptyMessage
	 * @return
	 */
	public static <O> String join(Collection<O> collection, ILazyStringEval<O> evaluator, String separator, String emptyMessage) {
		final List<String> strings = new ArrayList();
		createCollection(collection, evaluator, strings);

		final Iterator<String> iterator = strings.iterator();
		if (iterator.hasNext()) {
			return StringUtils.join(iterator, separator);
		}
		return emptyMessage;
	}

	/**
	 * @param collection
	 * @param evaluator
	 * @return an empty string if no values
	 */
	public static <O> String join(Collection<O> collection, ILazyStringEval<O> evaluator, String separator) {
		return join(collection, evaluator, separator, "");
	}

	/**
	 * @param element
	 * @param collection
	 */
	public static <O> void addIfNotNull(O element, Collection<O> collection) {
		if (element != null) {
			collection.add(element);
		}
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static <C extends Object & Collection> C instantiate(final Class<C> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot instantiate " + clazz.getName());
		}
	}
}
