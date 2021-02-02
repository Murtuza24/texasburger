package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Menu;
import com.egen.texasburger.models.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

public interface MenuRepository extends MongoRepository<Menu, String> {

    @Query(value = "{'restaurantId_fk':?0}")
    Page<Menu> findMenuByRestaurantId(String restaurantId, Pageable pageable);

    @Modifying
    @Query(value = "{'menuId':?0}, {$push: {'menuItems':?1}}")
    MenuItem saveMenuItem(String menuId, MenuItem menuItem);

}
