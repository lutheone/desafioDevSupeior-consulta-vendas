package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleMinDTO> search(String minDate, String maxDate, String sellerName, int page, int size) {
		DateRange dateRange = resolveDateRange(minDate, maxDate);
		String nameFilter = sellerName == null ? "" : sellerName;
		Pageable pageable = PageRequest.of(page, size);
		return repository.search(dateRange.min(), dateRange.max(), nameFilter, pageable).map(SaleMinDTO::new);
	}

	public List<SaleSummaryDTO> summarize(String minDate, String maxDate) {
		DateRange dateRange = resolveDateRange(minDate, maxDate);
		return repository.summarize(dateRange.min(), dateRange.max());
	}

	private DateRange resolveDateRange(String minDate, String maxDate) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate max = maxDate == null || maxDate.isBlank() ? today : LocalDate.parse(maxDate);
		LocalDate min = minDate == null || minDate.isBlank() ? max.minusYears(1L) : LocalDate.parse(minDate);
		return new DateRange(min, max);
	}

	private record DateRange(LocalDate min, LocalDate max) {
	}
}
