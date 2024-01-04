package mate.academy.webapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.webapp.dto.order.OrderRequestCreateDto;
import mate.academy.webapp.dto.order.OrderRequestUpdateDto;
import mate.academy.webapp.dto.order.OrderResponseDto;
import mate.academy.webapp.dto.orderitem.OrderItemResponseDto;
import mate.academy.webapp.model.User;
import mate.academy.webapp.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing user's orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order", description = "Create new order")
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody @Valid OrderRequestCreateDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get user's order history", description = "Get user's order history")
    public List<OrderResponseDto> getOrdersHistory(Authentication authentication,
                                                   Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAll(user, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping ("/{id}")
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              @RequestBody
                                              @Valid OrderRequestUpdateDto requestUpdateDto) {
        return orderService.updateOrderStatus(id, requestUpdateDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/items")
    @Operation(summary = "Get user's order items from specific order",
            description = "Get user's order items from specific order")
    public List<OrderItemResponseDto> getOrderItemsFromOrder(Authentication authentication,
                                                             @PathVariable Long id,
                                                             Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrderItemsFromOrder(user.getId(), id, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get specific order item from order",
            description = "Get specific order item from order")
    public OrderItemResponseDto getSpecificOrderItemFromOrder(Authentication authentication,
                                                             @PathVariable Long orderId,
                                                              @PathVariable Long itemId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItemFromOrder(user.getId(), orderId, itemId);
    }
}
