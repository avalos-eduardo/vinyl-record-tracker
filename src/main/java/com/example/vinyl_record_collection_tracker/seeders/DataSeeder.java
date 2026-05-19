package com.example.vinyl_record_collection_tracker.seeders;

import com.example.vinyl_record_collection_tracker.models.*;
import com.example.vinyl_record_collection_tracker.repositories.DiscogsReleaseRepository;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.repositories.UserVinylRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final DiscogsReleaseRepository discogsReleaseRepository;
    private final UserVinylRepository userVinylRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      DiscogsReleaseRepository discogsReleaseRepository,
                      UserVinylRepository userVinylRepository,
                      BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.discogsReleaseRepository = discogsReleaseRepository;
        this.userVinylRepository = userVinylRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User demo = userRepository.findByEmail("demo@vinyltracker.com");

        if (demo == null) {
            demo = new User();
            demo.setName("Demo User");
            demo.setEmail("demo@vinyltracker.com");
            demo.setPassword(passwordEncoder.encode("demo123"));
            userRepository.save(demo);
        } else {
            // Clear existing collection so it reseeds fresh every restart
            userVinylRepository.deleteAll(userVinylRepository.findByUserId(demo.getId()));
        }

        // Seed DiscogsReleases and UserVinyls
        seedVinyl(demo, "Meliora", "Ghost", 2015, "Loma Vista", "Rock", "LP", VinylCondition.VERY_GOOD);
        seedVinyl(demo, "Somewhere In Time", "Iron Maiden", 1986, "EMI", "Rock", "LP", VinylCondition.MINT);
        seedVinyl(demo, "Defenders of the Faith", "Judas Priest", 1984, "Columbia", "Rock", "LP", VinylCondition.VERY_GOOD);
        seedVinyl(demo, "Short N' Sweet (Deluxe)", "Sabrina Carpenter", 2025, "Island", "Pop", "LP", VinylCondition.GOOD);
        seedVinyl(demo, "Hit Me Hard And Soft", "Billie Eilish", 2024, "Interscope", "Pop", "LP", VinylCondition.MINT);

        System.out.println("Demo user and vinyls seeded successfully.");
    }

    private void seedVinyl(User user, String title, String artist, Integer year,
                           String label, String genre, String format,
                           VinylCondition condition) {
        DiscogsRelease release = new DiscogsRelease();
        release.setDiscogsId(null); // seeded manually, no Discogs ID
        release.setTitle(title);
        release.setArtist(artist);
        release.setReleaseYear(year);
        release.setLabel(label);
        release.setGenre(genre);
        release.setFormat(format);
        release.setLastSyncedAt(LocalDateTime.now());
        discogsReleaseRepository.save(release);

        UserVinyl userVinyl = new UserVinyl();
        userVinyl.setUser(user);
        userVinyl.setDiscogsRelease(release);
        userVinyl.setCondition(condition);
        userVinyl.setAddedAt(LocalDateTime.now());
        userVinylRepository.save(userVinyl);
    }
}