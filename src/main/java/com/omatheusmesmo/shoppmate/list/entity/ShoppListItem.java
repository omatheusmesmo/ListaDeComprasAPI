package com.omatheusmesmo.shoppmate.list.entity;

import com.omatheusmesmo.shoppmate.entity.Item;
import com.omatheusmesmo.shoppmate.shared.domain.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "list_items")
@Getter
@Setter
public class ShoppListItem extends BaseAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_list", nullable = false)
    private ShoppList shoppList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item", nullable = false)
    private Item item;

    private Integer quantity;
    private Boolean purchased = false;
}
