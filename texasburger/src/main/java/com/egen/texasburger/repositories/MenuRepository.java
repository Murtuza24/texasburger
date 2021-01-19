package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Menu;
import com.egen.texasburger.models.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * @author Murtuza
 */
public interface MenuRepository extends MongoRepository<Menu, String> {

    @Query(value = "{menuItems.itemId:?0}")
    Optional<MenuItem> getMenuItemById(String itemId);

}
