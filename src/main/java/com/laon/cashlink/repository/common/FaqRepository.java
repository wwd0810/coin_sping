package com.laon.cashlink.repository.common;

        import com.laon.cashlink.entity.common.Faq;

        import java.util.List;
        import java.util.Map;

        import lombok.RequiredArgsConstructor;
        import org.mybatis.spring.SqlSessionTemplate;
        import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FaqRepository {

    private final SqlSessionTemplate sql;

    public List<Faq> readFaqList(Map<String, Object> payload) {
        return sql.selectList("FaqRepository.readFaqList", payload);
    }

    public Faq readFaq(Map<String, Object> payload) {
        return sql.selectOne("FaqRepository.readFaq", payload);
    }

    public void createFaq(Map<String, Object> payload) {
        sql.insert("FaqRepository.createFaq", payload);
    }

    public void updateFaq(Map<String, Object> payload) {
        sql.update(("FaqRepository.updateFaq"), payload);
    }

    public void deleteFaq(Map<String, Object> payload) {
        sql.delete(("FaqRepository.deleteFaq"), payload);
    }

}
