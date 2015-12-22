package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.edu.icm.saos.persistence.common.IndexableObject;

public interface IndexableObjectRepository<I extends IndexableObject> extends IndexableObjectRepositoryCustom {

    @Query("select i from #{#entityName} i where i.indexed=false")
    Page<I> findAllNotIndexed(Pageable pageable);
    
    @Query("select i.id from #{#entityName} i where i.indexed=false")
    List<Long> findAllNotIndexedIds();
    
    @Modifying
    @Transactional
    @Query("update #{#entityName} i set i.indexed=false where i.indexed=true and i.id in :ids")
    void markAsNotIndexed(@Param("ids") List<Long> ids);
    
}
