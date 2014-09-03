package pl.edu.icm.saos.persistence.search.dto;


import pl.edu.icm.saos.persistence.common.DataObject;

import java.lang.reflect.ParameterizedType;

/**
 * Abstract base class for database search filters.
 *
 * @param <D> expected query result type
 * @author mpol@icm.edu.pl
 * @author pavtel
 */
@SuppressWarnings("serial")
public abstract class DatabaseSearchFilter<D extends DataObject> extends SearchFilter {

    public static final String PROP_ID = "id";

    /**
     * Returns the expected query result type.
     * <p>
     * This implementation returns the first class argument given to the superclass.
     * Useful for <code>FooFilter extends SomeSearchFilter&lt;Foo></code>, where it returns
     * <code>Foo.class</code>. Needs to be overridden otherwise.
     *
     * @return expected result type
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<D> getReqType() {
        return (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    private boolean initialize;

    /**
     * @return true if the result objects should be initialized before returning it to client, false otherwise
     */
    public boolean getInitialize() {
        return initialize;
    }

    void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    private Integer idFrom;

    /**
     * @return the minimum object id to query
     */
    public Integer getIdFrom() {
        return idFrom;
    }

    void setIdFrom(Integer idFrom) {
        this.idFrom = idFrom;
    }

    static abstract class Builder<B extends Builder<B, T>, T extends DatabaseSearchFilter<?>> {
        /**
         * @return <code>this</code> at current type
         */
        protected abstract B self();
        protected T instance;

        /**
         * @param idFrom the minimum object id to query
         * @return this builder
         */
        public B idFrom(int idFrom) {
            instance.setIdFrom(idFrom);
            return self();
        }

        /**
         * Requests that the result objects should be initialized before returning it to client.
         * @return this builder
         */
        public B initialize() {
            instance.setInitialize(true);
            return self();
        }

        /**
         * Requests that the results are sorted in ascending order on the values of the specified property.
         * @param prop property name
         * @return this builder
         */
        public B upBy(String prop) {
            instance.addOrderBy(prop, true);
            return self();
        }

        /**
         * Requests that the results are sorted in descending order on the values of the specified property.
         * @param prop property name
         * @return this builder
         */
        public B downBy(String prop) {
            instance.addOrderBy(prop, false);
            return self();
        }

        /**
         * @param limit requested maximum number of results
         * @return this builder
         */
        public B limit(int limit) {
            instance.setLimit(limit);
            return self();
        }

        /**
         * @param offset
         * @return this builder
         */
        public B offset(int offset){
            instance.setFirst(offset);
            return self();
        }

        /**
         * @return the configured filter
         */
        public T filter() {
            return instance;
        }
    }
}
