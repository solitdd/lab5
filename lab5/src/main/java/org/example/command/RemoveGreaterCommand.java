package org.example.command;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Organization;
import org.example.models.OrganizationWrapper;
import org.example.models.Product;
import org.example.models.ProductWrapper;
import org.example.repositories.OrganizationRepository;
import org.example.repositories.ProductRepository;
import org.example.utils.JsonUtil;

public class RemoveGreaterCommand implements Command{
    private final ProductRepository repository;

    private final OrganizationRepository orgRepository;

    public RemoveGreaterCommand(ProductRepository repository, OrganizationRepository orgRepository) {
        this.repository = repository;
        this.orgRepository = orgRepository;
    }

    @Override
    public void invoke(String params) {

        params = params.trim();

        try {

            if (params.contains("product")) {

                Product product =
                        JsonUtil.readFromString(params, new TypeReference<ProductWrapper>() {}).getProduct();

                int removed = repository.removeGreater(product);

                System.out.println("Удалено продуктов: " + removed);
            }

            else if (params.contains("organization")) {

                Organization org =
                        JsonUtil.readFromString(params, new TypeReference<OrganizationWrapper>() {}).getOrganization();

                int removed = orgRepository.removeGreater(org);

                System.out.println("Удалено организаций: " + removed);
            }

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String commandName() {
        return "remove_greater";
    }
}
