package com.egen.texasburger.controllers;

import com.egen.texasburger.exception.CustomException;
import com.egen.texasburger.models.Menu;
import com.egen.texasburger.models.MenuItem;
import com.egen.texasburger.services.MenuServImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author Murtuza
 */

@RestControllerAdvice
@RestController
@RequestMapping("/api")
@Log4j2
@Api(value = "Menu/MenuItems API")
public class MenuController {

    @Resource(name = "menuService")
    private MenuServImpl menuService;

    @GetMapping(value = "/menu")
    @ApiOperation(value = "Get All categories of Menu", notes = "Get All categories of Menu / filter by restaurant id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Map<String, Object>> getAllMenuCategories(
            @RequestParam(value = "restaurantId", required = false) String restaurantId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "4") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Menu> menuPage = null;
            List<Menu> menuList;

            if (restaurantId == null) {
                log.info("getting menu of all restaurants");
                menuPage = menuService.getAllMenuCategories(pageable);

            } else {
                log.info("getting menu by restaurant id");
                menuPage = menuService.getMenuByRestaurantId(restaurantId, pageable);
            }

            menuList = menuPage.getContent();
            if (menuList.isEmpty()) {
                log.info("Menu document is empty");
                throw new CustomException("No Content in Menu");
            } else {
                log.info("Total Menu Categories: " + menuList.size());

                Map<String, Object> response = new HashMap<>();
                response.put("Menu", menuList);
                response.put("restaurantId", restaurantId);
                response.put("currentPage", menuPage.getNumber());
                response.put("totalItems", menuPage.getTotalElements());
                response.put("totalPages", menuPage.getTotalPages());

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new CustomException("Something went wrong on Server side!!");
        }
    }


    @GetMapping(value = "/menu/{menuId}")
    @ApiOperation(value = "Get a particular menu category by Id", notes = "Get a particular menu category by Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Optional<Menu>> getMenuCategoryByType(@PathVariable("menuId") String menuId) {
        try {
            Optional<Menu> menu = menuService.getMenuItemsByType(menuId);
            log.info("menu: {}", menu);
            if (!menu.isPresent()) {
                log.info(String.format("Menu Id %s Not Found", menuId));
                throw new CustomException("No Such Menu Found");
            } else {
                log.info(String.format("Menu Id %s found", menuId));
                return new ResponseEntity<>(menu, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("exception raised");
            throw new CustomException("Something went wrong on server side!");
        }
    }

    @GetMapping(value = "/menu/item")
    @ApiOperation(value = "Get a particular menu item by Id", notes = "Get a particular menu item by Id")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No content found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Optional<MenuItem>> getMenuItemById(@RequestParam(value = "menuId") String menuId,
                                                              @RequestParam(value = "itemId") String itemId) {
        try {
            Optional<MenuItem> menuItem = menuService.getMenuItemById(menuId, itemId);
            if (!menuItem.isPresent()) {
                log.info(String.format("MenuItemId %s Not Found", itemId));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.info("Menu Item Found: " + menuItem);
                return new ResponseEntity<>(menuItem, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("exception in getting menu item by id: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/admin/menu/addMenu", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new Menu Category", notes = "Create a new Menu Category")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
        try {
            Menu newMenu = menuService.createNewMenu(menu);
            if (menu != null) {
                return new ResponseEntity<>(newMenu, HttpStatus.CREATED);
            } else {
                throw new CustomException("menu not created");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/admin/menu/addMenuItem", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add a new Item to a Menu Category", notes = "Adds a new item/dish to the existing menu category")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Optional<Menu>> addMenuItem(@RequestParam String menuId, @RequestBody MenuItem menuItem
    ) {
        try {
            Optional<Menu> newMenu = Optional.ofNullable(menuService.addMenuItem(menuId, menuItem));
            if (newMenu.isPresent()) {
                log.info("menuItem added");
                return new ResponseEntity<>(newMenu, HttpStatus.CREATED);
            } else {
                log.info("menu id not found");
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            log.info("error adding menu item: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/admin/menu/updateMenu", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updating Menu", notes = "Updating Menu")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<Menu> updateMenu(@RequestBody Menu menu) {
        try {
            Optional<Menu> menuData = menuService.getMenuItemsByType(menu.getMenuId());

            // update menu and menu items
            if (menuData.isPresent()) {
                Menu _menu = menuData.get();
                _menu.setMenuType(menu.getMenuType());
                _menu.setMenuItems(menu.getMenuItems());
                return new ResponseEntity<>(menuService.createNewMenu(menu), HttpStatus.OK);
            } else {
                log.error("no such menu found! Please try using add new menu.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(value = "/admin/menu/deleteMenu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Deleting a Menu Category", notes = "Deleting a Menu Category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<HttpStatus> deleteMenu(@RequestBody Menu menu) {

        try {
            menuService.deleteMenu(menu);
            log.info("menuId {} deleted", menu.getMenuId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.info("error in deleting menu: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/admin/menu/deleteAllMenu")
    @ApiOperation(value = "Deleting all Menu Categories", notes = "Deleting all Menu Categories")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No data found"),
            @ApiResponse(code = 500, message = "Internal Server error")})
    public ResponseEntity<HttpStatus> deleteAllMenu() {
        try {
            menuService.deleteAllMenu();
            log.info("deleted all menu!!");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.info("error in deleting all menu: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
