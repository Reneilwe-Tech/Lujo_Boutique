package com.example.lujosboutique1;

public class SizeUtils {

    public static List<String> getShoeSizesMen() {
        return Arrays.asList("6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5", "12", "13");
    }

    public static List<String> getShoeSizesWomen() {
        return Arrays.asList("5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11");
    }

    public static List<String> getClothingSizes() {
        return Arrays.asList("XS", "S", "M", "L", "XL", "XXL", "XXXL");
    }

    public static List<String> getJeansSizes() {
        return Arrays.asList("28x30", "29x30", "30x30", "31x30", "32x30", "33x30", "34x30", "36x30");
    }

    public static List<String> getSizesForProduct(SizeAdapter.ProductType productType, String category) {
        switch (productType) {
            case SHOES:
                if (category != null && (category.equalsIgnoreCase("men") || category.contains("men"))) {
                    return getShoeSizesMen();
                } else if (category != null && (category.equalsIgnoreCase("women") || category.contains("women"))) {
                    return getShoeSizesWomen();
                } else {
                    List<String> allSizes = new ArrayList<>();
                    allSizes.addAll(getShoeSizesMen());
                    allSizes.addAll(getShoeSizesWomen());
                    return allSizes;
                }
            case CLOTHING:
                if (category != null && (category.equalsIgnoreCase("jeans") || category.equalsIgnoreCase("pants"))) {
                    return getJeansSizes();
                } else {
                    return getClothingSizes();
                }
            case ACCESSORIES:
                return Arrays.asList("One Size");
            default:
                return getClothingSizes();
        }
    }
}