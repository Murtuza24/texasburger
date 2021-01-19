package com.egen.texasburger.controllers;

import java.util.List;
import java.util.Optional;

import com.egen.texasburger.exception.CustomException;
import com.egen.texasburger.models.Menu;
import com.egen.texasburger.models.MenuItem;
import com.egen.texasburger.services.MenuServImpl;
import lombok.extern.log4j.Log4j2;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Murtuza
 */

@RestController
@RequestMapping("/api/categories")
@Log4j2
public class MenuController {

    @Resource(name = "menuService")
    private MenuServImpl menuService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Menu>> getAllMenuCategories() {

        List<Menu> menuList = menuService.getAllMenuCategories();
        if (menuList.isEmpty()) {
            log.info("Menu document is empty");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.info("Total Menu Categories: " + menuList.size());
            menuList.forEach(System.out::println);
            return new ResponseEntity<>(menuList, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{menuId}", method = RequestMethod.GET)
    public ResponseEntity<Optional<Menu>> getMenuCategoryByType(@PathVariable("menuId") String menuId) {
        Optional<Menu> menu = menuService.getMenuItemsByType(menuId);
        if (!menu.isPresent()) {
            log.info(String.format("Menu Id %s Not Found", menuId));
            throw new CustomException("No Such Menu Found");
        } else {
            log.info(String.format("Menu Id %s found", menuId));
            return new ResponseEntity<>(menu, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Optional<MenuItem>> getMenuItemById(@PathVariable("itemId") String itemId) {
        Optional<MenuItem> menuItem = menuService.getMenuItemById(itemId);

        if (!menuItem.isPresent()) {
            log.info(String.format("MenuItemId %s Not Found", itemId));
            throw new CustomException("No Such Menu Item Found");
        } else {
            log.info("Menu Item Found");
            return new ResponseEntity<>(menuItem, HttpStatus.OK);
        }

    }

}
