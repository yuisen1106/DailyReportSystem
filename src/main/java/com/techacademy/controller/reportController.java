package com.techacademy.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;


import com.techacademy.entity.Report;

import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class reportController {

	private final ReportService reportService;

	@Autowired
	public reportController(ReportService reportService) {
		this.reportService = reportService;
	}

	// 日報一覧画面
	@GetMapping("/list")
	public String list(Model model, @AuthenticationPrincipal UserDetail userDetail) {

		String code = userDetail.getUsername();

		if ("1".equals(code)) {
			model.addAttribute("listSize", reportService.findAll().size());
			model.addAttribute("reportList", reportService.findAll());
		} else {
			model.addAttribute("listSize", reportService.findAll(code).size());
			model.addAttribute("reportList", reportService.findAll(code));
		}
		return "reports/list";
	}

	// 日報詳細画面
	@GetMapping("/{id}/")
	public String detail(@PathVariable("id") Integer id, Model model) {

		model.addAttribute("report", reportService.findById(id));

		return "reports/detail";
	}

	// 日報新規登録画面
	@GetMapping("/add")
	public String create(@ModelAttribute Report report, @AuthenticationPrincipal UserDetail userDetail, Model model) {

		//List<Report> reports = reportService.findAll(userDetail.getUsername());
		//String username = reports.get(0).getEmployee().getName();
          String username = userDetail.getEmployee().getName();
		model.addAttribute("username", username);

		return "reports/new";
	}

	// 日報新規登録処理
	@PostMapping(value = "/add")
	public String add(@Validated Report report, BindingResult res, @AuthenticationPrincipal UserDetail userDetail,
			Model model) {

		// 入力チェック
		if (res.hasErrors()) {
			return create(report, userDetail, model);
		}

		/*
		 * 追加日付の重複について
		 *
		 * LocalDate newReportDate = report.getReportDate(); String code =
		 * userDetail.getUsername(); List<Report> reportlist =
		 * reportService.findAll(code); for(Report exitReport : reportlist) {
		 * if(exitReport.getReportDate().equals(newReportDate)) {
		 * model.addAttribute("DateError", "既に登録されている日付です");
		 */
		ErrorKinds result = reportService.save(report, userDetail);

		if (ErrorMessage.contains(result)) {

			model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));

			return create(report, userDetail, model);
		}

		return "redirect:/reports/list";
	}

	// 日報削除処理
	@PostMapping(value = "/{id}/delete")
	public String delete(@PathVariable Integer id, Model model) {

		reportService.delete(id);

		return "redirect:/reports/list";
	}

	// 更新画面
	@GetMapping("/{id}/update")
	public String edit(@PathVariable("id") Integer id, Model model, Report report) {

		if (id != null) {
			model.addAttribute("report", reportService.findById(id));
			model.addAttribute("employeeName", reportService.findById(id).getEmployee().getName());
		} else {
			model.addAttribute("report", report);

		}
		return "reports/update";

	}

	/** 更新処理 */

	@PostMapping("/{id}/update")
	public String update(@Validated Report report, BindingResult res, @PathVariable("id") Integer id, Model model) {

		if (res.hasErrors()) {
			model.addAttribute("employeeName", reportService.findById(id).getEmployee().getName());
			return edit(null, model, report);
		}

		ErrorKinds result = reportService.update(report, id);

		if (ErrorMessage.contains(result)) {
			model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
			model.addAttribute("employeeName", reportService.findById(id).getEmployee().getName());
			return edit(null, model, report);
		}



		return "redirect:/reports/list";
	}


}
