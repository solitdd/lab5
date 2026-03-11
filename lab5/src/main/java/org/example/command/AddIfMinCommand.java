package org.example.command;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.models.Organization;
import org.example.models.OrganizationWrapper;
import org.example.models.Product;
import org.example.models.ProductWrapper;
import org.example.repositories.OrganizationRepository;
import org.example.repositories.ProductRepository;
import org.example.utils.JsonUtil;



public class AddIfMinCommand implements Command{
   private final ProductRepository productRepository;
    private final OrganizationRepository orgRepository;

    public AddIfMinCommand(ProductRepository productRepository, OrganizationRepository orgRepository) {
        this.productRepository = productRepository;
        this.orgRepository = orgRepository;
    }

    @Override
    public void invoke(String params) {
        params = params.trim();
        try {
            if (params.contains("product")) {
                Product product = JsonUtil.readFromString(params, new TypeReference<ProductWrapper>() {}).getProduct();


                Organization manufacturer = product.getManufacturer();
                if (manufacturer != null) {
                    Organization existingOrg = orgRepository.getById(manufacturer.getId());
                    if (existingOrg != null) {
                        product.setManufacturer(existingOrg);
                    } else {
                        orgRepository.add(manufacturer);
                    }
                }


                if (productRepository.addIfMin(product)) {
                    System.out.println("Продукт успешно добавлен (он является минимальным)");
                } else {
                    System.out.println("Продукт не добавлен: в коллекции есть элементы меньше или равные заданному");
                }
            }
            else if (params.contains("organization")) {
                Organization org = JsonUtil.readFromString(params, new TypeReference<OrganizationWrapper>() {}).getOrganization();

                if (orgRepository.add(org)) {
                    System.out.println("Организация успешно добавлена");
                }
            }

        } catch (Exception e) {
            System.out.println("Ошибка при выполнении add_if_min: " + e.getMessage());
        }
    }

    @Override
    public String commandName() {
        return "add_if_min";
    }
}
