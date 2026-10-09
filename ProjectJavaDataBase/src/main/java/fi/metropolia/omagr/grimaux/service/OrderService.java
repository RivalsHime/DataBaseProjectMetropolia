package fi.metropolia.omagr.grimaux.service;
import fi.metropolia.omagr.grimaux.entity.*;
import fi.metropolia.omagr.grimaux.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {
 private final CustomerRepository customers;
 private final CustomerAddressRepository addresses;
 private final ProductRepository products;
 private final ShopOrderRepository orders;
 private final OrderItemRepository items;
 public OrderService(CustomerRepository customers,CustomerAddressRepository addresses,ProductRepository products,ShopOrderRepository orders,OrderItemRepository items){
  this.customers=customers;this.addresses=addresses;this.products=products;this.orders=orders;this.items=items;
 }
 public record Line(Integer productId,int quantity){}
 public record PlaceOrder(Integer customerId,Integer shippingAddressId,List<Line> lines){}
 public record OrderLine(Integer productId,int quantity,BigDecimal unitPrice){}
 public record OrderView(Integer id,Integer customerId,String status,List<OrderLine> lines){}
 private ResponseStatusException bad(String message){return new ResponseStatusException(HttpStatus.BAD_REQUEST,message);}
 @Transactional
 public OrderView place(PlaceOrder request){
  if(request==null||request.customerId()==null||request.shippingAddressId()==null||request.lines()==null||request.lines().isEmpty()) throw bad("Customer, address and items required");
  Customer customer=customers.findById(request.customerId()).orElseThrow(()->bad("Customer not found"));
  CustomerAddress address=addresses.findById(request.shippingAddressId()).orElseThrow(()->bad("Address not found"));
  if(!Objects.equals(address.customer.id,customer.id))throw bad("Address belongs to another customer");
  TreeMap<Integer,Integer> requested=new TreeMap<>();
  for(Line line:request.lines()){
   if(line==null||line.productId()==null||line.quantity()<=0)throw bad("Invalid product or quantity");
   requested.merge(line.productId(),line.quantity(),(a,b)->Math.addExact(a,b));
  }
  Map<Integer,Product> locked=new LinkedHashMap<>();
  for(var entry:requested.entrySet()){
   Product p=products.findById(entry.getKey()).orElseThrow(()->bad("Product not found"));
   if(p.stockQuantity<entry.getValue())throw new ResponseStatusException(HttpStatus.CONFLICT,"Insufficient stock");
   locked.put(entry.getKey(),p);
  }
  ShopOrder order=new ShopOrder();order.customer=customer;order.shippingAddress=address;order.orderDate=LocalDateTime.now();order.status="NEW";order=orders.saveAndFlush(order);
  for(var entry:requested.entrySet()){
   Product p=locked.get(entry.getKey());p.stockQuantity-=entry.getValue();
   OrderItem item=new OrderItem();item.id=new OrderItemId(order.id,p.id);item.order=order;item.product=p;item.quantity=entry.getValue();item.unitPrice=p.price;items.save(item);
  }
  return view(order);
 }
 @Transactional(readOnly=true)
 public OrderView get(Integer id){return view(orders.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found")));}
 private OrderView view(ShopOrder order){
  List<OrderLine> lines=items.findByOrder_Id(order.id).stream().map(i->new OrderLine(i.product.id,i.quantity,i.unitPrice)).toList();
  return new OrderView(order.id,order.customer.id,order.status,lines);
 }
 @Transactional
 public OrderView ship(Integer id){
  ShopOrder o=orders.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
  if(!"NEW".equals(o.status)&&!"LATE".equals(o.status))throw new ResponseStatusException(HttpStatus.CONFLICT,"Cannot ship order in this state");
  o.status="SHIPPED";return view(o);
 }
 @Transactional
 public OrderView cancel(Integer id){
  ShopOrder o=orders.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
  if(!"NEW".equals(o.status)&&!"LATE".equals(o.status))throw new ResponseStatusException(HttpStatus.CONFLICT,"Cannot cancel order in this state");
  List<OrderItem> orderItems=items.findByOrder_Id(o.id);
  orderItems.stream().map(i->i.product.id).distinct().sorted().forEach(pid->products.findById(pid).orElseThrow());
  for(OrderItem i:orderItems){Product p=products.findById(i.product.id).orElseThrow();p.stockQuantity+=i.quantity;}
  o.status="CANCELLED";return view(o);
 }
}
