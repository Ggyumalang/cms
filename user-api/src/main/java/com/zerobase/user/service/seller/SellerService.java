package com.zerobase.user.service.seller;

import com.zerobase.user.domain.model.Seller;
import com.zerobase.user.domain.repository.SellerRepository;
import com.zerobase.user.domain.seller.SellerDto;
import com.zerobase.user.exception.CustomException;
import com.zerobase.user.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerDto findByIdAndEmail(Long id, String email) {
        return SellerDto.from(sellerRepository.findByIdAndEmail(id, email)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)));
    }

    public Optional<Seller> findValidSeller(String email, String password) {
        return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email,
            password);
    }
}
