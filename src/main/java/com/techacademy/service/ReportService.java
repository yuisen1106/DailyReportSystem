package com.techacademy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.EmployeeRepository;
import com.techacademy.repository.ReportRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

	private final ReportRepository reportRepository;

	@Autowired
	public ReportService(ReportRepository reportRepository, PasswordEncoder passwordEncoder) {
		this.reportRepository = reportRepository;
	}

	// 日報保存
	@Transactional
	public ErrorKinds save(Report report, @AuthenticationPrincipal UserDetail userDetail) {

		// 日付の重複について
		LocalDate newReportDate = report.getReportDate();
		String code = userDetail.getUsername();
		List<Report> reportlist = findAll(code);

		if(reportlist != null) {
		for (Report exitReport : reportlist) {
			if (exitReport.getReportDate().equals(newReportDate)) {
				return ErrorKinds.DATECHECK_ERROR;
			}
		}
		}
		report.setDeleteFlg(false);

		LocalDateTime now = LocalDateTime.now();
		report.setCreatedAt(now);
		report.setUpdatedAt(now);



		report.setEmployee(userDetail.getEmployee());
		reportRepository.save(report);
		return ErrorKinds.SUCCESS;

	}

	// 日報削除
	@Transactional
	public void delete(Integer id) {

		Report report = findById(id);
		LocalDateTime now = LocalDateTime.now();
		report.setUpdatedAt(now);
		report.setDeleteFlg(true);

	}

	// 日報一覧表示処理
	public List<Report> findAll() {

		return reportRepository.findAll();

	}



	// 従業員一覧表示処理
	public List<Report> findAll(String code) {

		return reportRepository.findByEmployeeCode(code);
	}

	// 1件を検索
	public Report findById(Integer id) {
		// findByIdで検索
		Optional<Report> option = reportRepository.findById(id);
		// 取得できなかった場合はnullを返す
		Report report = option.orElse(null);
		return report;
	}


	// 日報 更新

	@Transactional
	public ErrorKinds update(Report report, Integer id) {

		/**/Report registeredreport = findById(id);

		// 日付の重複について
		LocalDate newReportDate = report.getReportDate();
		String code = registeredreport.getEmployee().getCode();
		List<Report> reportlist = findAll(code);
		for (Report exitReport : reportlist) {
			if (exitReport.getReportDate().equals(newReportDate)) {
				return ErrorKinds.DATECHECK_ERROR;

			}
		}

		report.setDeleteFlg(false);

		LocalDateTime now = LocalDateTime.now();
		report.setCreatedAt(now);
		report.setUpdatedAt(now);

		//
		report.setEmployee(registeredreport.getEmployee());

		reportRepository.save(report);
		return ErrorKinds.SUCCESS;
	}
}

