package superapp.data.interfaces;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import superapp.data.entities.SuperAppObjectEntity;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface SuperAppObjectRepository extends 
	PagingAndSortingRepository<SuperAppObjectEntity,SuperAppObjectIdBoundary> {

    List<SuperAppObjectEntity> findBycreationTimestampAfter(Instant createdAfter);

	

	List<SuperAppObjectEntity> findAllByParent(SuperAppObjectEntity parent, Pageable page);
    
}	
