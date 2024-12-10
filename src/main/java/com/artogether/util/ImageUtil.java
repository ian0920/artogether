package com.artogether.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {

    // 壓縮圖片
    public static byte[] compressImage(MultipartFile file, double scale, int quality) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Thumbnails.of(file.getInputStream())
                    .scale(scale) // 縮放比例
                    .outputQuality(quality / 100.0) // 壓縮品質 (0.0 - 1.0)
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        }
    }

    // 壓縮圖片
    public static byte[] compressImage(byte[] imageBytes, double scale, int quality) throws IOException {
        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            Thumbnails.of(inputStream)
                    .scale(scale) // 縮放比例
                    .outputQuality(quality / 100.0) // 壓縮品質 (0.0 - 1.0)
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        }
    }
}
