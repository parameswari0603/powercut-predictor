package com.powercut.predictor.scheduler;

import com.powercut.predictor.dto.PredictionResponse;
import com.powercut.predictor.model.Area;
import com.powercut.predictor.model.OutageRisk;
import com.powercut.predictor.model.User;
import com.powercut.predictor.repository.AreaRepository;
import com.powercut.predictor.repository.UserRepository;
import com.powercut.predictor.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AlertScheduler {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(fixedRate = 3600000)
    public void checkAndSendAlerts() {
        System.out.println(
                "🔍 Checking predictions for alerts...");

        List<Area> areas = areaRepository.findAll();

        for (Area area : areas) {
            try {
                PredictionResponse prediction =
                        predictionService
                                .predict(area.getName());

                if (prediction.getRiskLevel()
                        == OutageRisk.HIGH
                        || prediction.getRiskLevel()
                        == OutageRisk.VERY_HIGH) {

                    List<User> users = userRepository
                            .findByAreaNameIgnoreCaseAndAlertsEnabledTrue(
                                    area.getName());

                    for (User user : users) {
                        sendAlert(user, prediction);
                    }
                }
            } catch (Exception e) {
                System.out.println(
                        "Error checking area: "
                                + area.getName());
            }
        }
    }

    private void sendAlert(
            User user,
            PredictionResponse prediction) {
        try {
            SimpleMailMessage message =
                    new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject(
                    "⚡ Power Cut Alert: "
                            + prediction.getArea());
            message.setText(
                    "Hello " + user.getName() + ",\n\n"
                            + "High power cut risk detected "
                            + "in your area!\n\n"
                            + "Area: " + prediction.getArea()
                            + "\nRisk Level: "
                            + prediction.getRiskLevel()
                            + "\nRisk Score: "
                            + prediction.getRiskScore() + "%\n"
                            + "Time Window: "
                            + prediction.getPredictedWindow()
                            + "\n\nReason: "
                            + prediction.getReason()
                            + "\n\nPlease charge your devices "
                            + "and plan accordingly.\n\n"
                            + "— PowerCut Predictor Team");
            mailSender.send(message);
            System.out.println(
                    "✅ Alert sent to: "
                            + user.getEmail());
        } catch (Exception e) {
            System.out.println(
                    "❌ Failed to send email to: "
                            + user.getEmail());
        }
    }
}
