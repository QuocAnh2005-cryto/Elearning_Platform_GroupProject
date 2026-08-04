package vn.edu.fpt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.fpt.dto.course.CategoryDto;
import vn.edu.fpt.dto.user.UserDto;
import vn.edu.fpt.entity.Cart;
import vn.edu.fpt.entity.User;
import vn.edu.fpt.mapper.DtoMapper;
import vn.edu.fpt.repository.UserRepository;
import vn.edu.fpt.service.CartItemService;
import vn.edu.fpt.service.CartService;
import vn.edu.fpt.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final DtoMapper dtoMapper;

    public GlobalControllerAdvice(CartService cartService, CartItemService cartItemService,
            UserRepository userRepository, CategoryService categoryService, DtoMapper dtoMapper) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.dtoMapper = dtoMapper;
    }

    private boolean isHtmlRequest(HttpServletRequest request) {
        if (request == null) {
            return true;
        }
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/") || uri.startsWith("/api") || uri.contains("/material/url")) {
            return false;
        }
        if (uri.contains(".")) {
            String ext = uri.substring(uri.lastIndexOf("."));
            if (ext.matches("\\.(css|js|png|jpg|jpeg|gif|svg|ico|woff|woff2|ttf|map|json|html)$")) {
                return false;
            }
        }
        return true;
    }

    @ModelAttribute("headerCategories")
    public List<CategoryDto> getHeaderCategories(HttpServletRequest request) {
        if (!isHtmlRequest(request)) {
            return List.of();
        }
        try {
            return categoryService.getActiveParentCategories();
        } catch (Exception ignored) {
        }
        return List.of();
    }

    @ModelAttribute("currentUser")
    public UserDto getCurrentUser(HttpServletRequest request) {
        if (!isHtmlRequest(request)) {
            return null;
        }
        try {
            User user = vn.edu.fpt.util.SecurityUtils.getCurrentUser();
            return user != null ? dtoMapper.toUserDto(user) : null;
        } catch (Exception e) {
            log.error("Error getting current user: ", e);
        }
        return null;
    }

    @ModelAttribute("cartSize")
    public int getCartSize(HttpServletRequest request) {
        if (!isHtmlRequest(request)) {
            return 0;
        }
        try {
            User user = vn.edu.fpt.util.SecurityUtils.getCurrentUser();
            if (user != null) {
                Cart cart = cartService.getOrCreateCartForUser(user);
                return cartItemService.countItemsInCart(cart);
            }
        } catch (Exception e) {
            log.error("Error getting cart size: ", e);
        }
        return 0;
    }

}
