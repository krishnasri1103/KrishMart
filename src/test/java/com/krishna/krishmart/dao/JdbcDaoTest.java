package com.krishna.krishmart.dao;

import com.krishna.krishmart.model.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.*;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Integration tests for the JDBC DAO layer against an embedded H2 database. */
class JdbcDaoTest {
    private static HikariDataSource dataSource;
    @BeforeAll static void setUp() throws Exception {
        HikariConfig config=new HikariConfig();config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");config.setUsername("sa");config.setPassword("");config.setDriverClassName("org.h2.Driver");dataSource=new HikariDataSource(config);
        try(Connection c=dataSource.getConnection();var schema=JdbcDaoTest.class.getClassLoader().getResourceAsStream("schema.sql");var seed=JdbcDaoTest.class.getClassLoader().getResourceAsStream("seed.sql")){RunScript.execute(c,new InputStreamReader(schema,StandardCharsets.UTF_8));RunScript.execute(c,new InputStreamReader(seed,StandardCharsets.UTF_8));}
    }
    @AfterAll static void tearDown(){dataSource.close();}
    @Test void userAndProductDaosPersistAndSearch() throws Exception {
        JdbcUserDao users=new JdbcUserDao(dataSource);User seller=new User();seller.setName("Test Seller");seller.setEmail("seller@test.local");seller.setPasswordHash("bcrypt");seller.setRole(Role.SELLER);seller.setId(users.create(seller));
        assertEquals("Test Seller",users.findByEmail("SELLER@TEST.LOCAL").orElseThrow().getName());
        Product product=new Product();product.setSellerId(seller.getId());product.setName("Blue Notebook");product.setDescription("Paper");product.setPrice(new BigDecimal("125.50"));product.setStockQty(8);product.setCategory("Stationery");product.setImageUrl(null);
        JdbcProductDao products=new JdbcProductDao(dataSource);long id=products.create(product);
        assertEquals(id,products.search("notebook","Stationery").get(0).getId());
    }
    @Test void checkoutDaoCreatesOrderAndDecrementsStock() throws Exception {
        JdbcUserDao users=new JdbcUserDao(dataSource);User buyer=new User();buyer.setName("Test Buyer");buyer.setEmail("buyer@test.local");buyer.setPasswordHash("bcrypt");buyer.setRole(Role.BUYER);buyer.setId(users.create(buyer));
        User seller=new User();seller.setName("Checkout Seller");seller.setEmail("checkout-seller@test.local");seller.setPasswordHash("bcrypt");seller.setRole(Role.SELLER);seller.setId(users.create(seller));
        Product productToCreate=new Product();productToCreate.setSellerId(seller.getId());productToCreate.setName("Checkout Product");productToCreate.setDescription("Product");productToCreate.setPrice(new BigDecimal("125.50"));productToCreate.setStockQty(8);productToCreate.setCategory("Test");
        Product product=new JdbcProductDao(dataSource).findById(new JdbcProductDao(dataSource).create(productToCreate)).orElseThrow();
        JdbcCartDao carts=new JdbcCartDao(dataSource);carts.add(buyer.getId(),product.getId(),2);List<com.krishna.krishmart.model.CartItem> items=carts.findByUser(buyer.getId());
        long orderId=new JdbcOrderDao(dataSource).createFromCart(buyer.getId(),items,new BigDecimal("251.00"));
        assertTrue(orderId>0);assertEquals(6,new JdbcProductDao(dataSource).findById(product.getId()).orElseThrow().getStockQty());assertTrue(carts.findByUser(buyer.getId()).isEmpty());
    }
}