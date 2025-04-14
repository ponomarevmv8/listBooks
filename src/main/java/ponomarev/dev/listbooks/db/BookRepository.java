package ponomarev.dev.listbooks.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("""
            select 
                case when count(b) > 0 then true else false end
            from BookEntity b where lower(b.vendorCode) = lower(:vendorCode)
            """)
    boolean existsByVendorCode(String vendorCode);

    @Query("""
            select b from BookEntity b
            where (:title is null or lower(b.title) like lower(concat('%', :title, '%')) ) and
            (:brand is null or lower(b.brand) like lower(concat('%', :brand, '%')) ) and
            (:year is null or b.year = :year)
            """)
    Page<BookEntity> findBookEntitiesByTitleAndBrandAndYear(String title, String brand, Integer year, Pageable pageable);

}
