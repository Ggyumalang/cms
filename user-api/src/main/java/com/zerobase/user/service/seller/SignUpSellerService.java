package com.zerobase.user.service.seller;

import static com.zerobase.user.exception.ErrorCode.ALREADY_VERIFIED_USER;
import static com.zerobase.user.exception.ErrorCode.EXPIRED_VERIFICATION;
import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.user.exception.ErrorCode.WRONG_VERIFICATION_CODE;

import com.zerobase.user.domain.SignUpForm;
import com.zerobase.user.domain.model.Seller;
import com.zerobase.user.domain.repository.SellerRepository;
import com.zerobase.user.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpSellerService {

    private final SellerRepository sellerRepository;

    public Seller signUp(SignUpForm form) {
        return sellerRepository.save(Seller.from(form));
    }

    public boolean isEmailExist(String email) {
        return sellerRepository.findByEmail(email.toLowerCase(Locale.ROOT))
            .isPresent();
    }

    @Transactional
    public void verifyEmail(String email, String code) {
        Seller seller = sellerRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        if (seller.isVerify()) {
            throw new CustomException(ALREADY_VERIFIED_USER);
        }

        if (!code.equals(seller.getVerificationCode())) {
            throw new CustomException(WRONG_VERIFICATION_CODE);
        }

        if (LocalDateTime.now().isAfter(seller.getVerifyExpiredAt())) {
            throw new CustomException(EXPIRED_VERIFICATION);
        }

        seller.setVerify(true);
    }

    @Transactional
    public void changeSellerValidateEmail(Long sellerId,
        String verificationCode) {
        Optional<Seller> optionalSeller = sellerRepository.findById(sellerId);

        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            seller.setVerificationCode(verificationCode);
            seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
        }

        throw new CustomException(NOT_FOUND_USER);
    }
}
