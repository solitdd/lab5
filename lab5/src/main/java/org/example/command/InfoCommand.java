package org.example.command;

import org.example.repositories.ProductRepository;

public class InfoCommand implements Command {

    private final ProductRepository repository;

    public InfoCommand(ProductRepository repository) {
        this.repository = repository;
    }


    @Override
    public void invoke(String params) {
        System.out.println(repository.getInfo());
    }

    @Override
    public String commandName() {
        return "info";
    }
}
