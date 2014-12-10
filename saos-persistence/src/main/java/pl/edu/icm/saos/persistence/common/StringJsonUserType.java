package pl.edu.icm.saos.persistence.common;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

/**
 * Hbernate user type converting String into json type. A relevant
 * column type should be registered in used {@link Dialect}
 * @author timfulmer /see: http://stackoverflow.com/questions/15974474/mapping-postgresql-json-column-to-hibernate-value-type/
 * @author lukdumi /just copied and removed comments/
 */
public class StringJsonUserType implements UserType {

    
    @Override
    public int[] sqlTypes() {
        return new int[] { Types.JAVA_OBJECT};
    }

    @Override
    public Class<?> returnedClass() {
        return String.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {

        if( x== null){

            return y== null;
        }

        return x.equals( y);
    }

    
    @Override
    public int hashCode(Object x) throws HibernateException {

        return x.hashCode();
    }

    
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        if(rs.getString(names[0]) == null){
            return null;
        }
        return rs.getString(names[0]);
    }

    
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }

        st.setObject(index, value, Types.OTHER);
    }

    
    @Override
    public Object deepCopy(Object value) throws HibernateException {

        return value;
    }

    
    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (String)this.deepCopy( value);
    }

    
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return this.deepCopy( cached);
    }

    
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}