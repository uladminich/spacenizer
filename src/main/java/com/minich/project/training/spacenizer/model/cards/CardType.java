package com.minich.project.training.spacenizer.model.cards;

public enum CardType {

    //TODO i18n, small fiction description for each card
    // ready for play:
    STATION(0L, "station", "Станция", "СТАНЦИЯ. Начальная постройка. Добыча - 2 КР, расход - 2 КР.", 2, 2, 0, 0, 1),
    ROAD(1L, "road", "Дорога", "ДОРОГА. Увеличивает добычу КР на 1.", 1, 0, 0, 0, 1),
    MINE(2L, "mine", "Шахта", "ШАХТА. Увеличивает добычу КР на 2, СР на 1. Увеличивает расход КР на 1.", 2, 1, 0, 0, 1),
    BAR(3L, "bar", "Бар", "БАР. Уменьшает добычу КР на 1, увеличивает расход КР на 1.", -1, 1, 0, 0, 1),
    WASTE_RECYCLE(4L, "waste-recycle", "Переработка отходов", "ПЕРЕРАБОТКА ОТХОДОВ. Уменьшает расход КР на 1.", 0, -1, 2, 0, 1),
    NANO_TECHNOLOGIES(5L, "nano-technologies", "Нано технологии", "НАНО ТЕХНОЛОГИИ. Увеличивает добычу КР на 2.", 2, 0, 0, 0, 1),// only one per player
    ADVERSE_TERRAIN(6L, "adverse-terrain", "Плохая местность", "НЕБЛАГОПРИЯТНАЯ МЕСТНОСТЬ. Уменьшает добычу КР на 1, увеличивает расходы на 1", -1, 1, 0, 0, 1),  // only one per player
    LABORATORY(7L, "laboratory", "Лаборатория", "ЛАБОРАТОРИЯ. Увеличивает расход КР на 1. Добывает по 2 CР.", 0, 1, 2, 0, 1), // TODO blue resource logic is not implemented
    ROBOTS(8L, "robots", "Роботизация", "РОБОТИЗАЦИЯ. Увеличивает показатели карт 'Шахта', 'Дорога', 'Переработка отходов', 'Лаборатория' в два раза.", 0, 0, 0, 0, 2),  // only one per player TODO blue resource logic (for laboratory) is not implemented
    // not implemented:
    RESOURCE_CONVERTER(8L, "resource-converter", "Конвертор ресурсов", "КОНВЕРТОР РЕСУРСОВ. Позволяет переводить КР в СР 1 к 1 и в конце раунда, а не только когда нет КР", 1, 0, 0, -1, 1),
    SECURITY_GUARDS(9L, "security-guard", "Охрана", "ОХРАНА. Защищает от атак других колоний. Расходует 1 КР в конце раунда и 1 СР при отражении атаки.", 0, -1, 0, -1, 1),
    BARRACK(10L, "barrack", "Казарма", "КАЗАРМА. Уеличивает расход КР на 2, СР на 1. Защищает от атак Боевого отряда. В конце раунда добавляет карту действия - Шпион, Боевой отряд, Диверсия", 0, -2, 0, -1, 1),
    SPY(11L, "spy", "Шпион", "ШПИОН. Одноразовая карта. Играет во время вашего хода. Похищает 3 КР. Расходует 1 СР во время своего действия.", 0, 0, 0, -1, 1);

    /**
     * TODO ideas for new cars
     * SPECIAL_CARDS:
     * - ASTEROID: уничтожает две карты-постройки (по выбору игрока)
     * -
     * <p>
     * GLOBAL CARDS:
     * - DANGEROUS_WORLD: влияет на всех игроков, уменьшает добычу на 1, уведичивает расходы на 1
     * - GLOBAL_WARMING: отключает бонус от всех карт-дорог и шахт(?)
     * - TERRA_FORMING: отключает показатели карт: 'Глобальное потепление', 'Опастная планета', 'Плохая местность'
     * - EARTHQUAKE: случайным образом в конце раунда уничтожает 3 постройки на карте (за исключением станций)
     * -
     */
    private long id;
    private String name;
    private String title;
    private String description;
    private int redProduction;
    private int redConsumption;
    private int blueProduction;
    private int blueConsumption;
    private int multiplier;

    CardType(long id, String name, String title, String description, int redProduction, int redConsumption, int blueProduction, int blueConsumption, int multiplier) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.redProduction = redProduction;
        this.redConsumption = redConsumption;
        this.blueProduction = blueProduction;
        this.blueConsumption = blueConsumption;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRedProduction() {
        return redProduction;
    }

    public int getRedConsumption() {
        return redConsumption;
    }

    public int getBlueProduction() {
        return blueProduction;
    }

    public int getBlueConsumption() {
        return blueConsumption;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
