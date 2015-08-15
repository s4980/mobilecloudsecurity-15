package org.magnum.mobilecloud.video.persistence;

import org.magnum.mobilecloud.video.model.UserVideoRating;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface JpaUserVideoRatingRepository extends CrudRepository<UserVideoRating, Long> {

    // Find all user ratings with a matching user name
    Collection<UserVideoRating> findByUser(String user);
}
