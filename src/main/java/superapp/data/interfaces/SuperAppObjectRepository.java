package superapp.data.interfaces;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import superapp.data.SuperAppObjectEntity;
import superapp.logic.boundaries.MiniAppCommandBoundary;
import superapp.logic.boundaries.identifiers.SuperAppObjectIdBoundary;

public interface SuperAppObjectRepository extends PagingAndSortingRepository<SuperAppObjectEntity,SuperAppObjectIdBoundary> {

    List<SuperAppObjectEntity> findBycreationTimestampAfter(Instant createdAfter);
    
    List<SuperAppObjectEntity> findBycreationTimestampAfterAndActive(boolean active, Instant createdAfter);

	List<SuperAppObjectEntity> findAllByParent(SuperAppObjectEntity parent, Pageable page);
	
	List<SuperAppObjectEntity> findAllByType(String type, Pageable page);
	
	List<SuperAppObjectEntity> findAllByTypeAndActive(String type,boolean active, Pageable page);
    
	List<SuperAppObjectEntity> findAllByAlias(String alias, Pageable page);
	
	List<SuperAppObjectEntity> findAllByAliasAndActive(String alias,boolean active, Pageable page);
	
	List<SuperAppObjectEntity> findAllByAliasContaining(String text, Pageable page);
	
	List<SuperAppObjectEntity> findAllByAliasContainingAndActive(String text,boolean active , Pageable page);
	
	List<SuperAppObjectEntity> findAllByActive(boolean active, Pageable page);
	
	List<SuperAppObjectEntity> findAllByParentAndActive(SuperAppObjectEntity parent, boolean active ,Pageable page);
	
	List<SuperAppObjectEntity> findAllByChildrens(SuperAppObjectEntity childrens, Pageable page);
	
	List<SuperAppObjectEntity> findAllByChildrensAndActive(SuperAppObjectEntity childrens, boolean active ,Pageable page);
	
	
	
}	
