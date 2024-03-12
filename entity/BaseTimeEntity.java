package kr.co.oraclejava.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class}) //auditing 감시기능 적용
@MappedSuperclass //부모클래스를 상속받은 자식 클래스에 매핑정보만 제공
@Getter
@Setter
public abstract class BaseTimeEntity {

	@CreatedDate //엔티티가 생성되어 저장되는 날짜 자동 저장
	@Column(updatable = false) // 수정 불가
	private LocalDateTime regTime;
	
	@LastModifiedDate //엔티티가 변경 저장 될 때 시간
	private LocalDateTime upDateTime;
}
