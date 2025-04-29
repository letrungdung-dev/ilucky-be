package burundi.ilucky.service;

import org.springframework.stereotype.Service;

import burundi.ilucky.model.dto.GiftDTO;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GiftService {
    
    private static final List<GiftProbability> GIFT_PROBABILITIES = List.of(
        new GiftProbability("10000VND", 1),
        new GiftProbability("1000VND", 2),
        new GiftProbability("500VND", 3),
        new GiftProbability("200VND", 5),
        new GiftProbability("SAMSUNG1", 7),
        new GiftProbability("SAMSUNG2", 7),
        new GiftProbability("SAMSUNG3", 5),
        new GiftProbability("SAMSUNG4", 7),
        new GiftProbability("L", 5),
        new GiftProbability("I", 2),
        new GiftProbability("T", 5),
        new GiftProbability("E", 5),
        new GiftProbability("SHARE", 8),
        new GiftProbability("5STARS", 10),
        new GiftProbability("55STARS", 8),
        new GiftProbability("555STARS", 6),
        new GiftProbability("5555STARS", 5),
        new GiftProbability("UNLUCKY", 9)
    );

    private static final Map<String, GiftDTO> GIFTS = Map.ofEntries(
        Map.entry("10000VND", new GiftDTO("10000VND", "10.000 VND", 10000, "VND")),
        Map.entry("1000VND", new GiftDTO("1000VND", "1.000 VND", 1000, "VND")),
        Map.entry("500VND", new GiftDTO("500VND", "500 VND", 500, "VND")),
        Map.entry("200VND", new GiftDTO("200VND", "200 VND", 200, "VND")),
        Map.entry("SAMSUNG1", new GiftDTO("SAMSUNG1", "Mảnh Samsung 1", 1, "SAMSUNG")),
        Map.entry("SAMSUNG2", new GiftDTO("SAMSUNG2", "Mảnh Samsung 2", 1, "SAMSUNG")),
        Map.entry("SAMSUNG3", new GiftDTO("SAMSUNG3", "Mảnh Samsung 3", 1, "SAMSUNG")),
        Map.entry("SAMSUNG4", new GiftDTO("SAMSUNG4", "Mảnh Samsung 4", 1, "SAMSUNG")),
        Map.entry("L", new GiftDTO("L", "1 Chữ cái \"L\"", 1, "PIECE")),
        Map.entry("I", new GiftDTO("I", "1 Chữ cái \"I\"", 1, "PIECE")),
        Map.entry("T", new GiftDTO("T", "1 Chữ cái \"T\"", 1, "PIECE")),
        Map.entry("E", new GiftDTO("E", "1 Chữ cái \"E\"", 1, "PIECE")),
        Map.entry("SHARE", new GiftDTO("SHARE", "Chia sẻ cho bạn bè để nhận được 1 lượt chơi", 1, "SHARE")),
        Map.entry("5STARS", new GiftDTO("5STARS", "5 Sao", 5, "STARS")),
        Map.entry("55STARS", new GiftDTO("55STARS", "55 Sao", 55, "STARS")),
        Map.entry("555STARS", new GiftDTO("555STARS", "555 Sao", 555, "STARS")),
        Map.entry("5555STARS", new GiftDTO("5555STARS", "5555 Sao", 5555, "STARS")),
        Map.entry("UNLUCKY", new GiftDTO("UNLUCKY", "Chúc bạn may mắn lần sau", 1, "UNLUCKY"))
    );

    private static final int[] CUMULATIVE_WEIGHTS;
    private static final int TOTAL_WEIGHT = 100;

    static {
        CUMULATIVE_WEIGHTS = new int[GIFT_PROBABILITIES.size()];
        int cumulative = 0;
        
        for (int i = 0; i < GIFT_PROBABILITIES.size(); i++) {
            cumulative += GIFT_PROBABILITIES.get(i).probability();
            CUMULATIVE_WEIGHTS[i] = cumulative;
        }
        
        if (cumulative != TOTAL_WEIGHT) {
            throw new IllegalStateException("Tổng tỉ lệ phải bằng 100%");
        }
    }

    public static GiftDTO getRandomGift() {
        int randomValue = ThreadLocalRandom.current().nextInt(TOTAL_WEIGHT);
        
        int index = Arrays.binarySearch(CUMULATIVE_WEIGHTS, randomValue);
        int giftIndex = (index >= 0) ? index + 1 : -index - 1;
        
        return GIFTS.get(GIFT_PROBABILITIES.get(giftIndex).giftId());
    }
    
    public static GiftDTO getGiftById(String giftId) {
        return GIFTS.get(giftId);
    }
    
    public static Map<String, GiftDTO> getAllGifts() {
        return Collections.unmodifiableMap(GIFTS); 
    }

    private record GiftProbability(String giftId, int probability) {}
}