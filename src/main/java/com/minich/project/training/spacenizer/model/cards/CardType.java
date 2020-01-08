package com.minich.project.training.spacenizer.model.cards;

public enum CardType {

    STATION("Станция", "Станция. Начальная постройка. Добыча - 2 КР, расход - 2 КР.", 2, 2),
    ROAD("Дорога", "Увеличивает добычу кр на 1.", 1, 0),
    MINE("Шахта", "Увеличивает добычу КР на 2. Увеличивает расход КР на 1.", 2, 1),
    BAR("Бар", "Уменьшает добычу КР на 1, увеличивает расход КР на 1.", -1, 1),
    LABORATORY("Лаборатория", "Увеличивает расход КР на 1. TODO", 0, 1),
    WASTE_RECYCLE("Переработка отходов", "Уменьшает расход КР на 1.", 0, -1);

    private String name;
    private String description;
    private int redProduction;
    private int redConsumption;

    CardType(String name, String description, int redProduction, int redConsumption) {
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
}
