package com.example.vinyl_record_collection_tracker.repositories;

import com.example.vinyl_record_collection_tracker.models.UserVinyl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserVinylRepository extends JpaRepository<UserVinyl, Long> {
    List<UserVinyl> findByUserId(Long userId);
    boolean existsByUserIdAndDiscogsReleaseId(Long userId, Long discogsReleaseId);
    List<UserVinyl> findByUserIdAndWishlistFalse(Long userId);
    List<UserVinyl> findByUserIdAndWishlistTrue(Long userId);
    boolean existsByUserIdAndDiscogsReleaseIdAndWishlistFalse(Long userId, Long releaseId);
    boolean existsByUserIdAndDiscogsReleaseIdAndWishlistTrue(Long userId, Long releaseId);
}
