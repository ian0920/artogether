package com.artogether.product.prd_img;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class PrdImgService  {
    private final PrdImgRepository prdImgRepository;

    public PrdImgService(PrdImgRepository prdImgRepository) {
        this.prdImgRepository = prdImgRepository;
    }

    public void deleteAllByProductId(Integer productId) {
        List<PrdImg> existingImages = prdImgRepository.getPrdImgByProductId(productId);
        if (!existingImages.isEmpty()) {
            prdImgRepository.deleteAll(existingImages);
        }
    }
}
