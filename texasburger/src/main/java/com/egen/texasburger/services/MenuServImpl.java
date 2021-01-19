package com.egen.texasburger.services;

import com.egen.texasburger.models.Menu;
import com.egen.texasburger.models.MenuItem;
import com.egen.texasburger.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Murtuza
 */

@Service("menuService")
public class MenuServImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public @NotNull(message = "there must be atleast one menu category") List<Menu> getAllMenuCategories() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> getMenuItemsByType(@NotNull(message = "menu type cannot be null") @Valid String menuType) {
        return menuRepository.findById(menuType);
    }

    @Override
    public Optional<MenuItem> getMenuItemById(@Valid String itemId) {
        return menuRepository.getMenuItemById(itemId);
    }

    @Override
    public Menu createNewMenu(@NotNull(message = "menu object cannot be null") Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(@NotNull(message = "menu object cannot be null") Menu menu) {
        menuRepository.delete(menu);
    }


}
