package com.egen.texasburger.services;

import com.egen.texasburger.models.Menu;
import com.egen.texasburger.models.MenuItem;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

public interface MenuService {

    @NotNull(message = "there must be atleast one menu category")
    List<Menu> getAllMenuCategories();

    Optional<Menu> getMenuItemsByType(@NotNull(message = "menu type cannot be null") @Valid String menuType);

    Optional<MenuItem> getMenuItemById(@Valid String itemId);



}
