package com.nashtech.ecommerce.controller;

import com.nashtech.ecommerce.domain.Product;
import com.nashtech.ecommerce.dto.CartDetailDTO;
import com.nashtech.ecommerce.security.SecurityUtils;
import com.nashtech.ecommerce.service.CartDetailService;
import com.nashtech.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-detail-management")
public class CartDetailController {
    private final CartDetailService cartDetailService;
    public CartDetailController(CartDetailService cartDetailService) {
        this.cartDetailService = cartDetailService;
    }

    @Autowired
    private CustomerService customerService;

    //TODO: set up cart detail security
    @GetMapping("/details/{id}/product")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public Product getProductFromCartDetailId(@PathVariable("id") int id) {
        return cartDetailService.getProductByCartDetailId(id);
    }

    //for add/update
    @PostMapping("/details")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public CartDetailDTO addCartDetail(@RequestBody CartDetailDTO cartDetailDTO) {
        return cartDetailService.addCartDetail(cartDetailDTO);
    }

    //remove individual cart detail
    @DeleteMapping("/details")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'ADMIN')")
    public void removeCartDetail(@RequestParam int id){
        cartDetailService.removeCartDetail(id);
    }
}
