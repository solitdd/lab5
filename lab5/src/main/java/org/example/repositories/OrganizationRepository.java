package org.example.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.DuplicateOrganizationException;
import org.example.exceptions.ValidationException;
import org.example.models.Organization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrganizationRepository {

    private Map<Long, Organization> organizations = new HashMap<>();
    private long nextOrgId = 1;
    private final ObjectMapper mapper= new ObjectMapper();
    private final String fileName;
    public OrganizationRepository(String fileName){
        this.fileName=fileName;
        try{
            load();
        }catch(Exception e){
            System.out.println("ошибка загрузки");
        }
    }

    public boolean add(Organization org) {
        validateOrganization(org);
        // проверка уникальности id
        if (org.getId() == 0) {
            org.setId(nextOrgId++);
        } else if (organizations.containsKey(org.getId())) {
            throw new DuplicateOrganizationException("Организация с таким id уже существует");
        }
        organizations.put(org.getId(), org);
        // пересчёт nextOrgId
        if (org.getId() >= nextOrgId) nextOrgId = org.getId() + 1;
        return true;
    }

    public Organization getById(long id) {
        return organizations.get(id);
    }

    public void showAll() {
        if (organizations.isEmpty()) {
            throw new ValidationException( "Организации отсутствуют");

        }
        for (Organization org : organizations.values()) {
            System.out.println(org);
        }
    }

    public boolean update(long id, Organization newOrg) {
        if (!organizations.containsKey(id)) return false;
        organizations.put(id, newOrg);
        return true;
    }

    public boolean removeById(long id) {
        return organizations.remove(id) != null;
    }


    public void save() throws IOException {

            String json = mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(organizations);

            FileWriter writer = new FileWriter(fileName);
            writer.write(json);

    }
    public boolean addIfMin(Organization org) {

        if (organizations.isEmpty()) {
            add(org);
            return true;
        }

        Organization min = null;

        for (Organization o : organizations.values()) {
            if (min == null || o.compareTo(min) < 0) {
                min = o;
            }
        }

        if (org.compareTo(min) < 0) {
            add(org);
            return true;
        }

        return false;
    }
    public int removeLower(Organization org) {

        int removed = 0;

        var iterator = organizations.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<Long, Organization> entry = iterator.next();

            if (entry.getValue().compareTo(org) < 0) {
                iterator.remove();
                removed++;
            }
        }

        return removed;
    }
    public int removeGreater(Organization org) {

        int removed = 0;

        var iterator = organizations.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<Long, Organization> entry = iterator.next();

            if (entry.getValue().compareTo(org) > 0) {
                iterator.remove();
                removed++;
            }
        }

        return removed;
    }
    public void load() {

        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            organizations= new HashMap<>();
            return;
        }

        StringBuilder jsonBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                jsonBuilder.append(scanner.nextLine());
            }

            String json = jsonBuilder.toString().trim();

            if (json.isEmpty()) {
                organizations = new HashMap<>();
                return;
            }

            organizations = mapper.readValue(
                    json,
                    new TypeReference<HashMap<Long, Organization>>() {}
            );

        } catch (Exception e) {
            System.out.println("Ошибка загрузки файла, коллекция будет пустой");
            organizations = new HashMap<>();
        }


        long maxId = 0;
        for (long key : organizations.keySet()) {
            if (key > maxId) maxId = key;
        }
        nextOrgId = maxId + 1;
    }


    public void clear(){
        organizations.clear();
    }

    private void validateOrganization(Organization org){
        if (org.getName() == null || org.getName().isBlank()) {
            throw new ValidationException("Organization name не может быть пустым");
        }

        if (org.getEmployeesCount() != null && org.getEmployeesCount() <= 0) {
            throw new ValidationException("employeesCount должен быть > 0");
        }
        if (org.getFullName() != null) {
            boolean exists = organizations.values().stream()
                    .anyMatch(o -> org.getFullName().equals(o.getFullName())
                            && o.getId() != org.getId());

            if (exists) {
                throw new DuplicateOrganizationException("Organization fullName должен быть уникальным");
            }
        }
    }


}
