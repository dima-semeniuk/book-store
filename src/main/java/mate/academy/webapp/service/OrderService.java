package mate.academy.webapp.service;

import java.util.List;
import mate.academy.webapp.dto.order.OrderRequestCreateDto;
import mate.academy.webapp.dto.order.OrderRequestUpdateDto;
import mate.academy.webapp.dto.order.OrderResponseDto;
import mate.academy.webapp.dto.orderitem.OrderItemResponseDto;
import mate.academy.webapp.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderResponseDto> getAll(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long id, OrderRequestUpdateDto requestDto);

    OrderResponseDto createOrder(User user, OrderRequestCreateDto requestDto);

    List<OrderItemResponseDto> getAllOrderItemsFromOrder(Long userId, Long orderId,
                                                         Pageable pageable);

    OrderItemResponseDto findOrderItemFromOrder(Long userId, Long orderId, Long orderItemId);
}
