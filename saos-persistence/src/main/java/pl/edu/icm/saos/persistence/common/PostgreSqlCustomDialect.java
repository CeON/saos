package pl.edu.icm.saos.persistence.common;

import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.dialect.PostgreSQL9Dialect;


public class PostgreSqlCustomDialect extends PostgreSQL9Dialect {

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public PostgreSqlCustomDialect() {

        this.registerColumnType(Types.JAVA_OBJECT, "json");
        
    }
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String getTypeName(int code, long length, int precision, int scale) throws HibernateException {
        if (code == Types.VARCHAR && length == 255 && precision == 19 && scale == 2) {
            return "text";
        }
        
        return super.getTypeName(code, length, precision, scale);
    }

}
