package fi.metropolia.omagr.grimaux.controller;
import fi.metropolia.omagr.grimaux.entity.Product;
import fi.metropolia.omagr.grimaux.repository.ProductRepository;
import fi.metropolia.omagr.grimaux.service.OrderService;
import fi.metropolia.omagr.grimaux.dto.ProductView;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController @RequestMapping("/api")
public class ApiController {
 private final ProductRepository products;private final OrderService service;
 public ApiController(ProductRepository products,OrderService service){this.products=products;this.service=service;}
 @GetMapping("/products")
 public List<ProductView> products(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size){
  return products.findAll(PageRequest.of(Math.max(0,page),Math.max(1,Math.min(size,100)))).stream().map(p->new ProductView(p.id,p.name,p.price,p.stockQuantity)).toList();
 }
 @GetMapping("/products/{id}")
 public ProductView product(@PathVariable Integer id){
  Product p=products.findById(id).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
  return new ProductView(p.id,p.name,p.price,p.stockQuantity);
 }
 @PostMapping("/orders") public OrderService.OrderView place(@RequestBody OrderService.PlaceOrder body){return service.place(body);}
 @GetMapping("/orders/{id}") public OrderService.OrderView order(@PathVariable Integer id){return service.get(id);}
 @PostMapping("/orders/{id}/ship") public OrderService.OrderView ship(@PathVariable Integer id){return service.ship(id);}
 @PostMapping("/orders/{id}/cancel") public OrderService.OrderView cancel(@PathVariable Integer id){return service.cancel(id);}
}
