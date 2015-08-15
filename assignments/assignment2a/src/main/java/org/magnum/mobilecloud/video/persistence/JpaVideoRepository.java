package org.magnum.mobilecloud.video.persistence;

import org.magnum.mobilecloud.video.model.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVideoRepository extends CrudRepository<Video, Long> {
}
