package de.tum.in.ase.eist.service;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {
    // Do not change this
    private final List<Person> persons;

    public PersonService() {
        this.persons = new ArrayList<>();
    }

    public Person savePerson(Person person) {
        var optionalPerson = persons.stream().filter(existingPerson
                -> existingPerson.getId().equals(person.getId())).findFirst();
        if (person == null) {
            // Handle the case when the person object is null
            throw new IllegalArgumentException("Person object cannot be null");
        }
        if (optionalPerson.isEmpty()) {
            person.setId(UUID.randomUUID());
            persons.add(person);
            return person;
        } else {
            var existingPerson = optionalPerson.get();
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setBirthday(person.getBirthday());
//            existingPerson.setId(person.getId());
            return existingPerson;
        }
    }

    public void deletePerson(UUID personId) {
        this.persons.removeIf(person -> person.getId().equals(personId));
    }

    public List<Person> getAllPersons(PersonSortingOptions sortingOptions) {
        // TODO Part 3: Add sorting here
        switch (sortingOptions.getSortField()) {
            case ID:
                if (sortingOptions.getSortingOrder() == PersonSortingOptions.SortingOrder.ASCENDING) {
                    persons.sort(Comparator.comparing(Person::getId));
                } else {
                    persons.sort(Comparator.comparing(Person::getId).reversed());
                }
                break;
            case BIRTHDAY:
                if (sortingOptions.getSortingOrder() == PersonSortingOptions.SortingOrder.ASCENDING) {
                    persons.sort(Comparator.comparing(Person::getBirthday));
                } else {
                    persons.sort(Comparator.comparing(Person::getBirthday).reversed());
                }
                break;
            case LAST_NAME:
                if (sortingOptions.getSortingOrder() == PersonSortingOptions.SortingOrder.ASCENDING) {
                    persons.sort(Comparator.comparing(Person::getLastName));
                } else {
                    persons.sort(Comparator.comparing(Person::getLastName).reversed());
                }
                break;
            case FIRST_NAME:
                if (sortingOptions.getSortingOrder() == PersonSortingOptions.SortingOrder.ASCENDING) {
                    persons.sort(Comparator.comparing(Person::getFirstName));
                } else {
                    persons.sort(Comparator.comparing(Person::getFirstName).reversed());
                }
                break;
            default:
                break;
        }
        return persons;
    }
}
