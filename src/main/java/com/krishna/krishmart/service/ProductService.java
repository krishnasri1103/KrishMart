package com.krishna.krishmart.service;

import com.krishna.krishmart.dao.ProductDao;
import com.krishna.krishmart.dto.ProductRequest;
import com.krishna.krishmart.exception.*;
import com.krishna.krishmart.model.Product;
import com.krishna.krishmart.util.ValidationUtil;
import java.util.List;

/** Validates and orchestrates product listing operations. */
public class ProductService {
    private final ProductDao productDao;
    /** Creates a product service. */
    public ProductService(ProductDao productDao){this.productDao=productDao;}
    /** Searches the public catalog. */
    public List<Product> search(String keyword,String category)throws AppException{return productDao.search(keyword,category==null||category.isBlank()?null:category);}
    /** Loads one product or raises a safe not-found error. */
    public Product get(long id)throws AppException{return productDao.findById(id).orElseThrow(()->new NotFoundException("Product not found."));}
    /** Lists products owned by a seller. */
    public List<Product> sellerProducts(long sellerId)throws AppException{return productDao.findBySeller(sellerId);}
    /** Lists catalog categories. */
    public List<String> categories()throws AppException{return productDao.findCategories();}
    /** Creates a validated listing for a seller. */
    public long create(long sellerId,ProductRequest request)throws AppException{Product product=toProduct(sellerId,request);return productDao.create(product);}
    /** Updates a seller-owned listing after checking ownership. */
    public void update(long sellerId,long productId,ProductRequest request)throws AppException{Product existing=get(productId);if(existing.getSellerId()!=sellerId)throw new ValidationException("You can only edit your own listings.");Product product=toProduct(sellerId,request);product.setId(productId);productDao.update(product);}
    /** Deletes a seller-owned listing after checking ownership. */
    public void delete(long sellerId,long productId)throws AppException{Product existing=get(productId);if(existing.getSellerId()!=sellerId)throw new ValidationException("You can only remove your own listings.");productDao.delete(productId);}
    /** Removes any listing for an administrator. */
    public void adminDelete(long productId)throws AppException{productDao.delete(productId);}
    private Product toProduct(long sellerId,ProductRequest r)throws ValidationException{
        Product p=new Product();p.setSellerId(sellerId);p.setName(ValidationUtil.required(r.getName(),"Product name",180));p.setDescription(ValidationUtil.required(r.getDescription(),"Description",2000));p.setPrice(ValidationUtil.money(r.getPrice()));
        if(r.getStockQty()<0)throw new ValidationException("Stock cannot be negative.");p.setStockQty(r.getStockQty());p.setCategory(ValidationUtil.required(r.getCategory(),"Category",80));
        String image=r.getImageUrl();if(image!=null&&image.length()>1000)throw new ValidationException("Image URL is too long.");if(image!=null&&!image.isBlank()&&!image.trim().matches("https?://.+"))throw new ValidationException("Image URL must use http or https.");p.setImageUrl(image==null?null:image.trim());return p;
    }
}