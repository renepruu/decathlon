package ee.rene.decathlon.service;

import org.springframework.stereotype.Service;

@Service
public class PointsCalculator {

    public int calculatePoints(String eventType, double result) {
        return switch (eventType) {
            case "100m Sprint" -> (int) (1000 - (result * 50));  // Kiirem aeg = rohkem punkte

            case "Long Jump" -> (int) (result * 100);  // Pikem hüpe = rohkem punkte

            case "Shot Put" -> (int) (result * 50);   // Pikem heide = rohkem punkte

            case "High Jump" -> (int) (result * 500);  // Kõrgem hüpe = rohkem punkte

            case "Javelin Throw" -> (int) (result * 10);   // Pikem vise = rohkem punkte

            default -> 30;
        };
    }
}