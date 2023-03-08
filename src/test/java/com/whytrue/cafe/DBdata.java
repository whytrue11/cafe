package com.whytrue.cafe;

import com.whytrue.cafe.entity.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBdata {
  public static final List<Role> roles;
  static {
    roles = new ArrayList<>(3);
    roles.add(new Role(1L, "ROLE_USER"));
    roles.add(new Role(2L, "ROLE_EMPLOYEE"));
    roles.add(new Role(3L, "ROLE_ADMIN"));
  }

  public static final List<User> users;
  static {
    users = new ArrayList<>(3);
    users.add(new User(UUID.fromString("11111111-1111-1111-1111-111111111111"), "USER",
        "$2a$10$FDUgIaQPEVjt1rCwr/rjCO/JZShkfUBvrmpaJlhWNLCS8UH8jLu5y", "dev_user", Collections.singleton(roles.get(0))));
    users.add(new User(UUID.fromString("22222222-2222-2222-2222-222222222222"), "EMPLOYEE",
        "$2a$10$KOos0FtYUQi1sqi.gXTJZedZbrR3aUbbTDAijN0r8kspKPKbWvM4K", "dev_employee", Collections.singleton(roles.get(1))));
    users.add(new User(UUID.fromString("33333333-3333-3333-3333-333333333333"), "ADMIN",
        "$2a$10$ylqKA7G5d0gYrM.lIUwc9eIdg3vpza1HbQQHwSoSxD.9spyaO9jWm", "dev_admin", Collections.singleton(roles.get(2))));
  }

  public static final List<ProductCategory> categories;
  static {
    categories = new ArrayList<>(3);
    categories.add(new ProductCategory(1L, "DEFAULT"));
    categories.add(new ProductCategory(2L, "FOOD"));
    categories.add(new ProductCategory(3L, "DRINK"));
  }

  public static final List<Product> products;
  static {
    products = new ArrayList<>(6);
    products.add(new Product(1L, "Salad", BigDecimal.valueOf(24000, 2),
        null, null, false, categories.get(1)));
    products.add(new Product(2L, "Steak", BigDecimal.valueOf(72000, 2),
        null, null, true, categories.get(1)));
    products.add(new Product(3L, "Red caviar", BigDecimal.valueOf(70000, 2),
        10L, null, true, categories.get(1)));
    products.add(new Product(4L, "Tea", BigDecimal.valueOf(6000, 2),
        null, null, true, categories.get(2)));
    products.add(new Product(5L, "Coffee", BigDecimal.valueOf(11000, 2),
        null, null, true, categories.get(2)));
    products.add(new Product(6L, "Branded souvenir", BigDecimal.valueOf(100000, 2),
        5L, null, true, categories.get(0)));
    products.add(new Product(7L, "Sugar", BigDecimal.valueOf(1000, 2),
        0L, null, false, categories.get(1)));
  }

  public static final List<OrderStatus> statuses;
  static {
    statuses = new ArrayList<>(3);
    statuses.add(new OrderStatus(1L, "DEFAULT"));
    statuses.add(new OrderStatus(2L, "PAID"));
    statuses.add(new OrderStatus(3L, "SUCCESS"));
    statuses.add(new OrderStatus(4L, "REJECTED"));
  }

  public static final List<Order> orders;
  static {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
    String dateInString = "2023-02-24 14:20:10.144";
    Date date = null;
    try {
      date = formatter.parse(dateInString);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    orders = new ArrayList<>(4);
    orders.add(new Order(1L, date, 1L, null, DBdata.users.get(0), DBdata.products.get(5), DBdata.statuses.get(0)));
    orders.add(new Order(2L, date, 1L, null, DBdata.users.get(0), DBdata.products.get(5), DBdata.statuses.get(1)));
    orders.add(new Order(3L, date, 1L, null, DBdata.users.get(0), DBdata.products.get(5), DBdata.statuses.get(2)));
    orders.add(new Order(4L, date, 1L, null, DBdata.users.get(0), DBdata.products.get(5), DBdata.statuses.get(3)));
    orders.add(new Order(5L, date, 1L, null, DBdata.users.get(0), DBdata.products.get(1), DBdata.statuses.get(0)));
    orders.add(new Order(6L, date, 1L, null, DBdata.users.get(0), DBdata.products.get(0), DBdata.statuses.get(0)));
    orders.add(new Order(7L, date, 10L, null, DBdata.users.get(0), DBdata.products.get(5), DBdata.statuses.get(0)));
    orders.add(new Order(8L, date, 5L, null, DBdata.users.get(0), DBdata.products.get(5), DBdata.statuses.get(0)));
    orders.add(new Order(9L, date, 3L, null, DBdata.users.get(0), DBdata.products.get(6), DBdata.statuses.get(1)));
  }
}
