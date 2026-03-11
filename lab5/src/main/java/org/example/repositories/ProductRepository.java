package org.example.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.NotFoundException;
import org.example.models.UnitOfMeasure;
import org.example.exceptions.DuplicateProductException;
import org.example.exceptions.ValidationException;
import org.example.models.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Репозиторий для управления коллекцией объектов {@link Product}.
 * <p>
 * Обеспечивает добавление, удаление, обновление, поиск, сохранение и загрузку коллекции продуктов.
 */
public class ProductRepository {

    /** Коллекция продуктов (отсортированная по имени) */
    private TreeSet<Product> collection = new TreeSet<>();

    /** Дата инициализации репозитория */
    private final LocalDateTime initDate = LocalDateTime.now();

    /** Следующий доступный идентификатор продукта */
    private int nextProductId = 1;

    /** Объект для сериализации/десериализации JSON */
    private final ObjectMapper mapper = new ObjectMapper();

    /** Имя файла для хранения коллекции */
    private final String fileName;

    /**
     * Создает репозиторий и загружает коллекцию из файла.
     *
     * @param fileName имя файла для хранения коллекции
     */
    public ProductRepository(String fileName) {
        this.fileName = fileName;
        try {
            load();
        } catch (Exception e) {
            System.out.println("Ошибка загрузки");
        }
    }

    /**
     * Добавляет продукт в коллекцию с генерацией ID и даты создания.
     *
     * @param product продукт для добавления
     * @return true, если продукт успешно добавлен
     */
    public boolean add(Product product) {
        validateProduct(product);
        product.setId(nextProductId++);
        product.setCreationDate(Date.from(Instant.now()));
        return collection.add(product);
    }

    /**
     * Добавляет продукт в коллекцию, если он меньше наименьшего элемента.
     *
     * @param product продукт для добавления
     * @return true, если продукт был добавлен
     */
    public boolean addIfMin(Product product) {
        validateProduct(product);
        if (collection.isEmpty() || product.compareTo(collection.first()) < 0) {
            return add(product);
        }
        return false;
    }

    /**
     * Удаляет все продукты, которые больше заданного.
     *
     * @param product продукт для сравнения
     * @return количество удаленных элементов
     */
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

    /**
     * Удаляет все продукты, которые меньше заданного.
     *
     * @param product продукт для сравнения
     * @return количество удаленных элементов
     */
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

    /**
     * Возвращает информацию о коллекции.
     *
     * @return информация о типе коллекции, размере и дате инициализации
     */
    public String getInfo() {
        return "Тип коллекции: TreeSet\n" +
                "Количество элементов: " + collection.size() + "\n" +
                "Дата инициализации: " + initDate;
    }

    /**
     * Ищет продукт по идентификатору.
     *
     * @param id идентификатор продукта
     * @return найденный продукт или null
     */
    public Product findById(int id) {
        for (Product prod : collection) {
            if (prod.getId() == id) return prod;
        }
        return null;
    }

    /**
     * Считает количество продуктов с {@link UnitOfMeasure} больше заданного.
     *
     * @param uom единица измерения для сравнения
     * @return количество продуктов
     * @throws ValidationException если uom равен null
     */
    public long countGreaterThanUnitOfMeasure(UnitOfMeasure uom) {
        if (uom == null) throw new ValidationException("unitOfMeasure не может быть null");
        long count = 0;
        for (Product product : collection) {
            UnitOfMeasure productUom = product.getUnitOfMeasure();
            if (productUom != null && productUom.compareTo(uom) > 0) count++;
        }
        return count;
    }

    /**
     * Удаляет все продукты с указанным partNumber.
     *
     * @param partNumber уникальный номер партии
     * @return количество удаленных продуктов
     * @throws ValidationException если partNumber пустой
     */
    public int removeAllByPartNumber(String partNumber) {
        if (partNumber == null || partNumber.isBlank())
            throw new ValidationException("partNumber не может быть пустым");

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

    /**
     * Удаляет продукт по id.
     *
     * @param id идентификатор продукта
     * @return true, если продукт удален
     * @throws NotFoundException если продукт с заданным id не найден
     */
    public boolean remove(int id) {
        Product prod = findById(id);
        if (prod == null) throw new NotFoundException("Product с таким id не найден");
        return collection.remove(prod);
    }

    /**
     * Возвращает все продукты.
     *
     * @return коллекция продуктов
     */
    public TreeSet<Product> getAll() {
        return collection;
    }

    /**
     * Обновляет продукт по id.
     *
     * @param id идентификатор продукта
     * @param newProduct новый объект продукта
     * @return true, если обновление успешно
     * @throws NotFoundException если продукт с заданным id не найден
     */
    public boolean update(int id, Product newProduct) {
        Product old = findById(id);
        if (old == null) throw new NotFoundException("Product с таким id не найден");
        newProduct.setId(id);
        newProduct.setCreationDate(old.getCreationDate());
        validateProduct(newProduct);
        collection.remove(old);
        collection.add(newProduct);
        return true;
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Сохраняет коллекцию в JSON-файл.
     *
     * @throws IOException если произошла ошибка записи
     */
    public void save() throws IOException {
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        }
    }

    /**
     * Загружает коллекцию из JSON-файла.
     * Если файл пуст или отсутствует — создается пустая коллекция.
     */
    public void load() {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            collection = new TreeSet<>();
            return;
        }

        StringBuilder jsonBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) jsonBuilder.append(scanner.nextLine());
            String json = jsonBuilder.toString().trim();
            if (json.isEmpty()) {
                collection = new TreeSet<>();
                return;
            }
            collection = mapper.readValue(json, new TypeReference<TreeSet<Product>>() {});
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

    /**
     * Проверяет корректность продукта перед добавлением или обновлением.
     *
     * @param product продукт для проверки
     * @throws ValidationException если поля некорректны
     * @throws DuplicateProductException если partNumber не уникален
     */
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isBlank())
            throw new ValidationException("Product name не может быть пустым");

        if (product.getCoordinates() == null)
            throw new ValidationException("Coordinates не могут быть null");

        if (product.getCoordinates().getX() == null || product.getCoordinates().getX() <= -286)
            throw new ValidationException("X должен быть > -286");

        if (product.getCoordinates().getY() <= -422)
            throw new ValidationException("Y должен быть > -422");

        if (product.getPrice() <= 0)
            throw new ValidationException("Price должен быть > 0");

        if (product.getPartNumber() == null || product.getPartNumber().isBlank())
            throw new ValidationException("partNumber не может быть пустым");

        boolean duplicatePart = collection.stream()
                .anyMatch(p -> p.getPartNumber().equals(product.getPartNumber()) && p.getId() != product.getId());
        if (duplicatePart) throw new DuplicateProductException("partNumber должен быть уникальным");

        if (product.getUnitOfMeasure() == null)
            throw new ValidationException("unitOfMeasure не может быть null");

        if (product.getManufacturer() == null)
            throw new ValidationException("manufacturer не может быть null");
    }
}