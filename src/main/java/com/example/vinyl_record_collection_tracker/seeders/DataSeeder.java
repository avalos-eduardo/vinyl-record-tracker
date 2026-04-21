package com.example.vinyl_record_collection_tracker.seeders;

import com.example.vinyl_record_collection_tracker.models.User;
import com.example.vinyl_record_collection_tracker.models.Vinyl;
import com.example.vinyl_record_collection_tracker.repositories.UserRepository;
import com.example.vinyl_record_collection_tracker.repositories.VinylRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {
    private final UserRepository userRepository;
    private final VinylRepository vinylRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, VinylRepository vinylRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.vinylRepository = vinylRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User demo = userRepository.findByEmail("demo@vinyltracker.com");
        if (demo == null) {
            demo = new User();
            demo.setActualUsername("Demo User");
            demo.setEmail("demo@vinyltracker.com");
            demo.setPassword(passwordEncoder.encode("demo123"));
            userRepository.save(demo);
        } else {
            vinylRepository.deleteAll(vinylRepository.findByUserId(demo.getId()));
        }

            Vinyl vinyl1 = new Vinyl();
            vinyl1.setTitle("Meliora");
            vinyl1.setArtist("Ghost");
            vinyl1.setReleaseYear(2015);
            vinyl1.setUser(demo);
            vinylRepository.save(vinyl1);

            Vinyl vinyl2 = new Vinyl();
            vinyl2.setTitle("Somewhere In Time");
            vinyl2.setArtist("Iron Maiden");
            vinyl2.setReleaseYear(1986);
            vinyl2.setUser(demo);
            vinylRepository.save(vinyl2);

            Vinyl vinyl3 = new Vinyl();
            vinyl3.setTitle("Defenders of the Faith");
            vinyl3.setArtist("Judas Priest");
            vinyl3.setReleaseYear(1984);
            vinyl3.setUser(demo);
            vinylRepository.save(vinyl3);

            Vinyl vinyl4 = new Vinyl();
            vinyl4.setTitle("Short n' Sweet");
            vinyl4.setArtist("Sabrina Carpenter");
            vinyl4.setReleaseYear(2024);
            vinyl4.setUser(demo);
            vinylRepository.save(vinyl4);

            Vinyl vinyl5 = new Vinyl();
            vinyl5.setTitle("To Pimp a Butterfly");
            vinyl5.setArtist("Kendrick Lamar");
            vinyl5.setReleaseYear(2015);
            vinyl5.setUser(demo);
            vinylRepository.save(vinyl5);

            System.out.println("Demo user and vinyls seeded successfully.");
    }
}
