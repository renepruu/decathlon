package ee.rene.decathlon.service;

import org.springframework.stereotype.Service;

@Service
public class PointsCalculator {

    public int calculatePoints(String eventType, double result) {
        switch (eventType) {
            case "100m Sprint":
                return (int) (1000 - (result * 50));  // Kiirem aeg = rohkem punkte

            case "Long Jump":
                return (int) (result * 100);  // Pikem hüpe = rohkem punkte

            case "Shot Put":
                return (int) (result * 50);   // Pikem heide = rohkem punkte

            case "High Jump":
                return (int) (result * 500);  // Kõrgem hüpe = rohkem punkte

            case "Javelin Throw":
                return (int) (result * 10);   // Pikem vise = rohkem punkte

            default:
                return 30;
        }
    }
}