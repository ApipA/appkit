/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;


/**
 * @author Alexandru Bledea
 * @since Jul 30, 2013
 */
public final class CollectionUtil {

	/**
	 *
	 */
	private CollectionUtil() {
	}

	/**
	 * @param collection
	 * @param resultClass
	 * @return
	 */
	public static boolean isOf(final Collection<?> collection, final Class<?> resultClass) {
		Validate.notNull(resultClass, "No result class provided.");
		Validate.notNull(collection, "No collection provided.");
		if (collection.isEmpty()) {
			return true;
		}
		for (final Object cal : collection) {
			if (cal != null && !resultClass.isInstance(cal)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(final Collection<?> c) {
		return c == null || c.isEmpty();
	}

	/**
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(final Map<?, ?> c) {
		return c == null || c.isEmpty();
	}

	/**
	 * if parameter is null, set won't contain anything
	 *
	 * @param set
	 * @return
	 */
	public static <E> Set<E> newSet(final Collection<E> collection) {
		final Set<E> set = new LinkedHashSet<E>();
		if (!CollectionUtil.isEmpty(collection)) {
			set.addAll(collection);
		}
		return set;
	}

	/**
	 * if parameter is null, list won't contain anything
	 *
	 * @param set
	 * @return
	 */
	public static <E> List<E> newList(final Collection<E> collection) {
		final List<E> set = new ArrayList<E>();
		if (!CollectionUtil.isEmpty(collection)) {
			set.addAll(collection);
		}
		return set;
	}

	/**
	 * for defensive copying
	 * @param set
	 * @return
	 */
	public static <E> Set<E> cloneToSet(final Collection<E> set) {
		if (set == null) {
			return null;
		}
		return new LinkedHashSet<E>(set);
	}

	/**
	 * for defensive copying
	 * @param list
	 * @return
	 */
	public static <E> List<E> cloneToList(final Collection<E> list) {
		if (list == null) {
			return null;
		}
		return new ArrayList<E>(list);
	}

	/**
	 * @param where collection to clear and add to
	 * @param fromWhere what to add
	 * @throws NullPointerException if <code>where</code> is empty
	 */
	public static <E> void clearAndAddAll(final Collection<? super E> where, final Collection<? extends E> fromWhere) throws NullPointerException {
		if (where == fromWhere) {
			return;
		}
		where.clear();
		if (!isEmpty(fromWhere)) {
			where.addAll(fromWhere);
		}
	}

	/**
	 * @param where map to clear and add to
	 * @param fromWhere what to add
	 * @throws NullPointerException if <code>where</code> is empty
	 */
	public static <K, V> void clearAndPutAll(final Map<? super K, ? super V> where,
			final Map<? extends K, ? extends V> fromWhere) throws NullPointerException {
		if (where == fromWhere) {
			return;
		}
		where.clear();
		if (!isEmpty(fromWhere)) {
			where.putAll(fromWhere);
		}
	}

	/**
	 * adds all not null elements to the specified collection
	 * @param c
	 * @param elements
	 */
	public static <E> void addAllNotNull(final Collection<? super E> c, final E... elements) {
		if (elements == null) {
			return;
		}
		for (final E e : elements) {
			addIfNotNull(e, c);
		}
	}

	/**
	 * Used to transform a collection of <b>objects</b> into a list<br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @return a list filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <E, O, X extends Exception> List<E> createList(final Collection<? extends O> objects,
			final ExceptionalFunction<O, E, X> evaluator) throws X {
		return (List<E>) createCollection(objects, evaluator, Type.ARRAYLIST);
	}

	/**
	 * Used to transform a collection of <b>objects</b> into a set<br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @return a list filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <E, O, X extends Exception> Set<E> createSet(final Collection<? extends O> objects,
			final ExceptionalFunction<O, E, X> evaluator) throws X {
		return (Set<E>) createCollection(objects, evaluator, Type.LINKED_HASHSET);
	}

	/**
	 * @deprecated use {@link #createSet(Collection, Function)} or {@link #createList(Collection, Function)} instead
	 * Used to transform a collection of <b>objects</b> into another <b>collection</b><br>
	 * The values for the second collection are generated by using a <b>evaluator</b>. <br>
	 * Objects are null or that evaluate to <b>null</b> will be skipped
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	@Deprecated
	public static <C extends Collection<V>, V, O, X extends Exception> C createCollection(final Collection<? extends O> objects,
			final ExceptionalFunction<O, V, X> evaluator, final C collection) throws X {
		if (objects != null) {

			for (final O obj : objects) {
				if (obj == null) {
					continue;
				}

				final V value = evaluator.evaluate(obj);
				addIfNotNull(value, collection);
			}

		}
		return collection;
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
	private static <V, O, X extends Exception> Collection<V> createCollection(final Collection<? extends O> objects,
			final ExceptionalFunction<O, V, X> evaluator, final Type type) throws X {
		final Collection<V> collection = type.create();
		if (null == objects) {
			return collection;
		}
		for (final O obj : objects) {
			if (obj == null) {
				continue;
			}

			final V value = evaluator.evaluate(obj);
			addIfNotNull(value, collection);
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
	public static <C extends Collection<O>, O> C filter(final C objects, final IFilter<O> filter) {
		if (objects != null) {
			final Iterator<O> iterator = objects.iterator();
			while (iterator.hasNext()) {
				if (!filter.keep(iterator.next())) {
					iterator.remove();
				}
			}
		}
		return objects;
	}

	/**
	 * used to break a large collection into hashsets
	 * @param collection
	 * @param maxElements
	 * @return
	 */
	public static <O> List<Collection<O>> breakInSets(final Collection<O> collection, final int maxElements) {
		return breakCollection(collection, maxElements, Type.HASHSET);
	}

	/**
	 * used to break a large collection into arraylists
	 * @param collection
	 * @param maxElements
	 * @return
	 */
	public static <O> List<Collection<O>> breakInLists(final Collection<O> collection, final int maxElements) {
		return breakCollection(collection, maxElements, Type.ARRAYLIST);
	}

	/**
	 * used to break a large collection into smaller collection
	 * consider using {@link CollectionUtil#breakInLists(Collection, int)} or {@link CollectionUtil#breakInSets(Collection, int)} instead
	 * @param collection
	 * @param maxElements
	 * @param type
	 * @return
	 */
	private static <O> List<Collection<O>> breakCollection(final Collection<O> collection, final int maxElements, final Type type) {
		final List<Collection<O>> result = new ArrayList<Collection<O>>();
		if (collection == null){
			return result;
		}
		Collection<O> step = type.create();
		final Iterator<O> iterator = collection.iterator();
		while (iterator.hasNext()) {
			if (step.size() == maxElements) {
				result.add(step);
				step = type.create();
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
	public static <O, X extends Exception> String join(final Collection<O> collection, final ExceptionalFunction<O, String, X> evaluator,
			final String separator, final String emptyMessage) throws X {
		final List<String> strings = createList(collection, evaluator);
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
	public static <O, X extends Exception> String join(final Collection<O> collection, final ExceptionalFunction<O, String, X> evaluator,
			final String separator) throws X {
		return join(collection, evaluator, separator, "");
	}

	/**
	 * @param element
	 * @param collection
	 */
	public static <O> void addIfNotNull(final O element, final Collection<O> collection) {
		if (element != null) {
			collection.add(element);
		}
	}

	/**
	 * Converts the elements to a list of elements. <br> Null elements are not included. Returns an unmodifiable view of the specified array.
	 * This method allows modules to provide users with "read-only" access to the list.
	 * Query operations on the returned list "read through" to the specified list, and attempts to modify the returned list, whether direct or via its iterator,
	 * result in an {@link UnsupportedOperationException}.
	 *
	 * @param the array for which an unmodifiable view is to be returned.
	 * @return an unmodifiable view of the generated list
	 */
	public static <E> List<E> convertToList(final E... elements) {
		if (elements == null || elements.length == 0) {
			return Collections.emptyList();
		}
		final List<E> list = new ArrayList<E>();
		addAllNotNull(list, elements);
		return Collections.unmodifiableList(list);
	}

	/**
	 * Used to transform a element <b>objects</b> into another<br>
	 * if the object is <b>null</b> or that evaluate to <b>null</b>, we will return the third parameter
	 * @param the object to be evaluated
	 * @param evaluator the evaluator
	 * @return a list filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <E, O, X extends Exception> E evaluate(final O object, final ExceptionalFunction<O, E, X> evaluator, final E whatIfNull) throws X {
		if (null == object) {
			return whatIfNull;
		}
		final E evaluate = evaluator.evaluate(object);
		if (null == evaluate) {
			return whatIfNull;
		}
		return evaluate;
	}

	/**
	 * @param iterable
	 * @return null if the iterable element is null or if there are no elements in it or the first element if it does
	 */
	public static <E> E first(final Iterable<E> iterable) {
		if (null == iterable) {
			return null;
		}
		final Iterator<E> it = iterable.iterator();
		if (!it.hasNext()) {
			return null;
		}
		return it.next();
	}

	/**
	 * @author Alexandru Bledea
	 * @since Feb 4, 2014
	 */
	private static enum Type {
		LINKED_HASHSET {
			@Override
			<E> Collection<E> create() {
				return new LinkedHashSet<E>();
			}
		},
		HASHSET {
			@Override
			<E> Collection<E> create() {
				return new HashSet<E>();
			}
		},
		ARRAYLIST {
			@Override
			<E> Collection<E> create() {
				return new ArrayList<E>();
			}
		};

		abstract <E> Collection<E> create();
	}

}
