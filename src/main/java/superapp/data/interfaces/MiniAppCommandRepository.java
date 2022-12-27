package superapp.data.interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import superapp.data.entities.MiniAppCommandEntity;
import superapp.logic.boundaries.identifiers.CommandId;

public interface MiniAppCommandRepository extends PagingAndSortingRepository<MiniAppCommandEntity,CommandId> {
	
	List<MiniAppCommandEntity> findAllByCommandId_Miniapp(@Param("miniapp") String miniapp,Pageable page);

}
