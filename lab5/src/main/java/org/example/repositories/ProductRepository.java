package org.example.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.DuplicateProductException;
import org.example.exceptions.NotFoundException;
import org.example.exceptions.ValidationException;
import org.example.models.Product;
import org.example.models.UnitOfMeasure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class ProductRepository {
    private TreeSet<Product> collection=new TreeSet<>();
    private final LocalDateTime initDate = LocalDateTime.now();
    private int nextProductId = 1;
    private final ObjectMapper mapper = new ObjectMapper();
    private final String fileName;
    public ProductRepository(String fileName){
        this.fileName=fileName;
        try{
        load();
        }catch(Exception e){
            System.out.println("ошибка загрузки");
        }
    }
    public boolean add(Product product) {
        validateProduct(product);
        product.setId(nextProductId++);

        product.setCreationDate(Date.from(Instant.now()));

        return collection.add(product);
    }

    public boolean addIfMin(Product product){
        validateProduct(product);

        if (collection.isEmpty()) {
            return add(product);
        }

        if (product.compareTo(collection.first()) < 0) {
            return add(product);
        }

        return false;

    }

    public int removeGreater(Product product) {
        validateProduct(product);

        int removed = 0;

        Iterator<Product> iterator = collection.iterator();

        while (iterator.hasNext()) {
            Product p = iterator.next();

            if (p.compareTo(product) > 0) {
                iterator.remove();
                removed++;
            }
        }

        return removed;
    }

    public String getInfo() {
        return "Тип коллекции: TreeSet\n" +
                "Количество элементов: " + collection.size() + "\n" +
                "Дата инициализации: " + initDate;
    }

    public Product findById(int id){
        for(Product prod : collection){
            if(prod.getId()==id){
                return prod;
            }
        }
        return null;
    }
    public long countGreaterThanUnitOfMeasure(UnitOfMeasure uom) {

        if (uom == null) {
            throw new ValidationException("unitOfMeasure не может быть null");
        }

        long count = 0;

        for (Product product : collection) {

            UnitOfMeasure productUom = product.getUnitOfMeasure();

            if (productUom != null && productUom.compareTo(uom) > 0) {
                count++;
            }
        }

        return count;
    }

    public int removeAllByPartNumber(String partNumber) {

        if (partNumber == null || partNumber.isBlank()) {
            throw new ValidationException("partNumber не может быть пустым");
        }

        int removed = 0;

        Iterator<Product> iterator = collection.iterator();

        while (iterator.hasNext()) {
            Product product = iterator.next();

            if (partNumber.equals(product.getPartNumber())) {
                iterator.remove();
                removed++;
            }
        }

        return removed;
    }

    public boolean remove(int id){
        Product prod = findById(id);
        if(prod == null){
            throw new NotFoundException("Product с таким id не найден");
        }

        return collection.remove(prod);

    }

    public TreeSet<Product> getAll() {
        return collection;
    }

    public boolean update(int id, Product newProduct) {
        Product old = findById(id);
        if (old == null) {
            throw new NotFoundException("Product с таким id не найден");
        }
        newProduct.setId(id);
        newProduct.setCreationDate(old.getCreationDate());
        validateProduct(newProduct);
        collection.remove(old);
        collection.add(newProduct);
        return true;
    }

    public void clear(){
        collection.clear();
    }


public void save() throws IOException {



        String json = mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(collection);

        FileWriter writer = new FileWriter(fileName);
        writer.write(json);

}
public void load() {

    File file = new File(fileName);

    if (!file.exists() || file.length() == 0) {
        collection = new TreeSet<>();
        return;
    }

    StringBuilder jsonBuilder = new StringBuilder();

    try (Scanner scanner = new Scanner(file)) {

        while (scanner.hasNextLine()) {
            jsonBuilder.append(scanner.nextLine());
        }

        String json = jsonBuilder.toString().trim();

        if (json.isEmpty()) {
            collection = new TreeSet<>();
            return;
        }

        collection = mapper.readValue(
                json,
                new TypeReference<TreeSet<Product>>() {}
        );

    } catch (Exception e) {
        System.out.println("Ошибка загрузки файла, коллекция будет пустой");
        collection = new TreeSet<>();
    }


    int maxId = 1;
    for (Product p : collection) {
        if (p.getId() > maxId) maxId = p.getId();
    }

    nextProductId = maxId + 1;
}
    public int removeLower(Product product) {

        validateProduct(product);

        int removed = 0;

        Iterator<Product> iterator = collection.iterator();

        while (iterator.hasNext()) {

            Product p = iterator.next();

            if (p.compareTo(product) < 0) {
                iterator.remove();
                removed++;
            }
        }

        return removed;
    }


    private void validateProduct(Product product) {

        if (product.getName() == null || product.getName().isBlank()) {
            throw new ValidationException("Product name не может быть пустым");
        }

        if (product.getCoordinates() == null) {
            throw new ValidationException("Coordinates не могут быть null");
        }

        if (product.getCoordinates().getX() == null ||
                product.getCoordinates().getX() <= -286) {
            throw new ValidationException("X должен быть > -286");
        }

        if (product.getCoordinates().getY() <= -422) {
            throw new ValidationException("Y должен быть > -422");
        }

        if (product.getPrice() <= 0) {
            throw new ValidationException("Price должен быть > 0");
        }

        if (product.getPartNumber() == null ||
                product.getPartNumber().isBlank()) {
            throw new ValidationException("partNumber не может быть пустым");
        }

        // Проверка уникальности partNumber
        boolean duplicatePart = collection.stream()
                .anyMatch(p ->
                        p.getPartNumber().equals(product.getPartNumber())
                                && p.getId() != product.getId());

        if (duplicatePart) {
            throw new DuplicateProductException("partNumber должен быть уникальным");
        }

        if (product.getUnitOfMeasure() == null) {
            throw new ValidationException("unitOfMeasure не может быть null");
        }

        if (product.getManufacturer() == null) {
            throw new ValidationException("manufacturer не может быть null");
        }
    }
}
