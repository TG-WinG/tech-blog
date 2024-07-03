package kr.tgwing.tech.project.respository.thumbnail;

import kr.tgwing.tech.project.domain.ThumbnailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepository extends JpaRepository<ThumbnailEntity, Long> {
    @Modifying
    @Query("DELETE FROM ThumbnailEntity p WHERE p.project.id = :projectId")
    void deleteByProjectId(@Param("projectId") Long projectId);
}
