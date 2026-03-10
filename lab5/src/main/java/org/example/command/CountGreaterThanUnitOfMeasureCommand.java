package org.example.command;

import org.example.models.UnitOfMeasure;
import org.example.repositories.ProductRepository;

public class CountGreaterThanUnitOfMeasureCommand implements Command{
    private final ProductRepository repository;

    public CountGreaterThanUnitOfMeasureCommand(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void invoke(String params) {
        try {

            UnitOfMeasure uom = UnitOfMeasure.valueOf(params.trim().toUpperCase());

            long count = repository.countGreaterThanUnitOfMeasure(uom);

            System.out.println("Количество элементов: " + count);

        } catch (IllegalArgumentException e) {
            System.out.println("Неверное значение unitOfMeasure");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

    }

    @Override
    public String commandName() {
        return "count_greater_than_unit_of_measure";
    }

}



