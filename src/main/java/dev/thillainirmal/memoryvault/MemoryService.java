package dev.thillainirmal.memoryvault;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class MemoryService {
 private static final Pattern EMAIL=Pattern.compile("[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}");
 private static final Pattern PHONE=Pattern.compile("(?<!\\d)(?:\\+?\\d[ -]?){9,14}(?!\\d)");
 private static final Pattern SECRET=Pattern.compile("(?i)(api[_ -]?key|token|password)\\s*[:=]\\s*\\S+");
 private final JdbcTemplate db;
 public MemoryService(JdbcTemplate db){this.db=db;}
 public Turn remember(String id,String role,String content,String mediaType,int ttlHours){
   String clean=redact(content);Instant now=Instant.now();Instant expiry=now.plus(Math.max(1,Math.min(ttlHours,720)),ChronoUnit.HOURS);
   db.update("INSERT INTO memory_turns(conversation_id,role,content,media_type,created_at,expires_at) VALUES(?,?,?,?,?,?)",id,role,clean,mediaType,Timestamp.from(now),Timestamp.from(expiry));
   return new Turn(role,clean,mediaType,now,expiry);
 }
 public List<Turn> recall(String id){
   db.update("DELETE FROM memory_turns WHERE expires_at < CURRENT_TIMESTAMP");
   return db.query("SELECT role,content,media_type,created_at,expires_at FROM memory_turns WHERE conversation_id=? ORDER BY created_at", (rs,n)->new Turn(rs.getString(1),rs.getString(2),rs.getString(3),rs.getTimestamp(4).toInstant(),rs.getTimestamp(5).toInstant()),id);
 }
 public Context context(String id){
   List<Turn> turns=recall(id);List<Turn> recent=turns.stream().skip(Math.max(0,turns.size()-6)).toList();
   String summary=turns.size()<=6?"No compression needed":turns.subList(0,turns.size()-6).stream().map(t->t.role()+": "+t.content()).reduce((a,b)->a+" | "+b).orElse("");
   return new Context(id,turns.size(),summary,recent,turns.stream().anyMatch(t->t.mediaType()!=null));
 }
 public int forget(String id){return db.update("DELETE FROM memory_turns WHERE conversation_id=?",id);}
 public String redact(String value){if(value==null)return "";return SECRET.matcher(PHONE.matcher(EMAIL.matcher(value).replaceAll("[EMAIL_REDACTED]")).replaceAll("[PHONE_REDACTED]")).replaceAll("$1=[SECRET_REDACTED]");}
 public record Turn(String role,String content,String mediaType,Instant createdAt,Instant expiresAt){}
 public record Context(String conversationId,int totalTurns,String compressedSummary,List<Turn> recentTurns,boolean multimodal){}
}
