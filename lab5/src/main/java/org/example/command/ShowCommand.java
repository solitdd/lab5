package org.example.command;

import org.example.models.Product;
import org.example.repositories.ProductRepository;

public class ShowCommand implements Command {

    private final ProductRepository repository;

    public ShowCommand(ProductRepository repository) {
        this.repository = repository;
    }



    @Override
    public void invoke(String params) {
        if (repository.getAll().isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }

        for (Product p : repository.getAll()) {
            System.out.println(p);
        }
    }

    @Override
    public String commandName() {
        return "show";
    }
}
