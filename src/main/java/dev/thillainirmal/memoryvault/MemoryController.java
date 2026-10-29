package dev.thillainirmal.memoryvault;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/memory")
public class MemoryController{
 private final MemoryService service;public MemoryController(MemoryService s){service=s;}
 @PostMapping("/{id}") public MemoryService.Turn add(@PathVariable String id,@Valid @RequestBody AddTurn r){return service.remember(id,r.role(),r.content(),r.mediaType(),r.ttlHours());}
 @GetMapping("/{id}") public MemoryService.Context get(@PathVariable String id){return service.context(id);}
 @DeleteMapping("/{id}") public Map<String,Integer> delete(@PathVariable String id){return Map.of("deleted",service.forget(id));}
 @GetMapping("/demo") public MemoryService.Context demo(){String id="portfolio-demo";service.forget(id);service.remember(id,"user","My email is demo@example.com and I prefer concise Java examples.",null,24);service.remember(id,"assistant","Preference saved without retaining direct contact information.","text/plain",24);service.remember(id,"user","This architecture image shows three services.","image/png",24);return service.context(id);}
 public record AddTurn(@NotBlank String role,@NotBlank String content,String mediaType,int ttlHours){}
}
