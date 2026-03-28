package com.hotelreservationsystem.domain.room;

public enum RoomType {

    STANDARD(1L, "STANDARD", 1000.0, 2, "Ekonomik ve konforlu temel oda"),
    SUIT(2L, "SUIT", 2500.0, 3, "Oturma alanı + yatak odası bulunan geniş süit"),
    KINGSUIT(3L, "KINGSUIT", 4500.0, 4, "Lüks, büyük, king-size yataklı süit"),
    SUPERIOR(4L, "SUPERIOR", 1500.0, 2, "Standarttan geniş ve daha konforlu oda"),
    HANDICAP(5L, "HANDICAP", 1200.0, 2, "Engelli dostu, erişilebilir oda"),
    CONNECTION(6L, "CONNECTION", 2000.0, 4, "Bağlantılı aile odası"),
    SINGLE(7L, "SINGLE", 800.0, 1, "Tek kişilik ekonomik oda"),
    TWIN(8L, "TWIN", 1100.0, 2, "İki ayrı yataklı oda"),
    TRIPLE(9L, "TRIPLE", 1600.0, 3, "3 kişilik oda");

    private final Long id;
    private final String name;
    private final double basePrice;
    private final int capacity;
    private final String description;

    RoomType(Long id, String name, double basePrice, int capacity, String description) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.capacity = capacity;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    public static RoomType fromId(Long id) {
        for (RoomType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid RoomType id: " + id);
    }

    @Override
    public String toString() {
        return name + " (" + capacity + " kişi, " + basePrice + "TL)";
    }

}
