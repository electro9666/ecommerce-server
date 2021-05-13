package org.example.repository;

import static org.example.entity.QProduct.product;

import java.util.List;

import org.example.dto.PageRequestDto;
import org.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductSupport extends QuerydslRepositorySupport {
	private final JPAQueryFactory queryFactory;

	public ProductSupport(JPAQueryFactory queryFactory) {
		super(Product.class);
		this.queryFactory = queryFactory;
	}

	public Page<Product> searchProductForSeller(Long sellerId, Pageable pageable, PageRequestDto pageRequestDto) {
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(product.store.seller.id.eq(sellerId));
		
		System.out.println("pageRequestDto?" + pageRequestDto);
		if (pageRequestDto.getProductName() != null) {
			builder.and(product.name.contains(pageRequestDto.getProductName())); // TODO like대신 contains
		}
		if (pageRequestDto.getProductStatus() != null) {
			builder.and(product.status.eq(pageRequestDto.getProductStatus()));
		}
		
		JPAQuery<Product> query = queryFactory
				.selectFrom(product)
				.where(builder);
				;
		
		long total = query.fetchCount();
		List<Product> result = getQuerydsl().applyPagination(pageable, query).fetch();
		return new PageImpl<>(result, pageable, total);
	}
	
	public Page<Product> searchProductForUser(PageRequestDto pageRequestDto) {
		System.out.println("searchProductForUser");
		PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getTake(), Direction.DESC, "updateDate");
		
		BooleanBuilder builder = new BooleanBuilder();
		
		if (pageRequestDto.getProductName() != null) {
			builder.and(product.name.contains(pageRequestDto.getProductName()));
		}
		if (pageRequestDto.getSort() != null) {
			if ("price".equals(pageRequestDto.getSort())) {
				pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getTake(), Direction.ASC, "price");
			} else if ("favorite".equals(pageRequestDto.getSort())) {
//				pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getTake(), Direction.DESC, "favorite");
			}
		}
		
		JPAQuery<Product> query = queryFactory
				.selectFrom(product)
				.where(builder);
				;
				
		long total = query.fetchCount();
		List<Product> result = getQuerydsl().applyPagination(pageRequest, query).fetch();
		return new PageImpl<>(result, pageRequest, total);
	}

}
