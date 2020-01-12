package com.minich.project.training.spacenizer.model.cards;

public enum CardType {

    STATION("station", "Станция", "Станция. Начальная постройка. Добыча - 2 КР, расход - 2 КР.", 2, 2),
    ROAD("road", "Дорога", "Увеличивает добычу кр на 1.", 1, 0),
    MINE("mine", "Шахта", "Увеличивает добычу КР на 2. Увеличивает расход КР на 1.", 2, 1),
    BAR("bar", "Бар", "Уменьшает добычу КР на 1, увеличивает расход КР на 1.", -1, 1),
    LABORATORY("laboratory", "Лаборатория", "Увеличивает расход КР на 1. TODO", 0, 1),
    WASTE_RECYCLE("waste-recycle", "Переработка отходов", "Уменьшает расход КР на 1.", 0, -1);

    private String name;
    private String id;
    private String description;
    private int redProduction;
    private int redConsumption;

    CardType(String id, String name, String description, int redProduction, int redConsumption) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.redProduction = redProduction;
        this.redConsumption = redConsumption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRedProduction() {
        return redProduction;
    }

    public void setRedProduction(int redProduction) {
        this.redProduction = redProduction;
    }

    public int getRedConsumption() {
        return redConsumption;
    }

    public void setRedConsumption(int redConsumption) {
        this.redConsumption = redConsumption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
