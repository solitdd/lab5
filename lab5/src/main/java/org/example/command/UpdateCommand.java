package org.example.command;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Organization;
import org.example.models.OrganizationWrapper;
import org.example.models.Product;
import org.example.models.ProductWrapper;
import org.example.repositories.OrganizationRepository;
import org.example.repositories.ProductRepository;
import org.example.utils.JsonUtil;

import java.util.Scanner;

public class UpdateCommand implements Command {
    private final Scanner scanner;
    private final ProductRepository repository;
    private final OrganizationRepository orgRepository;

    public UpdateCommand(Scanner scanner, ProductRepository repository, OrganizationRepository orgRepository) {
        this.scanner = scanner;
        this.repository = repository;
        this.orgRepository = orgRepository;
    }



    @Override
    public void invoke(String params) {
        if(params.contains("product")){
            try {
                Product product = JsonUtil.readFromString(params, new TypeReference<ProductWrapper>(){}).getProduct();

                Organization manufacturer = product.getManufacturer();
                if (manufacturer != null) {
                    Organization existingOrg = orgRepository.getById(manufacturer.getId());
                    if (existingOrg != null) {
                        // подставляем уже существующую организацию
                        orgRepository.update(manufacturer.getId(),manufacturer);
                        product.setManufacturer(existingOrg);

                    } else {
                        // добавляем новую организацию в репозиторий
                        orgRepository.add(manufacturer);
                    }
                }
                repository.update(product.getId(), product);

                System.out.println("Продукт успешно обновлен");
            } catch (Exception e) {
                System.out.println("Ошибка ввода: " + e.getMessage());
            }
        }
        else if(params.contains("organization")){
            try {
                Organization organization = JsonUtil.readFromString(params, new TypeReference<OrganizationWrapper>() {}).getOrganization();
                orgRepository.update(organization.getId(), organization);
            }
            catch (Exception e){
                System.out.println("Ошибка ввода: " + e.getMessage());
            }
        }
    }

    @Override
    public String commandName() {
        return "update";
    }
}