package com.erp360.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author mauriciobejaranorivera
 *
 * @param <T>
 *            class type
 * @param <ID>
 *            param
 */
public interface DataAccessObject<T, E, R, S, O, P, Q, U, V, W> {

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param T
	 *            Object
	 * @return Object
	 */
	T create(T entity) throws Exception;

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param E
	 *            Object
	 * @return Object
	 */
	E createE(E entity) throws Exception;

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param R
	 *            Object
	 * @return Object
	 */
	R createR(R entity) throws Exception;

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param S
	 *            Object
	 * @return Object
	 */
	S createS(S entity) throws Exception;

	/**
	 * Stores an instance of the entity class in the database
	 * 
	 * @param O
	 *            Object
	 * @return Object
	 */

	O createO(O entity) throws Exception;

	// ,P,Q,U,V,W
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	P createP(P entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	Q createQ(Q entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	U createU(U entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	V createV(V entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	W createW(W entity) throws Exception;

	/**
	 * Update the entity instance
	 * 
	 * @param <T>
	 * @param t
	 * @return the object that is updated
	 */
	T update(T entity) throws Exception;

	/**
	 * Update the entity instance
	 * 
	 * @param <E>
	 * @param e
	 * @return the object that is updated
	 */
	E updateE(E entity) throws Exception;

	/**
	 * Update the entity instance
	 * 
	 * @param <R>
	 * @param r
	 * @return the object that is updated
	 */
	R updateR(R entity) throws Exception;

	/**
	 * Update the entity instance
	 * 
	 * @param <S>
	 * @param s
	 * @return the object that is updated
	 */
	S updateS(S entity) throws Exception;

	/**
	 * Update the entity instance
	 * 
	 * @param <O>
	 * @param o
	 * @return the object that is updated
	 */
	O updateO(O entity) throws Exception;

	// ,P,Q,U,V,W
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	P updateP(P entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	Q updateQ(Q entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	U updateU(U entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	V updateV(V entity) throws Exception;

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	W updateW(W entity) throws Exception;

	/**
	 * Remove the record that is associated with the entity instance
	 * 
	 * @param id
	 */
	boolean delete(Object id) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	T findById(Object id) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	List<T> findAll() throws Exception;

	/**
	 * 
	 * @param parameter
	 * @return
	 */
	List<T> findDescAllOrderedByParameter(String parameter);

	/**
	 * 
	 * @param parameter
	 * @return
	 */
	List<T> findAscAllOrderedByParameter(String parameter);

	/**
	 * 
	 * @param estado
	 * @return
	 */
	List<T> findAllByEstadoOrderByAsc(String estado);

	/**
	 * 
	 * @param estado
	 * @return
	 */
	List<T> findAllByEstadoOrderByDesc(String estado);

	/**
	 * Returns the number of total records
	 * 
	 * @param namedQueryName
	 * @return Long
	 */
	BigInteger countTotalRecord(String table);

	/**
	 * 
	 * @param size
	 * @return
	 */
	List<T> findAllBySize(int start, int maxRows);

	/**
	 * 
	 * @param parameter
	 * @param valor
	 * @return
	 */
	T findByParameter(String parameter, Object valor);

	/**
	 * 
	 * @param parameter
	 * @param valor
	 * @return
	 */
	List<T> findAllByParameter(String parameter, Object valor);

	/**
	 * 
	 * @param parameter
	 * @param parameterTwo
	 * @param valor
	 * @param valorTwo
	 * @return
	 */
	List<T> findAllByParameterObjectTwo(String parameter, String parameterTwo,
			Object valor, Object valorTwo);

	/**
	 * 
	 * @param parameter
	 * @param parameterTwo
	 * @param valor
	 * @param valorTwo
	 * @return
	 */
	T findByParameterObjectTwo(String parameter, String parameterTwo,
			Object valor, Object valorTwo);

	/**
	 * 
	 * @param maxRows
	 * @return
	 */
	List<T> findAllActivosByMaxResultOrderByDesc(int maxRows);

	/**
	 * 
	 * @param maxRows
	 * @return
	 */
	List<T> findAllActivosByMaxResultOrderByAsc(int maxRows);

	/**
	 * 
	 * @param parameter
	 * @param valor
	 * @return
	 */
	List<T> findAllActivosByQuery(String parameter, Object valor);

	/**
	 * 
	 * @param parameter
	 * @param valor
	 * @return
	 */
	List<T> findAllActivosByParameter(String parameter, Object valor);

	/**
	 * 
	 * @param parameter1
	 * @param value
	 * @param parameter2
	 * @param valor
	 * @return
	 */
	List<T> findAllActivosByQueryAndTwoParameter(String parameter1,
			Object value, String parameter2, Object valor);

	/**
	 * 
	 * @param nameTableObject
	 * @param nameTableQuery
	 * @param paramter
	 * @param value
	 * @return
	 */
	List<T> findAllActiveInactiveOtherTableAndParameter(String nameTableObject,
			String nameTableQuery, String paramter, Object value);

	/**
	 * 
	 * @param parameter
	 * @param valor
	 * @return
	 */
	List<T> findAllByParameterDate(String parameter, Date valor);

	/**
	 * 
	 * @param parameter
	 * @param valor
	 * @param parameterTwo
	 * @param valueTwo
	 * @return
	 */
	T findAllByParameterDateAndTwoParameter(String parameter, Date valor,
			String parameterTwo, Object valueTwo);

	/**
	 * Init Transaction
	 */
	void beginTransaction();

	/**
	 * Commit Transaction
	 */
	void commitTransaction();

	/**
	 * Rollback Transaction
	 */
	void rollbackTransaction();

}
