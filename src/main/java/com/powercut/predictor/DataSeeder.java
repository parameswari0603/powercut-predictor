package com.powercut.predictor;

import com.powercut.predictor.model.Area;
import com.powercut.predictor.model.OutageReport;
import com.powercut.predictor.repository.AreaRepository;
import com.powercut.predictor.repository.OutageReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private OutageReportRepository reportRepository;

    @Override
    public void run(String... args) {

        // Anna Nagar
        Area annaNagar = new Area();
        annaNagar.setName("Anna Nagar");
        annaNagar.setCity("Chennai");
        annaNagar.setPincode("600040");
        annaNagar.setLatitude(13.0850);
        annaNagar.setLongitude(80.2101);
        annaNagar = areaRepository.save(annaNagar);

        // T Nagar
        Area tNagar = new Area();
        tNagar.setName("T Nagar");
        tNagar.setCity("Chennai");
        tNagar.setPincode("600017");
        tNagar.setLatitude(13.0418);
        tNagar.setLongitude(80.2341);
        tNagar = areaRepository.save(tNagar);

        // Velachery
        Area velachery = new Area();
        velachery.setName("Velachery");
        velachery.setCity("Chennai");
        velachery.setPincode("600042");
        velachery.setLatitude(12.9815);
        velachery.setLongitude(80.2180);
        velachery = areaRepository.save(velachery);

        // Adyar
        Area adyar = new Area();
        adyar.setName("Adyar");
        adyar.setCity("Chennai");
        adyar.setPincode("600020");
        adyar.setLatitude(13.0012);
        adyar.setLongitude(80.2565);
        adyar = areaRepository.save(adyar);

        // Tambaram
        Area tambaram = new Area();
        tambaram.setName("Tambaram");
        tambaram.setCity("Chennai");
        tambaram.setPincode("600045");
        tambaram.setLatitude(12.9249);
        tambaram.setLongitude(80.1000);
        tambaram = areaRepository.save(tambaram);

        // Seed reports for Anna Nagar
        LocalDateTime base =
                LocalDateTime.now().minusDays(30);
        for (int i = 0; i < 10; i++) {
            OutageReport r = new OutageReport();
            r.setArea(annaNagar);
            r.setStartTime(base.plusDays(i * 2)
                    .withHour(18).withMinute(30));
            r.setTemperatureAtTime(36.0 + (i % 3));
            r.setDayType("WEEKDAY");
            r.setReportedHour(18);
            r.setReportedMonth(6);
            reportRepository.save(r);
        }

        // Seed reports for T Nagar
        for (int i = 0; i < 5; i++) {
            OutageReport r = new OutageReport();
            r.setArea(tNagar);
            r.setStartTime(base.plusDays(i * 3)
                    .withHour(20).withMinute(0));
            r.setTemperatureAtTime(35.0 + (i % 2));
            r.setDayType("WEEKDAY");
            r.setReportedHour(20);
            r.setReportedMonth(6);
            reportRepository.save(r);
        }

        // Seed reports for Velachery
        for (int i = 0; i < 7; i++) {
            OutageReport r = new OutageReport();
            r.setArea(velachery);
            r.setStartTime(base.plusDays(i)
                    .withHour(19).withMinute(0));
            r.setTemperatureAtTime(37.0 + (i % 2));
            r.setDayType("WEEKDAY");
            r.setReportedHour(19);
            r.setReportedMonth(6);
            reportRepository.save(r);
        }

        System.out.println(
                "✅ Sample data seeded successfully");
    }
}
