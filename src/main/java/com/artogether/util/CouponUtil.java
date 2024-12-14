package com.artogether.util;

import com.artogether.product.prd_coup.PrdCoup;
import com.artogether.product.product.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CouponUtil {

    private static final Map<Integer, Set<String>> couponApplicableProductTypes = new HashMap<>();
    private static final Map<Integer, String> productTypes = new HashMap<>();

    static {
        // Initialize the mappings
        // Example: couponApplicableProductTypes.put(couponId, Set.of("type1", "type2"));
        // Example: productTypes.put(productId, "type1");
    }

    public static Set<String> getApplicableProductTypes(PrdCoup coupon) {
        return couponApplicableProductTypes.get(coupon.getId());
    }

    public static String getProductType(Product product) {
        return productTypes.get(product.getId());
    }
}
