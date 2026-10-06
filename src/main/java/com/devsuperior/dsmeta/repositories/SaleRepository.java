package com.devsuperior.dsmeta.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query("SELECT s FROM Sale s WHERE s.date BETWEEN :minDate AND :maxDate "
			+ "AND LOWER(s.seller.name) LIKE LOWER(CONCAT('%', :sellerName, '%'))")
	Page<Sale> search(@Param("minDate") LocalDate minDate,
			@Param("maxDate") LocalDate maxDate,
			@Param("sellerName") String sellerName,
			Pageable pageable);

	@Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(s.seller.name, SUM(s.amount)) "
			+ "FROM Sale s WHERE s.date BETWEEN :minDate AND :maxDate "
			+ "GROUP BY s.seller.id, s.seller.name ORDER BY s.seller.name")
	List<SaleSummaryDTO> summarize(@Param("minDate") LocalDate minDate,
			@Param("maxDate") LocalDate maxDate);
}
