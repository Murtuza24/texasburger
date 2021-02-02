package com.egen.texasburger.services;

import com.egen.texasburger.models.Menu;
import com.egen.texasburger.models.MenuItem;
import com.egen.texasburger.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

@Service("menuService")
public class MenuServImpl {

    @Autowired
    private MenuRepository menuRepository;

    // gets menus of all restaurants
    public @NotNull(message = "there must be atleast one menu category") Page<Menu> getAllMenuCategories(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    // return all Menu categories of specific restaurant
    public Page<Menu> getMenuByRestaurantId(String restaurantId, Pageable pageable) {
        return menuRepository.findMenuByRestaurantId(restaurantId, pageable);
    }


    // return Menu by menuId
    public Optional<Menu> getMenuItemsByType(@NotNull(message = "menu type cannot be null") @Valid String menuId) {
        return menuRepository.findById(menuId);
    }

    // send menuId and itemId
    public Optional<MenuItem> getMenuItemById(@Valid String menuId, @Valid String itemId) {
        Optional<Menu> menu = menuRepository.findById(menuId);
        List<MenuItem> menuItems;
        if (menu.isPresent()) {
            menuItems = menu.get().getMenuItems();
            return menuItems.stream().
                    filter(p -> p.getItemId().equals(itemId)).
                    findFirst();
        } else {
            return Optional.empty();
        }

    }

    public Menu createNewMenu(@NotNull(message = "menu object cannot be null") Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu addMenuItem(String menuId,
                            @NotNull(message = "menu object cannot be null") MenuItem menuItem
    ) {
        Optional<Menu> menu = menuRepository.findById(menuId);
        List<MenuItem> menuItems;
        if (menu.isPresent()) {
            menuItems = menu.get().getMenuItems();
            menuItems.add(menuItem);
            menu.get().setMenuItems(menuItems);
            return menuRepository.save(menu.get());

        } else {
            return null;
        }
    }

    public void deleteMenu(@NotNull(message = "menu object cannot be null") Menu menu) {
        menuRepository.deleteById(menu.getMenuId());
    }

    public void deleteAllMenu() {
        menuRepository.deleteAll();
    }


}
