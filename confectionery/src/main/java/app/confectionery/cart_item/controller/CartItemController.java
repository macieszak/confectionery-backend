package app.confectionery.cart_item.controller;

import app.confectionery.cart_item.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cart-item/")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;





}
