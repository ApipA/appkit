/**
 *
 */
package de.xwic.appkit.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.xwic.appkit.core.util.InternalEvaluator.EvaluationResult;
import de.xwic.appkit.core.util.InternalEvaluator.IDupeChecker;


/**
 * @author Alexandru Bledea
 * @since Jul 30, 2013
 */
public class CollectionUtil {

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <C extends Collection<V>, V, O> C createCollection(O[] objects, IEvaluator<O, V> evaluator, C collection) {
		if (objects == null) {
			return collection;
		}
		return createCollection(Arrays.asList(objects), evaluator, collection);
	}

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<? extends O> objects, IEvaluator<O, V> evaluator, C collection) {
		try {
			return createCollection(objects, evaluator, collection, true);
		} catch (DuplicateKeyException e) {
			return null; //not going to happen
		}
	}

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @param allowDupes if we allow dupes in the collection
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<? extends O> objects, IEvaluator<O, V> evaluator, C collection,
			boolean allowDupes) throws DuplicateKeyException {
		return createCollection(objects, evaluator, collection, allowDupes, true);
	}

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @param allowDupes if we allow dupes in the collection
	 * @param skipNullObjects if we skip null objects, if we don't we will get a we will get a {@link java.lang.NullPointerException}
	 * @return the collection passed as argument filled with the evaluated values, if an object evaluates to null, it is not added to the collection
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 * @throws NullPointerException if there is a null value in the collection and we don't skip it
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<? extends O> objects, IEvaluator<O, V> evaluator, C collection,
			boolean allowDupes, boolean skipNullObjects) throws DuplicateKeyException {
		return createCollection(objects, evaluator, collection, allowDupes, skipNullObjects, true);
	}

	/**
	 * @param objects the collection from which we create the collection
	 * @param evaluator the evaluator
	 * @param collection the collection where the evaluation result will be added
	 * @param allowDupes if we allow dupes in the collection
	 * @param skipNullObjects if we skip null objects, if we don't we will get a we will get a {@link java.lang.NullPointerException}
	 * @param skipNullValues if we want to skip the null values from being added to the collection
	 * @return the collection passed as argument filled with the evaluated values
	 * @throws DuplicateKeyException if we have a duplicate key and we don't allow that
	 * @throws NullPointerException if there is a null value in the collection and we don't skip it
	 */
	public static <C extends Collection<V>, V, O> C createCollection(Collection<? extends O> objects, IEvaluator<O, V> evaluator, final C collection,
			boolean allowDupes, boolean skipNullObjects, boolean skipNullValues) throws DuplicateKeyException {
		EvaluationResult<V> result = new EvaluationResult<V>();
		IDupeChecker<V> dupeChecker = new IDupeChecker<V>() {

			@Override
			public boolean checkIfDupe(V what) {
				return collection.contains(what);
			}
		};

		for (O t : objects) {
			result = InternalEvaluator.evaluate(t, skipNullObjects, evaluator, skipNullValues, allowDupes, dupeChecker, result);
			if (!result.skip()) {
				collection.add(result.getResult());
			}
		}
		return collection;
	}

	/**
	 * @param collection
	 * @param maxElements
	 * @param clazz
	 * @return
	 */
	public static <O, C extends Collection & Cloneable> List<Collection<O>> breakCollection(Collection<O> collection, int maxElements, Class<C> clazz) {
		List<Collection<O>> result = new ArrayList<Collection<O>>();
		C step = instantiate(clazz);
		Iterator<O> iterator = collection.iterator();
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
	 * @param clazz
	 * @return
	 */
	public static <C extends Collection> C instantiate(Class<C> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Illegal implementation class " + clazz.getName());
		}
	}
}
