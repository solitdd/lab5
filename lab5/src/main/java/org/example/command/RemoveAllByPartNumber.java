package org.example.command;

import org.example.repositories.ProductRepository;

public class RemoveAllByPartNumber implements Command{
    private final ProductRepository repository;

    public RemoveAllByPartNumber(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void invoke(String params) {
        try {

            int removed = repository.removeAllByPartNumber(params.trim());

            System.out.println("Удалено элементов: " + removed);

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String commandName() {
        return "remove_all_by_part_number";
    }
}
