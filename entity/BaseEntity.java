package kr.co.oraclejava.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class}) //auditing 감시기능 적용
@MappedSuperclass //부모클래스를 상속받은 자식 클래스에 매핑정보만 제공
@Getter
@Setter									// 등록일, 수정일 상속
public abstract class BaseEntity extends BaseTimeEntity{

	@CreatedBy // 등록자
	@Column(updatable = false)
	private String createBy;
	
	@LastModifiedBy // 수정자
	private String modifiedBy;
}
