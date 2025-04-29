package burundi.ilucky.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.PurchaseHistoryDTO;
import burundi.ilucky.service.PurchaseHistoryService;
import burundi.ilucky.service.UserService;
import burundi.ilucky.service.VNPayService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private PurchaseHistoryService purchaseHistoryService;

    @Autowired
    private VNPayService vnPayService;
    
    @Value("${client.redirect-url}")
    private String clientRedirectUrl;
    
    @Value("${payment.price-per-turn}")
    private int pricePerTurn;

    @PostMapping("/submit-order")
    public ResponseEntity<?> submitOrder(
            @RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUserName(username);
        
        PurchaseHistoryDTO purchaseHistory = purchaseHistoryService.createPurchaseHistory(user.getId(), orderTotal / pricePerTurn, false);

        // Generate VNPay URL
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl, purchaseHistory.getId());

        log.info("Created VNPay URL for order: {}", orderInfo);

        return ResponseEntity.ok().body(Map.of(
            "paymentUrl", vnpayUrl,
            "redirectType", "URL_REDIRECT"
        ));
    }


    @GetMapping("/vnpay-return")
    public void vnpayReturnHandler(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException {
        String purchaseHistoryIdStr = queryParams.get("purchaseHistoryId");
        if (purchaseHistoryIdStr == null) {
            response.sendRedirect(clientRedirectUrl);
            return;
        }

        UUID purchaseHistoryId;
        try {
            purchaseHistoryId = UUID.fromString(purchaseHistoryIdStr);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format for purchaseHistoryId: {}", purchaseHistoryIdStr);
            response.sendRedirect(clientRedirectUrl);
            return;
        }

        PurchaseHistoryDTO purchaseHistory = purchaseHistoryService.findById(purchaseHistoryId);
        if (Boolean.TRUE.equals(purchaseHistory.getPaymentStatus())) {
            response.sendRedirect(clientRedirectUrl);
            return;
        }

        int paymentStatus = vnPayService.orderReturn(queryParams);
        if (paymentStatus == 1) {
            purchaseHistoryService.updatePaymentStatus(purchaseHistoryId, true);
            log.info("Payment success for PurchaseHistory {}", purchaseHistoryId);
        } else {
            log.warn("Payment failed for PurchaseHistory {}", purchaseHistoryId);
        }

        response.sendRedirect(clientRedirectUrl);
    }
}
