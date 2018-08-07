package com.erp360.dao;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.erp360.util.FacesUtil;
import com.erp360.util.Time;

/**
 * @author mauriciobejaranorivera
 * 
 *         Implementation of the generic Data Access Service All CRUD (create,
 *         read, update, delete) basic data access operations for any persistent
 *         object are performed in this class.
 */

@Stateless
public abstract class DataAccessObjectJpa<T, E, R, S, O, P, Q, U, V, W> implements DataAccessObject<T, E, R, S, O, P, Q, U, V, W> {

	private @Inject EntityManager em;

	private @Inject Event<T> tEventSrc;

	private @Inject Event<E> eEventSrc;

	private @Inject Event<R> rEventSrc;

	private @Inject Event<S> sEventSrc;

	private @Inject Event<O> oEventSrc;

	private @Inject Event<P> pEventSrc;

	private @Inject Event<Q> qEventSrc;

	private @Inject Event<U> uEventSrc;

	private @Inject Event<V> vEventSrc;

	private @Inject Event<W> wEventSrc;

	private EntityTransaction tx = null;

	public DataAccessObjectJpa() {
	}

	private Class<T> typeT;

	private Class<E> typeE;

	private Class<R> typeR;

	private Class<S> typeS;

	private Class<O> typeO;

	private Class<P> typeP;

	private Class<Q> typeQ;

	private Class<U> typeU;

	private Class<V> typeV;

	private Class<W> typeW;

	/**
	 * Default constructor
	 * 
	 * @param type
	 *            entity class
	 */
	public DataAccessObjectJpa(Class<T> typeT) {
		this.typeT = typeT;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE) {
		this.typeT = typeT;
		this.typeE = typeE;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR) {
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR,
			Class<S> typeS) {
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
		this.typeS = typeS;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR,
			Class<S> typeS, Class<O> typeO) {
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
		this.typeS = typeS;
		this.typeO = typeO;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR,
			Class<S> typeS, Class<O> typeO, Class<P> typeP) {
		super();
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
		this.typeS = typeS;
		this.typeO = typeO;
		this.typeP = typeP;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR,
			Class<S> typeS, Class<O> typeO, Class<P> typeP, Class<Q> typeQ) {
		super();
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
		this.typeS = typeS;
		this.typeO = typeO;
		this.typeP = typeP;
		this.typeQ = typeQ;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR,
			Class<S> typeS, Class<O> typeO, Class<P> typeP, Class<Q> typeQ,
			Class<U> typeU) {
		super();
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
		this.typeS = typeS;
		this.typeO = typeO;
		this.typeP = typeP;
		this.typeQ = typeQ;
		this.typeU = typeU;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR,
			Class<S> typeS, Class<O> typeO, Class<P> typeP, Class<Q> typeQ,
			Class<U> typeU, Class<V> typeV) {
		super();
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
		this.typeS = typeS;
		this.typeO = typeO;
		this.typeP = typeP;
		this.typeQ = typeQ;
		this.typeU = typeU;
		this.typeV = typeV;
	}

	public DataAccessObjectJpa(Class<T> typeT, Class<E> typeE, Class<R> typeR,
			Class<S> typeS, Class<O> typeO, Class<P> typeP, Class<Q> typeQ,
			Class<U> typeU, Class<V> typeV, Class<W> typeW) {
		super();
		this.typeT = typeT;
		this.typeE = typeE;
		this.typeR = typeR;
		this.typeS = typeS;
		this.typeO = typeO;
		this.typeP = typeP;
		this.typeQ = typeQ;
		this.typeU = typeU;
		this.typeV = typeV;
		this.typeW = typeW;
	}

	@Override
	public T create(T entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		tEventSrc.fire(entity);
		return entity;
	}

	@Override
	public E createE(E entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		eEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public R createR(R entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		rEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public S createS(S entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		sEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public O createO(O entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		oEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public P createP(P entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		pEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public Q createQ(Q entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		qEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public U createU(U entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		uEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public V createV(V entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		vEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public W createW(W entity) {
		this.em.persist(entity);
		this.em.flush();
		this.em.refresh(entity);
		wEventSrc.fire(entity);
		// registerBitacora(entity,"Registro un nuevo Campo : "+
		// entity.toString());
		return entity;
	}

	@Override
	public T update(T entity) {
		T t = (T) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return t;
	}

	@Override
	public E updateE(E entity) {
		E e = (E) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return e;
	}

	@Override
	public R updateR(R entity) {
		R r = (R) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return r;
	}

	@Override
	public S updateS(S entity) {
		S s = (S) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return s;
	}

	@Override
	public O updateO(O entity) {
		O o = (O) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return o;
	}

	@Override
	public P updateP(P entity) {
		P o = (P) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return o;
	}

	@Override
	public Q updateQ(Q entity) {
		Q o = (Q) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return o;
	}

	@Override
	public U updateU(U entity) {
		U o = (U) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return o;
	}

	@Override
	public V updateV(V entity) {
		V o = (V) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return o;
	}

	@Override
	public W updateW(W entity) {
		W o = (W) this.em.merge(entity);
		// registerBitacora(entity,"Modifico  : "+ entity.toString());
		return o;
	}

	@Override
	public boolean delete(Object id) {
		Object ref = this.em.getReference(this.typeT, id);
		this.em.remove(ref);
		return true;
	}
	
	public List<T> findAllByParameterObjectTwoOrderByDesc(String parameter,String parameterTwo,Object valor,Object valorTwo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get(parameter), valor),cb.equal(object.get(parameterTwo), valorTwo)).orderBy(cb.desc(object.get("id")));
		return em.createQuery(criteria).getResultList();
	}
	
	public BigInteger countTotalRecord() {
		String query = "select count(em) from "
				+ this.typeT.getSimpleName()
				+ " em where em.estado='AC' or em.estado='IN' or em.estado='PN' ";
		System.out.println("query:" + query);
		return (BigInteger) em.createNativeQuery(query).getSingleResult();
	}
	
	public List<T> findAllBySize(int start, int maxRows, Map filters) {
		StringBuffer queryAdd = new StringBuffer();
		boolean sw1 = false;
		boolean sw2 = true;
		for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
			String filterProperty = it.next();
			Object filterValue = filters.get(filterProperty);
			queryAdd.append(sw2 ? " WHERE (em.estado='AC' OR em.estado='IN' OR em.estado='PR' OR em.estado='PN') AND ": "");
			queryAdd.append(sw1 ? " AND " : "");

			if (isObjectNumber(filterProperty)) {
				queryAdd.append(" to_char(em." + filterProperty+ ",'999') LIKE '%" + filterValue + "%' ");
				System.out.println(queryAdd.toString());
			} else if (isObjectNumericDouble(filterProperty)) {
				queryAdd.append(" to_char(em." + filterProperty+ ",'FM999999999.999') LIKE '%" + filterValue + "%' ");
			} else if (isObjectDate(filterProperty)) {
				queryAdd.append(" to_char(em." + filterProperty+ ",'DD/MM/YYYY') LIKE '%" + filterValue + "%' ");
			} else if (isStringTypeFieldOfClass(filterProperty)) {
				queryAdd.append(" upper(em.");
				queryAdd.append(filterProperty);
				queryAdd.append(") ");
				queryAdd.append("like '%");
				queryAdd.append(FacesUtil.stripAccents(filterValue).toUpperCase());
				queryAdd.append("%' ");
			} else {// es un objeto com.qallta.tiluchi.model.?

			}
			sw1 = true;
			sw2 = false;
		}
		queryAdd.append(filters.isEmpty() ? " WHERE (em.estado='AC' OR em.estado='IN' OR em.estado='PR' OR em.estado='PN')": "");
		String sql = "SELECT em FROM " + this.typeT.getSimpleName() + " em "+ queryAdd.toString() + "  order by em.id desc";
		System.out.println(sql);
		Query q = em.createQuery(sql);
		// Query q =
		// em.createQuery("SELECT em FROM "+this.type.getSimpleName()+" em where ( em.estado='AC' or em.estado='CN') order by em.id desc");
		q.setFirstResult(start);
		q.setMaxResults(maxRows);
		return q.getResultList();
	}
	
	private boolean isStringTypeFieldOfClass(String filterProperty) {
		try {
			Class nameClass = Class.forName("com.erp360.model."+ this.typeT.getSimpleName());
			System.out.println(filterProperty.toString());
			if (filterProperty.contains(".")) {
				String[] cadena = filterProperty.split("\\.");
				System.out.println(cadena[0]);
				System.out.println(cadena[1]);
				String name=cadena[0];
				name = Character.toUpperCase(name.charAt(0)) + name.substring(1,name.length());
				System.out.println(name);
				String filter=cadena[1];
				nameClass = Class.forName("com.erp360.model."+name);
				System.out.println(nameClass.toString());
				System.out.println(filter);
				filterProperty = filter;
				
			};
			Field field = nameClass.getDeclaredField(filterProperty);
			field.setAccessible(true);
			System.out.println(" - field.getType().toString() -: "+ field.getType().getCanonicalName());

			if (field.getType().getCanonicalName().equals("java.lang.String")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(" - error -: " + e.getMessage());
			return false;
		}
	}
	
	private boolean isObjectNumber(String filterProperty) {
		try {
			if (filterProperty.contains(".")) {
				return false;
			}
			Class nameClass = Class.forName("com.erp360.model.model."+ this.typeT.getSimpleName());
			Field field = nameClass.getDeclaredField(filterProperty);
			field.setAccessible(true);
			String typeField = field.getType().getCanonicalName();
			System.out.println(typeField);
			if (typeField.equals("java.lang.Integer")|| typeField.equals("int")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out
					.println("ERROR->Transfer->DataAccessObjectJPA->isObjectEntero: "
							+ e.getMessage());
			return false;
		}
	}

	private boolean isObjectNumericDouble(String filterProperty) {
		try {
			if (filterProperty.contains(".")) {
				return false;
			}
			Class nameClass = Class.forName("com.erp360.model.model."
					+ this.typeT.getSimpleName());
			Field field = nameClass.getDeclaredField(filterProperty);
			field.setAccessible(true);
			String typeField = field.getType().getCanonicalName();
			System.out.println(typeField);
			if (typeField.equals("java.lang.Double")|| typeField.equals("double")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out
					.println("ERROR->Transfer->DataAccessObjectJPA->isObjectEntero: "
							+ e.getMessage());
			return false;
		}
	}

	private boolean isObjectDate(String filterProperty) {
		try {
			if (filterProperty.contains(".")) {
				return false;
			}
			Class nameClass = Class.forName("com.erp360.model.model."
					+ this.typeT.getSimpleName());
			Field field = nameClass.getDeclaredField(filterProperty);
			field.setAccessible(true);
			String typeField = field.getType().getCanonicalName();
			if (typeField.equals("java.util.Date")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out
					.println("ERROR->Transfer->DataAccessObjectJPA->isObjectEntero: "
							+ e.getMessage());
			return false;
		}
	}


	@Override
	public T findById(Object id) {
		return this.em.find(this.typeT, id);
	}

	@Override
	public List<T> findAll() {
		return null;
	}

	@Override
	public void beginTransaction() {

	}

	@Override
	public void commitTransaction() {

	}

	@Override
	public void rollbackTransaction() {

	}

	@Override
	public List<T> findDescAllOrderedByParameter(String parameter) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).orderBy(cb.desc(object.get(parameter)));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<T> findAscAllOrderedByParameter(String parameter) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).orderBy(cb.asc(object.get(parameter)));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<T> findAllByEstadoOrderByAsc(String estado) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get("estado"), estado))
				.orderBy(cb.asc(object.get("id")));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<T> findAllByEstadoOrderByDesc(String estado) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get("estado"), estado))
				.orderBy(cb.desc(object.get("id")));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public T findByParameter(String parameter, Object valor) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get(parameter), valor));
		return em.createQuery(criteria).getSingleResult();
	}

	@Override
	public BigInteger countTotalRecord(String table) {
		String query = "select count(em) from " + table
				+ " em where em.estado='AC' or em.estado='IN' or em.estado='PR' OR em.estado='PN'";
		System.out.println("query:" + query);
		return (BigInteger) em.createNativeQuery(query).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllBySize(int start, int maxRows) {
		Query q = em
				.createQuery("SELECT em FROM "
						+ this.typeT.getSimpleName()
						+ " em where em.estado='AC' or em.estado='IN' order by em.id desc");
		q.setFirstResult(start);
		q.setMaxResults(maxRows);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllActivosByMaxResultOrderByDesc(int maxRows) {
		Query q = em.createQuery("SELECT em FROM " + this.typeT.getSimpleName()
				+ " em where em.estado='AC' order by em.id desc");
		q.setMaxResults(maxRows);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllActivosByMaxResultOrderByAsc(int maxRows) {
		Query q = em.createQuery("SELECT em FROM " + this.typeT.getSimpleName()
				+ " em where em.estado='AC' order by em.id desc");
		q.setMaxResults(maxRows);
		return q.getResultList();
	}

	@Override
	public List<T> findAllByParameter(String parameter, Object valor) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get(parameter), valor));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<T> findAllActivosByParameter(String parameter, Object valor) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get(parameter), valor),
				cb.equal(object.get("estado"), "AC"));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public List<T> findAllByParameterObjectTwo(String parameter,
			String parameterTwo, Object valor, Object valorTwo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object)
				.where(cb.equal(object.get(parameter), valor),
						cb.equal(object.get(parameterTwo), valorTwo))
				.orderBy(cb.desc(object.get("id")));
		return em.createQuery(criteria).getResultList();
	}

	public List<T> executeQueryResulList(String query) {
		//Query q = em.createQuery(query);
		Query q = em.createQuery(query);
		return q.getResultList();
	}
	
	/**
	 * 
	 * @param query
	 * @param stDate Fecha Inicio
	 * @param edDate Fecha Final
	 * @return
	 */
	public List<T> executeQueryResulListByDate(String query,Date stDate,Date edDate) {
		Query q = em.createQuery(query).setParameter("stDate", stDate).setParameter("edDate", edDate);
		return q.getResultList();
	}

	public T executeQuerySingleResult(String query) {
		Query q = em.createQuery(query);
		return (T) q.getSingleResult();
	}
	
	/**
	 * Obtiene el ultimo registro con estado activo('AC') de la tabla 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findLastActiveRecord(String param,Object value) {
		Query q = em.createQuery("SELECT em FROM "+this.typeT.getSimpleName()+" em  where em."+param+"="+value+" order by em.id desc").setMaxResults(1);
		return (T) q.getSingleResult();
	}
	
	public Object executeNativeQuerySingleResult(String query) {
		Query q = em.createNativeQuery(query);
		return (Object) q.getSingleResult();
	}

	public Number executeQuerySingleResultNumber(String query){
		return ((Number) em.createQuery(query).getSingleResult());
	}
	
	@Override
	public T findByParameterObjectTwo(String parameter, String parameterTwo,
			Object valor, Object valorTwo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get(parameter), valor),
				cb.equal(object.get(parameterTwo), valorTwo));
		return em.createQuery(criteria).getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllActivosByQuery(String parameter, Object valor) {
		String upperQuery = valor.toString().toUpperCase();
		Query q = em
				.createQuery("SELECT em FROM "
						+ this.typeT.getSimpleName()
						+ " em where upper(translate(em."
						+ parameter
						+ ", 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"
						+ upperQuery + "%' and em.estado='AC' order by em."
						+ parameter + " asc");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllActivosByQueryAndTwoParameter(String parameter1,
			Object value1, String parameter2, Object value2) {
		String upperQuery = value2.toString().toUpperCase();
		String qu = "SELECT em FROM "
				+ this.typeT.getSimpleName()
				+ " em where upper(translate(em."
				+ parameter2
				+ ", 'áéíóúÁÉÍÓÚäëïöüÄËÏÖÜñ', 'aeiouAEIOUaeiouAEIOUÑ')) like '%"
				+ upperQuery + "%' and em." + parameter1 + "='" + value1
				+ "' order by em." + parameter2 + " desc";
		Query q = em.createQuery(qu);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAllActiveInactiveOtherTableAndParameter(
			String nameTableObject, String nameTableQuery, String paramter,
			Object value) {
		String query = "select em from " + this.typeT.getSimpleName() + " em,"
				+ nameTableObject
				+ " gcc where (em.estado='AC' or em.estado='IN') and em."
				+ nameTableQuery + ".id=gcc.id and gcc." + paramter + ".id="
				+ value;
		System.out.println("Query " + query);
		return em.createQuery(query).getResultList();
	}

	@Override
	public List<T> findAllByParameterDate(String parameter, Date valor) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object).where(cb.equal(object.get(parameter), valor))
				.orderBy(cb.desc(object.get("id")));
		return em.createQuery(criteria).getResultList();
	}

	@Override
	public T findAllByParameterDateAndTwoParameter(String parameter,
			Date valor, String parameterTwo, Object valueTwo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(this.typeT);
		Root<T> object = criteria.from(this.typeT);
		criteria.select(object)
				.where(cb.equal(object.get(parameter), valor),
						cb.equal(object.get(parameterTwo), valueTwo))
				.orderBy(cb.desc(object.get("id")));
		return em.createQuery(criteria).getSingleResult();
	}

	public EntityManager getEm() {
		return this.em;
	}

	/*
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAllActiveParameter(String paramter, Object value) {
		String query = "select em from " + this.typeT.getSimpleName()
				+ " em where (em.estado='AC' OR em.estado='IN') and  em."
				+ paramter + ".id=" + value;
		System.out.println("Query " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllActiveOtherTableAndParameter(String nameTableObject,
			String nameTableQuery, String paramter, Object value) {
		String query = "select em from " + this.typeT.getSimpleName() + " em,"
				+ nameTableObject + " gcc where em.estado='AC' and em."
				+ nameTableQuery + ".id=gcc.id and gcc." + paramter + "="
				+ value ;
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllActiveParameterOrderedForId(String paramter,
			Object value) {
		String query = "select em from " + this.typeT.getSimpleName()
				+ " em where (em.estado='AC' OR em.estado='IN') and  em."
				+ paramter + ".id=" + value + " order by em.id asc";
		System.out.println("Query " + query);
		return em.createQuery(query).getResultList();
	}

	/**
	 * 
	 * @param parameterUsuer
	 * @param usuario
	 * @param nameTableQuery
	 * @param value1
	 * @param nameTable2Query
	 * @param value2
	 * @param paramterBegin
	 * @param fechaini
	 * @param paramterEnd
	 * @param fechafin
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> findActiveForThwoDatesAndThwoObject(String parameterUsuer,
			String usuario, String nameTableQuery, Object value1,
			String nameTable2Query, Object value2, String paramterBegin,
			Date fechaini, String paramterEnd, Date fechafin) {
		String query = "select em from " + this.typeT.getSimpleName()
				+ " em where  em." + nameTableQuery + ".id=" + value1
				+ " and  em." + nameTable2Query + ".id=" + value2 + " and (em."
				+ parameterUsuer + " like '" + usuario + "' or em."
				+ parameterUsuer + " like '" + usuario
				+ "') and  to_number(to_char(em." + paramterEnd
				+ "  ,'YYYYMMDD'), '999999999')>="
				+ Time.obtenerFormatoYYYYMMDD(fechaini)
				+ " and  to_number(to_char(em." + paramterEnd
				+ " ,'YYYYMMDD'), '999999999')<="
				+ Time.obtenerFormatoYYYYMMDD(fechafin)
				+ "  order by em.id desc";
		System.out.println("Query Factura: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllActiveForThwoDatesAndThwoObject(
			String nameTableQuery, Object value1, String nameTable2Query,
			Object value2, String paramterBegin, Date fechaini,
			String paramterEnd, Date fechafin) {
		String query = "select em from " + this.typeT.getSimpleName()
				+ " em where  em." + nameTableQuery + ".id=" + value1
				+ " and  em." + nameTable2Query + ".id=" + value2
				+ " and to_number(to_char(em." + paramterEnd
				+ "  ,'YYYYMMDD'), '999999999')>="
				+ Time.obtenerFormatoYYYYMMDD(fechaini)
				+ " and  to_number(to_char(em." + paramterEnd
				+ " ,'YYYYMMDD'), '999999999')<="
				+ Time.obtenerFormatoYYYYMMDD(fechafin)
				+ "  order by em.id desc";
		System.out.println("Query Factura: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findActiveForThwoDatesAndThwoObject(String parameterUsuer,
			String usuario, String nameTableQuery, Object value,
			String paramterBegin, Date fechaini, String paramterEnd,
			Date fechafin) {
		String query = "select em from " + this.typeT.getSimpleName()
				+ " em  where   em." + nameTableQuery + ".id=" + value
				+ " and (em." + parameterUsuer + " like '" + usuario
				+ "' or em." + parameterUsuer + " like '" + usuario
				+ "') and  to_number(to_char(em." + paramterEnd
				+ "  ,'YYYYMMDD'), '999999999')>="
				+ Time.obtenerFormatoYYYYMMDD(fechaini)
				+ " and  to_number(to_char(em." + paramterEnd
				+ " ,'YYYYMMDD'), '999999999')<="
				+ Time.obtenerFormatoYYYYMMDD(fechafin)
				+ "  order by em.id desc";
		System.out.println("Query Factura: " + query);
		return em.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllActiveForThwoDatesAndThwoObject(
			String nameTableQuery, Object value, String paramterBegin,
			Date fechaini, String paramterEnd, Date fechafin) {
		String query = "select em from " + this.typeT.getSimpleName()
				+ " em  where   em." + nameTableQuery + ".id=" + value
				+ " and  to_number(to_char(em." + paramterEnd
				+ "  ,'YYYYMMDD'), '999999999')>="
				+ Time.obtenerFormatoYYYYMMDD(fechaini)
				+ " and  to_number(to_char(em." + paramterEnd
				+ " ,'YYYYMMDD'), '999999999')<="
				+ Time.obtenerFormatoYYYYMMDD(fechafin)
				+ "  order by em.id desc";
		System.out.println("Query Factura: " + query);
		return em.createQuery(query).getResultList();
	}

	public EntityManager getEntityManager() {
		return em;
	}
}