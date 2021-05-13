package org.example.service;

import java.util.List;

import org.example.dto.PageRequestDto;
import org.example.dto.PageResultDto;
import org.example.dto.ProductDto;
import org.example.entity.Category;
import org.example.entity.Product;
import org.example.repository.CategoryRepository;
import org.example.repository.OptionRepository;
import org.example.repository.ProductRepository;
import org.example.repository.ProductSupport;
import org.example.repository.SellerRepository;
import org.example.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {

	private final StoreRepository storeRepository;
	private final SellerRepository sellerRepository;
	private final OptionRepository optionRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProductSupport productSupport;

	@Transactional(readOnly = true)
	public List<Category> category() {
		return categoryRepository.findAll();
	}
}
