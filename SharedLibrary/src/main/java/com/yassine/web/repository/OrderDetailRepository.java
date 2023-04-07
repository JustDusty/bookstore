package com.yassine.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yassine.web.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
