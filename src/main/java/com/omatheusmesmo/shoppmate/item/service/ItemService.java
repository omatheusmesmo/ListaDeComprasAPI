package com.omatheusmesmo.shoppmate.item.service;

import com.omatheusmesmo.shoppmate.category.service.CategoryService;
import com.omatheusmesmo.shoppmate.item.entity.Item;
import com.omatheusmesmo.shoppmate.item.repository.ItemRepository;
import com.omatheusmesmo.shoppmate.shared.service.AuditService;
import com.omatheusmesmo.shoppmate.unit.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private AuditService auditService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CategoryService categoryService;

    public Item addItem(Item item) {
        isItemValid(item);
        auditService.setAuditData(item,true);
        itemRepository.save(item);
        return item;
    }

    public void isItemValid(Item item) {
        categoryService.isCategoryValid(item.getCategory());
        unitService.isUnitValid(item.getUnit());
        item.checkName();
    }

    public Optional<Item> findItem(Item item) {
        Optional<Item> foundItem = itemRepository.findById(item.getId());
        if (foundItem.isPresent()) {
            return foundItem;
        } else {
            throw new NoSuchElementException("Item not found");
        }
    }

    public Optional<Item> findItemById(Long id) {
        Optional<Item> foundItem = itemRepository.findById(id);
        if (foundItem.isPresent()) {
            return foundItem;
        } else {
            throw new NoSuchElementException("Item not found");
        }
    }
    //TODO remove item by item
    public void removeItem(Long id) {
        findItemById(id);
        //auditService.softDelete(item);
        itemRepository.deleteById(id);
    }

    public Item editItem(Item item) {
        findItemById(item.getId());
        isItemValid(item);
        auditService.setAuditData(item, false);
        itemRepository.save(item);
        return item;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
