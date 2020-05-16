package com.minich.project.training.spacenizer.model.cards;

public enum CardType {

    //TODO i18n, small fiction description for each card
    // ready for play:
    STATION(0L, "station", "Станция", "Начальная постройка. Добыча - 2 КР, расход - 2 КР.", 2, 2, 0, 0, 1, false),
    ROAD(1L, "road", "Дорога", "Увеличивает добычу КР на 1.", 1, 0, 0, 0, 1, false),
    MINE_RED(2L, "mine", "Шахта[КР]", "Увеличивает добычу КР на 2. Увеличивает расход КР на 1.", 2, 1, 0, 0, 1, false),
    MINE_BLUE(22L, "mine", "Шахта[СР]", "Увеличивает добычу CР на 2. Увеличивает расход СР на 1.", 0, 0, 2, 1, 1, false),
    BAR(3L, "bar", "Бар", "Уменьшает добычу КР на 1, увеличивает расход КР на 1.", -1, 1, 0, 0, 1, false),
    WASTE_RECYCLE(4L, "waste-recycle", "Переработка отходов", "Уменьшает расход КР и СР на 1.", 0, -1, 0, -1, 1, false),
    LABORATORY(7L, "laboratory", "Лаборатория", "Увеличивает расход КР на 1. Добывает по 2 CР.", 0, 1, 2, 0, 1, false),
    SUN_BATTERY(13L, "sun-buttery", "Солнечные батареи", "Уменьшает расход КР на 1.", 0, -1, 0, 0, 1, false),
    WIND_GENERATOR(14L, "wind-generator", "Ветрогенератор", "Уменьшает расход СР на 1.", 0, 0, 0, -1, 1, false),
    // [start] only one per player
    NANO_TECHNOLOGIES(5L, "nano-technologies", "Нано технологии", "Увеличивает добычу КР на 2.", 2, 0, 0, 0, 1, false),
    ADVERSE_TERRAIN(6L, "adverse-terrain", "Плохая местность", "Уменьшает добычу КР на 1, увеличивает расходы КР на 1", -1, 1, 0, 0, 1, false),
    ROBOTS(8L, "robots", "Роботизация", "Увеличивает показатели карт 'Шахта', 'Дорога', 'Переработка отходов', 'Лаборатория' в два раза.", 0, 0, 0, 0, 2, false),
    RESOURCE_CONVERTER(15L, "resource-converter", "Конвертор ресурсов", "Преобразование СР в КР идет 1 к 1. Увеличивает расход СР на 1.", 0, 0, 0, 1, 1, false),
    // [end]
    // [start] global
    DANGEROUS_WORLD(9L, "dangerous-world", "Враждебная природа", "Уменьшает всю добычу на 2", -2, 0, -2, 0, 1, false),
    ICE_WORLD(10L, "ice-world", "Ледяная планета", "Увеличивает расход КР на 1, СР на 2.", 0, 1, 0, 2, 1, false),
    RICH_MINERAL_DEPOSIT(11L, "rich-mineral-deposits", "Обширные залежи ресурсов", "Увеличивает всю добычу на 1. ", 1, 0, 1, 0, 1, false), // "Карты 'Шахта' и 'Переработка отходов' приностят дополнительно по 1 КР."
    EARTHQUAKES(12L, "earthquakes", "Повышенная сейсмическая активность", "С некоторой вероятностью в конце раунда может уничтожить до 3 построек (кроме станции).", 0, 0, 0, 0, 1, false),
    // [end]
    // [start] one time card
    HOME_HELP_RESOURCES(16L, "home-help-resources", "Помощь из дома: ресурсы", "Добавляет игроку по 5 КР и СР.", 5, 0, 5, 0, 1, true),
    HOME_HELP_CARD(17L, "home-help-card", "Помощь из дома: карта", "Добавляет игроку одну случайную карту.", 0, 0, 0, 0, 1, true),
    FIRE_DISASTER(18L, "fire-disaster", "Пожар", "Игрок теряет по 1 СР и КР.", 0, 1, 0, 1, 1, true),
    DISEASE_OUTBREAK(19L, "disease-outbreak", "Вспышка болезни", "Игрок теряет 2 КР.", 0, 2, 0, 0, 1, true),
    INDUSTRIAL_ACTION(20L, "industrial-action", "Забастовка рабочих", "Игрок теряет 3 СР.", 0, 0, 0, 3, 1, true),
    NEAR_INVESTIGATION(21L, "near-investigation", "Разведка округи", "Добавляет игроку по 1 КР и 2 СР.", 1, 0, 2, 0, 1, true);
    // [end]
    // [start] not implemented:
//    SECURITY_GUARDS(112L, "security-guard", "Охрана", "Защищает от атак других колоний. Расходует 1 КР в конце раунда и 1 СР при отражении атаки.", 0, -1, 0, -1, 1, false),
//    BARRACK(113L, "barrack", "Казарма", "Уеличивает расход КР на 2, СР на 1. Защищает от атак Боевого отряда. В конце раунда добавляет карту действия - Шпион, Боевой отряд, Диверсия", 0, -2, 0, -1, 1, false),
//    SPY(114L, "spy", "Шпион", "Одноразовая карта. Играет во время вашего хода. Похищает 3 КР. Расходует 1 СР во время своего действия.", 0, 0, 0, -1, 1, false);
    // [end]

    /**
     * TODO ideas for new cars
     * SIMPLE
     *
     * SPECIAL_CARDS:
     * - ASTEROID: уничтожает две карты-постройки (по выбору игрока)
     * -
     *
     * GLOBAL CARDS:
     * - GLOBAL_WARMING: отключает бонус от всех карт-дорог и шахт(?)
     * - PANDEMIC: уменьшает добычу на 1, уведичивает расходы на 1, длиться 5 кругов
     * - TERRA_FORMING: отключает показатели карт: 'Глобальное потепление', 'Опастная планета', 'Плохая местность'
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
    private boolean isOneRound;

    CardType(long id, String name, String title, String description, int redProduction, int redConsumption, int blueProduction, int blueConsumption, int multiplier, boolean isOneRound) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.redProduction = redProduction;
        this.redConsumption = redConsumption;
        this.blueProduction = blueProduction;
        this.blueConsumption = blueConsumption;
        this.multiplier = multiplier;
        this.isOneRound = isOneRound;
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

    public boolean isOneRound() {
        return isOneRound;
    }
}
