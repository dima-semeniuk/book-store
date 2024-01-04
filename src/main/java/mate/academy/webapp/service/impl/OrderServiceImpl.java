package mate.academy.webapp.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import mate.academy.webapp.dto.order.OrderRequestCreateDto;
import mate.academy.webapp.dto.order.OrderRequestUpdateDto;
import mate.academy.webapp.dto.order.OrderResponseDto;
import mate.academy.webapp.dto.orderitem.OrderItemResponseDto;
import mate.academy.webapp.exception.EntityNotFoundException;
import mate.academy.webapp.mapper.OrderItemMapper;
import mate.academy.webapp.mapper.OrderMapper;
import mate.academy.webapp.model.Order;
import mate.academy.webapp.model.OrderItem;
import mate.academy.webapp.model.ShoppingCart;
import mate.academy.webapp.model.User;
import mate.academy.webapp.repository.CartItemRepository;
import mate.academy.webapp.repository.OrderItemRepository;
import mate.academy.webapp.repository.OrderRepository;
import mate.academy.webapp.repository.ShoppingCartRepository;
import mate.academy.webapp.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemResponseDto> getAllOrderItemsFromOrder(Long userId, Long orderId,
                                                                Pageable pageable) {
        orderRepository.findOrderByIdAndUserId(orderId, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId)
        );
        return orderItemRepository.getAllByOrderId(orderId, pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto findOrderItemFromOrder(Long userId,
                                                       Long orderId, Long orderItemId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + orderId)
        );
        return orderItemMapper.toDto(order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find orderItem by id: "
                                + orderItemId)));
    }

    @Override
    public List<OrderResponseDto> getAll(User user, Pageable pageable) {
        return orderRepository.getAllByUserId(user.getId(), pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long id, OrderRequestUpdateDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + id)
        );
        order.setStatus(requestDto.status());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(User user, OrderRequestCreateDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user id: "
                        + user.getId()));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Shopping cart is empty!");
        }

        BigDecimal total = shoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

        Order savedOrder = orderRepository
                .save(newOrder(user, requestDto.shippingAddress(), total));

        List<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> new OrderItem(savedOrder, cartItem.getBook(),
                        cartItem.getQuantity(), cartItem.getBook().getPrice()))
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(new HashSet<>(orderItems));
        cartItemRepository.deleteAll(shoppingCart.getCartItems());

        return orderMapper.toDto(savedOrder);
    }

    private Order newOrder(User user, String shippingAddress, BigDecimal total) {
        Order order = new Order();
        order.setTotal(total);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(shippingAddress);
        return order;
    }
}
