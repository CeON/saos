package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import pl.edu.icm.saos.persistence.common.IndexableObject;

public interface IndexableObjectRepository<I extends IndexableObject> {

    @Query("select i from #{#entityName} i where i.indexed=false")
    Page<I> findAllNotIndexed(Pageable pageable);
    
    @Query("select i.id from #{#entityName} i where i.indexed=false")
    List<Long> findAllNotIndexedIds();
    
}
