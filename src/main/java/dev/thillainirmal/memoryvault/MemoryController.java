package dev.thillainirmal.memoryvault;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/memory")
public class MemoryController{
 private final MemoryService service;public MemoryController(MemoryService s){service=s;}
 @PostMapping("/{id}") public MemoryService.Turn add(@PathVariable @Pattern(regexp="[A-Za-z0-9_-]{1,80}") String id,@Valid @RequestBody AddTurn r){return service.remember(id,r.role(),r.content(),r.mediaType(),r.ttlHours());}
 @GetMapping("/{id}") public MemoryService.Context get(@PathVariable @Pattern(regexp="[A-Za-z0-9_-]{1,80}") String id){return service.context(id);}
 @DeleteMapping("/{id}") public Map<String,Integer> delete(@PathVariable @Pattern(regexp="[A-Za-z0-9_-]{1,80}") String id){return Map.of("deleted",service.forget(id));}
 public record AddTurn(@NotBlank @Pattern(regexp="user|assistant|system") String role,@NotBlank @Size(max=4000) String content,@Size(max=80) String mediaType,@Min(1) @Max(720) int ttlHours){}
}
