package fi.metropolia.omagr.grimaux.service;
import fi.metropolia.omagr.grimaux.entity.*;
import fi.metropolia.omagr.grimaux.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import java.util.*;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {
 CustomerRepository customers=mock(CustomerRepository.class);
 CustomerAddressRepository addresses=mock(CustomerAddressRepository.class);
 ProductRepository products=mock(ProductRepository.class);
 ShopOrderRepository orders=mock(ShopOrderRepository.class);
 OrderItemRepository items=mock(OrderItemRepository.class);
 OrderService service=new OrderService(customers,addresses,products,orders,items);
 Customer customer;CustomerAddress address;Product product;
 @BeforeEach void init(){
  customer=new Customer();customer.id=1;
  address=new CustomerAddress();address.id=2;address.customer=customer;
  product=new Product();product.id=3;product.stockQuantity=10;product.price=new BigDecimal("12.50");
  when(customers.findById(1)).thenReturn(Optional.of(customer));
  when(addresses.findById(2)).thenReturn(Optional.of(address));
  when(products.findById(3)).thenReturn(Optional.of(product));
  when(orders.saveAndFlush(any())).thenAnswer(inv->{ShopOrder o=inv.getArgument(0);o.id=4;return o;});
  when(items.findByOrder_Id(4)).thenReturn(List.of());
 }
 @Test void orderDeductsStock(){
  var result=service.place(new OrderService.PlaceOrder(1,2,List.of(new OrderService.Line(3,4))));
  assertEquals(6,product.stockQuantity);assertEquals(4,result.id());verify(items).save(any());
 }
 @Test void rejectsInsufficientStock(){
  assertThrows(org.springframework.web.server.ResponseStatusException.class,()->service.place(new OrderService.PlaceOrder(1,2,List.of(new OrderService.Line(3,11)))));
  assertEquals(10,product.stockQuantity);verifyNoInteractions(orders);
 }
 @Test void rejectsForeignAddress(){
  address.customer=new Customer();address.customer.id=999;
  assertThrows(org.springframework.web.server.ResponseStatusException.class,()->service.place(new OrderService.PlaceOrder(1,2,List.of(new OrderService.Line(3,1)))));
 }
 @Test void rejectsNonPositiveQuantity(){
  assertThrows(org.springframework.web.server.ResponseStatusException.class,()->service.place(new OrderService.PlaceOrder(1,2,List.of(new OrderService.Line(3,0)))));
 }
}
