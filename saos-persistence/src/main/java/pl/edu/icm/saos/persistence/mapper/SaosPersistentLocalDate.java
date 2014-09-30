package pl.edu.icm.saos.persistence.mapper;

import org.hibernate.usertype.ParameterizedType;
import org.jadira.usertype.spi.shared.AbstractParameterizedUserType;
import org.jadira.usertype.spi.shared.IntegratorConfiguredType;
import org.joda.time.LocalDate;

import java.sql.Date;

/**
 * Persist {@link LocalDate} via Hibernate.
 * @author pavtel
 */
public class SaosPersistentLocalDate  extends AbstractParameterizedUserType<LocalDate, Date, LocalDateDateColumnMapper> implements ParameterizedType, IntegratorConfiguredType {
    private static final long serialVersionUID = 3837989592585803621L;
}
