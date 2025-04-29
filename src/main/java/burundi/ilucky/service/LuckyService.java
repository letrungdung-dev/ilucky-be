package burundi.ilucky.service;

import burundi.ilucky.model.LuckyHistory;
import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.GiftDTO;
import burundi.ilucky.model.dto.LuckyHistoryDTO;
import burundi.ilucky.repository.LuckyHistoryRepository;
import burundi.ilucky.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class LuckyService {

    private final UserRepository userRepository;
    private final LuckyHistoryRepository luckyHistoryRepository;

    public List<LuckyHistory> getHistoriesByUserId(Long userId) {
        return luckyHistoryRepository.findByUserIdOrderByAddTimeDesc(userId);
    }

    @Transactional
    public GiftDTO lucky(User user) {
    	GiftDTO gift = GiftService.getRandomGift();
        processGiftReward(user, gift);
        createLuckyHistory(user, gift);
        updateUserPlayCount(user);
        return gift;
    }

    @Transactional
    public GiftDTO lucky() {
    	GiftDTO gift = GiftService.getRandomGift();
        createLuckyHistory(null, gift);
        return gift;
    }

    public List<LuckyHistoryDTO> convertLuckyHistoriesToDTO(List<LuckyHistory> luckyHistories) {
        return luckyHistories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void processGiftReward(User user, GiftDTO gift) {
        switch (gift.getType()) {
            case "VND":
                user.setTotalVnd(user.getTotalVnd() + gift.getNoItem());
                break;
            case "STARS":
                user.setTotalStar(user.getTotalStar() + gift.getNoItem());
                break;
            default:
                break;
        }
    }

    private void createLuckyHistory(User user, GiftDTO gift) {
        LuckyHistory history = LuckyHistory.builder()
                .giftType(gift.getType())
                .addTime(new Date())
                .giftId(gift.getId())
                .noItem(gift.getNoItem())
                .user(user)
                .build();
        luckyHistoryRepository.save(history);
    }

    private void updateUserPlayCount(User user) {
        user.setTotalPlay(user.getTotalPlay() - 1);
        userRepository.save(user);
    }

    private LuckyHistoryDTO convertToDTO(LuckyHistory history) {
    	GiftDTO gift = GiftService.getGiftById(history.getGiftId());
        return new LuckyHistoryDTO(history.getAddTime(), gift);
    }
    
}