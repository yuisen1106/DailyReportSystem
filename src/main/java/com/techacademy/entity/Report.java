package com.techacademy.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name="reports")
public class Report {

	/**主キー、　自動生成*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**日付*/
	@NotNull
	@Column(nullable = false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate reportDate;

	/**タイトル*/
	@Column(length=100,nullable = false)
	private String title;

	/**内容*/
	@Column(columnDefinition = "LONGTEXT",nullable = false)
	private String content;

	/**社員番号*/
	 @OneToOne
	 @JoinColumn(name="employee_code",referencedColumnName="code")
	 private Employee employee;

	 /**削除フラグ*/
    @Column(columnDefinition="TINYINT", nullable = false)
    private boolean deleteFlg;

     /** 登録日時*/
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**更新日時*/
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}

