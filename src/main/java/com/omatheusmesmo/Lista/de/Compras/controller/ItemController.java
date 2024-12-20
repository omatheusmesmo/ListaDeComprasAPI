package com.omatheusmesmo.Lista.de.Compras.controller;

import com.omatheusmesmo.Lista.de.Compras.Entity.Item;
import com.omatheusmesmo.Lista.de.Compras.service.ItemService;
import com.omatheusmesmo.Lista.de.Compras.utils.HttpResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @Operation(summary = "Retornar todos os itens")
    @GetMapping("/item")
    public ResponseEntity<List<Item>> retornarTodosItens(){
        try {
            List<Item> itens = itemService.buscarTodos();
            return HttpResponseUtil.ok(itens);
        }catch (Exception e){
            return HttpResponseUtil.internalServerError();
        }
    }

    @Operation(summary =  "Adicionar um novo item")
    @PostMapping("/item")
    public ResponseEntity<Item> adicionarItem(@RequestBody Item item){
        try {
            Item itemAdicionado = itemService.adicionarItem(item);
            return HttpResponseUtil.created(itemAdicionado);
        }catch(IllegalArgumentException e){
            return HttpResponseUtil.badRequest(item);
        }
    }

    @Operation(summary =  "Apagar um item pelo ID")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<Void> removerItem(@PathVariable Long id){
        try {
            itemService.removerItem(id);
            return HttpResponseUtil.noContent();
        }catch(NoSuchElementException exception){
            return HttpResponseUtil.notFound();
        }
    }

    @Operation(summary =  "Editar um item existente")
    @PutMapping("/item")
    public ResponseEntity<Item>  editarItem(@RequestBody Item item){
        try {
            itemService.editarItem(item);
            return HttpResponseUtil.ok(item);
        }catch (NoSuchElementException noSuchElementException){
            return HttpResponseUtil.notFound();
        }catch (IllegalArgumentException illegalArgumentException){
            return HttpResponseUtil.badRequest(item);
        }
    }


}
