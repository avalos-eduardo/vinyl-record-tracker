package com.example.vinyl_record_collection_tracker.seeders;

import com.example.vinyl_record_collection_tracker.models.*;
import com.example.vinyl_record_collection_tracker.repositories.DiscogsMasterRepository;
import com.example.vinyl_record_collection_tracker.repositories.DiscogsReleaseRepository;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.repositories.UserVinylRepository;
import com.example.vinyl_record_collection_tracker.services.DiscogsService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final UserVinylRepository userVinylRepository;
    private final DiscogsService discogsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      UserVinylRepository userVinylRepository,
                      DiscogsService discogsService,
                      BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userVinylRepository = userVinylRepository;
        this.discogsService = discogsService;
        this.passwordEncoder = passwordEncoder;
    }

    private static final String[] COLLECTION_DISCOGS_IDS = {
            "8419262",
            "27571194",
            "2045602",
            "31556044",
            "33136755",
            "30696067",
    };

    private static final String[] WISHLIST_DISCOGS_IDS = {
            "30024862",
            "30045766",
            "17156179",
    };

    private static final VinylCondition[] COLLECTION_CONDITIONS = {
            VinylCondition.VERY_GOOD,
            VinylCondition.MINT,
            VinylCondition.VERY_GOOD,
            VinylCondition.GOOD,
            VinylCondition.GOOD,
            VinylCondition.MINT,
    };

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User demo = userRepository.findByEmail("demo@vinyltracker.com");

        if (demo == null) {
            demo = new User();
            demo.setName("Demo User");
            demo.setEmail("demo@vinyltracker.com");
            demo.setPassword(passwordEncoder.encode("demo123"));
            demo.setDemo(true);
            userRepository.save(demo);
        } else if (!demo.isDemo()) {
            // backfill flag if this user existed before the column was added
            demo.setDemo(true);
            userRepository.save(demo);
        } else {
            userVinylRepository.deleteAll(userVinylRepository.findByUserId(demo.getId()));
        }

        for (int i = 0; i < COLLECTION_DISCOGS_IDS.length; i++) {
            seedVinyl(demo, COLLECTION_DISCOGS_IDS[i], COLLECTION_CONDITIONS[i], false);
        }
        for (String discogsId : WISHLIST_DISCOGS_IDS) {
            seedVinyl(demo, discogsId, VinylCondition.MINT, true);
        }

        System.out.println("Demo user and vinyls seeded successfully.");
    }

    private void seedVinyl(User user, String discogsId, VinylCondition condition, boolean wishlist) {
        DiscogsRelease release = discogsService.fetchAndSaveRelease(discogsId);
        if (release == null) {
            System.out.println("Could not fetch Discogs release " + discogsId + ", skipping.");
            return;
        }

        UserVinyl userVinyl = new UserVinyl();
        userVinyl.setUser(user);
        userVinyl.setDiscogsRelease(release);
        userVinyl.setCondition(condition);
        userVinyl.setWishlist(wishlist);
        userVinyl.setAddedAt(LocalDateTime.now());
        userVinylRepository.save(userVinyl);
    }
}