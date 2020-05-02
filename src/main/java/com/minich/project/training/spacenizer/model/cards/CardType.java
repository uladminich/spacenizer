package com.minich.project.training.spacenizer.model.cards;

public enum CardType {

    //TODO i18n, small fiction description for each card
    // ready for play:
    STATION(0L, "station", "Станция", "Начальная постройка. Добыча - 2 КР, расход - 2 КР.", 2, 2, 0, 0, 1, false),
    ROAD(1L, "road", "Дорога", "Увеличивает добычу КР на 1.", 1, 0, 0, 0, 1, false),
    MINE(2L, "mine", "Шахта", "Увеличивает добычу КР на 2, СР на 1. Увеличивает расход КР на 1.", 2, 1, 0, 0, 1, false),
    BAR(3L, "bar", "Бар", "Уменьшает добычу КР на 1, увеличивает расход КР на 1.", -1, 1, 0, 0, 1, false),
    WASTE_RECYCLE(4L, "waste-recycle", "Переработка отходов", "Уменьшает расход КР на 1.", 0, -1, 0, 0, 1, false),
    LABORATORY(7L, "laboratory", "Лаборатория", "Увеличивает расход КР на 1. Добывает по 2 CР.", 0, 1, 2, 0, 1, false),
    // [start] only one per player
    NANO_TECHNOLOGIES(5L, "nano-technologies", "Нано технологии", "Увеличивает добычу КР на 2.", 2, 0, 0, 0, 1, false),
    ADVERSE_TERRAIN(6L, "adverse-terrain", "Плохая местность", "Уменьшает добычу КР на 1, увеличивает расходы на 1", -1, 1, 0, 0, 1, false),
    ROBOTS(8L, "robots", "Роботизация", "Увеличивает показатели карт 'Шахта', 'Дорога', 'Переработка отходов', 'Лаборатория' в два раза.", 0, 0, 0, 0, 2, false),
    // [start] global
    DANGEROUS_WORLD(9L, "dangerous-world", "Враждебный мир", "Уменьшает всю добычу на 2", 0, 2, 0, 2, 1, true),
    // [end]
    // [end]
    // [start] special

    // [end]
    // [start] not implemented:
    RESOURCE_CONVERTER(111L, "resource-converter", "Конвертор ресурсов", "Позволяет переводить КР в СР 1 к 1 при отсутствии КР.", 1, 0, 0, -1, 1, false),
    SECURITY_GUARDS(112L, "security-guard", "Охрана", "Защищает от атак других колоний. Расходует 1 КР в конце раунда и 1 СР при отражении атаки.", 0, -1, 0, -1, 1, false),
    BARRACK(113L, "barrack", "Казарма", "Уеличивает расход КР на 2, СР на 1. Защищает от атак Боевого отряда. В конце раунда добавляет карту действия - Шпион, Боевой отряд, Диверсия", 0, -2, 0, -1, 1, false),
    SPY(114L, "spy", "Шпион", "Одноразовая карта. Играет во время вашего хода. Похищает 3 КР. Расходует 1 СР во время своего действия.", 0, 0, 0, -1, 1, false);
    // [end]

    /**
     * TODO ideas for new cars
     * SIMPLE
     * - SUN_BATTERY - снижает расходы на 1, постройка - 1 СР.
     * - HYDRO_STATION - снижает расходы на 1, постройка - 1 СР.
     * - WIND_GENERATOR - снижает расходы на 1, постройка - 1 СР, не работают в 'Плохая местность'
     *
     * SPECIAL_CARDS:
     * - ASTEROID: уничтожает две карты-постройки (по выбору игрока)
     * -
     *
     * GLOBAL CARDS:
     * - DANGEROUS_WORLD: уменьшает добычу на 2
     * - GLOBAL_WARMING: отключает бонус от всех карт-дорог и шахт(?)
     * - PANDEMIC: уменьшает добычу на 1, уведичивает расходы на 1, длиться 5 кругов
     * - TERRA_FORMING: отключает показатели карт: 'Глобальное потепление', 'Опастная планета', 'Плохая местность'
     * - EARTHQUAKES: случайным образом в конце раунда уничтожает 3 постройки на карте (за исключением станций)
     *
     * COMBINATIONS:
     * - alter energy (sun_buttery, hydrogenation, wind_power)
     * - high tech (nano_tech, robots, laboratory) - доп. 3 СР в конце раунда
     * - manufacture ( mine >= 3) - шахты начинают добывать по 1 СР дополнительно
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
    private boolean isGlobal;

    CardType(long id, String name, String title, String description, int redProduction, int redConsumption, int blueProduction, int blueConsumption, int multiplier, boolean isGlobal) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.redProduction = redProduction;
        this.redConsumption = redConsumption;
        this.blueProduction = blueProduction;
        this.blueConsumption = blueConsumption;
        this.multiplier = multiplier;
        this.isGlobal = isGlobal;
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

    public boolean isGlobal() {
        return isGlobal;
    }
}
